package com.sp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
import com.utils.Connectivity;

public class FavActivity extends BaseFragmentActivity {

    private BMHApplication app;
    public static final String TAG = FavActivity.class.getSimpleName();
    private NewLaunchVO vo;
    private ListView lv_favList;
    private NewLaunchListAdapter adapter = null;
    private AsyncThread mAsyncThread = null;
    private final int LOGIN_REQ_CODE = 451;
    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        toolbar = setToolBar();
        toolbar.setTitle("Favorites");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (BMHApplication) FavActivity.this.getApplication();
        lv_favList = findViewById(R.id.listViewNewLaunch);
        if (Connectivity.isConnected(this)) {
            getFavourites();
        } else {
            lv_favList.setBackgroundResource(R.drawable.no_internet);

            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String setActionBarTitle() {
        return null;
    }


    private void getFavourites() {
        CustomAsyncTask loadingTask = new CustomAsyncTask(FavActivity.this, new AsyncListner() {

            @Override
            public void OnBackgroundTaskCompleted() {
                if (vo == null) {
                    showToast(getString(R.string.unable_to_connect_server));
                } else {
                    if (vo.isSuccess() && vo.getArrNewLaunch().size() > 0) {
                        adapter = new NewLaunchListAdapter(FavActivity.this, vo.getArrNewLaunch(), favClickListener);
                        //NewLaunchListAdapter adapter = new NewLaunchListAdapter(getActivity(), vo.getArrNewLaunch());
                        lv_favList.setAdapter(adapter);
                    } else {
                        //TODO: show empty view
                        lv_favList.setAdapter(null);
                        /*LayoutInflater mLayoutInflater = LayoutInflater.from(FavActivity.this);
                        View mView = mLayoutInflater.inflate(R.layout.nofavorite, null);
                        TextView tv_message = (TextView) mView.findViewById(R.id.tv_message);
                        tv_message.setText(getString(R.string.no_favorite_items));*/
                       // lv_favList.setEmptyView(mView);
                        lv_favList.setBackgroundResource(R.drawable.no_favorite_marked);
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
            public void OnPreExec()
            {

                 }


        });

        loadingTask.execute("");
          }

    @Override
    public void onResume() {
        super.onResume();
    }

    View.OnClickListener favClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag(R.integer.project_item);
            if (obj != null && obj instanceof NewLaunch) {
                NewLaunch vo = (NewLaunch) obj;
                if (app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0) {
                    if (vo.getPropertyType().equalsIgnoreCase("Unit")) {
                        app.saveIntoPrefs(BMHConstants.VALUE, vo.getUnitId());
                        app.saveIntoPrefs(BMHConstants.KEY, ParamsConstants.UNIT_ID);
                    } else if (vo.getPropertyType().equalsIgnoreCase("Project")) {
                        app.saveIntoPrefs(BMHConstants.KEY, ParamsConstants.ID);
                        app.saveIntoPrefs(BMHConstants.VALUE, vo.getID());
                    }
                    Intent i = new Intent(FavActivity.this, LoginActivity.class);
                    i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                    startActivityForResult(i, LOGIN_REQ_CODE);
                } else {
                    if (vo.getPropertyType().equalsIgnoreCase("Unit")) {
                        favRequest(ParamsConstants.UNIT_ID, vo.getUnitId());
                    } else if (vo.getPropertyType().equalsIgnoreCase("Project")) {
                        favRequest(ParamsConstants.ID, vo.getID());
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
            if (projectId != null && !projectId.isEmpty() && key != null && !key.isEmpty()) {
                favRequest(key, projectId);
                app.saveIntoPrefs(BMHConstants.VALUE, "");
            }
            //TODO
        }

    }

    private void favRequest(String key, String id) {
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
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.USER_TYPE);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.SALES_PERSON);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.BUILDER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.CURRENT_BUILDER_ID);

        mBean.setJson(mStringBuilder.toString());
        mBean.setRequestObj(id);
        mBean.setmHandler(mHandler);
        mBean.setCtx(FavActivity.this);
        mAsyncThread = new AsyncThread();
        //mAsyncThread.initProgressDialog(getActivity(),mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
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
                        if (adapter != null && mBean.getRequestObj() != null && mBean.getRequestObj() instanceof String) {
                            String id = (String) mBean.getRequestObj();
                            adapter.toggleFav(id);
                        }
                        getFavourites();
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });

    protected void showToast(String msg) {
        Toast.makeText(FavActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
