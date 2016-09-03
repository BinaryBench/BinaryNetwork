package me.binarynetwork.core.component;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bench on 9/3/2016.
 */
public class ComponentWrapper extends BaseComponent {

    private List<Component> components = new ArrayList<>();

    public void addComponent(Component component, Component... components)
    {
        ListUtil.append(component, components).forEach(component1 -> {
            this.components.add(component1);
            if (isEnabled())
                component1.enable();
        });

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
