package watsalacanoa.todolisttest.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.objects.Nota;

/**
 * Created by forne on 30/03/2017.
 */

public class ImageAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Nota> data;

    public ImageAdapter(Activity a, ArrayList<Nota> d) {
        this.activity = a;
        this.data = d;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Nota getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.data.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = this.activity.getLayoutInflater().inflate(R.layout.notioli_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.notioli_title);
        ImageView image = (ImageView) convertView.findViewById(R.id.notioli_image);

        String imageString;
        Bitmap imageBitmap = null;

        Nota currentNote = this.data.get(position);

        if(currentNote.getImage() != null) {
            imageString = currentNote.getImage();
            imageBitmap = BitmapFactory.decodeFile(imageString);
        }

        title.setText(currentNote.getTitle());
        image.setImageBitmap(imageBitmap);

        return convertView;
    }
}
