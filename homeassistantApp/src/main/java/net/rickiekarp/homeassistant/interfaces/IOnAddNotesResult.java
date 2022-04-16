package net.rickiekarp.homeassistant.interfaces;

/**
 * Created by sebastian on 06.12.17.
 */

public interface IOnAddNotesResult {

    void onAddNotesSuccess(String title);
    void onAddNotesError();
}
