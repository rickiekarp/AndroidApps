package com.rkarp.sha1pass.android.fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rkarp.sha1pass.android.encrypt.Base64Coder;
import com.rkarp.sha1pass.android.encrypt.EncryptFunc;
import com.rkarp.sha1pass.android.R;

import java.security.SignatureException;

public class MainFragment extends Fragment {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;

    AppCompatEditText sentence_tf;
    AppCompatEditText peek_tf;
    AppCompatButton hexBtn;
    AppCompatButton hexHalfBtn;
    AppCompatButton b64Btn;
    AppCompatButton b64HalfBtn;
    SwitchCompat viewMode;
    SwitchCompat secureMode;
    SwitchCompat hmacMode;
    SwitchCompat complexMode;

    boolean isSecure = false;
    boolean half = false;
    boolean hmac = false;
    boolean complex = false;

    //string which is added in complex mode
    public static String comp_string = ".H0k";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View onInflateView = inflater.inflate(R.layout.fragment_main, container, false);

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        sentence_tf = (AppCompatEditText) onInflateView.findViewById(R.id.editText1);
        sentence_tf.setTransformationMethod(new PasswordTransformationMethod());
        peek_tf = (AppCompatEditText) onInflateView.findViewById(R.id.editText2);
        hexBtn = (AppCompatButton) onInflateView.findViewById(R.id.hexBtn);
        hexHalfBtn = (AppCompatButton) onInflateView.findViewById(R.id.hexHalfBtn);
        b64Btn = (AppCompatButton) onInflateView.findViewById(R.id.b64Btn);
        b64HalfBtn = (AppCompatButton) onInflateView.findViewById(R.id.b64HalfBtn);
        viewMode = (SwitchCompat) onInflateView.findViewById(R.id.viewMode);
        secureMode = (SwitchCompat) onInflateView.findViewById(R.id.secureMode);
        hmacMode = (SwitchCompat) onInflateView.findViewById(R.id.hmacMode);
        complexMode = (SwitchCompat) onInflateView.findViewById(R.id.complexMode);

        hexBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                half = false;
                try {
                    calcHex();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        });

        hexHalfBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                half = true;
                try {
                    calcHex();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        });

        b64Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                half = false;
                try {
                    calcBase64();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        });

        b64HalfBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                half = true;
                try {
                    calcBase64();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        });

        viewMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (viewMode.isChecked()) {
                    sentence_tf.setTransformationMethod(new SingleLineTransformationMethod());
                } else {
                    sentence_tf.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        secureMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (secureMode.isChecked()) {
                    viewMode.setEnabled(false); viewMode.setChecked(false);
                    isSecure = secureMode.isChecked();
                    sentence_tf.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    viewMode.setEnabled(true);
                    isSecure = secureMode.isChecked();
                    sentence_tf.setText(""); peek_tf.setText("Peek");
                    send2Clipboard("", "Clipboard cleared");
                    sentence_tf.setTransformationMethod(new SingleLineTransformationMethod());
                }
            }
        });

        hmacMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hmac = hmacMode.isChecked();
            }
        });

        complexMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                complex = complexMode.isChecked();
            }
        });

        // Inflate the layout for this fragment
        return onInflateView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
//        Bundle args = getArguments();
//        if (args != null) {
//            // Set article based on argument passed in
//            updateArticleView(args.getInt(ARG_POSITION));
//        } else if (mCurrentPosition != -1) {
//            // Set article based on saved instance state defined during onCreateView
//            updateArticleView(mCurrentPosition);
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    private void peekText(String s) {
        if (!isSecure) {
            peek_tf.setText(s.substring(0, 6));
        } else {
            peek_tf.setText("Peek");
        }
    }

    protected void calcHex() throws SignatureException {
        String data = sentence_tf.getText().toString();
        byte[] output = EncryptFunc.getSha1(data);
        String hex = EncryptFunc.bytesToHex(output);

        if (half) { hex = hex.substring(0, hex.length() / 2); }
        send2Clipboard(hex, "Copied to Clipboard");
        peekText(hex);

        if (hmac)
        {
            //calculate HMAC string
            String s = EncryptFunc.calcHMAC(data, hex);

            //get the half of the encoded string
            if (half) { s = s.substring(0, hex.length()/2); }

            //append complex string
            if (complex) { s = s + comp_string; }

            //copy encoded string to clipboard and set the peek TextField text
            send2Clipboard(s, "Copied to Clipboard");
            peekText(s);
        }

        else if (complex)
        {
            //get the half of the encoded string
            if (half) { hex = hex.substring(0, hex.length()/2); }

            //copy encoded string to clipboard and set the peek TextField text
            send2Clipboard(hex + comp_string, "Copied to Clipboard");
            peekText(hex + comp_string);
        }
        else
        {
            //get the half of the encoded string
            if (half) { hex = hex.substring(0, hex.length()/2); }

            //copy encoded string to clipboard and set the peek TextField text
            send2Clipboard(hex, "Copied to Clipboard");
            peekText(hex);
        }
    }

    protected void calcBase64() throws SignatureException {
        String data = sentence_tf.getText().toString();
        String b64 = String.valueOf(Base64Coder.encode(EncryptFunc.getSha1(data)));
        if (half) { b64 = b64.substring(0, b64.length() / 2); }

        if (hmac)
        {
            //calculate HMAC string
            String s = EncryptFunc.calcHMAC(data,b64);

            //calculate Base64 string
            s = String.valueOf(Base64Coder.encode(EncryptFunc.getSha1(s)));

            //get the half of the encoded string
            if (half) { s = s.substring(0, s.length() / 2); }

            //append complex string
            if (complex) { s = s + comp_string; }

            //copy encoded string to clipboard and set the peek TextField text
            send2Clipboard(s, "Copied to Clipboard");
            peekText(s);
        }
        else if (complex)
        {
            //get the half of the encoded string
            if (half) { b64 = b64.substring(0, b64.length() / 2); }

            //copy encoded string to clipboard and set the peek TextField text
            send2Clipboard(b64 + comp_string, "Copied to Clipboard");
            peekText(b64 + comp_string);
        }
        else
        {
            //get the half of the encoded string
            if (half) { b64 = b64.substring(0, b64.length() / 2); }

            //copy encoded string to clipboard and set the peek TextField text
            send2Clipboard(b64, "Copied to Clipboard");
            peekText(b64);
        }
    }

    protected void send2Clipboard(String s, String msg) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(s);
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }
}