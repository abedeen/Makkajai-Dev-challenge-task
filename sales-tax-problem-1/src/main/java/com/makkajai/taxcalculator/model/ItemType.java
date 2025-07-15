package com.makkajai.taxcalculator.model;

/**
 * Defines categories for items, indicating if they are tax-exempt.
 */
public enum ItemType {
    BOOK(true),
    FOOD(true),
    MEDICAL(true),
    OTHER(false); // Non-exempt by default

    private final boolean isExempt;

    ItemType(boolean isExempt) {
        this.isExempt = isExempt;
    }

    public boolean isExempt() {
        return isExempt;
    }
}