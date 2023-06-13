package commands;

import managers.CollectionManager;
import managers.CommandReceiver;

@CommandInfo(name = "register", description = "register new user, usage: register <username> <password>")//, argsCount = 2, argumentTypes = {String.class, String.class})
public class Register extends AbstractCommand{
    private final CommandReceiver commandReceiver;
    private final CollectionManager collectionManager;

    public Register(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("register", "register new user, syntax: register <username> <password>");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }
    @Override
    public String describe() {
        return "register new user";
    }
    @Override
    public String getName() {
        return "register";
    }
    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.register(args, username);
    }

}
