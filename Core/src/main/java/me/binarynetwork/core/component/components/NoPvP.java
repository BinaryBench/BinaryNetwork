package me.binarynetwork.core.component.components;

import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class NoPvP extends ListenerComponent {

    private Predicate<Player> playerPredicate;

    public NoPvP(Predicate<Player> playerPredicate)
    {
        this.playerPredicate = playerPredicate;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player && getPlayerPredicate().test((Player) event.getDamager()))
            event.setCancelled(true);
    }

    public Predicate<Player> getPlayerPredicate()
    {
        return playerPredicate;
    }
}
