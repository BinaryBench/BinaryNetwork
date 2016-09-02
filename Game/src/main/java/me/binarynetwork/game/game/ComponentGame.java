package me.binarynetwork.game.game;

import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.component.Component;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Bench on 9/2/2016.
 */
public class ComponentGame extends BaseComponent {

    private Map<Pair<Integer, Integer>, Component> components = new HashMap<>();

    @Override
    public boolean enable()
    {
        if (!super.enable())
            return false;

        Map<Pair<Integer, Integer>, Component> sorted = new TreeMap<Pair<Integer, Integer>, Component>((o1, o2) -> {
            return o1.getLeft().compareTo(o2.getLeft());
        });
        sorted.putAll(components);
        sorted.values().forEach(Component::enable);

        return true;
    }

    @Override
    public boolean disable()
    {
        if (!super.disable())
            return false;

        Map<Pair<Integer, Integer>, Component> sorted = new TreeMap<Pair<Integer, Integer>, Component>((o1, o2) -> {
            return o1.getRight().compareTo(o2.getRight());
        });

        sorted.putAll(components);
        sorted.values().forEach(Component::disable);
        return true;
    }


    protected void addComponent(Component component)
    {
        addComponent(component, 0, 0);
    }

    protected void addComponent(Component component, Integer startPriority)
    {
        addComponent(component, startPriority, 0);
    }

    protected void addComponent(Component component, Integer startPriority, Integer endPriority)
    {
        components.put(Pair.of(startPriority, endPriority), component);
    }



}
