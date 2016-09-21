package me.binarynetwork.core.playerholder;

import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Bench on 8/28/2016.
 */
public class BasePlayerHolder implements PlayerHolder {

    private Set<Player> players;

    public BasePlayerHolder()
    {
        this(new HashSet<>());
    }

    public BasePlayerHolder(Set<Player> players)
    {
        this.players = players;
    }

    @Override
    public Collection<Player> getPlayers()
    {
        return players;
    }

    public boolean addPlayer(Player player)
    {
        if (getPlayers().contains(player))
            return false;

        PlayerAddEvent event = new PlayerAddEvent(this, player);
        Bukkit.getPluginManager().callEvent(event);

        getPlayers().add(player);

        return true;
    }

    public boolean removePlayer(Player player)
    {
        if (!getPlayers().contains(player))
            return false;

        PlayerAddEvent event = new PlayerAddEvent(this, player);
        Bukkit.getPluginManager().callEvent(event);

        getPlayers().remove(player);

        return true;
    }
}