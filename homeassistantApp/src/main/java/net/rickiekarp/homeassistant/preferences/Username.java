package net.rickiekarp.homeassistant.preferences;

/**
 * Created by sebastian on 08.12.17.
 */

public class Username extends AbstractStringPreference {

    public static final String KEY = "username";

    private static Username instance;

    public static Username getInstance() {
        if (instance == null) {
            instance = new Username();
        }

        return instance;
    }

    @Override
    protected String getKey() {
        return KEY;
    }
}
