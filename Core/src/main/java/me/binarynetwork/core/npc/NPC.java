package me.binarynetwork.core.npc;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.entity.EntityManager;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMob;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMobs;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AILookAtEntity;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AIRandomLookaround;
import me.binarynetwork.core.entity.custom.CustomEntity;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/24/2016.
 */
public abstract class NPC implements Listener {

    private LivingEntity entity;
    private Consumer<Player> interact;


    public NPC(CustomEntity customEntity, Location location)
    {
        this(customEntity, location, null, null);
    }

    public NPC(CustomEntity customEntity, Location location, Consumer<Player> interact)
    {
        this(customEntity, location, null, interact);
    }

    public NPC(@Nonnull CustomEntity customEntity, @Nonnull Location location, @Nullable String name, @Nullable Consumer<Player> interact)
    {
        LivingEntity livingEntity = (LivingEntity) EntityManager.spawnCustom(location, customEntity.getEntity()).getBukkitEntity();
        ControllableMob<LivingEntity> controllableMob = ControllableMobs.control(livingEntity, true);
        controllableMob.getAI().addBehavior(new AILookAtEntity(0, null, null, 0.2f));
        controllableMob.getAI().addBehavior(new AIRandomLookaround(1));

        if (name != null)
            EntityManager.setName(livingEntity, name);

        this.entity = livingEntity;
        this.interact = interact;
        ServerUtil.registerEvents(this);
    }

    @EventHandler
    public void combust(EntityCombustEvent event)
    {
        if (entity.equals(event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEntityEvent event)
    {
        if (entity.equals(event.getRightClicked()))
        {
            interact(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void punch(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player && entity.equals(event.getEntity()))
        {
            event.setCancelled(true);
            interact((Player) event.getDamager());
        }
    }

    private void interact(Player player)
    {
        if (interact != null)
            interact.accept(player);
    }

    public void kill()
    {
        EntityManager.removeName(entity);
        ControllableMobs.releaseIfControlled(entity);
        entity.remove();
        ServerUtil.unregisterEvents(this);
    }
}
