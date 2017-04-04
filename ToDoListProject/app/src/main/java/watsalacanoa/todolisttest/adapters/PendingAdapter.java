package watsalacanoa.todolisttest.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.objects.Pending;

/**
 * Created by MiguelAngel on 03/04/2017.
 */

public class PendingAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Pending> data;

    public PendingAdapter(Activity activity, ArrayList<Pending> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Pending getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Pending currentPending = this.data.get(position);
        if(convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.pending_row, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.pending_date);
        TextView title = (TextView) convertView.findViewById(R.id.pending_title);
        TextView desc = (TextView) convertView.findViewById(R.id.pending_desc);
        date.setText(dateFormat.format(currentPending.getDate()));
        title.setText(currentPending.getTitle());
        desc.setText(currentPending.getEvent());

        return convertView;
    }
}
