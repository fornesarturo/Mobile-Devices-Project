package watsalacanoa.todolisttest.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Button delete = (Button) convertView.findViewById(R.id.notioli_delete);

        Nota currentNote = this.data.get(position);

        title.setText(currentNote.getTitle());

        delete.setFocusable(false);
        delete.setFocusableInTouchMode(false);

        return convertView;
    }
}
