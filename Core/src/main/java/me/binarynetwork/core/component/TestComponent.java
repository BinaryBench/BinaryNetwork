package me.binarynetwork.core.component;

/**
 * Created by Bench on 8/30/2016.
 */
public class TestComponent extends BaseComponent {
    private String name;

    public TestComponent(String name)
    {
        this.name = name;
    }

    @Override
    public void onEnable()
    {
        System.out.println("Enabled:  " + name);
    }

    @Override
    public void onDisable()
    {
        System.out.println("Disabled: " + name);
    }
}
