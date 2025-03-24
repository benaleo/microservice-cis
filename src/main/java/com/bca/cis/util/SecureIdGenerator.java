package com.bca.cis.util;

import java.security.SecureRandom;
import java.util.UUID;

public class SecureIdGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a secure random UUID.
     * This method uses SecureRandom to ensure cryptographic strength.
     *
     * @return A secure random UUID as a String.
     */
    public static String generateSecureUUID() {
        // Generate 128 random bits using SecureRandom
        byte[] randomBytes = new byte[16];
        SECURE_RANDOM.nextBytes(randomBytes);

        // Set the version and variant bits according to UUID specifications
        randomBytes[6] &= 0x0f;  /* Clear the version bits */
        randomBytes[6] |= 0x40;  /* Set the version to 4 (random UUID) */
        randomBytes[8] &= 0x3f;  /* Clear the variant bits */
        randomBytes[8] |= 0x80;  /* Set the variant to IETF (RFC 4122) */

        // Construct the UUID from the random bytes
        return new UUID(
                ((long) randomBytes[0] & 0xff) << 56 |
                        ((long) randomBytes[1] & 0xff) << 48 |
                        ((long) randomBytes[2] & 0xff) << 40 |
                        ((long) randomBytes[3] & 0xff) << 32 |
                        ((long) randomBytes[4] & 0xff) << 24 |
                        ((long) randomBytes[5] & 0xff) << 16 |
                        ((long) randomBytes[6] & 0xff) << 8  |
                        ((long) randomBytes[7] & 0xff),
                ((long) randomBytes[8] & 0xff) << 56 |
                        ((long) randomBytes[9] & 0xff) << 48 |
                        ((long) randomBytes[10] & 0xff) << 40 |
                        ((long) randomBytes[11] & 0xff) << 32 |
                        ((long) randomBytes[12] & 0xff) << 24 |
                        ((long) randomBytes[13] & 0xff) << 16 |
                        ((long) randomBytes[14] & 0xff) << 8  |
                        ((long) randomBytes[15] & 0xff)
        ).toString();
    }
}