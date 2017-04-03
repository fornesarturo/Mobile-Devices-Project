package watsalacanoa.todolisttest.caldroidStuff;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;

public class CalendioliPendings extends AppCompatActivity {

    TextView noEvent, startDate, lastDate;
    ListView eventList;
    TaskHelper dbh;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendioli_pendings);

        eventList = (ListView) findViewById(R.id.lvCalendioli_Pendings);

        startDate = (TextView) findViewById(R.id.calendioli_startdate);
        lastDate = (TextView) findViewById(R.id.calendioli_enddate);

        noEvent = (TextView) findViewById(R.id.calendioli_pendings_no_events);
        noEvent.setText("No events"); //Cambiar a "" y luego a No events si el query no regresa nada (moveToFirst() es falso)

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String myDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date mDate = null;
        try {
            mDate = (Date) sdf.parse(myDate);
        } catch (ParseException pepe){
        }
        Date mDateP7 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);
        cal.add(Calendar.DATE, 7);

        String endDate = sdf.format(cal.getTime());
        Log.d("TAIM", endDate);
        cal = null;
        JSONArray data = new JSONArray();
        dbh = new TaskHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        //Query BETWEEN date (parsear a DATE format) AND endDate (parsear a DATE format)

        db.close();

        startDate.setText(myDate);
        lastDate.setText(endDate);
    }
}
