package commands;

import managers.CollectionManager;
import managers.CommandReceiver;

@CommandInfo(name = "login", description = "login user")
public class Login extends AbstractCommand {
    private final CommandReceiver commandReceiver;
    private final CollectionManager collectionManager;

    public Login(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("login", "login user");
        this.commandReceiver = commandReceiver;
        this.collectionManager = collectionManager;
    }

    @Override
    public String describe() {
        return "login user";
    }
    @Override
    public String getName() {
        return "login";
    }
    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.login(args, username);
    }

}
