package watsalacanoa.todolisttest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;
import watsalacanoa.todolisttest.objects.Pending;
import watsalacanoa.todolisttest.objects.Placioli;
import watsalacanoa.todolisttest.adapters.PlacioliAdapter;
import watsalacanoa.todolisttest.placioliStuff.PlacioliDialogBuilder;

public class Places extends AppCompatActivity {

    private ArrayList<Placioli> data = new ArrayList<>();
    private ListView lvPlaces;
    private FloatingActionButton addPlaceButton;
    private PlacioliAdapter adapter;
    private TaskHelper dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        this.addPlaceButton = (FloatingActionButton) findViewById(R.id.fab_add_place);
        this.addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace();
            }
        });

        this.lvPlaces = (ListView)findViewById(R.id.lvPlaces);
        this.lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Placioli currentPlace = adapter.getItem(position);
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("latLng", currentPlace.getLatLng());
                bundle.putString("title", currentPlace.getTitle());
                i.putExtra("bundle", bundle);
                startActivity(i);
            }
        });

        this.updatePlaces();
    }

    private void addPlace() {
        PlacioliDialogBuilder pdb = new PlacioliDialogBuilder();
        pdb.setLatLng(new LatLng(20, 100));
        pdb.show(getFragmentManager(), "ap");
        this.updatePlaces();
    }

    private void updatePlaces() {
        this.data.clear();

        this.dbh = new TaskHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.query(Task.TABLE_PLACIOLI, null, null, null, null, null, null);

        while(cursor.moveToNext()){
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            LatLng latLng = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
            this.data.add(new Placioli(title, desc, latLng));
        }

        cursor.close();

        this.adapter = new PlacioliAdapter(this.data, this);
        this.lvPlaces.setAdapter(this.adapter);
    }
}

