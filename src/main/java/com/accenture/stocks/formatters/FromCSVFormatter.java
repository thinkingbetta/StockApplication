package com.accenture.stocks.formatters;


import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class contains various methods to format the data of the .csv file.
 */
public class FromCSVFormatter {


    /**
     * This method transforms a given string into a Big Decimal.
     *
     * @param metadata string taken from a line of .csv file
     * @return Big Decimal
     */
    public BigDecimal getFormattedPrice(String metadata) {
        return new BigDecimal(metadata.replace("€", " ").replace(",", ".").trim());
    }

    /**
     * This method transforms a given string into a LocalDate.
     *
     * @param metadata string taken from a line of .csv file
     * @return LocalDate
     */
    public LocalDate getFormattedLocalDate(String metadata) {
        String date = metadata.replace(".", "");
        String day = date.substring(0, 2);
        String month = date.substring(2, 4);
        String year = date.substring(4, 8);
        LocalDate localDate = LocalDate.parse(year + "-" + month + "-" + day);
        return localDate;
    }


}


