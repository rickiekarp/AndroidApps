package net.rickiekarp.homeassistant.ui.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.adapter.NoteHistoryListViewAdapter;
import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.domain.ShoppingNote;
import net.rickiekarp.homeassistant.domain.ShoppingNoteList;
import net.rickiekarp.homeassistant.interfaces.IOnGetHistoryNotesResult;
import net.rickiekarp.homeassistant.preferences.Constants;
import net.rickiekarp.homeassistant.tasks.notes.GetNotesHistoryTask;

import java.util.ArrayList;

/**
 * Created by sebastian on 01.11.17.
 */

public class NotesHistoryFragment extends Fragment implements IOnGetHistoryNotesResult {

    private final String TAG = "singleNote";
    private AppDatabase database;
    private ArrayList<ShoppingNote> itemList;
    private NoteHistoryListViewAdapter adapter;
    private SharedPreferences sp;
    private ProgressDialog progressDialog;

    public NotesHistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notes_history, container, false);
        getActivity().setTitle(R.string.action_notes_history);

        sp = getContext().getSharedPreferences(Constants.Preferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        getAllNotes();
        database = AppDatabase.getDatabase(getActivity());
        itemList = new ArrayList<>();

        adapter = new NoteHistoryListViewAdapter(getActivity(), itemList);

        ListView myView = view.findViewById(R.id.listview);
        myView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        return view;
    }

    @Override
    public void onGetAllNotesSuccess(ShoppingNoteList notesList) {
        itemList.addAll(notesList.getNoteList());
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }

    @Override
    public void onGetAllNotesError() {
        progressDialog.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please check your internet connection");
        alertDialog.show();
    }

    private void getAllNotes(){
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading history", true, false);
        GetNotesHistoryTask getNotesTask = new GetNotesHistoryTask(sp, this);
        getNotesTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
