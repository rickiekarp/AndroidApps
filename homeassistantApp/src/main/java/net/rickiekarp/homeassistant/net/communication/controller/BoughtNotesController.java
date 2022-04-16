package net.rickiekarp.homeassistant.net.communication.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rickiekarp.homeassistant.interfaces.IOnRemoveNoteResult;
import net.rickiekarp.homeassistant.interfaces.IRunController;
import net.rickiekarp.homeassistant.net.communication.ApiInterfaces;
import net.rickiekarp.homeassistant.net.communication.vo.VONote;
import net.rickiekarp.homeassistant.preferences.Configuration;
import net.rickiekarp.homeassistant.utils.Util;

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

public class BoughtNotesController implements Callback<VONote>, IRunController {

    private String token;
    private IOnRemoveNoteResult uiCallback;
    private int id;
    private int noteId;

    public BoughtNotesController(String token, IOnRemoveNoteResult uiCallback, int id, int noteid) {
        this.token = token;
        this.uiCallback = uiCallback;
        this.id = id;
        this.noteId = noteid;
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

        VONote vo = new VONote(noteId);
        Call<VONote> call = api.doMarkAsBought(Util.generateToken(token), vo);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<VONote> call, Response<VONote> response) {
        if (response.code() == 200) {
            uiCallback.onRemoveNoteSuccess(id);
        } else {
            uiCallback.onRemoveNoteError();
        }
    }

    @Override
    public void onFailure(Call<VONote> call, Throwable t) {
        uiCallback.onRemoveNoteError();
        t.printStackTrace();
    }
}
