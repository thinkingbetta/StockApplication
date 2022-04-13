package com.accenture.stocks.commands;

import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.persistence.DBOperations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AddCommand extends Command {
    private final Boolean value = false;
    public final String name = "add";
    // public final String unknownIndustry = "n/a";
    private Scanner scanner;
    private DBOperations dbOperations;

    public AddCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        System.out.println("Enter ID company:");
        int companyId = Integer.parseInt(scanner.nextLine());

        //check if the id corresponds to a company inserted in the database and return company name
        try {
            ResultSet resultSetCompanyName = this.dbOperations.executeOneIntSelect(companyId,
                    "SELECT company_name FROM company WHERE id = ?");
            Boolean hasNextName = resultSetCompanyName.next();
            if (hasNextName) {
                String companyName = resultSetCompanyName.getString(1);
                //if the company exists, check the industry assigned to the company and return the name of industry
                String industryName;
                ResultSet resultSet = this.dbOperations.executeOneIntSelect(companyId,
                        "SELECT b.id , i.industry_name \n" +
                                "FROM company_industry b\n" +
                                "left join industry i on i.id = b.industry_id \n" +
                                "WHERE stock_id = ?");
                industryName = resultSet.getString(2);

              /*  //Is this code necessary?
                ResultSet resultSet = this.dbOperations.executeOneIntSelect(companyId,
                        "SELECT b.id , i.industry_name \n" +
                                "FROM company_industry b\n" +
                                "left join industry i on i.id = b.industry_id \n" +
                                "WHERE stock_id = ?");
                Boolean hasNextIndustry = resultSet.next();
                //if the industry is assigned, return the industry name
                if (hasNextIndustry) {
                    industryName = resultSet.getString(2);
                }
                //if the industry is not assigned? is it a possible that this happens?
                else {
                    ResultSet resultSetUnknownIndustry = this.dbOperations.executeOneStringSelect(unknownIndustry,
                            "SELECT id, name FROM industry WHERE industry_name = ?");
                    int industryId = resultSetUnknownIndustry.getInt(1);
                    industryName = resultSetUnknownIndustry.getString(2);
                }*/

                System.out.println("Enter date[YYYY-MM-DD]:");
                LocalDate date = LocalDate.parse(scanner.nextLine());

                System.out.println("Enter price[use '.' for two decimal points]:");
                BigDecimal price = new BigDecimal(scanner.nextLine());

                Stock stock = new Stock(companyName, price, date, industryName);
                System.out.println(stock + "\nIs everything correct?");
                Boolean input = Boolean.valueOf(scanner.nextLine());

                if (input.equals(true)) {
                    this.dbOperations.importStockInDB(stock);
                    System.out.println("New price and date for stock " + companyName + " added!");
                } else {
                    System.out.println("Stock was not added");
                }
            } else {
                System.out.println("Invalid ID. If you are unsure, use 'search'.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DateTimeParseException w) {
            System.out.println("Wrong date format!");
        } catch (NumberFormatException e) {
            System.out.println("Wrong price format!");
        }
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
