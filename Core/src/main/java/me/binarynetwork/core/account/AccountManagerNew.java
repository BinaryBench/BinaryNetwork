package me.binarynetwork.core.account;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.database.DataSourceManager;
import me.binarynetwork.core.database.SQLDataCacheWithTemp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/13/2016.
 */
public class AccountManagerNew extends SQLDataCacheWithTemp<UUID, Account> implements Listener {

    private int tempAccountId = -1;

    public static String TABLE_NAME = "player_account";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INT unsigned NOT NULL AUTO_INCREMENT,uuid varchar(36) NOT NULL,PRIMARY KEY (id),UNIQUE (uuid),UNIQUE (id)) ENGINE=InnoDB;";
    private static final String NEW_LOGIN = "INSERT IGNORE INTO " + TABLE_NAME + " (uuid) VALUES(?);";
    private static final String SELECT_ACCOUNT = "SELECT id FROM " + TABLE_NAME + " where uuid=?;";

    private ConcurrentHashMap<AccountListener, Boolean> listeners = new ConcurrentHashMap<>();

    public AccountManagerNew(ScheduledExecutorService scheduler)
    {
        super(scheduler, DataSourceManager.PLAYER_DATA);
        BinaryNetworkPlugin.registerEvents(this);
    }

    public void  addListener(AccountListener listener, boolean listenToJoin)
    {
        listeners.put(listener, listenToJoin);
    }

    public void removeListener(AccountListener listener)
    {
        listeners.remove(listener);
    }

    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent event)
    {
        Account account = getSync(event.getUniqueId());

        StringBuilder sb = new StringBuilder();

        List<AccountListener> returnList = new ArrayList<>();

        for (Map.Entry<AccountListener, Boolean> entry: listeners.entrySet())
        {
            if (entry.getValue())
            {
                sb.append(entry.getKey().getQuery(account));
                returnList.add(entry.getKey());
                //System.err.println("Added " + entry.getKey().getClass().getSimpleName() + " at position " + (returnList.size() - 1));
            }
        }

        if (sb.length() == 0)
            return;

        try(Connection connection = getDataSource().getConnection();
            Statement statement = connection.createStatement())
        {
            boolean hasMoreResultSets = statement.execute(sb.toString());

            int counter = 0;
            while ( hasMoreResultSets || statement.getUpdateCount() != -1 ) {
                if ( hasMoreResultSets ) {
                    ResultSet rs = statement.getResultSet();

                    // handle your rs here
                    //System.err.println("Returning to " + returnList.get(counter).getClass().getSimpleName() + " ResultSet at position " + (counter));
                    returnList.get(counter++).handleResultSet(account, connection, rs);

                } // if has rs
                else { // if ddl/dml/...
                    counter++;
                    int queryResult = statement.getUpdateCount();
                    if ( queryResult == -1 ) { // no more queries processed
                        break;
                    } // no more queries processed
                    // handle success, failure, generated keys, etc here
                } // if ddl/dml/...

                // check to continue in the loop
                hasMoreResultSets = statement.getMoreResults();
            } // while results
        }
        catch (SQLException e)
        {
            System.err.println("Welp, things didn't load about this player");
            e.printStackTrace();
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        get(uuid, account -> {

            removeFromCache(uuid);

            if (account == null)
                return;

            for (AccountListener accountListener : listeners.keySet())
            {
                accountListener.accountRemoved(account);
            }
        });
    }



    @Override
    public Account loadValue(UUID key, Connection connection) throws SQLException
    {
        Account returnAccount = loadAccount(key, connection);
        if (returnAccount != null)
            return returnAccount;
        try (PreparedStatement insertStatement = connection.prepareStatement(NEW_LOGIN))
        {
            insertStatement.setString(1, key.toString());
            insertStatement.executeUpdate();
        }
        return loadAccount(key, connection);
    }

    public Account loadAccount(UUID key, Connection connection) throws SQLException
    {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT))
        {
            statement.setString(1, key.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                Account account = new Account(resultSet.getInt("id"), key);
                System.err.println("Loaded account: " + account.toString());
                return account;
            }
        }
        return null;
    }

    @Override
    public Account loadTemp(UUID key)
    {
        Player player = Bukkit.getPlayer(key);
        if (player != null)
            player.sendMessage("Hey, things didn't load right!  " +
                    "You are currently in 'temp account' mode, which means changes to your account won't be saved.\n " +
                    "Please contact BinaryBench(or whoever is running the server) about this!");
        return new Account(--tempAccountId, key);
    }

    @Override
    public void unloadedTemp(UUID uuid, Account account)
    {
        if (account == null)
            return;

        for (AccountListener accountListener : listeners.keySet())
            accountListener.accountRemoved(account);
    }

    @Override
    protected CompletableFuture<Account> getFuture(UUID key)
    {
        if (Bukkit.getPlayer(key) == null)
            markAsTemp(key);
        return super.getFuture(key);
    }
}
