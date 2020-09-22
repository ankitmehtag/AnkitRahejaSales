package com.sp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.VO.MediaGellaryVO;
import com.adapters.GalleryFragmentAdapter;
import com.helper.BMHConstants;
import com.interfaces.GalleryCallback;
import com.views.ExtendedViewPager;

import java.util.ArrayList;

public class ProjectGalleryActivity extends FragmentActivity implements GalleryCallback {

    ArrayList<MediaGellaryVO> arrImages;
    String projName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_property_gallery);

        arrImages = (ArrayList<MediaGellaryVO>) getIntent().getSerializableExtra("arrImages");
        int pos = getIntent().getIntExtra("pos", -1);
        projName = getIntent().getStringExtra("pdfname");

        ExtendedViewPager pager = (ExtendedViewPager) findViewById(R.id.viewPagerGallery);
        GalleryFragmentAdapter adapter = new GalleryFragmentAdapter(this, getSupportFragmentManager(), arrImages);
        pager.setAdapter(adapter);
        if (pos != -1) {
            pager.setCurrentItem(pos);
        }


        ImageView back_img = (ImageView) findViewById(R.id.image_back);
        back_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }


    @Override
    public void onPageClicked(int pos, int fileType) {
        if (fileType == BMHConstants.TYPE_PDF) {
            String pdfurl = arrImages.get(pos).getUrl();
            Intent service = new Intent(this, DownloadService.class);
            service.putExtra("pdfurl", pdfurl);
            service.putExtra("pdfname", projName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(service);
            } else {
                startService(service);
            }
        }
    }


//	private int getHeight(){
//		int measuredHeight = 0;
//		Point size = new Point();
//		WindowManager w = getWindowManager();
//
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
//		{
//		    w.getDefaultDisplay().getSize(size);
//		    measuredHeight = size.y;
//		}
//		else
//		{
//		    Display d = w.getDefaultDisplay();
//		    measuredHeight = d.getHeight();
//		}
//		return measuredHeight;
//	}

}
