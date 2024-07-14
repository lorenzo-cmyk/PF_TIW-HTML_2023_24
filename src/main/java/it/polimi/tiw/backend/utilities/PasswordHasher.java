package it.polimi.tiw.backend.utilities;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * This class contains utility methods to hash passwords.
 */
public class PasswordHasher {
    /**
     * This method hashes a password using the PBKDF2 algorithm with HMAC SHA-256.
     * The salt is hardcoded and constant (all zeros).
     *
     * @param password the password to hash
     * @return the hashed password (Base64 encoded)
     */
    public static String hashPassword(String password) {
        try {
            // Referencing: https://dzone.com/articles/secure-password-hashing-in-java

            // For the sake of simplicity the salt is hardcoded and constant (all zeros)
            byte[] salt = new byte[16];

            // Configure the key derivation function as PBKDF2 with HMAC SHA-256
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // Hash the password
            byte[] hash = secretKeyFactory.generateSecret(keySpec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {
            throw new IllegalStateException("The algorithm is not available to the JVM " +
                    "or the key specification is invalid or unsupported.");
        }
    }
}
