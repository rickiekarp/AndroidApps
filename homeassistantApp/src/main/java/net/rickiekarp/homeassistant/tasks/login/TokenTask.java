package net.rickiekarp.homeassistant.tasks.login;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import net.rickiekarp.homeassistant.interfaces.IOnGetTokenResult;
import net.rickiekarp.homeassistant.net.communication.controller.TokenController;

/**
 * Created by sebastian on 05.12.17.
 */

public class TokenTask extends AsyncTask<Void, Void, String> {

    private SharedPreferences sp;
    private IOnGetTokenResult uiCallback;
    private String username, password;

    public TokenTask(SharedPreferences sp, IOnGetTokenResult uiCallback, String username, String password) {
        this.sp = sp;
        this.uiCallback = uiCallback;
        this.username = username;
        this.password = password;
    }
    @Override
    protected String doInBackground(Void... voids) {
        TokenController tokenController = new TokenController(sp, uiCallback, username, password);
        tokenController.start();

        return "Task finished";
    }
}
