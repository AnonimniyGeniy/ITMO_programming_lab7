package managers;


import collections.askers.Asker;

import java.util.HashMap;
import java.util.Map;

/**
 * class that manages Askers calls
 */
public class AskerManager {
    private static Map<Object, Asker> askers = new HashMap<>();

    /**
     * method to get asker by object
     *
     * @param object
     * @return
     */
    public static Asker getAsker(Object object) {
        return askers.get(object);
    }

    /**
     * method to set asker by object
     */
    public static void setAsker(Object object, Asker asker) {
        askers.put(object, asker);
    }

    public static void setAskers(Map<Object, Asker> askers) {
        AskerManager.askers = askers;
    }
}
