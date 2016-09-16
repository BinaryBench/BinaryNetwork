package me.binarynetwork.core.portal.commands;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.command.BaseCommand;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.CommandUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.common.utils.StringUtil;
import me.binarynetwork.core.permissions.PermissionManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/15/2016.
 */
public class ServerCommand extends BaseCommand {

    private String server;

    public ServerCommand(PermissionManager permissionManager, String server)
    {
        super(permissionManager, "portal.command");
        this.server = server;
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        if (CommandUtil.mustBePlayer(sender))
            return true;
        Player player = (Player) sender;
        PlayerUtil.sendToServer(player, server);
        return true;
    }
}
