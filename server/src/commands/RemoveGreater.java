package commands;

import collections.HumanBeing;
import managers.CollectionManager;
import managers.CommandReceiver;

/**
 * command for removing all elements from collection that are greater than the specified one
 */
@CommandInfo(name = "remove_greater", description = "remove all elements from collection that are greater than the specified one", requiredObjectType = HumanBeing.class)
public class RemoveGreater extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;
    public RemoveGreater(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("remove_greater", "remove all elements from collection that are greater than the specified one");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }

    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.removeGreater(args, obj, username);
    }
}
