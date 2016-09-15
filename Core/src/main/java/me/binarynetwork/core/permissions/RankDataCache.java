package me.binarynetwork.core.permissions;

import me.binarynetwork.core.account.*;
import me.binarynetwork.core.database.DataSourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/8/2016.
 */
public class RankDataCache extends PlayerDataCache<Rank> {

    public RankDataCache(ScheduledExecutorService scheduler, AccountManager accountManager)
    {
        super(DataSourceManager.PLAYER_DATA, scheduler, accountManager, true);
        execute(this::initialize);
    }

    public static final String CREATE_RANKS_TABLE =
            "CREATE TABLE IF NOT EXISTS ranks (" +
                "id TINYINT unsigned NOT NULL AUTO_INCREMENT," +
                "rankName varchar(10) NOT NULL," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE (rankName)," +
                    "UNIQUE (id)" +
            ") ENGINE=InnoDB;";

    public static final String INSERT_INTO_RANKS = "INSERT IGNORE INTO ranks (rankName) VALUES (UPPER(?));";

    public static final String CREATE_PLAYER_RANKS_TABLE =
            "CREATE TABLE IF NOT EXISTS player_data.player_rank (" +
                "accountId INT unsigned NOT NULL," +
                "rankId TINYINT unsigned NOT NULL," +
                "rankTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (accountId)," +
                    "FOREIGN KEY (rankId) REFERENCES ranks(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY (accountId) REFERENCES player_account(id) ON DELETE CASCADE ON UPDATE CASCADE" +
            ") ENGINE=InnoDB;";

    public static final String INSERT_ONLINE_PLAYER = "INSERT IGNORE INTO player_rank (accountId, rankId) SELECT ?, id FROM ranks WHERE (rankName=UPPER(?)) ON DUPLICATE KEY UPDATE rankId=VALUES(rankId);";

    public static final String INSERT_OFFLINE_PLAYER = "INSERT IGNORE INTO player_rank (accountId, rankId) " +
            "SELECT B.id, A.id FROM ranks A " +
            "JOIN player_account B ON B.uuid = ?" +
            "WHERE (A.rankName=UPPER(?)) ON DUPLICATE KEY UPDATE rankId=VALUES(rankId);";

    @Override
    public String getAccountQuery(Account account)
    {
        return "SELECT B.rankName FROM player_data.player_rank A LEFT JOIN player_data.ranks B ON A.rankId = B.id WHERE A.accountId=" + account.getId() + ";";
    }

    @Override
    public Rank loadData(ResultSet resultSet) throws SQLException
    {
        Rank returnRank = null;
        if (resultSet.next())
        {
            String id = resultSet.getString("rankName");
            returnRank = Rank.getRankById(id);
        }
        if (returnRank == null)
            returnRank = Rank.DEFAULT;

        return returnRank;
    }

    @Override
    public Rank sqlFailure(Account key)
    {
        return Rank.DEFAULT;
    }

    public void saveData(Connection connection, Map<Account, Rank> key, Consumer<Integer> successes)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ONLINE_PLAYER))
        {
            for (Map.Entry<Account, Rank> accountsEntry : key.entrySet())
            {
                preparedStatement.setInt(1, accountsEntry.getKey().getId());
                preparedStatement.setString(2, accountsEntry.getValue().getId());
                preparedStatement.addBatch();
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

    public void setRank(UUID uuid, Rank rank, Consumer<Boolean> callback)
    {
        Runnable onFail = () -> {
            callback.accept(false);
        };
        final Account account = getAccountManager().getIfExists(uuid);
        if (account != null)
            execute(connection -> {

                try (PreparedStatement statement = connection.prepareStatement(INSERT_ONLINE_PLAYER))
                {
                    statement.setInt(1, account.getId());
                    statement.setString(2, rank.getId());
                    statement.executeUpdate();
                }
                callback.accept(true);
            }, onFail);
        else
            execute(connection -> {
                connection.setAutoCommit(false);

                getAccountManager().executeNewLogin(uuid, connection);

                try (PreparedStatement statement = connection.prepareStatement(INSERT_OFFLINE_PLAYER))
                {
                    statement.setString(1, uuid.toString());
                    statement.setString(2, rank.getId());
                    statement.executeUpdate();
                }
                connection.commit();
                callback.accept(true);
            }, onFail);
    }

    public void initialize(Connection connection) throws SQLException
    {
        try (PreparedStatement createRanksTable = connection.prepareStatement(CREATE_RANKS_TABLE);
             PreparedStatement createPlayerRanksTable = connection.prepareStatement(CREATE_PLAYER_RANKS_TABLE);
             PreparedStatement populateRanksTable = connection.prepareStatement(INSERT_INTO_RANKS))
        {

            //To do log stuff!
            createRanksTable.executeUpdate();
            createPlayerRanksTable.executeUpdate();

            for (Rank rank : Rank.values())
            {
                populateRanksTable.setString(1, rank.getId());
                populateRanksTable.addBatch();
            }

            populateRanksTable.executeBatch();
        }
    }

    @Override
    public void accountRemoved(Account account)
    {

    }
}
