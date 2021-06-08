package edu.gatech.seclass.crypto6300;
import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String email;
    private boolean isAdmin;

    public User(String name, String emailAddress,boolean isAdmin) {
        this.userName = name;
        this.email = emailAddress;
        this.isAdmin = isAdmin;
    }

    public void setUserName (String name) {
        this.userName = name;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setEmail (String emailAddress) {
        this.email = emailAddress;
    }

    public String getEmail() {
        return this.email;
    }

    public void setIsAdmin(boolean isAdmin){ this.isAdmin = isAdmin;}

    public boolean getIsAdmin() { return this.isAdmin;}


}
