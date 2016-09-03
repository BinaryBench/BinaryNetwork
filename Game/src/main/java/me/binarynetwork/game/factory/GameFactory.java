package me.binarynetwork.game.factory;

import me.binarynetwork.core.component.Component;

/**
 * Created by Bench on 9/3/2016.
 */
public interface GameFactory {
    Component getGame(Runnable onEnd);
}
