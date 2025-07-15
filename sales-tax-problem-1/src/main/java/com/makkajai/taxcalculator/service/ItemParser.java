package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.config.AppConfig;
import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.ItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service to parse raw string input into an Item object.
 */
public class ItemParser {

    private static final Logger logger = LoggerFactory.getLogger(ItemParser.class);
    // Regex to parse input like "1 imported bottle of perfume at 27.99"
    private static final Pattern ITEM_PATTERN = Pattern.compile("(\\d+)\\s(.+)\\sat\\s(\\d+\\.\\d{2})");

    public Item parseItem(String inputLine) {
        if (inputLine == null || inputLine.trim().isEmpty()) {
            throw new IllegalArgumentException("Input line cannot be empty.");
        }

        Matcher matcher = ITEM_PATTERN.matcher(inputLine);
        if (!matcher.matches()) {
            logger.warn("Input string did not match expected pattern: {}", inputLine);
            throw new IllegalArgumentException("Invalid item format. Expected: 'QUANTITY ITEM_NAME at PRICE'");
        }

        try {
            int quantity = Integer.parseInt(matcher.group(1));
            String name = matcher.group(2).trim();
            double price = Double.parseDouble(matcher.group(3));

            boolean isImported = name.toLowerCase().contains("imported");
            // Clean the name after checking for 'imported'
            name = name.replace("imported ", "").trim();

            ItemType type = determineItemType(name);

            return new Item(name, price, isImported, type, quantity);
        } catch (NumberFormatException e) {
            logger.error("Failed to parse number from input: {}", inputLine, e);
            throw new IllegalArgumentException("Invalid number format in input. Price and quantity must be valid numbers.", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during item parsing for input: {}", inputLine, e);
            throw new RuntimeException("Failed to parse item due to an unexpected error.", e);
        }
    }

    /**
     * Determines the ItemType based on keywords in the item name.
     * This logic can be externalized to a configuration file or a more sophisticated rules engine
     * for easier updates without code changes.
     */
    private ItemType determineItemType(String name) {
        String lowerCaseName = name.toLowerCase();
        for (String keyword : AppConfig.BOOK_KEYWORDS) {
            if (lowerCaseName.contains(keyword)) {
                return ItemType.BOOK;
            }
        }
        for (String keyword : AppConfig.FOOD_KEYWORDS) {
            if (lowerCaseName.contains(keyword)) {
                return ItemType.FOOD;
            }
        }
        for (String keyword : AppConfig.MEDICAL_KEYWORDS) {
            if (lowerCaseName.contains(keyword)) {
                return ItemType.MEDICAL;
            }
        }
        return ItemType.OTHER;
    }
}