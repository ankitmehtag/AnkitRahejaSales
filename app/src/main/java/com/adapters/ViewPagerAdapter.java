package com.adapters;

import java.util.ArrayList;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sp.R;
import com.helper.BMHConstants;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
	// Declare Variables
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<String> arrUrls;

	public ViewPagerAdapter(Context context, ArrayList<String> arrURLs) {
		this.context = context;
		this.arrUrls = arrURLs;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}
	
	public int getRealCount() {
		return arrUrls.size();
    }

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		
		int virtualPosition = position % getRealCount();
		ImageView imgflag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,
				false);

		// Locate the ImageView in viewpager_item.xml
		imgflag = (ImageView) itemView.findViewById(R.id.flag);
		// Capture position and set to the ImageView
		
		if(arrUrls.size()>0 && !arrUrls.get(virtualPosition).equalsIgnoreCase("")){
			Picasso.with(context)
			.load(arrUrls.get(virtualPosition))
			.placeholder(BMHConstants.PLACE_HOLDER)
			.error(BMHConstants.NO_IMAGE)
			.into(imgflag);
		}

		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}
