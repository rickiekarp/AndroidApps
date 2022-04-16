package net.rickiekarp.homeassistant.interfaces;

/**
 * Created by sebastian on 06.12.17.
 */

public interface IOnUpdateNotesResult {

    void onUpdateNotesSuccess(String title);
    void onUpdateNotesError(int responseCode);
}
