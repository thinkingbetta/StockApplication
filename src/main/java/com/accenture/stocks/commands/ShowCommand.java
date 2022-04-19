package com.accenture.stocks.commands;

import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.formatters.FromDBFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.sql.*;
import java.util.Scanner;

/**
 * This class allows to show the last ten prices of a stock.
 */
public class ShowCommand extends Command {
    public final String NAME = "show";
    public final Boolean VALUE = false;
    private Scanner scanner;
    private DBOperations dbOperations;

    public ShowCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method reads the company_id written by the user, creates a stock and shows the last ten prices
     * corresponding to the id.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        FromDBFormatter fromDBFormatter = new FromDBFormatter();
        System.out.println("Enter company id:");
        try {
            int input = Integer.parseInt(scanner.nextLine());
            ResultSet resultSet = dbOperations.selectTenLastStocksByCompanyId(input);
            int count = 0;
            Boolean hasNext = resultSet.next();
            while (hasNext) {
                int companyId = resultSet.getInt(1);
                Stock stock = new Stock(resultSet.getString(2), resultSet.getBigDecimal(3),
                        fromDBFormatter.fromDateSQLtoLocalDate(resultSet, 4), resultSet.getString(5));
                System.out.println("==============ID:" + companyId + "==============\n" + stock);
                count++;
                hasNext = resultSet.next();
            }
            System.out.println(count + " results.");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException n) {
            System.out.println("Invalid input, enter a number!");
        }
        return VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
