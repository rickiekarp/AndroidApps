package net.rickiekarp.homeassistant.tasks.notes;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.interfaces.IOnRemoveNoteResult;
import net.rickiekarp.homeassistant.net.communication.controller.RemoveNotesController;

/**
 * Created by sebastian on 06.12.17.
 */

public class RemoveNoteTask extends AsyncTask<Void, Void, String> {

    private SharedPreferences sp;
    private AppDatabase database;
    private IOnRemoveNoteResult uiCallback;
    private int id;

    public RemoveNoteTask(SharedPreferences sp, IOnRemoveNoteResult uiCallback, AppDatabase database, int id) {
        this.sp = sp;
        this.uiCallback = uiCallback;
        this.database = database;
        this.id = id;
    }

    @Override
    protected String doInBackground(Void... voids) {

        RemoveNotesController removeNotesController = new RemoveNotesController(sp, uiCallback, database, id);
        removeNotesController.start();
        return "Task executed";
    }
}
