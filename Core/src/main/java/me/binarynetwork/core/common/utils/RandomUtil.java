package me.binarynetwork.core.common.utils;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Bench on 9/3/2016.
 */
public class RandomUtil {
    private RandomUtil()
    {
    }

    private static Random random = new Random();

    public static Random getRandom()
    {
        return random;
    }

    public static double randomDouble(double min, double max)
    {
        return min + (getRandom().nextDouble() * Math.abs(max - min));
    }

    public static <T> T randomElement(List<T> list)
    {
        return list.get(getRandom().nextInt(list.size()));
    }

    public static <T> T randomElement(Set<T> set)
    {
        int item = getRandom().nextInt(set.size());
        int i = 0;
        for(T obj : set)
        {
            if (i == item)
                return obj;
            i++;
        }
        //Should never happen!
        return null;
    }

    public static <T> T randomElement(T[] array)
    {
        return array[getRandom().nextInt(array.length)];
    }

}
