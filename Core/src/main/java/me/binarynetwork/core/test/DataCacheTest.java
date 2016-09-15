package me.binarynetwork.core.test;

import me.binarynetwork.core.database.DataCacheTemp;

import java.util.concurrent.*;

/**
 * Created by Bench on 9/12/2016.
 */
public class DataCacheTest {

    public static void main(String[] args) throws Exception
    {

    }

    public String loadTemp(String key)
    {
        return key + " temp!";
    }

    private String compile(String string) throws Exception
    {

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
