package me.binarynetwork.core.permissions;

import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bench on 9/5/2016.
 */
public enum Rank {

    OWNER("owner", ChatColor.RED + "Owner", "*"),
    DEV("dev", ChatColor.RED + "Dev", "*"),
    MODERATOR("mod", ChatColor.GOLD + "Mod", "*"),
    BUILDER("build", ChatColor.DARK_GRAY + "Builder", "*"),
    BERRY("berry", ChatColor.DARK_PURPLE + "Berry", "*"),
    DEFAULT("default", null);


    private String id;
    private String tag;
    private List<String> permissions;

    Rank(String id, String tag, String... permissions)
    {
        this.tag = tag;
        this.id = id;
        this.permissions = Arrays.asList(permissions);
    }

    public String getTag()
    {
        return tag;
    }

    public String getId()
    {
        return id;
    }

    //protected to make sure no one uses this, it should only be used by the PermissionManager
    protected List<String> getPermissions()
    {
        return permissions;
    }
}
