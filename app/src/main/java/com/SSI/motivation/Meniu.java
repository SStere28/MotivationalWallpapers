package com.SSI.motivation;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Meniu extends AppCompatActivity {


    private GridView gridView;
    private static final int TIME_INTERVAL = 1500;

    final ArrayList<ImageItem> imageItems = new ArrayList<>();
    private static int RESULT_LOAD_IMAGE = 1;
    private ArrayList<String> user = new ArrayList<>();
    private ArrayList<String> imgadress = new ArrayList<>();

    ArrayList<Integer> dell = new ArrayList<>();
    int del = 0;
    int k=0;
    private long mBackPressed;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meniu);
        getData();

        gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new AppsAdapter());

        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (del == 1) {
                    k=0 ;
                    if (position < 35) {
                        Toast.makeText(getBaseContext(),
                                getResources().getString(R.string.delete), Toast.LENGTH_SHORT)
                                .show();
                        gridView.setItemChecked(position, false);
                    } else {
                        if(k==0){

                            gridView.setItemChecked(position, true);

                            k=1; }
                        else {
                            gridView.setItemChecked(position, false);
                            k=0;
                        }

                    }

                } else {
                    gridView.setItemChecked(position, false);
                    Intent in = new Intent(getApplicationContext(),
                            ViewPic.class);
                    in.putExtra("Number", position);
                    startActivity(in);
                    System.gc();

                }

            }
        });

        ImageView img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent h = new Intent(v.getContext(), ViewPic.class);
                startActivityForResult(h, 0);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

            }
        });

        final ImageView img2 = (ImageView) findViewById(R.id.delete);
        img2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (del == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.selimages),
                            Toast.LENGTH_SHORT).show();
                    img2.setBackgroundResource(R.drawable.delete2);
                    del = 1;

                } else {
                    img2.setBackgroundResource(R.drawable.delete1);

                    int len = gridView.getCount();
                    SparseBooleanArray checked = gridView
                            .getCheckedItemPositions();
                    if (checked.size() > 0) {

                        for (int i = 0; i < len; i++)
                            if (checked.get(i)) {

                                dell.add(i);

                            }

                        delete(dell);
                    }
                    del = 0;
                }

            }
        });

        final ImageView m = (ImageView) findViewById(R.id.m);
        m.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent h = new Intent(v.getContext(), Anim.class);
                startActivityForResult(h, 0);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

            }
        });

        ImageView up = (ImageView) findViewById(R.id.up);
        up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            return;
        } else {
            Toast.makeText(getBaseContext(),  getResources().getString(R.string.back), Toast.LENGTH_SHORT)
                    .show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @SuppressLint("NewApi")
    private void delete(ArrayList<Integer> dell) {

        ArrayList<String> delet = new ArrayList<String>();
        BufferedReader br = null;

        if (dell.size() > 0) {


            try {

                String fpath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/Motivational/user/set.mt";
                File m = new File(fpath);
                br = new BufferedReader(new FileReader(fpath));

                if (m.exists()) {

                    String line = "";
                    while ((line = br.readLine()) != null) {

                        if(dell.size()>0){
                            for (int i = 0; i < dell.size(); i++) {
                                if (imgadress.get(dell.get(i) - 35).contains(line)) {
                                    dell.remove(i);
                                    break ;

                                }else {
                                    delet.add(line);

                                }
                            }
                        }
                        else {
                            delet.add(line);
                        }

                    }
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String fpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/Motivational/user/";

            File dir = new File(fpath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file2 = new File(dir, "set.mt");
            file2.delete();
            File file = new File(dir, "set.mt");

            if (!file.exists()) {

                try {
                    @SuppressWarnings({ "resource", "unused" })
                    FileOutputStream fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch
                    // block
                    e.printStackTrace();
                }
            }
            BufferedWriter buf;
            try {
                buf = new BufferedWriter(new FileWriter(file, true));
                for (int i = 0; i < delet.size(); i++) {
                    buf.append(delet.get(i));
                    buf.append("\r\n");
                }
                buf.close();

            } catch (IOException e) {
                // TODO Auto-generated catch
                // block
                e.printStackTrace();
            }

            delet.clear();

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    private void getData() {


        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            ImageItem m = new ImageItem(imgs.getResourceId(i, -1)+"", "img");
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
                    imgadress.add(line);

                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (user.size() > 0) {

            for (int i = 0; i < user.size(); i++) {

                ImageItem m = new ImageItem(user.get(i), "img");

                imageItems.add(m);

            }
            user.clear();

        }

    }

    public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckableLayout l;
            ImageView i;

            if (convertView == null) {
                i = new ImageView(Meniu.this);

                l = new CheckableLayout(Meniu.this);
                l.setLayoutParams(new GridView.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                l.addView(i);

            } else {
                l = (CheckableLayout) convertView;
                i = (ImageView) l.getChildAt(0);
            }

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.widthPixels;
            if(position<35){
                Picasso.with(Meniu.this).load(Integer.parseInt(imageItems
                        .get(position).getImage())).placeholder(R.drawable.progress).error(R.drawable.ic_error).resize(height/3, height/3).into(i);
            }
            else {
                Picasso.with(Meniu.this).load(new File(imageItems
                        .get(position).getImage())).placeholder(R.drawable.progress).error(R.drawable.ic_error).resize(height/3, height/3).into(i);
            }
            return l;
        }

        @Override
        public final int getCount() {
            return imageItems.size();
        }

        @Override
        public final Object getItem(int position) {
            return imageItems.get(position);
        }

        @Override
        public final long getItemId(int position) {
            return position;
        }
    }

    public class CheckableLayout extends FrameLayout implements Checkable {
        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void setChecked(boolean checked) {
            mChecked = checked;
            setBackgroundDrawable(checked ? getResources().getDrawable(
                    R.color.albastru) : null);
        }

        @Override
        public boolean isChecked() {
            return mChecked;
        }

        @Override
        public void toggle() {
            setChecked(!mChecked);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.MediaColumns.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ArrayList<String> pict = new ArrayList<String>();
            BufferedReader br = null;

            try {

                String fpath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/Motivational/user/set.mt";
                File m = new File(fpath);
                br = new BufferedReader(new FileReader(fpath));


                if (m.exists()) {

                    String line = "";
                    while ((line = br.readLine()) != null) {
                        pict.add(line);


                    }
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!pict.contains(picturePath)){

                String fpath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/Motivational/user/";

                File dir = new File(fpath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir, "set.mt");

                if (!file.exists()) {

                    try {
                        @SuppressWarnings({ "resource", "unused" })
                        FileOutputStream fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch
                        // block
                        e.printStackTrace();
                    }
                }


                BufferedWriter buf;
                try {
                    buf = new BufferedWriter(new FileWriter(file, true));

                    buf.append(picturePath);
                    buf.append("\r\n");

                    buf.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch
                    // block
                    e.printStackTrace();
                }

                ImageItem m = new ImageItem(picturePath, "img");


                imageItems.add(m);

                Toast.makeText(getBaseContext(), getResources().getString(R.string.img),
                        Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                finish();
                overridePendingTransition(R.anim.abc_fade_out, R.anim.abc_fade_in);
                startActivity(intent);
            }
            else {
                Toast.makeText(getBaseContext(), "Image Exist",
                        Toast.LENGTH_SHORT).show();
            }



        }

    }







}

