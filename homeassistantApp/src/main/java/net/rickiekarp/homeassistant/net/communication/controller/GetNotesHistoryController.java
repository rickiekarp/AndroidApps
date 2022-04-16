package net.rickiekarp.homeassistant.net.communication.controller;

import android.content.SharedPreferences;

import net.rickiekarp.homeassistant.domain.ShoppingNoteList;
import net.rickiekarp.homeassistant.interfaces.IOnGetHistoryNotesResult;
import net.rickiekarp.homeassistant.interfaces.IRunController;
import net.rickiekarp.homeassistant.net.communication.ApiInterfaces;
import net.rickiekarp.homeassistant.preferences.Configuration;
import net.rickiekarp.homeassistant.preferences.Token;
import net.rickiekarp.homeassistant.utils.Util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.protobuf.ProtoConverterFactory;

import static net.rickiekarp.homeassistant.preferences.Constants.URL.BASE_URL_APPSERVER;

/**
 * Created by sebastian on 22.11.17.
 */
public class GetNotesHistoryController implements Callback<ShoppingNoteList>, IRunController {

    private SharedPreferences sp;
    private IOnGetHistoryNotesResult uiCallback;

    public GetNotesHistoryController(SharedPreferences sp, IOnGetHistoryNotesResult uiCallback) {
        this.uiCallback = uiCallback;
        this.sp = sp;
    }

    @Override
    public void start() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.host + BASE_URL_APPSERVER)
                .client(okHttpClient)
                .addConverterFactory(ProtoConverterFactory.create())
                .build();

        ApiInterfaces.NotesApi api = retrofit.create(ApiInterfaces.NotesApi.class);

        Call<ShoppingNoteList> call = api.doGetHistory(Util.generateToken(sp.getString(Token.KEY, "")));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ShoppingNoteList> call, Response<ShoppingNoteList> response) {
        if (response.code() == 200) {
            ShoppingNoteList voList = response.body();
            uiCallback.onGetAllNotesSuccess(voList);
        } else {
            uiCallback.onGetAllNotesError();
        }
    }

    @Override
    public void onFailure(Call<ShoppingNoteList> call, Throwable t) {
        uiCallback.onGetAllNotesError();
    }
}
