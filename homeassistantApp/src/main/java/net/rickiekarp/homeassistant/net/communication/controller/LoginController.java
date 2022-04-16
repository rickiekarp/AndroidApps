package net.rickiekarp.homeassistant.net.communication.controller;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.interfaces.IOnLoginResult;
import net.rickiekarp.homeassistant.net.communication.ApiInterfaces;
import net.rickiekarp.homeassistant.net.communication.vo.VOData;
import net.rickiekarp.homeassistant.preferences.Configuration;
import net.rickiekarp.homeassistant.preferences.Token;
import net.rickiekarp.homeassistant.utils.Util;

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
public class LoginController implements Callback<VOData> {

    private SharedPreferences sp;
    private IOnLoginResult uiCallback;
    private AppDatabase database;

    public LoginController(SharedPreferences sp, IOnLoginResult uiCallback, AppDatabase database) {
        this.sp = sp;
        this.uiCallback = uiCallback;
        this.database = database;
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.host + BASE_URL_LOGIN)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterfaces.LoginApi api = retrofit.create(ApiInterfaces.LoginApi.class);

        String voToken = sp.getString(Token.KEY, "");
        Call<VOData> call = api.doLogin(Util.generateToken(voToken));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<VOData> call, Response<VOData> response) {
        // -> save login to preferences
        if (response.code() == 200) {
            VOData voData = response.body();
            database.setAppData(voData);

            uiCallback.onLoginSuccess();
        } else {
            uiCallback.onLoginError();
        }
    }


    @Override
    public void onFailure(Call<VOData> call, Throwable t) {
        t.printStackTrace();
        uiCallback.onLoginError();
    }
}
