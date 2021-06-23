package com.hasan.foraty.mapboxtutorials.viewmodel;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasan.foraty.mapboxtutorials.common.MapboxStyle;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public class MainViewModel extends ViewModel {

    public static final String Terrain_Source_Id = "terrain_Source_Id";
    public static final String Terrain_Layer_Id = "terrain_Layer_Id";
    private static final String SOURCE_ID = "SOURCE_ID";
    private final List<Feature> dataFromRep = new ArrayList<>();
    private final LinkedList<Point> currentPoint = new LinkedList<>();
    private final GeoJsonSource geoJsonSource ;
    private Bitmap currentLocationIcon;
    private MutableLiveData<Style.Builder> currentMapBoxStyle ;

    private MutableLiveData<Map<Source,Layer>> listOfPolygonLayer;

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

    public MainViewModel() {
        geoJsonSource = new GeoJsonSource(SOURCE_ID);
    }

    private final MutableLiveData<LatLng> currentFocusMutable = new MutableLiveData<>();




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

    public LiveData<LatLng> getCurrentFocuse() {
        return currentFocusMutable;
    }


}
