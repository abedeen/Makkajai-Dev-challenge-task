package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.config.AppConfig;
import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.ItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service to parse raw string input into an Item object.
 */
public class ItemParser {

    private static final Logger logger = LoggerFactory.getLogger(ItemParser.class);

    // Regex to parse input like "1 imported bottle of perfume at 27.99"
    private static final Pattern ITEM_PATTERN = Pattern.compile("(\\d+)\\s(.+)\\sat\\s(\\d+\\.\\d{2})");

    public boolean isvalid(String[] item){

        if(Arrays.stream(item).filter(p-> p.length()==0).count()>0) return false;
        return true;
    }
    public Matcher buildMatch(String inputLine){

        Matcher matcher = ITEM_PATTERN.matcher(inputLine);

        return matcher;
    }
    public String[] toStringStage1(Matcher matcher) {
        String[] item = new String[3]; // Declare and initialize a String array of size 3
        item[0] = matcher.group(1); //quantity
        item[1] = matcher.group(2); // name
        item[2] = matcher.group(3); // price
        return item;
    }
    public Item parseItem(String[] item){

        boolean isImported = item[1].toLowerCase().contains("imported");

        ItemType type = determineItemType(item[2]);

        return new Item(item[1], Double.parseDouble(item[3]), isImported, type, Integer.parseInt(item[0]));

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