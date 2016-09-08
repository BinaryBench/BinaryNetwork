package me.binarynetwork.core.command;


import me.binarynetwork.core.common.utils.ListUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Bench on 9/6/2016.
 */
public class DummyCommand implements Command {

    private boolean hasPermission;
    private boolean succeed;
    private String usage;
    private List<String> tabComplete;

    public DummyCommand()
    {
        this(true, true, null, null);
    }

    public DummyCommand(boolean hasPermission, boolean succeed)
    {
        this(hasPermission, succeed, null, null);
    }

    public DummyCommand(boolean hasPermission, boolean succeed, String usage, List<String> tabComplete)
    {
        this.hasPermission = hasPermission;
        this.succeed = succeed;
        this.usage = usage;
        this.tabComplete = tabComplete;
    }

    @Override
    public boolean executeCommand(CommandSender commandSender, String label, String[] args)
    {
        return succeed;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String label, String[] args)
    {
        if (tabComplete == null || tabComplete.isEmpty())
            return null;
        if (args.length >= 1)
            return ListUtil.startsWith(tabComplete, args[0], false);
        else
            return tabComplete;
    }

    @Override
    public String getUsage(CommandSender commandSender, String label, String[] args)
    {
        return usage;
    }

    @Override
    public boolean hasPermission(CommandSender commandSender, String label, String[] args)
    {
        return hasPermission;
    }
}
