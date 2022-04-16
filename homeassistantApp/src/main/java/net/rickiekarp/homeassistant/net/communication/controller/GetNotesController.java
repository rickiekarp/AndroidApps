package net.rickiekarp.homeassistant.net.communication.controller;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rickiekarp.homeassistant.interfaces.IOnGetAllNotesResult;
import net.rickiekarp.homeassistant.interfaces.IRunController;
import net.rickiekarp.homeassistant.net.communication.ApiInterfaces;
import net.rickiekarp.homeassistant.net.communication.vo.VONote;
import net.rickiekarp.homeassistant.preferences.Configuration;
import net.rickiekarp.homeassistant.preferences.Token;
import net.rickiekarp.homeassistant.utils.Util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.rickiekarp.homeassistant.preferences.Constants.URL.BASE_URL_APPSERVER;

/**
 * Created by sebastian on 22.11.17.
 */
public class GetNotesController implements Callback<List<VONote>>, IRunController {

    private SharedPreferences sp;
    private IOnGetAllNotesResult uiCallback;

    public GetNotesController(SharedPreferences sp, IOnGetAllNotesResult uiCallback) {
        this.uiCallback = uiCallback;
        this.sp = sp;
    }

    @Override
    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.host + BASE_URL_APPSERVER)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterfaces.NotesApi api = retrofit.create(ApiInterfaces.NotesApi.class);

        Call<List<VONote>> call = api.doGetNotes(Util.generateToken(sp.getString(Token.KEY, "")));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<VONote>> call, Response<List<VONote>> response) {
        if (response.code() == 200) {
            List<VONote> voList = response.body();
            uiCallback.onGetAllNotesSuccess(voList);
        } else {
            uiCallback.onGetAllNotesError();
        }
    }

    @Override
    public void onFailure(Call<List<VONote>> call, Throwable t) {
        uiCallback.onGetAllNotesError();
    }
}
