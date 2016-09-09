package me.binarynetwork.core.database;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.common.utils.RandomUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/4/2016.
 */
public abstract class KeyValueDataStorage<K, V> extends DataStorage {

    public static long UPDATE_TIME = 60 * 3;

    private ConcurrentHashMap<K, V> cache;
    private Cache<K, V> tempCache;

    protected ConcurrentHashMap<K, Set<Consumer<V>>> waitingList;

    public KeyValueDataStorage(DataSource dataSource, ScheduledExecutorService scheduler)
    {
        super(dataSource, scheduler);
        init();
    }

    public KeyValueDataStorage(DataSource dataSource, ScheduledExecutorService scheduler, int maxAttempts)
    {
        super(dataSource, scheduler, maxAttempts);
        init();
    }

    public KeyValueDataStorage(DataSource dataSource, ScheduledExecutorService scheduler, int maxAttempts, long period, TimeUnit timeUnit)
    {
        super(dataSource, scheduler, maxAttempts, period, timeUnit);
        init();
    }

    private void init()
    {
        this.cache = new ConcurrentHashMap<K, V>();
        this.waitingList = new ConcurrentHashMap<>();
        this.tempCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();

        if (getScheduler() != null)
            getScheduler().scheduleWithFixedDelay(this::saveData, (long) ((UPDATE_TIME*RandomUtil.getRandom().nextDouble()) % UPDATE_TIME), UPDATE_TIME, TimeUnit.SECONDS);

        execute(this::initialize);
    }

    public V getIfExists(K key)
    {
        return cache.get(key);
    }

    public void getIfExists(K key, Consumer<V> callback)
    {
        callback.accept(getIfExists(key));
    }

    public void get(K key, Consumer<V> callback)
    {
        if (cache.containsKey(key)){
            callback.accept(cache.get(key));
            return;
        }

        V v;
        if ((v = tempCache.getIfPresent(key)) != null)
        {
            callback.accept(v);
            return;
        }


        if (waitingList.containsKey(key))
        {
            waitingList.get(key).add(callback);
            return;
        }

        waitingList.put(key, new HashSet<>());
        waitingList.get(key).add(callback);

        execute(connection -> {
            V value = null;
            try
            {
                value = loadValue(connection, key);

                // if loadValue returned null then sate value to new
                if (value == null)
                    value = getNew(connection, key);

                addToCache(key, value);
            }
            catch (SQLException e)
            {
                // If a SQL error occurred return a new key and add to temp cache
                value = getNew(key);
                addToTempCache(key, value);
                e.printStackTrace();
            }

            final V returnValue = value;

            Scheduler.runSync(() -> {
                waitingList.get(key).forEach(vConsumer -> vConsumer.accept(returnValue));
                waitingList.remove(key);
            });
        });
    }

    public void addToCache(K key, V value)
    {
        cache.put(key, value);
        tempCache.invalidate(key);
    }

    public void addToTempCache(K key, V value)
    {
        tempCache.put(key, value);
    }


    //public abstract String getQuery(K key);
    //public abstract V loadData(ResultSet resultSet) throws SQLException;

    protected abstract V loadValue(Connection connection, K key) throws SQLException;

    protected V getNew(Connection connection, K key) throws SQLException
    {
        return getNew(key);
    }

    protected abstract V getNew(K key);

    public abstract void saveData(Connection connection, Map<K, V> key, Consumer<Integer> successes);


    public boolean removeFromCache(K key)
    {
        if (!cache.containsKey(key))
            return false;
        cache.remove(key);
        return true;
    }

    public boolean save(K key, Consumer<Boolean> success)
    {
        if (!cache.containsKey(key))
            return false;

        execute(connection -> {
            saveData(connection, Collections.singletonMap(key, cache.get(key)), integer -> {
                success.accept(integer < 1);
            });
        });


        return true;
    }

    public boolean save(K key)
    {
        return save(key, aBoolean -> {
            if (aBoolean)
                throw new RuntimeException("Failed to save key: " + key);
        });
    }

    public void saveData()
    {
        saveData(integer -> {
            if (integer < cache.size())
                throw new RuntimeException("Failed to save cache. cache size="+cache.size() + " saved=" + integer);
        });
    }

    public void saveData(Consumer<Integer> successes)
    {
        execute(connection -> {
            saveData(connection, cache, successes);
        });

    }

    public abstract void initialize(Connection connection) throws SQLException;
}