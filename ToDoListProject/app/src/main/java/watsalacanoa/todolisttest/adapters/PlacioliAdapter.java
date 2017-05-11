package watsalacanoa.todolisttest.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.objects.Placioli;

public class PlacioliAdapter extends BaseAdapter {

    private ArrayList<Placioli> places;
    private Activity activity;

    public PlacioliAdapter() {}
    public PlacioliAdapter(ArrayList<Placioli> places, Activity activity){
        this.places = places;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Placioli getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // create if doesn't exist
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.row_placioli_test, null);
        }

        // populate with data
        TextView nameText = (TextView)convertView.findViewById(R.id.placesTitle);
        TextView gradeText = (TextView)convertView.findViewById(R.id.placesDescription);

        Placioli current = places.get(position);
        nameText.setText(current.getTitle());
        gradeText.setText(current.getDescription() + "");

        return convertView;
    }
}
