package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.FromDBFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class MinCommand extends Command{
    final private Boolean value = false;
    final private String name = "min";

    private Scanner scanner;
    private DBOperations dbOperations;

    public MinCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        System.out.println("Enter company ID: ");
        int input = Integer.parseInt(scanner.nextLine());

        try {
            ResultSet resultSet = dbOperations.getMinPriceStock(input);
            String companyName = resultSet.getString(1);
            LocalDate date = new FromDBFormatter().fromDateSQLtoLocalDate(resultSet, 2);
            BigDecimal minPrice = resultSet.getBigDecimal(3);


            System.out.println("\nCompany name: " + companyName + "\nMin price: " + minPrice + "€"+ "\nDate: " + date);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return value;
    }



    @Override
    public String getName() {
        return name;
    }
}
