package me.binarynetwork.core.common;

/**
 * Created by Bench on 10/3/2016.
 */
public class NullableRunnable implements Runnable {
    private Runnable runnable;

    public NullableRunnable(Runnable runnable)
    {
        this.runnable = runnable;
    }

    @Override
    public void run()
    {
        if (runnable != null)
            runnable.run();
    }
}
