package org.mandl.services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordService {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static String hashPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        validatePassword(password);
        var spec = new PBEKeySpec(password.toCharArray(),
                Base64.getDecoder().decode(salt), ITERATIONS, KEY_LENGTH);
        var factory = SecretKeyFactory.getInstance(ALGORITHM);
        var hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public static String generateSalt() {
        var random = new SecureRandom();
        var salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static boolean verifyPassword(String password, String salt, String hash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String calculatedHash = hashPassword(password, salt);
        return calculatedHash.equals(hash);
    }


    public static void validatePassword(String password) {
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException(
                    "Password must have at least 8 characters, including one uppercase letter, one lowercase letter, one number, and one special character."
            );
        }
    }
}
