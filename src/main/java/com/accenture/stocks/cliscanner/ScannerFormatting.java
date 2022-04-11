package com.accenture.stocks.cliscanner;

import java.util.Scanner;

public class ScannerFormatting {
    private Scanner scanner;

    public ScannerFormatting(Scanner scanner) {
        this.scanner=scanner;
    }

    public String getFormattedCommand() {
        String command = scanner.nextLine().trim().toLowerCase();
        return command;
    }


}
