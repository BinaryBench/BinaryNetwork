package me.binarynetwork.core.playerholder;

import me.binarynetwork.core.common.utils.ServerUtil;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.function.Predicate;

/**
 * Created by Bench on 9/20/2016.
 */
public class WorldPlayerHolder extends BasePlayerHolder implements Listener {

    private Predicate<World> worldPredicate;

    public WorldPlayerHolder(Predicate<World> worldPredicate)
    {
        this.worldPredicate = worldPredicate;
        ServerUtil.registerEvents(this);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event)
    {
        if (worldPredicate.test(event.getTo().getWorld()))
            addPlayer(event.getPlayer());
        else
            removePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event)
    {
        if (worldPredicate.test(event.getPlayer().getWorld()))
            addPlayer(event.getPlayer());
    }

}
