package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeIntents;
import com.sp.ProjectDetailActivity;
import com.sp.ProjectGalleryActivity;
import com.sp.R;
import com.sp.UnitDetailActivity;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.interfaces.GalleryCallback;

public class PropertyVideoFragment extends Fragment {

    private FragmentActivity fragmentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_video, container, false);
        ImageView img = view.findViewById(R.id.videoThumb);
        Bundle b = getArguments();
        final int pos = b.getInt("pos");
        final String url = b.getString("imgurl");

        Glide.with(getActivity())
                .load(UrlFactory.getThumbUrlViaId(url))
                .placeholder(BMHConstants.PLACE_HOLDER)
                // .centerCrop()
                .error(BMHConstants.NO_IMAGE)
                .into(img);
        int count = b.getInt("count");
//			String imageName = b.getString("imgname");
        if (fragmentActivity instanceof ProjectGalleryActivity) {
            TextView tvFragNo = (TextView) view.findViewById(R.id.tvFragNo);
//				TextView tvImageName = (TextView) view.findViewById(R.id.tvImageName);

            tvFragNo.setVisibility(View.VISIBLE);
//				tvImageName.setVisibility(View.VISIBLE);
            int n = pos + 1;
            tvFragNo.setText(n + "/" + count);
//				tvImageName.setText(imageName);


        }
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActivity instanceof ProjectDetailActivity | fragmentActivity instanceof UnitDetailActivity) {
                    GalleryCallback callBack = (GalleryCallback) fragmentActivity;
                    callBack.onPageClicked(pos, BMHConstants.TYPE_VIDEOS);

                } else if (fragmentActivity instanceof ProjectGalleryActivity) {
                    System.out.println("hh instance of gellary");
                    Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(fragmentActivity, url, true, false);
                    startActivity(intent);
                }
            }
        });
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
