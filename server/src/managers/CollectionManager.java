package managers;

import collections.HumanBeing;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for managing collection
 */
public class CollectionManager {

    private final LocalDateTime creationTime;
    private final FileManager fileManager;
    /**
     * Comparator to sort TreeMap by value of HumanBeing impact speed
     */
    Comparator<HumanBeing> humanBeingComparator = new Comparator<HumanBeing>() {
        @Override
        public int compare(HumanBeing o1, HumanBeing o2) {
            return o1.compareTo(o2);
        }
    };
    private TreeMap<Integer, HumanBeing> humanBeingCollection;

    private HashMap<String, String> users = new HashMap<>(); // username: password

    private HashMap<Integer, String> idOwner = new HashMap<>(); // id: username


    /**
     * Constructor for CollectionManager
     *
     * @param fileManager - FileManager for working with file
     */
    public CollectionManager(FileManager fileManager) {
        this.fileManager = fileManager;
        humanBeingCollection = new TreeMap(humanBeingComparator);
        creationTime = LocalDateTime.now();

        users.put("user", "ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f");
        idOwner.put(1, "user");
        idOwner.put(2, "user");
        idOwner.put(3, "user");

    }

    /**
     * @return current HumanBeing collection
     */
    public TreeMap<Integer, HumanBeing> getHumanBeingCollection() {
        return humanBeingCollection;
    }


    /*
    check if user has access to element with id
     */
    public boolean checkAccess(String username, int id) {
        return idOwner.get(id).equals(username);
    }

    /**
     * Setter for controlled collection
     *
     * @param humanBeingCollection - collection to set
     * @param username             - username of user that owns collection
     */
    public void setHumanBeingCollection(TreeMap<Integer, HumanBeing> humanBeingCollection, String username) {
        //delete all elements from collection that are owned by user
        for (int i = 0; i < humanBeingCollection.size(); i++) {
            if (idOwner.get(i).equals(username)) {
                //добавить проверку на то что все в бд сохранилось
                this.humanBeingCollection.remove(i);
            }
        }

    }

    /**
     * getter for creation time of collection
     *
     * @return creation Time
     */
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * @param obj
     */
    public void insert(int id, HumanBeing obj, String username) {
        this.humanBeingCollection.put(id, obj);
        idOwner.put(id, username);
        //надо дописать добавление в бд и добавление строчки что нужный юзер добавил
    }

    /**
     * returns last id in collection
     *
     * @return last id
     */
    public int getLastId() {
        return humanBeingCollection.lastKey();
    }

    /**
     * saves collection to file
     */
    public void saveCollection() {
        fileManager.writeCollection(humanBeingCollection);
    }

    /**
     * loads collection from file
     */
    public void loadCollection() {
        this.humanBeingCollection = fileManager.readCollection();
    }


    /**
     * removes element from collection by id
     *
     * @param id - id of element to remove
     * @return true if element was removed, false if element was not found
     */
    public boolean removeById(int id, String username) {
        if (this.humanBeingCollection.containsKey(id) && idOwner.get(id).equals(username)) {
            //добавить проверку на то что все в бд сохранилось
            this.humanBeingCollection.remove(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * removes all elements from collection that are greater than given
     */
    public void removeGreater(HumanBeing humanBeing, String username) {
        humanBeingCollection.entrySet().removeIf(
                entry -> (entry.getValue().compareTo(humanBeing) > 0 &&
                        idOwner.get(entry.getKey()).equals(username))
        );
    }

    /**
     * returns array of all elements in collection
     */
    public HumanBeing[] getArray() {
        return humanBeingCollection.values().toArray(new HumanBeing[0]);
    }

    /*
    returns map of all users and their passwords
     */
    public Map<String, String> getUsers() {
        return users;
    }

}
