package server;

import managers.CollectionManager;
import managers.DbManager;


public class App {
    public static void main(String[] args) {
        String user = null;
        try {
            user = System.getenv("USER");
        } catch (Exception e) {
            System.out.println("Environment variable FILENAME is not set");
            System.exit(1);
        }

        DbManager dbManager = new DbManager(user.equals("s367132"));
        dbManager.connect();
        dbManager.createTablesIfNotExist();
        CollectionManager collectionManager = new CollectionManager(dbManager);


        try {
            collectionManager.loadCollection();
        } catch (Exception e) {
            System.out.println("Error while loading collection from file");
            System.exit(1);
        }

        //password: 12345678, enc: ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f
        Server server = new Server(collectionManager);

        server.run();

    }
}