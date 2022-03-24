package com.SSI.motivation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GridViewCompatActivity extends AppCompatActivity {

    GridViewCompat gridView;
    private ArrayList<String> user = new ArrayList<String>();
    final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
    String[] intArray;
    int a = 1;
    ArrayList<Integer> result = new ArrayList<Integer>();
    String value = "";
    private ArrayList<Integer> pics = new ArrayList<Integer>();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_compat);

        getData();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // pics.clear();
            value = extras.getString("Res");
            intArray = value.split(" ");
            if (intArray.length > 1) {
                for (int i = 0; i < intArray.length; i++) {

                    if (!pics.contains(Integer.parseInt(intArray[i]))) {
                        pics.add(Integer.parseInt(intArray[i]));
                    }

                }
                Collections.sort(pics);
            }
        }

        gridView = (GridViewCompat) findViewById(R.id.gridView1);

        // NOTE: We are using setChoiceMode, as I said, its a drop-in
        // replacement
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        gridView.setAdapter(new ImageAdapter(getApplicationContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> view, View arg1, int pos,
                                    long id) {
                // We need to invalidate all views on 4.x versions
                GridViewCompat gridView = (GridViewCompat) view;
                gridView.invalidateViews();
            }

        });

        findViewById(R.id.button1).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        SparseBooleanArray checkArray;
                        checkArray = gridView.getCheckedItemPositions();

                        String selectedPos = "";
                        int count = checkArray.size();
                        for (int i = 0; i < count; i++) {
                            if (checkArray.valueAt(i) && count >= 2) {
                                selectedPos += checkArray.keyAt(i) + " ";
                            } else if (checkArray.valueAt(i)) {
                                selectedPos += checkArray.keyAt(i);
                            }
                        }

                        String[] intArray2;

                        intArray2 = selectedPos.split(" ");
                        if (intArray2.length > 1) {
                            for (int i = 0; i < intArray2.length; i++) {

                                if (!pics.contains(Integer
                                        .parseInt(intArray2[i]))) {
                                    pics.add(Integer.parseInt(intArray2[i]));
                                }

                            }
                            Collections.sort(pics);
                        }

                        for (int i = 0; i < pics.size(); i++) {
                            result.add(pics.get(i));
                        }
                        Intent intent = new Intent();
                        intent.putExtra("Res", result);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });

    }

    private void getData() {

        // retrieve String drawable array

        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {

		/*	Matrix matrix = new Matrix();
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
					imgs.getResourceId(i, -1));

			int width = bitmap.getWidth();

			int height = bitmap.getHeight();

			float scaleWidth = ((float) 200) / width;

			float scaleHeight = ((float) 200) / height;

			matrix.postScale(scaleWidth, scaleHeight);

			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
					height, matrix, false); */

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

                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (user.size() > 0) {

            for (int i = 0; i < user.size(); i++) {
			/*	Matrix matrix = new Matrix();
				Bitmap bm = BitmapFactory.decodeFile(user.get(i));
				int width = bm.getWidth();

				int height = bm.getHeight();

				float scaleWidth = ((float) 200) / width;

				float scaleHeight = ((float) 200) / height;

				matrix.postScale(scaleWidth, scaleHeight);

				Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width,
						height, matrix, false); */

                ImageItem m = new ImageItem(user.get(i), "img");
                imageItems.add(m);

            }
            user.clear();

        }

    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return imageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        // create a new grid view item for each item referenced by the Adapter
        @Override
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            CheckBox checkBox;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.grid_view_item,
                        parent, false);
            }
            imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);

            GridViewCompat gvc = (GridViewCompat) parent;
            if (gvc.getChoiceMode() == AbsListView.CHOICE_MODE_MULTIPLE) {
                SparseBooleanArray checkArray;
                checkArray = gvc.getCheckedItemPositions();

                if (pics.contains(position)) {
                    // Toast.makeText(getApplicationContext(), "mergeu",
                    // Toast.LENGTH_SHORT).show();
                    gvc.setItemChecked(position, true);
                    pics.remove(pics.indexOf(position));
                }
                checkBox.setChecked(false);

                if (checkArray != null) {
                    if (checkArray.get(position)) {
                        checkBox.setChecked(true);
                    }

                }

            }
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.widthPixels;
            if(position<35){
                Picasso.with(GridViewCompatActivity.this).load(Integer.parseInt(imageItems
                        .get(position).getImage())).placeholder(R.drawable.progress).resize(height/3, height/3).into(imageView);
            }
            else {
                Picasso.with(GridViewCompatActivity.this).load(new File(imageItems
                        .get(position).getImage())).placeholder(R.drawable.progress).resize(height/3, height/3).into(imageView);
            }



            checkBox.setText("" + position);
            checkBox.setTextColor(Color.BLACK);

            return convertView;
        }

    }
}
