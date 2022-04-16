package net.rickiekarp.homeassistant.tasks.notes;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import net.rickiekarp.homeassistant.interfaces.IOnGetHistoryNotesResult;
import net.rickiekarp.homeassistant.net.communication.controller.GetNotesHistoryController;

/**
 * Created by sebastian on 05.12.17.
 */

public class GetNotesHistoryTask extends AsyncTask<Void, Void, String> {

    private SharedPreferences sp;
    private IOnGetHistoryNotesResult uiCallback;

    public GetNotesHistoryTask(SharedPreferences sp, IOnGetHistoryNotesResult uiCallback) {
        this.sp = sp;
        this.uiCallback = uiCallback;
    }
    @Override
    protected String doInBackground(Void... voids) {
        GetNotesHistoryController getNotesController = new GetNotesHistoryController(sp, uiCallback);
        getNotesController.start();
        return "Task executed";
    }
}
