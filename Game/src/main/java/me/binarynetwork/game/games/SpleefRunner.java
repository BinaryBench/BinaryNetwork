package me.binarynetwork.game.games;

import me.binarynetwork.core.common.utils.FallingBlockKiller;
import me.binarynetwork.core.component.RunComponent;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.*;
import me.binarynetwork.core.component.runnables.GameModeRunnable;
import me.binarynetwork.core.component.world.GameWorldComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.game.countdown.TempCountdown;
import me.binarynetwork.game.countdown.TempPlayerCountdown;
import me.binarynetwork.game.games.runner.components.RunnerComponent;
import me.binarynetwork.game.games.spleef.components.SpleefComponent;
import me.binarynetwork.game.gamestate.GameState;
import me.binarynetwork.game.gamestate.GameStateManager;
import me.binarynetwork.game.lobby.LobbyComponent;
import me.binarynetwork.game.spawn.SimpleSpawnManager;
import me.binarynetwork.game.spawn.runnables.SpawnAtRunnable;
import me.binarynetwork.game.spectate.GameModeSpectateComponent;
import me.binarynetwork.game.spectate.components.DeathSpectate;
import me.binarynetwork.game.spectate.components.JoinSpectate;
import me.binarynetwork.game.victorycondition.LMSVictoryCondition;
import org.bukkit.GameMode;
import org.bukkit.World;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/20/2016.
 */
public class SpleefRunner extends SimpleComponentWrapper {
    public static final String NAME = "Spleef";

    GameStateManager manager;
    GameModeSpectateComponent spectateManager;
    GameWorldComponent worldManager;
    SimpleSpawnManager spawnManager;

    public SpleefRunner(PlayerHolder playerHolder, ScheduledExecutorService scheduler, Supplier<World> lobbyWorldComponent, Runnable onEnd)
    {
        worldManager = new GameWorldComponent(scheduler, NAME, onEnd);
        manager = new GameStateManager(onEnd);
        spectateManager = new GameModeSpectateComponent(playerHolder);
        spawnManager = new SimpleSpawnManager(worldManager, playerHolder);

        addComponent(worldManager, manager, spectateManager, spawnManager, new RunComponent(manager.getSetStateRunnable(GameState.LOBBY)));

        //Disable weather
        addComponent(new WeatherComponent(worldManager));

        //Lobby parkours & events
        manager.add(new LobbyComponent(playerHolder, lobbyWorldComponent), GameState.LOBBY);



        //All game
        manager.add(Arrays.asList(
                //Spectate
                new JoinSpectate(spectateManager),
                new DeathSpectate(spectateManager),

                //Keep playerdata in world
                new KeepInWorld(playerHolder, worldManager),

                //Standard-ish stuff
                new NoBlockBreak(playerHolder),
                new NoBlockPlace(playerHolder),
                new NoDropItem(playerHolder),
                new NoPickUpItem(playerHolder),
                new NoHunger(playerHolder),
                new VoidKiller(spectateManager.getNonSpectateHolder(), worldManager)//,

                //This Game
                //new NoDamage(playerHolder)

        ), GameState.PRE_GAME, GameState.GAME, GameState.POST_GAME);

        // Spawning playerdata
        manager.add(new RunComponent(new SpawnAtRunnable(spawnManager, spectateManager.getNonSpectateHolder())), GameState.PRE_GAME);
        manager.add(new RunComponent(new GameModeRunnable(spectateManager.getNonSpectateHolder(), GameMode.SURVIVAL)), GameState.PRE_GAME);

        // Runner Game
        manager.add(
                Arrays.asList(
                    new SpleefComponent(spectateManager.getNonSpectateHolder(), worldManager),
                    new RunnerComponent(spectateManager.getNonSpectateHolder(), scheduler)
                ),
                GameState.GAME);
        manager.add(new FallingBlockKiller(worldManager), GameState.GAME, GameState.POST_GAME);


        //Periods
        manager.add(new TempPlayerCountdown(playerHolder, scheduler, 10, manager.getSetStateRunnable(GameState.PRE_GAME), 1, 1), GameState.LOBBY);
        manager.add(new TempCountdown(playerHolder, scheduler, 5, manager.getSetStateRunnable(GameState.GAME)), GameState.PRE_GAME);
        manager.add(new LMSVictoryCondition(spectateManager.getNonSpectateHolder(), 1, playerHolder, manager.getSetStateRunnable(GameState.POST_GAME)), GameState.PRE_GAME, GameState.GAME);
        manager.add(new TempCountdown(playerHolder, scheduler, 5, manager.getSetStateRunnable(GameStateManager.DEAD_STATE)), GameState.POST_GAME);


    }
}