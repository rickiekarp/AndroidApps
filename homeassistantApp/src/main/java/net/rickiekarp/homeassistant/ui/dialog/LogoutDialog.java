package net.rickiekarp.homeassistant.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import net.rickiekarp.homeassistant.interfaces.IOnDialogClick;

/**
 * Created by sebastian on 05.12.17.
 */

public class LogoutDialog extends DialogFragment {

    private IOnDialogClick listener;

    public static LogoutDialog newInstance(IOnDialogClick listener) {

        LogoutDialog fragment = new LogoutDialog();
        fragment.listener = listener;

        return fragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Möchtest du dich ausloggen?");
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onPositiveClick(null, "");
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
