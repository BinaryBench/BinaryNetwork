package me.binarynetwork.core.account;


import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.database.DataSourceManager;
import me.binarynetwork.core.database.DataStorage;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


/**
 * Created by Bench on 9/3/2016.
 */
public class AccountManager extends DataStorage<UUID, Account> {


    public static String TABLE_NAME = "player_account";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INT unsigned NOT NULL AUTO_INCREMENT,uuid varchar(36) NOT NULL,PRIMARY KEY (id),UNIQUE (uuid),UNIQUE (id)) ENGINE=InnoDB;";
    private static final String NEW_LOGIN = "INSERT IGNORE INTO " + TABLE_NAME + " (uuid) VALUES(?);";
    private static final String SELECT_ACCOUNT = "SELECT id FROM " + TABLE_NAME + " where uuid=?;";

    private List<DataStorage<Account, ?>> listeners = Collections.synchronizedList(new ArrayList<>());

    public AccountManager()
    {
        super(DataSourceManager.PLAYER_DATA);
    }

    public void addListener(DataStorage<Account, ?> listener)
    {
        listeners.add(listener);
    }
    private void removeListener(DataStorage<Account, ?> listener)
    {
        listeners.remove(listener);
    }

    public void onLogin(UUID playersUUID)
    {
        Scheduler.runAsync(() -> {
            get(playersUUID, account ->
            {
                Scheduler.runAsync(() ->
                {
                    StringBuilder sb = new StringBuilder();


                    for (DataStorage<Account, ?> listener : listeners)
                    {
                        sb.append(listener.getQuery(account));
                    }
                    try (Connection connection = getDataSource().getConnection(); Statement statement = connection.createStatement())
                    {
                        boolean hasMoreResultSets = statement.execute(sb.toString());

                        int counter = 0;
                        while ( hasMoreResultSets || statement.getUpdateCount() != -1 ) {
                            if ( hasMoreResultSets ) {
                                ResultSet rs = statement.getResultSet();
                                // handle your rs here
                                listeners.get(counter++).loadAndAddToCache(account, rs);
                            } // if has rs
                            else { // if ddl/dml/...
                                int queryResult = statement.getUpdateCount();
                                if ( queryResult == -1 ) { // no more queries processed
                                    break;
                                } // no more queries processed
                                // handle success, failure, generated keys, etc here
                            } // if ddl/dml/...

                            // check to continue in the loop
                            hasMoreResultSets = statement.getMoreResults();
                        } // while results
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }

                });

            });
        });
    }

    @Override
    public Account loadData(ResultSet resultSet)
    {
        return null;
    }

    @Override
    public String getQuery(UUID key)
    {
        return "SELECT id FROM " + TABLE_NAME + " WHERE uuid='" + key.toString() +"';";
    }

    @Override
    public Account addData(UUID key)
    {

        try (PreparedStatement insertStatement = getDataSource().getConnection().prepareStatement(NEW_LOGIN);
             // Might want to use something besides getQuery(), but for now I'm using it for consistency
             PreparedStatement queryStatement = getDataSource().getConnection().prepareStatement(getQuery(key)))
        {
            insertStatement.setString(1, key.toString());
            insertStatement.executeUpdate();

            ResultSet resultSet = queryStatement.executeQuery();

            if (resultSet.next())
            {
                int id = resultSet.getInt("id");
                if (id != 0)
                    return new Account(id, key);
            }
            return null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveData(Map<UUID, Account> key)
    {

    }
}
