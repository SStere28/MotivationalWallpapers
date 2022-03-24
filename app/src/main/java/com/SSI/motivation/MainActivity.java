package com.SSI.motivation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

     private ProgressBar pr;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            pr = (ProgressBar) findViewById(R.id.progressBar);

            if (getIntent().getBooleanExtra("EXIT", false)) {
                finish();
                return;
            }
            SharedPreferences prefs = getSharedPreferences("ShortCutPrefs", MODE_PRIVATE);
            if(!prefs.getBoolean("isFirstTime", false)){
                addShortcut();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isFirstTime", true);
                editor.commit();
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(getApplicationContext(), Meniu.class);

                    startActivity(in);

                    pr.animate();
                    overridePendingTransition(R.anim.abc_fade_in,
                            R.anim.abc_fade_out);

                }
            }, 3000);

        }

        private void addShortcut() {
            //Adding shortcut for MainActivity on Home screen
            Intent shortcutIntent = new Intent(getApplicationContext(),MainActivity.class);

            shortcutIntent.setAction(Intent.ACTION_MAIN);

            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, this.getResources().getString(R.string.app_name));
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                    R.drawable.logo_intro));

            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(addIntent);
        }
    }


