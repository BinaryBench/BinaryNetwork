package me.binarynetwork.game.spawn.components;

import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.spawn.SpawnManager;

/**
 * Created by Bench on 9/3/2016.
 */
public class SpawnAtComponent extends BaseComponent {
    private SpawnManager spawnManager;
    private PlayerHolder playerHolder;
    private boolean spawnAfter;

    public SpawnAtComponent(SpawnManager spawnManager, PlayerHolder playerHolder)
    {
        this(spawnManager, playerHolder, false);
    }

    public SpawnAtComponent(SpawnManager spawnManager, PlayerHolder playerHolder, boolean spawnAfter)
    {
        this.spawnManager = spawnManager;
        this.playerHolder = playerHolder;
        this.spawnAfter = spawnAfter;
    }

    @Override
    public void onEnable()
    {
        if (!spawnAfter)
            spawnManager.respawn(playerHolder);
    }

    @Override
    public void onDisable()
    {
        if (spawnAfter)
            spawnManager.respawn(playerHolder);
    }
}
