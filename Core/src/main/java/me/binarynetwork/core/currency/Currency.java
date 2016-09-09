package me.binarynetwork.core.currency;

/**
 * Created by Bench on 9/9/2016.
 */
public enum Currency {

    GAME("game", "Gem"),
    COSMETIC("cos", "Coin");

    private String id;
    private String displayName;

    Currency(String id, String displayName)
    {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId()
    {
        return id;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public static Currency getById(String id)
    {
        for (Currency currency : values())
        {
            if (currency.getId().equalsIgnoreCase(id))
                return currency;
        }
        return null;
    }
}
