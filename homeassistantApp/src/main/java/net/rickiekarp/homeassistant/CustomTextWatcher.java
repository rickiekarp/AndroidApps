package net.rickiekarp.homeassistant;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by sebastian on 05.12.17.
 */

public class CustomTextWatcher implements TextWatcher {

    private String text;

    public CustomTextWatcher(String text){
        this.text = text;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        text = editable.toString();
    }
}
