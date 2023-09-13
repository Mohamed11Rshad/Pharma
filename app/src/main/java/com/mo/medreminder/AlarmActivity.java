package com.mo.medreminder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AlarmActivity extends AppCompatActivity {
    Intent generaIntent;
    ALARMDB db;
    TimePicker alarmTimePicker;
    Button add;
    CheckBox recurring ;
    EditText repeatingHours;
    TextView brandNameTv , strengthTv , doseTv ;
    private int req_code ;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myprefs";
    public static final  String value = "key";
    String brandName;
    String strength;
    String dose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        db = new ALARMDB(this);
        alarmTimePicker = findViewById(R.id.timePicker);
        add = findViewById(R.id.add);
        brandNameTv = findViewById(R.id.brand_name_tv_al);
        strengthTv = findViewById(R.id.brand_strength_tv_al);
        doseTv = findViewById(R.id.brand_dose_tv_al);
        recurring = findViewById(R.id.createalarm_recurring);
        repeatingHours = findViewById(R.id.hours_repeating);


        Intent intent  = getIntent();
        brandName = intent.getStringExtra("brand_name");
        strength = intent.getStringExtra("strength");
        dose = intent.getStringExtra("dose");

        brandNameTv.setText(brandName);
        if(strength!=null){
        strengthTv.setText(strength);}
        if(dose!=null){
        doseTv.setText(dose);}


        add.setOnClickListener(this::onClick);

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    repeatingHours.setVisibility(View.VISIBLE);
                else
                    repeatingHours.setVisibility(View.GONE);
            }
        });


    }


    private void changeReqCode(){
            req_code += 1;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(value, req_code);
            editor.apply();

    }


    private void onClick(View v) {

        if (recurring.isChecked()) {
            String hours = repeatingHours.getText().toString();
            if (hours.isEmpty()) {
                Toast.makeText(AlarmActivity.this, "please type a repetition hours", Toast.LENGTH_SHORT).show();
            } else {
                int intHours = Integer.parseInt(hours);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                req_code = sharedpreferences.getInt(value, 1);

                long interval = (long) intHours * 1000 * 60 * 60;
                long time = calendar.getTimeInMillis() + interval;

                generaIntent = new Intent(getBaseContext(), AlarmReceiver.class);
                generaIntent.putExtra("brand name2", brandName);
                generaIntent.putExtra("brand strength2", strength);
                generaIntent.putExtra("brand dose2", dose);
                generaIntent.putExtra("brand req2", req_code);
                generaIntent.putExtra("rep hours", intHours);
                generaIntent.putExtra("time", time);

                String timeString = "Every " + hours + " hours";

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), req_code, generaIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                Alarm alarm = new Alarm(brandName, dose, timeString, req_code);
                db.insertAlarm(alarm);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);

                Toast.makeText(getBaseContext(), "Medicine reminder set for " + timeString, Toast.LENGTH_SHORT).show();
                db.close();
                Intent intent2 = new Intent(getBaseContext(), ListActivity.class);
                intent2.putExtra("req_code", req_code);

                changeReqCode();
                startActivity(intent2);

            }
        } else {


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String timeString = dateFormat.format(date);

            req_code = sharedpreferences.getInt(value, 1);

            generaIntent = new Intent(getBaseContext(), AlarmReceiver.class);
            generaIntent.putExtra("brand name2", brandName);
            generaIntent.putExtra("brand strength2", strength);
            generaIntent.putExtra("brand dose2", dose);
            generaIntent.putExtra("brand req2", req_code);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), req_code, generaIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            Alarm alarm = new Alarm(brandName, dose, timeString, req_code);
            db.insertAlarm(alarm);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.canScheduleExactAlarms();
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(getBaseContext(), "Medicine reminder set for " + timeString, Toast.LENGTH_SHORT).show();
            db.close();
            Intent intent2 = new Intent(getBaseContext(), ListActivity.class);
            intent2.putExtra("req_code", req_code);

            changeReqCode();
            startActivity(intent2);

        }
    }
}