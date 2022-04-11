package com.accenture.stocks;


import com.accenture.stocks.cli.ScannerFormatting;
import com.accenture.stocks.commands.Command;
import com.accenture.stocks.commands.ExitCommand;

import java.util.ArrayList;
import java.util.Scanner;

public class StockApp {

    private void starter() {
        Scanner scanner = new Scanner(System.in);
        ScannerFormatting scannerFormatting = new ScannerFormatting(scanner);

        Command exit = new ExitCommand();

        ArrayList<Command> commands = new ArrayList<>();
        commands.add(exit);

        System.out.println("Welcome to StockApp!");

        boolean suspend = false;
        while (!suspend) {
            System.out.println("\nWhat do you want to do?");
            for (Command command : commands) {
                System.out.println(command.getName());
            }
            String input = scannerFormatting.getFormattedCommand();

            for (Command command : commands) {
                if (command.getName().equals(input)) {
                    suspend = command.execute();
                }
            }


        }


    }

    public static void main(String[] args) {
        new StockApp().starter();


    }
}
