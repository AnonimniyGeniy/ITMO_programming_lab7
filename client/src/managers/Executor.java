package managers;

import collections.Car;
import collections.Coordinates;
import collections.HumanBeing;
import collections.WeaponType;
import collections.askers.*;
import commands.CommandDescription;
import commands.CommandRequest;
import commands.CommandResponse;
import exceptions.EmptyFieldException;
import exceptions.IncorrectScriptInputException;
import exceptions.InvalidObjectException;
import exceptions.RecursionInScriptRecursion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import collections.askers.UserAsker;

import static java.lang.Integer.parseInt;


public class Executor {
    private final List<String> recursionStack = new ArrayList<>();
    private final CommandManager commandManager;
    private Client client;
    private String username;
    private String password;

    /**
     * constructor for Executor
     *
     * @param commandManager - CommandManager
     *                       for managing commands
     */
    public Executor(CommandManager commandManager, Console console, Client client, String username, String password) {

        AskerManager.setAsker(Car.class, new CarAsker(console));
        AskerManager.setAsker(Coordinates.class, new CoordinatesAsker(console));
        AskerManager.setAsker(HumanBeing.class, new HumanBeingAsker(console));
        AskerManager.setAsker(WeaponType.class, new WeaponTypeAsker(console));
        this.commandManager = commandManager;
        this.client = client;
        this.username = username;
        this.password = password;
    }

    public void consoleMode() {
        Scanner userScanner = CommandParser.getScanner();
        try {
            Status status = Status.OK;
            String[] command;
            do {
                System.out.println("Enter command: ");
                try {
                    command = userScanner.nextLine().trim().split(" ");

                    if (command[0].equals("exit")) {
                        status = Status.EXIT;
                        break;
                    } else if (command[0].equals("execute_script")) {
                        status = scriptMode(command[1]);
                    }
                    else if (command[0].equals("login") || command[0].equals("register")) {
                        this.username = UserAsker.askUsername();
                        this.password = UserAsker.askPassword();
                        CommandRequest request = new CommandRequest(command[0], new String[]{username, password}, null, username, password);
                        manageResponse(request);
                        continue;
                    }
                    if (validateCommand(command)) {
                        CommandDescription description = commandManager.getCommand(command[0]);
                        //call needed asker
                        Object object = null;
                        if (description.getRequiredObjectType() != Void.class) {
                            Asker asker = AskerManager.getAsker(description.getRequiredObjectType());
                            object = asker.build();
                        }
                        String[] args = new String[command.length - 1];
                        System.arraycopy(command, 1, args, 0, command.length - 1);

                        CommandRequest request = new CommandRequest(command[0], args, object, username, password);
                        manageResponse(request);
                        //client.run(request);
                    } else {
                        System.out.println("Wrong command, use help to get list of commands");
                    }

                } catch (NoSuchElementException e) {
                    System.out.println("Ctrl-D pressed, finishing program...");
                    status = Status.EXIT;
                }

            } while (status != Status.EXIT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method for validating command by checking its commandDescription
     */
    private boolean validateCommand(String[] command) {
        try {
            CommandDescription description = commandManager.getCommand(command[0]);
            if (description == null) {
                System.out.println("Command not found");
                return false;
            }
            if (description.getArgsCount() != command.length - 1) {
                System.out.println("Wrong number of arguments");
                return false;
            }
            for (int i = 1; i < command.length; i++) {
                //check that ith argument can be cast to class of ith argument in commandDescription
                if (description.getArgumentTypes().get(i - 1) != int.class && description.getArgumentTypes().get(i - 1) != Integer.class) {
                    try {
                        description.getArgumentTypes().get(i - 1).cast(command[i]);
                    } catch (ClassCastException e) {
                        System.out.println("Wrong type of argument");
                        return false; // Casting failed
                    }
                } else {
                    try {
                        parseInt(command[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("Wrong type of argument");
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error while validating command");
            return false;
        }
    }

    public Status scriptMode(String arg) {
        String[] command = new String[2];
        Status status = Status.OK;
        recursionStack.add(arg);
        if (!new File(arg).exists()) {
            arg = "../" + arg;
        }
        Scanner defaultScanner = CommandParser.getScanner();
        try (Scanner scanner = new Scanner(new File(arg))) {
            if (!scanner.hasNextLine()) {
                System.out.println("File is empty");
                return Status.ERROR;
            }

            CommandParser.setScanner(scanner);
            CommandParser.setFileMode();
            //do while loop for executing commands from file
            do {

                command = scanner.nextLine().trim().split(" ");
                while (scanner.hasNextLine() && command[0].isEmpty()) {
                    command = scanner.nextLine().trim().split(" ");
                }
                System.out.println("Executing command: " + command[0]);
                if (command[0].equals("execute_script")) {
                    for (String s : recursionStack) {
                        if (s.equals(command[1])) throw new RecursionInScriptRecursion();
                    }
                }

                if (command[0].equals("exit")) {
                    status = Status.EXIT;
                    return status;
                }


                if (validateCommand(command)) {
                    CommandDescription description = commandManager.getCommand(command[0]);
                    //call needed asker
                    Object object = null;
                    if (description.getRequiredObjectType() != Void.class) {
                        Asker asker = AskerManager.getAsker(description.getRequiredObjectType());
                        object = asker.build();
                    }
                    String[] args = new String[command.length - 1];
                    System.arraycopy(command, 1, args, 0, command.length - 1);

                    CommandRequest request = new CommandRequest(command[0], args, object, username, password);
                    status = Status.OK;
                    manageResponse(request);
                }

            } while (status == Status.OK && scanner.hasNextLine());

            CommandParser.setScanner(defaultScanner);
            CommandParser.setConsoleMode();
            return status;

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (RecursionInScriptRecursion e) {
            System.out.println("Recursion in script");
        } catch (InvalidObjectException | EmptyFieldException | IncorrectScriptInputException e) {
            throw new RuntimeException(e);
        } finally {
            CommandParser.setScanner(defaultScanner);
            CommandParser.setConsoleMode();
        }

        return Status.ERROR;
    }

    private void manageResponse(CommandRequest request) {
        CommandResponse response = client.run(request);
        System.out.println(response.getMessage());
        if (response.getObject() != null) {
            Object object1 = response.getObject();
            if (object1 instanceof Iterable<?>)
                for (Object o : (Iterable<?>) object1) System.out.println(o.toString());
            else System.out.println(object1.toString());
        }
    }

    /**
     * enum for status of execution
     */


    public enum Status {
        OK,
        ERROR,
        EXIT,
    }

}
