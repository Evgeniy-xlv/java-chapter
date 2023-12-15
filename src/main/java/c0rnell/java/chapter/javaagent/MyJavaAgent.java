package c0rnell.java.chapter.javaagent;

import java.lang.instrument.Instrumentation;

@SuppressWarnings("unused")
public class MyJavaAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from premain!");
        inst.addTransformer(new MyClassTransformer());
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello from agentmain!");
        inst.addTransformer(new MyClassTransformer());
    }
}


/*

MANIFEST.MF
    Manifest-Version: 1.0
    Premain-Class: c0rnell.java.chapter.javaagent.MyJavaAgent
    Agent-Class: c0rnell.java.chapter.javaagent.MyJavaAgent
    Can-Redefine-Classes: true
    Can-Retransform-Classes: true
    Can-Set-Native-Method-Prefix: true

*/