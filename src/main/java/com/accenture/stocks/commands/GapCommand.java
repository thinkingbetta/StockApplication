package com.accenture.stocks.commands;

import com.accenture.stocks.persistence.DBOperations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * This class allows to show the difference between the highest and the lowest price of a stock.
 */
public class GapCommand extends Command {
    private final String NAME = "gap";
    private final Boolean VALUE = false;
    private Scanner scanner;
    private DBOperations dbOperations;

    public GapCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method selects the highest and lowest price of a stock, makes a subtraction and print the result.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        System.out.println("Enter company ID: ");
        try {
            int input = Integer.parseInt(scanner.nextLine());
            ResultSet resultSetMax = dbOperations.getMaxPriceStock(input);
            BigDecimal maxPrice = resultSetMax.getBigDecimal(3);
            ResultSet resultSetMin = dbOperations.getMinPriceStock(input);
            BigDecimal minPrice = resultSetMin.getBigDecimal(3);
            BigDecimal gapValue = maxPrice.subtract(minPrice);
            System.out.println("The difference between the highest and lowest price is " + gapValue + "€");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException n) {
            System.out.println("Enter a valid id!");
        }
        return VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
