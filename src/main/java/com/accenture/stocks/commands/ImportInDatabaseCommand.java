package com.accenture.stocks.commands;

import com.accenture.stocks.cliscanner.FromCSVFormatting;
import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.persistence.DbOperations;


import java.io.*;
import java.sql.*;

public class ImportInDatabaseCommand extends Command {
    private final boolean value = false;
    private final String name = "importindb";
    private Connection connection;
    private DbOperations dbOperations;


    public ImportInDatabaseCommand(Connection connection, DbOperations dbOperations){
        this.connection = connection;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute(){
        FromCSVFormatting csvFormatting = new FromCSVFormatting();
        String fileName = "data\\STOCK_DATA.csv";
        try (FileReader fileReader = new FileReader(fileName)) {
            BufferedReader br = new BufferedReader(fileReader);
            br.readLine(); //TODO controlla che la stringa iniziale sia corretta o if o eccezione personalizzata
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(";");
                Stock stock = new Stock(attributes[0], csvFormatting.getFormattedPrice(attributes[1]),
                        csvFormatting.getFormattedLocalDate(attributes[2]),attributes[3]);
                this.dbOperations.insertCompanyInTable(stock);


                line = br.readLine();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Typo in the file name or check if file exists in data/ folder");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Companies successfully imported");
        return value;
    }




    @Override
    public String getName() {
        return name;
    }
}
