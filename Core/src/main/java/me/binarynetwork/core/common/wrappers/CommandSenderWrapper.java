package me.binarynetwork.core.common.wrappers;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

/**
 * Created by Bench on 9/20/2016.
 */
public class CommandSenderWrapper implements CommandSender {
    private CommandSender wrappedCommandSender;

    public CommandSenderWrapper(CommandSender wrappedCommandSender)
    {
        this.wrappedCommandSender = wrappedCommandSender;
    }

    @Override
    public void sendMessage(String s)
    {
        wrappedCommandSender.sendMessage(s);
    }

    @Override
    public void sendMessage(String[] strings)
    {
        wrappedCommandSender.sendMessage(strings);
    }

    @Override
    public Server getServer()
    {
        return wrappedCommandSender.getServer();
    }

    @Override
    public String getName()
    {
        return wrappedCommandSender.getName();
    }

    @Override
    public boolean isPermissionSet(String s)
    {
        return wrappedCommandSender.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(Permission permission)
    {
        return wrappedCommandSender.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String s)
    {
        return wrappedCommandSender.hasPermission(s);
    }

    @Override
    public boolean hasPermission(Permission permission)
    {
        return wrappedCommandSender.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b)
    {
        return wrappedCommandSender.addAttachment(plugin, s, b);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin)
    {
        return wrappedCommandSender.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i)
    {
        return wrappedCommandSender.addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i)
    {
        return wrappedCommandSender.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment)
    {
        wrappedCommandSender.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions()
    {
        wrappedCommandSender.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions()
    {
        return wrappedCommandSender.getEffectivePermissions();
    }

    @Override
    public boolean isOp()
    {
        return wrappedCommandSender.isOp();
    }

    @Override
    public void setOp(boolean b)
    {
        wrappedCommandSender.setOp(b);
    }
}
