package collections;


import java.io.Serializable;

/**
 * Enum for WeaponType
 */
public enum WeaponType implements Serializable {
    HAMMER,
    AXE,
    SHOTGUN,
    RIFLE,
    BAT;

    public static String names() {
        String names = "";
        for (WeaponType weaponType : WeaponType.values()) {
            names += weaponType.name() + ", ";
        }
        return names.substring(0, names.length() - 2);
    }
}