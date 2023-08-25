package collections.askers;


import exceptions.EmptyFieldException;
import exceptions.IncorrectScriptInputException;
import exceptions.InvalidObjectException;

/**
 * Abstract class for asking objects
 *
 * @param <T> type of object to be asked
 */
public abstract class Asker<T> {
    public abstract T build() throws IncorrectScriptInputException, EmptyFieldException, InvalidObjectException;
}
