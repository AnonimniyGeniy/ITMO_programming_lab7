package commands;

/**
 * Default interface describing the behavior of command
 *
 * @author AnonimniyGeniy
 */

public interface Command {
    /**
     * Command descriptor
     *
     * @return Command description
     */
    String describe();

    /**
     * Command name getter
     *
     * @return Command name
     */
    String getName();

    /**
     * Execute command
     *
     * @param args
     * @return Execution result
     */
    //boolean execute(String[] args);

    CommandResponse execute(String[] args, Object object, String username);

}
