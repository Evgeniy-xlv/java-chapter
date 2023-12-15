package c0rnell.java.chapter.reflect;

import c0rnell.java.chapter.util.SomeClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ReflectionExample {

    public static void main(String[] args) {
        try {
            SomeClass someClass = new SomeClass(999);

            Method doInternal = SomeClass.class.getDeclaredMethod("doInternal");
            doInternal.setAccessible(true);
            doInternal.invoke(someClass); // prints "internal work completed"

            Method someParamMethod = SomeClass.class.getDeclaredMethod("someParamMethod", String.class, String.class, boolean.class);
            Parameter[] parameters = someParamMethod.getParameters();
            System.out.println(parameters[0].getName()); // 'arg0' by default, but it's 'email' when -parameters arg is passed to compiler

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}




