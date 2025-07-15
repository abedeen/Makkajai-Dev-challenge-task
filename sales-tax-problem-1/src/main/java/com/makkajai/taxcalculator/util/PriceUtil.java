package com.makkajai.taxcalculator.util;

/**
 * Utility class for common price and tax related calculations.
 */
public final class PriceUtil {

    private PriceUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Rounds a double value up to the nearest 0.05.
     * This is a common requirement for sales tax calculations.
     * @param value The value to round.
     * @return The rounded value.
     */
    public static double roundToNearest0_05(double value) {
        // Multiply by 20, ceiling, then divide by 20
        // e.g., 0.56 * 20 = 11.2 -> ceil(11.2) = 12.0 -> 12.0 / 20 = 0.60
        return Math.ceil(value * 20) / 20.0;
    }
}