package net.rickiekarp.homeassistant.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.rickiekarp.homeassistant.CustomTextWatcher;
import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.db.AppDatabase;

/**
 * Created by sebastian on 05.12.17.
 */

public class SingleNoteFragment extends Fragment {

    private SharedPreferences sp;
    private EditText editTitle, editBody;
    private String title, body;
    private String action;

    private AppDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_single_note, container, false);
        action = getTag();
        editTitle = (EditText) view.findViewById(R.id.single_note_title);
        editTitle.addTextChangedListener(new CustomTextWatcher(title));
//        editBody.addTextChangedListener(new CustomTextWatcher(body));
        return view;
    }

  /*  @Override
    public void onDestroyView() {
        if (action.equals("add")) {
            addNote();
        } else if (action.equals("update")) {
            updateNote();
        }
        super.onDestroyView();
    }*/

  /*  private void addNote() {
        Fragment fragment = getFragmentManager().getFragment(getArguments(), "notes");
        if (fragment != null) {
            if (!body.isEmpty() && !title.isEmpty()) {
                AddNoteTask addNoteTask = new AddNoteTask(sp, (IOnAddNotesResult) fragment, title, body, database);
                addNoteTask.execute();
            }
        }
    }*/

    private void updateNote(){

    }
}
