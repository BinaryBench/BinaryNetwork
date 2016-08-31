package me.binarynetwork.core.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 8/28/2016.
 */
public class ServerUtil {

    public static void shutdown(String shutdownMessage)
    {
        shutdown(shutdownMessage, shutdownMessage);
    }

    public static void shutdown(String shutdownMessage, String kickMessage)
    {
        for (Player player : Bukkit.getOnlinePlayers())
            player.kickPlayer(kickMessage);

        System.err.println(" ");
        System.err.println(shutdownMessage);
        System.err.println(" ");

        Bukkit.shutdown();
    }

    public static void broadcast(String message, Iterable<Player> players)
    {
        for (Player player : players)
            player.sendMessage(message);
    }

    public static void broadcast(String message)
    {
        Bukkit.broadcastMessage(message);
    }
}
