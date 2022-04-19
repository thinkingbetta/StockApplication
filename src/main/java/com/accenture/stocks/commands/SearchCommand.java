package com.accenture.stocks.commands;

import com.accenture.stocks.formatters.ScannerFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * This class allows to find the name of a company given the first letters of its name.
 */
public class SearchCommand extends Command {
    private final Boolean VALUE = false;
    private final String NAME = "search";
    private Scanner scanner;
    private DBOperations dbOperations;

    public SearchCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method takes input from user, use it to find the name of one or more companies and print the corresponding
     * company_id.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        System.out.println("Type first characters of a company to find its ID [example ac]");
        String input = new ScannerFormatter(scanner).getFormattedSqlLikeString();
        try {
            ResultSet resultSet = this.dbOperations.executeSelectLikeStartsWith(input);
            int count = 0;
            Boolean hasNext = resultSet.next();
            while (hasNext) {
                int id = resultSet.getInt(1);
                String companyName = resultSet.getString(2);
                count++;
                System.out.println("==============ID:" + id + "==============" + "\n\t" + companyName +
                        "\n==================================\n");
                hasNext = resultSet.next();
            }
            System.out.println(count + " companies found.");
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
