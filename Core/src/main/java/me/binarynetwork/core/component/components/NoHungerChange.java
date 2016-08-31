package me.binarynetwork.core.component.components;

import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class NoHungerChange extends ListenerComponent {

    private Predicate<Player> playerPredicate;

    public NoHungerChange(Predicate<Player> playerPredicate)
    {
        this.playerPredicate = playerPredicate;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if (event.getEntity() instanceof Player && getPlayerPredicate().test((Player) event.getEntity()))
            event.setCancelled(true);
    }

    public Predicate<Player> getPlayerPredicate()
    {
        return playerPredicate;
    }
}
