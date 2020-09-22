package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sp.R;
import com.interfaces.FragmentClickListener;
import com.model.HotProjDataModel;

public class HotProjFragment extends Fragment {


	public static Fragment newInstance(HotProjDataModel model) {
		HotProjFragment fragmentFirst = new HotProjFragment();
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
		final View mView = inflater.inflate(R.layout.hot_proj_item_view, container, false);
		final HotProjDataModel model = (HotProjDataModel) this.getArguments().getSerializable("obj");
		final Button btn_hot_proj_title = (Button)mView.findViewById(R.id.btn_hot_proj_title);
		final ImageView iv_hot_proj = (ImageView)mView.findViewById(R.id.iv_hot_proj);
		iv_hot_proj.setImageResource(model.getImgRes());
		btn_hot_proj_title.setText(model.getProjName());
		mView.setTag(R.integer.hotProjModel,model);
		btn_hot_proj_title.setTag(R.integer.hotProjModel,model);
		btn_hot_proj_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( getParentFragment() instanceof FragmentClickListener ) {
					FragmentClickListener mFragmentClickListener = (FragmentClickListener) getParentFragment();
					mFragmentClickListener.myOnClickListener(HotProjFragment.this,v.getTag(R.integer.hotProjModel));
				}
			}
		});
		return mView;
	}

}
