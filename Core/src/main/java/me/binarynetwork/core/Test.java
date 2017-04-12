package me.binarynetwork.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by Bench on 9/4/2016.
 */
public class Test {
    public static void main(String[] args)
    {
        System.out.println("moo");

        BiMap<Integer, String> biMap = HashBiMap.create();

        biMap.put(1, "a");
        biMap.put(2, "a");
        biMap.put(3, "b");

        System.out.printf("1: %s\n", biMap.get(1));
        System.out.printf("a: %s\n", biMap.inverse().get("a"));

    }
}
