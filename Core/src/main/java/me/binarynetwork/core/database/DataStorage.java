package me.binarynetwork.core.database;

import me.binarynetwork.core.common.scheduler.Scheduler;
import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bench on 9/8/2016.
 */
public class DataStorage {

    private Map<DatabaseCall, Integer> failedCalls = new ConcurrentHashMap<>();

    private final int maxAttempts;
    private DataSource dataSource;
    private ScheduledExecutorService scheduler;

    private volatile boolean running;

    private ScheduledFuture<?> futureTask;

    private long period;
    private TimeUnit timeUnit;

    public DataStorage(DataSource dataSource, ScheduledExecutorService scheduler)
    {
        this(dataSource, scheduler, 5);
    }

    public DataStorage(DataSource dataSource, ScheduledExecutorService scheduler, int maxAttempts)
    {
        this(dataSource, scheduler, maxAttempts, 10, TimeUnit.SECONDS);
    }

    public DataStorage(DataSource dataSource, ScheduledExecutorService scheduler, int maxAttempts, long period, TimeUnit timeUnit)
    {
        this.maxAttempts = maxAttempts == 0 ? 1 : maxAttempts;
        this.dataSource = dataSource;
        this.scheduler = scheduler;
        this.period = period;
        this.timeUnit = timeUnit;

    }

    public boolean start()
    {
        if (isRunning())
            return false;
        futureTask = scheduler.scheduleAtFixedRate(() -> {
            if (!running)
            {
                running = true;
                executeCalls(failedCalls.keySet());
                running = false;
            }
        }, period, period, timeUnit);
        return true;
    }

    public boolean isRunning()
    {
        return futureTask != null && !futureTask.isCancelled();
    }

    public boolean stop()
    {
        if (!isRunning())
            return false;
        futureTask.cancel(true);
        futureTask = null;
        return true;
    }

    public void execute(DatabaseCall call)
    {
        if (Bukkit.isPrimaryThread())
            Scheduler.runAsync(() -> executeCall(call));
        else
            executeCall(call);
    }

    public void executeCalls(Iterable<DatabaseCall> calls)
    {
        try (Connection connection = dataSource.getConnection())
        {
            for (DatabaseCall call : calls)
            {
                executeCall(call, connection);
            }
        }
        catch (SQLException e)
        {
            //TODO Log connection fails
            e.printStackTrace();
            calls.forEach(call -> failedCalls.put(call, 0));
            updateScheduler();
        }
    }

    private void executeCall(DatabaseCall call)
    {
        try (Connection connection = dataSource.getConnection())
        {
            executeCall(call, connection);
        }
        catch (SQLException e)
        {
            //TODO Log connection failures
            e.printStackTrace();
            failedCalls.put(call, 0);
            updateScheduler();
        }
    }

    private void executeCall(DatabaseCall call, Connection connection)
    {
        try {
            call.execute(connection);
            failedCalls.remove(call);
        }
        catch (SQLException e)
        {
            //TODO Log fails
            e.printStackTrace();

            Integer attempts = failedCalls.get(call);

            if (attempts == null)
                attempts = 1;
            else
                attempts = attempts+1;

            if (attempts >= maxAttempts && maxAttempts >= 0)
                failedCalls.remove(call);
            else
                failedCalls.put(call, attempts);
        }
        updateScheduler();
    }

    private void updateScheduler()
    {
        if (failedCalls.isEmpty())
            stop();
        else
            start();
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }
}
