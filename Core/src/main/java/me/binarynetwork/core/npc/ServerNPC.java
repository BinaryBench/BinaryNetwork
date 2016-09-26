package me.binarynetwork.core.npc;

import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.StringUtil;
import me.binarynetwork.core.entity.custom.CustomEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/24/2016.
 */
public class ServerNPC extends NPC {

    public ServerNPC(@Nonnull CustomEntity customEntity, @Nonnull Location location, @Nonnull String server)
    {
        this(customEntity, location, server, null);
    }

    public ServerNPC(@Nonnull CustomEntity customEntity, @Nonnull Location location, @Nonnull String server, @Nullable String npcName)
    {
        super(customEntity, location, npcName == null ? ChatColor.GOLD + StringUtil.capitalize(server) + " server" : npcName, player -> PlayerUtil.sendToServer(player, server.toLowerCase()));
    }
}
