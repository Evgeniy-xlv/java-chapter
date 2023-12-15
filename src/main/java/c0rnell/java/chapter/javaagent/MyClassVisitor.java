package c0rnell.java.chapter.javaagent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

class MyClassVisitor extends ClassVisitor {

    String targetName = "createJpaQuery";
    String targetDescriptor = "(Ljava/lang/String;Lorg/springframework/data/repository/query/ReturnedType;)Ljavax/persistence/Query;";

    MyClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (targetName.equals(name) && targetDescriptor.equals(descriptor)) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return new AdviceAdapter(this.api, mv, access, name, descriptor) {
                @Override
                protected void onMethodEnter() {
                    mv.visitCode();

                    // загружаем в OS this, т.к. у не статичных методов в стеке всегда лежит this под 0-ым индексом
                    mv.visitVarInsn(Opcodes.ALOAD, 0);
                    // вызываем метод getQueryMethod() у “хукаемого” объекта
                    mv.visitMethodInsn(
                            Opcodes.INVOKEVIRTUAL, // вызываем виртуальный метод
                            "org/springframework/data/jpa/repository/query/NativeJpaQuery", // который находится в этом классе
                            "getQueryMethod", // называется вот так
                            "()Lorg/springframework/data/jpa/repository/query/JpaQueryMethod;", // и имеет такую сигнатуру
                            false // и еще этот класс не является интерфейсом
                    );
                    // загружаем в OS переменную с индексом 1 (в нашем случае это параметр с индексом 0, т.е. queryString)
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    // вызываем метод у Flexer, который запрепроцессит sql
                    mv.visitMethodInsn(
                            Opcodes.INVOKESTATIC, // вызываем статичный метод
                            "c0rnell/flexer/query/rewriter/ConditionalQueryRewriterResolver", // который находится в этом классе
                            "resolveThenRewrite", // называется вот так
                            "(Lorg/springframework/data/repository/query/QueryMethod;Ljava/lang/String;)Ljava/lang/String;", // и имеет такую сигнатуру
                            false // и еще этот класс не является интерфейсом
                    );
                    // сохраняем в LVA под индекс 1 (queryString) результат из OS, полученный после вызова метода
                    mv.visitVarInsn(Opcodes.ASTORE, 1);

                    mv.visitEnd();
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
