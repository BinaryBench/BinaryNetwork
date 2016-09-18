package me.binarynetwork.core.commands;

import me.binarynetwork.core.command.BaseCommand;
import me.binarynetwork.core.common.utils.CommandUtil;
import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/17/2016.
 */
public class GameModeCommand extends BaseCommand {

    private GameMode gameMode;

    public GameModeCommand(PermissionManager permissionManager, GameMode gameMode)
    {
        super(permissionManager, "command.gamemode");
        this.gameMode = gameMode;
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        if (CommandUtil.mustBePlayer(sender))
            return true;
        Player player = (Player) sender;
        player.setGameMode(gameMode);
        return true;
    }
}
