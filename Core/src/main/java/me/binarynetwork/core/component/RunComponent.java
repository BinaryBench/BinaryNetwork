package me.binarynetwork.core.component;

/**
 * Created by Bench on 9/3/2016.
 */
public class RunComponent extends BaseComponent {

    private Runnable runnable;
    private boolean runAfter;

    public RunComponent(Runnable runnable)
    {
        this(runnable, false);
    }

    public RunComponent(Runnable runnable, boolean runAfter)
    {
        this.runnable = runnable;
        this.runAfter = runAfter;
    }


    @Override
    public void onEnable()
    {
        if (!runAfter)
            runnable.run();
    }

    @Override
    public void onDisable()
    {
        if (runAfter)
            runnable.run();
    }
}