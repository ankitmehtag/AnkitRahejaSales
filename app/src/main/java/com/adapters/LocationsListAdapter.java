package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.R;
import com.VO.LocationVO;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.squareup.picasso.Picasso;
import com.utils.Utils;

import java.util.ArrayList;

public class LocationsListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	public ArrayList<LocationVO> arrLocation;

	Typeface fond;

	public LocationsListAdapter(Activity c, ArrayList<LocationVO> arr) {
		context = c;
		mInflater = LayoutInflater.from(context);
		arrLocation = arr;
		fond = Typeface.createFromAsset(context.getAssets(), "fonts/regular.ttf");
	}

	public void setData(ArrayList<LocationVO> arrLocation) {

		this.arrLocation = arrLocation;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(arrLocation != null)
			return arrLocation.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		if(arrLocation != null)
			return arrLocation.get(position);
		else return  null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_locations_list, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_loc_list_name);
			holder.imgLoc = (ImageView) convertView.findViewById(R.id.imageViewLoc);
			holder.tvNoOfProj = (TextView) convertView.findViewById(R.id.tv_no_of_projects);
			holder.tv_avgPrice = (TextView) convertView.findViewById(R.id.tv_avg_price);
			holder.tvRating = (TextView) convertView.findViewById(R.id.tv_loc_rating);
			holder.tvReturnPrice = (TextView)convertView.findViewById(R.id.tvReturnPrice);

			holder.tvInfra = (TextView) convertView.findViewById(R.id.tv_infra);
			holder.tvNeeds = (TextView) convertView.findViewById(R.id.tv_needs);
			holder.tvLife = (TextView) convertView.findViewById(R.id.tv_lifestyle);

			holder.tv_name.setTypeface(fond);
			holder.tvNoOfProj.setTypeface(fond);
			holder.tv_avgPrice.setTypeface(fond);
			holder.tvRating.setTypeface(fond);
			holder.tvInfra.setTypeface(fond);
			holder.tvNeeds.setTypeface(fond);
			holder.tvLife.setTypeface(fond);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(arrLocation.get(position).getName());
		String avgPriceStr = arrLocation.get(position).getAvgPsfLocation();
		float avgPrice = Utils.toFloat(avgPriceStr);
		if(avgPrice > 0) avgPriceStr = Utils.priceFormat(avgPrice);
		holder.tv_avgPrice.setText(avgPriceStr + " " + arrLocation.get(position).getAvgPsfLocation_unit());
		holder.tvNoOfProj.setText("No. of Projects: " + arrLocation.get(position).getTotal_projects());
		holder.tvRating.setText(arrLocation.get(position).getAvg_rating() + "");
		holder.tvReturnPrice.setText(arrLocation.get(position).getReturns() + "");
		holder.tvInfra.setText("Infra\n" + arrLocation.get(position).getInfra());
		holder.tvNeeds.setText("Needs\n" + arrLocation.get(position).getNeeds());
		holder.tvLife.setText("Lifestyle\n" + arrLocation.get(position).getLifeStyle());

		if (arrLocation.size() > 0 && !arrLocation.get(position).getLocationImage().equalsIgnoreCase("")) {
			String imgUrl = UrlFactory.getShortImageByWidthUrl(BMHConstants.LIST_IMAGE_WIDTH,
					arrLocation.get(position).getLocationImage());
			Picasso.with(context).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER)
					.resize(BMHConstants.LIST_IMAGE_WIDTH, BMHConstants.LIST_IMAGE_WIDTH).error(BMHConstants.NO_IMAGE)
					.into(holder.imgLoc);
		}
		convertView.setTag(R.integer.locality_item,arrLocation.get(position));
		return convertView;

	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_avgPrice;
		TextView tvNoOfProj;
		TextView tvRating;
		ImageView imgLoc;
		TextView tvInfra;
		TextView tvNeeds;
		TextView tvLife;
		TextView tvReturnPrice;
	}

}
