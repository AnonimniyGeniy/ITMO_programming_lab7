package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbManager {

    String url = "jdbc:postgresql://localhost:5432/";
    //надо сделать выбор url от sys env user, если s367132, то pg, иначе localhost
    Connection conn;
    Properties props;
    boolean debug;

    public DbManager(boolean debug) {
        this.props = new Properties();
        this.props.setProperty("user", "s367132");
        this.props.setProperty("password", "WJg3uEZw3Qys9hVm");
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
    //какой-то метод для считывания коллекции



}
