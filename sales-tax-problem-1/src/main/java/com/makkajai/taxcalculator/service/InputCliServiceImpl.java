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
    public String read1(Scanner scanner) {
        String temp="";
        try {
            if (scanner.hasNextLine()) {
                temp = scanner.nextLine();
                System.out.print("");
            } else {
                // End of input (stream closed or EOF)
                return null;
            }
        } catch (Exception e) {
            logger.error("An error occurred while reading input: " + e.getMessage());
            System.err.println("An error occurred while reading input.");
            return null;
        }
        return temp;
    }

    public List<String> read() {
        List<String> lines = new ArrayList<>();
        System.out.println("Enter multiple lines of text. Type 'exit' to stop."); // Note: your prompt said 'exit' but code breaks on empty.
        // Create a single Scanner instance for multiple reads
        Scanner scanner = new Scanner(System.in);
        while (true) {
          //  System.out.print("Enter input: ");
            String line = read1(scanner);
            if (line == null || line.isEmpty())
                break;
            else lines.add(line);
        }
        return lines;
    }



}
