package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.model.ReceiptOperations;
import com.makkajai.taxcalculator.model.ReceiptLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service responsible for printing the formatted sales receipt to the console.
 */
public class ReceiptPrinter {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptPrinter.class);

    public void printReceipt(ReceiptOperations receipt) {
        if (receipt == null) {
            logger.error("Attempted to print a null receipt.");
            System.err.println("Cannot print an empty receipt.");
            return;
        }

        logger.info("Printing receipt...");
        for (ReceiptLine line : receipt.getReceiptLines()) {
            // Using quantity in the print statement
            System.out.printf("%d %s: %.2f%n",
                    line.getItem().getQuantity(),
                    line.getItem().getName(),
                    line.getTotalPrice());
        }
        System.out.printf("Sales Taxes: %.2f%n", receipt.getTotalTax());
        System.out.printf("Total: %.2f%n", receipt.getTotal());
        logger.info("Receipt printing complete.");
    }
}