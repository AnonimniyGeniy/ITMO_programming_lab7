package commands;


import managers.CollectionManager;
import managers.CommandReceiver;

/**
 * command for removing element from collection by id
 */
@CommandInfo(name = "remove", description = "remove element from collection by id", argsCount = 1, argumentTypes = {int.class}, requiredObjectType = Void.class)
public class Remove extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;
    public Remove(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("remove", "remove element from collection by id");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }

    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.remove(args, obj, username);
    }
}
