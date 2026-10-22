package com.university.registration.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Minimal password hashing for demo/coursework purposes.
 * A real production system should use a salted algorithm such as bcrypt or Argon2.
 */
public class PasswordUtil {

    public static String hash(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainText.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to hash password", e);
        }
    }
}
