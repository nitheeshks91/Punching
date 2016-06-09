package com.inout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 08468 on 6/9/2016.
 */
public class PunchDetails extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.punch_details_view);
        TextView total = (TextView) findViewById(R.id.total);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ArrayList<Punch> punches = (ArrayList<Punch>) extras.getSerializable("BUNDLE");
        String _total = (String) extras.getSerializable("TOTAL");
        total.setText(_total);
        PunchDetailsAdapter detailsAdapter = new PunchDetailsAdapter(this, 0, punches);
        listView = (ListView) findViewById(R.id.list_details);
        listView.setAdapter(detailsAdapter);


    }
}
