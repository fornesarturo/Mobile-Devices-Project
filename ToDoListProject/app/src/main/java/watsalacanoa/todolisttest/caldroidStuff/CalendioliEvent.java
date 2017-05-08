package watsalacanoa.todolisttest.caldroidStuff;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import watsalacanoa.todolisttest.R;
import watsalacanoa.todolisttest.adapters.CalendioliAdapter;
import watsalacanoa.todolisttest.db.Task;
import watsalacanoa.todolisttest.db.TaskHelper;

public class CalendioliEvent extends AppCompatActivity implements CalendioloDialogEditBuilder.DialogEditBuilderFinish{
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
        Intent i = getIntent();
        date = i.getStringExtra("date");
        updateUI();
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
    }

    public void editCalendioli(View v){
        //dbh = new TaskHelper(this);
        //SQLiteDatabase db = dbh.getWritableDatabase();
       // View parent = (View) v.getParent();
        CalendioloDialogEditBuilder cdb = new CalendioloDialogEditBuilder();
        View parent = (View) v.getParent();
        TextView titleTV = (TextView) parent.findViewById(R.id.calendioli_event_title);
        String titleT = titleTV.getText().toString();
        TextView eventTV = (TextView) parent.findViewById(R.id.calendioli_event_description);
        String eventT = eventTV.getText().toString();
        cdb.setDate(date);
        cdb.setTitle(titleT);
        cdb.setEvent(eventT);
        cdb.show(getFragmentManager(), "hh");
        //if(date!=null){
        //    updateUI();
        //}
    }
    public void deleteCalendioli(View v) {
        //Toast.makeText(this, "SUP", Toast.LENGTH_SHORT).show();
        dbh = new TaskHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();
        View parent = (View) v.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.calendioli_event_title);
        String calendioli = taskTextView.getText().toString();
        db.delete(Task.TABLE_CALENDIOLI, Task.CALENDIOLI_TITLE+ " = ? ", new String[] {calendioli});
        //db.delete(Task.TABLE_CALENDIOLI, Task.CALENDIOLI_DATE + " = ? ", new String[] {notioli});
        db.close();
        if(date!=null) {
            updateUI();
        }
    }

    private void updateUI(){
        setContentView(R.layout.activity_calendioli_event);
        eventList = (ListView) findViewById(R.id.calendioli_list_view);

        noEvent = (TextView) findViewById(R.id.calendioli_no_event);
        noEvent.setText("");


        if(date != null) {
            JSONArray data = new JSONArray();
            dbh = new TaskHelper(this);
            SQLiteDatabase db = dbh.getReadableDatabase();
            String clause = Task.CALENDIOLI_DATE + " = ?";
            String[] values = {date};
            Cursor c = db.query(Task.TABLE_CALENDIOLI, null, clause, values, null, null, null);
            if (c.moveToFirst()) {
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
                } catch (JSONException jose) {
                    jose.printStackTrace();
                }
                //While there are other events
                while (c.moveToNext()) {
                    title = c.getString(titleIndex);
                    description = c.getString(eventIndex);
                    id = c.getString(idIndex);

                    event = new JSONObject();
                    try {
                        event.put(TITLE, title);
                        event.put(DESCRIPTION, description);
                        event.put(ID, id);
                        data.put(event);
                    } catch (JSONException jose) {
                        jose.printStackTrace();
                    }
                }
                eventList.setAdapter(new CalendioliAdapter(this, data));

            } else {
                noEvent.setText("No events");
            }
        }
    }

    @Override
    public void onFinishCalendioliEditDialog() {
        this.updateUI();
    }

        /*
        ArrayList<Pending> notioliList = new ArrayList<>();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.query(Task.TABLE_NOTIOLI, null,
                null, null, null, null, null);
        while(cursor.moveToNext()){
            int date = cursor.getColumnIndex(Task.CALENDIOLI_DATE);
            int title = cursor.getColumnIndex(Task.CALENDIOLI_TITLE);
            int event = cursor.getColumnIndex(Task.CALENDIOLI_EVENT);
            //notioliList.add(new Pending(cursor.getString(date), cursor.getString(title), cursor.getInt(event)));
        }

        //mAdapter = new ImageAdapter(this, notioliList);
        //mListView.setAdapter(mAdapter);

        cursor.close();
        db.close();

        */


}
