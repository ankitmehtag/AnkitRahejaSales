package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.VO.CityVO;
import com.sp.R;

import java.util.ArrayList;

public class CitiesListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	ArrayList<CityVO> arrCities;

	public CitiesListAdapter(Activity c, ArrayList<CityVO> arr) {
		context = c;
		mInflater = LayoutInflater.from(context);
		arrCities = arr;
	}

	@Override
	public int getCount() {
		return arrCities.size();
	}

	@Override
	public Object getItem(int position) {
		return arrCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_cities, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.textViewcityname);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.llright);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_name.setText(arrCities.get(position).getName());
		holder.ll.setVisibility(View.GONE);

		return convertView;

	}
	class ViewHolder {
		TextView tv_name;
		LinearLayout ll ;
	}
	
	public void setData(ArrayList<CityVO> arrCity){
		arrCities = arrCity;
	}

}
