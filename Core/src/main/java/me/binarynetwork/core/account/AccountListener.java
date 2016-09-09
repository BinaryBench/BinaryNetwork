package me.binarynetwork.core.account;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by Bench on 9/8/2016.
 */
public interface AccountListener {

    String getQuery(Account account);

    void handleResultSet(Account account, Connection connection, ResultSet resultSet);

    void accountRemoved(Account account);

}
