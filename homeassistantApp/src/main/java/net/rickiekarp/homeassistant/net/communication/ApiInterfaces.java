package net.rickiekarp.homeassistant.net.communication;


import net.rickiekarp.homeassistant.domain.ShoppingNoteList;
import net.rickiekarp.homeassistant.domain.ShoppingStoreList;
import net.rickiekarp.homeassistant.net.communication.vo.VOCredentials;
import net.rickiekarp.homeassistant.net.communication.vo.VOData;
import net.rickiekarp.homeassistant.net.communication.vo.VONote;
import net.rickiekarp.homeassistant.net.communication.vo.VOResult;
import net.rickiekarp.homeassistant.net.communication.vo.VOToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by sebastian on 10.11.17.
 */

public abstract class ApiInterfaces {

    private static class Params {
        static final String AUTHORIZATION = "Authorization";

        static final String NOTE_DOMAIN = "note";
    }

    public interface NotesApi {
        @GET("shopping/get")
        Call<List<VONote>> doGetNotes(@Header(Params.AUTHORIZATION) String apiToken);

        @POST("shopping/add")
        Call<VONote> doAddNotes(@Header(Params.AUTHORIZATION) String apiToken, @Body VONote notes);

        @POST("shopping/markAsBought")
        Call<VONote> doMarkAsBought(@Header(Params.AUTHORIZATION) String apiToken, @Body VONote notes);

        @POST("shopping/update")
        Call<VONote> doUpdateNotes(@Header(Params.AUTHORIZATION) String apiToken, @Body VONote notes);

        @POST("shopping/remove")
        Call<VOResult> doRemoveNotes(@Header(Params.AUTHORIZATION) String apiToken, @Body VONote notes);

        @GET("shopping/history")
        Call<ShoppingNoteList> doGetHistory(@Header(Params.AUTHORIZATION) String apiToken);

        @GET("shopping/stores")
        Call<ShoppingStoreList> doGetStoreList(@Header(Params.AUTHORIZATION) String apiToken);
    }

    public interface LoginApi {
        @POST("account/authorize")
        Call<VOToken> doGetToken(@Body VOCredentials credentials);

        @POST("account/login")                                         // Token String: "Basic token"
        Call<VOData> doLogin(@Header(Params.AUTHORIZATION) String token);

        @POST("account/create")
        Call<VOToken> doCreateAccount(@Body VOCredentials credentials);

    }

}