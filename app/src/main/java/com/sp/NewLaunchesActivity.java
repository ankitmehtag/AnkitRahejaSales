package com.sp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.AppEnums.APIType;
import com.VO.NewLaunch;
import com.VO.NewLaunchVO;
import com.adapters.NewLaunchListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.model.PropertyModel;

//import com.google.android.gcm.GCMRegistrar;

public class NewLaunchesActivity extends BaseFragmentActivity {

	private Activity ctx = this;
	private ListView list;
	private NewLaunchVO vo;
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
				Intent i = new Intent(NewLaunchesActivity.this , ProjectDetailActivity.class);
				i.putExtra("propertyId", vo.getArrNewLaunch().get(position).getID());
				startActivity(i);
			}
		});
			getNewLaunchesTask();
		setDrawerAndToolbar();
//		initGCM();
		
		
//		int actionbarHeight = getSupportActionBar().getHeight();
//		System.out.println("hh height = "+actionbarHeight);
//		int min1 = ((app.getWidth(ctx)) - actionbarHeight);
//		int min = Math.min(min1, 6 * actionbarHeight);
//		System.out.println("hh height min = "+min);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_launches, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
//		if (id == R.id.action_logout) {
//			
//			Intent intent = new Intent(this, LoginActivity.class);
//			intent.putExtra("classToStart", BMHConstants.HOME_ACTI);
//		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
//		                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
//		                    Intent.FLAG_ACTIVITY_NEW_TASK);
//		    SharedPreferences p = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
//		    p.edit().clear().commit();
//		    startActivity(intent);
//		    finish();
//			
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void getNewLaunchesTask() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(NewLaunchesActivity.this, new AsyncListner(){
			
					@Override
					public void OnBackgroundTaskCompleted() {
						if(vo == null){
//							app.showAppMessage(NewLaunchesActivity.this, "Something went wrong, Please Try again.");
							showToast(getString(R.string.unable_to_connect_server));
						}else{
							if(vo.isSuccess() && vo.getArrNewLaunch().size()>0){
								NewLaunchListAdapter adapter = new NewLaunchListAdapter(NewLaunchesActivity.this, vo.getArrNewLaunch(),favClickListener);
								list.setAdapter(adapter);
							}else{
								showToast(vo.getMessage());
							}
						}
					}
					@Override
					public void DoBackgroundTask(String[] url) {
						PropertyModel model = new PropertyModel();
						try {
							String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
							vo = model.getNewLaunches(userid);
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
	
	

	@Override
	protected String setActionBarTitle() {
			return "New Launches";
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		System.out.println("hh on new intent new launches");
		getNewLaunchesTask();
	}
	View.OnClickListener favClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Object obj = v.getTag(R.integer.project_item);
			if (obj != null && obj instanceof NewLaunch) {
				NewLaunch vo = (NewLaunch) obj;
				if(app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0){
					app.saveIntoPrefs(BMHConstants.VALUE,vo.getID());
					Intent i = new Intent(ctx, LoginActivity.class);
					i.putExtra(LoginActivity.LOGIN_INTENT,false);// set false if login screen open from intent.
					startActivityForResult(i, LOGIN_REQ_CODE);
				}else{
					favRequest(vo.getID());
				}
			}
		}
	};


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
