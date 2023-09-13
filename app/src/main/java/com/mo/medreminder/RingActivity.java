package com.mo.medreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RingActivity extends AppCompatActivity {

    TextView brandNameRing , strengthRing , doseRing ;
    ImageButton okRing ;
    ALARMDB alarmdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        alarmdb = new ALARMDB(this);

        brandNameRing = findViewById(R.id.brand_name_ring);
        strengthRing = findViewById(R.id.strength_ring);
        doseRing = findViewById(R.id.dose_ring);
        okRing = findViewById(R.id.ok_ring);

        Intent intent = getIntent();
        String brand2 = intent.getStringExtra("brand_name");
        String strength2 = intent.getStringExtra("strength");
        String dose2 = intent.getStringExtra("dose");
        int req2 = intent.getIntExtra("req" , 0);
        boolean rep = intent.getBooleanExtra("rep" , false);


        brandNameRing.setText(brand2);
        strengthRing.setText(strength2);
        doseRing.setText(dose2);

        okRing.setOnClickListener(v -> {
            if(!rep)
                alarmdb.deleteAlarm(req2);
                Intent stopIntent = new Intent(RingActivity.this, MyService.class);
                stopService(stopIntent);
                Intent intent1 = new Intent(getBaseContext() , MainActivity.class);
                startActivity(intent1);
                finish();
        });


    }
}