package me.binarynetwork.core.command;

import me.binarynetwork.core.common.utils.ListUtil;
import me.binarynetwork.core.common.utils.MapUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bench on 9/6/2016.
 */
public class SimpleCommandWrapper implements Command, CommandWrapper {

    private Map<Command, List<String>> subCommands;

    public SimpleCommandWrapper()
    {
        this.subCommands = new LinkedHashMap<>();
    }

    public SimpleCommandWrapper addCommand(Command command, String label, String... aliases)
    {
        subCommands.put(command, ListUtil.append(label, aliases));
        return this;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String label, String[] args)
    {
        List<String> returnList = new ArrayList<>();
        if (args.length < 1)
        {
            returnList.addAll(getPermittedCommands(commandSender, label, args).values().stream().filter(commands -> commands.size() >= 1).map(commands -> commands.get(0)).collect(Collectors.toList()));
        }
        else
        {
            CommandData commandData = getSupCommandData(commandSender, label, args);

            if (commandData != null)
                return commandData.tabComplete();

            for (List<String> commands : getPermittedCommands(commandSender, label, args).values())
            {
                for (String command : commands)
                    if (command.toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        returnList.add(command);
                        break;
                    }
            }

        }
        return returnList;
    }

    @Override
    public boolean executeCommand(CommandSender commandSender, String label, String[] args)
    {
        CommandData commandData = getSupCommandData(commandSender, label, args);
        return commandData != null && commandData.executeCommand();
    }

    @Override
    public String getUsage(CommandSender commandSender, String label, String[] args)
    {
        CommandData commandData = getSupCommandData(commandSender, label, args);

        if (commandData != null)
        {
            String returnString = commandData.getUsage();
            return returnString == null ? null : commandData.getAliases().get(0) + " " + returnString;
        }

        Map<Command, List<String>> permitted = getPermittedCommands(commandSender, label, args);
        if (permitted.isEmpty())
        {
            System.out.println("Permitted is empty!");
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
    public boolean hasPermission(CommandSender commandSender, String label, String[] args)
    {
        CommandData commandData = getSupCommandData(commandSender, label, args);
        return commandData != null && commandData.hasPermission() || !getPermittedCommands(commandSender, label, args).isEmpty();
    }

    protected Map<Command, List<String>> getSubCommands()
    {
        return subCommands;
    }

    protected Map<Command, List<String>> getPermittedCommands(CommandSender commandSender, String label, String[] args)
    {

        Map<Command, List<String>> returnMap = new LinkedHashMap<>();

        for (Map.Entry<Command, List<String>> entry : getSubCommands().entrySet())
        {
            if (entry.getKey().hasPermission(commandSender, label, args))
                returnMap.put(entry.getKey(), entry.getValue());
        }

        return returnMap;
    }

    protected CommandData getSupCommandData(CommandSender commandSender, String label, String[] args)
    {
        if (args.length >= 1)
        {
            String newLabel = args[0];
            String[] newArgs = (String[]) ArrayUtils.subarray(args, 1, args.length);

            for (Map.Entry<Command, List<String>> entry : getSubCommands().entrySet())
            {
                if (ListUtil.containsIgnoreCase(entry.getValue(), newLabel) && entry.getKey().hasPermission(commandSender, newLabel, newArgs))
                {
                    return new CommandData(commandSender, newLabel, newArgs, entry.getKey(), entry.getValue());
                }
            }
        }
        return null;
    }

    protected class CommandData
    {
        CommandSender commandSender;
        String label;
        String[] args;
        List<String> aliases;
        Command command;

        public CommandData(CommandSender commandSender, String label, String[] args, Command command, List<String> aliases)
        {
            this.commandSender = commandSender;
            this.label = label;
            this.args = args;
            this.aliases = aliases;
            this.command = command;
        }

        //Command methods

        public boolean hasPermission()
        {
            return getCommand().hasPermission(getCommandSender(), getLabel(), getArgs());
        }

        public boolean executeCommand()
        {
            return getCommand().executeCommand(getCommandSender(), getLabel(), getArgs());
        }

        public List<String> tabComplete()
        {
            return getCommand().tabComplete(getCommandSender(), getLabel(), getArgs());
        }

        public String getUsage()
        {
            return getCommand().getUsage(getCommandSender(), getLabel(), getArgs());
        }

        // Getters & setters
        public List<String> getAliases()
        {
            return aliases;
        }

        public void setAliases(List<String> aliases)
        {
            this.aliases = aliases;
        }

        public CommandSender getCommandSender()
        {
            return commandSender;
        }

        public void setCommandSender(CommandSender commandSender)
        {
            this.commandSender = commandSender;
        }

        public String getLabel()
        {
            return label;
        }

        public void setLabel(String label)
        {
            this.label = label;
        }

        public String[] getArgs()
        {
            return args;
        }

        public void setArgs(String[] args)
        {
            this.args = args;
        }

        public Command getCommand()
        {
            return command;
        }

        public void setCommand(Command command)
        {
            this.command = command;
        }
    }

}
