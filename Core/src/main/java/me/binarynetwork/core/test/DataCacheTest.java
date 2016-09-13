package me.binarynetwork.core.test;

import me.binarynetwork.core.database.DataCache;
import me.binarynetwork.core.database.DataCacheWithTemp;

import java.util.concurrent.*;
import java.util.function.*;

/**
 * Created by Bench on 9/12/2016.
 */
public class DataCacheTest extends DataCacheWithTemp<String, String> {

    public static void main(String[] args) throws Exception
    {
        DataCacheTest o = new DataCacheTest(Executors.newScheduledThreadPool(10));

        System.out.println("Print: " + o.getSync("test"));

        o.get("test", s -> {
            System.out.println("Consumer: " + s);
        });

        System.out.println(o.getSync("fail"));

        Thread.sleep(10000);

        System.out.println(o.getSync("fail"));
    }

    private boolean fail = true;

    public DataCacheTest(ScheduledExecutorService scheduler)
    {
        super(scheduler);
    }

    @Override
    public String loadValue(String key) throws Exception
    {
        return compile(key);
    }

    @Override
    public String loadTemp(String key)
    {
        return key + " temp!";
    }

    private String compile(String string) throws Exception
    {
        if (string.equalsIgnoreCase("fail") && fail)
        {
            fail = false;
            throw new Exception("Oh no it failed!");
        }

        int amount = 3;
        for (int i = 0; i < amount; i++)
        {

            System.out.println("Preparing " + string + ".  Currently at " + ((int) (((double)i/amount)*100)) + "%");
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Completed preparing " + string + "!\n");
        return string + " compiled";
    }

}
