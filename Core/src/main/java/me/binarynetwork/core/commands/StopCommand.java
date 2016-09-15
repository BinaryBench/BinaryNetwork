package me.binarynetwork.core.commands;

import me.binarynetwork.core.command.BaseCommand;
import me.binarynetwork.core.command.Command;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Bench on 9/15/2016.
 */
public class StopCommand extends BaseCommand {
    public StopCommand(PermissionManager permissionManager)
    {
        super(permissionManager, "server.stop");
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        ServerUtil.shutdown("Oh no, someone shut down the server!");
        return true;
    }
}
