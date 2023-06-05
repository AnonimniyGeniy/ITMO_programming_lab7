package managers;

import java.sql.*;
import java.util.Properties;

public class DbManager {

    String url = "jdbc:postgresql://localhost:5432/";
    //надо сделать выбор url от sysenv user, если s367132, то pg, иначе localhost
    Connection conn;
    Properties props;
    public DbManager(boolean debug) {
        this.props = new Properties();
        this.props.setProperty("user", "s367132");
        this.props.setProperty("password", "WJg3uEZw3Qys9hVm");
        if (debug) {
            this.url = "jdbc:postgresql://pg:5432/studs";
        }
        else {
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




}
