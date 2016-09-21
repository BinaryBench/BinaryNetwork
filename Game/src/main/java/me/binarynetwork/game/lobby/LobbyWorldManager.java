package me.binarynetwork.game.lobby;

import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.LocationUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.*;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.WorldPlayerHolder;
import me.binarynetwork.core.world.DefaultWorldManager;
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
 * Created by Bench on 9/20/2016.
 */
public class LobbyWorldManager extends DefaultWorldManager implements Listener {

    private SimpleComponentWrapper componentWrapper;

    private PlayerHolder playerHolder;

    public LobbyWorldManager()
    {
        super(FileUtil.newFileIgnoreCase(ServerUtil.getPlugin().getDataFolder(), "LobbyWorld.zip"));

        this.playerHolder = new WorldPlayerHolder(this);
        this.componentWrapper = new SimpleComponentWrapper();

        getWrapper().addComponent(
                new NoDamage(playerHolder),
                new NoBlockPlace(player -> !player.getGameMode().equals(GameMode.CREATIVE) && playerHolder.test(player)),
                new NoBlockBreak(player -> !player.getGameMode().equals(GameMode.CREATIVE) && playerHolder.test(player)),
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

    public SimpleComponentWrapper getWrapper()
    {
        return componentWrapper;
    }
}
