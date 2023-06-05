package managers;

import collections.HumanBeing;
import models.User;

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
    private final DbManager dbManager;
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
    private HashMap<String, User> users = new HashMap<>(); // username: User
    private HashMap<Integer, String> idOwner = new HashMap<>(); // id: username

    /**
     * Constructor for CollectionManager
     *
     * @param fileManager - FileManager for working with file
     */
    public CollectionManager(FileManager fileManager, DbManager dbManager) {
        this.fileManager = fileManager;
        this.dbManager = dbManager;
        humanBeingCollection = new TreeMap(humanBeingComparator);
        creationTime = LocalDateTime.now();
        //dbManager.getCollection();
        this.users = dbManager.getUsers();
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
                if (dbManager.deleteHumanBeing(i)) {
                    this.humanBeingCollection.remove(i);
                }
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
        if (dbManager.addHumanBeing(obj, users.get(username).getId())) {
            this.humanBeingCollection.put(id, obj);
            idOwner.put(id, username);
        }
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
     * loads collection from file
     */
    public void loadCollection() {
        this.humanBeingCollection = dbManager.getCollection();
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
            if (dbManager.deleteHumanBeing(id)) {
                this.humanBeingCollection.remove(id);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * removes all elements from collection that are greater than given
     */
    public void removeGreater(HumanBeing humanBeing, String username) {
        for (Map.Entry<Integer, HumanBeing> entry : humanBeingCollection.entrySet()) {
            if (entry.getValue().compareTo(humanBeing) > 0 &&
                    idOwner.get(entry.getKey()).equals(username)) {
                if (dbManager.deleteHumanBeing(entry.getKey())) {
                    humanBeingCollection.remove(entry.getKey());
                }
            }
        }
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
    public Map<String, User> getUsers() {
        return users;
    }

}
