package com.bca.cis.util;

import java.util.Random;

public class PhoneNumberGenerator {

    // Common Indonesian phone number prefixes
    private static final String[] PREFIXES = {
            "0812", "0813", "0814", "0815", "0816", "0817", "0818", "0819", // Telkomsel
            "0852", "0853", "0851",                                         // Indosat
            "0821", "0822", "0823",                                        // XL Axiata
            "0831", "0832", "0833",                                        // Axis
            "0881", "0882", "0883", "0884", "0885", "0886", "0887", "0888", "0889" // Smartfren
    };

    private static final Random RANDOM = new Random();

    /**
     * Generates a random Indonesian phone number.
     *
     * @return A random Indonesian phone number as a String.
     */
    public static String generateRandomPhoneNumber() {
        // Select a random prefix
        String prefix = PREFIXES[RANDOM.nextInt(PREFIXES.length)];

        // Generate 8-10 random digits
        int length = 8 + RANDOM.nextInt(3); // Random length between 8 and 10
        StringBuilder phoneNumber = new StringBuilder(prefix);

        for (int i = 0; i < length; i++) {
            phoneNumber.append(RANDOM.nextInt(10)); // Append a random digit (0-9)
        }

        return phoneNumber.toString();
    }

    /**
     * Generates a random Indonesian phone number with international format (+62).
     *
     * @return A random Indonesian phone number in international format as a String.
     */
    public static String generateRandomInternationalPhoneNumber() {
        String localNumber = generateRandomPhoneNumber();
        return "+62" + localNumber.substring(1); // Replace '0' with '+62'
    }
}