package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.ProjectDetailActivity;
import com.sp.R;
import com.VO.UnitType;
import com.interfaces.FragmentClickListener;
import com.utils.Utils;

import java.util.HashMap;

public class UnitTypeFragment extends Fragment {


	public static UnitTypeFragment newInstance(UnitType model) {
		UnitTypeFragment fragmentFirst = new UnitTypeFragment();
		Bundle args = new Bundle();
		args.putSerializable("obj", model);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		final View convertView = inflater.inflate(R.layout.row_unit_type, container, false);
		TextView tvDisplayName = (TextView) convertView.findViewById(R.id.tvdisplayName);
		//TextView tvType = (TextView) convertView.findViewById(R.id.tvBHKType);
		TextView tvNoOfUnits = (TextView) convertView.findViewById(R.id.tvNoOfUnits);
		TextView tvAreaRange = (TextView) convertView.findViewById(R.id.tvAreaRange);
		LinearLayout parent = (LinearLayout)convertView.findViewById(R.id.parent);
		TextView tvPriceRange = (TextView) convertView.findViewById(R.id.tvPriceRange);
		TextView tvPricePerSqft = (TextView) convertView.findViewById(R.id.tvPricePerSqft);
		TextView tvPriceRange_title = (TextView) convertView.findViewById(R.id.tvPriceRange_title);
		TextView tvAreaRange_title = (TextView) convertView.findViewById(R.id.tvAreaRange_title);

		UnitType item = (UnitType) this.getArguments().getSerializable("obj");
		HashMap<String,String> map = (HashMap<String, String>) this.getArguments().getSerializable("map");
		StringBuilder displayName = new StringBuilder(item.getFlat_typology());
		if(item.getTotal_types() != null && !item.getTotal_types().isEmpty()){
			displayName.append(" - ");
			displayName.append(item.getTotal_types());
			if(item.getTotal_units().equalsIgnoreCase("1")){
				displayName.append(" Type");
			}else{
				displayName.append(" Types");
			}
		}
		if(item.getIsLotteryProject() != null && item.getIsLotteryProject().equalsIgnoreCase("1")){
			tvPriceRange_title.setText(getActivity().getString(R.string.price_only));
			tvAreaRange_title.setText(getActivity().getString(R.string.area_only));
		}else{
			tvPriceRange_title.setText(getActivity().getString(R.string.price_range));
			tvAreaRange_title.setText(getActivity().getString(R.string.area_range));
		}
		tvDisplayName.setText(displayName);
		if(item.getIsLotteryProject() != null && item.getIsLotteryProject().equalsIgnoreCase("1")) {
			tvNoOfUnits.setVisibility(View.GONE);
		}else{
			tvNoOfUnits.setVisibility(View.VISIBLE);
			if (item.getTotal_units().equalsIgnoreCase("1")) {
				tvNoOfUnits.setText(item.getTotal_units() + " Unit");
			} else {
				tvNoOfUnits.setText(item.getTotal_units() + " Units");
			}
		}
		if(map!=null && map.containsKey("type") && map.get("type").equalsIgnoreCase("E-Auction")){
			tvPricePerSqft.setText(" - "+" " + item.getPer_square_price_range_unit());
			tvPriceRange.setText(" - ");
		}else if(map!=null && map.containsKey("type") && map.get("type").equalsIgnoreCase("Rent")){
			tvPricePerSqft.setText(" - "+" " + item.getPer_square_price_range_unit());
			tvPriceRange.setText(" - ");
		}else{
			String psRange = item.getPer_square_price_range()+" "+item.getPer_square_price_range_unit();
			float psRangePrice = Utils.toFloat(item.getPer_square_price_range());
			if(psRangePrice > 0) psRange = Utils.priceFormat(psRangePrice) + " "+item.getPer_square_price_range_unit();
			tvPricePerSqft.setText(psRange);

			String priceTxt = item.getPrice_range();
			float price = Utils.toFloat(item.getPrice_range());
			if(price > 0) priceTxt = Utils.priceFormat(price);
			tvPriceRange.setText(getString(R.string.price_marker) + priceTxt);
		}
		tvAreaRange.setText(item.getArea_range()+" "+item.getStd_unit_size());

		convertView.setTag(R.integer.unit,item);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( getActivity() instanceof ProjectDetailActivity) {
					FragmentClickListener mFragmentClickListener = (FragmentClickListener) getActivity();
					mFragmentClickListener.myOnClickListener(UnitTypeFragment.this,v.getTag(R.integer.unit));
				}
			}
		});
		return convertView;
	}

}
