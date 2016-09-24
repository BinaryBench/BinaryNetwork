package me.binarynetwork.hub;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.LocationUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.world.PlayerDataPurgeComponent;
import me.binarynetwork.core.entity.EntityManager;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMob;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMobs;
import me.binarynetwork.core.entity.controllablemobs.api.ai.AIType;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AILookAtEntity;
import me.binarynetwork.core.entity.custom.CustomEntity;
import me.binarynetwork.core.entity.custom.CustomEntityManager;
import me.binarynetwork.core.entity.custom.types.frozen.FrozenSkeleton;
import me.binarynetwork.core.entity.custom.types.frozen.FrozenVillager;
import me.binarynetwork.core.entity.custom.types.frozen.FrozenZombie;
import me.binarynetwork.core.portal.commands.ServerCommand;
import me.binarynetwork.hub.world.HubWorldManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Bench on 8/31/2016.
 */
public class Main extends BinaryNetworkPlugin implements Listener {

    @Override
    public void enable()
    {
        HubWorldManager hubWorldManager = new HubWorldManager(getServerPlayerHolder());
        getComponentWrapper().addComponent(new PlayerDataPurgeComponent(world -> true, getScheduler()));

        getCommandManager().addCommand(new ServerCommand(getPermissionManager(), "game"), "Game");
        ServerUtil.registerEvents(this);
    }

    @EventHandler
    public void onBlockRightClick(PlayerInteractEvent event) {
        if(event.getAction()== Action.RIGHT_CLICK_BLOCK) {

            CustomEntity entity = null;
            Location loc = LocationUtil.centerOnBlock(event.getClickedBlock().getLocation().add(0, 1, 0));
            World world = loc.getWorld();
            switch (event.getPlayer().getItemInHand().getType())
            {
                case ROTTEN_FLESH: entity = new FrozenZombie(world); break;
                case BONE: entity = new FrozenSkeleton(world); break;
                case EMERALD: entity = new FrozenVillager(world); break;
                default: return;
            }

            LivingEntity livingEntity = (LivingEntity) EntityManager.spawnCustom(loc, entity.getEntity()).getBukkitEntity();
            ControllableMob<LivingEntity> controllableMob = ControllableMobs.control(livingEntity);

            controllableMob.getAI().clear();
            //controllableMob.getAI().addBehavior()



            event.getPlayer().sendMessage("Entity: " + controllableMob.getEntity().getClass().getSimpleName());
            //controllableMob.getAI().clear();
        }
    }
}
