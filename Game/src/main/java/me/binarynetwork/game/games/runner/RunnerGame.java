package me.binarynetwork.game.games.runner;

import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.game.ComponentGame;
import me.binarynetwork.game.gamestate.GameStateManager;
import me.binarynetwork.game.lobby.LobbyWorldComponent;
import me.binarynetwork.game.spectate.GameModeSpectateComponent;

/**
 * Created by Bench on 9/3/2016.
 */
public class RunnerGame extends ComponentWrapper {
    GameStateManager manager;
    GameModeSpectateComponent spectateManager;
    public RunnerGame(PlayerHolder playerHolder, LobbyWorldComponent lobbyWorldComponent, Runnable onEnd)
    {
        manager = new GameStateManager(onEnd);
        spectateManager = new GameModeSpectateComponent(playerHolder);

        addComponent(manager, spectateManager);


    }
}
