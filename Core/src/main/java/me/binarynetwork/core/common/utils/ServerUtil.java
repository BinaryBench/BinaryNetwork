package me.binarynetwork.core.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

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

    public static Player getPlayer(String name)
    {
        return Bukkit.getPlayer(name);
    }

    public static List<String> getOnlinePlayerNames()
    {
        List<String> names = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(o -> names.add(o.getDisplayName()));
        return names;
    }
}
