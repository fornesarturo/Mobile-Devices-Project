package watsalacanoa.todolisttest.placioliStuff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.caldroidStuff.CalendioliDialogBuilder;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;

/**
 * Created by miguel on 21/04/17.
 */

public class PlacioliDialogBuilder extends DialogFragment {

    private TaskHelper placioliliDB;
    private EditText etPlaceTitle;
    private TextView tvLat;
    private TextView tvLng;
    private LatLng locationLatLng;
    private TextView tvDate;

    public void setLatLng(LatLng latLng) {
        this.locationLatLng = latLng;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Save your location");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_placioli, null);

        this.placioliliDB = new TaskHelper(getActivity());
        this.etPlaceTitle = (EditText) view.findViewById(R.id.etTitleDialogPlace);
        this.tvLat = (TextView) view.findViewById(R.id.tvLatDialog);
        this.tvLng = (TextView) view.findViewById(R.id.tvLngDialog);
        this.tvDate = (TextView)view.findViewById(R.id.tvDate);

        tvDate.setText(this.dateText);
        Toast.makeText(getActivity(), "DATE: " + dateText, Toast.LENGTH_SHORT).show();
        Log.d("DB", "0");
        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title = etTitle.getText().toString();
                        event = etEvent.getText().toString();
                        SQLiteDatabase db = calendioliDB.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.CALENDIOLI_DATE, dateText);
                        values.put(Task.CALENDIOLI_TITLE, title);
                        values.put(Task.CALENDIOLI_EVENT, event);
                        Log.d("DB INSERT", values.toString());
                        db.insertWithOnConflict(Task.TABLE_CALENDIOLI, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CalendioliDialogBuilder.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
