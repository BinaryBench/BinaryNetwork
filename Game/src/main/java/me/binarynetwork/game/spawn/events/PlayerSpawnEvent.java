package me.binarynetwork.game.spawn.events;

import me.binarynetwork.game.lobby.LobbyWorldComponent;
import me.binarynetwork.game.spawn.SpawnManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Bench on 9/3/2016.
 */
public class PlayerSpawnEvent extends Event {
    private SpawnManager spawnManager;
    private Player player;

    private Location from;
    private Location to;


    public PlayerSpawnEvent(SpawnManager spawnManager, Player player, Location from, Location to)
    {
        this.spawnManager = spawnManager;
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public SpawnManager getSpawnManager()
    {
        return spawnManager;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Location getFrom()
    {
        return from;
    }

    public Location getTo()
    {
        return to;
    }

    //Bukkit event stuff!
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
}
