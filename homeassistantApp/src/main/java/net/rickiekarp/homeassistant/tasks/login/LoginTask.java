package net.rickiekarp.homeassistant.tasks.login;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.interfaces.IOnLoginResult;
import net.rickiekarp.homeassistant.net.communication.controller.LoginController;

/**
 * Created by sebastian on 04.12.17.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    private SharedPreferences sp;
    private IOnLoginResult uiCallback;
    private AppDatabase database;

    public LoginTask(SharedPreferences sp, IOnLoginResult uiCallback, AppDatabase database) {
        this.uiCallback = uiCallback;
        this.sp = sp;
        this.database = database;
    }

    @Override
    protected String doInBackground(Void... voids) {
        LoginController loginController = new LoginController(sp, uiCallback, database);
        loginController.start();

        return "Task finished";
    }

}

