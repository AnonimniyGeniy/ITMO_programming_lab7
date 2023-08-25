package commands;

import managers.CommandReceiver;

/**
 * shows help for available commands
 */
@CommandInfo(name = "help", description = "shows help for available commands")
public class Help extends AbstractCommand {
    private final AbstractCommand[] commands;
    private final CommandReceiver commandReceiver;
    public Help(AbstractCommand[] commands, CommandReceiver commandReceiver) {
        super("help", "shows help for available commands");
        this.commands = commands;
        this.commandReceiver = commandReceiver;
    }

    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.help(args, commands);
    }
}
