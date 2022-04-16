package net.rickiekarp.homeassistant.preferences;

/**
 * Created by sebastian on 17.11.17.
 */

public class Token extends AbstractStringPreference {

    public static final String KEY = "token";

    private static Token instance;

    public static Token getInstance() {
        if (instance == null) {
            instance = new Token();
        }

        return instance;
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
