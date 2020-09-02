package com.chang.project_yak_ver2.Lunch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chang.project_yak_ver2.Model.DrugModel;
import com.chang.project_yak_ver2.R;
import com.chang.project_yak_ver2.RecyclerView.postAdapter;
import com.chang.project_yak_ver2.mainScreen;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Lunch extends AppCompatActivity implements Serializable {
    public static final String DailyAlarm = "lDailyAlarm";
    public static final String NextNotifyTime = "lNextNotifyTime";
    public static final String sharderPending = "lPending";
    private AlarmManager alarmManager = null;
    private PendingIntent pendingIntent = null;
    boolean IsAlarmExist;
    private int hour, minute;
    private ArrayList<DrugModel> drugList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunch);

        Intent StartIntent = getIntent();
        drugList = (ArrayList<DrugModel>) StartIntent.getSerializableExtra("array");

        RecyclerView rvList = findViewById(R.id.rv_lunch);

        postAdapter adapter = new postAdapter(this, drugList,2);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(adapter);

        SharedPreferences IsAlarmExistPrefer = getPreferences(Context.MODE_PRIVATE);
        IsAlarmExist = IsAlarmExistPrefer.getBoolean(sharderPending,false);


        final TimePicker Picker = findViewById(R.id.tp_lunch);

        SharedPreferences sharedPref = getSharedPreferences(DailyAlarm, MODE_PRIVATE);

        Calendar defaltLunchTime = Calendar.getInstance();
        defaltLunchTime.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 13, 0);

        long millis = sharedPref.getLong(
                NextNotifyTime, defaltLunchTime.getTimeInMillis());

        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(millis);

        Date currentTime = nextNotifyTime.getTime();
        SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());
        final int pre_hour = Integer.parseInt(HourFormat.format(currentTime));
        final int pre_minute = Integer.parseInt(MinuteFormat.format(currentTime));

        if (Build.VERSION.SDK_INT >= 23) {
            Picker.setHour(pre_hour);
            Picker.setMinute(pre_minute);
        } //Build.VERSION.SDK_INT를 통해서 기기의 SDK Version을 가져올 수 있습니다.
        else {
            Picker.setCurrentHour(pre_hour);
            Picker.setCurrentMinute(pre_minute);
        }

        Button OnButton = (Button) findViewById(R.id.btn_lunchOn);
        OnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences IsAlarmExistPrefer = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor IsAlarmExistPreferEditor = IsAlarmExistPrefer.edit();
                IsAlarmExistPreferEditor.putBoolean(sharderPending, true);
                IsAlarmExistPreferEditor.commit();
                IsAlarmExist=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = Picker.getHour();
                    minute = Picker.getMinute();
                } else{
                    hour = Picker.getCurrentHour();
                    minute = Picker.getCurrentMinute();
                }



                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }

                Date currentDateTime = calendar.getTime();
                String date_text = new SimpleDateFormat
                        ("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ",
                                Locale.getDefault()).format(currentDateTime);
                Toast.makeText(getApplicationContext(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPref = getSharedPreferences(DailyAlarm, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(NextNotifyTime, (long) calendar.getTimeInMillis());
                editor.commit();

                diaryNotification(calendar);


            }
        });

        Button OffButton = (Button) findViewById(R.id.btn_lunchOff);
        OffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalcelAlarm();
            }
        });


    }


    private void diaryNotification(Calendar calendar) {
        ComponentName receiver = new ComponentName(this, LunchBoot.class);
        PackageManager pm = this.getPackageManager();
        Intent alarmIntent = new Intent(this, LunchReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 2348, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //버전에 따른
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    private void CalcelAlarm() {
        if(IsAlarmExist){
            SharedPreferences IsAlarmExistPrefer = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor IsAlarmExistPreferEditor = IsAlarmExistPrefer.edit();
            IsAlarmExistPreferEditor.putBoolean(sharderPending, false);
            IsAlarmExistPreferEditor.commit();
            IsAlarmExist=false;
            ComponentName receiver = new ComponentName(this, LunchBoot.class);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PackageManager pm = this.getPackageManager();
            Intent alarmIntent = new Intent(this, LunchReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 2348, alarmIntent, 0);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            pendingIntent = null;
            alarmManager = null;
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            Toast.makeText(getApplicationContext(), "점심 알람이 취소되었습니다!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "설정된 알람이 없습니다...", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((mainScreen)mainScreen.mContext).setTime();
    }
}