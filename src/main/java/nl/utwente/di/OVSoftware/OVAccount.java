package nl.utwente.di.OVSoftware;

public class OVAccount {
    private final String username;
    private final String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public OVAccount (String username, String password){
        this.username=username;
        this.password=password;
    }
}
