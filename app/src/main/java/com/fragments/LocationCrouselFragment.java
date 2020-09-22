package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.BMHApplication;
import com.sp.LocationMapActivity;
import com.sp.R;
import com.VO.LocationVO;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.CrouselOnItemClickListner;
import com.squareup.picasso.Picasso;

public class LocationCrouselFragment extends Fragment {
	
	private static CrouselOnItemClickListner listner;
	
	
	public static Fragment newInstance(LocationMapActivity context, int pos, float scale, LocationVO vo)
	{
		Bundle b = new Bundle();
		b.putInt("pos", pos);
//		b.putFloat("scale", scale);
		b.putParcelable("locationvo", vo);
		listner = context;
		return Fragment.instantiate(context, LocationCrouselFragment.class.getName(), b);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		final LinearLayout l = (LinearLayout) inflater.inflate(R.layout.location_crousel_item, container, false);
		
		int pos = this.getArguments().getInt("pos");
		final LocationVO locVO = this.getArguments().getParcelable("locationvo");
		BMHApplication app = (BMHApplication) getActivity().getApplication();
		LinearLayout llContent = (LinearLayout) l.findViewById(R.id.ll_crousel);
		llContent.setTag(pos);
		ImageView img = (ImageView) l.findViewById(R.id.imageViewLocation);
		TextView tvLocName = (TextView) l.findViewById(R.id.textViewLocation);
		TextView tvAvgPrice = (TextView) l.findViewById(R.id.textViewAvgPrice);
		TextView tvNoOfProjects = (TextView) l.findViewById(R.id.textViewNoOfProjects);
		
		TextView tvInfra = (TextView) l.findViewById(R.id.tv_infra);
		TextView tvNeeds = (TextView) l.findViewById(R.id.tv_needs);
		TextView tvLife = (TextView) l.findViewById(R.id.tv_lifestyle);
		TextView tvReturns = (TextView) l.findViewById(R.id.return_view);
		TextView tvRating = (TextView) l.findViewById(R.id.textViewRating);
		
		tvLocName.setText(locVO.getName());
		tvNoOfProjects.setText("No. of Projects: "+locVO.getTotal_projects());
		float stPrice = 0;
		try {
			stPrice = Float.parseFloat(locVO.getAvgPsfLocation());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		int a = (int) stPrice;
		tvAvgPrice.setText( a + " Avg Price/Sq.ft");
		tvInfra.setText("Infra\n"+locVO.getInfra());
		tvNeeds.setText("Needs\n"+locVO.getNeeds());
		tvLife.setText("Lifestyle\n"+locVO.getLifeStyle());
		tvReturns.setText(locVO.getReturns());
		tvRating.setText(locVO.getAvg_rating());
		
		img.setTag(pos);
		System.out.println("hh location image path ="+locVO.getLocationImage());
		if(locVO.getLocationImage() != null && !locVO.getLocationImage().isEmpty()){
			String imgurl = UrlFactory.getShortImageByWidthUrl(BMHConstants.CRAOUSEL_IMAGE_WIDTH, locVO.getLocationImage());
			System.out.println("hh short url ="+imgurl);
			Picasso.with(getActivity())
			.load(imgurl)
			.placeholder(BMHConstants.PLACE_HOLDER)
			.error(BMHConstants.NO_IMAGE)
			.resize(BMHConstants.CRAOUSEL_IMAGE_WIDTH, BMHConstants.CRAOUSEL_IMAGE_HEIGHT)
			.into(img);
		}
		
//		MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.root);
//		float scale = this.getArguments().getFloat("scale");
//		root.setScaleBoth(scale);
		
		llContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listner!= null) {
					System.out.println("hh seting pos ="+v.getTag());
					listner.onclickCrouselItem(v);
				}else{
					System.out.println("hh listner obj null");
				}
			}
		});
		return l;
	}

}
