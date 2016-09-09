package me.binarynetwork.core.component;

/**
 * Created by Bench on 9/8/2016.
 */
public class SelfAddingListenerComponent extends ListenerComponent {
    public SelfAddingListenerComponent(ComponentWrapper componentWrapper)
    {
        componentWrapper.addComponent(this);
    }
}
