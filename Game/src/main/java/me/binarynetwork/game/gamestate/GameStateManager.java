package me.binarynetwork.game.gamestate;

import me.binarynetwork.core.common.Log;
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

    private boolean isStateChanging = false;
    private Runnable backloggedState = null;

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
        if (isStateChanging)
        {
            backloggedState = getSetStateRunnable(toGameState);
            return;
        }

        isStateChanging = true;
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
        {
            onEnd.run();
            return;
        }

        isStateChanging = false;
        if (backloggedState != null)
        {
            Runnable backlogged = backloggedState;
            backloggedState = null;
            backlogged.run();
        }






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

    //Add

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

        if (states.remove(DEAD_STATE))
            Log.debug("You cannot add components to the DEAD_STATE");

        for (Component component : componentsToAdd)
        {
            Collection<Object> enabledStates = components.get(component);
            if (enabledStates == null)
                components.put(component, enabledStates = new HashSet<>());

            if (enabledStates.addAll(states))
                modified = true;

            if (states.contains(getCurrentState()))
                component.enable();
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



    //Remove



    public boolean remove(Component component, Object state, Object... states)
    {
        return remove(Collections.singleton(component), ListUtil.append(state, states));
    }

    public boolean remove(Component component, Collection<Object> states)
    {
        return remove(Collections.singleton(component), states);
    }

    public boolean remove(Collection<Component> components, Object state, Object... states)
    {
        return remove(components, ListUtil.append(state, states));
    }

    public boolean remove(Collection<Component> components, Object[] states)
    {
        return remove(components, Arrays.asList(states));
    }

    public boolean remove(Collection<Component> componentsToRemove, Collection<Object> states)
    {
        boolean modified = false;

        for (Component component : componentsToRemove)
        {
            Collection<Object> enabledStates = components.get(component);
            if (enabledStates == null)
                components.put(component, enabledStates = new HashSet<>());

            if (enabledStates.removeAll(states))
                modified = true;

            if (states.contains(getCurrentState()))
                component.disable();

            if (enabledStates.isEmpty())
                components.remove(component);
        }

        return modified;
    }

    public boolean remove(Map<Component, Collection<Object>> map)
    {
        boolean modified = false;
        for (Entry<Component, Collection<Object>> entry : map.entrySet())
        {
            if (remove(entry.getKey(), entry.getValue()))
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
        {
            Collection<Object> enabledStates = components.get(component);

            if (enabledStates == null)
                continue;

            if (enabledStates.contains(getCurrentState()))
                component.disable();

            if (components.remove(component) != null)
                modified = true;


        }


        return modified;
    }

}
