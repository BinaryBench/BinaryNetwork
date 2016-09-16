package me.binarynetwork.core.database;

import me.binarynetwork.core.account.Account;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Filter;

/**
 * Created by Bench on 9/13/2016.
 */
public abstract class DataCache<K, V> {

    protected ConcurrentHashMap<K, CompletableFuture<V>> futures = new ConcurrentHashMap<>();

    public abstract V load(K key);

    public void get(K key, Consumer<V> callback)
    {
        CompletableFuture<V> future = getOrCreateFuture(key);
        future.thenAccept(callback);
    }

    public V getSync(K key)
    {
        CompletableFuture<V> future = getOrCreateFuture(key);
        try
        {
            return future.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public V getIfExists(K key)
    {
        CompletableFuture<V> future = futures.get(key);
        if (future == null)
            return null;
        return future.getNow(null);
    }

    public boolean isCached(K key)
    {
        return getIfExists(key) != null;
    }

    protected CompletableFuture<V> getOrCreateFuture(K key)
    {
        CompletableFuture<V> future;
        if ((future = futures.get(key)) != null)
            return future;

        future = CompletableFuture.supplyAsync(() -> load(key));

        futures.put(key, future);

        return future;
    }



    public void removeFromCache(K key)
    {
        futures.remove(key);
    }

    public void addToCache(K key, V value)
    {
        CompletableFuture<V> future = futures.get(key);
        if (future != null)
        {
            if(future.complete(value))
                return;
        }
        futures.put(key, CompletableFuture.completedFuture(value));
    }

    public Map<K, V> getCacheAsMap()
    {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, CompletableFuture<V>> entry : futures.entrySet())
        {
            V value = entry.getValue().getNow(null);
            if (value != null)
                map.put(entry.getKey(), value);
        }
        return map;
    }

    public Map<K, V> getCacheAsMap(Predicate<Map.Entry<K, V>> predicate)
    {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, CompletableFuture<V>> entry : futures.entrySet())
        {
            V value = entry.getValue().getNow(null);
            if (value != null && predicate.test(new AbstractMap.SimpleEntry<>(entry.getKey(), value)))
                map.put(entry.getKey(), value);
        }
        return map;
    }


    public int getCacheSize()
    {
        return getFutures().size();
    }

    protected ConcurrentHashMap<K, CompletableFuture<V>> getFutures()
    {
        return futures;
    }
}
