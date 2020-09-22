package com.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.VO.UnitType;
import com.sp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitTypeListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	public ArrayList<UnitType> arrUnits;
//	private UnitTypeListActivity acti;
	public int selectedIndex = 0;
	HashMap<String, String> map;
    private View.OnClickListener deleteListener;
	private Activity act;

    
    public UnitTypeListAdapter(Activity c,HashMap<String, String> map, ArrayList<UnitType> arr) {
		mInflater = LayoutInflater.from(c);
		arrUnits = arr;
		this.map = map;
		act = c;
//		deleteListener = new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				onClickView(v, (int)v.getTag());
//			}
//		};
//		acti = c;
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
			holder.parent = (LinearLayout)convertView.findViewById(R.id.parent);
			holder.tvPricePerSqft = (TextView) convertView.findViewById(R.id.tvPricePerSqft);
			holder.tvPriceRange = (TextView) convertView.findViewById(R.id.tvPriceRange);
			holder.tvAreaRange = (TextView) convertView.findViewById(R.id.tvAreaRange);
			holder.tvPriceRange_title = (TextView) convertView.findViewById(R.id.tvPriceRange_title);
			holder.tvAreaRange_title = (TextView) convertView.findViewById(R.id.tvAreaRange_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UnitType item = arrUnits.get(position);
		holder.tvDisplayName.setText(item.getFlat_typology());
		if(item.getIsLotteryProject() != null && item.getIsLotteryProject().equalsIgnoreCase("1")){
			holder.tvPriceRange_title.setText(act.getString(R.string.price_only));
			holder.tvAreaRange_title.setText(act.getString(R.string.area_only));
		}else{
			holder.tvPriceRange_title.setText(act.getString(R.string.price_range));
			holder.tvAreaRange_title.setText(act.getString(R.string.area_range));
		}
		if(item.getTotal_units() != null && !item.getTotal_units().isEmpty()) {
			holder.tvType.setVisibility(View.VISIBLE);
			if (item.getTotal_units().equalsIgnoreCase("1")) {
				holder.tvType.setText(" - " + item.getTotal_types() + " Type");
			} else {
				holder.tvType.setText(" - " + item.getTotal_types() + " Types");
			}
		}else{
			holder.tvType.setVisibility(View.GONE);
		}
		if(item.getIsLotteryProject() != null && item.getIsLotteryProject().equalsIgnoreCase("1")){
			holder.tvNoOfUnits.setVisibility(View.GONE);
		}else {
			holder.tvNoOfUnits.setVisibility(View.VISIBLE);
			if (item.getTotal_units().equalsIgnoreCase("1")) {
				holder.tvNoOfUnits.setText(item.getTotal_units() + " Unit");
			} else {
				holder.tvNoOfUnits.setText(item.getTotal_units() + " Units");
			}
		}
		if(map!=null && map.containsKey("type") && map.get("type").equalsIgnoreCase("E-Auction")){
			holder.tvPricePerSqft.setText(" - "+" per sqft");
			holder.tvPriceRange.setText(" - ");
		}else if(map!=null && map.containsKey("type") && map.get("type").equalsIgnoreCase("Rent")){
			holder.tvPricePerSqft.setText(" - "+" per sqft");
			holder.tvPriceRange.setText(" - ");
		}else{
			holder.tvPricePerSqft.setText(item.getPer_square_price_range()+" per sqft");
			holder.tvPriceRange.setText(item.getPrice_range());
		}
		
		holder.tvAreaRange.setText(item.getArea_range()+" sqft");
		
//		holder.parent.setTag(position);
//		holder.parent.setOnClickListener(deleteListener);
		
		return convertView;

	}
	class ViewHolder {
		TextView tvDisplayName;
		TextView tvType;
		TextView tvNoOfUnits;
		LinearLayout parent;
		TextView tvAreaRange;
		TextView tvPriceRange;
		TextView tvAreaRange_title;
		TextView tvPriceRange_title;
		TextView tvPricePerSqft;
	}
	
//    protected abstract void onClickView(View v, int pos);
	
}
