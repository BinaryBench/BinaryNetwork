package me.binarynetwork.core;

import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoDamage;
import me.binarynetwork.core.currency.Currency;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bench on 9/4/2016.
 */
public class Test {
    public static void main(String[] args)
    {
        Object object = new Object();

        Set<Pair<Object, Currency>> pairs = new HashSet<>();

        pairs.add(Pair.of(object, Currency.COSMETIC));

        System.out.printf("Contains: %s", pairs.contains(Pair.of(object, Currency.COSMETIC)));

    }
}
