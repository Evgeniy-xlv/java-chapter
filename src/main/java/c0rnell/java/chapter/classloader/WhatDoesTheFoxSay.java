package c0rnell.java.chapter.classloader;

import c0rnell.java.chapter.util.Fox;
import c0rnell.java.chapter.util.FoxService;
import c0rnell.java.chapter.util.FoxService2;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class WhatDoesTheFoxSay {

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        MyClassLoader myClassLoader = new MyClassLoader(new File("./build/libs/java-chapter-0.0.1-SNAPSHOT.jar"));

        Fox noramlFox = new Fox();
        System.out.println("Лиса говорит:");
        noramlFox.say();

        Object infectedFox = myClassLoader.findClass("c0rnell.java.chapter.util.Fox")
                .getConstructor()
                .newInstance();

        System.out.println("Лиса говорит:");
        infectedFox.getClass().getMethod("say").invoke(infectedFox);

        System.out.println(infectedFox.getClass().getClassLoader());

        System.out.println("#Service");

        new FoxService().say();
        new FoxService2().say();

        Object service = myClassLoader.findClass("c0rnell.java.chapter.util.FoxService")
                .getConstructor()
                .newInstance();
        service.getClass().getMethod("say").invoke(service);

        service = myClassLoader.findClass("c0rnell.java.chapter.util.FoxService2")
                .getConstructor()
                .newInstance();
        service.getClass().getMethod("say").invoke(service);

        myClassLoader.close();
    }

    public static class MyClassLoader extends URLClassLoader {

        private final Map<String, Class<?>> cached = new HashMap<>();

        public MyClassLoader(File file) throws MalformedURLException {
            super(new URL[]{file.toURI().toURL()});
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (cached.containsKey(name)) {
                return cached.get(name);
            }
            try (
                    InputStream is = getResourceAsStream(name.replaceAll("\\.", "/").concat(".class"));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()
            ) {
                if (is != null) {
                    byte[] buffer = new byte[1024];
                    int i;
                    while ((i = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, i);
                    }
                    byte[] bytes = processBytes(name, baos.toByteArray());
                    Class<?> aClass = defineClass(name, bytes, 0, bytes.length);
                    cached.put(name, aClass);
                    return aClass;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return super.findClass(name);
        }

        private byte[] processBytes(String name, byte[] bytes) {

            if (!name.equals("c0rnell.java.chapter.util.Fox")) {
                return bytes;
            }

            ClassReader classReader = new ClassReader(bytes);
            ClassWriter classWriter = new ClassWriter(classReader, 0);

            classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    if (name.equals("say")) {
                        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                        return new AdviceAdapter(api, mv, access, name, descriptor) {
                            @Override
                            public void visitLdcInsn(Object value) {
                                //
                                super.visitLdcInsn("- вкладывайся в крипту");
                                //
                            }
                        };
                    }
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }, 0);

            return classWriter.toByteArray();
        }
    }
}
