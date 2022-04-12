package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.ScannerFormatting;
import com.accenture.stocks.persistence.DBOperations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SearchCommand extends Command {
    private final Boolean value = false;
    private final String name = "search";
    private final String tableName = "company";
    private final String columnName = "company_name";
    private Connection connection;
    private Scanner scanner;
    private DBOperations dbOperations;

    public SearchCommand(Connection connection, Scanner scanner, DBOperations dbOperations) {
        this.connection = connection;
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }


    @Override
    public boolean execute() {
        System.out.println("Type first characters of a company to find its ID [example ac]");
        String input = new ScannerFormatting(scanner).getFormattedSqlLikeString();

        try {
            ResultSet resultSet = this.dbOperations.executeSelectLikeStartsWith(tableName, columnName, input);
            int count = 0;
            Boolean hasNext = resultSet.next();
            while (hasNext){
                int id = resultSet.getInt(1);
                String companyName = resultSet.getString(2);
                count ++;
                System.out.println("==========ID:" + id + "==========" + "\n\t"  + companyName +  "\n==========================\n");
                hasNext= resultSet.next();
            }
            System.out.println( count + " companies found.");

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
