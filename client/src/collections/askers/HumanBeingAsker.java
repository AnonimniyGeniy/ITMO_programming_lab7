package collections.askers;

import collections.Car;
import collections.Coordinates;
import collections.HumanBeing;
import collections.WeaponType;
import exceptions.EmptyFieldException;
import exceptions.IncorrectScriptInputException;
import exceptions.InvalidObjectException;
import managers.CommandParser;
import managers.Console;

/**
 * Class for asking HumanBeing fields
 */
public class HumanBeingAsker extends Asker {
    private final Console console;

    public HumanBeingAsker(Console console) {
        this.console = console;
    }

    public HumanBeing build() throws IncorrectScriptInputException, EmptyFieldException, InvalidObjectException {
        String name = askName(); //Поле не может быть null, Строка не может быть пустой
        Coordinates coordinates = askCoordinates(); //Поле не может быть null
        boolean realHero = askRealHero();
        Boolean hasToothpick = askHasToothpick(); //Поле может быть null
        Float impactSpeed = askImpactSpeed(); //Поле не может быть null
        String soundtrackName = askSoundtrackName(); //Поле не может быть null
        Double minutesOfWaiting = askMinuteOfWaiting(); //Поле может быть null
        WeaponType weaponType = askWeaponType(); //Поле может быть null
        Car car = askCar(); //Поле может быть null

        HumanBeing humanBeing = new HumanBeing(name, coordinates, realHero, hasToothpick, impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car);
        if (!humanBeing.validate()) throw new InvalidObjectException();

        return humanBeing;
    }


    /**
     * @return Coordinates object
     * @throws EmptyFieldException
     * @throws IncorrectScriptInputException
     */
    private Coordinates askCoordinates() throws InvalidObjectException, IncorrectScriptInputException, EmptyFieldException {
        return new CoordinatesAsker(console).build();
    }

    /**
     * @return WeaponType object
     * @throws EmptyFieldException
     * @throws IncorrectScriptInputException
     */
    private WeaponType askWeaponType() throws InvalidObjectException, IncorrectScriptInputException, EmptyFieldException {
        return new WeaponTypeAsker(console).build();
    }

    /**
     * @return Car object
     * @throws EmptyFieldException
     * @throws IncorrectScriptInputException
     */
    private Car askCar() throws InvalidObjectException, IncorrectScriptInputException, EmptyFieldException {
        return new CarAsker(console).build();
    }

    /**
     * @return String name
     * @throws IncorrectScriptInputException
     */
    private String askName() throws IncorrectScriptInputException {
        String name;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter name:");
                name = CommandParser.getScanner().nextLine().trim();
                if (name.equals("")) throw new EmptyFieldException();
                break;
            } catch (EmptyFieldException e) {
                console.println("Name cannot be empty");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return name;
    }

    /**
     * @return boolean realHero
     * @throws IncorrectScriptInputException
     */
    private boolean askRealHero() throws IncorrectScriptInputException {
        boolean realHero;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter realHero (should type True or False, no case sensitive):");
                String realHeroString = CommandParser.getScanner().nextLine().trim();
                if (realHeroString.equals("")) throw new EmptyFieldException();
                if (realHeroString.equalsIgnoreCase("TRUE")) {
                    realHero = true;
                    break;
                } else if (realHeroString.equalsIgnoreCase("FALSE")) {
                    realHero = false;
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                console.println("RealHero must be 'true' or 'false'");
                if (fileMode) throw new IncorrectScriptInputException();
            } catch (EmptyFieldException e) {
                console.println("RealHero cannot be empty");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return realHero;
    }

    /**
     * @return boolean hasToothpick
     * @throws IncorrectScriptInputException
     */
    private Boolean askHasToothpick() throws IncorrectScriptInputException {
        boolean hasToothpick;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter hasToothpick (should be True or False, or null):");
                String hasToothpickString = CommandParser.getScanner().nextLine().trim();
                if (hasToothpickString.equals("")) return null;
                if (hasToothpickString.equalsIgnoreCase("TRUE")) {
                    hasToothpick = true;
                    break;
                } else if (hasToothpickString.equalsIgnoreCase("FALSE")) {
                    hasToothpick = false;
                    break;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                console.println("hasToothpick must be 'true' or 'false' or empty");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return hasToothpick;
    }

    /**
     * @return double impactSpeed
     * @throws IncorrectScriptInputException
     */
    private Float askImpactSpeed() throws IncorrectScriptInputException {
        Float impactSpeed;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter impactSpeed (should be float):");
                String impactSpeedString = CommandParser.getScanner().nextLine().trim();
                if (impactSpeedString.equals("")) throw new EmptyFieldException();
                impactSpeed = Float.parseFloat(impactSpeedString);
                break;
            } catch (NumberFormatException e) {
                console.println("impactSpeed must be double");
                if (fileMode) throw new IncorrectScriptInputException();
            } catch (EmptyFieldException e) {
                console.println("impactSpeed cannot be empty");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return impactSpeed;
    }

    /**
     * @return String soundtrackName
     * @throws IncorrectScriptInputException
     */
    private String askSoundtrackName() throws IncorrectScriptInputException {
        String soundtrackName;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter soundtrackName (should be String):");
                soundtrackName = CommandParser.getScanner().nextLine().trim();
                if (soundtrackName.equals("")) throw new EmptyFieldException();
                break;
            } catch (EmptyFieldException e) {
                console.println("soundtrackName cannot be empty");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return soundtrackName;
    }

    /**
     * @return double minutesOfWaiting
     * @throws IncorrectScriptInputException
     */
    private Double askMinuteOfWaiting() throws IncorrectScriptInputException {
        Double minutesOfWaiting;
        var fileMode = CommandParser.fileMode();
        while (true) {
            try {
                console.println("Enter minutesOfWaiting (should be double, or null):");
                String minutesOfWaitingString = CommandParser.getScanner().nextLine().trim();
                if (minutesOfWaitingString.equals("")) return null;
                minutesOfWaiting = Double.parseDouble(minutesOfWaitingString);
                break;
            } catch (NumberFormatException e) {
                console.println("minutesOfWaiting must be double or empty");
                if (fileMode) throw new IncorrectScriptInputException();
            }
        }
        return minutesOfWaiting;
    }
}
