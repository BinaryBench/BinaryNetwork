package me.binarynetwork.core.component;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ListUtil;

import java.util.*;

/**
 * Created by Bench on 9/3/2016.
 */
public class SimpleComponentWrapper extends BaseComponent implements ComponentWrapper {

    private Collection<Component> components = new LinkedHashSet<>();

    public SimpleComponentWrapper(Component... components)
    {
        this.components.addAll(Arrays.asList(components));
    }


    public void addComponent(Component component, Component... components)
    {
        ListUtil.append(component, components).forEach(this::addComponent);
    }

    @Override
    public void addComponent(Component component)
    {
        if (components.add(component))
            if (isEnabled())
                component.enable();
    }

    @Override
    public void removeComponent(Component component)
    {
        if (components.remove(component))
            if (component.isEnabled())
                component.disable();
    }

    @Override
    public boolean enable()
    {
        if (super.enable())
        {
            components.forEach(Component::enable);
            return true;
        }
        return false;
    }

    @Override
    public boolean disable()
    {
        if (super.disable())
        {
            components.forEach(Component::disable);
            return true;
        }
        return false;
    }

}
