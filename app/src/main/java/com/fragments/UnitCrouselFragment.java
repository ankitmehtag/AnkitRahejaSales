package com.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.sp.BMHApplication;
import com.sp.R;
import com.sp.UnitMapActivity;
import com.VO.UnitCaraouselVO;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.CrouselOnItemClickListner;
import com.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UnitCrouselFragment extends Fragment {

	private static CrouselOnItemClickListner listner;
	protected BMHApplication app;
	protected static Activity ctx;
	public static final int FAVORITE_CLICK = 1;
	public static final int CAROUSEL_ITEM_CLICK = 2;
	public UnitCaraouselVO unit;

	public static Fragment newInstance(UnitCaraouselVO vo) {
		UnitCrouselFragment mUnitCrouselFragment = new UnitCrouselFragment();
		Bundle args = new Bundle();
		args.putParcelable("obj", vo);
		mUnitCrouselFragment.setArguments(args);
		return mUnitCrouselFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		unit = this.getArguments().getParcelable("obj");
		final BMHApplication app = (BMHApplication) getActivity().getApplication();
		if(unit == null || app == null)return null;
		View unit_crousel_item = inflater.inflate(R.layout.unit_crousel_item, container, false);
		LinearLayout llContent = (LinearLayout) unit_crousel_item.findViewById(R.id.ll_crousel);
		LinearLayout ll_empty_view = (LinearLayout)unit_crousel_item.findViewById(R.id.ll_empty_view);
		RelativeLayout rlImgge = (RelativeLayout) unit_crousel_item.findViewById(R.id.rlUnitImage);
		ImageView img = (ImageView) unit_crousel_item.findViewById(R.id.imageViewUnit);
		ImageView imageViewFav = (ImageView) unit_crousel_item.findViewById(R.id.imageViewFav);
		TextView tvTitle = (TextView) unit_crousel_item.findViewById(R.id.tv_project_name);
		TextView tvUnit = (TextView) unit_crousel_item.findViewById(R.id.text_Unit_No);
		TextView tvFloor = (TextView) unit_crousel_item.findViewById(R.id.text_Floor);
		TextView tvPlc = (TextView) unit_crousel_item.findViewById(R.id.text_Plc);
		TextView tvSqft = (TextView) unit_crousel_item.findViewById(R.id.Sq_ft);
		TextView tvpsf = (TextView) unit_crousel_item.findViewById(R.id.text_psf);
		TextView tvlifestyle = (TextView) unit_crousel_item.findViewById(R.id.textLifestyle);
		TextView tvSubTitle = (TextView) unit_crousel_item.findViewById(R.id.textViewSubtitle);
		TextView tvSubSubTitle = (TextView) unit_crousel_item.findViewById(R.id.textViewSubSubTitle);
		TextView tv_total_floor = (TextView) unit_crousel_item.findViewById(R.id.total_floor);
		if(unit != null && unit.getUnit_id() != null) {
			llContent.setVisibility(View.VISIBLE);
			ll_empty_view.setVisibility(View.GONE);

			String flatSize = unit.getWpcf_flat_size() + " " + unit.getWpcf_flat_size_unit();
			tvTitle.setText(unit.getWpcf_flat_unit_with());
			tvUnit.setText(unit.getUnit_no());
			tvFloor.setText(unit.getFloor_value());
			tvPlc.setText(unit.getPlc_value());
			tvSqft.setText(flatSize);
			String priceTxt = unit.getPrice_SqFt() + " " + unit.getPrice_SqFt_unit();
			float pricePsf = Utils.toFloat(unit.getPrice_SqFt());
			if(pricePsf > 0) priceTxt = Utils.priceFormat(pricePsf) + " " + unit.getPrice_SqFt_unit();
			tvpsf.setText(priceTxt);
			tvlifestyle.setText("Lifestyle" + "\n" + unit.getLife_style());
			tv_total_floor.setText(unit.getTotal_floor());
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			String floor = "Floor " + unit.getFlat_floor();
			float intPrice = Utils.toFloat(unit.getFlat_price());
			tvSubTitle.setText(floor + ", " + flatSize);
			HashMap<String, String> map = ((UnitMapActivity) getActivity()).searchParams;
			if (map != null && map.containsKey("type") && map.get("type").equalsIgnoreCase("E-Auction")) {
				tvSubSubTitle.setText(" - ");
			} else if (map != null && map.containsKey("type") && map.get("type").equalsIgnoreCase("Rent")) {
				tvSubSubTitle.setText(" - ");
			} else {
				tvSubSubTitle.setText(app.getDecimalFormatedPrice(intPrice));
			}

			if (unit.isUser_favourite()) {
				imageViewFav.setImageResource(R.drawable.favorite_filled);
			} else {
				imageViewFav.setImageResource(R.drawable.favorite_outline);
			}
			if (!unit.getImage_unit().equalsIgnoreCase("")) {
				Picasso.with(getActivity())
						.load(UrlFactory.getShortImageByWidthUrl(BMHConstants.CRAOUSEL_IMAGE_HEIGHT, unit.getImage_unit()))
						.placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE)
						.resize(BMHConstants.CRAOUSEL_IMAGE_WIDTH, BMHConstants.CRAOUSEL_IMAGE_HEIGHT).into(img);
			}

			if (unit.getSold_status().equalsIgnoreCase("sold")) {
				ImageView imgReserved = new ImageView(getActivity());
				imgReserved.setScaleType(ImageView.ScaleType.FIT_XY);
				imgReserved.setLayoutParams(params);
				imgReserved.setImageResource(R.drawable.reserved_tilt);
				//imgReserved.getLayoutParams().height = 280;
				imgReserved.setBackgroundColor(Color.parseColor("#66000000"));
				rlImgge.addView(imgReserved);
				//tvSubSubTitle.setText(" - ");
				imageViewFav.setVisibility(View.GONE);
			} else {
				//imageViewFav.setVisibility(View.VISIBLE);
				imageViewFav.setVisibility(View.VISIBLE);
			}
			/*Button buttonOffer = (Button) unit_crousel_item.findViewById(R.id.btn_offer);
			buttonOffer.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {

					final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialog_offers);
					Window window = dialog.getWindow();
					WindowManager.LayoutParams wlp = window.getAttributes();
					wlp.gravity = Gravity.BOTTOM;
					wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
					window.setAttributes(wlp);
					dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
					dialog.show();
					final Button exitButton = (Button) dialog.findViewById(R.id.offers);
					exitButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							getDialog().dismiss();
						}

						private Dialog getDialog() {
							return dialog;
						}
					});
				}
			});
*/
			unit_crousel_item.setTag(R.integer.unit, unit);
			imageViewFav.setTag(R.integer.unit, unit);
			imageViewFav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getActivity() instanceof UnitMapActivity) {
						v.setTag(R.integer.event, FAVORITE_CLICK);
						CrouselOnItemClickListner mCrouselOnItemClickListner = (CrouselOnItemClickListner) getActivity();
						mCrouselOnItemClickListner.onclickCrouselItem(v);

					}
				}
			});
			unit_crousel_item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getActivity() instanceof UnitMapActivity) {
						v.setTag(R.integer.event, CAROUSEL_ITEM_CLICK);
						CrouselOnItemClickListner mCrouselOnItemClickListner = (CrouselOnItemClickListner) getActivity();
						mCrouselOnItemClickListner.onclickCrouselItem(v);
					}
				}
			});
		}else{
			llContent.setVisibility(View.GONE);
			ll_empty_view.setVisibility(View.VISIBLE);
		}
		return unit_crousel_item;
	}
}
