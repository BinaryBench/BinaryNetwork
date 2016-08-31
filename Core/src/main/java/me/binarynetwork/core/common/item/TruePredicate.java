package me.binarynetwork.core.common.item;

import java.util.function.Predicate;

/**
 * Created by Bench on 8/29/2016.
 */
public class TruePredicate<T> implements Predicate<T> {
    @Override
    public boolean test(T t)
    {
        return true;
    }
}
