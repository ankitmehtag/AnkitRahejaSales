package com.galleryview;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.R;
import com.VO.PhotoVO;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.ContentLoader;
import com.helper.UrlFactory;
import com.squareup.picasso.Picasso;

public class GalleryDemoActivity extends Activity {

	protected static final Intent DownloadImageTask = null;
	private HorizontalScrollView hsv;
	private Button btnPrevoius, btnNext;
	private TextView btn_virtual, btn_tour, btn_const,	btn_video, btn_photo;
	String propertyId;
	
	
	private LinearLayout llLineInfra, llLineNeeds, llLineLife, llLinePrice, llLineReturns,llLinePhoto,llLineVideo,
	                    llLineConstion, llLineVirtual,llLineTour ;

	ImageView selected_imageview;

	String media = "image";

	ArrayList<String> map = new ArrayList<String>();
	protected String id;
	private boolean isOneFound = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_view_main);

		ImageView back_img = (ImageView) findViewById(R.id.image_back_button);
		back_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		selected_imageview = (ImageView) findViewById(R.id.selected_imageview);

		
		
		
//		LinearLayout llPhoto = (LinearLayout) findViewById(R.id.llPhoto);
//		LinearLayout llVideos = (LinearLayout) findViewById(R.id.llVideos);
//		LinearLayout llConstion = (LinearLayout) findViewById(R.id.llConstion);
//		LinearLayout llVirtual = (LinearLayout) findViewById(R.id.llVirtual);
//		LinearLayout llTour = (LinearLayout) findViewById(R.id.llTour);

		btn_virtual = (TextView) findViewById(R.id.btn_virtual);
		btn_const = (TextView) findViewById(R.id.btn_const);
		btn_video = (TextView) findViewById(R.id.btn_video);
		btn_photo = (TextView) findViewById(R.id.btn_photo);
		btn_tour = (TextView) findViewById(R.id.btn_tour);
		
//		llLinePhoto = (LinearLayout) findViewById(R.id.llUnderlinePhoto);
//		llLineVideo = (LinearLayout) findViewById(R.id.llUnderlineVideos);
//		llLineConstion = (LinearLayout) findViewById(R.id.llUnderlineConstraction);
//		llLineVirtual = (LinearLayout) findViewById(R.id.llUnderlineVirtual);
//		llLineTour = (LinearLayout) findViewById(R.id.llUnderlineTour);

//		llPhoto.setOnClickListener(StatusPinsClickListner);
//		llVideos.setOnClickListener(StatusPinsClickListner);
//		llConstion.setOnClickListener(StatusPinsClickListner);
//		llVirtual.setOnClickListener(StatusPinsClickListner);
//		llTour.setOnClickListener(StatusPinsClickListner);


		
		
		

		btn_tour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		btn_const.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
//				 if (!arg0.isSelected()) {
//					 arg0.setSelected(true);
//					}

				media = "under_image";

				try {
					Intent intent = getIntent();
					final String id = intent.getStringExtra("id");
					new DownloadImageTask().execute(id);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		btn_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 if (!arg0.isSelected()) {

					 arg0.setSelected(true);

					}
				media = "image";

				try {
					Intent intent = getIntent();
					final String id = intent.getStringExtra("id");
					new DownloadImageTask().execute(id);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btn_video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				 if (!arg0.isSelected()) {

					 arg0.setSelected(true);

					}

				media = "video";

				try {

					Intent intent = getIntent();
					final String id = intent.getStringExtra("id");
					new DownloadImageTask().execute(id);

//					btn_video.setTextColor((R.color.selected_tv));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private Context getVideos() {

				return null;
			}
		});

		btn_virtual.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		// =============================================== Horizontal scroll
		hsv = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);

		btnPrevoius = (Button) findViewById(R.id.btnPrevoius);
		btnNext = (Button) findViewById(R.id.btnNext);

		btnPrevoius = (Button) findViewById(R.id.btnPrevoius);
		btnPrevoius.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				hsv.scrollTo((int) hsv.getScrollX() - 400,(int) hsv.getScrollY());

			}
		});

		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				hsv.scrollTo((int) hsv.getScrollX() + 400,(int) hsv.getScrollY());

			}
		});

		try {
			Intent intent = getIntent();
			final String id = intent.getStringExtra("id");
			new DownloadImageTask().execute(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ==================================================
	}
	
	//========================  Add New ClickListner
	
//	OnClickListener StatusPinsClickListner = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			Resources r = getResources();
//			switch (v.getId()) {
//			case R.id.llPhoto:
//				llLinePhoto.setBackgroundColor(r.getColor(R.color.selected_underline));
//				llLineVideo.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineConstion.setBackgroundColor(r .getColor(R.color.unselected_underline));
//				llLineVirtual.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineTour.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//				btn_photo.setTextColor(r.getColor(R.color.selected_tv));
//				btn_const.setTextColor(r.getColor(R.color.unselected_tv));
//				btn_video.setTextColor(r.getColor(R.color.unselected_tv));
//				btn_virtual.setTextColor(r.getColor(R.color.unselected_tv));
//				btn_tour.setTextColor(r.getColor(R.color.unselected_tv));
//
//				media = "image";
//
//				try {
//					Intent intent = getIntent();
//					final String id = intent.getStringExtra("id");
//					new DownloadImageTask().execute(id);
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				// changePolygonColor(INFRA);
//
//				break;
//			case R.id.llVideos:
//				
//					if (isOneFound) {
//						
//						
//						llLinePhoto.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineVideo.setBackgroundColor(r.getColor(R.color.selected_underline));
//						llLineConstion.setBackgroundColor(r.getColor(R.color.selected_underline));
//						llLineVirtual.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineTour.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						btn_photo.setTextColor(r .getColor(R.color.unselected_tv));
//						btn_video.setTextColor(r.getColor(R.color.selected_tv));
//						btn_const.setTextColor(r.getColor(R.color.unselected_tv));
//						btn_virtual.setTextColor(r.getColor(R.color.unselected_tv));
//						btn_tour.setTextColor(r.getColor(R.color.unselected_tv));
//						
//						
//						
//						media = "video";
//
//						try {
//
//							Intent intent = getIntent();
//							final String id = intent.getStringExtra("id");
//							new DownloadImageTask().execute(id);
//
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					
//						
//					} 
//					break;
//				}
//		}
//
//	};

	//=====================================================

	protected static Context getString(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, String> {

		private OnClickListener clickListener;

		protected String doInBackground(String... urls) {
			try {

				map.clear();

				if (media.equals("image")) {

					getImages(urls[0]);
				} else {
					getVideos(urls[0]);
				}

			} catch (BMHException e) {
				e.printStackTrace();
			}
			return "asfd";
		}

		protected void onPostExecute(String result) {
			LinearLayout llGALLERY = (LinearLayout) findViewById(R.id.llGALLERY);

			llGALLERY.removeAllViews();

			for (int i = 0; i < map.size(); i++) {
				View v = getLayoutInflater().inflate(R.layout.gallery_item,
						null);
				final ImageView tv = (ImageView) v.findViewById(R.id.tvGallery);
				Picasso.with(GalleryDemoActivity.this).load(map.get(i))
						.placeholder(BMHConstants.PLACE_HOLDER)
						.error(BMHConstants.NO_IMAGE).into(tv);
				// tv.setTag(i);
				tv.setContentDescription(String.valueOf(i));

				Picasso.with(GalleryDemoActivity.this).load(map.get(0))
						.placeholder(BMHConstants.PLACE_HOLDER)
						.error(BMHConstants.NO_IMAGE).into(selected_imageview);

				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						CharSequence aaa = tv.getContentDescription();
						String sss = String.valueOf(aaa);
						Picasso.with(GalleryDemoActivity.this)
								.load(map.get(Integer.valueOf(sss)))
								.placeholder(BMHConstants.PLACE_HOLDER)
								.error(BMHConstants.NO_IMAGE)
								.into(selected_imageview);
					}
				});
				llGALLERY.addView(v);

			}

		}

	}

	public ArrayList<String> getImages(String projId) throws BMHException {
		PhotoVO vo = null;

		String url = UrlFactory.getGelleryImage();
		String param = "id=" + projId;
		String response = ContentLoader.getJsonUsingPost(url, param);
		System.out.println("hs serverHit= " + url);
		System.out.println("hs params= " + param);
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);

			if (jsonObj != null) {
				map.clear();
				JSONArray arrjsonGellary = jsonObj.optJSONArray("data");

				for (int i = 0; i < arrjsonGellary.length(); i++) {
					JSONObject obj1 = arrjsonGellary.optJSONObject(i);

					map.add(obj1.getString("url"));
				}

				System.out.println("jsonObj   Images  :   "
						+ jsonObj.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// ============================================== Video

	public ArrayList<String> getVideos(String projId) throws BMHException {
		PhotoVO vo = null;

		String url = UrlFactory.getGelleryVideo();
		String param = "id=" + projId;
		String response = ContentLoader.getJsonUsingPost(url, param);
		System.out.println("hs serverHit= " + url);
		System.out.println("hs params= " + param);
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj != null) {
				map.clear();
				JSONArray arrjsonGellary = jsonObj.optJSONArray("media_video");

				for (int i = 0; i < arrjsonGellary.length(); i++) {
					JSONObject obj1 = arrjsonGellary.optJSONObject(i);

					map.add(obj1.getString("url"));
				}

				System.out
						.println("jsonObj   Video  :   " + jsonObj.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// ============================= Under Constraction

	public ArrayList<String> getUnderConstraction(String projId)
			throws BMHException {
		PhotoVO vo = null;

		String url = UrlFactory.getUnderImage();
		String param = "id=" + projId;
		String response = ContentLoader.getJsonUsingPost(url, param);
		System.out.println("hs serverHit= " + url);
		System.out.println("hs params= " + param);
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj != null) {
				map.clear();
				JSONArray arrjsonGellary = jsonObj
						.optJSONArray("under_construction");

				for (int i = 0; i < arrjsonGellary.length(); i++) {
					JSONObject obj1 = arrjsonGellary.optJSONObject(i);

					map.add(obj1.getString("url"));
				}

				System.out.println("jsonObj   under_image  :   "
						+ jsonObj.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}