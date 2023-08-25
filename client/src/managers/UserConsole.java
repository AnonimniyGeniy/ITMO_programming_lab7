package managers;


/**
 * class to print messages to the console
 */
public class UserConsole implements Console {


    /**
     * @param obj
     */
    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * @param obj
     */
    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * @param obj
     */
    @Override
    public void printErr(Object obj) {
        System.err.print("Error:" + obj);
    }
}
