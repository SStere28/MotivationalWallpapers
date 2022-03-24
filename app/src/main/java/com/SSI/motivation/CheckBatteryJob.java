package com.SSI.motivation;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;;
import java.util.Calendar;



public class CheckBatteryJob extends BroadcastReceiver {

    public static final String PREFS_FILE = "Mt";

    int level;

    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        batteryLevel(context);

    }// end onReceive

    private void RegisterSlideShow(Context context) {
        Intent intent2 = new Intent(context, CheckBatteryJob.class);
        PendingIntent sender2 = PendingIntent.getBroadcast(context, 0, intent2, 0);
        AlarmManager am2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am2.cancel(sender2);

        SharedPreferences settings = context.getSharedPreferences(
                PREFS_FILE, 0);
        Intent intent = new Intent(context, SetWallpaperJob.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        calendar.add(Calendar.MINUTE, settings.getInt("Interval", 0));
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                settings.getInt("Interval", 0) * 60000, sender);
    }

    private void batteryLevel(Context context) {

        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }

                if(level>60){  RegisterSlideShow(context);  }
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.getApplicationContext().registerReceiver(batteryLevelReceiver, batteryLevelFilter);

    }

}
