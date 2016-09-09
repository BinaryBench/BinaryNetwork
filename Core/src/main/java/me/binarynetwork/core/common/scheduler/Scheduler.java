package me.binarynetwork.core.common.scheduler;

import me.binarynetwork.core.BinaryNetworkPlugin;
import org.bukkit.Bukkit;

/**
 * Created by Bench on 9/4/2016.
 */
public class Scheduler {
    private Scheduler()
    {

    }

    public static void runSync(Runnable runnable)
    {
        Bukkit.getScheduler().runTask(BinaryNetworkPlugin.getPlugin(), runnable);
    }

    public static void runAsync(Runnable runnable, long ticks)
    {
        Bukkit.getScheduler().runTaskLaterAsynchronously(BinaryNetworkPlugin.getPlugin(), runnable, ticks);
    }

    public static void runAsync(Runnable runnable)
    {
        Bukkit.getScheduler().runTaskAsynchronously(BinaryNetworkPlugin.getPlugin(), runnable);
    }

}
