package managers;

import collections.HumanBeing;
import collections.User;

import java.security.NoSuchAlgorithmException;
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
     */
    public CollectionManager(DbManager dbManager) {
        this.dbManager = dbManager;
        humanBeingCollection = new TreeMap(humanBeingComparator);
        creationTime = LocalDateTime.now();
        //dbManager.getCollection();
        this.users = dbManager.getUsers();
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
        String owner = idOwner.get(id);
        return owner.equals(username);
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
            if (checkAccess(username, i)) {
                //добавить проверку на то что все в бд сохранилось
                if (dbManager.deleteHumanBeing(i)) {
                    this.humanBeingCollection.remove(i);
                }
            }
        }

    }

    public boolean updateHumanBeing(HumanBeing humanBeing, int id, String username) {
        if (!checkAccess(username, id)) {
            return false;
        }
        return dbManager.updateHumanBeing(humanBeing, users.get(username).getId(), id);
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
    public void insert(HumanBeing obj, String username) {
        int id = getLastId() + 1;
        obj.setId(id);
        if (dbManager.addHumanBeing(obj, users.get(username).getId())) {
            humanBeingCollection.put(id, obj);
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
     * gets all owners
     */
    public HashMap<Integer, Integer> getOwners() {
        return dbManager.getOwners();
    }

    /**
     * loads collection from file
     */
    public void loadCollection() {
        this.humanBeingCollection = dbManager.getCollection();
        HashMap<Integer, Integer> owners = dbManager.getOwners();
        for (Map.Entry<Integer, Integer> entry : owners.entrySet()) {
            for (Map.Entry<String, User> user : users.entrySet()) {
                if (user.getValue().getId() == entry.getValue()) {
                    idOwner.put(entry.getKey(), user.getKey());
                    break;
                }
            }
        }
    }


    /**
     * removes element from collection by id
     *
     * @param id - id of element to remove
     * @return true if element was removed, false if element was not found
     */
    public boolean removeById(int id, String username) {
        if (this.humanBeingCollection.containsKey(id) && checkAccess(username, id)) {
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

    /*
    method that registers new user
     */
    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        } else {
            //get last id and increment it
            int id = users.size() + 1;
            User user = null;
            try {
                user = new User(username, PasswordUtil.hashPassword(password), id);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            users.put(username, user);
            return dbManager.addUser(user);
        }
    }

    /*
    method that checks if user exists and password is correct
     */
    public boolean login(String username, String password) {
        if (users.containsKey(username)) {
            try {
                return PasswordUtil.hashPassword(password).equals(users.get(username).getPasswordSha256());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }
}
