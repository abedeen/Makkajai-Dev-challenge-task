package com.makkajai.taxcalculator.model;

import java.util.List;

/**
 * Defines the contract for receipt operations, allowing for different receipt implementations.
 */
public interface ReceiptOperations {
    void addItem(Item item);
    double getTotalTax();
    double getTotal();
    List<ReceiptLine> getReceiptLines();
}