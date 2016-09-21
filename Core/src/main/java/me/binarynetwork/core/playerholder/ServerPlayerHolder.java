package me.binarynetwork.core.playerholder;

import me.binarynetwork.core.common.utils.ServerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Bench on 9/17/2016.
 */
public class ServerPlayerHolder extends BasePlayerHolder implements Listener {

    public ServerPlayerHolder()
    {
        ServerUtil.registerEvents(this);
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event)
    {
        addPlayer(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event)
    {
        removePlayer(event.getPlayer());
    }
}
