package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.cli.TaxCalculatorCli;
import com.makkajai.taxcalculator.config.AppConfig;
import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class InputCliServiceImpl extends  InputService{

    private static final Logger logger = LoggerFactory.getLogger(InputCliServiceImpl.class);
    public String read(Scanner scanner) { // Accept Scanner as a parameter
        String line = "";
        try {
            if (scanner.hasNextLine()) { // Check if there's a next line before reading
                line = scanner.nextLine();
            } else {
                // Handle case where no more lines are available (e.g., end of input)
                return null;
            }
        } catch (InputMismatchException e) {
            logger.error("Input stream error, scanner closed unexpectedly or incorrect input type. " + e.getMessage());
            System.err.println("An unexpected input error occurred. Please try again.");
            line = null; // Set line to null to indicate an error
        } catch (Exception e) {
            logger.error("An unhandled error occurred during application execution: " + e.getMessage());
            System.err.println("An unhandled error occurred. Please contact support.");
            return null; // Set line to null for unhandled errors
        }
        return line;
    }


    public List<String> read() {

        List<String> lines= new ArrayList<>();
        System.out.println("Enter multiple lines of text. Type 'exit' to stop.");
        // Create a single Scanner instance for multiple reads
        try (Scanner consoleScanner = new Scanner(System.in)) {
            String inputLine;

            while (true) {
                inputLine = read(consoleScanner); // Pass the same scanner
                if (inputLine == null)
                    break;
                 else
                if (inputLine.equals(""))
                    break;

                else lines.add(inputLine);

            }
        } catch (Exception e) {
            logger.error("Error managing scanner lifecycle: " + e.getMessage());
        }
    return lines;
    }

}
