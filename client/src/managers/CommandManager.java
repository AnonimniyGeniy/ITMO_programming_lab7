package managers;

import commands.CommandDescription;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * class that holds map with command descriptions
 */
public class CommandManager {
    HashMap<String, CommandDescription> commands;
    /**
     * constructor for CommandManager, requires array of CommandDescriptions
     * @param commandDescriptions
     */
    public CommandManager(CommandDescription[] commandDescriptions) {
        commands = new HashMap<>();
        for (CommandDescription commandDescription : commandDescriptions) {
            commands.put(commandDescription.getName(), commandDescription);
        }
    }
    /**
     * getter for commands
     *
     * @return map with commands
     */
    public Map<String, CommandDescription> getCommands() {
        return commands;
    }

    /**
     * method to get array of CommandDescriptions
     * @return
     */
    public ArrayList<CommandDescription> getCommandsArray() {
        return new ArrayList<>(commands.values());
    }

    /**
     * method to get command by name
     * @param name
     * @return
     */
    public CommandDescription getCommand(String name) {
        return commands.get(name);
    }
}
