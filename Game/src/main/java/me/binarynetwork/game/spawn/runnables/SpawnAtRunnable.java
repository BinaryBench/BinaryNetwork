package me.binarynetwork.game.spawn.runnables;

import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.spawn.SpawnManager;

/**
 * Created by Bench on 9/3/2016.
 */
public class SpawnAtRunnable implements Runnable {
    private SpawnManager spawnManager;
    private PlayerHolder playerHolder;

    public SpawnAtRunnable(SpawnManager spawnManager, PlayerHolder playerHolder)
    {
        this.spawnManager = spawnManager;
        this.playerHolder = playerHolder;
    }

    @Override
    public void run()
    {
        spawnManager.respawn(playerHolder);
    }
}
