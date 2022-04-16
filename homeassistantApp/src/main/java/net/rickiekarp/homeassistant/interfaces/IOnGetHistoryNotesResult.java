package net.rickiekarp.homeassistant.interfaces;

import net.rickiekarp.homeassistant.domain.ShoppingNoteList;

/**
 * Created by sebastian on 05.12.17.
 */

public interface IOnGetHistoryNotesResult {
    void onGetAllNotesSuccess(ShoppingNoteList notesList);
    void onGetAllNotesError();
}
