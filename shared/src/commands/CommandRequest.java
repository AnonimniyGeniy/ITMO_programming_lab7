package commands;

import java.io.Serializable;

/**
 * Class that holds command name, description, count of arguments, type of arguments, and object info.
 * Used to send command from client to server.
 */

public class CommandRequest implements Serializable {
    private final String commandName;
    private final String[] arguments;
    private final Object object;
    private final String username;
    private final String password;

    public CommandRequest(String commandName, String[] arguments, Object object, String username, String password) {
        this.commandName = commandName;
        this.arguments = arguments;
        this.object = object;
        this.username = username;
        this.password = password;
    }


    public String getCommandName() {
        return commandName;
    }

    public String[] getArguments() {
        return arguments;
    }

    public Object getElement() {
        return object;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
