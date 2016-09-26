package me.binarynetwork.core.component;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.ServerUtil;
import org.bukkit.event.Listener;

/**
 * Created by Bench on 8/29/2016.
 */
public abstract class ListenerComponent extends BaseComponent implements Listener {

    @Override
    public boolean enable()
    {
        if (super.enable())
        {
            ServerUtil.registerEvents(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean disable()
    {
        if (super.disable())
        {
            ServerUtil.unregisterEvents(this);
            return true;
        }
        return false;
    }
}
