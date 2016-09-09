package me.binarynetwork.core.permissions;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Bench on 9/5/2016.
 */
public class RankPermissionManager extends ListenerComponent implements PermissionManager {

    private Map<UUID, PermissionAttachment> attachments;

    public RankPermissionManager(ComponentWrapper componentWrapper)
    {
        componentWrapper.addComponent(this);
        attachments = new HashMap<>();
    }

    @Override
    public boolean hasPermission(Player player, String permission)
    {
        if (!attachments.containsKey(player.getUniqueId()))
            return false;
        return player.hasPermission(permission);
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event)
    {
        attachPlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event)
    {
        detachPlayer(event.getPlayer());
    }

    @Override
    public void onEnable()
    {
        Bukkit.getOnlinePlayers().forEach(this::attachPlayer);
    }

    @Override
    public void onDisable()
    {
        for (Map.Entry<UUID, PermissionAttachment> entry : attachments.entrySet())
        {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null)
                player.removeAttachment(entry.getValue());
        }
        attachments.clear();
    }

    private void attachPlayer(Player player)
    {
        System.out.println("Attaching player: " + player.getName());
        if (!attachments.containsKey(player.getUniqueId()))
        {
            PermissionAttachment permissionAttachment = player.addAttachment(BinaryNetworkPlugin.getPlugin());
            attachments.put(player.getUniqueId(), permissionAttachment);

            //permissionAttachment.setPermission(new Permission());

            attachments.get(player.getUniqueId()).setPermission("*", true);

            System.out.println(player.hasPermission("this.is.test.test"));
        }

    }

    private void detachPlayer(Player player)
    {
        PermissionAttachment attachment = attachments.remove(player.getUniqueId());
        if (attachment != null)
            player.removeAttachment(attachment);
    }

}
