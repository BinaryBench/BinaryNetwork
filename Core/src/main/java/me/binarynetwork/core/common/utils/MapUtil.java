package me.binarynetwork.core.common.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bench on 8/30/2016.
 */
public class MapUtil {

    public static <K, V> LinkedHashMap<K, V> sortByValue(Map<K, V> unsortMap, Comparator<V> comparator) {

        // 1. Convert Map to List of Map
        List<Map.Entry<K, V>> list =
                new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, (o1, o2) -> (comparator.compare(o1.getValue(), o2.getValue())));

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        LinkedHashMap<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }


        return sortedMap;
    }

    public static <V> List<V> getValueStartsWith(Map<String, V> map, String search, boolean caseSensitive)
    {
        List<V> returnList = new ArrayList<>();

        for (Map.Entry<String, V> entry : map.entrySet())
        {

            if (entry.getKey().startsWith(search) || (entry.getKey().toLowerCase().startsWith(search.toLowerCase()) && !caseSensitive))
                returnList.add(entry.getValue());
        }
        return returnList;
    }

    public static <V> Map<String, V> getStartsWith(Map<String, V> map, String search, boolean caseSensitive)
    {
        Map<String, V> returnMap = new HashMap<String, V>();

        for (Map.Entry<String, V> entry : map.entrySet())
        {

            if (entry.getKey().startsWith(search) || (entry.getKey().toLowerCase().startsWith(search.toLowerCase()) && !caseSensitive))
                returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    public static <K, V> Collection<K> getKeys(Map<K, V> map, V value)
    {
        return map.entrySet().stream().filter(entry -> entry.getValue().equals(value)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

}
