package c0rnell.java.chapter.javaagent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class MyClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {

        if (className.equals("org/springframework/data/jpa/repository/query/NativeJpaQuery")) {
            try {
                ClassReader reader = new ClassReader(classfileBuffer);
                ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);

                reader.accept(
                        new MyClassVisitor(Opcodes.ASM5, writer),
                        ClassReader.EXPAND_FRAMES
                );

                return writer.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // nothing transformed
        return classfileBuffer;
    }
}
