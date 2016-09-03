package me.binarynetwork.game.games.spleef.components;

import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

/**
 * Created by Bench on 9/3/2016.
 */
public class SpleefComponent extends ListenerComponent {

    public static final int foodChange = 1;

    private PlayerHolder playerHolder;
    private Predicate<World> worldPredicate;

    public SpleefComponent(PlayerHolder playerHolder, Predicate<World> worldPredicate)
    {
        this.playerHolder = playerHolder;
        this.worldPredicate = worldPredicate;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();


        if (!getPlayerHolder().test(player))
            return;

        World world = event.getClickedBlock().getWorld();

        if (world == null)
            return;

        if (!getWorldPredicate().test(world))
            return;


        if (!(event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;



        Block block = event.getClickedBlock();

        if (block.getType().equals(Material.TNT)) {
            TNTPrimed tnt = (TNTPrimed) world.spawnEntity(block.getLocation().add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(30);
            block.setType(Material.AIR);
            return;
        }

        if (player.getFoodLevel() + foodChange >= 20)
            player.setFoodLevel(20);
        else
            player.setFoodLevel(player.getFoodLevel() + foodChange);

        block.getLocation().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
        block.setType(Material.AIR);

    }


    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event)
    {

        World world = event.getLocation().getWorld();

        if (!getWorldPredicate().test(world))
            return;

        event.setCancelled(true);

        double blockFlyChance = 0.3;

        for (Block block : event.blockList()) {

            if (Math.random() > blockFlyChance) {
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                block.setType(Material.AIR);
                continue;
            }
            if (block.getType().equals(Material.TNT)) {
                block.setType(Material.AIR);
                TNTPrimed tnt = (TNTPrimed) world.spawnEntity(block.getLocation().add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);

                tnt.setFuseTicks(5);

                tnt.setVelocity(bounceVector());
                block.setType(Material.AIR);
            }
            bounceBlock(block);
        }

        final double amount = 4;

        for (Player player : getPlayerHolder()) {

            Location first_location = event.getLocation();

            Location second_location = player.getLocation();

            Vector from = new Vector(first_location.getX(), first_location.getY(), first_location.getZ());

            Vector to = new Vector(second_location.getX(), second_location.getY(), second_location.getZ());

            Vector vector = to.subtract(from);

            double magnitude = amount / (Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));

            if (magnitude <= 0.05)
                continue;

            vector = vector.multiply(magnitude);

            player.setVelocity(vector);
        }
    }

    @SuppressWarnings("deprecation")
    public void bounceBlock(Block block)
    {
        if (block == null)
            return;
        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
        fallingBlock.setDropItem(false);
        fallingBlock.setVelocity(bounceVector());
        block.setType(Material.AIR);
    }

    public Vector bounceVector()
    {
        float x = (float) (Math.random() - 0.5);
        float y = (float) (Math.random() - 0.2);
        float z = (float) (Math.random() - 0.5);
        return new Vector(x, y, z);
    }



    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }

    public Predicate<World> getWorldPredicate()
    {
        return worldPredicate;
    }
}
