package net.rickiekarp.homeassistant.net;

import android.os.AsyncTask;

import net.rickiekarp.homeassistant.domain.WorldList;
import net.rickiekarp.homeassistant.preferences.Constants;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.rickiekarp.homeassistant.preferences.Constants.URL.BASE_URL_LOGIN;

public class WorldsCall extends AsyncTask<String, Void, WorldList> {

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected WorldList doInBackground(String... params) {
        Request.Builder builder = new Request.Builder();
        String url = Constants.DEFAULT_HOST + BASE_URL_LOGIN + NetworkApi.WORLDS_ACTION;
        builder.url(url);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            return WorldList.parseFrom(response.body().byteStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}