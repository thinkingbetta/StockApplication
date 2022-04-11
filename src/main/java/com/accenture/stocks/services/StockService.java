package com.accenture.stocks.services;


import com.accenture.stocks.entities.Stock;

import java.time.LocalDate;

public class StockService {

    private Stock stock;

    public Stock createStock(String[] metadata) {
        String companyName = metadata[0];
        Float price = getFormattedPrice(metadata);

        LocalDate date = getFormattedLocalDate(metadata);

        String industryName = metadata[3];
        this.stock = new Stock(companyName, price, date, industryName);
        return stock;
    }

    public Float getFormattedPrice(String[] metadata) {
        return Float.valueOf(metadata[1].replace("€", " ").replace(",", ".").trim());
    }

    public LocalDate getFormattedLocalDate(String[] metadata) {
        String date = metadata[2].replace(".", "-");
        String day = date.substring(0, 2);
        String month = date.substring(2, 6);
        String year = date.substring(6, 8);
        LocalDate localDate = LocalDate.parse("20" + year + month + day);
        return localDate;
    }



}

