package com.hasan.foraty.mapboxtutorials.network;

import java.util.ArrayList;
import java.util.List;


import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RetrofitServerApi {
    private final ServerApi serverApi;
    private static RetrofitServerApi retrofitServerApi;
    public static final String BASE_URL = "http://192.168.11.47/";


    private RetrofitServerApi() {
        List<ConnectionSpec> connectionSpecs = new ArrayList<>();
        connectionSpecs.add(ConnectionSpec.MODERN_TLS);
        connectionSpecs.add(ConnectionSpec.CLEARTEXT);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(connectionSpecs)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build();
        serverApi = retrofit.create(ServerApi.class);
    }
    public static RetrofitServerApi getInstance(){
        if (retrofitServerApi==null){
            retrofitServerApi = new RetrofitServerApi();
        }
        return retrofitServerApi;
    }

    public Call<ResponseBody> getPointDetail(String BBox, Double y, Double x){
        return serverApi.getResponceForClick(x,y,BBox);
    }
}
