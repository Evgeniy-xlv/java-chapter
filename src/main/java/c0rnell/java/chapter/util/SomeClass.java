package c0rnell.java.chapter.util;

public class SomeClass {
    public final long preInitializedId = 123;
    public final long alsoFinalButInitByConstructor;
    public String name = "Nname";
    public int age = 3;

    public SomeClass(long alsoFinalButInitByConstructor) {
        this.alsoFinalButInitByConstructor = alsoFinalButInitByConstructor;
        System.out.println("Some class init");
    }

    private void doInternal() {
        System.out.println("internal work completed");
    }

    public void someParamMethod(String email, String password, boolean isMale) {
    }
}