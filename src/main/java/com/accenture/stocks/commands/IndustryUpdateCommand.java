package com.accenture.stocks.commands;

import com.accenture.stocks.persistence.DBOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class IndustryUpdateCommand extends Command{
    private final Boolean VALUE = false;
    private final String NAME = "updateindustry";
    private Scanner scanner;
    private DBOperations dbOperations;
    private final String tableName = "company_industry";
    private final String columnName = "stock_id";

    public IndustryUpdateCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        System.out.println("Enter company id: ");
        try {
            int input = Integer.parseInt(scanner.nextLine());
            int companyId = this.dbOperations.fromCompanyIdToIndustry(input, this.scanner);
            System.out.println("Write industry name you want to associate with ID: ");
            String industryName = scanner.nextLine();
            int industryId = this.dbOperations.insertIndustryInIndustryTable(industryName);
            ResultSet companyIndustryIdResult = this.dbOperations.executeTwoIntSelect(companyId, industryId, "SELECT id FROM company_industry WHERE stock_id = ? AND industry_id = ?");
            boolean hasNext = companyIndustryIdResult.next();
            if (!hasNext) {
                this.dbOperations.executeTwoIntQuery(industryId, companyId,"UPDATE company_industry SET industry_id = ? WHERE stock_id = ?");
                System.out.println("Industry successfully updated!");

            }

        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, enter a number!");
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
