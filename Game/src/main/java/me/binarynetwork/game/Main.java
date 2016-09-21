package me.binarynetwork.game;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.world.PlayerDataPurgeComponent;
import me.binarynetwork.game.arena.Arena;
import me.binarynetwork.game.factory.SimpleGameFactory;
import me.binarynetwork.game.lobby.LobbyWorldComponent;
import me.binarynetwork.game.playerholder.GamePlayerHolder;
import me.binarynetwork.game.playerholder.SimpleGamePlayerHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 8/31/2016.
 */
public class Main extends BinaryNetworkPlugin implements Listener {

    private Arena arena;
    private GamePlayerHolder gamePlayerHolder;
    private LobbyWorldComponent lobbyWorldComponent;

    @Override
    public void enable()
    {
        getComponentWrapper().addComponent(new PlayerDataPurgeComponent(world -> true, getScheduler()));

        gamePlayerHolder = new SimpleGamePlayerHolder();
        lobbyWorldComponent = new LobbyWorldComponent(getScheduler(), () -> ServerUtil.shutdown("An error occurred in the lobbyWorldComponent"));
        SimpleGameFactory gameFactory = new SimpleGameFactory(gamePlayerHolder, getScheduler(), lobbyWorldComponent);
        arena = new Arena(gameFactory);
        registerEvents(this);
        arena.enable();


        //TestCommand testCommand = new TestCommand();
        //getCommand("test").setExecutor(testCommand);
        //getCommand("test").setTabCompleter(testCommand);
    }

    @Override
    public void disable()
    {
        //Disabling these cause a "Plugin attempted to register task while disabled"
        //arena.disable();
        //lobbyWorldComponent.disable();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if (gamePlayerHolder.canJoin(event.getPlayer()))
            gamePlayerHolder.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        gamePlayerHolder.removePlayer(event.getPlayer());
    }


}
