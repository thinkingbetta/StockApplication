package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.ScannerFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * This class allows to delete all data from database.
 */
public class DeleteCommand extends Command {
    private final String NAME = "delete";
    private final Boolean VALUE = false;
    private final String[] TABLES = {"company_pricedate", "company_industry", "industry", "company", "pricedate"};
    private Scanner scanner;
    private DBOperations dbOperations;

    public DeleteCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method asks for confirmation, then deletes all the data from the tables and sets autoincrement to 0.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        System.out.println("Do you want to delete all data from database? " +
                "[true to delete or press anything to NOT delete]");
        Boolean deleteIsWhatIWant = Boolean.valueOf(new ScannerFormatter(this.scanner).getFormattedString());
        if (deleteIsWhatIWant) {
            for (String t : TABLES) {
                try {
                    dbOperations.deleteDataFromTable(t);
                    dbOperations.autoincrementToZero(t);
                    System.out.println("Data in " + t + " were successfully deleted.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Data was not deleted from database.");
        }
        return VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
