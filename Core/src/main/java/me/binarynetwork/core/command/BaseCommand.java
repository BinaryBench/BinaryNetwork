package me.binarynetwork.core.command;

import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Bench on 9/15/2016.
 */
public abstract class BaseCommand implements Command {
    private PermissionManager permissionManager;
    private String permission;

    public BaseCommand(PermissionManager permissionManager, String permission)
    {
        this.permissionManager = permissionManager;
        this.permission = permission;
    }

    @Override
    public boolean hasPermission(CommandSender sender, String[] args)
    {
        return getPermissionManager().hasPermission(sender, getPermission());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        return null;
    }

    @Override
    public String getUsage(CommandSender sender, String[] args)
    {
        return null;
    }

    public PermissionManager getPermissionManager()
    {
        return permissionManager;
    }

    public String getPermission()
    {
        return permission;
    }
}
