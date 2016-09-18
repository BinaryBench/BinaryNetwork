package me.binarynetwork.hub;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.hub.world.HubWorldManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.File;
import java.io.IOException;

/**
 * Created by Bench on 8/31/2016.
 */
public class Main extends BinaryNetworkPlugin {
    @Override
    public void enable()
    {
        new HubWorldManager(getServerPlayerHolder());
    }

    @EventHandler
    public void player(AsyncPlayerPreLoginEvent event)
    {

    }
}
