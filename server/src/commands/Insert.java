package commands;

import collections.HumanBeing;
import managers.CollectionManager;
import managers.CommandReceiver;

/**
 * Command that inserts element in the collection
 */
@CommandInfo(name = "insert", description = "Adds element to the collection by id. Syntax: insert id {element}", argsCount =0, requiredObjectType = HumanBeing.class, argumentTypes = {})
public class Insert extends AbstractCommand {
    private final CommandReceiver commandReceiver;
    private final CollectionManager collectionManager;

    public Insert(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("Insert", "Add element to the collection by id. Syntax: insert id {element}");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }

    @Override
    public String describe() {
        return "Inserts element in the collection by id. Syntax: insert id {element}.";
    }

    @Override
    public String getName() {
        return "insert";
    }

    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.insert(args, obj, username);

    }

}
