package com.SSI.motivation;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Anim extends Activity {

    public static final int SELECT_FOLDER = 6541;
    public static final String PREFS_FILE = "Mt";
    ArrayList<Integer> result = new ArrayList<>();
    private Button p, m;
    private CheckBox shuffle;
    private CheckBox battery;

    int k=0;
    AnimationSet animSet;
    private TextView intervalt, pict;
    private int[] time = new int[12];
    private TextView anim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        time[0] = 1;
        time[1] = 5;
        time[2] = 10;
        time[3] = 15;
        time[4] = 20;
        time[5] = 30;
        time[6] = 40;
        time[7] = 60;
        time[8] = 80;
        time[9] = 100;
        time[10] = 120;
        time[11] = 180;

        anim = (TextView) findViewById(R.id.anim);
        intervalt = (TextView) findViewById(R.id.interval);
        pict = (TextView) findViewById(R.id.pict);
        p = (Button) findViewById(R.id.p);
        m = (Button) findViewById(R.id.m);
        pict.setText("");
        shuffle = (CheckBox) findViewById(R.id.show_shuffle);
        battery = (CheckBox) findViewById(R.id.battery);

        SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
        anim.setText(settings.getString("anim", ""));

        p.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (k < 11) {
                    k++;
                    if (k > 0) {
                        intervalt.setText("" + time[k] + " "+getResources().getString(R.string.minute));
                    } else {
                        intervalt.setText("" + time[k] + " "+getResources().getString(R.string.minutes));
                    }

                }
            }
        });

        m.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (k > 0) {
                    k--;
                    if (k > 0) {
                        intervalt.setText("" + time[k] + " "+getResources().getString(R.string.minute));
                    } else {
                        intervalt.setText("" + time[k] + " "+getResources().getString(R.string.minutes));
                    }
                }
            }
        });

        try {

            Button button = (Button) findViewById(R.id.show_dir);
            button.setOnClickListener(showDirClickLister);

            shuffle.setChecked(settings.getBoolean("ShuffleMode", false));
            battery.setChecked(settings.getBoolean("Battery", false));
            pict.setText(settings.getString("Res", ""));


            if (settings.getInt("Interval", 0) > 1) {
                intervalt.setText("" + settings.getInt("Interval", 0) + " "+getResources().getString(R.string.minute));
            } else {
                intervalt.setText("" + settings.getInt("Interval", 0) + " "+getResources().getString(R.string.minutes));
            }


        } catch (Exception e) {
            //	Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("ShuffleMode", shuffle.isChecked());
        editor.putBoolean("Battery", battery.isChecked());

        editor.putInt("Interval", time[k]);

        editor.putString("Res", pict.getText().toString());
        editor.putString("anim", anim.getText().toString());

        // Commit the edits!
        editor.commit();
    }


    private OnClickListener showDirClickLister = new OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                Intent intent = new Intent(getBaseContext(),
                        GridViewCompatActivity.class);
                intent.putExtra("Res", pict.getText().toString());
                startActivityForResult(intent, SELECT_FOLDER);
            } catch (Exception e) {

            }

        }
    };

    private void RegisterSlideShow(int _interval) {

        SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("Interval", _interval);
        editor.putString("Res",pict.getText().toString());
        editor.commit();
        Intent intent = new Intent(this, SetWallpaperJob.class);


        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        calendar.add(Calendar.MINUTE, _interval);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                _interval * 60000, sender);

    }

    private void UnRegisterSlideShow() {
        Intent intent = new Intent(this, SetWallpaperJob.class);

        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);

        SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean("ShuffleMode", false);
        editor.putBoolean("Battery", false);

        editor.putInt("Interval", 0);
        editor.putString("Res", "");

        editor.putString("anim", "");

        // Commit the edits!
        editor.commit();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            result = data.getExtras().getIntegerArrayList("Res");

            String da = "";
            for (int i = 0; i < result.size(); i++) {
                da = da + result.get(i) + " ";
            }
            pict.setText(da);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void saveOnClick(View v){
        if (pict.getText().length() != 0) {

            try {

                int interval = time[k];

                if (interval == 0)
                    throw new Exception(getResources().getString(R.string.intervalerr));

                SharedPreferences settings = getSharedPreferences(
                        PREFS_FILE, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putBoolean("ShuffleMode", shuffle.isChecked());
                editor.putBoolean("Battery", battery.isChecked());
                editor.putInt("Interval", time[k]);
                // Commit the edits!
                editor.commit();

                RegisterSlideShow(interval);
                anim.setText(getResources().getString(R.string.animationn));

                Toast.makeText(getBaseContext(),getResources().getString(R.string.animationn),
                        Toast.LENGTH_LONG).show();

            } catch (Exception e) {

            }// try-catch

            Intent intent = new Intent(getApplicationContext(),
                    Meniu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.picture)
                    , Toast.LENGTH_LONG).show();
        }
    }

    public void refreshOnClick(View v){

        animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(0.0f, -360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(1500);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        ImageView refresh=(ImageView) v.findViewById(R.id.refresh);
        refresh.startAnimation(animSet);

        UnRegisterSlideShow();
        pict.setText("");
        intervalt.setText(getResources().getString(R.string.interval));
        shuffle.setChecked(false);
        battery.setChecked(false);
        anim.setText(getResources().getString(R.string.animation));

        Toast.makeText(getBaseContext(), getResources().getString(R.string.animation),
                Toast.LENGTH_LONG).show();
    }

}
