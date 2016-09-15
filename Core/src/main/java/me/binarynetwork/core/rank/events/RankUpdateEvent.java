package me.binarynetwork.core.rank.events;

import me.binarynetwork.core.rank.Rank;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by Bench on 9/15/2016.
 */
public class RankUpdateEvent extends Event {
    private UUID uuid;
    private Rank oldRank;
    private Rank newRank;

    public RankUpdateEvent(UUID uuid, Rank oldRank, Rank newRank)
    {
        this.uuid = uuid;
        this.oldRank = oldRank;
        this.newRank = newRank;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public Rank getOldRank()
    {
        return oldRank;
    }

    public Rank getNewRank()
    {
        return newRank;
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
