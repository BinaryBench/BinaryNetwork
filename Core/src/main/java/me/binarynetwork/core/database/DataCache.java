package me.binarynetwork.core.database;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/13/2016.
 */
public abstract class DataCache<K, V> {

    private ConcurrentHashMap<K, CompletableFuture<V>> futures = new ConcurrentHashMap<>();

    private Function<K, V> get;

    public void get(K key, Consumer<V> callback)
    {
        CompletableFuture<V> future = getFuture(key);
        future.thenAccept(callback);
    }

    public V getSync(K key)
    {
        CompletableFuture<V> future = getFuture(key);
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

    private CompletableFuture<V> getFuture(K key)
    {
        CompletableFuture<V> future;
        if ((future = futures.get(key)) != null)
            return future;

        future = CompletableFuture.supplyAsync(() -> {
            return get.apply(key);
        });

        futures.put(key, future);

        return future;
    }

    public void removeFromCache(K key)
    {
        futures.remove(key);
    }
    public Map<K, V> getCache()
    {
        Map<K, V> map = new HashMap<K, V>();
        for (Map.Entry<K, CompletableFuture<V>> entry : futures.entrySet())
        {
            V value = entry.getValue().getNow(null);
            if (value != null)
                map.put(entry.getKey(), value);
        }
        return map;
    }
}
