package me.binarynetwork.core;

import me.binarynetwork.core.permissions.Rank;

import java.util.Arrays;

/**
 * Created by Bench on 9/4/2016.
 */
public class Test {
    public static void main(String[] args)
    {
        String message = "/this is a test ";
        System.out.println(Arrays.toString(message.substring(1, message.length()).trim().replaceAll("( )+", " ").split(" ")));
    }
}
