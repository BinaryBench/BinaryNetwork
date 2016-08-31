package me.binarynetwork.core.common.scheduler;

import me.binarynetwork.core.BinaryNetworkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.Main;

/**
 * Created by Bench on 8/29/2016.
 */
@FunctionalInterface
public interface SyncRunnable extends Runnable {

    @Override
    default void run()
    {
        Bukkit.getScheduler().runTask(BinaryNetworkPlugin.getPlugin(), this::syncRun);
    }

    void syncRun();
}
