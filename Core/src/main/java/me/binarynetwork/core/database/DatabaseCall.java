package me.binarynetwork.core.database;

import com.mysql.jdbc.exceptions.MySQLDataException;
import com.sun.glass.ui.EventLoop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Bench on 9/8/2016.
 */
@FunctionalInterface
public interface DatabaseCall {
    void execute(Connection connection) throws SQLException;
}
