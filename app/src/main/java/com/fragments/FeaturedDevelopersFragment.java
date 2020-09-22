package com.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sp.BMHApplication;
import com.sp.R;
import com.VO.DeveloperVO;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.FragmentClickListener;
import com.squareup.picasso.Picasso;

public class FeaturedDevelopersFragment extends Fragment {

	public static int CURRENT_EVENT = -1;
	public static final int VIEW_ALL_PROJECTS = 1;
	private BMHApplication app;
	private Activity ctx;

	private RelativeLayout loaderView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		final View v = (RelativeLayout) inflater.inflate(R.layout.featured_developers_layout, container, false);
//		int pos = this.getArguments().getInt("pos");
		final DeveloperVO dev = this.getArguments().getParcelable("developervo");
		// Font path
		String fontPath = "fonts/regular.ttf";
		app = (BMHApplication) getActivity().getApplication();
		ctx = getActivity();
		// Add image loader
		View includeView = v.findViewById(R.id.inclide_view);
		ImageView imageView = (ImageView) includeView.findViewById(R.id.img);
		loaderView = (RelativeLayout)v.findViewById(R.id.loader_view);
		imageView.setBackgroundResource(R.drawable.progress_loader);
		AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
		anim.start();


		ImageView img = (ImageView) v.findViewById(R.id.imgDevBanner);
		TextView tvName = (TextView) v.findViewById(R.id.tvDeveloperName);
		final TextView tvDetails = (TextView) v.findViewById(R.id.tvDeveloperDetail);
		TextView tvYearOfEstablishment = (TextView) v.findViewById(R.id.tvYearOfEstablishment);
		TextView tvAreaDelivered = (TextView) v.findViewById(R.id.tvAreaDelivered);
		Button btnShowAllProjs = (Button) v.findViewById(R.id.btnShowAllProj);
		TextView builder_name = (TextView)v.findViewById(R.id.builder_name);
		TextView ready = (TextView)v.findViewById(R.id.ready);
		TextView launch = (TextView)v.findViewById(R.id.pre_launch);
		TextView constraction = (TextView)v.findViewById(R.id.constraction);

		TextView ready_value = (TextView)v.findViewById(R.id.ready_value);
		TextView new_value = (TextView)v.findViewById(R.id.new_value);
		TextView under_value = (TextView)v.findViewById(R.id.under_value);

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/regular.ttf");

		// Applying font

		ready.setTypeface(font);
		launch.setTypeface(font);
		constraction.setTypeface(font);
		btnShowAllProjs.setTypeface(font);
		ready_value.setTypeface(font);
		new_value.setTypeface(font);
		under_value.setTypeface(font);

//		tvDetails.setMaxLines(3);
		final TextView readtext = (TextView)v. findViewById(R.id.read_text);

		tvName.setText(dev.getCompany_name());
		tvAreaDelivered.setText(Html.fromHtml("Area Delivered: "+"<b>"+dev.getArea_delevered()+" Million sqft"+"</b>"));
		tvDetails.setText(dev.getDescription());

		ready_value.setText(dev.getReady_to_move());
		new_value.setText(dev.getNew_launch());
		under_value.setText(dev.getUnder_construction());

		builder_name.setText(dev.getCompany_name());
//		System.out.println(dev.getReady_to_move);
//		Log.e(dev.getReady_to_move(), "Krishna" );

		tvYearOfEstablishment.setText(Html.fromHtml("Year of Establishment: "+"<b>"+dev.getEstablish_year()+"</b>"));

		if(dev.getLogo() != null && !dev.getLogo().isEmpty()){
			Picasso.with(getActivity())
					.load(UrlFactory.getShortImageByWidthUrl(BMHConstants.CRAOUSEL_IMAGE_WIDTH, dev.getLogo()))
					.placeholder(BMHConstants.PLACE_HOLDER)
					.error(BMHConstants.NO_IMAGE).resize(BMHConstants.CRAOUSEL_IMAGE_WIDTH, BMHConstants.CRAOUSEL_IMAGE_HEIGHT)
					.into(img);
		}

		btnShowAllProjs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CURRENT_EVENT = VIEW_ALL_PROJECTS;
				if( getParentFragment() instanceof FragmentClickListener) {
					FragmentClickListener mFragmentClickListener = (FragmentClickListener) getParentFragment();
					mFragmentClickListener.myOnClickListener(FeaturedDevelopersFragment.this,dev);
				}
			}
		});


//	readtext.setOnClickListener(new OnClickListener() {
//			
//		boolean temp = false;
//			@Override
//			public void onClick(View v) {
//				
//
//				if (temp) {
//					temp = !temp;
//					tvDetails.setMaxLines(3);
//					readtext.setText("Read More" );
//				} else {
//					temp = !temp;
//					tvDetails.setMaxLines(200);
//					readtext.setText("Read Less");
//
//				}
//				
//                   }
//		              });

		// <-----loader visible set
//				loadingTask.dontShowProgressDialog();
//				loaderView.setVisibility(View.VISIBLE);

		return v;
	}

}
