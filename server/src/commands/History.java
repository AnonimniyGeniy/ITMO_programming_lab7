package commands;


import managers.CommandManager;
import managers.CommandReceiver;

/**
 * Command that shows last 13 commands (without their arguments)
 */
@CommandInfo(name = "history", description = "Shows last 13 commands (without their arguments)")
public class History extends AbstractCommand {
    private final CommandManager commandManager;
    private final CommandReceiver commandReceiver;
    /**
     * Constructor for History
     */
    public History(CommandManager commandManager, CommandReceiver commandReceiver) {
        super("history", "Shows last 13 commands (without their arguments)");
        this.commandManager = commandManager;
        this.commandReceiver = commandReceiver;
    }


    /**
     * Description of command
     */
    @Override
    public String describe() {
        return "Show last 13 commands (without their arguments)";
    }

    /**
     * Name of command
     */
    @Override
    public String getName() {
        return "history";
    }

    /**
     * Method for executing this command
     */
    @Override
    public CommandResponse execute(String[] args, Object obj, String username) {
        return commandReceiver.history(args, obj);
    }
}
