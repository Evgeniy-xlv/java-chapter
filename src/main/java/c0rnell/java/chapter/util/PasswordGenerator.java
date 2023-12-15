package c0rnell.java.chapter.util;

public class PasswordGenerator {

    private static final String ALPHABET = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890!@#$%^&*()_+=-.,?";
    @Deprecated
    private static final String DEFAULT_SALT = "SALT";

    private final int offset;

    PasswordGenerator(String salt) {
        this.offset = 300 + salt.hashCode() % ALPHABET.length();
    }

    private String generate(int length) {
        if (length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(ALPHABET.charAt((offset + i) % ALPHABET.length()));
        }
        String result = stringBuilder.toString();
        validate(result);
        return result;
    }

    private void validate(String password) {
        char[] charArray = password.toCharArray();
        for (int i = 1; i < charArray.length; i++) {
            if (charArray[i - 1] == charArray[i]) {
                throw new RuntimeException("Char at " + (i + 1) + " is the same as char at " + (i));
            }
        }
    }

    @Deprecated
    public static String generatePassword(int length) {
        return new PasswordGenerator(DEFAULT_SALT).generate(length);
    }

    public static String generatePassword(String salt, int length) {
        return new PasswordGenerator(salt).generate(length);
    }
}
