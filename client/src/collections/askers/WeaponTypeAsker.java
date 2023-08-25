package collections.askers;

import collections.WeaponType;
import exceptions.EmptyFieldException;
import exceptions.IncorrectScriptInputException;
import managers.CommandParser;
import managers.Console;

/**
 * Class for asking WeaponType field
 */
public class WeaponTypeAsker extends Asker {
    private final Console console;

    public WeaponTypeAsker(Console console) {
        this.console = console;
    }

    /**
     * @return Asked WeaponType
     * @throws IncorrectScriptInputException
     * @throws EmptyFieldException
     */
    @Override
    public WeaponType build() throws IncorrectScriptInputException, EmptyFieldException {
        WeaponType weaponType = askWeaponType();
        return weaponType;
    }

    /**
     * @return WeaponType object
     * @throws IncorrectScriptInputException
     */
    public WeaponType askWeaponType() throws IncorrectScriptInputException {
        WeaponType weaponType;
        String weaponTypeString;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter weapon type (" + WeaponType.names() + "):");
                weaponTypeString = CommandParser.getScanner().nextLine().trim();
                if (weaponTypeString.equals("")) throw new EmptyFieldException();
                weaponType = WeaponType.valueOf(weaponTypeString.toUpperCase());
                break;

            } catch (IllegalArgumentException e) {
                console.println("Invalid weapon type");
                if (fileMode) throw new IncorrectScriptInputException();
            } catch (EmptyFieldException e) {
                console.println("Field can't be empty");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return weaponType;
    }
}
