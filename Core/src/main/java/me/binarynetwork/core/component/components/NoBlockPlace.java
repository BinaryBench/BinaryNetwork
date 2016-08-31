package me.binarynetwork.core.component.components;

import me.binarynetwork.core.common.item.TruePredicate;
import me.binarynetwork.core.common.utils.BlockUtil;
import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class NoBlockPlace extends ListenerComponent {

    private Predicate<Player> playerPredicate;
    private Predicate<Block> blockPredicate;

    /**
     *
     * @param playerPredicate What players should be affected.
     */
    public NoBlockPlace(Predicate<Player> playerPredicate)
    {
        this(playerPredicate, new TruePredicate<>(), null);
    }

    /**
     *
     * @param playerPredicate What players should be affected.
     * @param itemPredicate Which blocks should be affected.
     */
    public NoBlockPlace(Predicate<Player> playerPredicate, Predicate<ItemStack> itemPredicate)
    {
        this(playerPredicate, block -> itemPredicate.test(BlockUtil.toItemStack(block)), null);
    }

    /**
     *
     * @param playerPredicate What players should be affected.
     * @param blockPredicate Which blocks should be affected.
     * @param typeDefiner This is to differentiate from {@link NoBlockBreak#NoBlockBreak(Predicate, Predicate)}.
     */
    public NoBlockPlace(Predicate<Player> playerPredicate, Predicate<Block> blockPredicate, Object typeDefiner)
    {
        this.playerPredicate = playerPredicate;
        this.blockPredicate = blockPredicate;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        if (playerPredicate.test(event.getPlayer()) && blockPredicate.test(event.getBlock()))
            event.setCancelled(true);
    }

}
