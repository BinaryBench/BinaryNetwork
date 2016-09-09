package me.binarynetwork.core;

import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoDamage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bench on 9/4/2016.
 */
public class Test {
    public static void main(String[] args)
    {
        Map<String, Integer> map = new ConcurrentHashMap<>();

        map.put("moo", 1);

        int i = map.get("");

        System.out.println(i);
    }
}
