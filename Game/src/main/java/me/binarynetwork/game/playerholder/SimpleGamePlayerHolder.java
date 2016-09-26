package me.binarynetwork.game.playerholder;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.game.playerholder.events.PlayerCanJoinGameEvent;
import me.binarynetwork.game.playerholder.events.PlayerJoinGameEvent;
import me.binarynetwork.game.playerholder.events.PlayerQuitGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bench on 9/2/2016.
 */
public class SimpleGamePlayerHolder implements GamePlayerHolder, Listener {

    private List<Player> players = new ArrayList<>();

    public SimpleGamePlayerHolder()
    {
        ServerUtil.registerEvents(this);
    }

    @Override
    public boolean canJoin(Player player)
    {
        PlayerCanJoinGameEvent event = new PlayerCanJoinGameEvent(this, player);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    @Override
    public boolean addPlayer(Player player)
    {
        if (!test(player))
        {
            Bukkit.getPluginManager().callEvent(new PlayerJoinGameEvent(this, player));
            players.add(player);
        }
        return false;
    }

    @Override
    public boolean removePlayer(Player player)
    {
        if (test(player))
        {
            Bukkit.getPluginManager().callEvent(new PlayerQuitGameEvent(this, player));
            players.remove(player);
            return true;
        }
        return false;
    }

    @Override
    public Collection<Player> getPlayers()
    {
        return players;
    }
}