package com.hasan.foraty.mapboxtutorials.viewmodel;

import android.content.res.Resources;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasan.foraty.mapboxtutorials.common.MapboxStyle;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private static final String SOURCE_ID = "SOURCE_ID";
    private final List<Feature> dataFromRep = new ArrayList<>();
    private final GeoJsonSource geoJsonSource ;
    private Bitmap currentLocationIcon;
    private MutableLiveData currentMapBoxStyle ;

    public Bitmap getCurrentLocationIcon(Resources res) {
        if (currentLocationIcon==null){
            currentLocationIcon =  MapboxStyle.addPlusIcons(res);
        }
        return currentLocationIcon ;
    }

    public LiveData<Style.Builder> getCurrentMapBoxStyle() {
        if (currentMapBoxStyle==null){
            currentMapBoxStyle = new MutableLiveData(MapboxStyle.monochromeDarkSyle());
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
        Feature currentFeature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(),latLng.getLatitude()));
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
