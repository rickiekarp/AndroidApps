package net.rickiekarp.homeassistant.preferences;

/**
 * Created by sebastian on 08.12.17.
 */

public class Password extends AbstractStringPreference {

    public static final String KEY = "username";

    private static Password instance;

    public static Password getInstance() {
        if (instance == null) {
            instance = new Password();
        }

        return instance;
    }

    @Override
    protected String getKey() {
        return null;
    }
}
