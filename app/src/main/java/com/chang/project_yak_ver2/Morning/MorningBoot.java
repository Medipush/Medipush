package com.chang.project_yak_ver2.Morning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MorningBoot  extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            // on device boot complete, reset the alarm
            Intent alarmIntent = new Intent(context, MorningReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2346, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


            SharedPreferences sharedPreferences = context.getSharedPreferences("mDailyAlarm", MODE_PRIVATE);
            long millis = sharedPreferences.getLong("mNextNotifyTime", Calendar.getInstance().getTimeInMillis());


            Calendar current_calendar = Calendar.getInstance();
            Calendar nextNotifyTime = new GregorianCalendar();
            nextNotifyTime.setTimeInMillis(sharedPreferences.getLong("mNextNotifyTime", millis));

            if (current_calendar.after(nextNotifyTime)) {
                nextNotifyTime.add(Calendar.DATE, 1);
            }

            Date currentDateTime = nextNotifyTime.getTime();
            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
            Toast.makeText(context.getApplicationContext(), "[재부팅후] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();


            if (manager != null) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                /*if (Build.VERSION.SDK_INT >= 23)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(), pendingIntent);
                else {
                    if (Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(), pendingIntent);

                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(), pendingIntent);
                    }*/

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //버전에 따른
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(), pendingIntent);
                    }

            }
        }
    }
}