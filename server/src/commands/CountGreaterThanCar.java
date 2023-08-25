package commands;


import collections.Car;
import managers.CollectionManager;
import managers.CommandReceiver;

/**
 * show all elements whose car field value is greater than the specified one
 */
@CommandInfo(name = "count_greater_than_car", description = "show all elements whose car field value is greater than the specified one",
        requiredObjectType = Car.class)
public class CountGreaterThanCar extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final CommandReceiver commandReceiver;

    public CountGreaterThanCar(CollectionManager collectionManager, CommandReceiver commandReceiver) {
        super("count_greater_than_car", "show all elements whose car field value is greater than the specified one");
        this.collectionManager = collectionManager;
        this.commandReceiver = commandReceiver;
    }

    /**
     * CountGreaterThanCar command realization
     *
     * @param args arguments for command
     */
    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.countGreaterThanCar(args, obj);
    }


}
