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
        //Log.debugf("Event: %s", event.getUniqueId());
        //Log.debugf("OfflinePlayer: %s", ServerUtil.getOfflinePlayer(event.getName()).getUniqueId());
        UUID fetchedUUID = null;
        event.getUniqueId();
        try
        {
            fetchedUUID = UUIDFetcher.getUUIDOf(event.getName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.debugf("UUIDs: %s - %s", event.getUniqueId(), fetchedUUID);
        Log.debugf("Working: %s", event.getUniqueId().equals(fetchedUUID));
        Log.debugf("Ip: %s", event.getAddress());

        if (fetchedUUID == null)
            return;

        Log.debugf("Offline: %s", (event.getUniqueId().version() == 4 ? "Online" : "Offline"));
        Log.debugf("Online: %s", (fetchedUUID.version() == 4 ? "Online" : "Offline"));

    }
}
