package net.rickiekarp.homeassistant.preferences;

/**
 * Created by sebastian on 17.11.17.
 */

public class IsLoggedIn extends AbstractBooleanPreference {

    public static final String KEY = "isLoggedIn";

    private static IsLoggedIn instance;

    public static IsLoggedIn getInstance() {
        if (instance == null) {
            instance = new IsLoggedIn();
        }

        return instance;
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
