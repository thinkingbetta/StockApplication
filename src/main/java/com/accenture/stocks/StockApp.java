package com.accenture.stocks;

import com.accenture.stocks.commands.*;
import com.accenture.stocks.formatters.ScannerFormatter;
import com.accenture.stocks.persistence.DatabaseConnector;
import com.accenture.stocks.persistence.DBOperations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StockApp {

    /**
     * This method connects to the database, creates an ArrayList<> of commands and allows the user to interact with the
     * application.
     */
    private void starter() {

        System.out.println("Welcome to StockApp!");

        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection connection;
        try {
            connection = databaseConnector.getConnection();
        } catch (SQLException sqlException) {
            System.out.println("Error in establishing a database connection.");
            return;
        }

        DBOperations dbOperations = new DBOperations(connection);

        Scanner scanner = new Scanner(System.in);
        ScannerFormatter scannerFormatting = new ScannerFormatter(scanner);

        Command exit = new ExitCommand();
        Command importInDatabaseCommand = new ImportCommand(scanner, dbOperations);
        Command deleteCommand = new DeleteCommand(scanner, dbOperations);
        Command searchCommand = new SearchCommand(scanner, dbOperations);
        Command showCommand = new ShowCommand(scanner, dbOperations);
        Command addCommand = new AddCommand(scanner, dbOperations);
        Command maxCommand = new MaxCommand(scanner, dbOperations);
        Command minCommand = new MinCommand(scanner, dbOperations);
        Command gapCommand = new GapCommand(scanner, dbOperations);
        Command industriesCommand = new IndustriesCommand(scanner, dbOperations);
        Command updateIndustryCommand = new IndustryUpdateCommand(scanner, dbOperations);
        Command exportCommand = new ExportCommand(scanner, dbOperations);

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
        commands.add(industriesCommand);
        commands.add(updateIndustryCommand);
        commands.add(exportCommand);

        boolean suspend = false;
        while (!suspend) {
            System.out.println("\nChoose a command: ");
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

    /**
     * This method is the entry point of the application.
     *
     * @param args String array
     */
    public static void main(String[] args) {
        new StockApp().starter();

    }
}
