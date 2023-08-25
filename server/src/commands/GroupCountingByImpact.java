package commands;


import collections.HumanBeing;
import managers.CollectionManager;
import managers.CommandReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * group counting by impact speed
 */
@CommandInfo(name = "group_counting_by_impact", description = "group counting by impact speed")
public class GroupCountingByImpact extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;
    public GroupCountingByImpact(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("group_counting_by_impact", "group counting by impact speed");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }


    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.groupCountingByImpact(args, obj);

    }

}
