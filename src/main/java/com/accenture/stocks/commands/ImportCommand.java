package com.accenture.stocks.commands;

import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.services.StockService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ImportCommand extends Command {
    private final String name = "import";
    private final Boolean value = false;
    private ArrayList<Stock> stocks;
    private Scanner scanner;
    private StockService stockService;
    private final String relativePath = "data\\";
    private final String defaultFile = "STOCK_DATA.csv";

    public ImportCommand(ArrayList<Stock> stocks, Scanner scanner, StockService stockService) {
        this.stocks = stocks;
        this.scanner = scanner;
        this.stockService = stockService;
    }

    @Override
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
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(String.valueOf(pathToFile)))) {
            br.readLine(); //TODO controlla che la stringa iniziale sia corretta o if o eccezione personalizzata
            String line = br.readLine();

            while (line  != null) {
                String[] attributes = line.split(";");


                Stock stock = this.stockService.createStock(attributes);

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







    @Override
    public String getName() {
        return name;
    }
}
