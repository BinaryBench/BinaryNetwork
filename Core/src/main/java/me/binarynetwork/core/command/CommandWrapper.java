package me.binarynetwork.core.command;

/**
 * Created by Bench on 9/6/2016.
 */
public interface CommandWrapper {

    CommandWrapper addCommand(Command command, String label, String... aliases);

    CommandWrapper removeCommand(Command command);

}
