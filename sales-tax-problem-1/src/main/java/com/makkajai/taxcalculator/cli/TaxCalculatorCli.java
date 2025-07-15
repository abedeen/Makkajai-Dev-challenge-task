package com.makkajai.taxcalculator.cli;

import com.makkajai.taxcalculator.config.AppConfig;
import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.Receipt;
import com.makkajai.taxcalculator.service.ItemParser;
import com.makkajai.taxcalculator.service.ReceiptPrinter;
import com.makkajai.taxcalculator.service.TaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name = "taxcalc",
        mixinStandardHelpOptions = true,
        version = "Tax Calculator CLI 1.0",
        description = "Calculates sales tax and total for various items.")
public class TaxCalculatorCli implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TaxCalculatorCli.class);

    @Option(names = {"-d", "--debug"}, description = "Enable debug logging.")
    private boolean debugMode;

    @Override
    public Integer call() {
        if (debugMode) {
            System.setProperty("logback.configurationFile", "src/main/resources/logback-debug.xml");
            logger.info("Debug mode enabled.");
        }

        try (Scanner scanner = new Scanner(System.in)) {
            ReceiptPrinter printer = new ReceiptPrinter();
            TaxService taxService = new TaxService(AppConfig.BASIC_TAX_RATE, AppConfig.IMPORT_TAX_RATE);
            ItemParser itemParser = new ItemParser();

            while (true) {
                Receipt receipt = new Receipt(taxService); // Inject TaxService
                System.out.println("Enter items for the receipt (e.g., '1 book at 12.49' or type 'done' to finish):");
                logger.info("Starting new receipt input.");

                while (true) {
                    System.out.print("> ");
                    String line = scanner.nextLine().trim();

                    if (line.equalsIgnoreCase("done")) {
                        logger.debug("User finished input for current receipt.");
                        break;
                    }
                    if (line.isEmpty()) {
                        System.out.println("Input cannot be empty. Please enter an item or 'done'.");
                        continue;
                    }

                    try {
                        Item item = itemParser.parseItem(line);
                        receipt.addItem(item);
                        logger.debug("Added item: {}", item.getName());
                    } catch (IllegalArgumentException e) {
                        logger.error("Invalid input format: {}", line, e);
                        System.err.println("Error: " + e.getMessage() + ". Please use format 'QUANTITY ITEM_NAME at PRICE' (e.g., '1 book at 12.49').");
                    } catch (Exception e) {
                        logger.error("An unexpected error occurred while parsing item: {}", line, e);
                        System.err.println("An unexpected error occurred. Please try again.");
                    }
                }

                if (receipt.getReceiptLines().isEmpty()) {
                    System.out.println("No items entered for this receipt.");
                    logger.warn("Receipt was empty.");
                } else {
                    printer.printReceipt(receipt);
                    logger.info("Receipt printed successfully.");
                }


                System.out.println("\nDo you want to process another receipt? (yes/no)");
                String continueInput = scanner.nextLine().trim();
                if (!continueInput.equalsIgnoreCase("yes")) {
                    logger.info("User chose to exit.");
                    break;
                }
            }
        } catch (InputMismatchException e) {
            logger.error("Input stream error, scanner closed unexpectedly.", e);
            System.err.println("An unexpected input error occurred. Exiting.");
            return AppConfig.EXIT_CODE_INPUT_ERROR;
        } catch (Exception e) {
            logger.error("An unhandled error occurred during application execution.", e);
            System.err.println("An unhandled error occurred. Please contact support.");
            return AppConfig.EXIT_CODE_UNEXPECTED_ERROR;
        }
        return AppConfig.EXIT_CODE_SUCCESS;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TaxCalculatorCli()).execute(args);
        System.exit(exitCode);
    }
}