package com.makkajai.taxcalculator.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    @DisplayName("Should create Item with valid parameters")
    void constructor_validParameters() {
        Item item = new Item("music CD", 14.99, false, ItemType.OTHER, 1);
        assertNotNull(item);
        assertEquals("music CD", item.getName());
        assertEquals(14.99, item.getPrice(), 0.001);
        assertFalse(item.isImported());
        assertEquals(ItemType.OTHER, item.getType());
        assertEquals(1, item.getQuantity());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if price is negative")
    void constructor_negativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new Item("invalid item", -10.00, false, ItemType.OTHER, 1));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if quantity is zero or negative")
    void constructor_invalidQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new Item("invalid item", 10.00, false, ItemType.OTHER, 0));
        assertThrows(IllegalArgumentException.class, () -> new Item("invalid item", 10.00, false, ItemType.OTHER, -1));
    }

    @Test
    @DisplayName("Should throw NullPointerException if name is null")
    void constructor_nullName() {
        assertThrows(NullPointerException.class, () -> new Item(null, 10.00, false, ItemType.OTHER, 1));
    }

    @Test
    @DisplayName("Should throw NullPointerException if type is null")
    void constructor_nullType() {
        assertThrows(NullPointerException.class, () -> new Item("item", 10.00, false, null, 1));
    }

    @Test
    @DisplayName("Equals and HashCode should work correctly for same items")
    void equalsAndHashCode_sameItems() {
        Item item1 = new Item("book", 12.49, false, ItemType.BOOK, 1);
        Item item2 = new Item("book", 12.49, false, ItemType.BOOK, 1);
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    @DisplayName("Equals and HashCode should work correctly for different items")
    void equalsAndHashCode_differentItems() {
        Item item1 = new Item("book", 12.49, false, ItemType.BOOK, 1);
        Item item2 = new Item("music CD", 14.99, false, ItemType.OTHER, 1);
        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    @DisplayName("ToString should contain relevant item details")
    void toString_shouldContainDetails() {
        Item item = new Item("imported chocolate", 5.50, true, ItemType.FOOD, 2);
        String toStringResult = item.toString();
        assertTrue(toStringResult.contains("name='imported chocolate'"));
        assertTrue(toStringResult.contains("price=5.5"));
        assertTrue(toStringResult.contains("isImported=true"));
        assertTrue(toStringResult.contains("type=FOOD"));
        assertTrue(toStringResult.contains("quantity=2"));
    }
}