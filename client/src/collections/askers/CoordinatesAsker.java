package collections.askers;

import collections.Coordinates;
import exceptions.IncorrectScriptInputException;
import exceptions.InvalidObjectException;
import managers.CommandParser;
import managers.Console;

/**
 * Class for asking coordinates
 */
public class CoordinatesAsker extends Asker {

    private final Console console;

    public CoordinatesAsker(Console console) {
        this.console = console;
    }

    /**
     * @return
     */
    @Override
    public Coordinates build() throws IncorrectScriptInputException, InvalidObjectException {
        Coordinates coordinates = new Coordinates(askX(), askY());
        if (!coordinates.validate()) throw new InvalidObjectException();

        return coordinates;
    }


    /**
     * @return double x
     * @throws IncorrectScriptInputException
     */
    public double askX() throws IncorrectScriptInputException {
        double x;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter x (should be double less than 180):");
                String xString = CommandParser.getScanner().nextLine().trim();

                x = Double.parseDouble(xString);
                if (x > 180) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                console.println("X must be a double less than 180");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return x;
    }

    /**
     * @return int y
     * @throws IncorrectScriptInputException
     */
    public int askY() throws IncorrectScriptInputException {
        int y;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter y (should be int):");
                String yString = CommandParser.getScanner().nextLine().trim();
                y = Integer.parseInt(yString);
                break;
            } catch (NumberFormatException e) {
                console.println("Y must be a integer less than" + Integer.MAX_VALUE + "and more than " + Integer.MIN_VALUE );
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return y;
    }

}
