package watsalacanoa.todolisttest;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.objects.Nota;

public class DetailedNote extends AppCompatActivity {

    EditText text;
    ImageView image;
    String oldNote;
    String lastImageURI;
    private final static int SAVE_PICTURE = 1;
    private final static int STORAGE_PERMISSION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_note);
        text = (EditText) findViewById(R.id.item_title);
        image = (ImageView) findViewById(R.id.item_image);

        Intent i = getIntent();
        String textOld = oldNote = i.getStringExtra("text");
        String imagePath = i.getStringExtra("image");
        text.setText(textOld);

        String imageString;
        Bitmap imageBitmap = null;

        if(imagePath != null) {
            imageString = imagePath;
            try {
                imageBitmap = BitmapFactory.decodeFile(imageString);
            }
            catch(OutOfMemoryError error) {
                error.printStackTrace();
                Toast.makeText(this, "NO MORE MEMORY", Toast.LENGTH_SHORT).show();
            }
        }
        image.setImageBitmap(imageBitmap);
    }

    public void goBack(View v){
        Intent i = new Intent();
        i.putExtra("text",text.getText().toString());
        i.putExtra("old",oldNote);
        i.putExtra("image",lastImageURI);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public void onActivityResult(int req, int res, Intent data){
        if(res == Activity.RESULT_OK){
            switch(req){
                case SAVE_PICTURE:
                    Bitmap image2 = BitmapFactory.decodeFile(lastImageURI);
                    image.setImageBitmap(image2);
                    break;
            }
        }
    }

    public void takePicture(View v){
        if(Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION);
        }
        else{
            takePicturePermitted();
        }
    }

    public void takePicturePermitted(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager()) != null){
            File photo = null;
            try{
                String time = new SimpleDateFormat("yyyMMdd-HHmmss").format(new Date());
                String name  = "IMAGE_"+time;
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photo = File.createTempFile(name, ".jpg", directory);
                Log.d("PHOTO!", photo.getAbsolutePath());
                lastImageURI = photo.getAbsolutePath();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
            if(photo != null){
                Log.d("PHOTO?", "Taking photo");
                i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photo));
                Log.d("PHOTO?", "Putting extra");
                startActivityForResult(i, SAVE_PICTURE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePicturePermitted();
        }
    }
}
