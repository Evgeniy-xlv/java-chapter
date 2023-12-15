package c0rnell.java.chapter.unsafe;

import c0rnell.java.chapter.util.SomeClass;
import c0rnell.java.chapter.util.Utils;
import sun.misc.Unsafe;

public class UnsafeInstanceExample {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Unsafe unsafe = Utils.getUnsafe();

        Class<?> aClass = Class.forName("c0rnell.java.chapter.util.SomeClass");
        SomeClass someClassObj = (SomeClass) unsafe.allocateInstance(aClass);

        System.out.println(someClassObj.preInitializedId); // 123
        System.out.println(someClassObj.alsoFinalButInitByConstructor); // 0
        System.out.println(someClassObj.name); // null
        System.out.println(someClassObj.age); // 0
    }
}
