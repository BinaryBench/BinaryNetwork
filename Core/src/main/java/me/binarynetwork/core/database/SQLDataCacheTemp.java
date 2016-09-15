package me.binarynetwork.core.database;

import com.avaje.ebeaninternal.api.SpiUpdatePlan;
import me.binarynetwork.core.common.Log;
import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/13/2016.
 */
public abstract class SQLDataCacheTemp<K, V> extends DataCacheTemp<K, V> {

    public static int RETRY_TIMES = 5;

    private DataSource dataSource;

    public SQLDataCacheTemp(ScheduledExecutorService scheduler, DataSource dataSource)
    {
        super(scheduler, 1, TimeUnit.MINUTES);
        this.dataSource = dataSource;
    }

    public abstract V loadValue(K key, Connection connection) throws SQLException;

    public abstract V sqlFailure(K key);

    @Override
    public V load(K key)
    {
        V value = executeQuery(connection -> loadValue(key, connection));
        if (value == null)
            value = sqlFailure(key);
        return value;
    }

    public <T> T executeQuery(SQLCallable<T> callable)
    {
        return executeQuery(callable, () -> {});
    }

    public <T> T executeQuery(SQLCallable<T> callable, Runnable onFail)
    {
        return executeQuery(callable, () -> {
            if (onFail != null)
                onFail.run();
            return null;
        });
    }

    public <T> T executeQuery(SQLCallable<T> callable, Supplier<T> onFail)
    {
        if (Bukkit.isPrimaryThread())
            Log.error("Call being made on primary thread!");

        int tries = 0;
        while (tries < 5)
        {
            try (Connection connection = getDataSource().getConnection())
            {
                return callable.call(connection);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            tries++;
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return onFail.get();
    }

    public void execute(SQLRunnable runnable)
    {
        execute(runnable, () -> {});
    }

    public void execute(SQLRunnable runnable, Runnable onFail)
    {
        getScheduler().execute(() ->
                executeQuery(connection -> {
                    runnable.call(connection);
                    return null;
                }, onFail));
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }
}
