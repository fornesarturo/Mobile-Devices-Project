package watsalacanoa.todolisttest.caldroidStuff;

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

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;

/**
 * Created by spide on 22/4/2017.
 */

public class CalendioloDialogEditBuilder extends DialogFragment{

    private EditText etTitle,
            etEvent;
    private TextView tvDate;
    private String dateText;
    private String title;
    private String event;
    private TaskHelper calendioliDB;
    private String oldTitle;
    private DialogEditBuilderFinish listener;

    public void setDate(String date){
        this.dateText = date;
    }

    public void setTitle(String title){
        this.title = title;
        this.oldTitle = title;
    }

    public void setEvent(String event){
        this.event = event;
    }
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit event");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_calendioli, null);
        calendioliDB = new TaskHelper(getActivity());
        etTitle = (EditText)view.findViewById(R.id.etTitle);
        etTitle.setText(this.title);
        etEvent = (EditText)view.findViewById(R.id.etEvent);
        etEvent.setText(this.event);
        tvDate = (TextView)view.findViewById(R.id.tvDate);
        tvDate.setText(this.dateText);
        Toast.makeText(getActivity(), "DATE: " + dateText, Toast.LENGTH_SHORT).show();
        Log.d("DB","0");
        builder.setView(view)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        event = etEvent.getText().toString();
                        title = etTitle.getText().toString();
                        SQLiteDatabase db = calendioliDB.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.CALENDIOLI_DATE, dateText);
                        values.put(Task.CALENDIOLI_TITLE, title);
                        values.put(Task.CALENDIOLI_EVENT, event);
                        Log.d("DB INSERT",values.toString());
                        db.update(Task.TABLE_CALENDIOLI, values, Task.CALENDIOLI_TITLE + " = '" + oldTitle + "'", null);
                        //db.insertWithOnConflict(Task.TABLE_CALENDIOLI, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        listener.onFinishCalendioliEditDialog();
                        db.close();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CalendioloDialogEditBuilder.this.getDialog().cancel();
            }
        });
        return  builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CalendioloDialogEditBuilder.DialogEditBuilderFinish) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ItemDialogListener");
        }
    }

    public interface DialogEditBuilderFinish {
        void onFinishCalendioliEditDialog();
    }
}