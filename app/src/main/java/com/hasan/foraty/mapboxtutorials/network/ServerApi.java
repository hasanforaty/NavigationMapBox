package com.hasan.foraty.mapboxtutorials.network;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ServerApi {
    @Headers("Content-Type: application/json")
    @GET("wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetFeatureInfo&FORMAT=image%2Fpng&TRANSPARENT=true&QUERY_LAYERS=GeoTajak%3Aamlak_6566_2113&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624681452050&TILED=true&INFO_FORMAT=application%2Fjson&FEATURE_COUNT=50&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&")
    Call<ServerResponse> getResponceForClick(@Query("x") float x, @Query("y") float y, @Query("BBOX") String BBox);

    @Headers("Content-Type: application/json")
    @GET("wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetFeatureInfo&FORMAT=image%2Fpng&TRANSPARENT=true&QUERY_LAYERS=GeoTajak%3Aamlak_6566_2113&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624681452050&TILED=true&INFO_FORMAT=application%2Fjson&FEATURE_COUNT=50&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&")
    Call<ResponseBody> getResponceForClickTest(@Query("x") float x, @Query("y") float y, @Query("BBOX") String BBox);
}
