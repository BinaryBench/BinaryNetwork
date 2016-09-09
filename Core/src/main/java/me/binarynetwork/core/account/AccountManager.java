package me.binarynetwork.core.account;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.database.DataSourceManager;
import me.binarynetwork.core.database.KeyValueDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


/**
 * Created by Bench on 9/3/2016.
 */
public class AccountManager extends KeyValueDataStorage<UUID, Account> implements Listener {

    public static int RETRY_TIMES = 10;

    private int tempAccountId = -1;

    public static String TABLE_NAME = "player_account";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INT unsigned NOT NULL AUTO_INCREMENT,uuid varchar(36) NOT NULL,PRIMARY KEY (id),UNIQUE (uuid),UNIQUE (id)) ENGINE=InnoDB;";
    private static final String NEW_LOGIN = "INSERT IGNORE INTO " + TABLE_NAME + " (uuid) VALUES(?);";
    private static final String SELECT_ACCOUNT = "SELECT id FROM " + TABLE_NAME + " where uuid=?;";

    private ConcurrentHashMap<AccountListener, Boolean> listeners = new ConcurrentHashMap<>();

    public AccountManager(ScheduledExecutorService scheduler)
    {
        super(DataSourceManager.PLAYER_DATA, scheduler);
        BinaryNetworkPlugin.registerEvents(this);
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(PlayerLoginEvent event)
    {
        System.err.println("Login event for: " + event.getPlayer().getName());
        onLogin(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();

        getIfExists(uuid, account -> {

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
    public boolean removeFromCache(UUID key)
    {
        System.err.println("Removing from cache:" + Bukkit.getOfflinePlayer(key).getName());
        return super.removeFromCache(key);
    }

    @Override
    public void get(UUID key, Consumer<Account> callback)
    {
        System.err.println("Getting: " + Bukkit.getOfflinePlayer(key).getName());
        super.get(key, callback);
    }

    public void  addPlayerStorage(AccountListener listener, boolean listenToJoin)
    {
        listeners.put(listener, listenToJoin);
    }

    public void removeListener(AccountListener listener)
    {
        listeners.remove(listener);
    }

    public void onLogin(UUID playersUUID)
    {
        get(playersUUID, account -> {

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

            execute(connection ->
            {
                try (Statement statement = connection.createStatement())
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
                    e.printStackTrace();
                }

            });

        });
    }


    @Override
    protected Account loadValue(Connection connection, UUID key) throws SQLException
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
            return null;
        }
    }

    @Override
    public Account getNew(Connection connection, UUID key) throws SQLException
    {
        try (PreparedStatement insertStatement = connection.prepareStatement(NEW_LOGIN))
        {
            insertStatement.setString(1, key.toString());
            insertStatement.executeUpdate();
        }
        return loadValue(connection, key);
    }

    @Override
    protected Account getNew(UUID key)
    {
        return new Account(--tempAccountId, key);
    }

    @Override
    public void saveData(Connection connection, Map<UUID, Account> key, Consumer<Integer> successes)
    {

    }

    @Override
    public void initialize(Connection connection) throws SQLException
    {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE))
        {
            //To do log stuff!
            preparedStatement.executeUpdate();
        }
    }
}
