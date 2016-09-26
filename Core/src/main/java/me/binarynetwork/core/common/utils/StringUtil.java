package me.binarynetwork.core.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Bench on 9/9/2016.
 */
public class StringUtil {

    public static String spaceJoin(String... strings)
    {
        return join("", " ", "", strings);
    }


    public static String join(String left, String between, String right, String... strings)
    {
        StringJoiner sj = new StringJoiner(between, left, right);
        for (String string : strings)
            sj.add(string);
        return sj.toString();
    }

    public static String capitalize(String string)
    {
        return StringUtils.capitalize(string);
    }

}
