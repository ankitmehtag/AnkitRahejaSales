/*
package com.queppelin.bookmyhouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.AppEnums.APIType;
import com.queppelin.bookmyhouse.CustomAsyncTask.AsyncListner;
import com.VO.NewLaunch;
import com.VO.NewLaunchVO;
import com.adapters.NewLaunchListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.SnackBarCallback;
import com.model.PropertyModel;

public class FavouriteActivity extends BaseFragmentActivity {
	
	private Activity ctx = this;
	private NewLaunchVO vo;
	private ListView list;
	private NewLaunchListAdapter adapter = null;
	private  AsyncThread mAsyncThread = null;
	private final int LOGIN_REQ_CODE = 451;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_launches);
		list = (ListView) findViewById(R.id.listViewNewLaunch);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				*/
/*IntentDataObject mIntentDataObject = new IntentDataObject();
				mIntentDataObject.putData(ParamsConstants.ID, mUnitDetails.getArrNewLaunch().get(position).getID());
				mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
				Intent mIntent = new Intent(FavouriteActivity.this, ProjectDetailActivity.class);
				mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
				startActivity(mIntent);*//*

			}
		});
		
		setDrawerAndToolbar();
		getFavourites();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.favourite, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.imageViewFav) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getFavourites() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner(){
			
					@Override
					public void OnBackgroundTaskCompleted() {
						if(mUnitDetails == null){
							app.showSnackBarError(ctx, new SnackBarCallback() {
								@Override
								public void onActionButtonClick(Parcelable token) {
									getFavourites();
								}
							});
						}else{
							if(mUnitDetails.isSuccess() && mUnitDetails.getArrNewLaunch().size()>0){
								adapter = new NewLaunchListAdapter(ctx, mUnitDetails.getArrNewLaunch(),favClickListener);
								list.setAdapter(adapter);
							}else{
								app.showSnackBar(ctx, mUnitDetails.getMessage());
							}
						}
					}
					@Override
					public void DoBackgroundTask(String[] url) {
						PropertyModel model = new PropertyModel();
						try {
							String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
							mUnitDetails = model.getFavourites(userid);
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

	View.OnClickListener favClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Object obj = v.getTag(R.integer.project_item);
			if (obj != null && obj instanceof NewLaunch) {
				NewLaunch mUnitDetails = (NewLaunch) obj;
				if(app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0){
					app.saveIntoPrefs(BMHConstants.VALUE,mUnitDetails.getID());
					Intent i = new Intent(ctx, LoginActivity.class);
					i.putExtra(LoginActivity.LOGIN_INTENT,false);// set false if login screen open from intent.
					startActivityForResult(i, LOGIN_REQ_CODE);
				}else{
					favRequest(mUnitDetails.getID());
				}
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("hh location alert fragment acti result");
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LOGIN_REQ_CODE && resultCode == Activity.RESULT_OK) {
			String projectId = app.getFromPrefs(BMHConstants.VALUE);
			if(projectId != null && projectId.length() > 0) {
				favRequest(projectId);
				app.saveIntoPrefs(BMHConstants.VALUE, "");
			}
			//TODO
		}

	}
	@Override
	protected String setActionBarTitle() {
		return getResources().getString(R.string.favourite);
	}


	private void favRequest(String id){
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.FAV_PROJECT);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.FAV_PROJECT));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
		StringBuilder mStringBuilder = new StringBuilder("");
		mStringBuilder.append(ParamsConstants.USER_ID);
		mStringBuilder.append("=");
		mStringBuilder.append(userId);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.ID);
		mStringBuilder.append("=");
		mStringBuilder.append(id);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.TYPE);
		mStringBuilder.append("=");
		mStringBuilder.append(ParamsConstants.BUY);
		mBean.setJson(mStringBuilder.toString());
		mBean.setRequestObj(id);
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}

	Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.obj == null) {
				//TODO: Error
				return false;
			} else {
				ReqRespBean mBean = (ReqRespBean) msg.obj;
				switch (mBean.getApiType()) {
					case FAV_PROJECT:
						if(adapter != null && mBean.getRequestObj() != null && mBean.getRequestObj() instanceof  String) {
							String id = (String) mBean.getRequestObj();
							adapter.toggleFav(id);
						}
						break;
					default:
						break;
				}
			}
			return true;
		}
	});


}
*/
