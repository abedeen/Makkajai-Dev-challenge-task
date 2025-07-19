package com.makkajai.taxcalculator.cli;

import com.makkajai.taxcalculator.config.AppConfig;
import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.Receipt;
import com.makkajai.taxcalculator.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "taxcalc",
        mixinStandardHelpOptions = true,
        version = "Tax Calculator CLI 1.0",
        description = "Calculates sales tax and total for various items.")
public class TaxCalculatorCli implements Callable<Integer> {

    private InputService inputService;
    private  OutputService outputService;
    private ProcessRequest processRequest;
    public TaxCalculatorCli(InputService inputService, OutputService outputService){
         this.inputService= inputService; this.outputService=outputService; this.processRequest = new ProcessRequest(new ItemParser());
    }
    private static final Logger logger = LoggerFactory.getLogger(TaxCalculatorCli.class);

    @Option(names = {"-d", "--debug"}, description = "Enable debug logging.")
    private boolean debugMode;

    @Override
    public Integer call() {
        if (debugMode) {
            System.setProperty("logback.configurationFile", "src/main/resources/logback-debug.xml");
            logger.info("Debug mode enabled.");
        }
        // Below code read data
        List<String> inputitemList = this.inputService.read();
        logger.info("Input List of items {}",inputitemList.size());

        // Below Code performs regexp on the input data and segregate the data [quantity, name, price]
        List<String[]> matchedList = this.processRequest.toStage1(inputitemList);

        // Below code validate

        List<String[]> validatedList = this.processRequest.toStage2(matchedList);

        List<Item> itemList = this.processRequest.buildItem(validatedList);

        Receipt receipt = this.processRequest.calculateReceipt(itemList);

        ReceiptPrinter printer = new ReceiptPrinter();

        printer.printReceipt(receipt);


        return AppConfig.EXIT_CODE_SUCCESS;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TaxCalculatorCli(new InputCliServiceImpl(),new OutputCliServiceImpl())).execute(args);
        System.exit(exitCode);
    }
}