package cc.openhome.model;

public class Account {
    private String name;
    private String email;
    private String password;
    private String salt;
    
    public Account(String name, String email, String password, String salt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }
}
