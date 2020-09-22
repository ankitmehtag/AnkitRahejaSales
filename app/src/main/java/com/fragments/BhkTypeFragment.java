package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.R;
import com.model.BhkUnitSpecification;

import java.io.Serializable;
import java.util.List;

public class BhkTypeFragment extends Fragment {


	public static Fragment newInstance(List<BhkUnitSpecification> modelArray) {
		BhkTypeFragment fragmentFirst = new BhkTypeFragment();
		Bundle args = new Bundle();
		args.putSerializable("obj", (Serializable) modelArray);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		LinearLayout mLinearLayout = new LinearLayout(getActivity());
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mLinearLayout.setLayoutParams(mLayoutParams);
		final List<BhkUnitSpecification> modelList = (List<BhkUnitSpecification>) this.getArguments().getSerializable("obj");
		if(modelList != null ){
			for(BhkUnitSpecification model : modelList) {
				if(model != null) {
					View bhkView = inflater.inflate(R.layout.row_bhk_type, null);
					TextView bhkType = (TextView) bhkView.findViewById(R.id.bhk_type);
					TextView bhkSize = (TextView) bhkView.findViewById(R.id.bhk_sqft);
					TextView bhkPrice = (TextView) bhkView.findViewById(R.id.bhk_price);
					String topology = model.getWpcf_flat_typology() != null ? model.getWpcf_flat_typology() : "NA";
					String areaRange = model.getArea_range() != null ? model.getArea_range() + " sqft" : "NA";
					String price = model.getPrice_range() != null ? model.getPrice_range() : "NA";
					bhkType.setText(topology);
					bhkSize.setText(areaRange);
					bhkPrice.setText(price);
					mLinearLayout.addView(bhkView);
				}
			}
		}
		return mLinearLayout;
	}

}
