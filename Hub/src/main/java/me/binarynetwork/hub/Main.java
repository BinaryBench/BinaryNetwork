package me.binarynetwork.hub;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.UUIDFetcher;
import me.binarynetwork.core.common.utils.ServerUtil;
import org.apache.logging.log4j.core.helpers.UUIDUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

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
