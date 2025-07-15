package com.makkajai.taxcalculator.model;

import com.makkajai.taxcalculator.service.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ReceiptTest {

    private TaxService mockTaxService;
    private Receipt receipt;
    private static final double DELTA = 0.001; // For double comparisons

    @BeforeEach
    void setUp() {
        mockTaxService = Mockito.mock(TaxService.class);
        receipt = new Receipt(mockTaxService);
    }

    @Test
    @DisplayName("Should add item and update totals correctly")
    void addItem_shouldUpdateTotals() {
        Item item1 = new Item("book", 12.49, false, ItemType.BOOK, 1);
        when(mockTaxService.calculateTax(item1)).thenReturn(0.00); // Book is exempt

        receipt.addItem(item1);

        assertEquals(0.00, receipt.getTotalTax(), DELTA);
        assertEquals(12.49, receipt.getTotal(), DELTA);
        assertEquals(1, receipt.getReceiptLines().size());
        assertEquals("book", receipt.getReceiptLines().get(0).getItem().getName());
        assertEquals(12.49, receipt.getReceiptLines().get(0).getTotalPrice(), DELTA);
    }

    @Test
    @DisplayName("Should handle multiple items with different taxes")
    void addItem_multipleItems() {
        Item item1 = new Item("music CD", 14.99, false, ItemType.OTHER, 1);
        when(mockTaxService.calculateTax(item1)).thenReturn(1.50); // 1.499 -> 1.50

        Item item2 = new Item("imported box of chocolates", 10.00, true, ItemType.FOOD, 1);
        when(mockTaxService.calculateTax(item2)).thenReturn(0.50); // 0.50

        receipt.addItem(item1);
        receipt.addItem(item2);

        assertEquals(2.00, receipt.getTotalTax(), DELTA); // 1.50 + 0.50
        assertEquals(14.99 + 1.50 + 10.00 + 0.50, receipt.getTotal(), DELTA); // 26.99
        assertEquals(2, receipt.getReceiptLines().size());
    }

    @Test
    @DisplayName("Should return an unmodifiable list of receipt lines")
    void getReceiptLines_shouldBeUnmodifiable() {
        Item item1 = new Item("book", 12.49, false, ItemType.BOOK, 1);
        when(mockTaxService.calculateTax(item1)).thenReturn(0.00);
        receipt.addItem(item1);

        List<ReceiptLine> lines = receipt.getReceiptLines();
        assertThrows(UnsupportedOperationException.class, () -> lines.add(null));
    }

    @Test
    @DisplayName("Should throw NullPointerException if TaxService is null in constructor")
    void constructor_nullTaxService() {
        assertThrows(NullPointerException.class, () -> new Receipt(null));
    }

    @Test
    @DisplayName("Should throw NullPointerException if item added is null")
    void addItem_nullItem() {
        assertThrows(NullPointerException.class, () -> receipt.addItem(null));
    }

    @Test
    @DisplayName("Should correctly calculate totals for multiple quantities of the same item")
    void addItem_multipleQuantities() {
        Item item = new Item("music CD", 14.99, false, ItemType.OTHER, 2);
        when(mockTaxService.calculateTax(item)).thenReturn(3.00); // (14.99 * 0.10) * 2 = 1.499 * 2 = 2.998 -> 3.00

        receipt.addItem(item);

        assertEquals(3.00, receipt.getTotalTax(), DELTA);
        // (14.99 * 2) + 3.00 = 29.98 + 3.00 = 32.98
        assertEquals(32.98, receipt.getTotal(), DELTA);
        assertEquals(1, receipt.getReceiptLines().size());
        assertEquals(32.98, receipt.getReceiptLines().get(0).getTotalPrice(), DELTA);
    }
}