package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.ItemType;
import com.makkajai.taxcalculator.model.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessRequestTest {

    @Mock
    private ItemParser itemParser; // Mock ItemParser dependency

    @InjectMocks
    private ProcessRequest processRequest; // Inject mocks into ProcessRequest

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    // --- Tests for toStage1(List<String> data) ---

    @Test
    @DisplayName("toStage1: Should convert raw input lines to string arrays")
    void toStage1_shouldConvertInputLines() {
        List<String> inputData = Arrays.asList(
                "1 book at 12.49",
                "1 music CD at 14.99"
        );
        String[] parsedItem1 = {"1", "book", "12.49"};
        String[] parsedItem2 = {"1", "music CD", "14.99"};

        when(itemParser.buildMatch("1 book at 12.49")).thenReturn(parsedItem1);
        when(itemParser.buildMatch("1 music CD at 14.99")).thenReturn(parsedItem2);

        List<String[]> result = processRequest.toStage1(inputData);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertArrayEquals(parsedItem1, result.get(0));
        assertArrayEquals(parsedItem2, result.get(1));
        verify(itemParser, times(2)).buildMatch(anyString()); // Verify buildMatch was called twice
    }

    @Test
    @DisplayName("toStage1: Should handle empty input list")
    void toStage1_emptyInput() {
        List<String> inputData = Collections.emptyList();
        List<String[]> result = processRequest.toStage1(inputData);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemParser, never()).buildMatch(anyString()); // buildMatch should not be called
    }

    // --- Tests for toStage2(List<String[]> data) ---

    @Test
    @DisplayName("toStage2: Should filter out invalid item arrays")
    void toStage2_shouldFilterInvalidItems() {
        String[] validItem1 = {"1", "book", "12.49"};
        String[] invalidItem1 = {"", "music CD", "14.99"}; // Invalid due to empty quantity
        String[] validItem2 = {"1", "imported perfume", "47.50"};
        String[] invalidItem2 = {"1", "chocolate", ""}; // Invalid due to empty price

        List<String[]> stage1Data = Arrays.asList(validItem1, invalidItem1, validItem2, invalidItem2);

        when(itemParser.isvalid(validItem1)).thenReturn(true);
        when(itemParser.isvalid(invalidItem1)).thenReturn(false);
        when(itemParser.isvalid(validItem2)).thenReturn(true);
        when(itemParser.isvalid(invalidItem2)).thenReturn(false);

        List<String[]> result = processRequest.toStage2(stage1Data);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertArrayEquals(validItem1, result.get(0));
        assertArrayEquals(validItem2, result.get(1));
        verify(itemParser, times(4)).isvalid(any(String[].class)); // isvalid called for each item
    }

    @Test
    @DisplayName("toStage2: Should return empty list if all items are invalid")
    void toStage2_allInvalidItems() {
        String[] invalidItem1 = {"", "item1", "10.00"};
        String[] invalidItem2 = {"1", "", "20.00"};
        List<String[]> stage1Data = Arrays.asList(invalidItem1, invalidItem2);

        when(itemParser.isvalid(any(String[].class))).thenReturn(false); // Mock all as invalid

        List<String[]> result = processRequest.toStage2(stage1Data);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- Tests for buildItem(List<String[]> data) ---

    @Test
    @DisplayName("buildItem: Should convert string arrays to Item objects")
    void buildItem_shouldConvertToArrayToItem() {
        String[] itemData1 = {"1", "book", "12.49"};
        String[] itemData2 = {"1", "music CD", "14.99"};

        Item item1 = new Item("book", 12.49, false, ItemType.BOOK, 1);
        Item item2 = new Item("music CD", 14.99, false, ItemType.OTHER, 1);

        List<String[]> validatedData = Arrays.asList(itemData1, itemData2);

        when(itemParser.parseItem(itemData1)).thenReturn(item1);
        when(itemParser.parseItem(itemData2)).thenReturn(item2);

        List<Item> result = processRequest.buildItem(validatedData);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(item1, result.get(0));
        assertEquals(item2, result.get(1));
        verify(itemParser, times(2)).parseItem(any(String[].class)); // parseItem called for each valid item
    }

    @Test
    @DisplayName("buildItem: Should handle empty list correctly")
    void buildItem_emptyList() {
        List<String[]> validatedData = Collections.emptyList();
        List<Item> result = processRequest.buildItem(validatedData);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemParser, never()).parseItem(any(String[].class));
    }

    // --- Tests for calculateReceipt(List<Item> data) ---
    // Note: calculateReceipt creates its own TaxService and Receipt.
    // For unit testing ProcessRequest in isolation, we might just check that
    // the Receipt is created and items are added.
    // Full receipt calculation is already covered in ReceiptTest.
    @Test
    @DisplayName("calculateReceipt: Should create a Receipt and add all items")
    void calculateReceipt_shouldCreateReceiptAndAddItems() {
        Item item1 = new Item("book", 12.49, false, ItemType.BOOK, 1);
        Item item2 = new Item("music CD", 14.99, false, ItemType.OTHER, 1);
        List<Item> items = Arrays.asList(item1, item2);

        // We can't mock TaxService directly injected into Receipt inside calculateReceipt
        // Instead, we verify that the Receipt object's addAllItems method is called.
        // For a true unit test of calculateReceipt's logic, dependency injection of TaxService
        // and Receipt might be considered in ProcessRequest's constructor.
        // For now, we'll verify interaction at the Receipt level.

        Receipt receipt = processRequest.calculateReceipt(items);

        assertNotNull(receipt);
        assertEquals(2, receipt.getReceiptLines().size()); // Assuming TaxService calculates correctly for items
        // Verify that the items passed to calculateReceipt are reflected in the receipt
        // This implicitly tests that addItem/addAllItems were called on the new Receipt object.
        assertTrue(receipt.getReceiptLines().stream().anyMatch(ri -> ri.getItem().equals(item1)));
        assertTrue(receipt.getReceiptLines().stream().anyMatch(ri -> ri.getItem().equals(item2)));
    }

    @Test
    @DisplayName("calculateReceipt: Should handle empty item list")
    void calculateReceipt_emptyItemList() {
        List<Item> items = Collections.emptyList();
        Receipt receipt = processRequest.calculateReceipt(items);

        assertNotNull(receipt);
        assertTrue(receipt.getReceiptLines().isEmpty());
        assertEquals(0.0, receipt.getTotalTax(), 0.001);
        assertEquals(0.0, receipt.getTotal(), 0.001);
    }
}