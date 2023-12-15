package c0rnell.java.chapter.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Utils {

    public static Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }
}
