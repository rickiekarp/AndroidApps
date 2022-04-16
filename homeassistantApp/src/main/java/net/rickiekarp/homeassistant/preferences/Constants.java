package net.rickiekarp.homeassistant.preferences;

/**
 * Created by sebastian on 08.11.17.
 */
public abstract class Constants {
    public static final int HTTP_TIMEOUT = 20000;

    public static final String DEFAULT_HOST = "https://api.rickiekarp.net";

    public static final String LOGIN_SERVER = "loginServer";

    public abstract class Preferences {
        public static final String SHARED_PREFERENCES_NAME = "sharedPreferencesName";
        public static final String PREF_USERNAME = "userName";
    }

    @Deprecated
    public static abstract class URL {
        @Deprecated
        public static final String BASE_URL_LOGIN = "/LoginServer/";
        @Deprecated
        public static final String BASE_URL_APPSERVER = "/HomeServer/";
    }
}
