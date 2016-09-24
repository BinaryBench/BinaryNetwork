package me.binarynetwork.hub.world;

import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.world.DefaultWorldManager;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.Arrays;

/**
 * Created by Bench on 9/24/2016.
 */
public class HubWorldManager extends DefaultWorldManager implements Listener {

    public HubWorldManager()
    {
        super(FileUtil.newFileIgnoreCase(ServerUtil.getPlugin().getDataFolder(), "HubWorld.zip"));
    }

    @Override
    public void worldLoad(World world)
    {
        world.setAutoSave(false);
        Arrays.asList("doFireTick", "doMobSpawning", "mobGriefing", "naturalRegeneration").forEach(string -> world.setGameRuleValue(string, "false"));
        world.setGameRuleValue("randomTickSpeed", "0");
    }
}
