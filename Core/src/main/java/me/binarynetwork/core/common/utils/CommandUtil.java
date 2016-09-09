package me.binarynetwork.core.common.utils;

import me.binarynetwork.core.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/9/2016.
 */
public class CommandUtil {

    public static boolean mustBePlayer(CommandSender sender, String message)
    {
        if (sender instanceof Player)
            return false;
        sender.sendMessage(message);
        return true;
    }

    public static boolean mustBePlayer(CommandSender sender)
    {
        return mustBePlayer(sender, "You must be a player in order to use this command!");

    }

}
