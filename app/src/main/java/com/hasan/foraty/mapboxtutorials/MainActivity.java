package com.hasan.foraty.mapboxtutorials;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hasan.foraty.mapboxtutorials.common.MapboxStyle;
import com.hasan.foraty.mapboxtutorials.viewmodel.MainViewModel;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.MenuRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapbox.getInstance(getApplicationContext(), getString(R.string.map_box_access_token));
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (viewModel.getCurrentFocuse().getValue()!=null){
                viewModel.addNewpoitn(viewModel.getCurrentFocuse().getValue());
            }
        });

        FloatingActionButton customizeButton = findViewById(R.id.customize);
        customizeButton.setOnClickListener(view ->{
            showMenu(view,R.menu.popup_menu);
        });

        FloatingActionButton createPolygonIcon = findViewById(R.id.polygon);
        createPolygonIcon.setOnClickListener(view ->{
            viewModel.createPolygon();
        });

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (currentFragment == null){
            Fragment startingFragment= new MapFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.nav_host_fragment,startingFragment)
                    .commit();
        }


    }

    private void showMenu(View view, @MenuRes int popup_menu) {
        PopupMenu menu = new PopupMenu(this,view);
        menu.getMenuInflater().inflate(popup_menu,menu.getMenu());
        menu.setOnMenuItemClickListener(menuItem ->{
            switch (menuItem.getItemId()){
                case R.id.monochromeDarkSyle:
                viewModel.changeCurrentStyle(MapboxStyle.monochromeDarkSyle());
                break;
                case R.id.defaultStyle:
                    viewModel.changeCurrentStyle(MapboxStyle.defaultSty());
                    break;
            }
            return true;
        });
        menu.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}