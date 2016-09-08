package me.binarynetwork.core;

import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoDamage;

import java.util.Arrays;

/**
 * Created by Bench on 9/4/2016.
 */
public class Test {
    public static void main(String[] args)
    {
        new SimpleComponentWrapper().addComponent(new NoDamage(null));
    }
}
