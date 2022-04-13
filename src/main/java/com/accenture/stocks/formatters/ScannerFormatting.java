package com.accenture.stocks.formatters;

import java.util.Locale;
import java.util.Scanner;

public class ScannerFormatting {
    private Scanner scanner;
    private String[] fobiddenChars = {"[","%","_"}; //the order is IMPORTANT, square bracket has priority


    public ScannerFormatting(Scanner scanner) {
        this.scanner=scanner;
    }

    public String getFormattedString() {
        String formattedString = this.scanner.nextLine().trim().toLowerCase();
        return formattedString;
    }
   //Escape characters that can be used by SQL Like query. You can search for all chars, but "["
    public String getFormattedSqlLikeString(){
        String formattedString = this.scanner.nextLine().trim();
        for(String forbiddenChar : fobiddenChars) {
            formattedString = formattedString.replace(forbiddenChar,"[" + forbiddenChar);
        }
        System.out.println(formattedString);
        return formattedString;
    }


}
