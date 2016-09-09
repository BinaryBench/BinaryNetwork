package me.binarynetwork.core.common.format;

import me.binarynetwork.core.common.utils.ListUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.StringUtil;
import org.atteo.evo.inflector.English;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Bench on 9/9/2016.
 */
public class F {
    public static ChatColor BASE_COLOR = ChatColor.GRAY;

    public static String main(String string)
    {
        return BASE_COLOR + string;
    }

    public static String possession(Player player)
    {
        return possession(PlayerUtil.getName(player));
    }

    public static String possession(String name)
    {
        StringBuilder sb = new StringBuilder(name);
        sb.append("'");
        if (!name.endsWith("s"))
            sb.append("s");
        return sb.toString();
    }

    public static String pluralize(String string)
    {
        return English.plural(string);
    }

    public static String pluralize(String string, int amount)
    {
        return English.plural(string, amount);
    }

    public static String optionalArgument(String string, String... strings)
    {
        return StringUtil.join("[", " | ", "]", string, strings);
    }

    public static String requiredArgument(String string, String... strings)
    {
        return StringUtil.join("<", " | ", ">", string, strings);
    }

}
