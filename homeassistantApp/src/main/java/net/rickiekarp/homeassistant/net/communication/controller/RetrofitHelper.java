package net.rickiekarp.homeassistant.net.communication.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.rickiekarp.homeassistant.preferences.Configuration;
import net.rickiekarp.homeassistant.preferences.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastian on 08.12.17.
 */
public class RetrofitHelper {

    private String serverType;
    private static RetrofitHelper instance;

    private Retrofit retrofitClient;
    private OkHttpClient okHttpClient;

    private RetrofitHelper(String serverType) {
        this.serverType = serverType;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
        retrofitClient = createRetrofitClient(okHttpClient, serverType);
    }

    public synchronized static RetrofitHelper getInstance(String serverType) {
        if (instance == null) {
            instance = new RetrofitHelper(serverType);
        }
        return  instance;
    }

    private Retrofit createRetrofitClient(OkHttpClient okHttpClient, String serverType) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(getBaseUrl(serverType))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    protected <C> C createService(Class<C> serviceClass) {
        return retrofitClient.create(serviceClass);
    }

    protected <C> C createService(Class<C> serviceClass, int timeout) {
        return createRetrofitClient(okHttpClient
                .newBuilder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .build(), serverType)
                .create(serviceClass);
    }

    private String getBaseUrl(String serverType) {
        return serverType.equals(Constants.LOGIN_SERVER) ?
                Configuration.host + Constants.URL.BASE_URL_LOGIN :
                Configuration.host + Constants.URL.BASE_URL_APPSERVER;

    }
}
