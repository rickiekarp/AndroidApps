package net.rickiekarp.homeassistant.tasks.notes;

import android.os.AsyncTask;

import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.interfaces.IOnUpdateNotesResult;
import net.rickiekarp.homeassistant.net.communication.controller.UpdateNotesController;
import net.rickiekarp.homeassistant.net.communication.vo.VONote;

/**
 * Created by sebastian on 06.12.17.
 */

public class UpdateNoteTask extends AsyncTask<Void, Void, String> {
    private String token;
    private VONote note;
    private AppDatabase database;
    private IOnUpdateNotesResult uiCallback;

    public UpdateNoteTask(String token, IOnUpdateNotesResult uiCallback, VONote note, AppDatabase database) {
        this.token = token;
        this.note = note;
        this.database = database;
        this.uiCallback = uiCallback;
    }
    @Override
    protected String doInBackground(Void... voids) {
        UpdateNotesController updateNotesController = new UpdateNotesController(token, uiCallback, note, database);
        updateNotesController.start();
        return "Task executed";
    }
}
