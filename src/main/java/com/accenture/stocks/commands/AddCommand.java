package com.accenture.stocks.commands;

import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.formatters.ScannerFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * This class allows to add a new price for a specific time for a specific stock.
 */
public class AddCommand extends Command {
    private final Boolean VALUE = false;
    public final String NAME = "add";
    private Scanner scanner;
    private DBOperations dbOperations;

    public AddCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method reads the company_id, if the id corresponds to a company inserted in the database,
     * returns company name and the industry assigned to the company. Asks for new price and date, creates a stock
     * and after the confirmation of the user, imports the stock into the database.
     *
     * @return boolean
     */
    @Override
    public boolean execute() {
        System.out.println("Enter ID company:");
        try {
            int companyId = Integer.parseInt(scanner.nextLine());
            ResultSet resultSetCompanyName = this.dbOperations.executeOneIntSelect(companyId,
                    "SELECT company_name FROM company WHERE id = ?");
            Boolean hasNextName = resultSetCompanyName.next();
            if (hasNextName) {
                String companyName = resultSetCompanyName.getString(1);
                String industryName;
                ResultSet resultSet = this.dbOperations.executeOneIntSelect(companyId,
                        "SELECT b.id , i.industry_name \n" +
                                "FROM company_industry b\n" +
                                "left join industry i on i.id = b.industry_id \n" +
                                "WHERE stock_id = ?");
                resultSet.next();
                industryName = resultSet.getString(2);
                System.out.println("Enter date[YYYY-MM-DD]:");
                LocalDate date = LocalDate.parse(scanner.nextLine());
                System.out.println("Enter price[use '.' for two decimal points]:");
                BigDecimal price = new BigDecimal(scanner.nextLine());
                Stock stock = new Stock(companyName, price, date, industryName);
                System.out.println("==============ID:" + companyId + "==============\n" + stock +
                        "\nIs everything correct?[true to continue or press anything to cancel]");
                Boolean isCorrect = Boolean.valueOf(new ScannerFormatter(this.scanner).getFormattedString());
                if (isCorrect) {
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
            System.out.println("Wrong number format!");
        }
        return VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
