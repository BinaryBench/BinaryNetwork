package me.binarynetwork.core.rank.commands;

import me.binarynetwork.core.command.Command;
import me.binarynetwork.core.common.format.F;
import me.binarynetwork.core.common.utils.*;
import me.binarynetwork.core.currency.Currency;
import me.binarynetwork.core.currency.CurrencyManager;
import me.binarynetwork.core.permissions.PermissionManager;
import me.binarynetwork.core.rank.Rank;
import me.binarynetwork.core.rank.RankManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Bench on 9/15/2016.
 */
public class RankCommand implements Command {

    private RankManager rankManager;
    private PermissionManager permissionManager;

    public RankCommand(RankManager rankManager, PermissionManager permissionManager)
    {
        this.permissionManager = permissionManager;
        this.rankManager = rankManager;
    }

    @Override
    public boolean hasPermission(CommandSender commandSender, String[] args)
    {
        return permissionManager.hasPermission(commandSender, "rank.command");
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args)
    {
        OfflinePlayer offlinePlayer;
        Rank rank = null;

        if (args.length < 1)
        {
            if (CommandUtil.mustBePlayer(sender))
                return true;
            offlinePlayer = ((Player) sender);

        }
        else if (args.length == 1)
        {
            rank = Rank.getRankFrom(args[0]);
            if (rank != null)
            {
                if (CommandUtil.mustBePlayer(sender))
                    return true;
                offlinePlayer = ((Player) sender);
            }
            else
            {
                offlinePlayer = ServerUtil.getOfflinePlayer(args[0]);
                if (offlinePlayer == null)
                {
                    PlayerUtil.message(sender, "Player '" + args[0] + "' not found!");
                    return true;
                }
            }
        }
        else
        {
            offlinePlayer = ServerUtil.getOfflinePlayer(args[0]);
            if (offlinePlayer == null)
            {
                PlayerUtil.message(sender, "Player '" + args[0] + "' not found!");
                return true;
            }
            rank = Rank.getRankFrom(args[1]);
            if (rank == null)
            {
                PlayerUtil.message(sender, "Rank '" + args[1] + "' not found!");
                return true;
            }
        }

        if (rank != null)
        {
            final Rank finalRank = rank;
            final OfflinePlayer finalOfflinePlayer = offlinePlayer;

            getRankManager().setRank(finalOfflinePlayer.getUniqueId(), rank, aBoolean ->
            {
                if (aBoolean)
                {
                    PlayerUtil.message(sender, F.possession(finalOfflinePlayer.getName()) + " rank set to " + finalRank.getDisplayName() + ".");
                }
                else
                {
                    PlayerUtil.message(sender, "Unable to set player " + F.possession(finalOfflinePlayer.getName()) + " rank to " + finalRank.getDisplayName() + ".");
                }
            });
        }
        else
        {
            final OfflinePlayer finalOfflinePlayer = offlinePlayer;

            getRankManager().getRank(finalOfflinePlayer.getUniqueId(), returnRank -> {
                if (returnRank != null)
                {
                    PlayerUtil.message(sender, F.possession(finalOfflinePlayer.getName()) + " rank is " + returnRank.getDisplayName() + ".");
                }
                else
                {
                    PlayerUtil.message(sender, "Unable to getWorld" + F.possession(finalOfflinePlayer.getName()) + " rank.");
                }
            });
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String[] args)
    {
        if (args.length == 1)
        {
            List<String> returnList = ServerUtil.getOnlinePlayerNames();
            returnList.addAll(getTabableRanks(args[0]));
            return ListUtil.startsWith(returnList, args[0], false);
        }
        if (args.length == 2)
        {
            return ListUtil.startsWith(getTabableRanks(args[1]), args[1], false);
        }
        return null;
    }

    private List<String> getTabableRanks(String search)
    {
        List<String> returnList = new ArrayList<>();
        for (Rank rank : Rank.values())
        {
            List<String> names = ListUtil.startsWith(Arrays.asList(rank.getDisplayName(), rank.getId()), search, false);
            if (!names.isEmpty())
                returnList.add(names.get(0));
        }
        return returnList;
    }

    @Override
    public String getUsage(CommandSender commandSender, String[] args)
    {
        return StringUtil.spaceJoin(F.optionalArgument("player"), F.optionalArgument("amount"));
    }

    public RankManager getRankManager()
    {
        return rankManager;
    }
}
