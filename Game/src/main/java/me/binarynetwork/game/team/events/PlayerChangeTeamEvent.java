package me.binarynetwork.game.team.events;

import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

/**
 * Created by Bench on 9/3/2016.
 */
public class PlayerChangeTeamEvent extends Event implements Cancellable {

    private TeamManager teamManager;
    private Player player;
    private PlayerHolder from;
    private PlayerHolder to;

    public PlayerChangeTeamEvent(TeamManager teamManager, Player player, PlayerHolder from, PlayerHolder to)
    {
        this.teamManager = teamManager;
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public TeamManager getTeamManager()
    {
        return teamManager;
    }

    public Player getPlayer()
    {
        return player;
    }

    public PlayerHolder getFrom()
    {
        return from;
    }

    public PlayerHolder getTo()
    {
        return to;
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
