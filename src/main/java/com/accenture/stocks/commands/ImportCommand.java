package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.FromCSVFormatter;
import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.persistence.DBOperations;


import java.io.*;
import java.sql.*;
import java.util.Scanner;

/**
 * This class allows to import the data of a .csv file into the database.
 */
public class ImportCommand extends Command {
    private final boolean VALUE = false;
    private final String NAME = "import";
    private final String relativePath = "data\\";
    private final String defaultFile = "STOCK_DATA.csv";

    private Scanner scanner;
    private DBOperations dbOperations;

    public ImportCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method allows the user to choose the file to import.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        System.out.println("Which file do you want to import? [d for default or type filename.csv]");
        String input = scanner.nextLine();
        if (input.equals("d")) {
            insertStocksInDBFromCSV(relativePath + defaultFile);
        } else {
            insertStocksInDBFromCSV(relativePath + input);
        }
        return VALUE;
    }

    /**
     * Given a .csv file, this method reads every line of the file, create stock objects and imports the data of
     * each stock in the database.
     *
     * @param fileName name of the file to import
     */
    private void insertStocksInDBFromCSV(String fileName) {
        FromCSVFormatter csvFormatting = new FromCSVFormatter();
        try (FileReader fileReader = new FileReader(fileName)) {
            BufferedReader br = new BufferedReader(fileReader);
            br.readLine();
            String line = br.readLine();
            int count = 0;
            System.out.println("Importing stocks into database...");
            while (line != null) {
                String[] attributes = line.split(";");
                Stock stock = new Stock(attributes[0], csvFormatting.getFormattedPrice(attributes[1]),
                        csvFormatting.getFormattedLocalDate(attributes[2]), attributes[3]);
                dbOperations.importStockInDB(stock);
                count++;
                line = br.readLine();
            }
            System.out.println(count + " lines successfully imported into database");
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Error : typo in the file name or file doesn't exist in 'data/' folder.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
