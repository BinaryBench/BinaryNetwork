package me.binarynetwork.core.rank;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandWrapper;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.permissions.PermissionManager;
import me.binarynetwork.core.rank.commands.RankCommand;
import me.binarynetwork.core.rank.events.RankUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/10/2016.
 */
public class RankManager implements Listener{
    private RankDataCache rankDataCache;

    public RankManager(ScheduledExecutorService scheduler, AccountManager accountManager, CommandWrapper commandWrapper, PermissionManager permissionManager)
    {
        this.rankDataCache = new RankDataCache(scheduler, accountManager);
        commandWrapper.addCommand(new RankCommand(this, permissionManager), "rank");
        BinaryNetworkPlugin.registerEvents(this);
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event)
    {
        updateNames(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void updateRank(RankUpdateEvent event)
    {
        updateNames(event.getUuid());
    }

    private void updateNames(UUID playersUUID)
    {
        if (!ServerUtil.isOnline(playersUUID))
            return;
        getRank(playersUUID, rank -> {
            Player player = ServerUtil.getPlayer(playersUUID);
            if (player == null)
                return;

            StringBuilder sb = new StringBuilder();
            if (rank.getTag() != null)
                sb.append(rank.getTag()).append(" ");
            sb.append(player.getName());

            player.setPlayerListName(sb.toString());
            player.setDisplayName(sb.toString());
        });
    }

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
        Rank rank = getRankIfExists(player);
        if (rank != null)
        {
            callback.accept(rank);
            return;
        }
        getRankDataCache().get(player, consumedRank -> Scheduler.runSync(() -> callback.accept(consumedRank)));
    }

    private RankDataCache getRankDataCache()
    {
        return rankDataCache;
    }
}
