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

    public static final Object DEAD_STATE = new Object();

    private Object currentState = DEAD_STATE;

    private Runnable onEnd;

    private Map<Component, Collection<Object>> components = new LinkedHashMap<>();


    public GameStateManager(Runnable onEnd)
    {
        this.onEnd = onEnd;
    }

    public void end()
    {
        setCurrentState(DEAD_STATE);
    }

    public void setCurrentState(Object toGameState)
    {

        GameStateChangeEvent event = new GameStateChangeEvent(this, getCurrentState(), toGameState);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        this.currentState = toGameState;

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

    public Object getCurrentState()
    {
        return currentState;
    }

    public Map<Component, Collection<Object>> getComponents()
    {
        return components;
    }

    public Runnable getSetStateRunnable(Object state)
    {
        return () -> setCurrentState(state);
    }


    public boolean add(Component component, Object state, Object... states)
    {
        return add(Collections.singleton(component), ListUtil.append(state, states));
    }

    public boolean add(Component component, Collection<Object> states)
    {
        return add(Collections.singleton(component), states);
    }

    public boolean add(Collection<Component> components, Object state, Object... states)
    {
        return add(components, ListUtil.append(state, states));
    }

    public boolean add(Collection<Component> components, Object[] states)
    {
        return add(components, Arrays.asList(states));
    }

    public boolean add(Collection<Component> componentsToAdd, Collection<Object> states)
    {
        boolean modified = false;

        states.remove(DEAD_STATE);

        for (Component component : componentsToAdd)
        {
            if (components.put(component, states) != states)
                modified = true;
        }

        return modified;
    }


    public boolean add(Map<Component, Collection<Object>> map)
    {
        boolean modified = false;

        for (Entry<Component, Collection<Object>> entry : map.entrySet())
        {
            if (add(entry.getKey(), entry.getValue()))
                modified = true;
        }
        return modified;
    }

    public boolean remove(Component component)
    {
        return remove(Collections.singleton(component));
    }

    public boolean remove(Collection<Component> componentsToRemove)
    {
        boolean modified = false;

        for (Component component : componentsToRemove)
            if (components.remove(component) != null)
                modified = true;

        return modified;
    }

}
