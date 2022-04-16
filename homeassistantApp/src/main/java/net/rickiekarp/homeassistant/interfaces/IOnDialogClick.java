package net.rickiekarp.homeassistant.interfaces;

import net.rickiekarp.homeassistant.net.communication.vo.VONote;

/**
 * Created by sebastian on 05.12.17.
 */

public interface IOnDialogClick {

    void onPositiveClick(VONote note, String type);
    void onNegativeClick(int id);
}
