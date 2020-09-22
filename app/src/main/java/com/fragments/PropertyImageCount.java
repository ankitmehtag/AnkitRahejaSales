//package com.fragments;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.ImageView.ScaleType;
//import android.widget.LinearLayout.LayoutParams;
//
//import com.queppelin.builder.BMHApplication;
//import com.queppelin.builder.ProjectDetailActivity;
//import com.queppelin.builder.ProjectGalleryActivity;
//import com.builder.R;
//import com.queppelin.builder.UnitDetailActivity;
//import com.helper.BMHConstants;
//import com.helper.UrlFactory;
//import com.interfaces.GalleryCallback;
//import com.views.TouchImageView;
//import com.squareup.picasso.Picasso;
//
//public class PropertyImageCount extends Fragment {
//
//	private FragmentActivity fragmentActivity;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.fragment_property_image,container, false);
//
//		LinearLayout llImgHolder = (LinearLayout) view 	.findViewById(R.id.llImageHolder);
//		Bundle b = getArguments();
//		String url = b.getString("imgurl");
//		final int pos = b.getInt("pos");
//		int count = b.getInt("count");
//		String imageName = b.getString("imgname");
//
//		BMHApplication app = (BMHApplication) getActivity().getApplication();
//
//		ImageView img = null;
//		if (fragmentActivity instanceof ProjectGalleryActivity) {
//			// ImageView img = (TouchImageView)
//			// view.findViewById(R.id.imageViewHelp);
//
//			img = new TouchImageView(fragmentActivity);
//			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			img.setLayoutParams(params);
//			// img.setScaleType(ScaleType.CENTER_CROP);
//
//			TextView tvFragNo = (TextView) view.findViewById(R.id.tvFragNo);
//			tvFragNo.setVisibility(View.VISIBLE);
//			int n = pos + 1;
//			tvFragNo.setText(n + "/" + count);
//
//			if (imageName != null && !imageName.isEmpty()) {
//				TextView tvImageName = (TextView) view 	.findViewById(R.id.tvImageName);
//				tvImageName.setVisibility(View.VISIBLE);
//				tvImageName.setText(imageName);
//			}
//		} else {
//			img = new ImageView(fragmentActivity);
//			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			img.setLayoutParams(params);
//			img.setScaleType(ScaleType.CENTER_CROP);
//
//		}
//		llImgHolder.addView(img);
//
//		if (!url.equalsIgnoreCase("")) {
//			if (fragmentActivity instanceof ProjectGalleryActivity) {
//				Picasso.with(getActivity()) .load(UrlFactory.getShortImageByWidthUrl(
//								app.getWidth(fragmentActivity), url))
//						.placeholder(BMHConstants.PLACE_HOLDER)
//						.error(BMHConstants.NO_IMAGE).into(img);
//			} else {
//				Picasso.with(getActivity())
//						// .load(UrlFactory.getShortImageByHeightUrl(BMHConstants.PAGER_IMAGE_HEIGHT,
//						// url))
//						.load(UrlFactory.getShortImageByWidthUrl(
//								app.getWidth(fragmentActivity), url))
//						.placeholder(BMHConstants.PLACE_HOLDER)
//						.error(BMHConstants.NO_IMAGE).into(img);
//				
//			}
//		}
//
//		img.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (fragmentActivity instanceof ProjectDetailActivity
//						| fragmentActivity instanceof UnitDetailActivity) {
//					GalleryCallback callBack = (GalleryCallback) fragmentActivity;
//					callBack.onPageClicked(pos, BMHConstants.TYPE_IMAGES);
//				} else {
//					System.out.println("hh not instance");
//				}
//			}
//		});
//
//		return view;
//	}
//
//	@Override
//	public void onStart() {
//		super.onStart();
//	}
//
//	public FragmentActivity getFragmentActivity() {
//		return fragmentActivity;
//	}
//
//	public void setFragmentActivity(FragmentActivity fragmentActivity) {
//		this.fragmentActivity = fragmentActivity;
//	}
//
//}
