package me.binarynetwork.game.playerholder.events;

import me.binarynetwork.game.playerholder.GamePlayerHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Bench on 9/2/2016.
 */
public class PlayerCanJoinGameEvent extends Event implements Cancellable {

    private GamePlayerHolder gamePlayerHolder;
    private Player player;

    public PlayerCanJoinGameEvent(GamePlayerHolder gamePlayerHolder, Player player)
    {
        this.gamePlayerHolder = gamePlayerHolder;
        this.player = player;
    }

    public GamePlayerHolder getGamePlayerHolder()
    {
        return gamePlayerHolder;
    }

    public Player getPlayer()
    {
        return player;
    }

    //Bukkit event stuff!
    private boolean cancelled;
    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b)
    {
        this.cancelled = b;
    }

    //Bukkit event stuff
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}