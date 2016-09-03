package me.binarynetwork.core.component.runnables;

import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.GameMode;

/**
 * Created by Bench on 9/3/2016.
 */
public class GameModeRunnable implements Runnable {

    private PlayerHolder playerHolder;
    private GameMode gameMode;

    public GameModeRunnable(PlayerHolder playerHolder, GameMode gameMode)
    {
        this.playerHolder = playerHolder;
        this.gameMode = gameMode;
    }

    @Override
    public void run()
    {
        playerHolder.forEach(player -> player.setGameMode(gameMode));
    }
}
