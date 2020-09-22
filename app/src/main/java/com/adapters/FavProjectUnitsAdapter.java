package com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sp.R;

import org.json.JSONArray;
import org.json.JSONException;

public class FavProjectUnitsAdapter extends BaseAdapter {
	private Context context;
	private JSONArray unit;
	private LayoutInflater mInflater;

	public FavProjectUnitsAdapter(Context context, JSONArray unit) {
		this.context = context;
		this.unit = unit;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (unit == null) {
			return 0;
		}
		return unit.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		try {
			return unit.getJSONObject(arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_bhk_type, null);
			holder = new ViewHolder();
			holder.bhkType = (TextView) convertView.findViewById(R.id.bhk_type);
			holder.bhkSize = (TextView) convertView.findViewById(R.id.bhk_sqft);
			holder.bhkPrice = (TextView) convertView.findViewById(R.id.bhk_price);

			try {
				holder.bhkType.setText(unit.getJSONObject(position).getString("wpcf_flat_typology"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				String bhkSize = unit.getJSONObject(position).getString("area_range") + " " + unit.getJSONObject(position).getString("area_range_unit");
				holder.bhkSize.setText(bhkSize);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				holder.bhkPrice.setText(unit.getJSONObject(position).getString("price_range"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
		TextView bhkType;
		TextView bhkSize;
		TextView bhkPrice;
	}

}
