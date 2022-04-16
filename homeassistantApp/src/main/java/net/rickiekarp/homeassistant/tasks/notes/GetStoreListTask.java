package net.rickiekarp.homeassistant.tasks.notes;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import net.rickiekarp.homeassistant.interfaces.IOnGetStoreListResult;
import net.rickiekarp.homeassistant.net.communication.controller.GetStoreListController;

/**
 * Created by sebastian on 05.12.17.
 */

public class GetStoreListTask extends AsyncTask<Void, Void, String> {

    private SharedPreferences sp;
    private IOnGetStoreListResult uiCallback;

    public GetStoreListTask(SharedPreferences sp, IOnGetStoreListResult uiCallback) {
        this.sp = sp;
        this.uiCallback = uiCallback;
    }
    @Override
    protected String doInBackground(Void... voids) {
        GetStoreListController getNotesController = new GetStoreListController(sp, uiCallback);
        getNotesController.start();
        return "Task executed";
    }
}
