package collections;

public class User {
    private final String username;
    private final String password_sha256;
    private final int id;

    public User(String username, String password_sha256, int id) {
        this.username = username;
        this.password_sha256 = password_sha256;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordSha256() {
        return password_sha256;
    }

    public int getId() {
        return id;
    }

}
