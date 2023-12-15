package c0rnell.java.chapter.classloader;

import c0rnell.java.chapter.util.SomeClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public class WhenTheWorldFalls {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
          System.out.println(WhenTheWorldFalls.class.getClassLoader()); // AppClassLoader
        System.out.println(WhenTheWorldFalls.class.getClassLoader().getClass().getClassLoader()); // null - BootClassLoader

        Class<?> parentCLClass = SomeClass.class;
        System.out.println(parentCLClass.getClassLoader()); // AppClassLoader

        SimpleClassLoader classLoader = new SimpleClassLoader();
        Class<?> simpleCLClass = classLoader.loadClass("c0rnell.java.chapter.util.SomeClass");
        System.out.println(simpleCLClass.getClassLoader()); // AppClassLoader

        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> myCLClass = myClassLoader.loadClass("c0rnell.java.chapter.util.SomeClass");
        System.out.println(myCLClass.getClassLoader()); // MyClassLoader

        Object parentCLClassObj = parentCLClass.getConstructor(long.class).newInstance(999);
        Object simpleCLClassObj = simpleCLClass.getConstructor(long.class).newInstance(999);
        Object myCLClassObj = myCLClass.getConstructor(long.class).newInstance(999);

        System.out.println(parentCLClassObj instanceof SomeClass); // true
        System.out.println(simpleCLClassObj instanceof SomeClass); // true
        System.out.println(myCLClassObj instanceof SomeClass); // false

        SomeClass someClass = (SomeClass) parentCLClassObj;
        System.out.println(someClass); // ok
        someClass = (SomeClass) simpleCLClassObj;
        System.out.println(someClass); // ok
        someClass = (SomeClass) myCLClassObj; // exception
        System.out.println(someClass);

        // Exception in thread "main" java.lang.ClassCastException:
        // class c0rnell.java.chapter.util.SomeClass cannot be cast to class c0rnell.java.chapter.util.SomeClass
        // (c0rnell.java.chapter.util.SomeClass is in unnamed module of loader
        // c0rnell.java.chapter.classloader.ClassLoaderExample$MyClassLoader @8efb846;
        // c0rnell.java.chapter.util.SomeClass is in unnamed module of loader 'app')
    }

    public static class SimpleClassLoader extends ClassLoader {
    }

    public static class MyClassLoader extends ClassLoader {

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {

            try {
                byte[] bytes = loadClassFromFile(name);
                if (bytes != null && bytes.length > 0) {
                    return defineClass(name, bytes, 0, bytes.length);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return super.loadClass(name);
        }

        private byte[] loadClassFromFile(String fileName) throws IOException {
            String name = fileName.replace('.', File.separatorChar) + ".class";
            try (
                    InputStream is = getResourceAsStream(name);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()
            ) {
                if (is != null) {
                    int nextValue;
                    while ((nextValue = is.read()) != -1) {
                        baos.write(nextValue);
                    }
                    return baos.toByteArray();
                }
            }
            return null;
        }
    }
}
