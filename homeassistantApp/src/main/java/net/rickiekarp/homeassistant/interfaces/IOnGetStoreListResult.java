package net.rickiekarp.homeassistant.interfaces;

import net.rickiekarp.homeassistant.domain.ShoppingStoreList;

public interface IOnGetStoreListResult {
    void onGetAllNotesSuccess(ShoppingStoreList storeList);
    void onGetAllNotesError();
}
