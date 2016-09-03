package me.binarynetwork.game.countdown;

import me.binarynetwork.core.common.scheduler.SyncRunnable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bench on 9/2/2016.
 */
public class Countdown {



    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> futureTask;
    private Runnable run;

    private int count = 0;

    private long delay;
    private long period;
    private TimeUnit timeUnit;

    public Countdown(ScheduledExecutorService scheduler, Runnable run, int count)
    {
        this(scheduler, run, count, 1, TimeUnit.SECONDS);
    }

    public Countdown(ScheduledExecutorService scheduler, Runnable run, int count, long period, TimeUnit timeUnit)
    {
        this(scheduler, run, count, period, period, timeUnit);
    }

    public Countdown(ScheduledExecutorService scheduler, Runnable run, int count, long delay, long period, TimeUnit timeUnit)
    {
        this.scheduler = scheduler;
        this.run = run;
        this.count = count;
        this.delay = delay;
        this.period = period;
        this.timeUnit = timeUnit;
    }

    public boolean start()
    {
        if (isRunning())
            return false;
        futureTask = getScheduler().scheduleAtFixedRate((SyncRunnable) () -> {
            getRun().run();
            count--;
        }, getPeriod(), getPeriod(), getTimeUnit());
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


    public void setCount(int count)
    {
        this.count = count;
    }

    public int getCount()
    {
        return count;
    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    public ScheduledFuture<?> getFutureTask()
    {
        return futureTask;
    }

    public Runnable getRun()
    {
        return run;
    }

    public long getDelay()
    {
        return delay;
    }

    public long getPeriod()
    {
        return period;
    }

    public TimeUnit getTimeUnit()
    {
        return timeUnit;
    }

}
