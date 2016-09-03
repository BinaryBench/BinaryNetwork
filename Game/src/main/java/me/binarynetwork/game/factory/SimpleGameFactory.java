package me.binarynetwork.game.factory;

import me.binarynetwork.core.component.Component;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.games.runner.RunnerGame;
import me.binarynetwork.game.games.spleef.SpleefGame;
import me.binarynetwork.game.lobby.LobbyWorldComponent;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/3/2016.
 */
public class SimpleGameFactory implements GameFactory {
    private PlayerHolder playerHolder;
    private ScheduledExecutorService scheduler;
    private LobbyWorldComponent lobbyWorldComponent;

    public SimpleGameFactory(PlayerHolder playerHolder, ScheduledExecutorService scheduler, LobbyWorldComponent lobbyWorldComponent)
    {
        this.playerHolder = playerHolder;
        this.scheduler = scheduler;
        this.lobbyWorldComponent = lobbyWorldComponent;
        lobbyWorldComponent.enable();
    }

    @Override
    public Component getGame(Runnable onEnd)
    {
        return new SpleefGame(playerHolder, scheduler, lobbyWorldComponent, onEnd);
    }
}
