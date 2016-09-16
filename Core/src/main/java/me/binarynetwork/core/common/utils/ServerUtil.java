package me.binarynetwork.core.common.utils;

import me.binarynetwork.core.BinaryNetworkPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.*;

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

    public static Player getPlayer(UUID uuid)
    {
        return Bukkit.getPlayer(uuid);
    }



    public static boolean isOnline(UUID uuid)
    {
        return getPlayer(uuid) != null;
    }

    public static UUID getPlayersUUID(String name)
    {
        return getOfflinePlayer(name).getUniqueId();
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOfflinePlayer(String name)
    {
        return Bukkit.getOfflinePlayer(name);
    }

    public static OfflinePlayer getOfflinePlayer(UUID uuid)
    {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public static List<String> getOnlinePlayerNames()
    {
        List<String> names = new ArrayList<>();
        getOnlinePlayers().forEach(o -> names.add(o.getName()));
        return names;
    }

    public static Collection<? extends Player> getOnlinePlayers()
    {
        return Bukkit.getOnlinePlayers();
    }

    public static Plugin getPlugin()
    {
        return BinaryNetworkPlugin.getPlugin();
    }
}
