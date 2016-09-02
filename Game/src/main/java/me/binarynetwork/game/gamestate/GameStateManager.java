package me.binarynetwork.game.gamestate;

import me.binarynetwork.core.common.utils.ListUtil;
import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.component.Component;
import me.binarynetwork.game.gamestate.events.GameStateChangeEvent;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Bench on 9/2/2016.
 */
public class GameStateManager extends BaseComponent {

    private static final Object DEAD_STATE = new Object();

    private Object gameState = DEAD_STATE;

    private Runnable onEnd;

    private Map<Component, Collection<Object>> components = new LinkedHashMap<>();


    public GameStateManager(Runnable onEnd)
    {
        this.onEnd = onEnd;
    }

    public void end()
    {
        setGameState(DEAD_STATE);
    }

    public void setGameState(Object toGameState)
    {

        GameStateChangeEvent event = new GameStateChangeEvent(this, getGameState(), toGameState);
        //Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        this.gameState = toGameState;

        List<Component> toDisable = new ArrayList<>();
        List<Component> toEnable = new ArrayList<>();

        for (Entry<Component, Collection<Object>> entry : getComponents().entrySet())
            if (entry.getValue().contains(toGameState) && !toGameState.equals(DEAD_STATE))
                toEnable.add(entry.getKey());
            else
                toDisable.add(entry.getKey());

        toDisable.forEach(Component::disable);
        toEnable.forEach(Component::enable);


        if (toGameState.equals(DEAD_STATE))
            onEnd.run();
    }

    public Object getGameState()
    {
        return gameState;
    }

    public Map<Component, Collection<Object>> getComponents()
    {
        return components;
    }

    public void add(Component component, Object State, Object... States)
    {
        components.put(component, ListUtil.append(State, States));
    }
}
