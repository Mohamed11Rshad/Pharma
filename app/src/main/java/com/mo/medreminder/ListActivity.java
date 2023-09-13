package com.mo.medreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ALARMDB db;
    Intent generalIntent;
    RecyclerView rv ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        generalIntent = new Intent(this , AlarmReceiver.class);
        db = new ALARMDB(this);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        rv = findViewById(R.id.list_rv);

        ArrayList<Alarm> alarms = db.getAllALarms();
        AlarmListAdapter adapter = new AlarmListAdapter(alarmManager , generalIntent , this , alarms);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }
}