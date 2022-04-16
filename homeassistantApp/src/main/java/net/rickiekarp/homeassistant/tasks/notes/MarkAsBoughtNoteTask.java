package net.rickiekarp.homeassistant.tasks.notes;

import android.os.AsyncTask;

import net.rickiekarp.homeassistant.interfaces.IOnRemoveNoteResult;
import net.rickiekarp.homeassistant.net.communication.controller.BoughtNotesController;

/**
 * Created by sebastian on 06.12.17.
 */

public class MarkAsBoughtNoteTask extends AsyncTask<Void, Void, String> {

    private String sp;
    private int id;
    private int noteId;
    private IOnRemoveNoteResult uiCallback;

    public MarkAsBoughtNoteTask(String sp, IOnRemoveNoteResult uiCallback, int id, int noteid) {
        this.sp = sp;
        this.id = id;
        this.noteId = noteid;
        this.uiCallback = uiCallback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        BoughtNotesController updateNotesController = new BoughtNotesController(sp, uiCallback, id, noteId);
        updateNotesController.start();
        return "Task executed";
    }
}
