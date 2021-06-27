package com.hasan.foraty.mapboxtutorials;

import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hasan.foraty.mapboxtutorials.common.MapboxStyle;
import com.hasan.foraty.mapboxtutorials.databinding.FragmentMapBinding;
import com.hasan.foraty.mapboxtutorials.network.ServerResponse;
import com.hasan.foraty.mapboxtutorials.viewmodel.MainViewModel;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.geometry.VisibleRegion;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;

import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";
    private FragmentMapBinding binding ;
    private MapView mapView ;
    private MapboxMap mapboxMap;



    private MainViewModel mainViewModel;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map,container,false);

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            MapFragment.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(MapboxStyle.defaultSty(),onStyleLoaded);

        });
        mainViewModel.getWmsLayoutState().observe(getViewLifecycleOwner(), this::changeInWMS);

        mainViewModel.getCurrentMapBoxStyle().observe(getViewLifecycleOwner(), builder -> {
            if (mapboxMap==null) return;
            if (mapboxMap.getStyle()==null)return;
            Style style = mapboxMap.getStyle();
            style.removeLayer(MainViewModel.Terrain_Layer_Id);
            style.removeSource(MainViewModel.Terrain_Source_Id);

            if (builder.getUri().equals(MapboxStyle.defaultSty().getUri())) return;

            style.addSource(
                    new RasterSource(MainViewModel.Terrain_Source_Id,new TileSet("tileSet", "http://MT0.google.com/vt/lyrs=r&x={x}&y={y}&z={z}")
            ));



//            style.addSource(
//                    new RasterSource("terrain-data",new TileSet("tileSet","http://MT0.google.com/vt/lyrs=r&x={x}&y={y}&z={z}"))
//            );

//            LineLayer terrainData = new LineLayer("terrain-data", "terrain-data");
////            terrainData.setSourceLayer("contour");
//            terrainData.setProperties(
//                    lineJoin(Property.LINE_JOIN_ROUND),
//                    lineCap(Property.LINE_CAP_ROUND),
//                    lineColor(Color.parseColor("#ff69b4")),
//                    lineWidth(1.9f)
//            );
            Log.d(TAG, "onCreateView: "+style);
            if (style.getLayer(MainViewModel.WMS_Layer_Id)!=null){
                style.addLayerBelow(new RasterLayer(MainViewModel.Terrain_Layer_Id,MainViewModel.Terrain_Source_Id),MainViewModel.WMS_Layer_Id);
            }else {
                style.addLayerBelow(new RasterLayer(MainViewModel.Terrain_Layer_Id,MainViewModel.Terrain_Source_Id),MainViewModel.COUNTRY_LAYER_ID);
            }
            LatLng latLng = mapboxMap.getProjection().getVisibleRegion().farLeft;
//            LatLngBounds lat = mapboxMap.getProjection().getVisibleRegion().latLngBounds.getLatNorth();
//            BoundingBox boundingBox = BoundingBox.fromJson()

        });

        mainViewModel.getListOfPolygonLayer().observe(getViewLifecycleOwner(),sourceLayerMap -> {
            if (mapboxMap==null)return;
            if (mapboxMap.getStyle()==null)return;
            if (!mapboxMap.getStyle().isFullyLoaded()) return;
            sourceLayerMap.forEach((source, layer) -> {
                try {
                    mapboxMap.getStyle().addSource(source);
                    mapboxMap.getStyle().addLayerBelow(layer,LAYER_ID);
                }catch (Exception e){
                    Log.d(TAG, "onCreateView:"+e);
                }finally {

                }
            });
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void changeInWMS(Boolean state) {
        if (mainViewModel == null)return;
        if (mapboxMap==null) return;
        if (mapboxMap.getStyle() == null) return;
        if (!mapboxMap.getStyle().isFullyLoaded()) return;
        mapboxMap.getStyle().removeLayer(MainViewModel.WMS_Layer_Id);
        mapboxMap.getStyle().removeSource(MainViewModel.WMS_Source_Id);
        if (state){
            RasterSource source = new RasterSource(MainViewModel.WMS_Source_Id,mainViewModel.getWmsTileSet(),256);

            RasterLayer layer = new RasterLayer(MainViewModel.WMS_Layer_Id,MainViewModel.WMS_Source_Id);
            mapboxMap.getStyle().addSource(source);
            mapboxMap.getStyle().addLayerBelow(layer,MainViewModel.COUNTRY_LAYER_ID);


        }
        List<Layer> layers = mapboxMap.getStyle().getLayers();
        for (int i =0;i<layers.size();i++){
            Log.d(TAG, "changeInWMS: "+layers.get(i).getId());
        }


    }


    private MapboxMap.OnMapClickListener onMapClickListener(){
        return point -> {
            mainViewModel.changeCurrentFocus(point);
            if (mainViewModel.getResponseState()){
                getResponse(point);
            }
            return true;
        } ;
    }

    private void getResponse(LatLng point) {
        Projection currentProjection =mapboxMap.getProjection();
        VisibleRegion visibleRegion = currentProjection.getVisibleRegion();
        LatLngBounds latLngBounds = visibleRegion.latLngBounds;
        BoundingBox boundingBox =BoundingBox.fromLngLats(latLngBounds.getLonWest(),latLngBounds.getLatSouth(),latLngBounds.getLonEast(),latLngBounds.getLatNorth());
        PointF currentScreenLocation = currentProjection.toScreenLocation(point);
        int x = (int)currentScreenLocation.x;
        int y =(int)currentScreenLocation.y;
        String boundary = boundingBox.toJson().replace("[","").replace("]","");
        mainViewModel.getFocusPointClick(x,y,boundary, this::serverResponsePrint);
    }

    private void serverResponsePrint(ServerResponse serverResponse){

        String builder = "Total Features: " + serverResponse.totalFeatures +
                "type : " + serverResponse.type +
                "features : { " + serverResponse.features.toString();
        Log.d(TAG, "serverResponcePrint: "+ builder);
        Toast.makeText(requireContext(),"response = "+builder,Toast.LENGTH_LONG).show();
    }

    private final Style.OnStyleLoaded onStyleLoaded = style -> {
        style.addSource(mainViewModel.getGeoJsonSource());
        style.addImage(ICON_ID,mainViewModel.getCurrentLocationIcon(getResources()));
        style.addLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                .withProperties(
                        iconImage(ICON_ID),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                )
        );
        mapboxMap.addOnMapClickListener(onMapClickListener());
    };


}