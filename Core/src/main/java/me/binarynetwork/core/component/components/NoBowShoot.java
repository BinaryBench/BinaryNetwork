package me.binarynetwork.core.component.components;

import me.binarynetwork.core.common.item.TruePredicate;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class NoBowShoot extends ListenerComponent {

    private Predicate<Player> playerPredicate;

    public NoBowShoot(Predicate<Player> playerPredicate)
    {
        this.playerPredicate = playerPredicate;
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (getPlayerPredicate().test(player))
            event.setCancelled(true);
    }

    public Predicate<Player> getPlayerPredicate()
    {
        return playerPredicate;
    }
}
