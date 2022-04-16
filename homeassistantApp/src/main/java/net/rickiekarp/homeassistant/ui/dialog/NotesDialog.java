package net.rickiekarp.homeassistant.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.adapter.ShoppingStoreArrayAdapter;
import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.domain.ShoppingStore;
import net.rickiekarp.homeassistant.interfaces.IOnDialogClick;
import net.rickiekarp.homeassistant.net.communication.vo.VONote;

/**
 * Created by sebastian on 06.12.17.
 */

public class NotesDialog extends DialogFragment {

    private AppDatabase database;
    private IOnDialogClick listener;
    private VONote note;

    public static NotesDialog newInstance(IOnDialogClick listener, VONote noteItem, AppDatabase db) {
        NotesDialog fragment = new NotesDialog();
        fragment.database = db;
        fragment.listener = listener;
        fragment.note = noteItem;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_single_note, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);

        EditText editTitle = v.findViewById(R.id.single_note_title);
        editTitle.setHint("New title");

        EditText priceField = v.findViewById(R.id.note_detail_price);
        priceField.setHint("Item price");

        ShoppingStoreArrayAdapter shoppingStoreArrayAdapter = new ShoppingStoreArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, database.getStoreList().getStoreList());

        Spinner storeSpinner = v.findViewById(R.id.note_detail_store_spinner);
        storeSpinner.setAdapter(shoppingStoreArrayAdapter);

        if (note != null) {
            storeSpinner.setSelection(findId(note.getStore_id()));
        }

        switch (getTag()) {
            case "viewnote":
                builder.setTitle("Item details");
                break;

            case "addnote":
                builder.setTitle("Create item");
                builder.setPositiveButton("Hinzufügen", (dialogInterface, i) -> {
                    final String noteTitle = editTitle.getText().toString();
                    if (noteTitle.isEmpty()) {
                        Toast.makeText(getActivity(), "Title can not be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    VONote note = new VONote(noteTitle);
                    final String priceFieldContent = priceField.getText().toString();
                    if (!priceFieldContent.isEmpty()) {
                        final double notePrice = Double.valueOf(priceField.getText().toString());
                        note.setPrice(notePrice);
                    }
                    ShoppingStore store = (ShoppingStore) storeSpinner.getSelectedItem();
                    note.setStore_id((byte) store.getId());
                    listener.onPositiveClick(note, "add");
                    dialogInterface.dismiss();
                });
                break;
            case "updatenotes":
                builder.setTitle("Update item");
                editTitle.setText(note.getTitle());

                priceField.setText(String.valueOf(note.getPrice()));

                builder.setPositiveButton("Update", (dialogInterface, i) -> {
                    note.setTitle(editTitle.getText().toString());
                    note.setPrice(Double.valueOf(priceField.getText().toString()));
                    if (storeSpinner.getSelectedItem() != null) {
                        ShoppingStore store = (ShoppingStore) storeSpinner.getSelectedItem();
                        note.setStore_id((byte) store.getId());
                    }
                    listener.onPositiveClick(note,"update");
                    dialogInterface.dismiss();

                });
                builder.setNegativeButton("Löschen", (dialogInterface, i) -> {
                    listener.onNegativeClick(note.getId());
                    dismiss();
                });
                break;
            default:
                throw new RuntimeException("invalid parameter");
        }

        return builder.create();
    }

    private int findId(int storeId) {
        for (int i = 0; i < database.getStoreList().getStoreCount(); i++) {
            if (database.getStoreList().getStore(i).getId() == storeId) {
                return i;
            }
        }
        return 0;
    }
}
