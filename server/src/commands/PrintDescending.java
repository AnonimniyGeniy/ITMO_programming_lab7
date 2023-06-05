package commands;


import managers.CollectionManager;
import managers.CommandReceiver;

/**
 * prints all elements in descending order
 */
@CommandInfo(name = "print_descending", description = "print all elements in descending order")
public class PrintDescending extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;
    public PrintDescending(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("print_descending", "print all elements in descending order");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }

    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.printDesc(args, obj);
    }

}
