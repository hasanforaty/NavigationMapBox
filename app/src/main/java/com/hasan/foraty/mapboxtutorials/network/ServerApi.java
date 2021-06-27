package com.hasan.foraty.mapboxtutorials.network;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ServerApi {
    @Headers("Content-Type: application/json")
    @GET("wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetFeatureInfo&FORMAT=image/png&TRANSPARENT=true&QUERY_LAYERS=GeoTajak:amlak_6566_2113&LAYERS=GeoTajak:amlak_6566_2113&STYLES=&DATE=1624771588794&TILED=true&INFO_FORMAT=application/json&FEATURE_COUNT=50&WIDTH=256&HEIGHT=256&SRS=EPSG:3857")
    Call<ServerResponse> getResponseForClick(@Query("X") int x, @Query("Y") int y, @Query("BBOX") String BBox);
}
