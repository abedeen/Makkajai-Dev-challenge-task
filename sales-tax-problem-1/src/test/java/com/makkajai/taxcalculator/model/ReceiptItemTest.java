package com.makkajai.taxcalculator.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptItemTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Should create ReceiptItem and calculate total price correctly")
    void constructor_validParameters() {
        Item item = new Item("music CD", 14.99, false, ItemType.OTHER, 1);
        double tax = 1.50; // Example tax
        ReceiptItem receiptItem = new ReceiptItem(item, tax);

        assertNotNull(receiptItem);
        assertEquals(item, receiptItem.getItem());
        assertEquals(tax, receiptItem.getTax(), DELTA);
        // Total price should be (item.price * item.quantity) + tax
        assertEquals((14.99 * 1) + 1.50, receiptItem.getTotalPrice(), DELTA); // 16.49
    }

    @Test
    @DisplayName("Should calculate total price correctly for multiple quantities")
    void constructor_multipleQuantity() {
        Item item = new Item("imported bottle of perfume", 47.50, true, ItemType.OTHER, 2);
        double tax = 7.15 * 2; // (Basic: 4.75 + Import: 2.375 = 7.125 -> 7.15) * 2 = 14.30
        ReceiptItem receiptItem = new ReceiptItem(item, tax);

        assertEquals(item, receiptItem.getItem());
        assertEquals(tax, receiptItem.getTax(), DELTA);
        // Total price should be (item.price * item.quantity) + tax
        assertEquals((47.50 * 2) + 14.30, receiptItem.getTotalPrice(), DELTA); // 95.00 + 14.30 = 109.30
    }


    @Test
    @DisplayName("Should throw NullPointerException if item is null")
    void constructor_nullItem() {
        assertThrows(NullPointerException.class, () -> new ReceiptItem(null, 1.00));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if tax is negative")
    void constructor_negativeTax() {
        Item item = new Item("book", 12.49, false, ItemType.BOOK, 1);
        assertThrows(IllegalArgumentException.class, () -> new ReceiptItem(item, -0.50));
    }

    @Test
    @DisplayName("ToString should contain relevant receipt line details")
    void toString_shouldContainDetails() {
        Item item = new Item("chocolate bar", 0.85, false, ItemType.FOOD, 1);
        double tax = 0.00; // Exempt
        ReceiptItem receiptItem = new ReceiptItem(item, tax);

        String toStringResult = receiptItem.toString();
        assertTrue(toStringResult.contains("item=chocolate bar"));
        assertTrue(toStringResult.contains("quantity=1"));
        assertTrue(toStringResult.contains("tax=0.00"));
        assertTrue(toStringResult.contains("totalPrice=0.85"));
    }
}