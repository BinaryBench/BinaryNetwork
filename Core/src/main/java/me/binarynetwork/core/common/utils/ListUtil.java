package me.binarynetwork.core.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bench on 8/30/2016.
 */
public class ListUtil {

    private ListUtil()
    {
    }

    public boolean isIterable(Object obj)
    {
        return obj instanceof Iterable || obj.getClass().isArray();
    }

    @SafeVarargs
    public static <T> List<T> joinLists(Collection<T> collection, Collection<T>... collections)
    {
        List<T> list = new ArrayList<>(collection);

        for (Collection<T> collection1 : collections)
        {
            list.addAll(collection1);
        }

        return list;
    }

    public static <T> List<T> append(T single, T[] array)
    {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        list.add(single);

        return list;
    }


}