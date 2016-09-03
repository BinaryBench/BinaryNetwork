package me.binarynetwork.core;

import me.binarynetwork.core.common.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 8/31/2016.
 */
public abstract class BinaryNetworkPlugin extends JavaPlugin {

    private static BinaryNetworkPlugin plugin;
    public static BinaryNetworkPlugin getPlugin()
    {
        return plugin;
    }
    public static void registerEvents(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());
    }
    public static void unregisterEvents(Listener listener)
    {
        HandlerList.unregisterAll(listener);
    }

    private ScheduledExecutorService scheduler;



    @Override
    public final void onEnable()
    {
        plugin = this;
        getDataFolder().mkdirs();
        scheduler = Executors.newScheduledThreadPool(10);
        WorldUtil.purgeTemporaryWorlds();
        enable();
    }

    @Override
    public final void onDisable()
    {
        disable();
        WorldUtil.purgeTemporaryWorlds();
    }

    public void enable()
    {

    }

    public void disable()
    {

    }




    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }
}
