package com.example.storeapi.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Custom Password Encoder / Verifier
 * Independent of Spring Security
 */
public class PasswordEncoder {

    private static final int WORKLOAD = 12; // BCrypt cost factor

    /**
     * Hash a raw password using BCrypt
     *
     * @param rawPassword plain password
     * @return hashed password
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(WORKLOAD));
    }

    /**
     * Verify a raw password against a hashed password
     *
     * @param rawPassword   plain password
     * @param hashedPassword hashed password from DB
     * @return true if password matches, false otherwise
     */
    public static boolean verify(String rawPassword, String hashedPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) return false;
        if (hashedPassword == null || hashedPassword.isEmpty()) return false;

        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    // Optional: You can add a convenience method to check if hash needs rehashing
    public static boolean needsRehash(String hashedPassword, int newWorkload) {
        // Not implemented here; can be done by parsing BCrypt hash prefix
        return false;
    }
}