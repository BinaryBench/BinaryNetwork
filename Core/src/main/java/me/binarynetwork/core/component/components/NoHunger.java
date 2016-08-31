package me.binarynetwork.core.component.components;

        import me.binarynetwork.core.playerholder.PlayerHolder;
        import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
        import org.bukkit.entity.Player;
        import org.bukkit.event.EventHandler;

        import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class NoHunger extends NoHungerChange {

    private PlayerHolder playerHolder;
    private int foodLevel;

    public NoHunger(PlayerHolder playerHolder)
    {
        this(playerHolder, 20);
    }

    public NoHunger(PlayerHolder playerHolder, int foodLevel)
    {
        super(playerHolder);
        this.playerHolder = playerHolder;
        this.foodLevel = foodLevel;
    }

    @Override
    public void onEnable()
    {
        getPlayerHolder().forEach(this::setHunger);
    }

    @EventHandler
    public void onAddPlayer(PlayerAddEvent event)
    {
        if (!event.getPlayerHolder().equals(getPlayerHolder()))
            return;
        setHunger(event.getPlayer());
    }

    private void setHunger(Player player)
    {
        player.setFoodLevel(getFoodLevel());
        player.setSaturation(getFoodLevel());
    }

    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }

    public int getFoodLevel()
    {
        return foodLevel;
    }
}
