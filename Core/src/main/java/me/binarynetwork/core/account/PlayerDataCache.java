package me.binarynetwork.core.account;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.database.SQLDataCacheTemp;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/13/2016.
 */
public abstract class PlayerDataCache<V> extends SQLDataCacheTemp<Account, V> implements AccountListener {

    private AccountManager accountManager;

    public PlayerDataCache(DataSource dataSource, ScheduledExecutorService scheduler, AccountManager accountManager, boolean loadOnJoin)
    {
        super(scheduler, dataSource);
        this.accountManager = accountManager;
        getAccountManager().addListener(this, loadOnJoin);
    }

    public abstract String getAccountQuery(Account account);

    public abstract V loadData(ResultSet resultSet) throws SQLException;

    public V getSync(Player player)
    {
        Account account = getAccountManager().getSync(player.getUniqueId());
        if (account == null)
            return null;
        return getSync(account);
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
        getAccountManager().get(player.getUniqueId(), account ->
            get(account, callback)
        );
    }

    //Override
    @Override
    public V loadValue(Account key, Connection connection) throws SQLException
    {
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(getQuery(key));
            return loadData(resultSet);
        }
    }

    //AccountListener
    @Override
    public String getQuery(Account account)
    {
        if (isCached(account))
        {
            Log.errorf("Account %s is already cached!", account);
            return null;
        }
        if (getFutures().containsKey(account))
        {
            Log.errorf("There is already a CompletableFuture for account: %s", account);
            return null;
        }
        getFutures().put(account, new CompletableFuture<>());
        return getAccountQuery(account);
    }

    @Override
    public void handleResultSet(Account account, Connection connection, ResultSet resultSet)
    {
        if (isCached(account))
            Log.errorf("Account %s is already cached!", account);

        V value;
        try
        {
            value = loadData(resultSet);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            markAsTemp(account);
            value = sqlFailure(account);
        }

        CompletableFuture<V> future = getFutures().get(account);

        if (future == null)
            getFutures().put(account, future = new CompletableFuture<V>());

        future.complete(value);
    }

    //Getters
    public AccountManager getAccountManager()
    {
        return accountManager;
    }
}
