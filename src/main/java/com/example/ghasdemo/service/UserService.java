package com.example.ghasdemo.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 * Demonstrates java/weak-cryptographic-algorithm, hardcoded credentials
 * and java/predictable-seed.
 */
@Service
public class UserService {

    /** VULNERABLE: hardcoded credential. Secret scanning and CodeQL both have a say here. */
    private static final String DB_PASSWORD = "Sup3rS3cret!";

    /** VULNERABLE: MD5 is not a password hash. */
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, digest).toString(16);
    }

    /** VULNERABLE: java.util.Random is not cryptographically secure. */
    public String newResetToken() {
        Random random = new Random(System.currentTimeMillis());
        return Long.toHexString(random.nextLong());
    }

    public boolean checkDbPassword(String candidate) {
        return DB_PASSWORD.equals(candidate);
    }
}
