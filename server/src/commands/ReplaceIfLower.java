package commands;

import collections.HumanBeing;
import managers.CollectionManager;
import managers.CommandReceiver;

/**
 * command for replacing value by key if new value is lower
 */
@CommandInfo(name = "replace_if_lower", description = "replace value by key if new value is lower", argsCount = 1,
        argumentTypes = {Integer.class} , requiredObjectType = HumanBeing.class)
public class ReplaceIfLower extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;
    public ReplaceIfLower(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("replace_if_lower", "replace value by key if new value is lower");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }


    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.replaceIfLower(args, obj, username);
    }
}
