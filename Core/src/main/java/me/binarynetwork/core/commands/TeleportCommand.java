package me.binarynetwork.core.commands;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.command.BaseCommand;
import me.binarynetwork.core.common.format.F;
import me.binarynetwork.core.common.utils.*;
import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/17/2016.
 */
public class TeleportCommand extends BaseCommand {

    public TeleportCommand(PermissionManager permissionManager)
    {
        super(permissionManager, "command.tp");
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        if (CommandUtil.mustBePlayer(sender))
            return true;
        Player player = (Player) sender;

        if (args.length<3)
            return false;

        for (int i = 0; i < args.length; i++)
        {
            boolean relative = false;

            String arg = args[1];
            if (arg.startsWith("~"))
            {
                arg = arg.substring(1);
                if (arg.isEmpty())
                    arg = "0";
                relative = true;
            }

            if (!NumberUtil.isDouble(arg))
            {
                PlayerUtil.message(sender, arg + " is not a number!");
                return true;
            }
            double value = Double.valueOf(arg);

            if (relative)
                value = getCord(player.getLocation(), i);

            args[i] = Double.toString(value);
        }

        Location location = LocationUtil.toLocation(player.getWorld(), StringUtil.join("", ",", "", args));
        if (location == null)
            PlayerUtil.message(sender, "Unable to parse location!");
        else
            PlayerUtil.teleport(player, location);
        return true;
    }

    public double getCord(Location location, int cord)
    {
        switch (cord){
            case 0: return location.getX();
            case 1: return location.getY();
            case 2: return location.getZ();
            case 3: return location.getYaw();
            case 4: return location.getPitch();
            default: return 0;
        }
    }

    @Override
    public String getUsage(CommandSender sender, String[] args)
    {
        return F.requiredArgument("x") + " " + F.requiredArgument("y") + " " + F.requiredArgument("z");
    }
}
