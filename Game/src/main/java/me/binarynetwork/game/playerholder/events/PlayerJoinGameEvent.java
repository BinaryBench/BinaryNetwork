package me.binarynetwork.game.playerholder.events;

import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
import me.binarynetwork.game.playerholder.GamePlayerHolder;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/2/2016.
 */
public class PlayerJoinGameEvent extends PlayerAddEvent {

    public PlayerJoinGameEvent(GamePlayerHolder playerHolder, Player player)
    {
        super(playerHolder, player);
    }

    @Override
    public GamePlayerHolder getPlayerHolder()
    {
        return (GamePlayerHolder) super.getPlayerHolder();
    }
}
