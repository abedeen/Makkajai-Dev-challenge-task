package com.makkajai.taxcalculator.model;

import com.makkajai.taxcalculator.service.TaxService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a sales receipt, aggregating items, taxes, and total.
 * Implements an interface for better abstraction and testability.
 */
public class Receipt implements ReceiptOperations {
    private final List<ReceiptItem> receiptItems;
    private double totalTax;
    private double total;
    private final TaxService taxService; // Dependency injection for TaxService

    public Receipt(TaxService taxService) {
        this.taxService = Objects.requireNonNull(taxService, "TaxService cannot be null.");
        this.receiptItems = new ArrayList<>();
        this.totalTax = 0;
        this.total = 0;
    }

    @Override
    public void addItem(Item item) {
        Objects.requireNonNull(item, "Item to add cannot be null.");

        double itemTax = taxService.calculateTax(item);
        ReceiptItem receiptItem = new ReceiptItem(item, itemTax);
        receiptItems.add(receiptItem);

        totalTax += itemTax;
        total += (item.getPrice() * item.getQuantity()) + itemTax; // Corrected total calculation for quantity
    }

    @Override
    public void addAllItems(List<Item> itemList) {
        for(Item item : itemList)
            addItem(item);
    }

    @Override
    public double getTotalTax() { return totalTax; }

    @Override
    public double getTotal() { return total; }

    @Override
    public List<ReceiptItem> getReceiptLines() {
        return Collections.unmodifiableList(receiptItems); // Return unmodifiable list for immutability
    }
}