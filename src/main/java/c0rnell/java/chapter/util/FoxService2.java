package c0rnell.java.chapter.util;

import java.lang.reflect.InvocationTargetException;

public class FoxService2 {

    public void say() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object fox = Class.forName("c0rnell.java.chapter.util.Fox", true, this.getClass().getClassLoader())
                .getConstructor()
                .newInstance();
        fox.getClass().getMethod("say").invoke(fox);
    }
}
