package me.binarynetwork.hub;

import me.binarynetwork.core.BinaryNetworkPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * Created by Bench on 8/31/2016.
 */
public class Main extends BinaryNetworkPlugin {
    @Override
    public void enable()
    {
        System.out.println("This plugin works!");
    }

    @EventHandler
    public void player(AsyncPlayerPreLoginEvent event)
    {

    }
}
