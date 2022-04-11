package com.accenture.stocks.commands;

public class ExitCommand extends Command {
    private final String commandName = "exit";
    private final Boolean value = true;

    @Override
    public boolean execute() {
        System.out.println("See you next time!");
        return value;
    }

    @Override
    public String getName() {
        return commandName;
    }
}
