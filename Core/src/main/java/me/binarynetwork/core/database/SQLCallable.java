package me.binarynetwork.core.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Created by Bench on 9/14/2016.
 */
@FunctionalInterface
public interface SQLCallable<V> {
    V call(Connection connection) throws SQLException;
}
