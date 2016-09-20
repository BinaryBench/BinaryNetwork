package me.binarynetwork.core.commands;

import me.binarynetwork.core.command.BaseCommand;
import me.binarynetwork.core.common.utils.StringUtil;
import me.binarynetwork.core.common.wrappers.CommandSenderWrapper;
import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;

import java.util.List;

/**
 * Created by Bench on 9/20/2016.
 */
@SuppressWarnings("deprecated")
public class VanillaCommandWrapper extends BaseCommand {

    private VanillaCommand wrapped;

    public VanillaCommandWrapper(PermissionManager permissionManager, VanillaCommand wrapped)
    {
        super(permissionManager, "minecraft.command." + wrapped.getName());
        this.wrapped = wrapped;
        wrapped.setPermission(null);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        return wrapped.execute(getWrapper(sender), wrapped.getLabel(), args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        return wrapped.tabComplete(sender, wrapped.getLabel(), args);
    }

    @Override
    public String getUsage(CommandSender sender, String[] args)
    {
        String usage = wrapped.getUsage();
        if (usage.startsWith("/"))
        {
            String[] arr = usage.split(" ", 2);
            if (arr.length < 2)
                return null;
            usage = arr[1];
        }
        return usage;
    }

    private CommandSender getWrapper(CommandSender sender)
    {
        return new CommandSenderWrapper(sender)
        {
            @Override
            public void sendMessage(String s)
            {
                if (ChatColor.stripColor(s).toLowerCase().startsWith("Usage: ".toLowerCase()))
                    return;
                super.sendMessage(s);
            }
        };
    }
}
