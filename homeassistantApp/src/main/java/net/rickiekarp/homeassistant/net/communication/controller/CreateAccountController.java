package net.rickiekarp.homeassistant.net.communication.controller;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rickiekarp.homeassistant.interfaces.IOnCreateAccountResult;
import net.rickiekarp.homeassistant.interfaces.IRunController;
import net.rickiekarp.homeassistant.net.communication.ApiInterfaces;
import net.rickiekarp.homeassistant.net.communication.vo.VOCredentials;
import net.rickiekarp.homeassistant.net.communication.vo.VOToken;
import net.rickiekarp.homeassistant.preferences.Configuration;
import net.rickiekarp.homeassistant.preferences.Token;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.rickiekarp.homeassistant.preferences.Constants.URL.BASE_URL_LOGIN;

/**
 * Created by sebastian on 17.11.17.
 */
public class CreateAccountController implements Callback<VOToken>, IRunController {

    private SharedPreferences sp;
    private String username, password;
    private IOnCreateAccountResult taskCallback;

    public CreateAccountController(SharedPreferences sp, String username, String password, IOnCreateAccountResult taskCallback) {
        this.sp = sp;
        this.username = username;
        this.password = password;
        this.taskCallback = taskCallback;
    }

    @Override
    public void start() {
        VOCredentials credentials = new VOCredentials(username, password);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.host + BASE_URL_LOGIN)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterfaces.LoginApi api = retrofit.create(ApiInterfaces.LoginApi.class);

        Call<VOToken> call = api.doCreateAccount(credentials);
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<VOToken> call, @NonNull Response<VOToken> response) {
        if (response.code() == 200) {
            VOToken vo = response.body();
            if (vo != null) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Token.KEY, vo.getToken());
                editor.apply();

                taskCallback.onCreationSuccess();

            }
        } else {
            taskCallback.onCreationError();
        }
    }

    @Override
    public void onFailure(@NonNull Call<VOToken> call, @NonNull Throwable t) {
        t.printStackTrace();
        taskCallback.onCreationError();
    }
}
