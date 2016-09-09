package me.binarynetwork.core.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Bench on 9/6/2016.
 */
public interface Command {

    boolean hasPermission(CommandSender sender, String[] args);

    boolean executeCommand(CommandSender sender, String[] args);

    List<String> tabComplete(CommandSender sender, String[] args);

    String getUsage(CommandSender sender, String[] args);
}
