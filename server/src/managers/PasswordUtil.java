package managers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hashString = new StringBuilder();

        for (byte b : hashBytes) {
            String hex = String.format("%02x", b);
            hashString.append(hex);
        }

        return hashString.toString();
    }
}