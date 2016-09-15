package me.binarynetwork.core.currency;

import me.binarynetwork.core.currency.Currency;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bench on 9/14/2016.
 */
public class CurrencyToken {

    //Currency -> initialCurrency + newCurrency
    private Map<Currency, Integer> initialCurrencies;
    private Map<Currency, Integer> newCurrencies;

    public CurrencyToken(Map<Currency, Integer> initial)
    {
        initialCurrencies = new ConcurrentHashMap<>(initial);
        newCurrencies = new ConcurrentHashMap<>(initial);
    }

    public void resetInitialCurrency(Currency currency)
    {
        Integer amount = newCurrencies.get(currency);
        if (amount == null)
            return;
        initialCurrencies.put(currency, amount);
    }

    public void resetInitialCurrencies()
    {
        initialCurrencies.clear();
        initialCurrencies.putAll(getNewCurrencies());
    }

    public void setCurrency(Currency currency, int amount)
    {
        newCurrencies.put(currency, amount);
    }

    public int getCurrency(Currency currency)
    {
        return newCurrencies.getOrDefault(currency, 0);
    }

    public Map<Currency, Integer> getNewCurrencies()
    {
        return newCurrencies;
    }

    public Map<Currency, Integer> getInitialCurrencies()
    {
        return initialCurrencies;
    }


}
