package me.binarynetwork.core.playerholder.events;

import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by BinaryBench on 3/20/2016.
 */

/**
 * This event is called right before a Player is added from a PlayerHolder
 */
public class PlayerAddEvent extends Event {

    private PlayerHolder playerHolder;
    private Player player;

    public PlayerAddEvent(PlayerHolder playerHolder, Player player)
    {
        this.playerHolder = playerHolder;
        this.player = player;
    }
    public PlayerHolder getPlayerHolder()
    {
        return this.playerHolder;
    }

    public Player getPlayer()
    {
        return player;
    }


    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }


}
