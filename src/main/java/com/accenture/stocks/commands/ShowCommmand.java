package com.accenture.stocks.commands;

import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.persistence.DBOperations;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class ShowCommmand extends Command{
    public final String name = "showid";
    public final Boolean value = false;
    private Connection connection;
    private Scanner scanner;
    private DBOperations dbOperations;

    public ShowCommmand(Connection connection, Scanner scanner, DBOperations dbOperations) {
        this.connection = connection;
        this.scanner = scanner;

        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        System.out.println("Enter company id:");
        int input = Integer.parseInt(scanner.nextLine());
        try {
            ResultSet resultSet = dbOperations.getTenLastStocksByCompanyId(input);
            int count = 0;
            Boolean hasNext = resultSet.next();
            while (hasNext){
                int companyId = resultSet.getInt(1);
                String companyName = resultSet.getString(2);
                BigDecimal price = resultSet.getBigDecimal(3);
                Date sqlDate = resultSet.getDate(4);
                LocalDate date = sqlDate.toLocalDate();
                String industryName = resultSet.getString(5);

                Stock stock = new Stock(companyName,price,date,industryName);

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
