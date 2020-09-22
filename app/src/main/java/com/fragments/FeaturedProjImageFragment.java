package com.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sp.BMHApplication;
import com.sp.R;
import com.VO.ProjectVO;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.FragmentClickListener;
import com.utils.Connectivity;
import com.squareup.picasso.Picasso;

public class FeaturedProjImageFragment extends Fragment {

	public ImageView imageViewFav;
	private LinearLayout llFav;
	public ProjectVO vo;
	public static int CURRENT_EVENT = -1;
	public static final int FAV_CLICK_EVENT = 0;
	public static final int PROJECT_CLICK_EVENT = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.featured_projects_layout, container, false);
		Bundle b = getArguments();
		final int pos = b.getInt("pos");
		vo = b.getParcelable("projectvo");
		RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl1);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CURRENT_EVENT = PROJECT_CLICK_EVENT;
				if (Connectivity.isConnectedWithDoalog(getActivity())) {
					if( getParentFragment() instanceof FragmentClickListener) {
						FragmentClickListener mFragmentClickListener = (FragmentClickListener) getParentFragment();
						mFragmentClickListener.myOnClickListener(FeaturedProjImageFragment.this,vo);
					}
				}
			}
		});

		// Font path
		String fontPath = "fonts/regular.ttf";

		ImageView img = (ImageView) view.findViewById(R.id.imgFeaturedProj);
		TextView tvName = (TextView) view.findViewById(R.id.tvProjName);
		TextView tvBuilderName = (TextView) view.findViewById(R.id.tvBuilderName);
		TextView tvProjAdd = (TextView) view.findViewById(R.id.tvProjAdd);
		TextView tvType = (TextView) view.findViewById(R.id.bhk_type);
		// TextView tvPrice = (TextView)view.findViewById(R.id.price);
		// TextView tvReview = (TextView)view.findViewById(R.id.onwords);
		TextView tvPrice = (TextView) view.findViewById(R.id.price_proj);

		// ======================================================

		imageViewFav = (ImageView) view.findViewById(R.id.imageViewFav);
		imageViewFav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*// TODO Auto-generated method stub
				CommonLib commonLib = new CommonLib();
				commonLib.makeFavorite(imageViewFav, ctx, (BMHApplication) ctx.getApplication(), vo, adapter);*/
				CURRENT_EVENT = FAV_CLICK_EVENT;
				if (Connectivity.isConnectedWithDoalog(getActivity())) {
					if( getParentFragment() instanceof FragmentClickListener) {
						FragmentClickListener mFragmentClickListener = (FragmentClickListener) getParentFragment();
						mFragmentClickListener.myOnClickListener(FeaturedProjImageFragment.this,vo);
					}
				}
			}

		});

		if (vo.isUser_favourite()) {
			imageViewFav.setImageResource(R.drawable.favorite_filled);
		} else {
			imageViewFav.setImageResource(R.drawable.favorite_outline_grey);
		}
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/regular.ttf");
		tvName.setTypeface(font);
		tvType.setTypeface(font);
		tvProjAdd.setTypeface(font);
		// tvPrice.setTypeface(font);
		// tvReview.setTypeface(font);

		// tvName.setText(vo.getName());
		tvName.setText(Html.fromHtml(Html.fromHtml((String) vo.getName()).toString()));

		tvBuilderName.setText("By " + vo.getBuilder_name());
		tvProjAdd.setText(vo.getAddress().trim());
		tvType.setText(vo.getUnit_type());
		tvPrice.setText(vo.getPrice());

		BMHApplication app = (BMHApplication) getActivity().getApplication();
		int width = app.getWidth(getActivity());
		if (vo.getBanner() != null && !vo.getBanner().isEmpty()) {
			Picasso.with(getActivity()).load(UrlFactory.getShortImageByWidthUrl(width, vo.getBanner()))
					.placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(img);
		}

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
