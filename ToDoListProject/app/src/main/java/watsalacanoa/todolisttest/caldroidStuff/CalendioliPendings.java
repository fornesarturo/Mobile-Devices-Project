package watsalacanoa.todolisttest.caldroidStuff;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.adapters.PendingAdapter;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;
import watsalacanoa.todolisttest.objects.Pending;

public class CalendioliPendings extends AppCompatActivity {

    private TextView noEvent, startDate;
    private ListView eventList;
    private TaskHelper dbh;
    private Spinner lastDateSpinner;
    private ArrayList<String> pendingRangesNames;
    private int[] pendingRangesDays;
    private Date myDate;
    private SimpleDateFormat dateFormat;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendioli_pendings);

        pendingRangesNames = new ArrayList<String>(Arrays.asList("Today", "Tomorrow", "2 days", "5 days", "1 week", "2 weeks"));
        pendingRangesDays = new int[]{0, 1, 2, 5, 7, 14};

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        eventList = (ListView) findViewById(R.id.lvCalendioli_Pendings);

        startDate = (TextView) findViewById(R.id.calendioli_startdate);
        Spinner lastDate = (Spinner) findViewById(R.id.caliendoli_enddate);
        lastDateSpinner = lastDate;

        noEvent = (TextView) findViewById(R.id.calendioli_pendings_no_events);
        noEvent.setText("No events"); //Cambiar a "" y luego a No events si el query no regresa nada (moveToFirst() es falso)

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pendingRangesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lastDateSpinner.setAdapter(adapter);
        Date myCurrrentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(myCurrrentDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        try {
            myDate = dateFormat.parse(dateFormat.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDate.setText(dateFormat.format(myDate));

        lastDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePendings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updatePendings() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.DATE, pendingRangesDays[lastDateSpinner.getSelectedItemPosition()]);
        String endDateString = dateFormat.format(cal.getTime());
        Date eDate = null;
        try {
            eDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("MYDATE", dateFormat.format(myDate));
        Log.d("ENDDATE", endDateString);

        dbh = new TaskHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        ArrayList<Pending> data = new ArrayList<>();

        Cursor cursor = db.query(Task.TABLE_CALENDIOLI, null, null, null, null, null, null);
        long prevTime = myDate.getTime();
        boolean isFirstResult = true;
        while(cursor.moveToNext()){
            Date eventDate = null;
            try {
                eventDate = dateFormat.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(eventDate.getTime() >= myDate.getTime() && eventDate.getTime() <= eDate.getTime()){
                Log.d("DATE", dateFormat.format(eventDate));
                data.add(new Pending(eventDate, cursor.getString(2), cursor.getString(3)));
            }
        }
        cursor.close();

        PendingAdapter adapter = new PendingAdapter(this, data);
        Log.d("SIZE", data.size()+"");
        eventList.setAdapter(adapter);

        //Query BETWEEN date (parsear a DATE format) AND endDate (parsear a DATE format)

        db.close();
    }
}
