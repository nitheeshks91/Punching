package com.inout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private String currentDate;
    TextView header;
    Button button_in;
    Button button_out;
    Button showButton;
    EditText edit_hour;
    EditText edit_minute;
    CoordinatorLayout coordinatorLayout;
    private Button showPunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();

        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = curFormater.format(new Date());
        header.setText(this.currentDate);


    }

    private void initListeners() {
        /**
         * Listener to handle the punch IN
         */
        button_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = new Time(System.currentTimeMillis()).getHours();
                int minutes = new Time(System.currentTimeMillis()).getMinutes();
                edit_hour.setText(String.valueOf(hours));
                edit_minute.setText(String.valueOf(minutes));
                long inTime = TimeUnit.MINUTES.toMillis((hours * 60) + minutes);

                if (dbHelper.getAllRecord().getCount() == 0) {
                    dbHelper.insertData(currentDate, String.valueOf(inTime), "");
                    Snackbar.make(coordinatorLayout, "IN SUCCESSFULLY", Snackbar.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = dbHelper.getLastRowId();
                    cursor.moveToFirst();
                    int max = cursor.getInt(cursor.getColumnIndex("max"));
                    Cursor outCursor = dbHelper.getLastRowOutTime(max);
                    outCursor.moveToFirst();
                    String currentouttime = outCursor.getString(outCursor.getColumnIndex("currentouttime"));
                    if (currentouttime == null) {
                        Snackbar.make(coordinatorLayout, "YOU ALREADY IN .PLEASE MAKE OUT", Snackbar.LENGTH_SHORT).show();
                    } else {
                        dbHelper.insertData(currentDate, String.valueOf(inTime), "");
                        Snackbar.make(coordinatorLayout, "IN SUCCESSFULLY", Snackbar.LENGTH_SHORT).show();
                    }
                }


            }
        });

        /**
         * Listener to handle the punch OUT
         */
        button_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = new Time(System.currentTimeMillis()).getHours();
                int minutes = new Time(System.currentTimeMillis()).getMinutes();
                edit_hour.setText(String.valueOf(hours));
                edit_minute.setText(String.valueOf(minutes));
                long inTime = TimeUnit.MINUTES.toMillis((hours * 60) + minutes);
                if (dbHelper.getAllRecord().getCount() > 0) {
                    Cursor cursor = dbHelper.getLastRowId();
                    cursor.moveToFirst();
                    int max = cursor.getInt(cursor.getColumnIndex("max"));
                    Cursor outCursor = dbHelper.getLastRowOutTime(max);
                    outCursor.moveToFirst();
                    String currentouttime = outCursor.getString(outCursor.getColumnIndex("currentouttime"));
                    if (currentouttime == null) {
                        dbHelper.updateContact(max, String.valueOf(inTime));
                        Snackbar.make(coordinatorLayout, "OUT SUCCESSFULLY", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(coordinatorLayout, "PLEASE MAKE AN IN", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(coordinatorLayout, "PLEASE MAKE AN IN", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(coordinatorLayout, "YOUR TOTAL IN TIME : " + getTime(getTotalInTime()), Snackbar.LENGTH_SHORT).show();
            }
        });

        showPunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Punch> totalPunch = getTotalPunch();
                Bundle bundle = new Bundle();
                bundle.putSerializable("BUNDLE", totalPunch);
                bundle.putString("TOTAL", getTime(getTotalInTime()));
                Intent mIntent = new Intent(MainActivity.this, PunchDetails.class);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            }
        });
    }

    private void initView() {
        header = (TextView) findViewById(R.id.header);
        button_in = (Button) findViewById(R.id.btn_in);
        button_out = (Button) findViewById(R.id.btn_out);
        showButton = (Button) findViewById(R.id.showTime);
        showPunch = (Button) findViewById(R.id.showPunch);
        edit_hour = (EditText) findViewById(R.id.edit_hour);
        edit_minute = (EditText) findViewById(R.id.edit_minute);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
    }

    /**
     * Calculating the total IN time of the currenct date
     *
     * @return
     */
    private long getTotalInTime() {
        long totalInTime = 0;
        Cursor cursor = dbHelper.getAllRecord();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                long currentintime = cursor.getLong(cursor.getColumnIndex("currentintime"));
                String currentouttime = cursor.getString(cursor.getColumnIndex("currentouttime"));
                if (currentouttime == null) {
                    break;
                }
                long diff = Long.parseLong(currentouttime) - currentintime;
                long l = TimeUnit.MILLISECONDS.toMinutes(diff);
                totalInTime = totalInTime + l;
            } while (cursor.moveToNext());
        }
        return totalInTime;
    }


    private ArrayList<Punch> getTotalPunch() {
        ArrayList<Punch> punches = new ArrayList<>();
        int i = 1;
        Cursor cursor = dbHelper.getAllRecord();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Punch punch = new Punch();
                punch.setNo(String.valueOf(i));
                long currentintime = cursor.getLong(cursor.getColumnIndex("currentintime"));
                String currentouttime = cursor.getString(cursor.getColumnIndex("currentouttime"));
                String currentdate = cursor.getString(cursor.getColumnIndex("currentdate"));
                punch.setDate(currentdate);
                punch.setInTime(getTime(TimeUnit.MILLISECONDS.toMinutes(currentintime)));
                if (currentouttime != null) {
                    punch.setOutTime(getTime(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(currentouttime))));
                    long diff = Long.parseLong(currentouttime) - currentintime;
                    long l = TimeUnit.MILLISECONDS.toMinutes(diff);
                    punch.setDiffTime(String.valueOf(l));
                    punches.add(punch);
                }

                i++;
            }
            while (cursor.moveToNext());
        }
        return punches;
    }


    private String getTime(long time) {
        long l = time;
        String h = "00";
        String m = String.valueOf(l);
        if (l > 60) {
            h = String.valueOf(l / 60);
            if (h.length() == 1) {
                h = "0" + h;
            }
            m = String.valueOf(l % 60);
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        return h + ":" + m;
    }
}
