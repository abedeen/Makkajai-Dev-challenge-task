package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.util.PriceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Service responsible for calculating taxes on items.
 */
public class TaxService {

    private static final Logger logger = LoggerFactory.getLogger(TaxService.class);

    private final double basicTaxRate;
    private final double importTaxRate;

    public TaxService(double basicTaxRate, double importTaxRate) {
        if (basicTaxRate < 0 || importTaxRate < 0) {
            throw new IllegalArgumentException("Tax rates cannot be negative.");
        }
        this.basicTaxRate = basicTaxRate;
        this.importTaxRate = importTaxRate;
    }

    /**
     * Calculates the total tax for a given item.
     * Includes basic sales tax and import duty.
     * @param item The item for which to calculate tax.
     * @return The calculated tax, rounded to the nearest 0.05.
     */
    public double calculateTax(Item item) {
        Objects.requireNonNull(item, "Item cannot be null for tax calculation.");
        double tax = 0;

        // Add basic sales tax if not exempt
        if (!item.getType().isExempt()) {
            tax += item.getPrice() * basicTaxRate;
            logger.debug("Applied basic tax ({}%) for non-exempt item '{}'. Current tax: {}",
                    (basicTaxRate * 100), item.getName(), tax);
        }

        // Add import duty if applicable
        if (item.isImported()) {
            tax += item.getPrice() * importTaxRate;
            logger.debug("Applied import tax ({}%) for imported item '{}'. Current tax: {}",
                    (importTaxRate * 100), item.getName(), tax);
        }

        // Apply quantity to tax calculation
        tax *= item.getQuantity();

        double roundedTax = PriceUtil.roundToNearest0_05(tax);
        logger.debug("Final calculated tax for item '{}' (quantity {}): {}", item.getName(), item.getQuantity(), roundedTax);
        return roundedTax;
    }
}