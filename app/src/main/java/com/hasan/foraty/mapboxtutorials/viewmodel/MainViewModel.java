package com.hasan.foraty.mapboxtutorials.viewmodel;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasan.foraty.mapboxtutorials.common.MapboxStyle;
import com.hasan.foraty.mapboxtutorials.network.RetrofitServerApi;
import com.hasan.foraty.mapboxtutorials.network.ServerResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.TileSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    public static final String Terrain_Source_Id = "terrain_Source_Id";
    public static final String Terrain_Layer_Id = "terrain_Layer_Id";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String WMS_TILE_ID = "WMS_TILE_ID";

    public static final String WMS_Source_Id = "Wms_Source_Id";
    public static final String WMS_Layer_Id =  "Wms_layer_Id";
    public static final String COUNTRY_LAYER_ID ="country-label";
    private final List<Feature> dataFromRep = new ArrayList<>();
    private final LinkedList<Point> currentPoint = new LinkedList<>();
    private final GeoJsonSource geoJsonSource ;
    private Bitmap currentLocationIcon;
    private MutableLiveData<Style.Builder> currentMapBoxStyle ;

    private MutableLiveData<Map<Source,Layer>> listOfPolygonLayer;

    private MutableLiveData<Boolean> wmsLayoutState = new MutableLiveData<>(false);

    private TileSet wmsTileSet ;

    private Boolean responseState = false;
    private final MutableLiveData<LatLng> currentFocusMutable = new MutableLiveData<>();

    private final RetrofitServerApi retrofit = RetrofitServerApi.getInstance();

    public Boolean getResponseState() {
        return responseState;
    }

    public void setResponseState(Boolean responseState) {
        this.responseState = responseState;
    }

    public MainViewModel() {
        geoJsonSource = new GeoJsonSource(SOURCE_ID);
        ArrayList<String> wmsTileSetUrls = new ArrayList<>();
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050755.159054551%2C3748471.8671050426%2C6051366.655280833%2C3749083.363331324");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050755.159054551%2C3749083.3633313254%2C6051366.655280833%2C3749694.8595576067");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050143.66282827%2C3748471.8671050426%2C6050755.159054552%2C3749083.363331324");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6051366.655280832%2C3747860.3708787616%2C6051978.151507114%2C3748471.867105043");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6051366.655280832%2C3747860.3708787616%2C6051978.151507114%2C3748471.867105043");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050755.159054551%2C3749083.3633313254%2C6051366.655280833%2C3749694.8595576067");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050755.159054551%2C3748471.8671050426%2C6051366.655280833%2C3749083.363331324");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6051366.655280832%2C3749083.3633313254%2C6051978.151507114%2C3749694.8595576067");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6051366.655280832%2C3748471.8671050426%2C6051978.151507114%2C3749083.363331324");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050143.66282827%2C3747860.3708787616%2C6050755.159054552%2C3748471.867105043");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050143.66282827%2C3747860.3708787616%2C6050755.159054552%2C3748471.867105043");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050143.66282827%2C3749083.3633313254%2C6050755.159054552%2C3749694.8595576067");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050143.66282827%2C3749083.3633313254%2C6050755.159054552%2C3749694.8595576067");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050143.66282827%2C3748471.8671050426%2C6050755.159054552%2C3749083.363331324");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6051366.655280832%2C3749083.3633313254%2C6051978.151507114%2C3749694.8595576067");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6051366.655280832%2C3748471.8671050426%2C6051978.151507114%2C3749083.363331324");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511544684&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050755.159054551%2C3747860.3708787616%2C6051366.655280833%2C3748471.867105043");
//        wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624511536316&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=6050755.159054551%2C3747860.3708787616%2C6051366.655280833%2C3748471.867105043");
            wmsTileSetUrls.add("http://192.168.11.47/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=GeoTajak%3Aamlak_6566_2113&STYLES=&DATE=1624515051294&TILED=true&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX={bbox-epsg-3857}");
        String [] tiles = new String[wmsTileSetUrls.size()];
        wmsTileSetUrls.toArray(tiles);

        wmsTileSet = new TileSet(WMS_TILE_ID,tiles);

    }

    public TileSet getWmsTileSet() {
        return wmsTileSet;
    }

    public LiveData<Boolean> getWmsLayoutState() {
        return wmsLayoutState;
    }

    public void changeWmsLayoutState(Boolean state){
        wmsLayoutState.postValue(state);
    }

    public LiveData<Map<Source, Layer>> getListOfPolygonLayer() {
        if (listOfPolygonLayer==null){
            listOfPolygonLayer = new MutableLiveData<>(new HashMap<>());
        }
        return listOfPolygonLayer;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
     public void createPolygon(){
        if (getDataFromRep().size()<1) return;
        LinkedList<Point> points = currentPoint;
        Point firstPoint=points.getFirst();
        points.addLast(firstPoint);

        String sourceId = UUID.randomUUID().toString();
        String layerId = UUID.randomUUID().toString();
        Map<Source,Layer> currentPolygons = new HashMap<>(listOfPolygonLayer.getValue());
        List<List<Point>> outerList = new ArrayList<>();
        outerList.add(points);

        Source currentSource =new GeoJsonSource(sourceId,Polygon.fromLngLats(outerList));
        Layer polygonLayer = new FillLayer(layerId,sourceId).withProperties(fillColor(Color.parseColor("#3bb2d0")));

        currentPolygons.put(currentSource,polygonLayer);
        listOfPolygonLayer.postValue(currentPolygons);
        currentPoint.clear();
        dataFromRep.clear();
    }

    public Bitmap getCurrentLocationIcon(Resources res) {
        if (currentLocationIcon==null){
            currentLocationIcon =  MapboxStyle.addPlusIcons(res);
        }
        return currentLocationIcon ;
    }

    public LiveData<Style.Builder> getCurrentMapBoxStyle() {
        if (currentMapBoxStyle==null){
            currentMapBoxStyle = new MutableLiveData<>(MapboxStyle.monochromeDarkSyle());
        }
        return currentMapBoxStyle;
    }
    public void changeCurrentStyle(Style.Builder styleBuilder){
        currentMapBoxStyle.postValue(styleBuilder);
    }








    public void addNewpoitn(LatLng latLng){
        Point addedPoint = Point.fromLngLat(latLng.getLongitude(),latLng.getLatitude());
        currentPoint.add(addedPoint);
        Feature currentFeature = Feature.fromGeometry(addedPoint);
        dataFromRep.add(currentFeature);
        geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(dataFromRep));
    }

    public void changeCurrentFocus(LatLng latLng){
        currentFocusMutable.postValue(latLng);
    }


    public List<Feature> getDataFromRep() {
        return dataFromRep;
    }

    public GeoJsonSource getGeoJsonSource() {
        return geoJsonSource;
    }

    public LiveData<LatLng> getCurrentFocus() {
        return currentFocusMutable;
    }

    public void getFocusPointClick(int x, int y, String bbox, Consumer<ServerResponse> callBack){
        retrofit.getPointDetail(bbox,y,x).enqueue(new Callback<ServerResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d(TAG, "onResponse: body :"+response.body());
                callBack.accept(response.body());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: faile becouse ",t);
                Log.d(TAG, "onFailure: url request "+call.request().url());
                Log.d(TAG, "onFailure: BBox :"+bbox.toString());

            }
        });
    }


}
