package me.binarynetwork.core.test;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Created by Bench on 9/12/2016.
 */
public class CompletableFutureExample {

    public static void main(String[] args) throws Exception
    {
        CompletableFutureExample o = new CompletableFutureExample();


        System.out.println("Print: " + o.getSync("test"));

        o.get("test", s -> {
            System.out.println("Consumer: " + s);
        });
    }

    private ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, CompletableFuture<String>> futures = new ConcurrentHashMap<>();


    private String getSync(String key)
    {
        CompletableFuture<String> future = getFuture(key);
        if (future == null)
            return cache.get(key);
        try
        {
            return future.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void get(String key, Consumer<String> callback)
    {
        CompletableFuture<String> future = getFuture(key);
        if (future == null)
        {
            callback.accept(cache.get(key));
            return;
        }
        future.thenAccept(callback);
    }

    private CompletableFuture<String> getFuture(String key)
    {
        if (cache.containsKey(key))
            return null;

        CompletableFuture<String> future;
        if ((future = futures.get(key)) != null)
            return future;

        future = CompletableFuture.supplyAsync(() -> compile(key));
        future.thenAccept(s -> {
            cache.put(key, s);
        });
        futures.put(key, future);
        return future;
    }

    private String compile(String string)
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
