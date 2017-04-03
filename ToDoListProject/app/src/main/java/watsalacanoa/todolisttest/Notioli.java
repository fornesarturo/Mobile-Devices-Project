package watsalacanoa.todolisttest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import watsalacanoa.todolisttest.adapters.ImageAdapter;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;
import watsalacanoa.todolisttest.objects.Nota;

public class Notioli extends AppCompatActivity {

    // Dialog Builder
    private EditText mNewItemText;
    private String pastNotioliText;
    private View viewForCurrentImage;
    private String lastImageURI = "";

    private TaskHelper mNotioliHelper;
    private ListView mListView;
    private FloatingActionButton addNotioliButton;
    private ImageAdapter mAdapter;

    //Image
    private static final int SAVE_PICTURE = 1;
    private final static int STORAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notioli);

        mNotioliHelper = new TaskHelper(this);
        mListView = (ListView) findViewById(R.id.notioli_listView);
        addNotioliButton = (FloatingActionButton) findViewById(R.id.fab_add_notioli);
        addNotioliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
        updateUI();


    }

    private void addNote() {
        final EditText notioliEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Notioli")
                .setMessage("Add a new Notioli")
                .setView(notioliEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = notioliEditText.getText().toString();
                        SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.NOTIOLI_COLUMN_TITLE,task);
                        db.insertWithOnConflict(Task.TABLE_NOTIOLI, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();
        dialog.show();
    }

    private void updateUI(){
        ArrayList<Nota> notioliList = new ArrayList<>();
        SQLiteDatabase db = mNotioliHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TABLE_NOTIOLI, null,
                null, null, null, null, null);
        while(cursor.moveToNext()){
            int indexTitle = cursor.getColumnIndex(Task.NOTIOLI_COLUMN_TITLE);
            int indexImage = cursor.getColumnIndex(Task.NOTIOLI_COLUMN_IMAGE);
            int indexID = cursor.getColumnIndex(Task.NOTIOLI_COLUMN_ID);
            notioliList.add(new Nota(cursor.getString(indexTitle), cursor.getString(indexImage), cursor.getInt(indexID)));
        }

        mAdapter = new ImageAdapter(this, notioliList);
        mListView.setAdapter(mAdapter);

        cursor.close();
        db.close();
    }

    public void deleteNotioli(View v){
        View parent = (View) v.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.notioli_title);
        String notioli = taskTextView.getText().toString();
        SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
        db.delete(Task.TABLE_NOTIOLI, Task.NOTIOLI_COLUMN_TITLE + " = ? ", new String[] {notioli});
        db.close();
        updateUI();
    }

    public AlertDialog.Builder getDialog() {
        final SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
        LayoutInflater li = LayoutInflater.from(this);
        LinearLayout newNoteBaseLayout = (LinearLayout) li.inflate(R.layout.new_item_dialog, null);
        mNewItemText = (EditText) newNoteBaseLayout.getChildAt(0);
        mNewItemText.setText(pastNotioliText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues values = new ContentValues();
                values.put(Task.NOTIOLI_COLUMN_TITLE, mNewItemText.getText().toString());
                db.update(Task.TABLE_NOTIOLI, values, Task.NOTIOLI_COLUMN_TITLE + " = '" + pastNotioliText + "'" , null);
                updateUI();
                closeKeyboard();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeKeyboard();
            }
        }).setTitle("Edit");
        builder.setView(newNoteBaseLayout);
        return builder;
    }
    public void editNotioli(View v) {
        View parent = (View) v.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.notioli_title);
        String notioli = taskTextView.getText().toString();
        pastNotioliText = notioli;
        AlertDialog.Builder builder = getDialog();
        AlertDialog alertToShow = builder.create();
        alertToShow.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertToShow.show();
    }

    public void goHome(View v){
        Intent i = new Intent();
        i.putExtra("message","You visited Notioli");
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    private void closeKeyboard() {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNewItemText.getWindowToken(), 0);
    }
    //////////////////////
    //Image

    public void savePicture(View v){
        viewForCurrentImage = (View) v.getParent();
        if(Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION);
        }
        else{
            savePicturePermitted();
        }
    }

    public void savePicturePermitted(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager()) != null){
            File photo = null;
            try{
                String time = new SimpleDateFormat("yyyMMdd-HHmmss").format(new Date());
                String name  = "IMAGE_"+time;
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photo = File.createTempFile(name, ".jpg", directory);
                lastImageURI = photo.getAbsolutePath();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
            if(photo != null){
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                startActivityForResult(i, SAVE_PICTURE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            savePicturePermitted();
        }
    }

    public void saveImageDB(){
        TextView title = (TextView) viewForCurrentImage.findViewById(R.id.notioli_title);
        String titleForPhoto = title.getText().toString();
        SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task.NOTIOLI_COLUMN_IMAGE, lastImageURI);
        String clause = Task.NOTIOLI_COLUMN_TITLE + " = ?";
        String[] values = {titleForPhoto};
        db.update(Task.TABLE_NOTIOLI, contentValues, clause, values);
        db.close();
    }

    @Override
    public void onActivityResult(int req, int res, Intent data){
        if(res == Activity.RESULT_OK){
            switch(req){
                case SAVE_PICTURE:
                    saveImageDB();
                    updateUI();
                    break;
            }
        }
    }
}