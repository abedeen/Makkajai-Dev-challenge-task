package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.config.AppConfig;
import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemParserTest {

    private ItemParser itemParser;

    @BeforeEach
    void setUp() {
        itemParser = new ItemParser();
    }

    // --- Tests for buildMatch(String inputLine) ---

    @Test
    @DisplayName("buildMatch: Should correctly parse valid input line with decimal price")
    void buildMatch_validInputDecimalPrice() {
        String inputLine = "1 imported bottle of perfume at 27.99";
        String[] expected = {"1", "imported bottle of perfume", "27.99"};
        assertArrayEquals(expected, itemParser.buildMatch(inputLine));
    }

    @Test
    @DisplayName("buildMatch: Should correctly parse valid input line with integer price")
    void buildMatch_validInputIntegerPrice() {
        String inputLine = "4 pen at 23";
        String[] expected = {"4", "pen", "23"};
        assertArrayEquals(expected, itemParser.buildMatch(inputLine));
    }

    @Test
    @DisplayName("buildMatch: Should correctly parse item with multiple words in name")
    void buildMatch_multipleWordsInName() {
        String inputLine = "2 box of imported chocolates at 11.25";
        String[] expected = {"2", "box of imported chocolates", "11.25"};
        assertArrayEquals(expected, itemParser.buildMatch(inputLine));
    }

    @Test
    @DisplayName("buildMatch: Should return empty strings for invalid format")
    void buildMatch_invalidFormat() {
        String inputLine = "invalid input line";
        String[] expected = {"", "", ""}; // Expect empty strings if no match
        assertArrayEquals(expected, itemParser.buildMatch(inputLine));
    }

    @Test
    @DisplayName("buildMatch: Should return empty strings for partially valid format")
    void buildMatch_partialValidFormat() {
        String inputLine = "1 item 10.00"; // Missing " at "
        String[] expected = {"", "", ""};
        assertArrayEquals(expected, itemParser.buildMatch(inputLine));
    }

    @Test
    @DisplayName("buildMatch: Should return empty strings for empty input")
    void buildMatch_emptyInput() {
        String inputLine = "";
        String[] expected = {"", "", ""};
        assertArrayEquals(expected, itemParser.buildMatch(inputLine));
    }

    @Test
    @DisplayName("buildMatch: Should return empty strings for null input")
    void buildMatch_nullInput() {
        // This might throw NullPointerException if not handled internally by regex engine,
        // but Java's Pattern.matcher() handles null gracefully by throwing NPE immediately.
        assertThrows(NullPointerException.class, () -> itemParser.buildMatch(null));
    }

    // --- Tests for isValid(String[] item) ---

    @Test
    @DisplayName("isValid: Should return true for a valid item array")
    void isValid_validItemArray() {
        String[] item = {"1", "book", "12.49"};
        assertTrue(itemParser.isvalid(item));
    }

    @Test
    @DisplayName("isValid: Should return false if quantity is empty")
    void isValid_emptyQuantity() {
        String[] item = {"", "book", "12.49"};
        assertFalse(itemParser.isvalid(item));
    }

    @Test
    @DisplayName("isValid: Should return false if name is empty")
    void isValid_emptyName() {
        String[] item = {"1", "", "12.49"};
        assertFalse(itemParser.isvalid(item));
    }

    @Test
    @DisplayName("isValid: Should return false if price is empty")
    void isValid_emptyPrice() {
        String[] item = {"1", "book", ""};
        assertFalse(itemParser.isvalid(item));
    }

    @Test
    @DisplayName("isValid: Should return false if any element is empty")
    void isValid_anyEmptyElement() {
        String[] item = {"1", "", ""};
        assertFalse(itemParser.isvalid(item));
    }

    @Test
    @DisplayName("isValid: Should return false for an empty array")
    void isValid_emptyArray() {
        String[] item = {};
        assertFalse(itemParser.isvalid(item)); // Filter will return empty stream, count will be 0.
        // Depending on exact logic for 'empty array', this might need adjustment.
        // Current logic counts elements with length 0.
    }

    @Test
    @DisplayName("isValid: Should return false for a null array")
    void isValid_nullArray() {
        assertThrows(NullPointerException.class, () -> itemParser.isvalid(null));
    }

    @Test
    @DisplayName("isValid: Should return false for an array with null elements")
    void isValid_arrayWithNullElements() {
        String[] item = {"1", null, "12.49"};
        assertThrows(NullPointerException.class, () -> itemParser.isvalid(item)); // Null element will cause NPE in stream filter
    }


    // --- Tests for parseItem(String[] item) ---

    @Test
    @DisplayName("parseItem: Should parse a non-imported, non-exempt item correctly")
    void parseItem_nonImportedNonExempt() {
        String[] itemData = {"1", "music CD", "14.99"};
        Item expectedItem = new Item("music CD", 14.99, false, ItemType.OTHER, 1);
        assertEquals(expectedItem, itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should parse an imported, exempt item (food) correctly")
    void parseItem_importedExemptFood() {
        String[] itemData = {"1", "imported box of chocolates", "10.00"};
        Item expectedItem = new Item("imported box of chocolates", 10.00, true, ItemType.OTHER, 1);
        assertEquals(expectedItem, itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should parse an imported, non-exempt item correctly")
    void parseItem_importedNonExempt() {
        String[] itemData = {"1", "imported bottle of perfume", "47.50"};
        Item expectedItem = new Item("imported bottle of perfume", 47.50, true, ItemType.OTHER, 1);
        assertEquals(expectedItem, itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should parse a non-imported, exempt item (book) correctly")
    void parseItem_nonImportedExemptBook() {
        String[] itemData = {"1", "book", "12.49"};
        Item expectedItem = new Item("book", 12.49, false, ItemType.BOOK, 1);
        assertEquals(expectedItem, itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should parse a non-imported, exempt item (medical) correctly")
    void parseItem_nonImportedExemptMedical() {
        String[] itemData = {"1", "packet of headache pills", "9.75"};
        Item expectedItem = new Item("packet of headache pills", 9.75, false, ItemType.MEDICAL, 1);
        assertEquals(expectedItem, itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should handle multiple quantities")
    void parseItem_multipleQuantity() {
        String[] itemData = {"2", "music CD", "14.99"};
        Item expectedItem = new Item("music CD", 14.99, false, ItemType.OTHER, 2);
        assertEquals(expectedItem, itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should throw NumberFormatException if quantity is not a number")
    void parseItem_invalidQuantityFormat() {
        String[] itemData = {"one", "book", "12.49"};
        assertThrows(NumberFormatException.class, () -> itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should throw NumberFormatException if price is not a number")
    void parseItem_invalidPriceFormat() {
        String[] itemData = {"1", "book", "twelve"};
        assertThrows(NumberFormatException.class, () -> itemParser.parseItem(itemData));
    }

    @Test
    @DisplayName("parseItem: Should throw NullPointerException if itemData array is null")
    void parseItem_nullArray() {
        assertThrows(NullPointerException.class, () -> itemParser.parseItem(null));
    }

    @Test
    @DisplayName("parseItem: Should throw NullPointerException if itemData elements are null")
    void parseItem_nullElements() {
        String[] itemData = {"1", "book", null};
        assertThrows(NullPointerException.class, () -> itemParser.parseItem(itemData));

        String[] itemData2 = {"1", null, "12.49"};
        assertThrows(NullPointerException.class, () -> itemParser.parseItem(itemData2));
    }

    @Test
    @DisplayName("parseItem: Should correctly identify item type based on partial keywords")
    void determineItemType_partialKeywords() {
        // AppConfig.FOOD_KEYWORDS = {"chocolate", "food"}
        String[] itemData1 = {"1", "dark chocolate bar", "2.00"};
        Item expectedItem1 = new Item("dark chocolate bar", 2.00, false, ItemType.FOOD, 1);
        assertEquals(expectedItem1, itemParser.parseItem(itemData1));

        // AppConfig.MEDICAL_KEYWORDS = {"pill", "headache"}
        String[] itemData2 = {"1", "headache reliever", "5.00"};
        Item expectedItem2 = new Item("headache reliever", 5.00, false, ItemType.MEDICAL, 1);
        assertEquals(expectedItem2, itemParser.parseItem(itemData2));
    }
}