package me.binarynetwork.core.currency;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.command.Command;
import me.binarynetwork.core.common.format.F;
import me.binarynetwork.core.common.utils.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Bench on 9/9/2016.
 */
public class CurrencyCommand implements Command {

    private Currency currency;
    private CurrencyManager currencyManager;

    public CurrencyCommand(Currency currency, CurrencyManager currencyManager)
    {
        this.currency = currency;
        this.currencyManager = currencyManager;
    }

    @Override
    public boolean hasPermission(CommandSender commandSender, String[] args)
    {
        return true;
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        Player player;
        String stringAmount = null;

        if (args.length < 1)
        {
            if (CommandUtil.mustBePlayer(sender))
                return true;
            player = (Player) sender;
        }
        else if (args.length == 1)
        {
            player = ServerUtil.getPlayer(args[0]);

            if (player == null)
            {
                if (CommandUtil.mustBePlayer(sender))
                    return true;

                player = (Player) sender;
                stringAmount = args[0];
            }
        }
        else
        {
            player = ServerUtil.getPlayer(args[0]);
            if (player == null)
            {
                PlayerUtil.message(sender, "Player '" + args[0] + "' not found!");
                return true;
            }
            stringAmount = args[1];
        }

        if (stringAmount != null)
        {
            if (!NumberUtil.isInt(stringAmount))
            {
                PlayerUtil.message(sender,"'" + stringAmount + "' is not a number!");
                return true;
            }
            int amount = Integer.valueOf(stringAmount);

            final Player finalPlayer = player;

            getCurrencyManager().setCurrency(player, getCurrency(), amount, () -> {
                PlayerUtil.message(sender, F.possession(finalPlayer) + " " + F.pluralize(getCurrency().getDisplayName()) + " set to " + amount + ".");
            });
        }
        else
        {
            final Player finalPlayer = player;

            getCurrencyManager().getCurrency(player, getCurrency(), integer -> {
                PlayerUtil.message(sender, PlayerUtil.getName(finalPlayer) + " has " + integer + " " + F.pluralize(getCurrency().getDisplayName(), integer) + ".");
            });
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String[] args)
    {
        if (args.length == 1)
            return ListUtil.startsWith(ServerUtil.getOnlinePlayerNames(), args[0], false);
        return null;
    }

    @Override
    public String getUsage(CommandSender commandSender, String[] args)
    {
        return StringUtil.spaceJoin(F.optionalArgument("player"), F.optionalArgument("amount"));
    }

    public Currency getCurrency()
    {
        return currency;
    }

    public CurrencyManager getCurrencyManager()
    {
        return currencyManager;
    }
}
