package com.SSI.motivation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;


public class SetWallpaperJob extends BroadcastReceiver {

	public static final String PREFS_FILE = "Mt";

	Boolean ShuffleMode, battery;
	static int FileNumber = 0;
	private ArrayList<String> user = new ArrayList<String>();
	final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();

	int level;


	String[] a ;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub


		// load preferences

		SharedPreferences settings = context.getSharedPreferences(
				PREFS_FILE, 0);

		a	= settings.getString("Res","").trim().split(" ");
		ShuffleMode = settings.getBoolean("ShuffleMode", false);
		battery = settings.getBoolean("Battery", false);

		getData(context);


           if(battery){
			   batteryLevel(context);
		   }

		Bitmap myBitmap;

		if (imageItems.size() > 0) {

			if (ShuffleMode) {
				final Random myRandom = new Random();
				FileNumber = myRandom.nextInt(imageItems.size()-1);

				//	myBitmap = imageItems.get(FileNumber).getImage();
				//	myBitmap = Bitmap.createBitmap(imageItems.get(FileNumber).getImage());
				if(Integer.parseInt(a[FileNumber])<35){
					Options options = new BitmapFactory.Options();
					options.inScaled = false;

					myBitmap = BitmapFactory.decodeResource(context.getResources() ,
							Integer.parseInt(imageItems.get(FileNumber)
									.getImage()),options); }
				else {

					Options options = new BitmapFactory.Options();
					options.inScaled = false;
					myBitmap = BitmapFactory.decodeFile(imageItems
							.get(FileNumber).getImage(),options);
				}

			} else {

				if(Integer.parseInt(a[FileNumber])<35){

					Options options = new BitmapFactory.Options();
					options.inScaled = false;
					myBitmap = BitmapFactory.decodeResource(context.getResources() ,
							Integer.parseInt(imageItems.get(FileNumber)
									.getImage()),options);
					FileNumber++;}
				else {

					Options options = new BitmapFactory.Options();
					options.inScaled = false;
					myBitmap = BitmapFactory.decodeFile(imageItems
							.get(FileNumber).getImage(),options);
					FileNumber++;
				}


			}
			if (FileNumber == (a.length)) {
				FileNumber = 0;
			}


			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);



			int height = windowManager.getDefaultDisplay().getHeight();
			int width = windowManager.getDefaultDisplay().getWidth();


			Bitmap bitmap = Bitmap.createScaledBitmap(myBitmap, width, height, true);
			WallpaperManager wallpaperManager = WallpaperManager
					.getInstance(context);
			wallpaperManager.setWallpaperOffsetSteps(1, 1);
			wallpaperManager.suggestDesiredDimensions(width, height);

			try {
				wallpaperManager.setBitmap(bitmap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} else {


		}



	}// end onReceive

	@SuppressLint("Recycle")
	private void getData(Context context) {



		TypedArray imgs = context.getResources().obtainTypedArray(
				R.array.image_ids);
		for (int i = 0; i < a.length; i++) {

			//	Bitmap bitmap = BitmapFactory.decodeResource(
			//		context.getResources(), imgs.getResourceId(i, -1));

			ImageItem m = new ImageItem(imgs.getResourceId(Integer.parseInt(a[i]), -1)+"", "img");
			imageItems.add(m);

		}

		BufferedReader br = null;

		try {

			String fpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Motivational/user/set.mt";
			File m = new File(fpath);
			br = new BufferedReader(new FileReader(fpath));

			user.clear();
			if (m.exists()) {

				String line = "";
				while ((line = br.readLine()) != null) {

					user.add(line);

				}
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (user.size() > 0) {
			for (int i = 0; i < user.size(); i++) {
				ImageItem m = new ImageItem(user.get(i)+"", "img");
				imageItems.add(m);
			}


			user.clear();

		}


	}



	private void UnRegisterSlideShow(Context context) {
		Intent intent = new Intent(context, SetWallpaperJob.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(sender);

		Intent intent2 = new Intent(context, CheckBatteryJob.class);
		PendingIntent sender2 = PendingIntent.getBroadcast(context, 0, intent2, 0);
		AlarmManager am2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am2.setRepeating(AlarmManager.RTC_WAKEUP, 60 * 60000 ,
				90 * 60000, sender2);
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

				if(level<20){  UnRegisterSlideShow(context);  }
			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.getApplicationContext().registerReceiver(batteryLevelReceiver, batteryLevelFilter);



	}

}// end main class
