package com.makkajai.taxcalculator.model;

import java.util.Objects;

/**
 * Represents a single line item on a receipt, including calculated tax and total price for that line.
 */
public class ReceiptItem {
    private final Item item;
    private final double tax;
    private final double totalPrice;

    public ReceiptItem(Item item, double tax) {
        this.item = Objects.requireNonNull(item, "Item cannot be null for ReceiptLine.");
        if (tax < 0) {
            throw new IllegalArgumentException("Tax cannot be negative.");
        }
        this.tax = tax;
        this.totalPrice = (item.getPrice() * item.getQuantity()) + tax; // Corrected total price for quantity
    }

    public Item getItem() { return item; }
    public double getTax() { return tax; }
    public double getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return "ReceiptLine{" +
               "item=" + item.getName() +
               ", quantity=" + item.getQuantity() +
               ", tax=" + String.format("%.2f", tax) +
               ", totalPrice=" + String.format("%.2f", totalPrice) +
               '}';
    }
}