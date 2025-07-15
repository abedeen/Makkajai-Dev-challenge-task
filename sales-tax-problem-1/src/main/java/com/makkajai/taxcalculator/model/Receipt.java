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
    private final List<ReceiptLine> receiptLines;
    private double totalTax;
    private double total;
    private final TaxService taxService; // Dependency injection for TaxService

    public Receipt(TaxService taxService) {
        this.taxService = Objects.requireNonNull(taxService, "TaxService cannot be null.");
        this.receiptLines = new ArrayList<>();
        this.totalTax = 0;
        this.total = 0;
    }

    @Override
    public void addItem(Item item) {
        Objects.requireNonNull(item, "Item to add cannot be null.");

        double itemTax = taxService.calculateTax(item);
        ReceiptLine receiptLine = new ReceiptLine(item, itemTax);
        receiptLines.add(receiptLine);

        totalTax += itemTax;
        total += (item.getPrice() * item.getQuantity()) + itemTax; // Corrected total calculation for quantity
    }

    @Override
    public double getTotalTax() { return totalTax; }

    @Override
    public double getTotal() { return total; }

    @Override
    public List<ReceiptLine> getReceiptLines() {
        return Collections.unmodifiableList(receiptLines); // Return unmodifiable list for immutability
    }
}