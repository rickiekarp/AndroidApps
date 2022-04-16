package net.rickiekarp.homeassistant.net.communication.vo;

/**
 * Created by sebastian on 15.11.17.
 */

public class VOCredentials {

    String username, password;

    public VOCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
