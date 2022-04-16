package net.rickiekarp.homeassistant.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sebastian on 08.12.17.
 */

public abstract class AbstractStringPreference extends AbstractPreference {

    public void set(Context context, String value) {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences.contains(getKey())) {
            final String oldValue = get(context);
            if (oldValue != null && oldValue.equals(value)) {
                return;
            }
        }

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getKey(), value);
        editor.apply();
    }

    public String get(Context context, SharedPreferences sharedPreferences) {
        return sharedPreferences.contains(getKey()) ? sharedPreferences.getString(getKey(), null) : null;
    }

    public String get(Context context) {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        return get(context, sharedPreferences);
    }
}
