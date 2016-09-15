package me.binarynetwork.core.database;

import com.google.common.cache.CacheBuilder;

import java.util.concurrent.*;

/**
 * Created by Bench on 9/13/2016.
 */
public abstract class DataCacheTemp<K, V> extends DataCache<K, V> {

    private ScheduledExecutorService scheduler;
    private ConcurrentHashMap<K, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    private int amount;
    private TimeUnit unit;

    public DataCacheTemp(ScheduledExecutorService scheduler, int amount, TimeUnit unit)
    {
        this.scheduler = scheduler;
        CacheBuilder.newBuilder().build();
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public void removeFromCache(K key)
    {
        unmarkAsTemp(key);
        super.removeFromCache(key);
    }

    public void markAsTemp(K key)
    {
        markAsTemp(key, amount, unit);
    }

    public void markAsTemp(K key, int amount, TimeUnit unit)
    {
        ScheduledFuture<?> future = tasks.remove(key);
        if (future != null)
            future.cancel(true);
        tasks.put(key,getScheduler().schedule(() -> {
            removeFromCache(key);
        }, amount, unit));
    }

    public void unmarkAsTemp(K key)
    {
        ScheduledFuture<?> future = tasks.remove(key);
        if (future != null)
        {
            CompletableFuture<V> future1 = getFutures().get(key);
            V value = future1.getNow(null);
            future.cancel(true);
        }
    }

    public boolean isTemp(K key)
    {
        return tasks.containsKey(key);
    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }
}
