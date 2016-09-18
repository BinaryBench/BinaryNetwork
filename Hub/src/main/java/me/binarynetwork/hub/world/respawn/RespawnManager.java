package me.binarynetwork.hub.world.respawn;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Bench on 9/17/2016.
 */
public class RespawnManager extends BaseComponent implements Runnable {

    private int id;

    private PlayerHolder playerHolder;
    private Predicate<World> worldPredicate;

    private List<RespawnModule> modules;
    private Consumer<Pair<Player, Location>> respawn;


    public RespawnManager(PlayerHolder playerHolder, Predicate<World> worldPredicate, Consumer<Pair<Player, Location>> respawn)
    {
        this.playerHolder = playerHolder;
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
        this.id = Bukkit.getScheduler().runTaskTimer(BinaryNetworkPlugin.getPlugin(), this, 2, 2).getTaskId();
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTask(this.id);
    }




    @Override
    public void run()
    {
        for (Player player : getPlayerHolder())
        {
            for (RespawnModule respawnModule : modules)
            {
                Pair<Location, Boolean> check = respawnModule.check(player);

                if (check != null)
                {
                    if (check.getLeft() != null)
                    {
                        getRespawn().accept(Pair.of(player, check.getLeft()));
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


    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }

    public Predicate<World> getWorldPredicate()
    {
        return worldPredicate;
    }

    public Consumer<Pair<Player, Location>> getRespawn()
    {
        return respawn;
    }
}