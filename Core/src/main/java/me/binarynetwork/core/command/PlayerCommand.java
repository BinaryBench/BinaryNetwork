package me.binarynetwork.core.command;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.common.utils.CommandUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Bench on 9/6/2016.
 */
public interface PlayerCommand extends Command {

    @Override
    default boolean executeCommand(CommandSender sender, String[] args)
    {
        if (CommandUtil.mustBePlayer(sender))
            return true;
        return executeCommand((Player) sender, args);
    }

    boolean executeCommand(Player player, String[] args);


}
