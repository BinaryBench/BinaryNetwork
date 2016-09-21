package me.binarynetwork.hub.world;

import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.LocationUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.*;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.world.DefaultWorldManager;
import me.binarynetwork.hub.world.respawn.RespawnManager;
import me.binarynetwork.hub.world.respawn.respawns.DiagonalRespawn;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Arrays;

/**
 * Created by Bench on 9/17/2016.
 */
public class HubWorldManager extends DefaultWorldManager implements Listener {

    private SimpleComponentWrapper componentWrapper;

    private PlayerHolder playerHolder;

    public HubWorldManager(PlayerHolder playerHolder)
    {
        super(FileUtil.newFileIgnoreCase(ServerUtil.getPlugin().getDataFolder(), "HubWorld.zip"));

        this.playerHolder = playerHolder;
        this.componentWrapper = new SimpleComponentWrapper();

        ServerUtil.registerEvents(this);

        RespawnManager respawnManager = new RespawnManager(playerHolder, this, pair -> spawnPlayer(pair.getRight(), pair.getLeft()));
        respawnManager.addModule(new DiagonalRespawn());

        getWrapper().addComponent(
                respawnManager,
                new NoDamage(playerHolder),
                new NoBlockPlace(player -> !player.getGameMode().equals(GameMode.CREATIVE)),
                new NoBlockBreak(player -> !player.getGameMode().equals(GameMode.CREATIVE)),
                new WeatherComponent(this),
                new NoHunger(playerHolder)
        );

    }

    @Override
    public void worldLoad(World world)
    {
        world.setAutoSave(false);
        Arrays.asList("doFireTick", "doMobSpawning", "mobGriefing", "naturalRegeneration").forEach(string -> world.setGameRuleValue(string, "false"));
        world.setGameRuleValue("randomTickSpeed", "0");
        getWrapper().enable();
    }

    public void spawnPlayer(Location location, Player player)
    {
        //player.setGameMode(GameMode.ADVENTURE);
        PlayerUtil.resetPlayer(player);
        PlayerUtil.teleport(player, location);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        if (get() != null)
            return;
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "No hub world! Oh no! :o");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if (!playerHolder.test(event.getPlayer()))
            return;
        spawnPlayer(LocationUtil.centerOnBlock(get().getSpawnLocation()), event.getPlayer());
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    public SimpleComponentWrapper getWrapper()
    {
        return componentWrapper;
    }
}
