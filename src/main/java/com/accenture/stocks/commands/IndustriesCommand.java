package com.accenture.stocks.commands;

import com.accenture.stocks.persistence.DBOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * This class allows to list all industries with their id and the corresponding number of stocks.
 */
public class IndustriesCommand extends Command {
    private final Boolean VALUE = false;
    private final String NAME = "industries";
    private Scanner scanner;
    private DBOperations dbOperations;

    public IndustriesCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method counts the number of industries, then for every industry counts how many companies and print the
     * result.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        try {
            ResultSet resultSetNumIndustry = dbOperations.selectCountFromTableIndustry();
            resultSetNumIndustry.next();
            int industriesNumber = resultSetNumIndustry.getInt("num");
            System.out.println("Number of industries : " + industriesNumber);
            ResultSet resultSetNumStocksEveryIndustry = dbOperations.selectCountCompaniesFromIndustryTable();
            Boolean hasNext = resultSetNumStocksEveryIndustry.next();
            while (hasNext) {
                String industryName = resultSetNumStocksEveryIndustry.getString(1);
                int companiesNumber = resultSetNumStocksEveryIndustry.getInt(2);
                System.out.println("Industry name: " + industryName + "\nStocks number: " + companiesNumber + "\n");
                hasNext = resultSetNumStocksEveryIndustry.next();
            }
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
