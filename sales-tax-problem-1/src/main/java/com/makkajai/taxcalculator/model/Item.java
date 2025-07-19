package com.makkajai.taxcalculator.model;

import java.util.Objects;

/**
 * Represents a single item in the receipt.
 */
public class Item {
    private final String name;
    private final double price;
    private final boolean isImported;
    private final ItemType type;
    private final int quantity; // Added quantity

    public Item(String name, double price, boolean isImported, ItemType type, int quantity) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.name = Objects.requireNonNull(name, "Item name cannot be null.");
        this.price = price;
        this.isImported = isImported;
        this.type = Objects.requireNonNull(type, "Item type cannot be null.");
        this.quantity = quantity;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isImported() { return isImported; }
    public ItemType getType() { return type; }
    public int getQuantity() { return quantity; }

}