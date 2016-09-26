package me.binarynetwork.hub.respawn;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.playerholder.PlayerContainer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Created by Bench on 9/17/2016.
 */
public class RespawnManager extends BaseComponent implements Runnable {

    private int id;

    private PlayerContainer playerContainer;
    private Predicate<World> worldPredicate;

    private List<RespawnModule> modules;
    private BiConsumer<Player, Location> respawn;


    public RespawnManager(PlayerContainer playerContainer, Predicate<World> worldPredicate, BiConsumer<Player, Location> respawn)
    {
        this.playerContainer = playerContainer;
        this.worldPredicate = worldPredicate;
        this.respawn = respawn;
        this.modules = new LinkedList<>();
    }

    public void addModule(RespawnModule module)
    {
        this.modules.add(module);
    }

    @Override
    public void onEnable()
    {
        this.id = Bukkit.getScheduler().runTaskTimer(ServerUtil.getPlugin(), this, 2, 2).getTaskId();
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTask(this.id);
    }




    @Override
    public void run()
    {
        for (Player player : getPlayerContainer())
        {
            for (RespawnModule respawnModule : modules)
            {
                Pair<Location, Boolean> check = respawnModule.check(player);

                if (check != null)
                {
                    if (check.getLeft() != null)
                    {
                        getRespawn().accept(player, check.getLeft());
                        break;
                    }

                    if (check.getRight() != null)
                    {
                        if (check.getRight())
                        {
                            break;
                        }
                    }
                }
            }
        }
    }


    public PlayerContainer getPlayerContainer()
    {
        return playerContainer;
    }

    public Predicate<World> getWorldPredicate()
    {
        return worldPredicate;
    }

    public BiConsumer<Player, Location> getRespawn()
    {
        return respawn;
    }
}