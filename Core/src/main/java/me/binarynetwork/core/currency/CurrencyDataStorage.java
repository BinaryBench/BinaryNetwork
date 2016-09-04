package me.binarynetwork.core.currency;

import me.binarynetwork.core.account.Account;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.database.DataSourceManager;
import me.binarynetwork.core.database.PlayerDataStorage;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bench on 9/4/2016.
 */
public class CurrencyDataStorage extends PlayerDataStorage<Map<Integer, Integer>> {

    public CurrencyDataStorage(AccountManager accountManager)
    {
        super(DataSourceManager.PLAYER_DATA, accountManager, true);
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
        return map;
    }

    @Override
    public String getQuery(Account key)
    {
        return "SELECT currencyId, currencyAmount FROM player_data.currency WHERE accountId=" + key.getId() +";";
    }

    @Override
    public Map<Integer, Integer> addData(Account key)
    {
        return new HashMap<>();
    }

    @Override
    public void saveData(Map<Account, Map<Integer, Integer>> key)
    {
        //TODO save stuff here
    }
}
