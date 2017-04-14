package me.binarynetwork.game.factory;

import me.binarynetwork.core.component.Component;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.games.SpleefRunner;
import me.binarynetwork.game.games.runner.RunnerGame;
import me.binarynetwork.game.games.spleef.SpleefGame;
import me.binarynetwork.game.lobby.LobbyWorldManager;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/3/2016.
 */
public class SimpleGameFactory implements GameFactory {


    private int counter;

    private PlayerHolder playerHolder;
    private ScheduledExecutorService scheduler;
    private LobbyWorldManager lobbyWorldManager;

    public SimpleGameFactory(PlayerHolder playerHolder, ScheduledExecutorService scheduler, LobbyWorldManager lobbyWorldManager)
    {
        this.playerHolder = playerHolder;
        this.scheduler = scheduler;
        this.lobbyWorldManager = lobbyWorldManager;
    }

    @Override
    public Component getGame(Runnable onEnd)
    {
        if (playerHolder.getPlayers().size() <= 1)
            return new RunnerGame(playerHolder, scheduler, lobbyWorldManager, onEnd);

        switch (counter++)
        {
            case 0: return new SpleefGame(playerHolder, scheduler, lobbyWorldManager, onEnd);
            case 1: return new RunnerGame(playerHolder, scheduler, lobbyWorldManager, onEnd);
            case 2: return new SpleefRunner(playerHolder, scheduler, lobbyWorldManager, onEnd);
            default: counter = 0;
                return getGame(onEnd);
        }
    }
}
