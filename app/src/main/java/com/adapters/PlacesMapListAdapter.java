package com.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sp.R;
import com.VO.PlaceVo;

public class PlacesMapListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private ArrayList<PlaceVo> arrSaloons;
	
	public PlacesMapListAdapter(Activity c, ArrayList<PlaceVo> arr)
	{
		context = c;
		mInflater = LayoutInflater.from(context);
		arrSaloons = arr;
	}
	
	@Override
	public int getCount() {
		return arrSaloons.size();
	}

	@Override
	public Object getItem(int position) {
		return arrSaloons.get(position);
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
			holder.name = (TextView) convertView.findViewById(R.id.textViewcityname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.name.setText(arrSaloons.get(position).getName()+"");
		
		return convertView;
		
		
	}
	
	class ViewHolder {
		TextView name;
	}

}
