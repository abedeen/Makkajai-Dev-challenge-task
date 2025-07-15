package com.makkajai.taxcalculator.config;

/**
 * Centralized application configuration constants.
 */
public final class AppConfig {

    private AppConfig() {
        // Private constructor to prevent instantiation
    }

    public static final double BASIC_TAX_RATE = 0.10;
    public static final double IMPORT_TAX_RATE = 0.05;

    // Exit codes
    public static final int EXIT_CODE_SUCCESS = 0;
    public static final int EXIT_CODE_INVALID_ARGUMENTS = 1;
    public static final int EXIT_CODE_INPUT_ERROR = 2;
    public static final int EXIT_CODE_UNEXPECTED_ERROR = 99;

    // Item type keywords - can be loaded from a properties file for more flexibility
    public static final String[] BOOK_KEYWORDS = {"book"};
    public static final String[] FOOD_KEYWORDS = {"chocolate", "food"};
    public static final String[] MEDICAL_KEYWORDS = {"pill", "headache"}; // Added 'headache' for example
}