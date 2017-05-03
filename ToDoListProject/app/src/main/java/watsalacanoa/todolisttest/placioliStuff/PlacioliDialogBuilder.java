package watsalacanoa.todolisttest.placioliStuff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
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

    public interface PlacioliDialogInterface {
        public void onFinishPlacioliDialog();
    }

    private PlacioliDialogInterface listener;

    private TaskHelper placioliliDB;
    private EditText etPlaceTitle, etPlaceDesc;
    private TextView tvLat, tvLng;
    private LatLng locationLatLng;
<<<<<<< HEAD
    private String titlePlace, descPlace;
=======
    private TextView tvDate;
>>>>>>> refs/remotes/origin/Sebastian/dev

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
        this.etPlaceDesc = (EditText) view.findViewById(R.id.etDescDialogPlace);
        this.tvLat = (TextView) view.findViewById(R.id.tvLatDialog);
        this.tvLng = (TextView) view.findViewById(R.id.tvLngDialog);
        this.tvDate = (TextView)view.findViewById(R.id.tvDate);

        this.tvLat.setText(this.locationLatLng.latitude+"");
        this.tvLng.setText(this.locationLatLng.longitude+"");

        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        titlePlace = etPlaceTitle.getText().toString();
                        descPlace = etPlaceDesc.getText().toString();
                        SQLiteDatabase db = placioliliDB.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.PLACIOLI_TITLE, titlePlace);
                        values.put(Task.PLACIOLI_DESCRIPTION, descPlace);
                        values.put(Task.PLACIOLI_LAT, locationLatLng.latitude);
                        values.put(Task.PLACIOLI_LNG, locationLatLng.longitude);
                        Log.d("DB INSERT", values.toString());
                        db.insertWithOnConflict(Task.TABLE_PLACIOLI, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        listener.onFinishPlacioliDialog();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    PlacioliDialogBuilder.this.getDialog().cancel();
                }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (PlacioliDialogInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ItemDialogListener");
        }
    }
}
