package com.accenture.stocks.commands;

/**
 * Superior class of all commands.
 */
public abstract class Command implements Nameable {

    /**
     * @return boolean false to continue, true to exit from application
     */
    public abstract boolean execute();

}
