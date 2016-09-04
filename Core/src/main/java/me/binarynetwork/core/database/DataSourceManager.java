package me.binarynetwork.core.database;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * Created by Bench on 9/3/2016.
 */
public class DataSourceManager {

    public static final DataSource PLAYER_DATA = createDataSource("jdbc:mysql://localhost/player_data", "root", "1234567890");


    private static DataSource createDataSource(String url, String username, String password)
    {
        BasicDataSource source = new BasicDataSource();
        source.addConnectionProperty("allowMultiQueries", "true");
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl(url);
        source.setUsername(username);
        source.setPassword(password);
        return source;
    }

}
