package me.binarynetwork.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bench on 9/8/2016.
 */
public class TestCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args)
    {
        commandSender.sendMessage("Tab Input");
        commandSender.sendMessage("Label" + label);
        commandSender.sendMessage("Args" + Arrays.toString(args));
        commandSender.sendMessage("");
        return null;
    }
}
