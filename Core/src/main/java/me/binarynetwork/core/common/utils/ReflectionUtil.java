package me.binarynetwork.core.common.utils;

import java.lang.reflect.Field;

/**
 * Created by Bench on 9/24/2016.
 */
public class ReflectionUtil {
    public static Object getPrivateField(String fieldName, Class clazz, Object object) throws NoSuchFieldException
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }
}
