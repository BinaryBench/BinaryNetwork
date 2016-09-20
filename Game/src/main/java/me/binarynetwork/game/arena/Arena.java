package me.binarynetwork.game.arena;

import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.component.Component;
import me.binarynetwork.game.factory.GameFactory;

/**
 * Created by Bench on 9/3/2016.
 */
public class Arena implements Component, Runnable {
    private GameFactory gameFactory;

    private Component currentGame;

    public Arena(GameFactory gameFactory)
    {
        this.gameFactory = gameFactory;
    }

    @Override
    public boolean enable()
    {
        if (isEnabled())
            return false;
        currentGame = getGameFactory().getGame(this);
        currentGame.enable();
        return true;
    }
    @Override
    public boolean disable()
    {
        if (!isEnabled())
            return false;
        currentGame.disable();
        currentGame = null;
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return currentGame != null;
    }

    @Override
    public void run()
    {
        if (!isEnabled())
            return;
        currentGame.disable();
        currentGame = getGameFactory().getGame(this);
        currentGame.enable();
        ServerUtil.broadcast("Game " + currentGame.getClass().getSimpleName());
    }


    public GameFactory getGameFactory()
    {
        return gameFactory;
    }
}
