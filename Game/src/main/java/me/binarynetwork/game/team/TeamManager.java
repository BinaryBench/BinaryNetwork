package me.binarynetwork.game.team;

import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Bench on 9/3/2016.
 */
public interface TeamManager {

    Collection<PlayerHolder> getTeams();

    PlayerHolder getTeam(Player player);

    boolean setPlayerTeam(Player player, PlayerHolder team);

}
