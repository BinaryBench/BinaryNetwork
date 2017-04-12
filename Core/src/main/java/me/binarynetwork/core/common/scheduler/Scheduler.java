package me.binarynetwork.core.common.scheduler;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ServerUtil;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

/**
 * Created by Bench on 9/4/2016.
 */
public class Scheduler {
    private Scheduler()
    {}

    public static void runSync(Runnable runnable)
    {
        Bukkit.getScheduler().runTask(ServerUtil.getPlugin(), runnable);
    }

    public static void runSyncLater(Runnable runnable, long amount)
    {
        Bukkit.getScheduler().runTaskLater(ServerUtil.getPlugin(), runnable, amount);
    }

    public static <T> void runSync(Consumer<T> consumer, T consume)
    {
        runSync(() -> consumer.accept(consume));
    }

    public static void runAsync(Runnable runnable, long ticks)
    {
        Bukkit.getScheduler().runTaskLaterAsynchronously(ServerUtil.getPlugin(), runnable, ticks);
    }

    public static void runAsync(Runnable runnable)
    {
        Bukkit.getScheduler().runTaskAsynchronously(ServerUtil.getPlugin(), runnable);
    }

}
