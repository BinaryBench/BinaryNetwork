package me.binarynetwork.core.component.components;

import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
import org.bukkit.event.EventHandler;

/**
 * Created by Home on 4/16/2017.
 */
public class JoinMessage extends ListenerComponent {

    private PlayerHolder playerHolder;
    private String message;

    public JoinMessage(PlayerHolder playerHolder, String message) {
        this.playerHolder = playerHolder;
        this.message = message;
    }

    @Override
    public void onEnable() {
        ServerUtil.broadcast(message, playerHolder);
    }

    @EventHandler
    public void onAdd(PlayerAddEvent event)
    {
        if (!event.getPlayerHolder().equals(getPlayerHolder()))
            return;
        event.getPlayer().sendMessage(message);
    }

    public PlayerHolder getPlayerHolder() {
        return playerHolder;
    }

    public String getMessage() {
        return message;
    }
}
