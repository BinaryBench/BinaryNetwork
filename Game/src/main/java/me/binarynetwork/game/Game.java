package me.binarynetwork.game;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.world.PlayerDataPurgeComponent;
import me.binarynetwork.core.portal.commands.ServerCommand;
import me.binarynetwork.game.arena.Arena;
import me.binarynetwork.game.factory.SimpleGameFactory;
import me.binarynetwork.game.lobby.LobbyWorldComponentOld;
import me.binarynetwork.game.lobby.LobbyWorldManager;
import me.binarynetwork.game.playerholder.GamePlayerHolder;
import me.binarynetwork.game.playerholder.SimpleGamePlayerHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Bench on 8/31/2016.
 */
public class Game extends BinaryNetworkPlugin implements Listener {

    private Arena arena;
    private GamePlayerHolder gamePlayerHolder;
    private LobbyWorldManager lobbyWorldComponent;

    @Override
    public void enable()
    {
        getComponentWrapper().addComponent(new PlayerDataPurgeComponent(world -> true, getScheduler()));
        lobbyWorldComponent = new LobbyWorldManager();
        getCommandManager().addCommand(new ServerCommand(getPermissionManager(), "hub"), "Hub", "Leave", "Quit");
    }

    @Override
    public void postWorld()
    {
        gamePlayerHolder = new SimpleGamePlayerHolder();
        SimpleGameFactory gameFactory = new SimpleGameFactory(gamePlayerHolder, getScheduler(), lobbyWorldComponent);
        arena = new Arena(gameFactory);
        ServerUtil.registerEvents(this);
        arena.enable();
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
