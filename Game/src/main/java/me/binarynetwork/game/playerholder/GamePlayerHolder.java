package me.binarynetwork.game.playerholder;

import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/2/2016.
 */
public interface GamePlayerHolder extends PlayerHolder{
    boolean canJoin(Player player);

    boolean addPlayer(Player player);

    boolean removePlayer(Player player);
}
