package net.rickiekarp.homeassistant.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sebastian on 08.12.17.
 */

public abstract class AbstractBooleanPreference extends AbstractPreference {

    public void set(Context context, boolean value) {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences.contains(getKey())) {
            final Boolean oldValue = get(context);
            if (oldValue != null && oldValue == value) {
                return;
            }
        }

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getKey(), value);
        editor.apply();
    }

    public Boolean get(Context context, SharedPreferences sharedPreferences) {
        return sharedPreferences.contains(getKey()) ? sharedPreferences.getBoolean(getKey(), false) : null;
    }

    public Boolean get(Context context) {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        return get(context, sharedPreferences);
    }
}
