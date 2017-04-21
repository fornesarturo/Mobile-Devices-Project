package watsalacanoa.todolisttest;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import watsalacanoa.todolisttest.objects.Placioli;
import watsalacanoa.todolisttest.adapters.PlacioliAdapter;

public class Places extends AppCompatActivity {

    private ArrayList<Placioli> placesTest;
    private ListView places;
    private FloatingActionButton addPlaceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        addPlaceButton = (FloatingActionButton) findViewById(R.id.fab_add_place);
        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace();
            }
        });

        placesTest = new ArrayList<>();
        placesTest.add(new Placioli("A", "B", new LatLng(20, 100)));
        placesTest.add(new Placioli("C", "D", new LatLng(-10, -10)));
        placesTest.add(new Placioli("Teenage", "Mutant", new LatLng(-20, 20)));
        placesTest.add(new Placioli("Ninja", "Turtles", new LatLng(30, -40)));

        places = (ListView)findViewById(R.id.lvPlaces);
        final PlacioliAdapter adapterTest = new PlacioliAdapter(placesTest, this);
        places.setAdapter(adapterTest);
        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Placioli currentPlacioli = adapterTest.getItem(position);
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                Bundle loQueSeMePegaLaGana = new Bundle();
                loQueSeMePegaLaGana.putParcelable("latLng", currentPlacioli.getLatLng());
                i.putExtra("bundle", loQueSeMePegaLaGana);
                startActivity(i);
            }
        });
    }

    private void addPlace() {

    }
}

