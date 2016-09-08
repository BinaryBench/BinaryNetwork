package me.binarynetwork.core.command;

import com.comphenix.net.sf.cglib.asm.Label;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ListUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bench on 9/6/2016.
 */
public class CommandManager extends SimpleCommandWrapper implements Listener {

    public CommandManager(ProtocolManager protocolManager)
    {
        BinaryNetworkPlugin.registerEvents(this);
        protocolManager.addPacketListener(
                new PacketAdapter(BinaryNetworkPlugin.getPlugin(),
                        ListenerPriority.NORMAL,
                        PacketType.Play.Client.TAB_COMPLETE) {

                    @Override
                    public void onPacketReceiving(PacketEvent event)
                    {
                        if (event.getPacketType().equals(PacketType.Play.Client.TAB_COMPLETE))
                        {
                            PacketContainer packet = event.getPacket();
                            String message = packet.getStrings().read(0);

                            if (message.startsWith("/"))
                            {
                                Player player = event.getPlayer();

                                event.setCancelled(true);

                                String[] args = getArgs(message);

                                List<String> completions = tabComplete(player, null, args);

                                if (completions == null)
                                    return;

                                PacketContainer returnPacket = protocolManager.createPacket(PacketType.Play.Server.TAB_COMPLETE);

                                returnPacket.getStringArrays().write(0, completions.toArray(new String[0]));
                                try
                                {
                                    protocolManager.sendServerPacket(player, returnPacket);
                                }
                                catch (InvocationTargetException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
        );
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
        event.setCancelled(true);
        executeCommand(event.getPlayer(), event.getMessage());
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event)
    {
        executeCommand(event.getSender(), event.getCommand());
    }

    private void executeCommand(CommandSender commandSender, String message)
    {
        String[] args = getArgs(message);



        if (hasPermission(commandSender, null, args))
        {
            boolean success = executeCommand(commandSender, null, args);

            if (success)
                return;

            String usageMessage = getUsage(commandSender, null, args);

            if (usageMessage != null)
            {
                commandSender.sendMessage(usageMessage);
                return;
            }
        }

        commandSender.sendMessage("use /help to see commands!");

    }


    public List<String> tabComplete(CommandSender commandSender, String label, String[] args)
    {
        List<String> returnList = new ArrayList<>();
        if (args.length < 1)
        {
            returnList.addAll(getSubCommands().values().stream().filter(commands -> commands.size() >= 1).map(commands -> commands.get(0)).collect(Collectors.toList()));
        }
        else
        {
            CommandData commandData = getSupCommandData(commandSender, label, args);

            if (commandData != null)
                return commandData.tabComplete();

            for (List<String> commands : getSubCommands().values())
                for (String command : commands)
                    if (command.toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        returnList.add(command);
                        break;
                    }
        }

        List<String> temp = new ArrayList<>();
        for (String string : returnList)
            temp.add("/" + string);
        return temp;
    }


    public String getUsage(CommandSender commandSender, String label, String[] args)
    {
        CommandData commandData = getSupCommandData(commandSender, label, args);
        if (commandData != null)
        {
            String returnString = commandData.getUsage();
            System.out.print("return string: " + returnString);
            return returnString == null ? null : "/" + commandData.getAliases().get(0) + " " + returnString;
        }
        System.out.println("commandData is null");
        return null;
    }

    private String[] getArgs(String message)
    {
        //Remove preceding
        message = message.replaceAll("^\\s+", "");

        //Remove slash
        message = message.substring(1, message.length());

        //Remove double spaces
        message = message.replaceAll("( )+", " ");

        //Split
        return message.split(" ", -1);
    }

}
