package me.binarynetwork.hub;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.LocationUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.components.*;
import me.binarynetwork.core.component.world.PlayerDataPurgeComponent;
import me.binarynetwork.core.entity.EntityManager;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMob;
import me.binarynetwork.core.entity.controllablemobs.api.ControllableMobs;
import me.binarynetwork.core.entity.custom.CustomEntity;
import me.binarynetwork.core.entity.custom.base.CustomZombie;
import me.binarynetwork.core.entity.custom.types.frozen.FrozenSkeleton;
import me.binarynetwork.core.entity.custom.types.frozen.FrozenVillager;
import me.binarynetwork.core.entity.custom.types.frozen.FrozenZombie;
import me.binarynetwork.core.portal.commands.ServerCommand;
import me.binarynetwork.hub.npc.NPCManager;
import me.binarynetwork.hub.respawn.RespawnManager;
import me.binarynetwork.hub.respawn.respawns.DiagonalRespawn;
import me.binarynetwork.hub.world.HubWorldManager;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Bench on 8/31/2016.
 */
public class Hub extends BinaryNetworkPlugin implements Listener {

    private HubWorldManager hubWorldManager;

    @Override
    public void enable()
    {
        hubWorldManager = new HubWorldManager();
        getComponentWrapper().addComponent(new PlayerDataPurgeComponent(world -> true, getScheduler()));



        //NPC
        new NPCManager(hubWorldManager);

        //Respawn
        RespawnManager respawnManager = new RespawnManager(getServerPlayerHolder(), hubWorldManager, this::spawnPlayer);
        respawnManager.addModule(new DiagonalRespawn());


        //Components
        getComponentWrapper().addComponent(
                respawnManager,
                new NoDamage(getServerPlayerHolder()),
                new NoBlockPlace(player -> !player.getGameMode().equals(GameMode.CREATIVE)),
                new NoBlockBreak(player -> !player.getGameMode().equals(GameMode.CREATIVE)),
                new WeatherComponent(hubWorldManager),
                new NoHunger(getServerPlayerHolder())
        );


        //Commands
        getCommandManager().addCommand(new ServerCommand(getPermissionManager(), "game"), "Game");

        ServerUtil.registerEvents(this);
    }


    public void spawnPlayer(Player player, Location location)
    {
        //player.setGameMode(GameMode.ADVENTURE);
        PlayerUtil.resetPlayer(player);
        PlayerUtil.teleport(player, location);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event)
    {
        if (hubWorldManager.get() != null)
            return;
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "No hub world! Oh no! :o");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        spawnPlayer(event.getPlayer(), LocationUtil.centerOnBlock(hubWorldManager.get().getSpawnLocation()));
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
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
            //controllableMob.getAI().clear();


            EntityManager.setName(livingEntity, livingEntity.getClass().getSimpleName());

        }
    }


}
