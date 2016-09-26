package me.binarynetwork.game.countdown;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerAddEvent;
import me.binarynetwork.core.playerholder.events.PlayerRemoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/2/2016.
 */
public class TempPlayerCountdown extends TempCountdown implements Listener {
    private int startThreshold;
    private int stopThreshold;
    private int startCount;

    public TempPlayerCountdown(PlayerHolder playerHolder, ScheduledExecutorService scheduler, int startCount, Runnable onEnd, int startThreshold, int stopThreshold)
    {
        super(playerHolder, scheduler, startCount, onEnd);
        this.startCount = startCount;
        this.startThreshold = startThreshold;
        this.stopThreshold = stopThreshold;
    }

    public void checkCountdown(int playerCount)
    {
        if (playerCount >= this.startThreshold)
        {
            if (!getCountdown().isRunning())
            {
                getCountdown().stop();
                getCountdown().setCount(startCount);
                getCountdown().start();
            }
        }
        else if (playerCount < this.stopThreshold)
        {
            if (getCountdown().isRunning())
                getCountdown().stop();
        }
    }

    @EventHandler
    public void playerAdd(PlayerAddEvent event)
    {
        if (event.getPlayerHolder() != getPlayerHolder())
            return;
        checkCountdown(event.getPostPlayers().size());
    }

    @EventHandler
    public void playerRemove(PlayerRemoveEvent event)
    {
        if (event.getPlayerHolder() != getPlayerHolder())
            return;
        checkCountdown(event.getPostPlayers().size());
    }

    @Override
    public void onEnable()
    {
        ServerUtil.registerEvents(this);
        this.checkCountdown(getPlayerHolder().getPlayers().size());
    }

    @Override
    public void onDisable()
    {
        ServerUtil.unregisterEvents(this);
        super.onDisable();
    }


}
