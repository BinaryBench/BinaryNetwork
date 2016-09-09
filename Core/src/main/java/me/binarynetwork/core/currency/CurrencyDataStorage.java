package me.binarynetwork.core.currency;

import me.binarynetwork.core.account.Account;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.account.PlayerDataStorage;
import me.binarynetwork.core.database.DataSourceManager;
import me.binarynetwork.core.permissions.Rank;

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
 * Created by Bench on 9/9/2016.
 */
public class CurrencyDataStorage extends PlayerDataStorage<Map<Currency, Integer>> {

    public static final String CREATE_CURRENCIES_TABLE =
            "CREATE TABLE IF NOT EXISTS currencies (" +
                "id TINYINT unsigned NOT NULL AUTO_INCREMENT," +
                "currencyName varchar(10) NOT NULL," +
                "PRIMARY KEY (id)," +
                    "UNIQUE (currencyName)," +
                    "UNIQUE (id)" +
            ") ENGINE=InnoDB;";

    public static final String CREATE_PLAYER_CURRENCIES_TABLE =
            "CREATE TABLE IF NOT EXISTS player_currencies (" +
                "accountId INT unsigned NOT NULL," +
                "currencyId TINYINT unsigned NOT NULL," +
                "amount INT DEFAULT 0," +
                    "PRIMARY KEY (accountId, currencyId)," +
                    "FOREIGN KEY (currencyId) REFERENCES currencies(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY (accountId) REFERENCES player_account(id) ON DELETE CASCADE ON UPDATE CASCADE" +
            ") ENGINE=InnoDB;";

    public static final String INSERT_INTO_CURRENCIES =
            "INSERT IGNORE INTO currencies (currencyName) VALUES (UPPER(?));";


    public static final String INSERT_INTO_PLAYER_CURRENCIES_ON_DUPLICATE_KEY =
            "INSERT IGNORE INTO player_currencies (accountId, currencyId, amount) SELECT ?, id, ? FROM currencies WHERE (currencyName=UPPER(?)) ON DUPLICATE KEY UPDATE amount=VALUES(amount);";

    public CurrencyDataStorage(ScheduledExecutorService scheduler, AccountManager accountManager)
    {
        super(DataSourceManager.PLAYER_DATA, scheduler, accountManager, true);
    }

    @Override
    public String getAccountQuery(Account account)
    {
        return "SELECT B.currencyName, A.amount FROM player_currencies A LEFT JOIN player_data.currencies B ON A.currencyId = B.id WHERE A.accountId=" + account.getId() + ";";
    }

    @Override
    public Map<Currency, Integer> loadData(ResultSet resultSet) throws SQLException
    {
        Map<Currency, Integer> map = new HashMap<>();
        while (resultSet.next())
        {
            String currencyString = resultSet.getString("currencyName");
            int amount = resultSet.getInt("amount");

            Currency foundCurrency = Currency.getById(currencyString);

            if (foundCurrency == null)
            {
                System.err.println("Unknown currency: " + currencyString);
            }
            else
            {
                map.put(foundCurrency, amount);
            }
        }

        if (map.isEmpty())
            return null;

        for (Map.Entry<Currency, Integer> s : map.entrySet())
        {
            System.err.println("Id: " + s.getKey().getDisplayName() + " Amount: " + s.getValue());
        }

        return map;
    }

    @Override
    protected Map<Currency, Integer> getNew(Account key)
    {
        return new HashMap<>();
    }

    @Override
    public void saveData(Connection connection, Map<Account, Map<Currency, Integer>> key, Consumer<Integer> successes)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_PLAYER_CURRENCIES_ON_DUPLICATE_KEY))
        {
            for (Map.Entry<Account, Map<Currency, Integer>> accountsEntry : key.entrySet())
            {
                for (Map.Entry<Currency, Integer> currencyEntry : accountsEntry.getValue().entrySet())
                {
                    preparedStatement.setInt(1, accountsEntry.getKey().getId());
                    preparedStatement.setInt(2, currencyEntry.getValue());
                    preparedStatement.setString(3, currencyEntry.getKey().getId());
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

    @Override
    public void initialize(Connection connection) throws SQLException
    {
        try (PreparedStatement createRanksTable = connection.prepareStatement(CREATE_CURRENCIES_TABLE);
             PreparedStatement createPlayerRanksTable = connection.prepareStatement(CREATE_PLAYER_CURRENCIES_TABLE);
             PreparedStatement populateRanksTable = connection.prepareStatement(INSERT_INTO_CURRENCIES))
        {

            createRanksTable.executeUpdate();
            createPlayerRanksTable.executeUpdate();

            for (Currency currency : Currency.values())
            {
                populateRanksTable.setString(1, currency.getId());
                populateRanksTable.addBatch();
            }

            populateRanksTable.executeBatch();
        }
    }
}
