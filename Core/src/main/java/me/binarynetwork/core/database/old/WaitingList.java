package me.binarynetwork.core.database.old;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/8/2016.
 */
public class WaitingList<T> {

    private ConcurrentLinkedQueue<Consumer<T>> queue;
    private AtomicBoolean enabled;

    public WaitingList()
    {
        this.queue = new ConcurrentLinkedQueue<>();
        this.enabled = new AtomicBoolean(false);
    }

    public boolean add(Consumer<T> callback)
    {
        queue.add(callback);
        return !enabled.getAndSet(true);
    }

    public boolean enable()
    {
        return enabled.getAndSet(true);
    }

    public boolean isQueuing()
    {
        return enabled.get();
    }

    public void callQueue(T t)
    {
        for (Consumer<T> callback : queue)
        {
            callback.accept(t);
        }
        queue.clear();
        enabled.set(false);
    }


}
