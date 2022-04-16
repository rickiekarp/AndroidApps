package net.rickiekarp.homeassistant.adapter;

import android.content.Context;
import android.widget.TextView;

import net.rickiekarp.homeassistant.domain.ShoppingStore;

import java.util.List;

public class ShoppingStoreArrayAdapter extends GenericArrayAdapter<ShoppingStore> {

    public ShoppingStoreArrayAdapter(Context context, int resourceId, List<ShoppingStore> objects) {
        super(context, resourceId, objects);
    }

    @Override public void drawText(TextView textView, ShoppingStore object) {
        textView.setText(object.getName());
    }

}