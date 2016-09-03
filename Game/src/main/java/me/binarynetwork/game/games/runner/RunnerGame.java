package me.binarynetwork.game.games.runner;

import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.component.components.*;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.gamestate.GameState;
import me.binarynetwork.game.gamestate.GameStateManager;
import me.binarynetwork.game.lobby.LobbyComponent;
import me.binarynetwork.game.lobby.LobbyWorldComponent;
import me.binarynetwork.game.spectate.GameModeSpectateComponent;
import me.binarynetwork.game.spectate.components.DeathSpectate;
import me.binarynetwork.game.spectate.components.JoinSpectate;
import me.binarynetwork.core.component.world.SimpleWorldComponent;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/3/2016.
 */
public class RunnerGame extends ComponentWrapper {
    public static final String NAME = "Runner";

    GameStateManager manager;
    GameModeSpectateComponent spectateManager;
    SimpleWorldComponent worldManager;

    public RunnerGame(PlayerHolder playerHolder, ScheduledExecutorService scheduler, LobbyWorldComponent lobbyWorldComponent, Runnable onEnd)
    {
        worldManager = new SimpleWorldComponent(scheduler, NAME, onEnd);
        manager = new GameStateManager(onEnd);
        spectateManager = new GameModeSpectateComponent(playerHolder);

        addComponent(worldManager, manager, spectateManager);

        //Disable weather
        addComponent(new WeatherComponent(worldManager));

        //Lobby parkours & events
        manager.add(new LobbyComponent(playerHolder, lobbyWorldComponent), GameState.LOBBY);



        //All game
        manager.add(Arrays.asList(
                //Spectate
                new JoinSpectate(spectateManager),
                new DeathSpectate(spectateManager),
                //Keep players in world
                new KeepInWorld(playerHolder, worldManager),

                //Standard-ish stuff
                new NoBlockBreak(playerHolder),
                new NoBlockPlace(playerHolder),
                new NoDropItem(playerHolder),
                new NoPickUpItem(playerHolder),
                new NoHunger(playerHolder),
                new VoidKiller(spectateManager.getNonSpectateHolder(), worldManager),

                //This Game
                new NoDamage(playerHolder)
        ), GameState.PRE_GAME, GameState.GAME, GameState.POST_GAME);




    }
}
