package me.binarynetwork.core.commands;

import me.binarynetwork.core.command.CommandWrapper;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.permissions.PermissionManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.GameMode;
import org.bukkit.command.defaults.*;
import org.bukkit.command.defaults.GameModeCommand;
import org.bukkit.command.defaults.StopCommand;
import org.bukkit.command.defaults.TeleportCommand;

/**
 * Created by Bench on 9/20/2016.
 */
public class DefaultCommandManager {
    private CommandWrapper commandWrapper;
    private PermissionManager permissionManager;
    public DefaultCommandManager(CommandWrapper commandWrapper, PermissionManager permissionManager)
    {
        this.commandWrapper = commandWrapper;
        this.permissionManager = permissionManager;
        add(new TeleportCommand(), "Teleport", "tp");
        add(new OpCommand(), "op");
        add(new GameModeCommand(), "Gamemode", "gm");
        for (GameMode gameMode : GameMode.values())
        {
            commandWrapper.addCommand(new me.binarynetwork.core.commands.GameModeCommand(permissionManager, gameMode),
                    WordUtils.capitalizeFully(gameMode.name()),
                    "gm" + gameMode.name().substring(0,1).toLowerCase()
            );
        }

        add(new TellCommand(), "Tell", "msg", "Message");
        add(new TimeCommand(), "Time");
        add(new ToggleDownfallCommand(), "ToggleDownfall", "tdf");
        add(new KickCommand(), "Kick");
        add(new StopCommand(), "Stop");
    }

    public void add(VanillaCommand vanillaCommand, String alias, String... aliases)
    {
        Log.debugf("Is CommandWrapper null: %s", commandWrapper == null);
        Log.debugf("Is permissionManader null: %s", permissionManager == null);

        commandWrapper.addCommand(new VanillaCommandWrapper(permissionManager, vanillaCommand), alias, aliases);
    }
}
