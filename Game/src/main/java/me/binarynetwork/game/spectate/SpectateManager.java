package me.binarynetwork.game.spectate;

import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Bench on 9/2/2016.
 */
public interface SpectateManager {

    boolean enableSpectate(Player player);
    boolean disableSpectate(Player player);

    PlayerHolder getSpectateHolder();
    PlayerHolder getNonSpectateHolder();

    default boolean isSpectating(Player player)
    {
        return getSpectateHolder().test(player);
    }
    default boolean isNotSpectating(Player player)
    {
        return getNonSpectateHolder().test(player);
    }

    default Collection<Player> getSpectators()
    {
        return getSpectateHolder().getPlayers();
    }
    default Collection<Player> getNonSpectators()
    {
        return getNonSpectateHolder().getPlayers();
    }


    boolean isJoinSpectate();
    void setJoinSpectate(boolean joinSpectate);
}
