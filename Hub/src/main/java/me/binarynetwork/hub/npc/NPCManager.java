package me.binarynetwork.hub.npc;

import me.binarynetwork.core.common.utils.LocationUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.entity.EntityManager;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMob;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMobs;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AILookAtEntity;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AIPlayerTradingLook;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AIRandomLookaround;
import me.binarynetwork.core.entity.custom.CustomEntity;
import me.binarynetwork.core.entity.custom.types.frozen.FrozenZombie;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/24/2016.
 */
public class NPCManager implements Listener {

    private Predicate<World> worldPredicate;

    private Map<Entity, Consumer<Player>> map;

    public NPCManager(Predicate<World> worldPredicate)
    {
        this.worldPredicate = worldPredicate;
        map = new HashMap<>();
        ServerUtil.registerEvents(this);
    }




    @EventHandler
    public void onLoad(WorldLoadEvent event)
    {
        if (worldPredicate.test(event.getWorld()))
            createEntities(event.getWorld());
    }

    @EventHandler
    public void combust(EntityCombustEvent event)
    {
        if (map.containsKey(event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEntityEvent event)
    {
        if (map.containsKey(event.getRightClicked()))
        {
            interact(event.getPlayer(), event.getRightClicked());
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void punch(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player && map.containsKey(event.getEntity()))
        {
            event.setCancelled(true);
            interact((Player) event.getDamager(), event.getEntity());
        }
    }

    private void interact(Player player, Entity entity)
    {
        Consumer<Player> consumer = map.get(entity);
        if (consumer != null)
            consumer.accept(player);
    }


    private void createEntities(World world)
    {
        BiConsumer<Double, Double> consumer = (x, z) ->
        {
            serverEntity("game", new Location(world, x, 80, z));
        };
        double[] first = new double[]{2.5, -1.5};
        double[] second = new double[]{12.5, -11.5};

        corners(first, second, consumer);
        corners(second, first, consumer);

    }

    private void corners(double[] xs, double[] zs, BiConsumer<Double, Double> consumer)
    {
        for (double x : xs)
            for (double z : zs)
                consumer.accept(x, z);
    }

    public void serverEntity(String server, Location location)
    {
        map.put(createEntity(new FrozenZombie(location.getWorld()), location, ChatColor.GOLD + "Join " + StringUtils.capitalize(server) + " server"), player -> {
            PlayerUtil.sendToServer(player, server.toLowerCase());
            PlayerUtil.message(player, "sending to server...");
        });
    }

    private Entity createEntity(CustomEntity customEntity, Location location, String name)
    {
        LivingEntity livingEntity = (LivingEntity) EntityManager.spawnCustom(location, customEntity.getEntity()).getBukkitEntity();
        ControllableMob<LivingEntity> controllableMob = ControllableMobs.control(livingEntity, true);
        controllableMob.getAI().addBehavior(new AILookAtEntity(0, null, null, 0.2f));
        controllableMob.getAI().addBehavior(new AIRandomLookaround(1));

        EntityManager.setName(livingEntity, name);

        return livingEntity;
    }
}
