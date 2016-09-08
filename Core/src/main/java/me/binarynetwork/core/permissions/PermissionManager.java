package me.binarynetwork.core.permissions;

import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/5/2016.
 */
public interface PermissionManager {

    boolean hasPermission(Player player, String permission);

}
