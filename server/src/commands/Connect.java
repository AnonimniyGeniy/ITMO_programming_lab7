package commands;

import managers.CommandManager;
import managers.CommandReceiver;

import java.util.ArrayList;
@CommandInfo(name = "connect", description = "Connect to server")
public class Connect extends AbstractCommand{
    private final CommandManager commandManager;
    private final CommandReceiver commandReceiver;
    public Connect(CommandManager commandManager, CommandReceiver commandReceiver){
        super("connect", "Connect to server");
        this.commandManager = commandManager;
        this.commandReceiver = commandReceiver;
    }


    public CommandResponse execute(String[] argument, Object objArgument, String username) {
        return commandReceiver.connect(argument, commandManager, username);
    }
}

