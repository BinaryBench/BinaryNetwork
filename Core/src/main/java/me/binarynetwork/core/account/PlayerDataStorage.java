package me.binarynetwork.core.account;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.database.KeyValueDataStorage;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/4/2016.
 */
public abstract class PlayerDataStorage<V> extends KeyValueDataStorage<Account, V> implements AccountListener {

    public static final int RETRY_TIMES = 10;

    private AccountManager accountManager;

    public PlayerDataStorage(DataSource dataSource, ScheduledExecutorService scheduler, AccountManager accountManager, boolean loadOnJoin)
    {
        super(dataSource, scheduler);
        this.accountManager = accountManager;
        getAccountManager().addPlayerStorage(this, loadOnJoin);
    }

    public V getIfExists(Player player)
    {
        Account account = getAccountManager().getIfExists(player.getUniqueId());
        if (account == null)
            return null;
        return getIfExists(account);
    }

    public void get(Player player, Consumer<V> callback)
    {
        getAccountManager().get(player.getUniqueId(), account -> {
            get(account, callback);
        });
    }

    @Override
    public final String getQuery(Account account)
    {
        enableWaitingList(account);
        return getAccountQuery(account);
    }

    @Override
    public void handleResultSet(Account account, Connection connection, ResultSet resultSet)
    {
        loadAndAddToCache(account, resultSet, connection);
    }

    @Override
    public void accountRemoved(Account account)
    {
        if (account == null)
            return;

        save(account, new Consumer<Boolean>() {
            private int counter = 0;

            @Override
            public void accept(Boolean aBoolean)
            {
                if (aBoolean)
                    //FIXME maybe do a better way to make sure this doesn't happen.
                    if (getAccountManager().getIfExists(account.getUUID()) == null)
                        removeFromCache(account);
                    else
                    if (counter++ < RETRY_TIMES)
                    {
                        System.err.println("Unable to save: " + account + " Trying " + (RETRY_TIMES - counter) + " more times.");
                        getScheduler().scheduleWithFixedDelay(() -> save(account, this), 5, 5, TimeUnit.SECONDS);
                    }
            }
        });
    }

    public abstract String getAccountQuery(Account account);

    public abstract V loadData(ResultSet resultSet) throws SQLException;


    @Override
    protected V loadValue(Connection connection, Account key) throws SQLException
    {
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(getQuery(key));
            return loadData(resultSet);
        }
    }


    protected boolean enableWaitingList(Account account)
    {
        if (waitingList.containsKey(account))
        {
            throw new IllegalStateException("There is already a waiting list!");
        }
        waitingList.put(account, new LinkedHashSet<>());
        return true;
    }

    protected void loadAndAddToCache(Account account, ResultSet resultSet, Connection connection)
    {
        V value;
        try
        {
            value = loadData(resultSet);
            if (value == null)
                value = getNew(connection, account);
            addToCache(account, value);
        }
        catch (SQLException e)
        {
            value = getNew(account);
            addToTempCache(account, value);
            e.printStackTrace();
        }

        final V returnValue = value;

        Scheduler.runSync(() -> {
            waitingList.get(account).forEach(vConsumer -> vConsumer.accept(returnValue));
            waitingList.remove(account);
        });
    }

    public AccountManager getAccountManager()
    {
        return accountManager;
    }
}
