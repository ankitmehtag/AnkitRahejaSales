package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.BMHApplication;
import com.sp.R;
import com.sp.SubLocationHeatmapActivity;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.CrouselOnItemClickListner;
import com.model.SubLocHeatMRespModel;
import com.utils.Utils;
import com.squareup.picasso.Picasso;

public class SubLocHeatMCrouselFragment extends Fragment {

	private static CrouselOnItemClickListner listner;


	// newInstance constructor for creating fragment with arguments
	public static Fragment newInstance(SubLocHeatMRespModel.SectorLists heatmapDataModel,String location) {
		SubLocHeatMCrouselFragment fragmentFirst = new SubLocHeatMCrouselFragment();
		Bundle args = new Bundle();
		args.putSerializable("obj", heatmapDataModel);
		args.putString("location",location);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		final View mView = inflater.inflate(R.layout.location_crousel_item, container, false);
		final String location = this.getArguments().getString("location");
		BMHApplication app = (BMHApplication) getActivity().getApplication();
		LinearLayout ll_crousel = (LinearLayout) mView.findViewById(R.id.ll_crousel);
		LinearLayout ll_empty_view = (LinearLayout) mView.findViewById(R.id.ll_empty_view);
		ImageView img = (ImageView) mView.findViewById(R.id.imageViewLocation);
		TextView tvLocName = (TextView) mView.findViewById(R.id.textViewLocation);
		TextView tv_sublocation = (TextView) mView.findViewById(R.id.tv_sublocation);
		TextView tvAvgPrice = (TextView) mView.findViewById(R.id.textViewAvgPrice);
		TextView tvNoOfProjects = (TextView) mView.findViewById(R.id.textViewNoOfProjects);

		TextView tvInfra = (TextView) mView.findViewById(R.id.tv_infra);
		TextView tvNeeds = (TextView) mView.findViewById(R.id.tv_needs);
		TextView tvLife = (TextView) mView.findViewById(R.id.tv_lifestyle);
		TextView tvReturns = (TextView) mView.findViewById(R.id.return_view);
		TextView tvRating = (TextView) mView.findViewById(R.id.textViewRating);

		final SubLocHeatMRespModel.SectorLists heatmapDataModel = (SubLocHeatMRespModel.SectorLists) this.getArguments().getSerializable("obj");
		if(heatmapDataModel != null && heatmapDataModel.getSector_id() != null) {
			ll_crousel.setVisibility(View.VISIBLE);
			ll_empty_view.setVisibility(View.GONE);
			tvLocName.setText(heatmapDataModel.getSector_name());
			if (location != null && location.length() > 0) {
				tv_sublocation.setText(location);
				tv_sublocation.setVisibility(View.VISIBLE);
			}
			tvNoOfProjects.setText("No. of Projects: " + heatmapDataModel.getTotal_Projects());
			float stPrice = 0;
			try {
				stPrice = heatmapDataModel.getAvg_PSF_Sector();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			String price = Utils.priceFormat(stPrice);
			tvAvgPrice.setText(price + " " + heatmapDataModel.getAvg_PSF_Sector_unit());
			tvInfra.setText("Infra\n" + heatmapDataModel.getInfra());
			tvNeeds.setText("Needs\n" + heatmapDataModel.getNeeds());
			tvLife.setText("Lifestyle\n" + heatmapDataModel.getLifeStyle());
			tvReturns.setText(heatmapDataModel.getReturns());
			int rating = (int) heatmapDataModel.getAvg_rating();
			tvRating.setText(String.valueOf(rating));
			if (heatmapDataModel.getSector_image() != null && !heatmapDataModel.getSector_image().isEmpty()) {
				String imgurl = UrlFactory.getShortImageByWidthUrl(BMHConstants.CRAOUSEL_IMAGE_WIDTH, heatmapDataModel.getSector_image());
				System.out.println("hh short url =" + imgurl);
				Picasso.with(getActivity())
						.load(imgurl)
						.placeholder(BMHConstants.PLACE_HOLDER)
						.error(BMHConstants.PLACE_HOLDER)
					/*.error(BMHConstants.NO_IMAGE)*/
						.resize(BMHConstants.CRAOUSEL_IMAGE_WIDTH, BMHConstants.CRAOUSEL_IMAGE_HEIGHT)
						.into(img);
			}

			mView.setTag(heatmapDataModel);
			mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getActivity() instanceof SubLocationHeatmapActivity) {
						CrouselOnItemClickListner mCrouselOnItemClickListner = (CrouselOnItemClickListner) getActivity();
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
