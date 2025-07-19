package com.makkajai.taxcalculator.model;

import java.util.List;

/**
 * Defines the contract for receipt operations, allowing for different receipt implementations.
 */
public interface ReceiptOperations {
    void addItem(Item item);
    void addAllItems(List<Item> itemList);
    double getTotalTax();
    double getTotal();
    List<ReceiptItem> getReceiptLines();
}