package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sp.R;

public class SplashFragment extends Fragment {

	private FragmentActivity fragmentActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.help_slider,container, false);
		
		ImageView img = (ImageView) view.findViewById(R.id.imageViewHelp);
		Bundle b = getArguments();
		final int pos = b.getInt("pos");
		if(pos == 0)
			img.setImageResource(R.drawable.img1);
		else if (pos == 1)
			img.setImageResource(R.drawable.img2);
		else if (pos == 2)
			img.setImageResource(R.drawable.img3);
//		else if (pos == 3)
//			img.setImageResource(R.drawable.img4);
//		else if (pos == 4)
//			img.setImageResource(R.drawable.img5);
		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public FragmentActivity getFragmentActivity() {
		return fragmentActivity;
	}

	public void setFragmentActivity(FragmentActivity fragmentActivity) {
		this.fragmentActivity = fragmentActivity;
	}
	
}
