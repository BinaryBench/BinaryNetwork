package me.binarynetwork.core.component.components;

import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.function.Predicate;

/**
 * Created by Bench on 9/3/2016.
 */
public class KeepInWorld extends ListenerComponent {

    private PlayerHolder playerHolder;
    private Predicate<World> worldPredicate;

    private boolean stopTeleports;

    public KeepInWorld(PlayerHolder playerHolder, Predicate<World> worldPredicate)
    {
        this(playerHolder, worldPredicate, true);
    }

    public KeepInWorld(PlayerHolder playerHolder, Predicate<World> worldPredicate, boolean stopTeleports)
    {
        this.playerHolder = playerHolder;
        this.worldPredicate = worldPredicate;
        this.stopTeleports = stopTeleports;
    }


    @Override
    public void onEnable()
    {
        getPlayerHolder().forEach(this::checkPlayer);
    }


    @EventHandler
    public void onAdd(PlayerAddEvent event)
    {
        if (!event.getPlayerHolder().equals(getPlayerHolder()))
            return;

        checkPlayer(event.getPlayer());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {
        if (!getPlayerHolder().test(event.getPlayer()) || getWorldPredicate().test(event.getTo().getWorld()) || !stopTeleports)
            return;
        event.setCancelled(true);
        event.getPlayer().sendMessage("You are not allowed to leave this world!");
    }

    public void checkPlayer(Player player)
    {
        if (!getWorldPredicate().test(player.getWorld()))
            return;

        World world = getWorld();

        if (world == null)
            return;
        player.teleport(world.getSpawnLocation());
    }

    public World getWorld()
    {
        for (World world : Bukkit.getWorlds())
        {
            if (getWorldPredicate().test(world))
                return world;
        }
        return null;
    }

    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }

    public Predicate<World> getWorldPredicate()
    {
        return worldPredicate;
    }
}
