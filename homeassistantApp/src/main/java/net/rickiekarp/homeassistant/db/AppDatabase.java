package net.rickiekarp.homeassistant.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import net.rickiekarp.homeassistant.db.daos.NotesDAO;
import net.rickiekarp.homeassistant.db.daos.UserDAO;
import net.rickiekarp.homeassistant.db.entities.Notes;
import net.rickiekarp.homeassistant.db.entities.User;
import net.rickiekarp.homeassistant.domain.ShoppingStoreList;
import net.rickiekarp.homeassistant.net.NetworkApi;
import net.rickiekarp.homeassistant.net.communication.vo.VOData;

/**
 * Created by sebastian on 17.11.17.
 */
@Database(entities = {Notes.class, User.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static NetworkApi networkApi;

    private VOData appData;

    private ShoppingStoreList storeList;

    public abstract NotesDAO notesDAO();

    public abstract UserDAO userDAO();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            networkApi = new NetworkApi();
        }
        return INSTANCE;
    }

    public NetworkApi getNetworkApi() {
        return networkApi;
    }

    public VOData getAppData() {
        return appData;
    }

    public void setAppData(VOData data) {
        appData = data;
    }

    public ShoppingStoreList getStoreList() {
        return storeList;
    }

    public void setStoreList(ShoppingStoreList storeList) {
        this.storeList = storeList;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


}
