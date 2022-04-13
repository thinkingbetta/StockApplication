package com.accenture.stocks;


import com.accenture.stocks.commands.*;
import com.accenture.stocks.formatters.ScannerFormatter;
import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.persistence.DatabaseConnector;
import com.accenture.stocks.persistence.DBOperations;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StockApp {

    //TODO https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html

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
        DBOperations dbOperations = new DBOperations(connection);

        Scanner scanner = new Scanner(System.in);
        ScannerFormatter scannerFormatting = new ScannerFormatter(scanner);

        Command exit = new ExitCommand();
        Command importInDatabaseCommand = new ImportCommand(scanner,dbOperations);
        Command deleteCommand = new DeleteCommand(scanner, dbOperations);
        Command searchCommand = new SearchCommand(scanner, dbOperations);
        Command showCommand = new ShowCommmand(scanner,dbOperations);
        Command addCommand = new AddCommand(scanner, dbOperations);
        Command maxCommand = new MaxCommand(scanner,dbOperations);
        Command minCommand = new MinCommand(scanner,dbOperations);
        Command gapCommand = new GapCommand(scanner, dbOperations);

        ArrayList<Command> commands = new ArrayList<>();
        commands.add(exit);
        commands.add(importInDatabaseCommand);
        commands.add(deleteCommand);
        commands.add(searchCommand);
        commands.add(showCommand);
        commands.add(addCommand);
        commands.add(maxCommand);
        commands.add(minCommand);
        commands.add(gapCommand);



        boolean suspend = false;
        while (!suspend) {
            System.out.println("\nWhat do you want to do?");
            for (Command command : commands) {
                System.out.println(command.getName());
            }
            String input = scannerFormatting.getFormattedString();

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
