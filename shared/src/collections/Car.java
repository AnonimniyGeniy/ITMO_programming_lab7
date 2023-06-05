package collections;

import other.Validatable;

import java.io.Serializable;


/**
 * Class for Car
 */
public class Car implements Validatable, Serializable {
    private final String name; //Поле не может быть null

    public Car(String name) {
        this.name = name;
    }

    /**
     * @return validation result
     */
    @Override
    public boolean validate() {
        return name != null;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Car " + name;
    }

    /**
     * compare two car objects
     */

    public int compareTo(Car o) {
        return name.length() - o.name.length();
    }
}