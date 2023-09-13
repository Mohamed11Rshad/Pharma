package com.mo.medreminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    SharedPreferences sharedpreferences;
    ALARMDB db;
    Intent generaIntent;

    public static final String CHAN_ID = "chanx";

    @Override
    public void onReceive(Context context, Intent intent)
    {

        db = new ALARMDB(context);

        String brandName = intent.getStringExtra("brand name2");
        String strength = intent.getStringExtra("brand strength2");
        String dose = intent.getStringExtra("brand dose2");
        int reqCode = intent.getIntExtra("brand req2" , 0);
        int repHours = intent.getIntExtra("rep hours" , 0);
        long time2 = intent.getLongExtra("time" , 0);

        sharedpreferences = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        int newReq = sharedpreferences.getInt("key" , 1);
        String hours = String.valueOf(repHours);

        db.deleteAlarm(reqCode);

        if(repHours!=0){

            long time = time2 + repHours * 1000L * 60 * 60;

            generaIntent = new Intent(context, AlarmReceiver.class);
            generaIntent.putExtra("brand name2", brandName);
            generaIntent.putExtra("brand strength2", strength);
            generaIntent.putExtra("brand dose2", dose);
            generaIntent.putExtra("brand req2", newReq);
            generaIntent.putExtra("context", AlarmActivity.class);
            generaIntent.putExtra("rep hours", repHours);
            generaIntent.putExtra("time" , time);

            String timeString = "Every " + hours + " hours";

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, newReq, generaIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            Alarm alarm = new Alarm(brandName, dose, timeString, newReq);
            changeReqCode(newReq);
            db.insertAlarm(alarm);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.canScheduleExactAlarms();
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time , pendingIntent);

            db.close();

        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {

            NotificationChannel channel = new NotificationChannel(CHAN_ID , "CHANNEL DISPLAY" , NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channel description");
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);

        }

        Intent intent2 = new Intent(context , RingActivity.class);
        intent2.putExtra("brand_name" , brandName);
        intent2.putExtra("strength" , strength);
        intent2.putExtra("dose" , dose);
        intent2.putExtra("req" , reqCode);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pi = PendingIntent.getActivity(context , 0 , intent2 , PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context , CHAN_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_access_alarm_24).setContentTitle(brandName).setContentText("Medicine Time").setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pi).setStyle(new NotificationCompat.BigTextStyle().bigText("its time to take " +brandName )).addAction(R.drawable.ic_launcher_foreground , "" , pi).setAutoCancel(true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(10 , builder.build());

        // Play the ringtone
        Intent serviceIntent = new Intent(context, MyService.class);
        context.startService(serviceIntent);
    }

    private void changeReqCode(int req_code){
        req_code += 1;
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("key", req_code);
        editor.apply();
    }


}
