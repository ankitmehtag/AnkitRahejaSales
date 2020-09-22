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
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.model.SubLocationRespModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubLocationsListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	public ArrayList<SubLocationRespModel.SectorLists> sectorsList;
	private Typeface typeface;
	public String location = "";

	public SubLocationsListAdapter(Activity c, ArrayList<SubLocationRespModel.SectorLists> arr,String location) {
		context = c;
		mInflater = LayoutInflater.from(context);
		sectorsList = arr;
		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/regular.ttf");
		this.location = location;
	}

	public void setData(ArrayList<SubLocationRespModel.SectorLists> arrLocation) {

		this.sectorsList = arrLocation;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(sectorsList != null)
			return sectorsList.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		if(sectorsList != null)
			return sectorsList.get(position);
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
			holder.tv_header = (TextView)convertView.findViewById(R.id.tv_header);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_loc_list_name);
			holder.sublocation = (TextView)convertView.findViewById(R.id.tv_sublocation);
			holder.imgLoc = (ImageView) convertView.findViewById(R.id.imageViewLoc);
			holder.tvNoOfProj = (TextView) convertView.findViewById(R.id.tv_no_of_projects);
			holder.tv_avgPrice = (TextView) convertView.findViewById(R.id.tv_avg_price);
			holder.tvRating = (TextView) convertView.findViewById(R.id.tv_loc_rating);
			holder.tvReturnPrice = (TextView)convertView.findViewById(R.id.tvReturnPrice);

			holder.tvInfra = (TextView) convertView.findViewById(R.id.tv_infra);
			holder.tvNeeds = (TextView) convertView.findViewById(R.id.tv_needs);
			holder.tvLife = (TextView) convertView.findViewById(R.id.tv_lifestyle);
			//holder.tvReturns = (TextView) convertView.findViewById(R.id.textViewReturns);

			holder.tv_header.setTypeface(typeface);
			holder.tv_name.setTypeface(typeface);
			holder.tvNoOfProj.setTypeface(typeface);
			holder.tv_avgPrice.setTypeface(typeface);
			holder.tvRating.setTypeface(typeface);
			holder.tvInfra.setTypeface(typeface);
			holder.tvNeeds.setTypeface(typeface);
			holder.tvLife.setTypeface(typeface);
			//holder.tvReturns.setTypeface(typeface);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(sectorsList.get(position).getSector_name());
		if(location != null && location.length() > 0) {
			holder.sublocation.setText(location);
			holder.sublocation.setVisibility(View.VISIBLE);
		}
		if(position == 1){
			holder.tv_header.setVisibility(View.VISIBLE);
		}else{
			holder.tv_header.setVisibility(View.GONE);
		}
		holder.tv_avgPrice.setText(sectorsList.get(position).getAvgPSFLocation() + " " + sectorsList.get(position).getAvgPSFLocation_unit());
		holder.tvNoOfProj.setText("No. of Projects: " + sectorsList.get(position).getTotal_project());
		holder.tvRating.setText(sectorsList.get(position).getAvg_rating() + "");
		holder.tvReturnPrice.setText(sectorsList.get(position).getReturns() + "");
		holder.tvInfra.setText("Infra\n" + sectorsList.get(position).getInfra());
		holder.tvNeeds.setText("Needs\n" + sectorsList.get(position).getNeeds());
		holder.tvLife.setText("Lifestyle\n" + sectorsList.get(position).getLifeStyle());
		//holder.tvReturns.setText("Returns\n" + sectorsList.get(position).getReturns());

		if (sectorsList.size() > 0 && !sectorsList.get(position).getSector_image().equalsIgnoreCase("")) {
			String imgUrl = UrlFactory.getShortImageByWidthUrl(BMHConstants.LIST_IMAGE_WIDTH,
					sectorsList.get(position).getSector_image());
			Picasso.with(context).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER)
					.resize(BMHConstants.LIST_IMAGE_WIDTH, BMHConstants.LIST_IMAGE_WIDTH).error(BMHConstants.NO_IMAGE)
					.into(holder.imgLoc);
		}
		convertView.setTag(R.integer.locality_item, sectorsList.get(position));

		/*if(position == 1) {
			LinearLayout mLinearLayout = new LinearLayout(context);
			mLinearLayout.setOrientation(LinearLayout.VERTICAL);
			AbsListView.LayoutParams mLayoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
			mLinearLayout.setLayoutParams(mLayoutParams);
			TextView mTextView = new TextView(context);
			mTextView.setText("Nearby Sectors");
			mTextView.setTextColor(context.getResources().getColor(R.color.black));
			mTextView.setTextSize(20);
			mLinearLayout.addView(mTextView);
			mLinearLayout.addView(convertView);
			return mLinearLayout;
		}else {
			return convertView;
		}*/
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		TextView sublocation;
		TextView tv_avgPrice;
		TextView tvNoOfProj;
		TextView tvRating;
		ImageView imgLoc;
		TextView tvInfra;
		TextView tvNeeds;
		TextView tvLife;
		//TextView tvReturns;
		TextView tvReturnPrice;
		TextView tv_header;
	}

}
