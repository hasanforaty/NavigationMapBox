package com.hasan.foraty.mapboxtutorials;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hasan.foraty.mapboxtutorials.common.MapboxStyle;
import com.hasan.foraty.mapboxtutorials.databinding.FragmentMapBinding;
import com.hasan.foraty.mapboxtutorials.viewmodel.MainViewModel;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;

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
            mapboxMap.addOnMapClickListener(onMapClickListener());
        });

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

            style.addLayer(new RasterLayer(MainViewModel.Terrain_Layer_Id,MainViewModel.Terrain_Source_Id));
            mapboxMap.addOnMapClickListener(onMapClickListener());
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


    private MapboxMap.OnMapClickListener onMapClickListener(){
        return point -> {
            mainViewModel.changeCurrentFocus(point);
            return true;
        } ;
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
    };

}