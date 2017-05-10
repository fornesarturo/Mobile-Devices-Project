package watsalacanoa.todolisttest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import watsalacanoa.todolisttest.adapters.ImageAdapter;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;
import watsalacanoa.todolisttest.objects.Nota;

public class Notioli extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TaskHelper mNotioliHelper;
    private ListView mListView;
    private FloatingActionButton addNotioliButton;
    private ImageAdapter mAdapter;

    //Image
    private final static int DETAILED_NOTIOLI = 3;

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
        mListView.setOnItemClickListener(this);
        updateUI();
    }

    private void addNote() {
        final EditText notioliEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add notioli")
                .setMessage("Add a new notioli")
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
        mListView.setOnItemClickListener(this);
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

    public void goHome(View v){
        Intent i = new Intent();
        i.putExtra("message","You visited notioli");
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    //////////////////////
    //Image

    public void savePictureDB(String title, String path){
        SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task.NOTIOLI_COLUMN_IMAGE, path);
        String clause = Task.NOTIOLI_COLUMN_TITLE + " = ?";
        String[] values = {title};
        db.update(Task.TABLE_NOTIOLI, contentValues, clause, values);
        db.close();
    }

    @Override
    public void onActivityResult(int req, int res, Intent data){
        if(res == Activity.RESULT_OK){
            switch(req){
                case DETAILED_NOTIOLI:
                    String task = data.getStringExtra("text");
                    String old = data.getStringExtra("old");
                    String image = data.getStringExtra("image");
                    savePictureDB(old, image);
                    SQLiteDatabase db = mNotioliHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(Task.NOTIOLI_COLUMN_TITLE, task);
                    String clause = Task.NOTIOLI_COLUMN_TITLE + " = ?";
                    db.update(Task.TABLE_NOTIOLI, values, clause, new String[] {old});
                    db.close();
                    updateUI();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Nota nota = mAdapter.getItem(position);
        String text = nota.getTitle();
        String image_path = nota.getImage();
        Intent i = new Intent(this, DetailedNote.class);
        i.putExtra("text",text);
        i.putExtra("image",image_path);
        startActivityForResult(i, DETAILED_NOTIOLI);
    }
}