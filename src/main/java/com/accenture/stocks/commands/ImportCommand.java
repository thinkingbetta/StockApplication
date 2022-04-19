package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.FromCSVFormatter;
import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.persistence.DBOperations;


import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class ImportCommand extends Command {
    private final boolean value = false;
    private final String name = "import";
    private final String relativePath = "data\\";
    private final String defaultFile = "STOCK_DATA.csv";

    private Scanner scanner;
    private Connection connection;
    private DBOperations dbOperations;


    public ImportCommand(Scanner scanner, DBOperations dbOperations){
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }



    @Override
    public boolean execute(){
        System.out.println("Which file do you want to import? [d for default or type filename.csv]");
        String input = scanner.nextLine();
        if (input.equals("d")) {
            insertStocksInDBFromCSV(relativePath + defaultFile);
        } else {
            insertStocksInDBFromCSV(relativePath + input);
        }
      /*  for (Stock stock : this.stocks) {
            System.out.println(stock.toString());
        }*/
        return value;
    }

    private void insertStocksInDBFromCSV(String fileName) {
        FromCSVFormatter csvFormatting = new FromCSVFormatter();

        try (FileReader fileReader = new FileReader(fileName)) {
            BufferedReader br = new BufferedReader(fileReader);
            br.readLine(); //TODO controlla che la stringa iniziale sia corretta o if o eccezione personalizzata
            String line = br.readLine();
            int count = 0;
            System.out.println("Importing stocks into database...");
            while (line != null) {
                String[] attributes = line.split(";");
                Stock stock = new Stock(attributes[0], csvFormatting.getFormattedPrice(attributes[1]),
                        csvFormatting.getFormattedLocalDate(attributes[2]),attributes[3]);
                dbOperations.importStockInDB(stock);
                count++;
                line = br.readLine();
            }
            //TODO fare la print non delle linee che sono importate nel database, ma dalla count delle company
            System.out.println(count + " lines successfully imported into database");
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Typo in the file name or check if file exists in data/ folder");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getName() {
        return name;
    }
}
