package com.accenture.stocks.formatters;


import java.math.BigDecimal;
import java.time.LocalDate;

public class FromCSVFormatting {


    public BigDecimal getFormattedPrice(String metadata) {
        return new BigDecimal(metadata.replace("€", " ").replace(",", ".").trim());
    }

    public LocalDate getFormattedLocalDate(String metadata) {
        String date = metadata.replace(".", "-");
        String day = date.substring(0, 2);
        String month = date.substring(2, 6);
        String year = date.substring(6, 8);
        LocalDate localDate = LocalDate.parse("20" + year + month + day);
        return localDate;
    }


}


