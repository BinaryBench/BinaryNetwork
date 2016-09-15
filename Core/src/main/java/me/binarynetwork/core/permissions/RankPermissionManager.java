package me.binarynetwork.core.permissions;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.MapUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.rank.Rank;
import me.binarynetwork.core.rank.RankManager;
import me.binarynetwork.core.rank.events.RankUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/5/2016.
 */
public class RankPermissionManager extends ListenerComponent implements PermissionManager {

    private Map<Rank, Map<Permission, Boolean>> rankPermissions = new HashMap<>();
    private Map<UUID, PermissionAttachment> attachments = new WeakHashMap<>();
    private HashMap<String, Permission> registeredPermissions = new HashMap<>();
    private RankManager rankManager;


    public RankPermissionManager(RankManager rankManager, ComponentWrapper componentWrapper)
    {
        this.rankManager = rankManager;
        componentWrapper.addComponent(this);
    }

    @Override
    public void onEnable()
    {
        reloadPermissions();
    }


    @Override
    public boolean hasPermission(Player player, String permission)
    {
        if (!registeredPermissions.containsKey(permission.toLowerCase()))
        {
            // if never seen this command before add command to bukkit and reload known commands
            addPermission(permission);
        }

        return player.hasPermission(permission);
    }

    @Override
    public void hasPermission(Player player, String permission, Consumer<Boolean> callback)
    {
        //Just to wait till loaded, probably needs to be fixed later
        rankManager.getRank(player, rank ->
        {
            callback.accept(hasPermission(player, permission));
        });
    }

    public void addPermission(String permissionString)
    {
        if (registeredPermissions.containsKey(permissionString.toLowerCase()))
            return;
        Permission permission = new Permission(permissionString.toLowerCase());
        Bukkit.getPluginManager().addPermission(permission);
        registeredPermissions.put(permission.getName().toLowerCase(), permission);
        reloadRankPermissions();
        for (Player player : ServerUtil.getOnlinePlayers())
        {
            updatePermissions(player);
        }
    }

    public void reloadPermissions()
    {
        registeredPermissions.clear();
        for (Permission perm : Bukkit.getPluginManager().getPermissions())
            registeredPermissions.put(perm.getName().toLowerCase(), perm);
        reloadRankPermissions();
        for (Player player : ServerUtil.getOnlinePlayers())
        {
            updatePermissions(player);
        }
    }

    public void reloadRankPermissions()
    {
        for (Rank rank : Rank.values())
        {
            rankPermissions.put(rank, getPermissions(rank.getPermissions()));
        }
    }

    public Map<Permission, Boolean> getPermissions(List<String> permStrings)
    {
        Collections.sort(permStrings, (o1, o2) ->
        {
            if (o1.endsWith("*"))
                o1 = o1.substring(0, o1.length()-1);
            if (o2.endsWith("*"))
                o2 = o2.substring(0, o2.length()-1);

            if (o1.startsWith("!"))
                o1 = o1.substring(1);
            if (o2.startsWith("!"))
                o2 = o2.substring(1);

            return Integer.compare(o1.length(), (o2.length()));
        });

        Map<Permission, Boolean> perms = new HashMap<>();

        for (String string : permStrings)
        {
            final boolean hasPerm = !string.startsWith("!");
            if (!hasPerm)
                string = string.substring(1);

            if (string.endsWith("*"))
            {
                string = string.substring(0, string.length() - 1);
                List<Permission> list = MapUtil.getValueStartsWith(registeredPermissions, string, false);
                list.forEach(permission -> {
                    perms.put(permission, hasPerm);
                });
            }
            else
            {
                Permission permission = registeredPermissions.get(string.toLowerCase());
                if (permission!= null)
                    perms.put(permission, hasPerm);
            }
        }
        return perms;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerJoinEvent(PlayerJoinEvent event)
    {
        updatePermissions(event.getPlayer());
    }

    @EventHandler
    public void rankChange(RankUpdateEvent event)
    {
        Player player = ServerUtil.getPlayer(event.getUuid());
        if (player != null)
            updatePermissions(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event)
    {
        attachments.remove(event.getPlayer().getUniqueId());
    }

    public void updatePermissions(Player player)
    {
        PermissionAttachment attachment;
        if (this.attachments.containsKey(player.getUniqueId()))
        {
            attachment = attachments.get(player.getUniqueId());
        }
        else
        {
            attachment = player.addAttachment(BinaryNetworkPlugin.getPlugin());
            attachments.put(player.getUniqueId(), attachment);
        }
        rankManager.getRank(player, rank -> {
            Map<Permission, Boolean> perms = rankPermissions.get(rank);
            if (perms == null)
                throw new IllegalStateException("Permissions not set for rank: " + rank.getTag());

            for (String string : attachment.getPermissions().keySet())
            {
                attachment.unsetPermission(string);
            }

            for (Map.Entry<Permission, Boolean> entry : perms.entrySet())
            {
                attachment.setPermission(entry.getKey(), entry.getValue());
            }
        });

    }
}
