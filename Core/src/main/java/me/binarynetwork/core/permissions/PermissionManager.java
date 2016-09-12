package me.binarynetwork.core.permissions;

import com.comphenix.protocol.PacketType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Created by Bench on 9/5/2016.
 */
public interface PermissionManager {

    default boolean hasPermission(CommandSender commandSender, String permission)
    {
        if (commandSender instanceof Player)
            return hasPermission((Player) commandSender, permission);
        if (commandSender instanceof ConsoleCommandSender)
            return true;
        return false;
    }

    boolean hasPermission(Player player, String permission);

    void hasPermission(Player player, String permission, Consumer<Boolean> callback);

}
