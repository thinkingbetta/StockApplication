package com.accenture.stocks.formatters;

import java.util.Scanner;

/**
 * This class contains various methods to format the input given to a scanner by a user.
 */
public class ScannerFormatter {
    private Scanner scanner;
    /**
     * The order is IMPORTANT, square bracket has priority.
     */
    private String[] fobiddenChars = {"[", "%", "_"};


    public ScannerFormatter(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getFormattedString() {
        String formattedString = this.scanner.nextLine().trim().toLowerCase();
        return formattedString;
    }

    /**
     * This method escapes characters that can be used by SQL Like query. It is possible to search for all chars.
     *
     * @return string
     */
    public String getFormattedSqlLikeString() {
        String formattedString = this.scanner.nextLine().trim();
        for (String forbiddenChar : fobiddenChars) {
            formattedString = formattedString.replace(forbiddenChar, "[" + forbiddenChar);
        }
        return formattedString;
    }


}
