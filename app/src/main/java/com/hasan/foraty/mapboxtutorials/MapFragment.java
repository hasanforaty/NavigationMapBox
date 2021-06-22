package com.hasan.foraty.mapboxtutorials;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hasan.foraty.mapboxtutorials.databinding.FragmentMapBinding;
import com.hasan.foraty.mapboxtutorials.viewmodel.MainViewModel;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CustomLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map,container,false);

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            MapFragment.this.mapboxMap = mapboxMap;
            configureMapBoxMapStyle(mapboxMap);

        });

        mainViewModel.getCurrentMapBoxStyle().observe(getViewLifecycleOwner(), builder -> {
            if (mapboxMap==null) return;
            configureMapBoxMapStyle(mapboxMap);
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void configureMapBoxMapStyle(MapboxMap mapboxMap){
        mapboxMap.setStyle(
                mainViewModel.getCurrentMapBoxStyle().getValue()
                .withImage(ICON_ID,mainViewModel.getCurrentLocationIcon(getResources()))
                .withSource(mainViewModel.getGeoJsonSource())
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                ), style -> {

            // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.


        });

        mapboxMap.addOnMapClickListener(onMapClickListener());
    }

    private MapboxMap.OnMapClickListener onMapClickListener(){
        return point -> {
            mainViewModel.changeCurrentFocus(point);
            return true;
        } ;
    }

    private void changeStyle(Style.Builder styleBuilder){
        if (mapboxMap==null) return;
        mainViewModel.changeCurrentStyle(styleBuilder);
        configureMapBoxMapStyle(mapboxMap);
    }

}