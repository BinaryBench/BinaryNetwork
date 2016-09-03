package me.binarynetwork.game.playerholder.events;

import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerRemoveEvent;
import me.binarynetwork.game.playerholder.GamePlayerHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

/**
 * Created by Bench on 9/2/2016.
 */
public class PlayerQuitGameEvent extends PlayerRemoveEvent {

    public PlayerQuitGameEvent(GamePlayerHolder playerHolder, Player player)
    {
        super(playerHolder, player);
    }

    @Override
    public GamePlayerHolder getPlayerHolder()
    {
        return (GamePlayerHolder) super.getPlayerHolder();
    }
}