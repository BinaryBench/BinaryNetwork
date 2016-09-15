package me.binarynetwork.core.rank;

import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandWrapper;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.permissions.PermissionManager;
import me.binarynetwork.core.rank.commands.RankCommand;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/10/2016.
 */
public class RankManager {
    private RankDataCache rankDataCache;

    public RankManager(ScheduledExecutorService scheduler, AccountManager accountManager, CommandWrapper commandWrapper, PermissionManager permissionManager)
    {
        this.rankDataCache = new RankDataCache(scheduler, accountManager);
        commandWrapper.addCommand(new RankCommand(this, permissionManager), "rank");
    }

    //TODO set rank

    public void getRank(UUID uuid, Consumer<Rank> callback)
    {
        getRankDataCache().get(uuid, rank -> Scheduler.runSync(() -> callback.accept(rank)));
    }

    public void setRank(UUID uuid, Rank rank, Consumer<Boolean> callback)
    {
        getRankDataCache().setRank(uuid, rank, aBoolean -> Scheduler.runSync(() -> callback.accept(aBoolean)));
    }

    public Rank getRankSync(Player player)
    {
        return getRankDataCache().getSync(player);
    }

    public Rank getRankIfExists(Player player)
    {
        return getRankDataCache().getIfExists(player);
    }

    public void getRank(Player player, Consumer<Rank> callback)
    {
        getRankDataCache().get(player, rank -> Scheduler.runSync(() -> callback.accept(rank)));
    }

    private RankDataCache getRankDataCache()
    {
        return rankDataCache;
    }
}
