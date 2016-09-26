package me.binarynetwork.core.command;

import com.comphenix.net.sf.cglib.asm.Label;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.common.utils.StreamUtil;
import me.binarynetwork.core.common.utils.StringUtil;
import org.apache.commons.lang.WordUtils;
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

    private volatile boolean allowDefaultCommands;

    public CommandManager(ProtocolManager protocolManager)
    {
        ServerUtil.registerEvents(this);
        protocolManager.addPacketListener(
                new PacketAdapter(ServerUtil.getPlugin(),
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
                                event.setCancelled(true);

                                Player player = event.getPlayer();

                                String[] args = getArgs(message);

                                List<String> completions = tabComplete(player, args);


                                if (completions == null)
                                {
                                    if (allowDefaultCommands)
                                        event.setCancelled(false);
                                    return;
                                }

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


        if (hasPermission(commandSender, args))
        {
            boolean success = executeCommand(commandSender, args);

            if (success)
                return;

            String usageMessage = getUsage(commandSender, args);

            if (usageMessage != null)
            {
                commandSender.sendMessage(usageMessage);
                return;
            }
        }

        commandSender.sendMessage(getUsage(commandSender, new String[]{}));

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args)
    {
        List<String> tabComplete = super.tabComplete(sender, args);

        if (tabComplete == null || tabComplete.isEmpty())
            return tabComplete;

        Set<String> hs = new LinkedHashSet<>();
        hs.addAll(tabComplete);
        tabComplete.clear();

        String label = args[args.length - 1];
        if (label.startsWith("/"))
            label = label.substring(1);

        TabType tabType;
        String lastChar = "";
        if (label.length() > 1)
            lastChar = label.substring(label.length()-1);



        if (args.length > 1 && args[0].equals(args[0].toUpperCase()))
            tabType = TabType.UPPER_CASE;
        else if (!label.isEmpty() && label.equals(label.toLowerCase()))
            tabType = TabType.LOWER_CASE;
        else if (label.equals(WordUtils.capitalizeFully(label)) || lastChar.equals(lastChar.toLowerCase()) || label.length() <= 2)
            tabType = TabType.CAPITALIZED;
        else if (label.equals(label.toUpperCase()))
            tabType = TabType.UPPER_CASE;
        else
            tabType = TabType.APPEND;


        for (String completion : hs)
        {
            StringBuilder toAdd = new StringBuilder();
            if (completion.startsWith("/"))
            {
                toAdd.append("/");
                completion = completion.substring(1);
            }

            switch (tabType)
            {
                case LOWER_CASE:
                    toAdd.append(completion.toLowerCase());
                    break;
                case UPPER_CASE:
                    toAdd.append(completion.toUpperCase());
                    break;
                case CAPITALIZED:
                    toAdd.append(WordUtils.capitalizeFully(completion));
                    break;
                default:
                    toAdd.append(label).append(completion.substring(label.length()));
                    break;
            }
            tabComplete.add(toAdd.toString());
        }

        return tabComplete;
    }

    @Override
    public String modifyString(String string)
    {
        return "/" + string;
    }

    @Override
    public String localGetUsage(CommandSender sender, String[] args)
    {
        Map<Command, List<String>> permitted = getPermittedCommands(sender, args);
        if (permitted.isEmpty())
            return null;

        StringJoiner sj = new StringJoiner("\n", "", "");
        for (Map.Entry<Command, List<String>> commands : permitted.entrySet())
        {
            StringBuilder sb = new StringBuilder(commands.getValue().get(0));

            String commandUsage = commands.getKey().getUsage(sender, args);
            if (commandUsage!=null)
                sb.append(" ").append(commandUsage);
            sj.add(sb.toString());
        }

        return sj.toString();
    }

    private String[] getArgs(String message)
    {
        //Remove preceding
        message = message.replaceAll("^\\s+", "");

        //Remove slash
        //message = message.substring(1, message.length());

        //Remove double spaces
        message = message.replaceAll("( )+", " ");

        //Split
        return message.split(" ", -1);
    }

    private enum TabType
    {
        LOWER_CASE,
        CAPITALIZED,
        UPPER_CASE,
        APPEND;
    }

    public void setAllowDefaultCommands(boolean allowDefaultCommands)
    {
        this.allowDefaultCommands = allowDefaultCommands;
    }
}
