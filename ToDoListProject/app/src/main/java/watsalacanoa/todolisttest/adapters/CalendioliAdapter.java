package watsalacanoa.todolisttest.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import watsalacanoa.todolisttest.R;

/**
 * Created by forne on 31/03/2017.
 */

public class CalendioliAdapter extends BaseAdapter {

    Activity activity;
    JSONArray jsonArray;

    public CalendioliAdapter(Activity activity, JSONArray jsonArray){
        this.activity = activity;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        Object result = null;
        try{
            result = jsonArray.getJSONObject(position);
        }catch(JSONException jose){
            jose.printStackTrace();
        }
        return result;
    }

    @Override
    public long getItemId(int position) {
        long result = 0;
        try{
            result = jsonArray.getJSONObject(position).getLong("id");
        }catch(JSONException jose){
            jose.printStackTrace();
        }
        return result;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.calendioli_individual_event, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.calendioli_event_title);
        TextView description = (TextView) convertView.findViewById(R.id.calendioli_event_description);

        try{
            JSONObject objetitoDeJose = jsonArray.getJSONObject(position);
            title.setText(objetitoDeJose.getString("title"));
            description.setText(objetitoDeJose.getString("description"));
        }catch(JSONException jose){
            jose.printStackTrace();
        }
        return convertView;
    }
}
