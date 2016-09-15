package me.binarynetwork.core.portal.commands;

import me.binarynetwork.core.command.BaseCommand;
import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.command.CommandSender;

/**
 * Created by Bench on 9/15/2016.
 */
public class ServerCommand extends BaseCommand {

    private String server;

    public ServerCommand(PermissionManager permissionManager)
    {
        super(permissionManager, "portal.command");
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        return false;
    }
}
