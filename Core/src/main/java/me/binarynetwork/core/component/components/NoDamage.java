package me.binarynetwork.core.component.components;

import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class NoDamage extends ListenerComponent {

    private Predicate<Player> playerPredicate;

    public NoDamage(Predicate<Player> playerPredicate)
    {
        this.playerPredicate = playerPredicate;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player && playerPredicate.test((Player) event.getEntity()))
            event.setCancelled(true);
    }

    public Predicate<Player> getPlayerPredicate()
    {
        return playerPredicate;
    }
}
