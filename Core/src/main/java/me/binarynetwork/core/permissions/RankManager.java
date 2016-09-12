package me.binarynetwork.core.permissions;

import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.currency.Currency;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/10/2016.
 */
public class RankManager {
    private RankDataStorage rankDataStorage;

    public RankManager(ScheduledExecutorService scheduler, AccountManager accountManager)
    {
        this.rankDataStorage = new RankDataStorage(scheduler, accountManager);
    }

    //TODO set rank

    public Rank getRankIfExists(Player player)
    {
        return getRankDataStorage().getIfExists(player);
    }

    public void getRank(Player player, Consumer<Rank> callback)
    {
        getRankDataStorage().get(player, callback);
    }

    private RankDataStorage getRankDataStorage()
    {
        return rankDataStorage;
    }
}
