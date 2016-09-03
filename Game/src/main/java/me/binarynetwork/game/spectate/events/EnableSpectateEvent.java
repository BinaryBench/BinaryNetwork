package me.binarynetwork.game.spectate.events;

import me.binarynetwork.game.spectate.SpectateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Bench on 9/2/2016.
 */
public class EnableSpectateEvent extends Event {

    private SpectateManager spectateManager;
    private Player player;

    public EnableSpectateEvent(SpectateManager spectateManager, Player player)
    {
        this.spectateManager = spectateManager;
        this.player = player;
    }

    public SpectateManager getSpectateManager()
    {
        return spectateManager;
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