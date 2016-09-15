package me.binarynetwork.core.permissions;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Created by Bench on 9/14/2016.
 */
public class PermissionManagerWrapper implements PermissionManager {
    private PermissionManager permissionManager;

    public PermissionManagerWrapper()
    {
    }

    public PermissionManagerWrapper(PermissionManager permissionManager)
    {
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean hasPermission(Player player, String permission)
    {
        return permissionManager != null && permissionManager.hasPermission(player, permission);
    }

    @Override
    public void hasPermission(Player player, String permission, Consumer<Boolean> callback)
    {
        if (permission == null)
            callback.accept(false);
        permissionManager.hasPermission(player, permission, callback);
    }

    public void setWrappedPermissionManager(PermissionManager permissionManager)
    {
        if (permissionManager == this)
            throw new IllegalArgumentException("The permission manager cannot be this wrapper!");
        this.permissionManager = permissionManager;
    }

    public PermissionManager getWrappedPermissionManager()
    {
        return permissionManager;
    }
}
