package me.binarynetwork.game.gamestate.events;

import me.binarynetwork.game.gamestate.GameStateManager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Bench on 9/2/2016.
 */
public class GameStateChangeEvent extends Event implements Cancellable {

    private GameStateManager gameStateManager;

    private boolean cancelled = false;

    private Object fromState;
    private Object toState;

    public GameStateChangeEvent(GameStateManager gameStateManager, Object fromState, Object toState)
    {
        this.gameStateManager = gameStateManager;
        this.fromState = fromState;
        this.toState = toState;
    }

    public GameStateManager getGameStateManager()
    {
        return gameStateManager;
    }

    public Object getFromState()
    {
        return fromState;
    }

    public Object getToState()
    {
        return toState;
    }

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
