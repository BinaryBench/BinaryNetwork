package me.binarynetwork.core.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Bench on 9/6/2016.
 */
public interface Command {

    boolean hasPermission(CommandSender commandSender, String label, String[] args);

    boolean executeCommand(CommandSender commandSender, String label, String[] args);

    List<String> tabComplete(CommandSender commandSender, String label, String[] args);

    String getUsage(CommandSender commandSender, String label, String[] args);
}
