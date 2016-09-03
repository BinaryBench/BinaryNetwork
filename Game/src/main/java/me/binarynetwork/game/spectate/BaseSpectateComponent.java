package me.binarynetwork.game.spectate;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
import me.binarynetwork.core.playerholder.events.PlayerRemoveEvent;
import me.binarynetwork.game.spectate.events.DisableSpectateEvent;
import me.binarynetwork.game.spectate.events.EnableSpectateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bench on 9/2/2016.
 */
public abstract class BaseSpectateComponent extends ListenerComponent implements SpectateManager, PlayerHolder {

    private PlayerHolder playerHolder;
    private List<Player> spectaters = new ArrayList<>();
    private NonSpectateHolder nonSpectateHolder;

    private boolean joinSpectate;

    public BaseSpectateComponent(PlayerHolder playerHolder, boolean joinSpectate)
    {
        this.playerHolder = playerHolder;
        this.joinSpectate = joinSpectate;
        this.nonSpectateHolder = this.new NonSpectateHolder();
    }


    @Override
    public boolean enableSpectate(Player player)
    {
        if (!getDomainPlayerHolder().test(player))
        {
            throw new RuntimeException("Player not in the domain PlayerHolder!");
        }

        if (spectaters.contains(player))
            return false;

        Bukkit.getPluginManager().callEvent(new EnableSpectateEvent(this, player));
        fireRemove(getNonSpectateHolder(), player);
        fireAdd(getSpectateHolder(), player);

        this.spectaters.add(player);

        //call enable
        onEnableSpectate(player);

        return true;
    }

    @Override
    public boolean disableSpectate(Player player)
    {
        if (!getDomainPlayerHolder().test(player))
        {
            throw new RuntimeException("Player not in the domain PlayerHolder!");
        }

        if (!spectaters.contains(player))
            return false;

        Bukkit.getPluginManager().callEvent(new DisableSpectateEvent(this, player));
        fireRemove(getSpectateHolder(), player);

        fireAdd(getNonSpectateHolder(), player);
        this.spectaters.remove(player);

        //call disable
        onDisableSpectate(player);

        return true;
    }

    @EventHandler
    public void onJoin(PlayerAddEvent event)
    {
        if (!event.getPlayerHolder().equals(getDomainPlayerHolder()))
            return;

        Player player = event.getPlayer();

        if (isJoinSpectate())
        {
            fireAdd(getSpectateHolder(), player);
            Bukkit.getPluginManager().callEvent(new EnableSpectateEvent(this, player));
            this.spectaters.add(player);
            //call enable
            onEnableSpectate(player);
        }
        else
        {
            fireAdd(getNonSpectateHolder(), player);
            Bukkit.getPluginManager().callEvent(new DisableSpectateEvent(this, player));
            //call disable
            onDisableSpectate(player);
        }
    }

    public abstract void onEnableSpectate(Player player);

    public abstract void onDisableSpectate(Player player);

    @EventHandler
    public void onLeave(PlayerRemoveEvent event)
    {
        if (!event.getPlayerHolder().equals(getDomainPlayerHolder()))
            return;
        Player player = event.getPlayer();

        if (isSpectating(player))
        {
            fireRemove(getSpectateHolder(), player);
            this.spectaters.remove(player);
        }
        else if (isNotSpectating(player))
        {
            fireRemove(getNonSpectateHolder(), player);
        }
    }



    public boolean isJoinSpectate()
    {
        return joinSpectate;
    }

    private void fireRemove(PlayerHolder playerHolder, Player player)
    {
        Bukkit.getPluginManager().callEvent(new PlayerRemoveEvent(playerHolder, player));
    }

    private void fireAdd(PlayerHolder playerHolder, Player player)
    {
        Bukkit.getPluginManager().callEvent(new PlayerAddEvent(playerHolder, player));
    }



    @Override
    public Collection<Player> getPlayers()
    {
        return this.spectaters;
    }


    @Override
    public void setJoinSpectate(boolean joinSpectate)
    {
        this.joinSpectate = joinSpectate;
    }

    @Override
    public PlayerHolder getSpectateHolder()
    {
        return this;
    }

    @Override
    public PlayerHolder getNonSpectateHolder()
    {
        return this.nonSpectateHolder;
    }

    public PlayerHolder getDomainPlayerHolder()
    {
        return playerHolder;
    }








    private class NonSpectateHolder implements PlayerHolder {
        @Override
        public Collection<Player> getPlayers()
        {
            List<Player> players = new ArrayList<Player>(playerHolder.getPlayers());
            players.removeAll(spectaters);
            return players;
        }

        @Override
        public boolean test(Player player)
        {
            return playerHolder.test(player) && !spectaters.contains(player);
        }
    }

}