package me.binarynetwork.core.database;

import java.util.concurrent.*;

/**
 * Created by Bench on 9/13/2016.
 */
public abstract class DataCacheWithTemp<K, V> extends DataCache<K, V> {

    private ScheduledExecutorService scheduler;
    private ConcurrentHashMap<K, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    public DataCacheWithTemp(ScheduledExecutorService scheduler)
    {
        this.scheduler = scheduler;
    }

    @Override
    public V load(K key)
    {
        try
        {
            return loadValue(key);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        markAsTemp(key);
        return loadTemp(key);
    }

    @Override
    public void removeFromCache(K key)
    {
        unmarkAsTemp(key);
        super.removeFromCache(key);
    }

    public void markAsTemp(K key)
    {
        ScheduledFuture<?> future = tasks.remove(key);
        if (future != null)
            future.cancel(true);
        tasks.put(key,getScheduler().schedule(() -> {
            removeFromCache(key);
        }, 1, TimeUnit.MINUTES));
    }
    public void unmarkAsTemp(K key)
    {
        ScheduledFuture<?> future = tasks.remove(key);
        if (future != null)
        {
            CompletableFuture<V> future1 = getFutures().get(key);
            V value = future1.getNow(null);
            unloadedTemp(key, value);
            future.cancel(true);
        }
    }

    public abstract V loadValue(K key) throws Exception;

    public abstract V loadTemp(K key);


    public void unloadedTemp(K key, V value)
    {

    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }
}
