package me.binarynetwork.core.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Bench on 9/14/2016.
 */
@FunctionalInterface
public interface SQLRunnable {
    void call(Connection connection) throws SQLException;
}
