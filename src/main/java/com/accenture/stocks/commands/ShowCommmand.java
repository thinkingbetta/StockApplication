package com.accenture.stocks.commands;

import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.formatters.FromDBFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.sql.*;
import java.util.Scanner;

public class ShowCommmand extends Command{
    public final String name = "show";
    public final Boolean value = false;
    private Scanner scanner;
    private DBOperations dbOperations;

    public ShowCommmand( Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        FromDBFormatter fromDBFormatter = new FromDBFormatter();

        System.out.println("Enter company id:");
        int input = Integer.parseInt(scanner.nextLine());
        try {
            ResultSet resultSet = dbOperations.selectTenLastStocksByCompanyId(input);
            int count = 0;
            Boolean hasNext = resultSet.next();
            while (hasNext){
                int companyId = resultSet.getInt(1);


                Stock stock = new Stock(resultSet.getString(2),resultSet.getBigDecimal(3),
                        fromDBFormatter.fromDateSQLtoLocalDate(resultSet),resultSet.getString(5));

                System.out.println( "==============ID:" + companyId + "==============\n" + stock );
                count ++;
                hasNext = resultSet.next();
            }
            System.out.println(count + " results.");

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
