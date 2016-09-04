package me.binarynetwork.game.team;

import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.team.events.PlayerChangeTeamEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bench on 9/3/2016.
 */
public class SimpleTeamManager implements TeamManager {

    private List<SimpleTeam> teams;

    @Override
    public Collection<PlayerHolder> getTeams()
    {
        return new ArrayList<>(teams);
    }

    @Override
    public PlayerHolder getTeam(Player player)
    {
        for (Team team : teams)
        {
            if (team.test(player))
                return team;
        }
        return null;
    }

    @Override
    public boolean setPlayerTeam(Player player, PlayerHolder team)
    {
        if (!teams.contains(team))
            throw new IllegalArgumentException("The team given is not a known team!");

        PlayerHolder currentTeam = getTeam(player);

        if (currentTeam.equals(team))
            return true;

        PlayerChangeTeamEvent event = new PlayerChangeTeamEvent(this, player, currentTeam, team);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled())
            return false;



        return false;
    }
}
