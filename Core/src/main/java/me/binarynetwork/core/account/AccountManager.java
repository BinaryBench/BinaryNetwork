package me.binarynetwork.core.account;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.database.DataSourceManager;
import me.binarynetwork.core.database.DataStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


/**
 * Created by Bench on 9/3/2016.
 */
public class AccountManager extends DataStorage<UUID, Account> implements Listener {

    public static int RETRY_TIMES = 10;

    private int tempAccountId = -1;

    public static String TABLE_NAME = "player_account";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INT unsigned NOT NULL AUTO_INCREMENT,uuid varchar(36) NOT NULL,PRIMARY KEY (id),UNIQUE (uuid),UNIQUE (id)) ENGINE=InnoDB;";
    private static final String NEW_LOGIN = "INSERT IGNORE INTO " + TABLE_NAME + " (uuid) VALUES(?);";
    private static final String SELECT_ACCOUNT = "SELECT id FROM " + TABLE_NAME + " where uuid=?;";

    private ConcurrentHashMap<PlayerDataStorage<?>, Boolean> listeners = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler;

    public AccountManager(ScheduledExecutorService scheduler)
    {
        super(DataSourceManager.PLAYER_DATA, null, 0);
        BinaryNetworkPlugin.registerEvents(this);
        this.scheduler = scheduler;
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

            for (PlayerDataStorage<?> playerDataStorage : listeners.keySet())
            {
                playerDataStorage.save(account, new Consumer<Boolean>() {
                    private int counter = 0;

                    @Override
                    public void accept(Boolean aBoolean)
                    {
                        if (aBoolean)
                            //FIXME maybe do a better way to make sure this doesn't happen.
                            if (getIfExists(uuid) == null)
                                playerDataStorage.removeFromCache(account);
                        else
                            if (counter++ < RETRY_TIMES)
                            {
                                System.err.println("Unable to save: " + account);
                                scheduler.scheduleWithFixedDelay(() -> playerDataStorage.save(account, this), 5, 5, TimeUnit.SECONDS);
                            }
                    }
                });
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

    public void  addPlayerStorage(PlayerDataStorage<?> listener, boolean listenToJoin)
    {
        listeners.put(listener, listenToJoin);
    }

    public void removeListener(PlayerDataStorage<?> listener)
    {
        listeners.remove(listener);
    }

    public void onLogin(UUID playersUUID)
    {
        Scheduler.runAsync(() -> get(playersUUID, account ->
                Scheduler.runAsync(() ->
                {
                    StringBuilder sb = new StringBuilder();

                    List<PlayerDataStorage<?>> returnList = new ArrayList<>();

                    listeners.entrySet().stream().filter(entry -> entry.getValue() && entry.getKey().enableWaitingList(account)).forEach(entry -> {
                        sb.append(entry.getKey().getQuery(account));
                        returnList.add(entry.getKey());
                        System.err.println("Added " + entry.getKey().getClass().getSimpleName() + " at position " + (returnList.size() - 1));
                    });
                    if (sb.length() == 0)
                        return;
                    try (Connection connection = getDataSource().getConnection(); Statement statement = connection.createStatement())
                    {
                        boolean hasMoreResultSets = statement.execute(sb.toString());

                        int counter = 0;
                        while ( hasMoreResultSets || statement.getUpdateCount() != -1 ) {
                            if ( hasMoreResultSets ) {
                                ResultSet rs = statement.getResultSet();

                                // handle your rs here
                                PlayerDataStorage<?> storage = returnList.get(counter++);
                                System.err.println("Returning to " + storage.getClass().getSimpleName() + " ResultSet at position " + (counter-1));
                                storage.loadAndAddToCache(account, rs, connection);

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

                })));
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
    public void saveData(Map<UUID, Account> key, Consumer<Integer> successes)
    {

    }
}
