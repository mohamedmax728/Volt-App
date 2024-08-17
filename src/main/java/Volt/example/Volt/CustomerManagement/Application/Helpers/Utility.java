package Volt.example.Volt.CustomerManagement.Application.Helpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Utility {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String HMAC_ALGORITHM = "HmacSHA512";

    public static void createPasswordHash(String password, byte[] passwordHash, byte[] passwordSalt) throws Exception {
        // Generate a new salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64]; // HMAC-SHA-512 requires a 64-byte salt
        random.nextBytes(salt);

        // Create the HMAC key with the salt
        SecretKeySpec keySpec = new SecretKeySpec(salt, HMAC_ALGORITHM);

        // Initialize the HMAC instance
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(keySpec);

        // Compute the password hash
        byte[] hash = mac.doFinal(password.getBytes("UTF-8"));

        // Set the results
        System.arraycopy(salt, 0, passwordSalt, 0, salt.length);
        System.arraycopy(hash, 0, passwordHash, 0, hash.length);
    }


    public static String createRandomToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[4];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static String generateRefreshToken() {
        byte[] randomNumber = new byte[32];
        SecureRandom rng = new SecureRandom();
        rng.nextBytes(randomNumber);
        return Base64.getEncoder().encodeToString(randomNumber);
    }
}
