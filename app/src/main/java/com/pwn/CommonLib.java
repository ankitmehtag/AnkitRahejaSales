package com.pwn;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.sp.BMHApplication;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.LoginActivity;
import com.sp.R;
import com.VO.BaseVO;
import com.VO.NewLaunch;
import com.VO.ProjectVO;
import com.VO.ProjectsVO;
import com.VO.PropertyCaraouselVO;
import com.VO.PropertyVO;
import com.VO.UnitCaraouselVO;
import com.VO.UnitDetailVO;
import com.adapters.FeaturedProjFragmentAdapter;
import com.adapters.NewLaunchListAdapter;
import com.adapters.PropertyListAdapter;
import com.adapters.UnitCrouselPagerAdapter;
import com.adapters.UnitListAdapter;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.model.PropertyModel;

public class CommonLib {

	private ImageView fav;
	private PropertyCaraouselVO property;
	private ProjectVO propertyVO;
    private NewLaunch favProperty;
	private UnitDetailVO unitDetailVO;
	private Activity ctx;
	private BMHApplication app;
	private PropertyListAdapter adapter;
	
	private NewLaunchListAdapter favdapter;
	private String userid = "";
	private FeaturedProjFragmentAdapter featuredAdapter;
	public static final int LOGIN_FOR_FAV = 589;

	private ProjectsVO projectvo;
	private PropertyVO propertyVOYahiNamBachaHai;
	private UnitDetailVO unitDetailVOFix;
	private UnitCaraouselVO unit;
	private UnitCrouselPagerAdapter unitAdapter;

	private UnitListAdapter adapterUnit;

	public CommonLib() {

	}

	public void makeFavorite(ImageView fav_, Activity ctx_, BMHApplication app_, PropertyCaraouselVO property_,
			PropertyListAdapter adapter_) {
		this.fav = fav_;
		this.property = property_;
		this.ctx = ctx_;
		this.app = app_;
		this.adapter = adapter_;

		if (ctx == null) {
			System.out.println("Failed: No Activity found");
			return;
		}
		if (fav == null) {
			Toast.makeText(ctx, "Failed: No View Found", Toast.LENGTH_SHORT).show();
			return;
		}

		if (property == null) {
			Toast.makeText(ctx, "Failed: No Project Id Found", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		} catch (Exception e) {
			System.out.println("user is not log in");
		}
		System.out.println("UserId " + userid);
		if (userid == "") {
			Intent i = new Intent(ctx, LoginActivity.class);
			i.putExtra("propertyId", property.getId());
			app.saveIntoPrefs("TEMP_JUGAD", property.getId());
			ctx.startActivityForResult(i, LOGIN_FOR_FAV);
			return;
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO vo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast("No Network");
				} else {
					if (vo.isSuccess()) {
						// ImageView fav = (ImageView)
						// llFav.findViewById(R.id.imageViewFav);
						System.out.println("Property " + property.getId());
						String message = "";
						if (property.isUser_favourite()) {
							property.setUser_favourite(false);
							fav.setImageResource(R.drawable.favorite_outline_grey);
						} else {
							property.setUser_favourite(true);
							fav.setImageResource(R.drawable.favorite_filled);
						}
						adapter.notifyDataSetChanged();
						// Toast.makeText(ctx, vo.getMessage(),
						// Toast.LENGTH_SHORT).show();
						// app.showAppMessage(ctx, vo.getMessage());
					} else {
						showToast(vo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				System.out.println("userid " + userid + " propertyid" + property.getId());
				try {
					vo = model.Favorite(userid, property.getId(), "property");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.execute("");

	}

	// for fragment
	public void makeFavorite(ImageView fav_, Activity ctx_, BMHApplication app_, ProjectVO property_,
			FeaturedProjFragmentAdapter adapter_) {
		this.fav = fav_;
		this.propertyVO = property_;
		this.ctx = ctx_;
		this.app = app_;
		this.featuredAdapter = adapter_;
		if (ctx == null) {
			System.out.println("Failed: No Activity found");
			return;
		}
		if (fav == null) {
			Toast.makeText(ctx, "Failed: No View Found", Toast.LENGTH_SHORT).show();
			return;
		}

		if (propertyVO == null) {
			Toast.makeText(ctx, "Failed: No Project Id Found", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		} catch (Exception e) {
			System.out.println("user is not log in");
		}
		System.out.println("UserId " + userid);
		if (userid == "") {
			Intent i = new Intent(ctx, LoginActivity.class);
			i.putExtra("propertyId", propertyVO.getId());
			app.saveIntoPrefs("TEMP_JUGAD", propertyVO.getId());
			ctx.startActivityForResult(i, LOGIN_FOR_FAV);
			return;
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO vo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast("No Network");
				} else {
					if (vo.isSuccess()) {
						// ImageView fav = (ImageView)
						// llFav.findViewById(R.id.imageViewFav);
						System.out.println("Property " + propertyVO.getId());
						String message = "";
						if (propertyVO.isUser_favourite()) {
							propertyVO.setUser_favourite(false);
							fav.setImageResource(R.drawable.favorite_outline);
						} else {
							propertyVO.setUser_favourite(true);
							fav.setImageResource(R.drawable.favorite_filled);
						}
						featuredAdapter.notifyDataSetChanged();
						// Toast.makeText(ctx, vo.getMessage(),
						// Toast.LENGTH_SHORT).show();
						// app.showAppMessage(ctx, vo.getMessage());
					} else {
						showToast(vo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				System.out.println("userid " + userid + " propertyid" + propertyVO.getId());
				try {
					vo = model.Favorite(userid, propertyVO.getId(), "property");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.execute("");

	}

	// -------------------- unit fav

	public void makeFavorite(ImageView fav_, Activity ctx_, BMHApplication app_, UnitCaraouselVO unit_,
			UnitCrouselPagerAdapter adapter_) {
		this.fav = fav_;
		this.unit = unit_;
		this.ctx = ctx_;
		this.app = app_;
		this.unitAdapter = adapter_;

		if (ctx == null) {
			System.out.println("Failed: No Activity found");
			return;
		}
		if (fav == null) {
			Toast.makeText(ctx, "Failed: No View Found", Toast.LENGTH_SHORT).show();
			return;
		}

		if (unit == null) {
			Toast.makeText(ctx, "Failed: No Project Id Found", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		} catch (Exception e) {
			System.out.println("user is not log in");
		}
		System.out.println("UserId " + userid);
		if (userid == "") {
			Intent i = new Intent(ctx, LoginActivity.class);
			i.putExtra("unitId", unit.getUnit_id());
			app.saveIntoPrefs("TEMP_JUGAD", unit.getUnit_id());
			ctx.startActivityForResult(i, LOGIN_FOR_FAV);
			return;
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO vo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast( "No Network");
				} else {
					if (vo.isSuccess()) {
						// ImageView fav = (ImageView)
						// llFav.findViewById(R.id.imageViewFav);
						System.out.println("Unit " + unit.getUnit_id());
						String message = "";
						if (unit.isUser_favourite()) {
							unit.setUser_favourite(false);
							fav.setImageResource(R.drawable.favorite_outline_grey);
						} else {
							unit.setUser_favourite(true);
							fav.setImageResource(R.drawable.favorite_filled);
						}
						unitAdapter.notifyDataSetChanged();
						// Toast.makeText(ctx, vo.getMessage(),
						// Toast.LENGTH_SHORT).show();
						// app.showAppMessage(ctx, vo.getMessage());
					} else {
						showToast( vo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				System.out.println("userid " + userid + " unitid" + unit.getUnit_id());
				try {
					vo = model.Favorite(userid, unit.getUnit_id(), "unit");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.execute("");

	}

	public void makeFavorite(ImageView fav_, Activity ctx_, BMHApplication app_, PropertyVO propertyVO_) {
		this.fav = fav_;
		this.propertyVOYahiNamBachaHai = propertyVO_;
		this.ctx = ctx_;
		this.app = app_;

		if (ctx == null) {
			System.out.println("Failed: No Activity found");
			return;
		}
		if (fav == null) {
			Toast.makeText(ctx, "Failed: No View Found", Toast.LENGTH_SHORT).show();
			return;
		}

		if (propertyVOYahiNamBachaHai == null) {
			Toast.makeText(ctx, "Failed: No Project Id Found", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		} catch (Exception e) {
			System.out.println("user is not log in");
		}
		System.out.println("UserId " + userid);
		if (userid == "") {
			Intent i = new Intent(ctx, LoginActivity.class);
			i.putExtra("propertyId", propertyVOYahiNamBachaHai.getId());
			app.saveIntoPrefs("TEMP_JUGAD", propertyVOYahiNamBachaHai.getId());
			ctx.startActivityForResult(i, LOGIN_FOR_FAV);
			return;
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO vo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast("No Network");
				} else {
					if (vo.isSuccess()) {
						// ImageView fav = (ImageView)
						// llFav.findViewById(R.id.imageViewFav);
						System.out.println("Property " + propertyVOYahiNamBachaHai.getId());
						String message = "";
						if (propertyVOYahiNamBachaHai.isUser_favourite()) {
							propertyVOYahiNamBachaHai.setUser_favourite(false);
							fav.setImageResource(R.drawable.favorite_outline);
						} else {
							propertyVOYahiNamBachaHai.setUser_favourite(true);
							fav.setImageResource(R.drawable.favorite_filled);
						}
						// Toast.makeText(ctx, vo.getMessage(),
						// Toast.LENGTH_SHORT).show();
						// app.showAppMessage(ctx, vo.getMessage());
					} else {
						showToast(vo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				System.out.println("userid " + userid + " propertyid" + propertyVOYahiNamBachaHai.getId());
				try {
					vo = model.Favorite(userid, propertyVOYahiNamBachaHai.getId(), "property");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.execute("");

	}

	// unit details
	public void makeFavorite(ImageView fav_, Activity ctx_, BMHApplication app_, UnitDetailVO unitDetailVO) {
		this.fav = fav_;
		this.unitDetailVOFix = unitDetailVO;
		this.ctx = ctx_;
		this.app = app_;

		if (ctx == null) {
			System.out.println("Failed: No Activity found");
			return;
		}
		if (fav == null) {
			Toast.makeText(ctx, "Failed: No View Found", Toast.LENGTH_SHORT).show();
			return;
		}

		if (unitDetailVOFix == null) {
			Toast.makeText(ctx, "Failed: No Project Id Found", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		} catch (Exception e) {
			System.out.println("user is not log in");
		}
		System.out.println("UserId " + userid);
		if (userid == "") {
			Intent i = new Intent(ctx, LoginActivity.class);
			i.putExtra("propertyId", unitDetailVOFix.getId());
			app.saveIntoPrefs("TEMP_JUGAD", unitDetailVOFix.getId());
			ctx.startActivityForResult(i, LOGIN_FOR_FAV);
			return;
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO vo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast("No Network");
				} else {
					if (vo.isSuccess()) {
						// ImageView fav = (ImageView)
						// llFav.findViewById(R.id.imageViewFav);
						System.out.println("unit " + unitDetailVOFix.getId());
						String message = "";
						if (unitDetailVOFix.isUser_favourite()) {
							unitDetailVOFix.setUser_favourite(false);
							fav.setImageResource(R.drawable.favorite_outline);
						} else {
							unitDetailVOFix.setUser_favourite(true);
							fav.setImageResource(R.drawable.favorite_filled);
						}
						// Toast.makeText(ctx, vo.getMessage(),
						// Toast.LENGTH_SHORT).show();
						// app.showAppMessage(ctx, vo.getMessage());
					} else {
						showToast( vo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				System.out.println("userid " + userid + " propertyId" + unitDetailVOFix.getId());
				try {
					vo = model.Favorite(userid, unitDetailVOFix.getId(), "unit");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.execute("");

	}

	// ---------------------- Unit List Fav

	public void makeFavorite(ImageView fav_, Activity ctx_, BMHApplication app_, UnitCaraouselVO unit_,
			UnitListAdapter adapter_Unit) {
		this.fav = fav_;
		this.unit = unit_;
		this.ctx = ctx_;
		this.app = app_;
		this.adapterUnit = adapter_Unit;

		if (ctx == null) {
			System.out.println("Failed: No Activity found");
			return;
		}
		if (fav == null) {
			Toast.makeText(ctx, "Failed: No View Found", Toast.LENGTH_SHORT).show();
			return;
		}

		if (unit == null) {
			Toast.makeText(ctx, "Failed: No Project Id Found", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		} catch (Exception e) {
			System.out.println("user is not log in");
		}
		System.out.println("UserId " + userid);
		if (userid == "") {
			Intent i = new Intent(ctx, LoginActivity.class);
			i.putExtra("unitId", unit.getUnit_id());
			app.saveIntoPrefs("TEMP_JUGAD", unit.getUnit_id());
			ctx.startActivityForResult(i, LOGIN_FOR_FAV);
			return;
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO vo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast( "No Network");
				} else {
					if (vo.isSuccess()) {
						// ImageView fav = (ImageView)
						// llFav.findViewById(R.id.imageViewFav);
						System.out.println("Unit " + unit.getUnit_id());
						String message = "";
						if (unit.isUser_favourite()) {
							unit.setUser_favourite(false);
							fav.setImageResource(R.drawable.favorite_outline_grey);
						} else {
							unit.setUser_favourite(true);
							fav.setImageResource(R.drawable.favorite_filled);
						}
						adapterUnit.notifyDataSetChanged();
						// Toast.makeText(ctx, vo.getMessage(),
						// Toast.LENGTH_SHORT).show();
						// app.showAppMessage(ctx, vo.getMessage());
					} else {
						showToast(vo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				System.out.println("userid " + userid + " unitid" + unit.getUnit_id());
				try {
					vo = model.Favorite(userid, unit.getUnit_id(), "unit");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.execute("");

	}

	
	//------------------  Favorite page 
	public void makeFavorite(ImageView fav_, Activity ctx_, BMHApplication app_,
			 NewLaunchListAdapter adapter_, NewLaunch property_) {
		
		this.fav = fav_;
		this.favProperty = property_;
		this.ctx = ctx_;
		this.app = app_;
		this.favdapter = adapter_;

		if (ctx == null) {
			System.out.println("Failed: No Activity found");
			return;
		}
		if (fav == null) {
			Toast.makeText(ctx, "Failed: No View Found", Toast.LENGTH_SHORT).show();
			return;
		}

		if (property == null) {
			Toast.makeText(ctx, "Failed: No Project Id Found", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		} catch (Exception e) {
			System.out.println("user is not log in");
		}
		System.out.println("UserId " + userid);
		if (userid == "") {
			Intent i = new Intent(ctx, LoginActivity.class);
			i.putExtra("propertyId", property.getId());
			app.saveIntoPrefs("TEMP_JUGAD", property.getId());
			ctx.startActivityForResult(i, LOGIN_FOR_FAV);
			return;
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO vo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast("No Network");
				} else {
					if (vo.isSuccess()) {
						// ImageView fav = (ImageView)
						// llFav.findViewById(R.id.imageViewFav);
						System.out.println("Property " + property.getId());
						String message = "";
						if (property.isUser_favourite()) {
							property.setUser_favourite(false);
							fav.setImageResource(R.drawable.favorite_outline_grey);
						} else {
							property.setUser_favourite(true);
							fav.setImageResource(R.drawable.favorite_filled);
						}
						adapter.notifyDataSetChanged();
						// Toast.makeText(ctx, vo.getMessage(),
						// Toast.LENGTH_SHORT).show();
						// app.showAppMessage(ctx, vo.getMessage());
					} else {
						showToast( vo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				System.out.println("userid " + userid + " propertyid" + property.getId());
				try {
					vo = model.Favorite(userid, property.getId(), "property");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.execute("");
		

	}

	private void showToast(String msg){
		Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
	}
}
