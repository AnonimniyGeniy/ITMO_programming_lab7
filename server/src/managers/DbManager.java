package managers;

import java.sql.*;
import java.util.Properties;

public class DbManager {

    String url = "jdbc:postgresql://localhost:5432/";
    //надо сделать выбор url от sysenv user, если s367132, то pg, иначе localhost
    Connection conn;
    Properties props;
    public DbManager() {
        this.props = new Properties();
        this.props.setProperty("user", "s367132");
        this.props.setProperty("password", "");
        try {
            this.conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.out.println("Error while connecting to database: " + e.getMessage());
        }
    }




}
