package com.SSI.motivation;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ViewPic extends AppCompatActivity {

    // the images to display
    ViewPager viewPager;
    PagerAdapter adapter;
    String[] rank;
    int it = 0;
    int b;

    final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pic);


        getData();
        rank = new String[imageItems.size()];

        for (int i = 0; i < (imageItems.size()); i++) {
            rank[i] = "" + i;

        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(ViewPic.this, rank, imageItems);
        viewPager.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            it = extras.getInt("Number");
        }

        viewPager.setCurrentItem(it);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                b = viewPager.getCurrentItem();
                AsyncTaskRunner2 runner = new AsyncTaskRunner2();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"", "1");
                } else {
                    runner.execute("", "1");
                }


            }
        });

    }

    @SuppressWarnings("deprecation")
    private void getData() {


        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            imageItems.add(new ImageItem(imgs.getResourceId(i, -1)+"", "Image#" + i));
        }

        BufferedReader br = null;

        try {

            String fpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/Motivational/user/set.mt";
            File m = new File(fpath);
            br = new BufferedReader(new FileReader(fpath));
            if (m.exists()) {

                String line = "";
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height =metrics.heightPixels ;
                while ((line = br.readLine()) != null) {

                    ImageItem m1 = new ImageItem(line, "img");
                    imageItems.add(m1);

                }

                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private class AsyncTaskRunner2 extends AsyncTask<String, String, String> {

        private String resp="";
        private ProgressDialog progDailog;


        @Override
        protected String doInBackground(String... params) {
            publishProgress("");


            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            Bitmap bm;
            if(b<35){
                bm = BitmapFactory.decodeResource(getResources() ,
                        Integer.parseInt(imageItems.get(b)
                                .getImage())); }
            else {
                bm = BitmapFactory.decodeFile(imageItems
                        .get(b).getImage());
            }
            Bitmap bitmap = Bitmap.createScaledBitmap(bm, width, height, true);
            WallpaperManager wallpaperManager = WallpaperManager
                    .getInstance(ViewPic.this);
            wallpaperManager.setWallpaperOffsetSteps(1, 1);
            wallpaperManager.suggestDesiredDimensions(width, height);

            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




            return resp;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(ViewPic.this, R.style.MyTheme);
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(String result) {


            progDailog.dismiss();
            Toast.makeText(getBaseContext(), getResources().getString(R.string.background),
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

}
