package net.rickiekarp.homeassistant.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.domain.ShoppingNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Paresh N. Mayani
 */
public class NoteHistoryListViewAdapter extends BaseAdapter
{
    public ArrayList<ShoppingNote> items;
    private Activity activity;

    public NoteHistoryListViewAdapter(Activity activity, ArrayList<ShoppingNote> noteList) {
        super();
        this.activity = activity;
        this.items = noteList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
    }

    public ShoppingNote getNoteAtIndex(int index) {
        return items.get(index);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_notehistory_list, null);
            holder = new ViewHolder();
            holder.txtFirst = convertView.findViewById(R.id.FirstText);
            holder.txtSecond = convertView.findViewById(R.id.SecondText);
            holder.txtThird = convertView.findViewById(R.id.ThirdText);
            holder.txtFourth = convertView.findViewById(R.id.FourthText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShoppingNote map = items.get(position);
        holder.txtFirst.setText(map.getTitle());
        holder.txtSecond.setText(String.valueOf(map.getPrice()));
        holder.txtThird.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(map.getDateBought().getSeconds())));
        holder.txtFourth.setText(String.valueOf(map.getStoreId()));

        return convertView;
    }

}