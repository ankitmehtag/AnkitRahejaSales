package com.pwn;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.ViewGroup.LayoutParams;

public class Slider {

	private ViewPager pager;
	private Context drawingView;
	private PagerAdapter adapter;
	private Timer timer;
	private int itemSize = 0;
	public int currentIndex = 0;

	public Slider(PagerAdapter adapter, Context drawingView) {
		this.drawingView = drawingView;
		this.adapter = adapter;
		this.itemSize = adapter.getCount();
		initPager();
	}

	private void initPager() {
		createPager();

		pager.setClipToPadding(false);
		pager.setPadding(80, 0, 80, 0);
		pager.setPageMargin(25);

		pager.setAdapter(adapter);
		pager.setCurrentItem(0);

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// SearchFragment.this.runOnUiThread(new Runnable() {
				// getActivity().
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						if (currentIndex < itemSize) {
							pager.setCurrentItem(currentIndex);
							currentIndex++;
						} else {
							currentIndex = 0;
							pager.setCurrentItem(currentIndex);
						}
					}
				});
			}
		}, 200, 3000);
	}

	public ViewPager getPager(){
		return pager;
	}
	protected void createPager(){
		pager = new ViewPager(drawingView);
//		XmlPullParserFactory factory = null;
//		try {
//			factory = XmlPullParserFactory.newInstance();
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        factory.setNamespaceAware(true);
//        XmlPullParser xpp = null;
//		try {
//			xpp = factory.newPullParser();
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        try {
//			xpp.setInput( new StringReader ( "<TextView android:id=\"@+id/project_view\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:layout_marginBottom=\"15dp\" android:layout_marginLeft=\"10dp\" android:layout_marginTop=\"20dp\" android:text=\"Featured Projects\" android:textColor=\"#000000\" android:textSize=\"24sp\" />" ) );
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		AttributeSet attributes = Xml.asAttributeSet(xpp);
//		
		pager.setLayoutParams(new LayoutParams(drawingView, null));
	}

	protected void runOnUiThread(Runnable runnable) {
		// TODO Auto-generated method stub

	}

}
