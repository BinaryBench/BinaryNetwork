package me.binarynetwork.core.command;

import com.comphenix.protocol.PacketType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Bench on 9/6/2016.
 */
public interface PlayerCommand extends Command {

    @Override
    default boolean executeCommand(CommandSender commandSender, String label, String[] args)
    {
        if (commandSender instanceof Player)
            return executeCommand((Player) commandSender, label, args);
        commandSender.sendMessage("You must be a player in order to use this command!");
        return false;
    }

    boolean executeCommand(Player player, String label, String[] args);


}
