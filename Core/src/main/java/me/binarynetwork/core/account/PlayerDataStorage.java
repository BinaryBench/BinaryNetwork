package me.binarynetwork.core.account;

import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.database.DataStorage;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/4/2016.
 */
public abstract class PlayerDataStorage<V> extends DataStorage<Account, V> {

    private AccountManager accountManager;

    public PlayerDataStorage(DataSource dataSource, ScheduledExecutorService scheduler, double offset, AccountManager accountManager, boolean loadOnJoin)
    {
        super(dataSource, scheduler, offset);
        this.accountManager = accountManager;
        getAccountManager().addPlayerStorage(this, loadOnJoin);
    }


    public void get(Player player, Consumer<V> callback)
    {
        getAccountManager().get(player.getUniqueId(), account ->
                get(account, callback));
    }


    public abstract String getQuery(Account account);

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
        waitingList.put(account, new HashSet<>());
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

    @Override
    public boolean save(Account key)
    {
        return super.save(key);
    }

    @Override
    public boolean save(Account key, Consumer<Boolean> success)
    {
        return super.save(key, success);
    }

    @Override
    public boolean removeFromCache(Account key)
    {
        return super.removeFromCache(key);
    }

    public AccountManager getAccountManager()
    {
        return accountManager;
    }
}
