package net.rickiekarp.homeassistant.interfaces;

/**
 * Created by sebastian on 06.12.17.
 */

public interface IOnRemoveNoteResult {

    void onRemoveNoteSuccess(int id);
    void onRemoveNoteError();
}
