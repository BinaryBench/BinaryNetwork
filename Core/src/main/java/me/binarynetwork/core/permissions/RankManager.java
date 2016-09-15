package me.binarynetwork.core.permissions;

import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.account.AccountManagerOld;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/10/2016.
 */
public class RankManager {
    private RankDataCache rankDataCache;

    public RankManager(ScheduledExecutorService scheduler, AccountManager accountManager)
    {
        this.rankDataCache = new RankDataCache(scheduler, accountManager);
    }

    //TODO set rank

    public Rank getRankIfExists(Player player)
    {
        return getRankDataCache().getIfExists(player);
    }

    public void getRank(Player player, Consumer<Rank> callback)
    {
        getRankDataCache().get(player, callback);
    }

    private RankDataCache getRankDataCache()
    {
        return rankDataCache;
    }
}
