package c0rnell.java.chapter.unsafe;

import c0rnell.java.chapter.util.SomeClass;
import c0rnell.java.chapter.util.Utils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeMemoryExample {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Unsafe unsafe = Utils.getUnsafe();

        SomeClass regularSomeClass = new SomeClass(999);
        Field preInitializedId = SomeClass.class.getField("preInitializedId");

        System.out.println("reflection:");
        System.out.println(regularSomeClass.preInitializedId); //123
        System.out.println(preInitializedId.get(regularSomeClass)); //123
        System.out.println(unsafe.getLong(regularSomeClass, unsafe.objectFieldOffset(preInitializedId)));//123

        System.out.println("unsafe:");
        SomeClass unsafeSomeClass = (SomeClass) unsafe.allocateInstance(SomeClass.class);
        System.out.println(unsafeSomeClass.preInitializedId);//123
        System.out.println(preInitializedId.get(unsafeSomeClass));//0
        System.out.println(unsafe.getLong(unsafeSomeClass, unsafe.objectFieldOffset(preInitializedId)));//0
        unsafe.putLong(unsafeSomeClass, unsafe.objectFieldOffset(preInitializedId), 10000L);
        System.out.println(unsafe.getLong(unsafeSomeClass, unsafe.objectFieldOffset(preInitializedId)));//10000

    }
}
