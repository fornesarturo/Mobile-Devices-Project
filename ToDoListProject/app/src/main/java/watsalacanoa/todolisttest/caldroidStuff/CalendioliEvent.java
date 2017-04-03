package watsalacanoa.todolisttest.caldroidStuff;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.adapters.CalendioliAdapter;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;

public class CalendioliEvent extends AppCompatActivity {
    TextView noEvent;
    ListView eventList;
    String date;
    TaskHelper dbh;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendioli_event);

        eventList = (ListView) findViewById(R.id.calendioli_list_view);

        noEvent = (TextView) findViewById(R.id.calendioli_no_event);
        noEvent.setText("");

        Intent i = getIntent();
        date = i.getStringExtra("date");
        if(date != null) {
            ////////////////
            /*
            TaskHelper dbh2 = new TaskHelper(this);
            SQLiteDatabase dbTesting = dbh2.getReadableDatabase();
            String clauseTest = Task.CALENDIOLI_DATE + " = ?";
            String myDate = date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date mDate = null;
            try {
                mDate = (Date) sdf.parse(myDate);
            } catch (ParseException pepe) {
            }
            Date mDateP7 = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(mDate);
            cal.add(Calendar.DATE, 7);

            String output = sdf.format(cal.getTime());
            Log.d("TAIM", output);
            String currTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Log.d("CURRENT DATE = ", currTime);
            cal = null;


            //Cursor c2 = dbTesting.query(Task.TABLE_CALENDIOLI, null, mDate + " BETWEEN " + mDate + " AND " + mDateP7, null, null, null, null);

            */
            //////////////////
            JSONArray data = new JSONArray();
            dbh = new TaskHelper(this);
            SQLiteDatabase db = dbh.getReadableDatabase();
            String clause = Task.CALENDIOLI_DATE + " = ?";
            String[] values = {date};
            Cursor c = db.query(Task.TABLE_CALENDIOLI, null, clause, values, null, null, null);
            if(c.moveToFirst()){
                int titleIndex = c.getColumnIndex(Task.CALENDIOLI_TITLE);
                int eventIndex = c.getColumnIndex(Task.CALENDIOLI_EVENT);
                int idIndex = c.getColumnIndex(Task.CALENDIOLI_ID);
                String title = "", description = "", id = "";

                //First event
                title = c.getString(titleIndex);
                description = c.getString(eventIndex);
                id = c.getString(idIndex);

                JSONObject event = new JSONObject();
                try {
                    event.put(TITLE, title);
                    event.put(DESCRIPTION, description);
                    event.put(ID, id);
                    data.put(event);
                }catch (JSONException jose){
                    jose.printStackTrace();
                }

                //While there are other events
                while(c.moveToNext()){
                    title = c.getString(titleIndex);
                    description = c.getString(eventIndex);
                    id = c.getString(idIndex);

                    event = new JSONObject();
                    try {
                        event.put(TITLE, title);
                        event.put(DESCRIPTION, description);
                        event.put(ID, id);
                        data.put(event);
                    }catch (JSONException jose){
                        jose.printStackTrace();
                    }
                }
                eventList.setAdapter(new CalendioliAdapter(this, data));

            }
            else{
                noEvent.setText("No events");
            }
        }
    }


}
