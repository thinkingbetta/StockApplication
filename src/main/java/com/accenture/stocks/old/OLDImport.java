package com.accenture.stocks.old;

import com.accenture.stocks.formatters.FromCSVFormatting;
import com.accenture.stocks.entities.Stock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class OLDImport {
    private final String name = "";
    private final Boolean value = false;
    private ArrayList<Stock> stocks;
    private Scanner scanner;
    private final String relativePath = "data\\";
    private final String defaultFile = "STOCK_DATA.csv";

    public OLDImport(ArrayList<Stock> stocks, Scanner scanner) {
        this.stocks = stocks;
        this.scanner = scanner;

    }


    public boolean execute() {
        System.out.println("Which file do you want to import? [d for default or type filename.csv]");
        String input = scanner.nextLine();
        if (input.equals("d")) {
            this.stocks = readStocksFromCsv(relativePath + defaultFile);
        } else {
            this.stocks = readStocksFromCsv(relativePath + input);
        }
        for (Stock stock : this.stocks) {
            System.out.println(stock.toString());
        }
        return value;
    }

    public ArrayList<Stock> readStocksFromCsv(String fileName) {
        ArrayList<Stock> stocks = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName)) {
            BufferedReader br = new BufferedReader(fileReader);
            br.readLine(); //TODO controlla che la stringa iniziale sia corretta o if o eccezione personalizzata
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");
                FromCSVFormatting csvFormatting = new FromCSVFormatting();
                Stock stock = new Stock(attributes[0],
                csvFormatting.getFormattedPrice(attributes[1]),csvFormatting.getFormattedLocalDate(attributes[2]),attributes[3]);
                stocks.add(stock);
                line = br.readLine();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Typo in the file name or check if file exists in data/ folder");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stocks;
    }


    public String getName() {
        return name;
    }
}
