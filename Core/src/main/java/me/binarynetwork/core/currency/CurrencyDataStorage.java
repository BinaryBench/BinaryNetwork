package me.binarynetwork.core.currency;

import me.binarynetwork.core.account.Account;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.common.utils.RandomUtil;
import me.binarynetwork.core.database.DataSourceManager;
import me.binarynetwork.core.account.PlayerDataStorage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/4/2016.
 */
public class CurrencyDataStorage extends PlayerDataStorage<Map<Integer, Integer>> {

    public static String INSERT_INTO_ON_DUPLICATE_KEY = "INSERT INTO currency (accountId, currencyId, currencyAmount) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE currencyAmount=VALUES(currencyAmount);";

    public CurrencyDataStorage(ScheduledExecutorService scheduler, double offset, AccountManager accountManager)
    {
        super(DataSourceManager.PLAYER_DATA, scheduler, offset, accountManager, true);
    }

    @Override
    public Map<Integer, Integer> loadData(ResultSet resultSet) throws SQLException
    {
        Map<Integer, Integer> map = new HashMap<>();
        while (resultSet.next())
        {
            map.put(resultSet.getInt("currencyId"), resultSet.getInt("currencyAmount"));
        }
        if (map.isEmpty())
            return null;

        for (Map.Entry<Integer, Integer> s : map.entrySet())
        {
            System.err.println("Id: " + s.getKey() + " Amount: " + s.getValue());
        }

        return map;
    }

    @Override
    public String getQuery(Account key)
    {
        return "SELECT currencyId, currencyAmount FROM player_data.currency WHERE accountId=" + key.getId() +";";
    }

    @Override
    protected Map<Integer, Integer> getNew(Account key)
    {
        return new HashMap<>();
    }

    @Override
    public void saveData(Map<Account, Map<Integer, Integer>> key, Consumer<Integer> successes)
    {
        try(Connection connection = getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_ON_DUPLICATE_KEY))
        {
            for (Map.Entry<Account, Map<Integer, Integer>> accountsEntry : key.entrySet())
            {
                for (Map.Entry<Integer, Integer> currencyEntry : accountsEntry.getValue().entrySet())
                {
                    preparedStatement.setInt(1, accountsEntry.getKey().getId());
                    preparedStatement.setInt(2, currencyEntry.getKey());
                    preparedStatement.setInt(3, currencyEntry.getValue());
                    preparedStatement.addBatch();
                }
            }

            int[] affectedRows = preparedStatement.executeBatch();
            int successCount = 0;
            for (int affectedRow : affectedRows)
            {
                if (affectedRow >= 1)
                    successCount++;
            }
            successes.accept(successCount);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            successes.accept(0);
        }
    }
}
