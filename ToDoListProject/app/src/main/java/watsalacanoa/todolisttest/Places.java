package watsalacanoa.todolisttest;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import watsalacanoa.todolisttest.adapters.PlacioliAdapter;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;
import watsalacanoa.todolisttest.objects.Placioli;
import watsalacanoa.todolisttest.placioliStuff.PlacioliDialogBuilder;

public class Places extends AppCompatActivity implements PlacioliDialogBuilder.PlacioliDialogInterface {

    private ArrayList<Placioli> data = new ArrayList<>();
    private ListView lvPlaces;
    private FloatingActionButton addPlaceButton;
    private PlacioliAdapter adapter;
    private TaskHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        this.checkPermission();

        this.addPlaceButton = (FloatingActionButton) findViewById(R.id.fab_add_place);
        this.addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace();
            }
        });

        this.lvPlaces = (ListView) findViewById(R.id.lvPlaces);
        registerForContextMenu(lvPlaces);

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

        this.checkPermission();

        PlacioliDialogBuilder pdb = new PlacioliDialogBuilder();

        pdb.setLatLng(getLocation());
        pdb.show(getFragmentManager(), "ap");
    }

    private void updatePlaces() {
        this.data.clear();

        this.dbh = new TaskHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.query(Task.TABLE_PLACIOLI, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            LatLng latLng = new LatLng(cursor.getDouble(3), cursor.getDouble(4));
            int id = cursor.getInt(0);
            this.data.add(new Placioli(title, desc, latLng, id));
        }

        cursor.close();
        db.close();

        this.adapter = new PlacioliAdapter(this.data, this);
        Log.d("SIZE", this.data.size() + "");
        this.lvPlaces.setAdapter(this.adapter);
    }

    @Override
    public void onFinishPlacioliDialog() {
        this.updatePlaces();
    }

    private LatLng getLocation() {
        checkPermission();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        return new LatLng(latitude, longitude);
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.lvPlaces) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_places, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position;
        Placioli placioli;
        String clause;
        String args[] = {""};
        SQLiteDatabase db;

        if(item.getItemId() == R.id.action_editPlace) {
            position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
            placioli = this.adapter.getItem(position);

            this.checkPermission();

            PlacioliDialogBuilder pdb = new PlacioliDialogBuilder();

            pdb.setLatLng(placioli.getLatLng());
            pdb.show(getFragmentManager(), "ap");
            pdb.setUpdate(placioli.getTitle(), placioli.getDescription(), placioli.getId());

            return true;
        }
        else if(item.getItemId() == R.id.action_deletePlace) {
            position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
            placioli = this.adapter.getItem(position);

            this.dbh = new TaskHelper(this);
            db = this.dbh.getWritableDatabase();

            clause = Task.PLACIOLI_ID + " = ?";
            args[0] = placioli.getId()+"";
            db.delete(Task.TABLE_PLACIOLI, clause, args);

            this.updatePlaces();

            return true;
        }
        else {
            return super.onContextItemSelected(item);
        }
    }
}

