package me.binarynetwork.game.lobby;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.RandomUtil;
import me.binarynetwork.core.component.Component;
import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.component.components.*;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/3/2016.
 */
public class LobbyComponent extends ComponentWrapper implements Runnable, Listener {


    private int id;

    private PlayerHolder playerHolder;

    private Supplier<World> worldSupplier;


    public LobbyComponent(PlayerHolder playerHolder, Supplier<World> worldSupplier)
    {
        super(
                new NoBlockBreak(playerHolder),
                new NoBlockPlace(playerHolder),
                new NoDropItem(playerHolder),
                new NoPickUpItem(playerHolder),
                new NoHunger(playerHolder),
                new NoDamage(playerHolder));
        this.playerHolder = playerHolder;
        this.worldSupplier = worldSupplier;
    }

    @Override
    public void onEnable()
    {
        getPlayerHolder().forEach(this::spawnPlayer);
        this.id = Bukkit.getScheduler().runTaskTimer(BinaryNetworkPlugin.getPlugin(), this, 2, 2).getTaskId();

        BinaryNetworkPlugin.registerEvents(this);
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTask(this.id);

        BinaryNetworkPlugin.unregisterEvents(this);

    }


    @Override
    public void run()
    {
        for (Player player : getPlayerHolder())
        {
            Location loc = player.getLocation();
            if (loc.getBlockY() < 50)
            {
                spawnPlayer(player);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerAddEvent event)
    {
        if (event.getPlayerHolder() != getPlayerHolder())
            return;

        spawnPlayer(event.getPlayer());
    }


    public void spawnPlayer(Player player)
    {
        PlayerUtil.resetPlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(getSpawn());
    }


    public Location getSpawn()
    {
        double x = RandomUtil.randomDouble(-1, 2);
        double y = 60;
        double z = RandomUtil.randomDouble(-1, 2);

        return new Location(getWorld(), x, y, z);
    }

    public World getWorld()
    {
        World world = worldSupplier.get();

        if (world == null)
            System.err.println("NULL WORLD");

        return world;
    }

    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }
}