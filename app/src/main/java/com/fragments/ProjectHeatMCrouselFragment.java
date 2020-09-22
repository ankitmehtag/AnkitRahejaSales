package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.ProjectHeatmapActivity;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.CrouselOnItemClickListner;
import com.model.ProjectsListRespModel;
import com.utils.Utils;
import com.squareup.picasso.Picasso;

public class ProjectHeatMCrouselFragment extends Fragment {

	public static final int FAVORITE_CLICK = 1;
	public static final int CAROUSEL_ITEM_CLICK = 2;
	public ProjectsListRespModel.Data heatmapDataModel;
	public static ProjectHeatMCrouselFragment newInstance(ProjectsListRespModel.Data heatmapDataModel) {
		ProjectHeatMCrouselFragment fragmentFirst = new ProjectHeatMCrouselFragment();
		Bundle args = new Bundle();
		args.putSerializable("obj", heatmapDataModel);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		final View mView = inflater.inflate(R.layout.project_crousel_item, container, false);
		LinearLayout ll_crousel = (LinearLayout)mView.findViewById(R.id.ll_crousel);
		LinearLayout ll_empty_view = (LinearLayout)mView.findViewById(R.id.ll_empty_view);
		ImageView img = (ImageView) mView.findViewById(R.id.imageViewProp);
		TextView tvRating = (TextView) mView.findViewById(R.id.tv_rating);
		TextView tv_status = (TextView)mView.findViewById(R.id.tv_status);
		TextView tvReturns = (TextView) mView.findViewById(R.id.returns);
		TextView tv_builder_name = (TextView) mView.findViewById(R.id.tv_builder_name);
		TextView tv_project_name = (TextView) mView.findViewById(R.id.tv_project_name);
		TextView tv_locality = (TextView) mView.findViewById(R.id.tv_locality);
		TextView tv_bhk = (TextView) mView.findViewById(R.id.tv_bhk);
		TextView tv_psf = (TextView) mView.findViewById(R.id.tv_psf);
		ImageView iv_fav = (ImageView)mView.findViewById(R.id.iv_fav);

		TextView tvInfra = (TextView) mView.findViewById(R.id.tv_infra);
		TextView tvNeeds = (TextView) mView.findViewById(R.id.tv_needs);
		TextView tvLife = (TextView) mView.findViewById(R.id.tv_lifestyle);

		heatmapDataModel = (ProjectsListRespModel.Data) this.getArguments().getSerializable("obj");
		if(heatmapDataModel != null && heatmapDataModel.getID() != null) {
			ll_crousel.setVisibility(View.VISIBLE);
			ll_empty_view.setVisibility(View.GONE);
			//BMHApplication app = (BMHApplication) getActivity().getApplication();
			int rating = Utils.toInt(heatmapDataModel.getRatings_average());
			tvRating.setText(String.valueOf(rating));
			tvReturns.setText(heatmapDataModel.getPrice_one_year());
			tv_status.setText(heatmapDataModel.getStatus());
			tv_builder_name.setText(heatmapDataModel.getBuilder_name());
			tv_project_name.setText(Html.fromHtml(heatmapDataModel.getDisplay_name()));
			tv_locality.setText(heatmapDataModel.getExactlocation());
			tv_bhk.setText(heatmapDataModel.getUnit_type());
			String priceTxt = heatmapDataModel.getPsf() + " " + heatmapDataModel.getPsf_unit();
			float pricePsf = Utils.toFloat(heatmapDataModel.getPsf());
			if(pricePsf > 0) priceTxt = Utils.priceFormat(pricePsf) + " " + heatmapDataModel.getPsf_unit();
			tv_psf.setText(priceTxt);

			//String infra = heatmapDataModel.getInfra();
			//String needs = heatmapDataModel.getNeeds();
			//String life = heatmapDataModel.getLife_style();
			tvInfra.setText(heatmapDataModel.getInfra());
			tvNeeds.setText(heatmapDataModel.getNeeds());
			tvLife.setText(heatmapDataModel.getLife_style());
			if (heatmapDataModel.isFavorite()) {
				iv_fav.setImageResource(R.drawable.favorite_filled);
			} else {
				iv_fav.setImageResource(R.drawable.new_heart);
			}
			if (heatmapDataModel.getBanner_img() != null && !heatmapDataModel.getBanner_img().isEmpty()) {
				String imgurl = UrlFactory.getShortImageByWidthUrl(BMHConstants.CRAOUSEL_IMAGE_WIDTH, heatmapDataModel.getBanner_img());
				Picasso.with(getActivity())
						.load(imgurl)
						.placeholder(BMHConstants.PLACE_HOLDER)
						.error(BMHConstants.PLACE_HOLDER)
					/*.error(BMHConstants.NO_IMAGE)*/
						.resize(BMHConstants.CRAOUSEL_IMAGE_WIDTH, BMHConstants.CRAOUSEL_IMAGE_HEIGHT)
						.into(img);
			}
			mView.setTag(R.integer.project_item, heatmapDataModel);
			iv_fav.setTag(R.integer.project_item, heatmapDataModel);
			iv_fav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getActivity() instanceof ProjectHeatmapActivity) {
						CrouselOnItemClickListner mCrouselOnItemClickListner = (CrouselOnItemClickListner) getActivity();
						v.setTag(R.integer.event, FAVORITE_CLICK);
						mCrouselOnItemClickListner.onclickCrouselItem(v);
					}
				}
			});
			mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getActivity() instanceof ProjectHeatmapActivity) {
						CrouselOnItemClickListner mCrouselOnItemClickListner = (CrouselOnItemClickListner) getActivity();
						v.setTag(R.integer.event, CAROUSEL_ITEM_CLICK);
						mCrouselOnItemClickListner.onclickCrouselItem(v);
					}
				}
			});
		}else{
			ll_crousel.setVisibility(View.GONE);
			ll_empty_view.setVisibility(View.VISIBLE);
		}
		return mView;
	}

}
