package net.rickiekarp.homeassistant.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sebastian on 08.12.17.
 */

public abstract class AbstractPreference {

    protected abstract String getKey();

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.Preferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static boolean clear(Context context, String key) {
        return getSharedPreferences(context).edit().remove(key).commit();
    }

    public static boolean clearAll(Context context) {
        return getSharedPreferences(context).edit().clear().commit();
    }

    public static boolean isEmpty(Context context, String key) {
        return !getSharedPreferences(context).contains(key);
    }
}
