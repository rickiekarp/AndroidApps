package net.rickiekarp.homeassistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.interfaces.IOnRemoveNoteResult;
import net.rickiekarp.homeassistant.net.communication.vo.VONote;
import net.rickiekarp.homeassistant.tasks.notes.MarkAsBoughtNoteTask;

import java.util.ArrayList;

/**
 * Created by sebastian on 04.12.17.
 */
public class AdapterCustomRecyclerView extends RecyclerView.Adapter<AdapterCustomRecyclerView.MyViewHolder> {

    private IOnRemoveNoteResult onRemoveNoteResult;
    private String token;
    private ArrayList<VONote> items;

    public AdapterCustomRecyclerView(ArrayList<VONote> items, String token, IOnRemoveNoteResult removeResult) {
        this.items = items;
        this.token = token;
        this.onRemoveNoteResult = removeResult;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_list, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("position:"  + items.get(position).getId());
                MarkAsBoughtNoteTask updateNoteTask = new MarkAsBoughtNoteTask(token, onRemoveNoteResult, position, items.get(position).getId());
                updateNoteTask.execute();
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private Button button;

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notes_title);
            button = itemView.findViewById(R.id.remove);
        }
    }

    public VONote getNoteAtIndex(int index) {
        return items.get(index);
    }

    public String getItemTitle(int position) {
        return items.get(position).getTitle();
    }

    public int getNoteId(int position) {
        return items.get(position).getId();
    }
}
