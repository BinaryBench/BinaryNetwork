package me.binarynetwork.core.command;

import me.binarynetwork.core.common.utils.ListUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bench on 9/8/2016.
 */
public class SimpleCommandWrapper implements CommandWrapper, Command {

    private Map<Command, List<String>> subCommands;

    public SimpleCommandWrapper()
    {
        this.subCommands = new LinkedHashMap<>();
    }

    @Override
    public SimpleCommandWrapper addCommand(Command command, String mainAlias, String... aliases)
    {
        subCommands.put(command, ListUtil.append(mainAlias, aliases).stream().map(this::modifyString).collect(Collectors.toList()));
        return this;
    }

    @Override
    public SimpleCommandWrapper removeCommand(Command command)
    {
        subCommands.remove(command);
        return this;
    }

    public String modifyString(String string)
    {
        return string;
    }

    // Tab Complete
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        Pair<List<String>, CommandData> pair = subTabCompletePair(sender, args);

        if (pair != null)
            return pair.getLeft();
        else
            return localTabComplete(sender, args);
    }

    protected List<String> localTabComplete(CommandSender sender, String[] args)
    {
        List<String> returnList = new ArrayList<>();
        for (List<String> commands : getPermittedCommands(sender, args).values())
        {
            for (String command : commands)
                if (command.toLowerCase().startsWith(args[0].toLowerCase()))
                {
                    returnList.add(command);
                    break;
                }
        }
        if (returnList.isEmpty())
            return null;
        return returnList;
    }



    protected List<String> subTabComplete(CommandSender sender, String[] args)
    {
        Pair<List<String>, CommandData> pair = subTabCompletePair(sender, args);
        if (pair != null)
            return pair.getLeft();
        return null;
    }

    protected Pair<List<String>, CommandData> subTabCompletePair(CommandSender sender, String[] args)
    {
        CommandData commandData = getSupCommandData(sender, args);
        if (commandData == null)
            return null;
        if (commandData.getArgs().length < 1)
            return Pair.of(null, commandData);
        return Pair.of(commandData.tabComplete(), commandData);
    }



    // Permissions
    @Override
    public boolean hasPermission(CommandSender sender, String[] args)
    {
        Boolean hasSubPermission = subHasPermission(sender, args);
        if (hasSubPermission != null)
            return hasSubPermission;
        else
            return localHasPermission(sender, args);
    }

    protected boolean localHasPermission(CommandSender sender, String[] args)
    {
        return !getPermittedCommands(sender, args).isEmpty();
    }

    protected Boolean subHasPermission(CommandSender sender, String[] args)
    {
        Pair<Boolean, CommandData> pair = subHasPermissionPair(sender, args);
        if (pair != null)
            return pair.getLeft();
        return null;
    }

    protected Pair<Boolean, CommandData> subHasPermissionPair(CommandSender sender, String[] args)
    {
        CommandData commandData = getSupCommandData(sender, args);
        if (commandData == null)
            return null;

        if (commandData.getArgs().length < 1)
            return Pair.of(null, commandData);

        return Pair.of(commandData.hasPermission(), commandData);
    }

    // Execute Command
    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        Boolean success = subExecuteCommand(sender, args);
        if (success != null)
            return success;
        else
            return localExecuteCommand(sender, args);
    }

    protected boolean localExecuteCommand(CommandSender sender, String[] args)
    {
        return false;
    }

    protected Boolean subExecuteCommand(CommandSender sender, String[] args)
    {
        Pair<Boolean, CommandData> pair = subExecuteCommandPair(sender, args);
        if (pair != null)
            return pair.getLeft();
        return null;
    }

    protected Pair<Boolean, CommandData> subExecuteCommandPair(CommandSender sender, String[] args)
    {
        CommandData commandData = getSupCommandData(sender, args);
        if (commandData == null)
            return null;
        return Pair.of(commandData.executeCommand(), commandData);
    }



    // Get Usage
    @Override
    public String getUsage(CommandSender sender, String[] args)
    {
        Pair<String, CommandData> pair = subGetUsagePair(sender, args);

        if (pair != null)
            if (pair.getLeft() == null)
                return pair.getRight().getMainAlias();
            else
                return pair.getRight().getMainAlias() + " " + pair.getLeft();

        return localGetUsage(sender, args);
    }

    protected String localGetUsage(CommandSender sender, String[] args)
    {
        Map<Command, List<String>> permitted = getPermittedCommands(sender, args);
        if (permitted.isEmpty())
            return null;

        StringJoiner sj = new StringJoiner(" | ", "<", ">");
        for (List<String> commands : permitted.values())
            sj.add(commands.get(0));
        return sj.toString();
    }

    /**
     *
     * @param sender The sender that sent the command
     * @param args The arguments
     * @deprecated In pretty much all cases, you will want to append the alias to the beginning. (Let BinaryBench know if you have a use case where this is not true)
     * @return The sub commands result (which may be null) or null if no sub command was found.
     */
    @Deprecated
    protected String subGetUsage(CommandSender sender, String[] args)
    {
        Pair<String, CommandData> pair = subGetUsagePair(sender, args);
        if (pair != null)
            return pair.getLeft();
        return null;
    }

    protected Pair<String, CommandData> subGetUsagePair(CommandSender sender, String[] args)
    {
        CommandData commandData = getSupCommandData(sender, args);
        if (commandData == null)
            return null;
        return Pair.of(commandData.getUsage(), commandData);
    }






    public Map<Command, List<String>> getSubCommands()
    {
        return subCommands;
    }

    public Map<Command, List<String>> getPermittedCommands(CommandSender sender, String[] args)
    {

        Map<Command, List<String>> returnMap = new LinkedHashMap<>();

        for (Map.Entry<Command, List<String>> entry : getSubCommands().entrySet())
        {
            if (entry.getKey().hasPermission(sender, args))
                returnMap.put(entry.getKey(), entry.getValue());
        }

        return returnMap;
    }

    protected CommandData getSupCommandData(CommandSender sender, String[] args)
    {
        if (args.length >= 1)
        {
            String newLabel = args[0];
            String[] newArgs = (String[]) ArrayUtils.subarray(args, 1, args.length);

            for (Map.Entry<Command, List<String>> entry : getSubCommands().entrySet())
                if (entry.getKey().hasPermission(sender, newArgs))
                    for (String alias : entry.getValue())
                        if (alias.equalsIgnoreCase(newLabel))
                            return new CommandData(sender, newArgs, entry.getKey(), entry.getValue(), alias, entry.getValue().get(0));
        }
        return null;
    }

    public class CommandData
    {
        CommandSender sender;
        String[] args;
        String mainAlias;
        String usedAlias;
        List<String> aliases;
        Command command;

        public CommandData(CommandSender sender, String[] args, Command command, List<String> aliases, String usedAlias, String mainAlias)
        {
            this.sender = sender;
            this.args = args;
            this.mainAlias = mainAlias;
            this.usedAlias = usedAlias;
            this.aliases = aliases;
            this.command = command;
        }

        //Command methods

        public boolean hasPermission()
        {
            return getCommand().hasPermission(getCommandSender(), getArgs());
        }

        public boolean executeCommand()
        {
            return getCommand().executeCommand(getCommandSender(), getArgs());
        }

        public List<String> tabComplete()
        {
            return getCommand().tabComplete(getCommandSender(), getArgs());
        }

        public String getUsage()
        {
            return getCommand().getUsage(getCommandSender(), getArgs());
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
            return sender;
        }

        public void setCommandSender(CommandSender sender)
        {
            this.sender = sender;
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

        public String getMainAlias()
        {
            return mainAlias;
        }

        public void setMainAlias(String mainAlias)
        {
            this.mainAlias = mainAlias;
        }

        public String getUsedAlias()
        {
            return usedAlias;
        }

        public void setUsedAlias(String usedAlias)
        {
            this.usedAlias = usedAlias;
        }
    }
}