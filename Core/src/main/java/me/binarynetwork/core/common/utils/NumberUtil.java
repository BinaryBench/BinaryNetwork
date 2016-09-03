package me.binarynetwork.core.common.utils;

/**
 * Created by Bench on 9/3/2016.
 */
public class NumberUtil {

    private NumberUtil() {
    }

    public static boolean isInt(String str)
    {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String str)
    {
        try {
            Float.parseFloat(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str)
    {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}