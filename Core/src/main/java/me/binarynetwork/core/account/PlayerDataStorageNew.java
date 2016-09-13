package me.binarynetwork.core.account;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.database.SQLDataCacheWithTemp;
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
public abstract class PlayerDataStorageNew<V> extends SQLDataCacheWithTemp<Account, V> implements AccountListener {

    private AccountManagerNew accountManager;

    public PlayerDataStorageNew(DataSource dataSource, ScheduledExecutorService scheduler, AccountManagerNew accountManager, boolean loadOnJoin)
    {
        super(scheduler, dataSource);
        this.accountManager = accountManager;
        getAccountManager().addListener(this, loadOnJoin);
    }

    public V getSync(Player player)
    {
        Account account = getAccountManager().getIfExists(player.getUniqueId());
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

    @Override
    public String getQuery(Account account)
    {
        enableWaitingList(account);
        return getAccountQuery(account);
    }

    @Override
    public void handleResultSet(Account account, Connection connection, ResultSet resultSet)
    {
        loadAndAddToCache(account, resultSet, connection);
    }

    public abstract String getAccountQuery(Account account);

    public abstract V loadData(ResultSet resultSet) throws SQLException;

    @Override
    public V loadValue(Account key, Connection connection) throws SQLException
    {
        try (Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(getQuery(key));
            return loadData(resultSet);
        }
    }

    public void enableWaitingList(Account account)
    {
        if (hasCached(account))
            throw new IllegalStateException("Account " + account.toString() + " is already cached!");
        if (getFutures().containsKey(account))
            throw new IllegalStateException("There is already an CompletableFuture for account: " + account.toString());

        getFutures().put(account, new CompletableFuture<V>());
    }

    protected void loadAndAddToCache(Account account, ResultSet resultSet, Connection connection)
    {
        V value;
        try
        {
            value = loadData(resultSet);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            markAsTemp(account);
            value = loadTemp(account);
        }

        if (getFutures().containsKey(account))
            getFutures().get(account).complete(value);
        else //In the event that nothing was added just put a new one in
            getFutures().put(account, CompletableFuture.completedFuture(value));
    }

    public AccountManagerNew getAccountManager()
    {
        return accountManager;
    }
}
