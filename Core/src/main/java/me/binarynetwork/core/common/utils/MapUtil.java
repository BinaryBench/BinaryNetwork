package me.binarynetwork.core.common.utils;

import java.util.*;

/**
 * Created by Bench on 8/30/2016.
 */
public class MapUtil {

    public static <K, V> Map sortByValue(Map<K, V> unsortMap, Comparator<V> comparator) {

        // 1. Convert Map to List of Map
        List<Map.Entry<K, V>> list =
                new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, (o1, o2) -> (comparator.compare(o1.getValue(), o2.getValue())));

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }


        return sortedMap;
    }

}
