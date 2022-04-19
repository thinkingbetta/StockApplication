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

/**
 * This class allows to export all the data in the database in a new .csv file.
 */
public class ExportCommand extends Command {
    private final String NAME = "export";
    private final Boolean VALUE = false;

    private Scanner scanner;
    private DBOperations dbOperations;

    public ExportCommand(Scanner scanner, DBOperations dbOperations) {
        this.scanner = scanner;
        this.dbOperations = dbOperations;
    }

    /**
     * This method writes a standard line in new.csv, create stocks with the data from database and then write all the
     * stocks in new.csv.
     *
     * @return boolean
     */
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
                count++;
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println(count + " lines correctly imported in data/new.csv");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return VALUE;
    }

    /**
     * This method writes the formatted attributes of the stock through a Buffered Writer.
     *
     * @param stock     Instance of Stock
     * @param csvWriter Buffered Writer
     * @throws IOException
     */
    private void toCSV(Stock stock, BufferedWriter csvWriter) throws IOException {
        csvWriter.write(stock.getCompanyName());
        csvWriter.write(";");
        csvWriter.write("\u20ac " + stock.getPrice().toString().replace(".", ","));
        csvWriter.write(";");
        csvWriter.write(stock.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        csvWriter.write(";");
        csvWriter.write(stock.getIndustryName());
        csvWriter.newLine();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
