package irc_switch.security;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomPassword() {
        byte[] bytes = new byte[24];
        RANDOM.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
