package me.binarynetwork.core.common.utils;

import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/3/2016.
 */
public class FallingBlockKiller extends ListenerComponent {

    private Predicate<World> worldPredicate;

    public FallingBlockKiller(Predicate<World> worldPredicate)
    {
        this.worldPredicate = worldPredicate;
    }

    @EventHandler
    public void EntityChangeBlockEvent(EntityChangeBlockEvent event)
    {
        if (event.getEntityType().equals(EntityType.FALLING_BLOCK))
        {

            if (!worldPredicate.test(event.getBlock().getWorld()))
                return;

            if (event.getEntity() instanceof FallingBlock)
            {
                FallingBlock block = (FallingBlock) event.getEntity();

                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getMaterial());
            }
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }
}
