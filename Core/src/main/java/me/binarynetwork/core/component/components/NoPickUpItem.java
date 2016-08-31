package me.binarynetwork.core.component.components;

import me.binarynetwork.core.common.item.TruePredicate;
import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class NoPickUpItem extends ListenerComponent {

    private Predicate<Player> playerPredicate;
    private Predicate<ItemStack> itemPredicate;

    public NoPickUpItem(Predicate<Player> playerPredicate)
    {
        this(playerPredicate, new TruePredicate<>());
    }

    public NoPickUpItem(Predicate<Player> playerPredicate, Predicate<ItemStack> itemPredicate)
    {
        this.playerPredicate = playerPredicate;
        this.itemPredicate = itemPredicate;
    }

    @EventHandler
    public void onDrop(PlayerPickupItemEvent event)
    {
        if (getPlayerPredicate().test(event.getPlayer()) && getItemPredicate().test(event.getItem().getItemStack()))
            event.setCancelled(true);
    }

    public Predicate<Player> getPlayerPredicate()
    {
        return playerPredicate;
    }

    public Predicate<ItemStack> getItemPredicate()
    {
        return itemPredicate;
    }
}