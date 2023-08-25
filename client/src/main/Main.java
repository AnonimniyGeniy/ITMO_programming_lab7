package main;


import collections.askers.PortAsker;
import collections.askers.UserAsker;
import commands.CommandDescription;
import commands.CommandRequest;
import commands.CommandResponse;
import managers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Console console = new managers.UserConsole();
        CommandParser.setScanner(new Scanner(System.in));

        int port = PortAsker.askPort();
        Client client = new Client("localhost", port);

        //String username = UserAsker.askUsername();
        //String password = UserAsker.askPassword();
        String username = null;
        String password = null;

        CommandRequest request = new CommandRequest("connect", new String[]{}, null, username, password);
        CommandResponse commandResponse = client.run(request);

        ArrayList<CommandDescription> commandDescriptions = (ArrayList<CommandDescription>) commandResponse.getObject();
        CommandDescription[] commandsArray = new CommandDescription[commandDescriptions.size() - 1];
        for(int i = 0; i < commandDescriptions.size(); i++) {
            if (commandDescriptions.get(i).getName().equals("connect")){
                commandDescriptions.remove(i);
                break;
            }
        }
        for (int i = 0; i < commandDescriptions.size(); i++) {
            commandsArray[i] = commandDescriptions.get(i);
        }
        CommandManager commandManager = new CommandManager(commandsArray);
        Executor executor = new Executor(commandManager, console, client, username, password);

        executor.consoleMode();


    }
}
