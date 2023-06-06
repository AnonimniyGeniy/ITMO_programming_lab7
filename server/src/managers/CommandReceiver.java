package managers;

import collections.Car;
import collections.HumanBeing;
import commands.AbstractCommand;
import commands.CommandDescription;
import commands.CommandDescriptionFactory;
import commands.CommandResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandReceiver {
    private final CollectionManager collectionManager;

    public CommandReceiver(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    public CommandResponse help(String[] args, Object obj) {
        StringBuilder stringBuilder = new StringBuilder();
        AbstractCommand[] commands = (AbstractCommand[]) obj;
        Arrays.stream(commands)
                .filter(command -> !command.getName().equals("save"))
                .forEach(command -> stringBuilder.append(command.getName()).append(" - ").append(command.describe()).append("\n"));

        stringBuilder.append("help - ").append("shows help for available commands");
        return new CommandResponse(stringBuilder.toString(), null);
    }

    public CommandResponse update(String[] args, Object obj, String username) {
        int key = Integer.parseInt(args[0]);
        Optional<HumanBeing> optionalHumanBeing = collectionManager.getHumanBeingCollection().values().stream()
                .filter(human -> human.getId() == key)
                .findFirst();

        if (optionalHumanBeing.isEmpty()) {
            return new CommandResponse("Element with this key doesn't exist.", null);
        }
        HumanBeing humanBeing = (HumanBeing) obj;
        humanBeing.setId(key);
        if (collectionManager.updateHumanBeing(humanBeing, key, username)){
            return new CommandResponse("Element updated successfully.", null);
        }else{
            return new CommandResponse("You don't have permission to update this element or it doesn't exist.", null);
        }
    }


    public CommandResponse replaceIfLower(String[] args, Object obj, String username) {
        //check if key exists
        int key = Integer.parseInt(args[0]);
        return collectionManager.getHumanBeingCollection().containsKey(key)
                ? Optional.ofNullable((HumanBeing) obj)
                .map(humanBeing -> {
                    if (collectionManager.getHumanBeingCollection().get(key).compareTo(humanBeing) > 0 && collectionManager.checkAccess(username, key)) {
                        collectionManager.updateHumanBeing(humanBeing, key, username);
                        return new CommandResponse("Element changed successfully.", null);
                    } else {
                        return new CommandResponse("Element is not lower or you don't have permission to change it.", null);
                    }
                })
                .orElse(new CommandResponse("Invalid object.", null))
                : new CommandResponse("Key doesn't exist.", null);

    }

    public CommandResponse removeGreater(String[] args, Object obj, String username) {
        collectionManager.removeGreater((HumanBeing) obj, username);
        return new CommandResponse("All greater elements was successfully removed", null);
    }

    public CommandResponse remove(String[] args, Object obj, String username) {
        int id = Integer.parseInt(args[0]);
        boolean isRemoved = collectionManager.removeById(id, username);

        String message = isRemoved ? "Element with id " + id + " was removed" : "Element with id " + id + " wasn't found or you don't have permission to remove it";

        return new CommandResponse(message, null);

    }

    public CommandResponse printDesc(String[] args, Object obj) {
//        ArrayList<HumanBeing> elements = new ArrayList<>(List.of(collectionManager.getArray()));
//        elements.sort(Comparator.reverseOrder());
//        return new CommandResponse("Elements in descending order", elements);
        List<HumanBeing> elements = Arrays.stream(collectionManager.getArray())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        return new CommandResponse("Elements in descending order", elements);
    }

    public CommandResponse insert(String[] args, Object obj, String username) {
        HumanBeing humanBeing = null;
        humanBeing = (HumanBeing) obj;
        collectionManager.insert(humanBeing, username);
        return new CommandResponse("Element added successfully.", null);

    }


    public CommandResponse info(String[] args, Object obj) {
        LocalDateTime initTime = collectionManager.getCreationTime();
        String message = "Collection info:\n" + "Collection type: " + collectionManager.getHumanBeingCollection().getClass().getName() + "\n" + "Collection size: " + collectionManager.getHumanBeingCollection().size() + "\n" + "Initialization time: " + initTime;
        return new CommandResponse(message, null);
    }

    public CommandResponse show(String[] args, Object obj) {
        //ArrayList<HumanBeing> coll = new ArrayList<>(List.of(collectionManager.getArray()));
        List<HumanBeing> elements = Arrays.stream(collectionManager.getArray()).toList();

        return new CommandResponse("Showed all elements of collection", elements);
    }

    public CommandResponse clear(String[] args, Object object, String username) {

        collectionManager.setHumanBeingCollection(new TreeMap<>(), username);

        return new CommandResponse("All your elements was deleted", null);
    }

    public CommandResponse countGreaterThanCar(String[] args, Object obj) {
        Car car = (Car) obj;
        int count = (int) Arrays.stream(collectionManager.getArray())
                .filter(humanBeing -> humanBeing.getCar().compareTo(car) > 0)
                .count();
        return new CommandResponse("There are " + count + " elements whose car field value is greater than the specified one", null);
    }

    public CommandResponse groupCountingByImpact(String[] args, Object obj) {
        HumanBeing[] elements = collectionManager.getArray();
        int[] impactSpeed = Arrays.stream(elements)
                .mapToInt(humanBeing -> humanBeing.getImpactSpeed().intValue())
                .toArray();

        Map<Integer, Integer> counter = Arrays.stream(impactSpeed)
                .boxed()
                .collect(Collectors.toMap(
                        Function.identity(),
                        value -> 1,
                        Integer::sum
                ));

        String result = counter.entrySet().stream()
                .map(entry -> "Impact speed: " + entry.getKey() + " - " + entry.getValue() + " elements")
                .collect(Collectors.joining(System.lineSeparator()));
        return new CommandResponse("Grouped counting by impact speed entries", result);
    }

    public CommandResponse history(String[] args, Object obj) {
        CommandManager commandManager = (CommandManager) obj;
        String commands = Arrays.stream(commandManager.getCommandHistory().toArray())
                .map(Object::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        return new CommandResponse("Last 13 commands: ", commands);
    }

    public CommandResponse connect(String[] args, Object obj, String username) {
        CommandManager commandManager = (CommandManager) obj;
        AbstractCommand[] commands = commandManager.getCommandsArray();
        List<CommandDescription> commandDescriptions = Arrays.stream(commands)
                .filter(command -> !command.getName().equals("save"))
                .map(command -> CommandDescriptionFactory.createCommandDescription(command.getClass()))
                .collect(Collectors.toList());
        return new CommandResponse("Got commands", commandDescriptions);
    }
}
