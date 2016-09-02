package me.binarynetwork.game.gamestate;

import me.binarynetwork.core.component.TestComponent;

/**
 * Created by Bench on 9/2/2016.
 */
public class GameState {
    public static final GameState LOBBY = new GameState();
    public static final GameState PRE_GAME = new GameState();
    public static final GameState GAME = new GameState();
    public static final GameState POST_GAME = new GameState();

    private GameState()
    {

    }
}
