package c0rnell.java.chapter.reflect;

import c0rnell.java.chapter.util.SomeClass;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionFactoryExample {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

        Constructor<?> constructor = reflectionFactory.newConstructorForSerialization(SomeClass.class, Object.class.getConstructor());

        SomeClass someClassObj = (SomeClass) constructor.newInstance();

        System.out.println(someClassObj);
        System.out.println(someClassObj.preInitializedId); // 123
        System.out.println(someClassObj.alsoFinalButInitByConstructor); // 0
        System.out.println(someClassObj.name); // null
        System.out.println(someClassObj.age); // 0
    }
}
