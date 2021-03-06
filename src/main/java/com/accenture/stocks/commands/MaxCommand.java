package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.FromDBFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * This class allows to select the highest price of a stock.
 */
public class MaxCommand extends Command {
    final private Boolean VALUE = false;
    final private String NAME = "max";

    private Scanner scanner;
    private DBOperations dbOperations;

    public MaxCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * Given a company id, this method gets the highest price of a stock and print it.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        System.out.println("Enter company ID: ");
        int input = Integer.parseInt(scanner.nextLine());
        try {
            ResultSet resultSet = dbOperations.getMaxPriceStock(input);
            String companyName = resultSet.getString(1);
            LocalDate date = new FromDBFormatter().fromDateSQLtoLocalDate(resultSet, 2);
            BigDecimal maxPrice = resultSet.getBigDecimal(3);
            System.out.println("\nCompany name: " + companyName + "\nMax price: " + maxPrice + "€" + "\nDate: " + date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
