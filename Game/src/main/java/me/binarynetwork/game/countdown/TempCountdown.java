package me.binarynetwork.game.countdown;

import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/2/2016.
 */
public class TempCountdown extends BaseComponent implements Runnable {

    private Runnable onEnd;
    private PlayerHolder playerHolder;
    private Countdown countdown;

    public TempCountdown(PlayerHolder playerHolder, ScheduledExecutorService scheduler, int startCount, Runnable onEnd)
    {
        this.onEnd = onEnd;
        this.playerHolder = playerHolder;
        this.countdown = new Countdown(scheduler, this, startCount);
    }

    @Override
    public void onEnable()
    {
        getCountdown().start();
    }

    @Override
    public void onDisable()
    {
        getCountdown().stop();
    }


    @Override
    public void run()
    {
        if (getCountdown().getCount() > 0)
            ServerUtil.broadcast("Something is happening in " + getCountdown().getCount() + "s!");
        else
        {
            ServerUtil.broadcast("Something is happening!");
            getOnEnd().run();
            getCountdown().stop();
        }
    }

    public Runnable getOnEnd()
    {
        return onEnd;
    }

    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }

    public Countdown getCountdown()
    {
        return countdown;
    }
}
