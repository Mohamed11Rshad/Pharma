package com.mo.medreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder> {

    private ArrayList<Alarm> alarms;
    private ALARMDB db ;
    private Context context;
    private Intent intent;
    AlarmManager alarmManager;



    public AlarmListAdapter(AlarmManager alarmManager ,Intent intent ,Context context , ArrayList<Alarm> alarms) {
        this.alarms = alarms;
        this.context = context;
        this.intent = intent;
        this.alarmManager = alarmManager;
        db = new ALARMDB(context);

    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.alarm_list_item, parent, false);

        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        Alarm alarm = alarms.get(position);
        holder.textViewName.setText(alarm.getName());
        holder.textViewDose.setText(alarm.getDose());
        holder.textViewTime.setText(alarm.getTime());
        holder.cancelBtn.setTag(alarm.getReqCode());
        holder.cancelBtn.setOnClickListener(v -> {
            int alarmReq = (int)v.getTag();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmReq, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            int position1 = alarms.indexOf(alarm);
            alarms.remove(position1);
            notifyItemRemoved(position1);
            db.deleteAlarm(alarmReq);
        });
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewDose;
        public TextView textViewTime;
        public ImageButton cancelBtn ;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_med_name);
            textViewDose = itemView.findViewById(R.id.tv_med_dose_details);
            textViewTime = itemView.findViewById(R.id.tv_med_time);
            cancelBtn = itemView.findViewById(R.id.close_btn);
        }
    }



}


