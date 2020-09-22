package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.VO.UnitCaraouselVO;
import com.sp.BMHApplication;
import com.sp.R;
import com.sp.UnitListActivity;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.pwn.CommonLib;
import com.squareup.picasso.Picasso;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class UnitListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private ArrayList<UnitCaraouselVO> arrUnits;
	private UnitListActivity acti;
	private LayoutParams params;

	private UnitCaraouselVO unitFavDetails;

	private LinearLayout llFav;
	protected BMHApplication app;
	protected Activity ctx;
	// private UnitList <UnitCaraouselVO> arrUnit;

	List<ImageView> favViews = new ArrayList<ImageView>();
	// private Activity ctx = UnitListAdapter.this;

	public UnitListAdapter(UnitListActivity c, ArrayList<UnitCaraouselVO> arr) {

		acti = (UnitListActivity) c;
		context = c;
		mInflater = LayoutInflater.from(context);
		arrUnits = arr;
		ctx = c;
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
	}

	@Override
	public int getCount() {
		return arrUnits.size();
	}

	@Override
	public Object getItem(int position) {
		return arrUnits.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		// float intPrice = 0;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_propertylist, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_proj_list_name);
			holder.tv_subTitle = (TextView) convertView.findViewById(R.id.tv_per_sqFt_Price);
			holder.tv_subSubTile = (TextView) convertView.findViewById(R.id.tv_proj_address);
			holder.iv_reserved = (ImageView) convertView.findViewById(R.id.iv_reserved);
			holder.tv_unitNo = (TextView) convertView.findViewById(R.id.tv_unitNo);
			holder.tv_price = (TextView) convertView.findViewById(R.id.textViewSubSubTitle);
			holder.tv_Plc = (TextView) convertView.findViewById(R.id.text_Plc);
			holder.tv_psf = (TextView) convertView.findViewById(R.id.text_psf);
			holder.tv_lifeStyle = (TextView) convertView.findViewById(R.id.textLifestyle);
			holder.tv_FloorCount = (TextView) convertView.findViewById(R.id.total_floor);
			holder.unitImage = (ImageView) convertView.findViewById(R.id.imageViewProj);
			holder.imageViewFav = (ImageView) convertView.findViewById(R.id.imageViewFav);
			favViews.add(holder.imageViewFav);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UnitCaraouselVO item = arrUnits.get(position);

		holder.tv_title.setText(item.getWpcf_flat_unit_with());
		holder.tv_unitNo.setText(item.getUnit_no());
		holder.tv_Plc.setText(item.getPlc_value());
		String priceTxt = item.getPrice_SqFt() + " " + item.getPrice_SqFt_unit();
		float pricePsf = Utils.toFloat(item.getPrice_SqFt());
		if(pricePsf > 0) priceTxt = Utils.priceFormat(pricePsf) + " " + item.getPrice_SqFt_unit();
		holder.tv_psf.setText(priceTxt);
		holder.tv_lifeStyle.setText("Lifestyle" + "\n" + item.getLife_style());

		holder.tv_FloorCount.setText(item.getTotal_floor());

		if (item.isUser_favourite()) {
			holder.imageViewFav.setImageResource(R.drawable.favorite_filled);
		} else {
			holder.imageViewFav.setImageResource(R.drawable.favorite_outline_grey);
		}

		String floor = " " + item.getFlat_floor();
		String size = item.getWpcf_flat_size() + " " + item.getWpcf_flat_size_unit();

		final BMHApplication app = (BMHApplication) acti.getApplication();
		float intPrice = Utils.toFloat(item.getFlat_price());
		if(intPrice > 0) holder.tv_subSubTile.setText(app.getDecimalFormatedPrice(intPrice));
		else holder.tv_subSubTile.setText(item.getFlat_price());
		holder.tv_subTitle.setText(size + "\n " + "Floor :" + floor);

		try {
			if (!item.getImage_unit().equalsIgnoreCase("")) {
				Picasso.with(context)
						.load(UrlFactory.getShortImageByWidthUrl(BMHConstants.LIST_IMAGE_WIDTH, item.getImage_unit()))
						.placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE)
						.resize(BMHConstants.LIST_IMAGE_WIDTH, BMHConstants.LIST_IMAGE_WIDTH).into(holder.unitImage);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (item.getSold_status().equalsIgnoreCase("sold")) {
			holder.iv_reserved.setVisibility(View.VISIBLE);
		//	holder.tv_subSubTile.setText(" - ");
			holder.imageViewFav.setVisibility(View.GONE);
		} else {
			holder.iv_reserved.setVisibility(View.GONE);
			holder.imageViewFav.setVisibility(View.VISIBLE);
			/*if (acti.searchParams != null && acti.searchParams.containsKey("type")
					&& acti.searchParams.get("type").equalsIgnoreCase("E-Auction")) {
				holder.tv_subSubTile.setText(" - ");
			} else if (acti.searchParams != null && acti.searchParams.containsKey("type")
					&& acti.searchParams.get("type").equalsIgnoreCase("Rent")) {
				holder.tv_subSubTile.setText(" - ");
			} else {
				if(intPrice > 0) holder.tv_subSubTile.setText(Utils.priceFormat(intPrice));
				else holder.tv_subSubTile.setText(item.getFlat_price());
			}*/
		}

		holder.imageViewFav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				unitFavDetails = arrUnits.get(position);
				CommonLib commonLib = new CommonLib();
				commonLib.makeFavorite(holder.imageViewFav, ctx, app, unitFavDetails, UnitListAdapter.this);
			}
		});

		return convertView;

	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_subTitle;
		TextView tv_subSubTile;
		ImageView unitImage;
		ImageView iv_reserved;
		TextView tv_unitNo;
		TextView tv_price;
		TextView tv_sqft;
		TextView tv_Plc;
		TextView tv_psf;
		TextView tv_lifeStyle;
		TextView tv_priceBreckup;
		TextView tv_FloorCount;
		ImageView imageViewFav;
	}

}
