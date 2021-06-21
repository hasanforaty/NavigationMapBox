package com.hasan.foraty.mapboxtutorials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView ;


    private static final String SOURCE_ID = "Source_Id";
    public static final String LAYOUT_ID = "Layout_Id";
    public static final String ICON_ID = "Icon_Id";

    private GeoJsonSource source ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);
        source = new GeoJsonSource(SOURCE_ID);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(new Style.Builder()
                .fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                .withSource(source)
                .withImage(ICON_ID, BitmapFactory.decodeResource(getResources(),R.drawable.mapbox_marker_icon_default))
                .withLayer(new SymbolLayer(LAYOUT_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                )

        );
        source.setGeoJson(FeatureCollection.fromFeatures(symbolLayerIconFeatureList));
        mapboxMap.addOnMapClickListener(point ->{
            addingPoint(point);
            Toast.makeText(this,"this point is "+point,Toast.LENGTH_LONG).show();
            return true;
        });
        mapboxMap.addOnMapLongClickListener(point ->{
            symbolLayerIconFeatureList.add(Feature.fromGeometry(Point.fromLngLat(point.getLongitude(),point.getLatitude())));
            return true;
        });



    }

    private void addingPoint(LatLng point){
        symbolLayerIconFeatureList.add(Feature.fromGeometry(Point.fromLngLat(point.getLongitude(),point.getLatitude())));
        source.setGeoJson(FeatureCollection.fromFeatures(symbolLayerIconFeatureList));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    private final List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
    private List<Feature> mockList(){

        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-57.225365, -33.213144)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-54.14164, -33.981818)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-56.990533, -30.583266)));
        return symbolLayerIconFeatureList;
    }






}