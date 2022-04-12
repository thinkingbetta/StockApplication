package com.accenture.stocks.commands;

import com.accenture.stocks.cliscanner.ScannerFormatting;
import com.accenture.stocks.persistence.DbOperations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteCommand extends Command {
    private final String name = "delete";
    private final Boolean value = false;
    private final Boolean deleteValue = true;
    private final String[] tables = {"company_pricedate", "company_industry", "industry", "company", "pricedate"};
    private final Connection connection;
    private Scanner scanner;
    private DbOperations dbOperations;

    public DeleteCommand(Connection connection, Scanner scanner, DbOperations dbOperations) {
        this.connection = connection;
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        System.out.println("Do you want to delete all data from database? [true or false]");
        Boolean input = Boolean.valueOf(new ScannerFormatting(this.scanner).getFormattedCommand());
        //if the user insert true, then every table will be deleted and the autoincrement will be set to zero.
        if (input.equals(deleteValue)) {
            for (String t : tables) {
                try {
                    dbOperations.deleteDataFromTable(t);
                    dbOperations.autoincrementToZero(t);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Data was deleted successfully.");
        } else {
            System.out.println("Data was not deleted from database.");
        }
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
