package com.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.VO.UnitType;
import com.sp.R;
import com.sp.UnitTypeListActivity;
import com.utils.Utils;

import java.util.ArrayList;

public class DetailsUnitTypeAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	ArrayList<UnitType> arrUnits;
	private UnitTypeListActivity acti;

	public DetailsUnitTypeAdapter(UnitTypeListActivity c, ArrayList<UnitType> arr) {
		mInflater = LayoutInflater.from(c);
		arrUnits = arr;
		acti = c;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_unit_type, null);
			holder = new ViewHolder();
			holder.tvDisplayName = (TextView) convertView.findViewById(R.id.tvdisplayName);
			holder.tvType = (TextView) convertView.findViewById(R.id.tvBHKType);
			holder.tvNoOfUnits = (TextView) convertView.findViewById(R.id.tvNoOfUnits);
			holder.tvAreaRange = (TextView) convertView.findViewById(R.id.tvAreaRange);
			holder.tvPriceRange = (TextView) convertView.findViewById(R.id.tvPriceRange);
			holder.tvPricePerSqft = (TextView) convertView.findViewById(R.id.tvPricePerSqft);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UnitType item = arrUnits.get(position);
		holder.tvDisplayName.setText(item.getFlat_typology());
		
		if(item.getTotal_units().equalsIgnoreCase("1")){
			holder.tvType.setText(" - "+item.getTotal_types()+" Type");
		}else{
			holder.tvType.setText(" - "+item.getTotal_types()+" Types");
		}
		
		if(item.getTotal_units().equalsIgnoreCase("1")){
			holder.tvNoOfUnits.setText(item.getTotal_units()+" Unit");
		}else{
			holder.tvNoOfUnits.setText(item.getTotal_units()+" Units");
		}
		if(acti.map!=null && acti.map.containsKey("type") && acti.map.get("type").equalsIgnoreCase("E-Auction")){
			holder.tvPricePerSqft.setText(" - "+" Per Sq Ft");
			holder.tvPriceRange.setText(" - ");
		}else if(acti.map!=null && acti.map.containsKey("type") && acti.map.get("type").equalsIgnoreCase("Rent")){
			holder.tvPricePerSqft.setText(" - "+" Per Sq Ft");
			holder.tvPriceRange.setText(" - ");
		}else{
			String priceTxt = item.getPer_square_price_range() + " " + "Per Sq Ft";
			float price = Utils.toFloat(item.getPer_square_price_range());
			if(price > 0) priceTxt = Utils.priceFormat(price) + " " + "Per Sq Ft";
			holder.tvPricePerSqft.setText(priceTxt);
			holder.tvPriceRange.setText(item.getPrice_range());
		}
		
		holder.tvAreaRange.setText(item.getArea_range()+" Sq Fts");
		
		
		return convertView;

	}
	class ViewHolder {
		TextView tvDisplayName;
		TextView tvType;
		TextView tvNoOfUnits;
		TextView tvAreaRange;
		TextView tvPriceRange;
		TextView tvPricePerSqft;
	}
	
}
