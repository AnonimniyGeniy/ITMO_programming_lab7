package collections.askers;

import managers.CommandParser;

public class UserAsker {
    public static String askUsername() {
        while (true) {
            System.out.println("Enter username: ");
            String username = CommandParser.getScanner().nextLine().trim();
            if (username.equals("")) {
                System.out.println("Username can't be empty");
            } else {
                return username;
            }
        }

    }

    public static String askPassword() {
        while (true) {
            System.out.println("Enter password: ");
            String password = CommandParser.getScanner().nextLine().trim();
            if (password.equals("")) {
                System.out.println("Password can't be empty");
            } else {
                return password;
            }
        }
    }
}
