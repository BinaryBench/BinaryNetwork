package me.binarynetwork.core.command;

import me.binarynetwork.core.common.utils.ListUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Created by Bench on 9/6/2016.
 */
public class SimpleCommandWrapper extends BaseCommandWrapper implements Command {
    
    public SimpleCommandWrapper()
    {
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args)
    {
        List<String> pair = subTabComplete(sender, label, args);

        if (pair != null)
            return pair;

        return localTabComplete(sender, label, args);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String label, String[] args)
    {
        Boolean commandData = subExecuteCommand(sender, label, args);
        if (commandData != null)
            return commandData;
        return false;
    }

    @Override
    public String getUsage(CommandSender sender, String label, String[] args)
    {
        Pair<String, CommandData> pair = subGetUsagePair(sender, label, args);

        if (pair != null)
        {
            return pair.getLeft() == null ? null : pair.getRight().getAliases().get(0) + " " + pair.getLeft();
        }


        Map<Command, List<String>> permitted = getPermittedCommands(sender, label, args);
        if (permitted.isEmpty())
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        boolean first = true;
        for (List<String> commands : permitted.values())
        {
            if (!first)
                sb.append(" | ");
            else
                first = false;
            sb.append(commands.get(0));
        }
        sb.append(">");
        return sb.toString();
    }

    @Override
    public boolean hasPermission(CommandSender sender, String label, String[] args)
    {
        Boolean pair = subHasPermission(sender, label, args);

        if (pair != null)
            return pair;

        return !getPermittedCommands(sender, label, args).isEmpty();
    }
}
