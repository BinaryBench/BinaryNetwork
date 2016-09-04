package me.binarynetwork.core.database;

import me.binarynetwork.core.common.scheduler.Scheduler;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/4/2016.
 */
public abstract class DataStorage<K, V> {

    private ConcurrentHashMap<K, V> cache;
    
    private DataSource dataSource;

    public DataStorage(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public void get(K key, Consumer<V> callback)
    {
        if (cache.containsKey(key))
            callback.accept(cache.get(key));

        Scheduler.runAsync(() -> {
            try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(getQuery(key)))
            {
                ResultSet resultSet = statement.executeQuery();
                V value;
                if (resultSet.next())
                {
                    resultSet.beforeFirst();
                    value = loadData(resultSet);
                }
                else
                    value = addData(key);
                addToCache(key, value);

                Scheduler.runSync(() -> callback.accept(value));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Scheduler.runSync(() -> callback.accept(null));
            }
        });

    }

    public void loadAndAddToCache(K key, ResultSet resultSet)
    {
        try
        {
            V value = loadData(resultSet);

            if (value == null)
                throw new RuntimeException("ResultSet not applicable for this datatype");

            addToCache(key, value);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public V addToCache(K key, V value)
    {
        return cache.put(key, value);
    }

    public abstract V loadData(ResultSet resultSet) throws SQLException;
    
    public abstract String getQuery(K key);

    public abstract V addData(K key);

    public abstract void saveData(Map<K, V> key);

    public void saveData()
    {
        saveData(cache);
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }
}