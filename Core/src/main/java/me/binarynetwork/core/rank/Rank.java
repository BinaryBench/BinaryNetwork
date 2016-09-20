package me.binarynetwork.core.rank;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bench on 9/5/2016.
 */
public enum Rank {

    OWNER("owner", "Owner",ChatColor.RED + "Owner",
            "*"
    ),
    DEV("dev", "Dev",ChatColor.RED + "Dev",
            "*",
            "!server.stop"
    ),
    MODERATOR("mod", "Mod",ChatColor.GOLD + "Mod",
            "portal.command"
    ),
    BUILDER("build", "Builder", ChatColor.DARK_GRAY + "Builder",
            "portal.command"
    ),
    BERRY("berry", "Berry",ChatColor.DARK_PURPLE + "Berry",
            "portal.command"
    ),
    DEFAULT("default", "Default", null,
            "portal.command"
    );


    private String id;
    private String displayName;
    private String tag;
    private List<String> permissions;

    Rank(String id, String displayName, String tag, String... permissions)
    {
        if (tag != null)
            this.tag = tag + ChatColor.RESET;
        this.displayName = displayName;
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

    public String getDisplayName()
    {
        return displayName;
    }

    //protected to make sure no one uses this, it should only be used by the PermissionManager
    public List<String> getPermissions()
    {
        return permissions;
    }

    public static Rank getRankById(String string)
    {
        for (Rank rank : Rank.values())
        {
            if (rank.getId().equalsIgnoreCase(string))
            {
                return rank;
            }
        }
        return null;
    }

    public static Rank getRankFrom(String search)
    {
        for (Rank rank : Rank.values())
        {
            if (rank.getId().equalsIgnoreCase(search) ||
                    rank.toString().equalsIgnoreCase(search) ||
                    rank.getDisplayName().equalsIgnoreCase(search) ||
                    (rank.getTag() != null && ChatColor.stripColor(rank.getTag()).equalsIgnoreCase(search)))

                return rank;
        }
        return null;
    }
}
