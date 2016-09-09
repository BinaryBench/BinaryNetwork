package me.binarynetwork.core.common.utils;

import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Bench on 9/9/2016.
 */
public class StringUtil {

    public static String spaceJoin(String string, String... strings)
    {
        return join("", " ", "", string, strings);
    }

    public static String join(String left, String between, String right, String option, String... options)
    {
        List<String> strings = ListUtil.append(option, options);
        StringJoiner sj = new StringJoiner(between, left, right);
        for (String string : strings)
            sj.add(string);
        return sj.toString();
    }
}
