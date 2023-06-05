package commands;


import collections.HumanBeing;
import managers.CollectionManager;
import managers.CommandReceiver;

/**
 * command to update element by id
 */
@CommandInfo(name = "update", description = "update element by id", argsCount = 1,
        requiredObjectType = HumanBeing.class, argumentTypes = {Integer.class})
public class Update extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;
    public Update(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("update", "update element by id");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }

    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.update(args, obj, username);
    }
}
