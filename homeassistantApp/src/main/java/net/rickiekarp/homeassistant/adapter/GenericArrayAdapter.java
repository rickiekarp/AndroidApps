package net.rickiekarp.homeassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.domain.ShoppingStore;

import java.util.List;

public abstract class GenericArrayAdapter<T> extends ArrayAdapter<T> {

    // Vars
    private LayoutInflater mInflater;

    GenericArrayAdapter(Context context, int resourceId, List<ShoppingStore> objects) {
        super(context, resourceId, (List<T>) objects);
        init(context);
    }

    // Headers
    public abstract void drawText(TextView textView, T object);

    private void init(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        drawText(vh.textView, getItem(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_list,parent, false);
        }
        ShoppingStore rowItem = (ShoppingStore) getItem(position);
        TextView txtTitle = convertView.findViewById(R.id.title);
        txtTitle.setText(rowItem.getName());
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
//        imageView.setImageResource(rowItem.getImageId());
        return convertView;
    }

    static class ViewHolder {

        TextView textView;

        private ViewHolder(View rootView) {
            textView = rootView.findViewById(android.R.id.text1);
        }
    }
}