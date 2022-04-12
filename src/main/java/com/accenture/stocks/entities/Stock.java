package com.accenture.stocks.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class  Stock {
    private String companyName;
    private BigDecimal price;
    private LocalDate date;
    private String industryName;

    public Stock(String companyName, BigDecimal price, LocalDate date, String industryName) {
        this.companyName = companyName;
        this.price = price;
        this.date = date;
        this.industryName = industryName;
    }



    public String getCompanyName(){
        return companyName;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public LocalDate getDate(){
        return date;
    }

    public String getIndustryName(){
        return industryName;
    }

    public String toString(){
        return "Stock name: " + getCompanyName() +"\nPrice: " + getPrice() +" €\nDate: "+ getDate() +"\nIndustry: "+ getIndustryName() + "\n<><><><><><><><><><><><><><><><>";
    }
}
