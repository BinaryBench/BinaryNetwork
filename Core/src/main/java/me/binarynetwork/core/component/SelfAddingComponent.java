package me.binarynetwork.core.component;

import me.binarynetwork.core.command.CommandWrapper;

/**
 * Created by Bench on 9/8/2016.
 */
public class SelfAddingComponent extends BaseComponent {

    public SelfAddingComponent(ComponentWrapper componentWrapper)
    {
        componentWrapper.addComponent(this);
    }
}
