package com.makkajai.taxcalculator.service;

import com.makkajai.taxcalculator.config.AppConfig;
import com.makkajai.taxcalculator.model.Item;
import com.makkajai.taxcalculator.model.Receipt;
import com.makkajai.taxcalculator.model.ReceiptItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ProcessRequest {
    ItemParser itemParser;

    public ProcessRequest(ItemParser itemParser){
        this.itemParser = itemParser;
    }
    public  List<String> valide(List<String> data){
      //  itemParser.parseItem(line);
        return null;
    }

    public List<String[]> toStage1(List<String> data){
        List<String[]> result= new ArrayList<>();
        for(String item: data) {

         //   Matcher matcher =
            String[] matchString = itemParser.buildMatch(item);
            result.add(matchString);
        }
    return result;
    }
    public List<String[]> toStage2(List<String[]> data){
        List<String[]> result= new ArrayList<>();
        for(String[] item: data) {

            boolean isvalid = itemParser.isvalid(item);
            if(isvalid)
                result.add(item);
        }
        return result;
    }
    public List<Item> buildItem(List<String[]> data){
        List<Item> result= new ArrayList<>();
        for(String[] item: data)
                result.add(itemParser.parseItem(item));

        return result;
    }
    public Receipt calculateReceipt(List<Item> data) {
        TaxService taxService = new TaxService(AppConfig.BASIC_TAX_RATE, AppConfig.IMPORT_TAX_RATE);
        ItemParser itemParser = new ItemParser();
        Receipt receipt = new Receipt(taxService);
        receipt.addAllItems(data);
        return receipt;
    }

}
