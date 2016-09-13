package me.binarynetwork.core.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/13/2016.
 */
public abstract class SQLDataCacheWithTemp<K, V> extends DataCacheWithTemp<K, V> {

    public static int RETRY_TIMES = 5;

    private DataSource dataSource;

    public SQLDataCacheWithTemp(ScheduledExecutorService scheduler, DataSource dataSource)
    {
        super(scheduler);
        this.dataSource = dataSource;
    }

    @Override
    public V loadValue(K key) throws Exception
    {
        int tries = 0;
        while (tries < 5)
        {
            try (Connection connection = getDataSource().getConnection())
            {
                return loadValue(key, connection);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            tries++;
            Thread.sleep(1000);
        }
        throw new Exception("Unable to load value after: " + RETRY_TIMES + " times");
    }

    public abstract V loadValue(K key, Connection connection) throws SQLException;

    public DataSource getDataSource()
    {
        return dataSource;
    }
}
