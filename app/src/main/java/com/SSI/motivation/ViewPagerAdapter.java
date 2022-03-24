package com.SSI.motivation;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
	// Declare Variables
	Context context;
	String[] rank;
	ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
	LayoutInflater inflater;

	public ViewPagerAdapter(Context context, String[] rank,
			ArrayList<ImageItem> imageItems) {
		this.context = context;
		this.rank = rank;
		this.imageItems = imageItems;
	}

	@Override
	public int getCount() {
		return rank.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		ImageView imgflag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,
				false);

		// Locate the ImageView in viewpager_item.xml
		imgflag = (ImageView) itemView.findViewById(R.id.flag);
		// Capture position and set to the ImageView
		if(position<35){
			Picasso.with(context).load(Integer.parseInt(imageItems
					.get(position).getImage())).placeholder(R.drawable.progress).into(imgflag);
			}
			else {
				Picasso.with(context).load(new File(imageItems
						.get(position).getImage())).placeholder(R.drawable.progress).into(imgflag);
					}
		
		

		// Add viewpager_item.xml to ViewPager
		container.addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		container.removeView((RelativeLayout) object);

	}
}
