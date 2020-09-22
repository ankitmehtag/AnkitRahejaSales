package com.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.VO.NewLaunch;
import com.VO.NewLaunchVO;
import com.adapters.NewLaunchListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.sp.BMHApplication;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.LoginActivity;
import com.sp.R;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.model.PropertyModel;

public class FavFragment extends BaseFragment {

	private View rootView;
	private BMHApplication app;
//	private Activity ctx;
	public static final String TAG = FavFragment.class.getSimpleName();
	private NewLaunchVO vo;
	private ListView lv_favList;

	private NewLaunchListAdapter adapter = null;
	private AsyncThread mAsyncThread = null;
	private final int LOGIN_REQ_CODE = 451;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.activity_new_launches,container, false);
//			ctx = getActivity();
			app = (BMHApplication) getActivity().getApplication();
			lv_favList = (ListView) rootView.findViewById(R.id.listViewNewLaunch);
			/*lv_favList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					if(vo.getArrNewLaunch().get(position).getPropertyType().equalsIgnoreCase("unit")){
						Intent i = new Intent(getActivity() , UnitDetailActivity.class);
//						i.putExtra("unitId", vo.getArrNewLaunch().get(position).getID());
						i.putExtra("unitId", vo.getArrNewLaunch().get(position).getUnitId());
						i.putExtra("unitTitle", vo.getArrNewLaunch().get(position).getShow_text());
						startActivity(i);
					}else{
						IntentDataObject mIntentDataObject = new IntentDataObject();
						mIntentDataObject.putData(ParamsConstants.ID, vo.getArrNewLaunch().get(position).getID());
						mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
						Intent mIntent = new Intent(getActivity(), ProjectDetailActivity.class);
						mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
						startActivity(mIntent);
					}
				}
			});*/
			
			getFavourites();
		} else {
			if(rootView.getParent() != null)
			((ViewGroup) rootView.getParent()).removeView(rootView);
//			ctx = getActivity();
			app = (BMHApplication) getActivity().getApplication();
		}

		return rootView;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		for(int i = 0; i < menu.size(); i++){
			menu.getItem(i).setVisible(false);
		}

	}
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public String getTagText() {
		return TAG;
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

	
	private void getFavourites() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(getActivity(), new AsyncListner(){
			
					@Override
					public void OnBackgroundTaskCompleted() {
						if(vo == null){
							showToast(getString(R.string.unable_to_connect_server));
						}else{
							if(vo.isSuccess() && vo.getArrNewLaunch().size()>0){
								adapter = new NewLaunchListAdapter(getActivity(), vo.getArrNewLaunch(),favClickListener);
								//NewLaunchListAdapter adapter = new NewLaunchListAdapter(getActivity(), vo.getArrNewLaunch());
								lv_favList.setAdapter(adapter);
							}else{
								//TODO: show empty view
								lv_favList.setAdapter(null);
								LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
								View mView = mLayoutInflater.inflate(R.layout.search_empty_view,null);
								TextView tv_message = (TextView) mView.findViewById(R.id.tv_message);
								tv_message.setText(getString(R.string.no_favorite_items));
								lv_favList.setEmptyView(mView);
							}
						}
					}
					@Override
					public void DoBackgroundTask(String[] url) {
						PropertyModel model = new PropertyModel();
						try {
							String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
							vo = model.getFavourites(userid);
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
	public void onResume() {
		getFavourites();
		super.onResume();
	}

	View.OnClickListener favClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Object obj = v.getTag(R.integer.project_item);
			if (obj != null && obj instanceof NewLaunch) {
				NewLaunch vo = (NewLaunch) obj;
				if(app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0){
					if(vo.getPropertyType().equalsIgnoreCase("Unit")){
						app.saveIntoPrefs(BMHConstants.VALUE,vo.getUnitId());
						app.saveIntoPrefs(BMHConstants.KEY,ParamsConstants.UNIT_ID);
					}else if(vo.getPropertyType().equalsIgnoreCase("Project")){
						app.saveIntoPrefs(BMHConstants.KEY,ParamsConstants.ID);
						app.saveIntoPrefs(BMHConstants.VALUE,vo.getID());
					}
					Intent i = new Intent(getActivity(), LoginActivity.class);
					i.putExtra(LoginActivity.LOGIN_INTENT,false);// set false if login screen open from intent.
					startActivityForResult(i, LOGIN_REQ_CODE);
				}else{
					if(vo.getPropertyType().equalsIgnoreCase("Unit")){
						favRequest(ParamsConstants.UNIT_ID,vo.getUnitId());
					}else if(vo.getPropertyType().equalsIgnoreCase("Project")){
						favRequest(ParamsConstants.ID,vo.getID());
					}
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
			String key = app.getFromPrefs(BMHConstants.KEY);
			if(projectId != null && !projectId.isEmpty() && key != null && !key.isEmpty()) {
				favRequest(key,projectId);
				app.saveIntoPrefs(BMHConstants.VALUE, "");
			}
			//TODO
		}

	}

	private void favRequest(String key,String id){
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
		mStringBuilder.append(key);
		mStringBuilder.append("=");
		mStringBuilder.append(id);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.TYPE);
		mStringBuilder.append("=");
		mStringBuilder.append(ParamsConstants.BUY);
		mBean.setJson(mStringBuilder.toString());
		mBean.setRequestObj(id);
		mBean.setmHandler(mHandler);
		mBean.setCtx(getActivity());
		mAsyncThread = new AsyncThread();
		//mAsyncThread.initProgressDialog(getActivity(),mOnCancelListener);
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}
	DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			if(mAsyncThread != null)mAsyncThread.cancel(true);
			mAsyncThread = null;
		}
	};
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
						/*if(adapter != null && mBean.getRequestObj() != null && mBean.getRequestObj() instanceof  String) {
							String id = (String) mBean.getRequestObj();
							adapter.toggleFav(id);
						}*/
						getFavourites();
						break;
					default:
						break;
				}
			}
			return true;
		}
	});
    protected void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }


}
