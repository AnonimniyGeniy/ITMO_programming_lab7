package managers;

import collections.Car;
import collections.Coordinates;
import collections.HumanBeing;
import collections.WeaponType;
import collections.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

public class DbManager {

    String url = "jdbc:postgresql://localhost:5432/";
    //надо сделать выбор url от sys env user, если s367132, то pg, иначе localhost
    Connection conn;
    Properties props;
    boolean debug;

    public DbManager(boolean debug) {
        this.props = new Properties();

//        try {
//            props.load(new FileInputStream("db.cfg"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        this.props.setProperty("user", "s367132");
        this.props.setProperty("password", "");


        this.debug = debug;
    }

    public void connect() {
        if (debug) {
            this.url = "jdbc:postgresql://pg:5432/studs";
        } else {
            this.url = "jdbc:postgresql://localhost:5432/postgres";
        }
        try {
            this.conn = DriverManager.getConnection(url, props);
            if (!debug) {
                this.conn.setSchema("studs");
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to database: " + e.getMessage());
        }
    }

    /*
    method for adding human being to database
     */
    public boolean addHumanBeing(HumanBeing humanBeing, int userId) {
        try {
            String sql = "INSERT INTO human_being (name, coordinates_x, coordinates_y, creation_date, real_hero, " +
                    "has_toothpick, impact_speed, soundtrack_name, minutes_of_waiting, weapon_type, car_name, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, humanBeing.getName());
            statement.setDouble(2, humanBeing.getCoordinates().getX());
            statement.setInt(3, humanBeing.getCoordinates().getY());
            statement.setTimestamp(4, new java.sql.Timestamp(humanBeing.getCreationDate().getTime()));
            statement.setBoolean(5, humanBeing.isRealHero());
            statement.setBoolean(6, humanBeing.getHasToothpick());
            statement.setFloat(7, humanBeing.getImpactSpeed());
            statement.setString(8, humanBeing.getSoundtrackName());
            statement.setDouble(9, humanBeing.getMinutesOfWaiting());
            statement.setString(10, humanBeing.getWeaponType().name());
            statement.setString(11, humanBeing.getCar().getName());
            statement.setInt(12, userId);

            int rowsAffected = statement.executeUpdate();


            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    method for deleting human being from database
     */
    public boolean deleteHumanBeing(int id) {
        try {
            String sql = "DELETE FROM human_being WHERE id = ?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /*
    method for reading human being from database
     */
    public TreeMap<Integer, HumanBeing> getCollection() {
        TreeMap<Integer, HumanBeing> humanBeings = new TreeMap<>();

        try {
            String sql = "SELECT * FROM human_being";

            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                try {
                    HumanBeing humanBeing = new HumanBeing(
                            resultSet.getString("name"),
                            new Coordinates(resultSet.getDouble("coordinates_x"), resultSet.getInt("coordinates_y")),
                            resultSet.getBoolean("real_hero"),
                            resultSet.getBoolean("has_toothpick"),
                            resultSet.getFloat("impact_speed"),
                            resultSet.getString("soundtrack_name"),
                            resultSet.getDouble("minutes_of_waiting"),
                            WeaponType.valueOf(resultSet.getString("weapon_type").toUpperCase()),
                            new Car(resultSet.getString("car_name"))
                    );
                    humanBeing.setId(resultSet.getInt("id"));
                    humanBeing.setCreationDate(resultSet.getTimestamp("creation_date"));
                    humanBeings.put(humanBeing.getId(), humanBeing);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error while reading human being from database: " + e.getMessage());
                }
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return humanBeings;
    }

    public HashMap<Integer, Integer> getOwners() {
        HashMap<Integer, Integer> owners = new HashMap<>();

        try {
            String sql = "SELECT * FROM human_being";

            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                try {
                    owners.put(resultSet.getInt("id"), resultSet.getInt("user_id"));
                } catch (IllegalArgumentException e) {
                    System.out.println("Error while reading human being from database: " + e.getMessage());
                }
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
    }


    /*
    method for updating human being in database
     */
    public boolean updateHumanBeing(HumanBeing humanBeing, int userId, int id) {
        try {
            String sql = "UPDATE human_being SET name=?, coordinates_x=?, coordinates_y=?, creation_date=?, " +
                    "real_hero=?, has_toothpick=?, impact_speed=?, soundtrack_name=?, minutes_of_waiting=?, " +
                    "weapon_type=?, car_name=?, user_id=? WHERE id=?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, humanBeing.getName());
            statement.setDouble(2, humanBeing.getCoordinates().getX());
            statement.setInt(3, humanBeing.getCoordinates().getY());
            statement.setTimestamp(4, new java.sql.Timestamp(humanBeing.getCreationDate().getTime()));
            statement.setBoolean(5, humanBeing.isRealHero());
            statement.setBoolean(6, humanBeing.getHasToothpick());
            statement.setFloat(7, humanBeing.getImpactSpeed());
            statement.setString(8, humanBeing.getSoundtrackName());
            statement.setDouble(9, humanBeing.getMinutesOfWaiting());
            statement.setString(10, humanBeing.getWeaponType().name());
            statement.setString(11, humanBeing.getCar().getName());
            statement.setInt(12, userId);
            statement.setInt(13, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    get users from database
     */
    public HashMap<String, User> getUsers() {
        HashMap<String, User> users = new HashMap<>();

        try {
            String sql = "SELECT * FROM users";

            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.put(resultSet.getString("username"),
                        new User(resultSet.getString("username"),
                                resultSet.getString("password_sha256"),
                                resultSet.getInt("id")));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /*
    method for adding user to database
     */
    public boolean addUser(User user) {
        try {
            String sql = "INSERT INTO users (username, password_sha256) VALUES (?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordSha256());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    method that creates required tables, if they don't exist
     */
    public void createTablesIfNotExist() {
        try {
            Statement statement = conn.createStatement();

            // Создание таблицы users
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY," +
                    "username VARCHAR(255) NOT NULL," +
                    "password VARCHAR(255) NOT NULL" +
                    ")";
            statement.executeUpdate(createUsersTable);

            // Создание таблицы humanbeing
            String createHumanBeingTable = "CREATE TABLE IF NOT EXISTS humanbeing (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "coordinates_x DOUBLE PRECISION NOT NULL," +
                    "coordinates_y INT NOT NULL," +
                    "creation_date TIMESTAMPTZ NOT NULL," +
                    "real_hero BOOLEAN NOT NULL," +
                    "has_toothpick BOOLEAN," +
                    "impact_speed FLOAT NOT NULL," +
                    "soundtrack_name VARCHAR(255) NOT NULL," +
                    "minutes_of_waiting DOUBLE PRECISION," +
                    "weapon_type VARCHAR(50)," +
                    "car_name VARCHAR(255)," +
                    "user_id INT NOT NULL" +
                    ")";
            statement.executeUpdate(createHumanBeingTable);

            System.out.println("Таблицы созданы успешно.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
