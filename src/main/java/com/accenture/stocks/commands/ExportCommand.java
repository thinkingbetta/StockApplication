package com.accenture.stocks.commands;

import com.accenture.stocks.entities.Stock;
import com.accenture.stocks.formatters.FromDBFormatter;
import com.accenture.stocks.persistence.DBOperations;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ExportCommand extends Command{
    private final String name = "export";
    private final Boolean value = false;

    private Scanner scanner;
    private DBOperations dbOperations;

    public ExportCommand(Scanner scanner, DBOperations dbOperations) {

        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    @Override
    public boolean execute() {
        FromDBFormatter fromDBFormatter = new FromDBFormatter();

        try {
            BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("data/new.csv"), StandardCharsets.UTF_8));
            csvWriter.write("stockname");
            csvWriter.write(";");
            csvWriter.write("price");
            csvWriter.write(";");
            csvWriter.write("price_date");
            csvWriter.write(";");
            csvWriter.write("industry");
            csvWriter.newLine();

            ResultSet resultSet = this.dbOperations.selectAllStocks();

            int count = 0;
            Boolean hasNext = resultSet.next();
            while (hasNext) {

                Stock stock = new Stock(resultSet.getString(1), resultSet.getBigDecimal(2),
                        fromDBFormatter.fromDateSQLtoLocalDate(resultSet, 3), resultSet.getString(4));

                toCSV(stock, csvWriter);

                hasNext = resultSet.next();
                count ++;


            }

            csvWriter.flush();
            csvWriter.close();
            System.out.println(count + " lines correctly imported in data/new.csv");

        } catch(SQLException | IOException e) {
            e.printStackTrace();
        }
            return value;
        }

        private void toCSV(Stock stock, BufferedWriter csvWriter) throws IOException {
            csvWriter.write(stock.getCompanyName());
            csvWriter.write(";");
            csvWriter.write("\u20ac " + stock.getPrice().toString().replace(".",","));
            csvWriter.write(";");
            csvWriter.write(stock.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            csvWriter.write(";");
            csvWriter.write(stock.getIndustryName());
            csvWriter.newLine();
        }

        @Override
    public String getName() {
        return name;
    }
}
