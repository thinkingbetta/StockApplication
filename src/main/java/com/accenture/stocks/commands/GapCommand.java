package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.FromDBFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class GapCommand extends Command {
    private final String NAME = "gap";
    private final Boolean VALUE = false;
    private Scanner scanner;
    private DBOperations dbOperations;

    public GapCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        System.out.println("Enter company ID: ");
        int input = Integer.parseInt(scanner.nextLine());


        try {
            ResultSet resultSetMax = dbOperations.getMaxPriceStock(input);

            //String companyName = resultSet.getString(1);
            //LocalDate date = new FromDBFormatter().fromDateSQLtoLocalDate(resultSet, 2);
            BigDecimal maxPrice = resultSetMax.getBigDecimal(3);

            ResultSet resultSetMin = dbOperations.getMinPriceStock(input);

            //String companyName = resultSet.getString(1);
            //LocalDate date = new FromDBFormatter().fromDateSQLtoLocalDate(resultSet, 2);
            BigDecimal minPrice = resultSetMin.getBigDecimal(3);

            BigDecimal gapValue = maxPrice.subtract(minPrice);
            System.out.println(gapValue);

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
