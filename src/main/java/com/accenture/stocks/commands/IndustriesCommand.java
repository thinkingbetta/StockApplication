package com.accenture.stocks.commands;

import com.accenture.stocks.persistence.DBOperations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class IndustriesCommand extends  Command{
    private final Boolean VALUE = false;
    private final String NAME = "industries";
    private Scanner scanner;
    private DBOperations dbOperations;

    public IndustriesCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }


    @Override
    public boolean execute() {
        //first count the number of industries, than for every industry count how many companies
        try {
            ResultSet resultSetNumIndustry = dbOperations.selectCountFromTableIndustry();
            resultSetNumIndustry.next();
            int industriesNumber = resultSetNumIndustry.getInt("num");
            System.out.println("Number of industries : " + industriesNumber);


            ResultSet resultSetNumStocksEveryIndustry =  dbOperations.selectCountCompaniesFromIndustryTable();
            Boolean hasNext = resultSetNumStocksEveryIndustry.next();
            while(hasNext){
                String industryName =resultSetNumStocksEveryIndustry.getString(1);
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
