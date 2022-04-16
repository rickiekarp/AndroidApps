package net.rickiekarp.homeassistant.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.domain.WorldList;
import net.rickiekarp.homeassistant.interfaces.IOnCreateAccountResult;
import net.rickiekarp.homeassistant.interfaces.IOnGetTokenResult;
import net.rickiekarp.homeassistant.interfaces.IOnLoginResult;
import net.rickiekarp.homeassistant.net.WorldsCall;
import net.rickiekarp.homeassistant.preferences.Configuration;
import net.rickiekarp.homeassistant.preferences.Constants;
import net.rickiekarp.homeassistant.preferences.IsLoggedIn;
import net.rickiekarp.homeassistant.preferences.Token;
import net.rickiekarp.homeassistant.tasks.login.LoginTask;
import net.rickiekarp.homeassistant.tasks.login.RegistrationTask;
import net.rickiekarp.homeassistant.tasks.login.TokenTask;
import net.rickiekarp.homeassistant.utils.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements IOnCreateAccountResult, IOnLoginResult, IOnGetTokenResult, AdapterView.OnItemSelectedListener {

    private SharedPreferences sp;
    private ProgressDialog progressDialog;
    private AppDatabase database;
    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        database = AppDatabase.getDatabase(this);
        sp = this.getSharedPreferences(Constants.Preferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        final String deviceId = Settings.Secure.getString(this.getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        EditText editUsername = findViewById(R.id.edit_name);
        editUsername.setText(deviceId);

        EditText editPassword = findViewById(R.id.edit_password);
        editPassword.setText(md5(deviceId));

        Button buttonLogin = findViewById(R.id.button_login);
        Button buttonRegister = findViewById(R.id.button_register);


        buttonRegister.setOnClickListener(v -> {
            if (editUsername.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()) {
                showInvalidCredentialsToast();
            } else {
                progressDialog = ProgressDialog.show(this, "", "Registrierung wird ausgeführt", true, false);
                sp.edit().putString(Constants.Preferences.PREF_USERNAME, editUsername.getText().toString()).apply();
                RegistrationTask registrationTask = new RegistrationTask(sp, editUsername.getText().toString(), editPassword.getText().toString(), this);
                registrationTask.execute();
            }
        });

        buttonLogin.setOnClickListener(v -> {
            if (editUsername.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()) {
                showInvalidCredentialsToast();
            } else {
                progressDialog = ProgressDialog.show(this, "", "Login wird ausgeführt", true, false);
                TokenTask getTokenTask = new TokenTask(sp, this, editUsername.getText().toString(), editPassword.getText().toString());
                getTokenTask.execute();

                sp.edit().putString(Constants.Preferences.PREF_USERNAME, editUsername.getText().toString()).apply();
            }
        });

        List<String> hosts_array = new ArrayList<>();

        WorldsCall call = new WorldsCall();
        try {
            WorldList worlds = call.execute().get();
            for (int i = 0; i < worlds.getWorldCount(); i++) {
                hosts_array.add(worlds.getWorld(i).getUrl());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        spinner = findViewById(R.id.serverselection);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, hosts_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        ImageView accountsettings = findViewById(R.id.account_settings);
        accountsettings.setOnClickListener(v -> Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show());
    }

    private void showInvalidCredentialsToast() {
        Toast.makeText(this, "Please enter a valid username and password", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onCreationSuccess() {
        LoginTask loginTask = new LoginTask(sp, this, database);
        loginTask.execute();
    }

    @Override
    public void onCreationError() {
        progressDialog.dismiss();
        AlertDialog errorDialog = new AlertDialog.Builder(this).create();
        errorDialog.setTitle("Error");
        errorDialog.setMessage("Account registration failed");
        errorDialog.show();
    }

    @Override
    public void onLoginSuccess() {
        setLoggedIn(true);
        Util.startMainActivity(this);
    }

    @Override
    public void onLoginError() {
        progressDialog.dismiss();
        AlertDialog errorDialog = new AlertDialog.Builder(this).create();
        errorDialog.setTitle("Error");
        errorDialog.setMessage("Please check your internet connection");
        errorDialog.show();

        setLoggedIn(false);
    }

    private void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IsLoggedIn.KEY, isLoggedIn);
        editor.apply();
    }

    private void removeToken() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Token.KEY);
        editor.apply();
    }


    @Override
    public void onGetTokenSuccess() {
        LoginTask loginTask = new LoginTask(sp, this, database);
        loginTask.execute();
    }

    @Override
    public void onGetTokenError() {
        progressDialog.dismiss();
        AlertDialog errorDialog = new AlertDialog.Builder(this).create();
        errorDialog.setTitle("Error");
        errorDialog.setMessage("Please check your internet connection");
        errorDialog.show();
        removeToken();
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Configuration.host = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
