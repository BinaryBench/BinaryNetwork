package me.binarynetwork.game.games.runner;

import me.binarynetwork.core.common.utils.FallingBlockKiller;
import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.component.components.*;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.countdown.TempCountdown;
import me.binarynetwork.game.countdown.TempPlayerCountdown;
import me.binarynetwork.game.games.runner.components.RunnerComponent;
import me.binarynetwork.game.gamestate.GameState;
import me.binarynetwork.game.gamestate.GameStateManager;
import me.binarynetwork.game.lobby.LobbyComponent;
import me.binarynetwork.game.lobby.LobbyWorldComponent;
import me.binarynetwork.game.spawn.SimpleSpawnManager;
import me.binarynetwork.game.spawn.SpawnManager;
import me.binarynetwork.game.spawn.components.SpawnAtComponent;
import me.binarynetwork.game.spectate.GameModeSpectateComponent;
import me.binarynetwork.game.spectate.components.DeathSpectate;
import me.binarynetwork.game.spectate.components.JoinSpectate;
import me.binarynetwork.core.component.world.SimpleWorldComponent;
import me.binarynetwork.game.victorycondition.LMSVictoryCondition;

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
    SimpleSpawnManager spawnManager;

    public RunnerGame(PlayerHolder playerHolder, ScheduledExecutorService scheduler, LobbyWorldComponent lobbyWorldComponent, Runnable onEnd)
    {
        worldManager = new SimpleWorldComponent(scheduler, NAME, onEnd);
        manager = new GameStateManager(onEnd);
        spectateManager = new GameModeSpectateComponent(playerHolder);
        spawnManager = new SimpleSpawnManager(worldManager, playerHolder);

        addComponent(worldManager, manager, spectateManager, spawnManager);

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

        // Spawning players
        manager.add(new SpawnAtComponent(spawnManager, spectateManager.getNonSpectateHolder()), GameState.PRE_GAME);

        // Runner Game
        manager.add(new RunnerComponent(spectateManager.getNonSpectateHolder(), scheduler), GameState.GAME);
        manager.add(new FallingBlockKiller(worldManager), GameState.GAME, GameState.POST_GAME);


        //Periods
        manager.add(new TempPlayerCountdown(playerHolder, scheduler, 10, manager.getSetStateRunnable(GameState.PRE_GAME), 2, 1), GameState.LOBBY);
        manager.add(new TempCountdown(playerHolder, scheduler, 5, manager.getSetStateRunnable(GameState.GAME)), GameState.PRE_GAME);
        manager.add(new LMSVictoryCondition(spectateManager.getNonSpectateHolder(), playerHolder, manager.getSetStateRunnable(GameState.POST_GAME)), GameState.PRE_GAME, GameState.GAME);
        manager.add(new TempCountdown(playerHolder, scheduler, 5, manager.getSetStateRunnable(GameState.POST_GAME)), GameStateManager.DEAD_STATE);


    }
}
