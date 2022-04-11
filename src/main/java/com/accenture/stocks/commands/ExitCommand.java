package com.accenture.stocks.commands;

public class ExitCommand extends Command {
    private final String commandName = "exit";

    @Override
    public boolean execute() {
        System.out.println("See you next time!");
        return true;
    }

    @Override
    public String getName() {
        return commandName;
    }
}
