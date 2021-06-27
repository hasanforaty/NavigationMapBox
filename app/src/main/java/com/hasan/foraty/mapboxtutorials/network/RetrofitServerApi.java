package com.hasan.foraty.mapboxtutorials.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServerApi {
    private final ServerApi serverApi;
    private static RetrofitServerApi retrofitServerApi;
    public static final String BASE_URL = "http://192.168.11.47/";


    private RetrofitServerApi() {
        List<ConnectionSpec> connectionSpecs = new ArrayList<>();
        connectionSpecs.add(ConnectionSpec.MODERN_TLS);
        connectionSpecs.add(ConnectionSpec.CLEARTEXT);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(connectionSpecs)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        serverApi = retrofit.create(ServerApi.class);
    }
    public static RetrofitServerApi getInstance(){
        if (retrofitServerApi==null){
            retrofitServerApi = new RetrofitServerApi();
        }
        return retrofitServerApi;
    }

    public Call<ServerResponse> getPointDetail(String BBox, int y, int x){
        return serverApi.getResponseForClick(x,y,BBox);
    }
}
