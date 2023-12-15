package c0rnell.java.chapter.unsafe;

import c0rnell.java.chapter.util.SomeClass;
import c0rnell.java.chapter.util.Utils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeFinalExample {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        SomeClass someClass = new SomeClass(999);

        System.out.println(someClass.preInitializedId); // 123
        Field preInitializedId = SomeClass.class.getDeclaredField("preInitializedId");
        preInitializedId.setAccessible(true);
        preInitializedId.setLong(someClass, 10000L);
        System.out.println(someClass.preInitializedId); // 123

        Unsafe unsafe = Utils.getUnsafe();
        unsafe.putLong(someClass, unsafe.objectFieldOffset(preInitializedId), 10000L);
        System.out.println(someClass.preInitializedId); // 123

        System.out.println(someClass.alsoFinalButInitByConstructor); // 999
        Field alsoFinalButInitByConstructor = SomeClass.class.getDeclaredField("alsoFinalButInitByConstructor");
        alsoFinalButInitByConstructor.setAccessible(true);
        alsoFinalButInitByConstructor.setLong(someClass, 10000L);
        System.out.println(someClass.alsoFinalButInitByConstructor); // 10000
    }
}
