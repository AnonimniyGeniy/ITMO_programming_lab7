package commands;

import managers.CollectionManager;
import managers.CommandReceiver;

import java.util.TreeMap;

/**
 * command for clearing collection
 */
@CommandInfo(name = "clear", description = "clear collection", argsCount = 0, argumentTypes = {}, requiredObjectType = Void.class)
public class Clear extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;

    public Clear(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("clear", "clear collection");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }


    /**
     * Execute command clear
     *
     * @param args
     * @param object
     * @return Execution result
     */
    @Override
    public CommandResponse execute(String[] args, Object object, String username) {
        return commandReceiver.clear(args, object, username);
    }
}
