package com.accenture.stocks;


import com.accenture.stocks.cliscanner.ScannerFormatting;
import com.accenture.stocks.commands.Command;
import com.accenture.stocks.commands.DeleteCommand;
import com.accenture.stocks.commands.ExitCommand;
import com.accenture.stocks.commands.ImportCommand;
import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.persistence.DatabaseConnector;
import com.accenture.stocks.persistence.DbOperations;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StockApp {

    private void starter() {

        System.out.println("Welcome to StockApp!");

        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection connection = null;
        try {
            connection = databaseConnector.getConnection();
        } catch (SQLException sqlException) {
            System.out.println("Error in establishing a database connection.");
            return;
        }

        ArrayList<Stock> stocks = new ArrayList<>();
        DbOperations dbOperations = new DbOperations(connection);

        Scanner scanner = new Scanner(System.in);
        ScannerFormatting scannerFormatting = new ScannerFormatting(scanner);

        Command exit = new ExitCommand();
        Command importInDatabaseCommand = new ImportCommand(scanner,dbOperations);
        Command deleteCommand = new DeleteCommand(connection, scanner, dbOperations);

        ArrayList<Command> commands = new ArrayList<>();
        commands.add(exit);
        commands.add(importInDatabaseCommand);
        commands.add(deleteCommand);



        boolean suspend = false;
        while (!suspend) {
            System.out.println("\nWhat do you want to do?");
            for (Command command : commands) {
                System.out.println(command.getName());
            }
            String input = scannerFormatting.getFormattedCommand();

            for (Command command : commands) {
                if (command.getName().equals(input)) {
                    suspend = command.execute();
                }
            }


        }


    }

    public static void main(String[] args) {
        new StockApp().starter();


    }
}
