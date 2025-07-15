package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaxServiceTest {

    private TaxService taxService;
    private static final double DELTA = 0.001; // For double comparisons

    @BeforeEach
    void setUp() {
        // Initialize TaxService with standard rates for each test
        taxService = new TaxService(0.10, 0.05);
    }

    @Test
    @DisplayName("Should calculate basic tax for non-exempt non-imported item")
    void calculateTax_basicNonExemptNonImported() {
        Item item = new Item("music CD", 14.99, false, ItemType.OTHER, 1);
        double expectedTax = 1.50; // 14.99 * 0.10 = 1.499 -> rounds to 1.50
        assertEquals(expectedTax, taxService.calculateTax(item), DELTA);
    }

    @Test
    @DisplayName("Should calculate only import tax for exempt imported item")
    void calculateTax_exemptImported() {
        Item item = new Item("imported box of chocolates", 10.00, true, ItemType.FOOD, 1);
        double expectedTax = 0.50; // 10.00 * 0.05 = 0.50
        assertEquals(expectedTax, taxService.calculateTax(item), DELTA);
    }

    @Test
    @DisplayName("Should calculate both basic and import tax for non-exempt imported item")
    void calculateTax_nonExemptImported() {
        Item item = new Item("imported bottle of perfume", 47.50, true, ItemType.OTHER, 1);
        // Basic tax: 47.50 * 0.10 = 4.75
        // Import tax: 47.50 * 0.05 = 2.375
        // Total: 4.75 + 2.375 = 7.125 -> rounds to 7.15
        double expectedTax = 7.15;
        assertEquals(expectedTax, taxService.calculateTax(item), DELTA);
    }

    @Test
    @DisplayName("Should calculate no tax for exempt non-imported item")
    void calculateTax_exemptNonImported() {
        Item item = new Item("book", 12.49, false, ItemType.BOOK, 1);
        double expectedTax = 0.00;
        assertEquals(expectedTax, taxService.calculateTax(item), DELTA);

        Item item2 = new Item("packet of headache pills", 9.75, false, ItemType.MEDICAL, 1);
        assertEquals(expectedTax, taxService.calculateTax(item2), DELTA);
    }

    @Test
    @DisplayName("Should handle items with zero price gracefully")
    void calculateTax_zeroPrice() {
        Item item = new Item("free item", 0.00, false, ItemType.OTHER, 1);
        assertEquals(0.00, taxService.calculateTax(item), DELTA);
    }

    @Test
    @DisplayName("Should apply quantity to tax calculation")
    void calculateTax_withQuantity() {
        Item item = new Item("music CD", 14.99, false, ItemType.OTHER, 2);
        // Tax for one: 1.50
        // Tax for two: 1.50 * 2 = 3.00
        double expectedTax = 3.00;
        assertEquals(expectedTax, taxService.calculateTax(item), DELTA);
    }

    @Test
    @DisplayName("Should throw NullPointerException if item is null")
    void calculateTax_nullItem() {
        assertThrows(NullPointerException.class, () -> taxService.calculateTax(null));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for negative tax rates")
    void constructor_negativeTaxRates() {
        assertThrows(IllegalArgumentException.class, () -> new TaxService(-0.10, 0.05));
        assertThrows(IllegalArgumentException.class, () -> new TaxService(0.10, -0.05));
    }
}