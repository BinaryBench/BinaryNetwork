package me.binarynetwork.core.component;

/**
 * Created by Bench on 8/29/2016.
 */
public abstract class BaseComponent implements Component {

    private boolean enabled = false;

    @Override
    public boolean enable()
    {
        if (isEnabled())
            return false;
        onEnable();
        enabled = true;
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public boolean disable()
    {
        if (!isEnabled())
            return false;
        onDisable();
        enabled = false;
        return true;
    }

    public void onEnable()
    {

    }

    public void onDisable()
    {

    }

}
