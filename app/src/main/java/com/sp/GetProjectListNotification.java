package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.BaseVO;
import com.VO.PropertyCaraouselListVO;
import com.VO.PropertyCaraouselVO;
import com.adapters.PropertyListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.exception.BMHException;
import com.filter.Builder;
import com.fragments.BaseFragment;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.interfaces.HostActivityInterface;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.model.PropertyModel;
import com.pwn.CommonLib;
import com.utils.MyDividerItemDecoration;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetProjectListNotification extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener, HostActivityInterface {

    private static final String TAG = GetProjectListNotification.class.getSimpleName();
    private Activity ctx = GetProjectListNotification.this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Builder> selectedBuilders = null;
    private HashMap<String, String> mapParams = new HashMap<String, String>();
    private int GET_ALERT_REQ = 548;
    private final int SELECT_BUILDER_REQ = 480;
    private final int LOGIN_REQ_CODE = 451;
    private PropertyListAdapter propertyAdapter;

    private ArrayList<PropertyCaraouselVO> propertyListVO = null;
    private String special_category = "";
    private IntentDataObject mIntentDataObject = null;
    private AsyncThread mAsyncThread = null;
    private View currentFooterView = null;
    private Toolbar toolbar = null;
    private boolean isInitialState = false;
    private NetworkErrorObject mNetworkErrorObject = null;
    private StringBuilder mStringBuilderForFilter;
    private String favProjId = "";
    private String getAlertParams;

    private BaseFragment selectedFragment;

    private static final int MULTI_FILTER = 452;
    private static final int HEAT_MAP_REQ = 453;
    private HashMap<String, String> appliedFilterStateMap = null;
    private ImageView btn_projects, btn_blog, btn_user, btn_notification, btn_home;
    //private LinearLayout ll_sort,ll_filter,ll_sort_by_root,ll_sort_sub_root;
    private TextView tv_sort_value, tv_filter_count;
    ;
    private Button btn_price_l_to_h, btn_price_h_to_l, btn_psf_l_to_h, btn_psf_h_to_l, btn_possession_earlier, btn_possession_later;
    private String sort_value = "priceltoh";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_project_list_notification);
        mRecyclerView =  findViewById(R.id.recyclerView);

        updateReadStatus();
        isInitialState = true;
        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        toolbar.setTitle("Project List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InitializeFilterView();
        setListeners();

        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            String title = (mIntentDataObject.getMap() != null) ? mIntentDataObject.getMap().get(ParamsConstants.TITLE) : BMHConstants.BUILDER_NAME;
            if (mIntentDataObject.getMap() != null) {
                mIntentDataObject.putData("priceltoh", sort_value);
                if (mIntentDataObject.getMap().get(ParamsConstants.SPECIAL_CATEGORY) != null) {
                    //toolbar.setTitle(title + " Projects");
                } else {
                    //toolbar.setTitle(title);
                }
            }
        } else {
            mIntentDataObject = new IntentDataObject();
            mIntentDataObject.putData("priceltoh", sort_value);
            mIntentDataObject.putData(ParamsConstants.BUILDER_ID,  BMHConstants.CURRENT_BUILDER_ID);
            //toolbar.setTitle(BMHConstants.BUILDER_NAME);
        }



        /*if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            String title = (mIntentDataObject.getMap() != null) ? mIntentDataObject.getMap().get(ParamsConstants.TITLE) : BMHConstants.BUILDER_NAME;
            if (mIntentDataObject.getMap() != null) {
                mIntentDataObject.putData("priceltoh", sort_value);
                if (mIntentDataObject.getMap().get(ParamsConstants.SPECIAL_CATEGORY) != null) {
                    //toolbar.setTitle(title + " Projects");
                } else {
                    //toolbar.setTitle(title);
                }
            }
        } else {
            mIntentDataObject = new IntentDataObject();
            mIntentDataObject.putData("priceltoh", sort_value);
            mIntentDataObject.putData(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
            //toolbar.setTitle(BMHConstants.BUILDER_NAME);
        }
*/
        defaultUIState();
        getProjectsData(getRequestParams());

    }
           // End of onCreate().

    private void defaultUIState() {

        if (mapParams == null || toolbar == null) return;
        sortByBtnState(btn_price_l_to_h);

    }

    private String getRequestParams() {
        if (mIntentDataObject == null || mIntentDataObject.getData() == null) return "";
        HashMap<String, String> mapParams = mIntentDataObject.getData();
        if (mapParams == null) return "";
        if (app.getFromPrefs(BMHConstants.USERID_KEY) != null && !app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
            mapParams.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
        }
        StringBuilder mStringBuilder = new StringBuilder("");
        for (Map.Entry<String, String> entry : mIntentDataObject.getData().entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                mStringBuilder.append(entry.getKey());
                mStringBuilder.append("=");
                mStringBuilder.append(entry.getValue());
                mStringBuilder.append("&");
            }
        }
        return Utils.removeLastAndSign(mStringBuilder.toString()) != null ? Utils.removeLastAndSign(mStringBuilder.toString()) : mStringBuilder.toString();
    }

    private void setListeners() {
        btn_home.setOnClickListener(mOnClickListener);
        btn_blog.setOnClickListener(mOnClickListener);
        btn_user.setOnClickListener(mOnClickListener);
        btn_notification.setOnClickListener(mOnClickListener);

       /* ll_filter.setOnClickListener(mOnClickListener);
        ll_sort.setOnClickListener(mOnClickListener);*/

        btn_price_l_to_h.setOnClickListener(mOnClickListener);
        btn_price_h_to_l.setOnClickListener(mOnClickListener);
        btn_psf_l_to_h.setOnClickListener(mOnClickListener);
        btn_psf_h_to_l.setOnClickListener(mOnClickListener);
        btn_possession_earlier.setOnClickListener(mOnClickListener);
        btn_possession_later.setOnClickListener(mOnClickListener);
        //ll_sort_by_root.setOnClickListener(mOnClickListener);
        //ll_sort_sub_root.setOnClickListener(mOnClickListener);


    }

    private void InitializeFilterView() {
      //  mRecyclerView =  findViewById(R.id.recyclerView);
        btn_projects = (ImageView) findViewById(R.id.btn_projects);
        btn_blog = (ImageView) findViewById(R.id.btn_blog);
        btn_user = (ImageView) findViewById(R.id.btn_user);
        btn_notification = (ImageView) findViewById(R.id.btn_notification);
        btn_home = (ImageView) findViewById(R.id.btn_home);

        /*ll_filter = (LinearLayout) findViewById(R.id.ll_filter);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        ll_sort_by_root = (LinearLayout)findViewById(R.id.ll_sort_by_root);
        ll_sort_sub_root = (LinearLayout)findViewById(R.id.ll_sort_sub_root);*/

        tv_sort_value = (TextView) findViewById(R.id.tv_sort_value);
        tv_filter_count = (TextView) findViewById(R.id.tv_filter_count);

        btn_price_l_to_h = (Button) findViewById(R.id.btn_price_l_to_h);
        btn_price_h_to_l = (Button) findViewById(R.id.btn_price_h_to_l);

        btn_psf_l_to_h = (Button) findViewById(R.id.btn_psf_l_to_h);
        btn_psf_h_to_l = (Button) findViewById(R.id.btn_psf_h_to_l);

        btn_possession_earlier = (Button) findViewById(R.id.btn_possession_earlier);
        btn_possession_later = (Button) findViewById(R.id.btn_possession_later);


        LinearLayoutManager obj=new LinearLayoutManager(getApplicationContext());
        obj.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(obj);
        View footerV = getLayoutInflater().inflate(R.layout.row_alert_footer, null);
        TextView textViewAlertTitle = (TextView) footerV.findViewById(R.id.textViewAlertTitle);
        textViewAlertTitle.setText("Get notified as soon as a new property is added in this locality.");


    }

    private void filterSorting(String result) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.property_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_filter:
                goToFilterScreen();
                break;
            case R.id.action_heatmap:
                Intent mIntent = new Intent(GetProjectListNotification.this, ProjectHeatmapActivity.class);
                mIntentDataObject.setFilterStateMap(appliedFilterStateMap);
                mIntentDataObject.setObj(selectedBuilders);
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivityForResult(mIntent, HEAT_MAP_REQ);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void goToFilterScreen() {
        if (mIntentDataObject != null) {
            Intent mIntent = new Intent(GetProjectListNotification.this, FilterActivityActivity.class);
            mIntentDataObject.setFilterStateMap(appliedFilterStateMap);
            mIntentDataObject.setObj(selectedBuilders);
            mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
            startActivityForResult(mIntent, MULTI_FILTER);

                }
    }

    public void setPropertyListAdapter(ArrayList<PropertyCaraouselVO> propertyListVO) {

        if (propertyListVO != null && propertyListVO.size() > 0) {
            propertyAdapter = new PropertyListAdapter(ctx, propertyListVO);

              mRecyclerView.setAdapter(propertyAdapter);
            mRecyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(ctx), LinearLayoutManager.VERTICAL, 5));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            layoutManager = new LinearLayoutManager(ctx);
            mRecyclerView.setLayoutManager(layoutManager);

                 }

             /* else {
            mRecyclerView.setBackgroundResource(R.drawable.no_lead_assigned);

                 }  */

       // if (currentFooterView != null) listProperty.removeFooterView(currentFooterView);
        if (propertyListVO == null || propertyListVO.size() == 0) {
            currentFooterView = getFooterView(true);
            //   listProperty.setEmptyView(currentFooterView);
           // propertyAdapter.updateListData(propertyListVO);
            mRecyclerView.setBackgroundResource(R.drawable.no_item_searched);
        }

        else {
            //currentFooterView = (getFooterView(false));
                //listProperty.addFooterView(currentFooterView);
                  propertyAdapter.updateListData(propertyListVO);
                   mRecyclerView.smoothScrollToPosition(0);

        }

    }

   /* OnClickListener favClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag(R.integer.project_item);
            if (obj != null && obj instanceof PropertyCaraouselVO) {
                final PropertyCaraouselVO vo = (PropertyCaraouselVO) obj;
                if (app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0) {
                    app.saveIntoPrefs(BMHConstants.VALUE, vo.getId());
                    Intent i = new Intent(ctx, LoginActivity.class);
                    i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                    startActivityForResult(i, LOGIN_REQ_CODE);
                } else {
                    favProjId = vo.getId();
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                        favRequest(favProjId);
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(GetProjectListNotification.this, UIEventType.RETRY_FEV,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            favRequest(favProjId);
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(GetProjectListNotification.this, getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }
                }
            }
        }
    };
    OnClickListener listItemClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag(R.integer.project_item) != null && view.getTag(R.integer.project_item) instanceof PropertyCaraouselVO) {
                PropertyCaraouselVO model = (PropertyCaraouselVO) view.getTag(R.integer.project_item);
                IntentDataObject mIntentDataObject = new IntentDataObject();
                mIntentDataObject.putData(ParamsConstants.ID, model.getId());
                mIntentDataObject.putData(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                mIntentDataObject.putData(ParamsConstants.TYPE, ParamsConstants.BUY);
                Intent mIntent = new Intent(GetProjectListNotification.this, ProjectDetailActivity.class);
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivity(mIntent);
            }
        }
    };*/

    private void favRequest(String id) {
        if (id == null || id.isEmpty()) return;
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
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private View getFooterView(boolean isEmptyView) {
        View footerView = getLayoutInflater().inflate(R.layout.row_alert_footer, null);
        Button getAlerts = (Button) footerView.findViewById(R.id.btnGetAlerts);
        getAlerts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnquryDialog();
            }
        });
        if (isEmptyView) {
            LinearLayout mView = (LinearLayout) findViewById(R.id.empty_view);
            LinearLayout.LayoutParams emptyViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            emptyViewParams.weight = 1.0f;//0.75f;
            emptyViewParams.setMargins(0, 0, 0, (int) Utils.dp2px(5f, this));
            LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            footerParams.weight = 0.0f;///0.25f;
            View emptyView = getLayoutInflater().inflate(R.layout.search_empty_view, null);
            emptyView.setLayoutParams(emptyViewParams);
            footerView.setLayoutParams(footerParams);
            mView.addView(emptyView);
            //mView.addView(footerView);
            return mView;
        } else {
            return footerView;
        }
    }

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    private void submitAlert() {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new CustomAsyncTask.AsyncListner() {
            BaseVO baseVo;

            @Override
            public void OnBackgroundTaskCompleted() {
                if (baseVo == null) {
                    showToast("Something went wrong. Try again");
                } else {
                    if (baseVo.isSuccess()) {
                        // app.showSnackBar(getActivity(), baseVo.getMessage(),
                        // SnackBar.MED_SNACK);
                        showSuccessDialog();
                    } else {
                        showToast(baseVo.getMessage());
                    }
                }

            }

            @Override
            public void DoBackgroundTask(String[] url) {
                PropertyModel model = new PropertyModel();
                try {
                    mapParams = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
                    if (mapParams != null) {
                        mapParams.put("email", app.getFromPrefs(BMHConstants.USER_EMAIL));
                        mapParams.put("name", app.getFromPrefs(BMHConstants.USERNAME_KEY));
                        mapParams.put("contactno", "1234567890");
                        mapParams.put("search", "");
                        mapParams.put("device_type", "android");
                        mapParams.put("device_id", app.getFromPrefs(BMHConstants.GCM_REG_ID));
                        baseVo = model.submitAlert(mapParams);
                    }
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

    private void showSuccessDialog() {
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View dialogView = factory.inflate(R.layout.alert_registration, null);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        Button close = (Button) dialogView.findViewById(R.id.btnClose);
        //close.setTypeface(fond);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("hh location alert fragment acti result");
        super.onActivityResult(requestCode, resultCode, data);
        CommonLib commonLib = new CommonLib();

        if (requestCode == GET_ALERT_REQ && resultCode == RESULT_OK) {
            submitAlert();
        } else if (requestCode == commonLib.LOGIN_FOR_FAV && resultCode == RESULT_OK) {
            String projectId = app.getFromPrefs("TEMP_JUGAD");

            System.out.println(" ProjectId " + projectId);
            System.out.println("Now succesfull login");
            propertyAdapter.toggleFav(projectId);
        } else if (requestCode == SELECT_BUILDER_REQ && resultCode == RESULT_OK) {

        } else if (requestCode == LOGIN_REQ_CODE && resultCode == RESULT_OK) {
            String projectId = app.getFromPrefs(BMHConstants.VALUE);
            if (projectId != null && projectId.length() > 0) {
                favRequest(projectId);
                app.saveIntoPrefs(BMHConstants.VALUE, "");
            }
            //TODO
        }
            else if (requestCode == MULTI_FILTER && resultCode == RESULT_OK) {
            if (data != null && data.getSerializableExtra(IntentDataObject.OBJ) != null
                    && data.getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
                IntentDataObject mIntentDataObject = (IntentDataObject) data.getSerializableExtra(IntentDataObject.OBJ);
                appliedFilterStateMap = mIntentDataObject.getData();
                if (mIntentDataObject.getObj() != null)
                    selectedBuilders = (ArrayList<Builder>) mIntentDataObject.getObj();
                if (appliedFilterStateMap != null) {
                    StringBuilder mStringBuilder = new StringBuilder("");
                    for (Map.Entry<String, String> entry : appliedFilterStateMap.entrySet()) {
                        if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                            mStringBuilder.append(entry.getKey());
                            mStringBuilder.append("=");
                            mStringBuilder.append(entry.getValue());
                            mStringBuilder.append("&");
                        }
                    }
                    if (getAppliedFilterCount() == 0) {
                        tv_filter_count.setVisibility(View.GONE);
                    } else {
                        tv_filter_count.setVisibility(View.VISIBLE);
                        tv_filter_count.setText(String.valueOf(getAppliedFilterCount()));
                    }
                    getProjectsData(mStringBuilder.toString());
                }

            } else {
                //TODO: error
            }
        }

    }

    private void showEnquryDialog() {
        LayoutInflater factory = LayoutInflater.from(ctx);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        final View dialogView = factory.inflate(R.layout.save_alert, null);
        final EditText edUserName = (EditText) dialogView.findViewById(R.id.et_user_name);
        final EditText edUserEmail = (EditText) dialogView.findViewById(R.id.et_email);
        final EditText edUserPhone = (EditText) dialogView.findViewById(R.id.et_mobile);
        Button btnSend = (Button) dialogView.findViewById(R.id.btnSend);
        ImageButton img_close = (ImageButton) dialogView.findViewById(R.id.img_close);
        TextView tvTermsofuse = (TextView) dialogView.findViewById(R.id.terms_conition);
        if (app.getFromPrefs(BMHConstants.USERNAME_KEY) != null)
            edUserName.setText(app.getFromPrefs(BMHConstants.USERNAME_KEY));
        if (app.getFromPrefs(BMHConstants.USER_EMAIL) != null)
            edUserEmail.setText(app.getFromPrefs(BMHConstants.USER_EMAIL));

		/*edUserName.setTypeface(fond);
        edUserEmail.setTypeface(fond);
		edUserPhone.setTypeface(fond);
		btnSend.setTypeface(fond);
		*/
        OnClickListener dialogViewsClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSend:
                        if (isValidAlertData(dialogView)) {
                            getAlertParams = getAlertParams(edUserName.getText().toString(), edUserPhone.getText().toString(), edUserEmail.getText().toString());
                            if (getAlertParams == null || getAlertParams.isEmpty()) return;
                            if (ConnectivityReceiver.isConnected()) {
                                //TODO: network call
                                sendAlertRequest(getAlertParams);
                            } else {
                                mNetworkErrorObject = Utils.showNetworkErrorDialog(GetProjectListNotification.this, UIEventType.RETRY_ALERT,
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (ConnectivityReceiver.isConnected()) {
                                                    //TODO: network call
                                                    sendAlertRequest(getAlertParams);
                                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                                    mNetworkErrorObject = null;
                                                } else {
                                                    Utils.showToast(GetProjectListNotification.this, getString(R.string.check_your_internet_connection));
                                                }
                                            }
                                        });
                            }
                            dialog.dismiss();
                        }
                        break;
                    case R.id.img_close:
                        dialog.dismiss();
                        break;
                    case R.id.terms_conition:
                        Intent i = new Intent(GetProjectListNotification.this, TermsWebActivity.class);
                        i.putExtra("pageType", 0);
                        startActivity(i);
                        break;
                }
            }
        };
        img_close.setOnClickListener(dialogViewsClick);
        tvTermsofuse.setOnClickListener(dialogViewsClick);
        btnSend.setOnClickListener(dialogViewsClick);

        dialog.setView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

    }

    private String getAlertParams(String name, String phone, String email) {
        if (name == null || name.isEmpty() || phone == null || phone.isEmpty() || email == null || email.isEmpty())
            return "";
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("name=");
        mStringBuilder.append(name);
        mStringBuilder.append("&contactno=");
        mStringBuilder.append(phone);
        mStringBuilder.append("&email=");
        mStringBuilder.append(email);
        if (app.getFromPrefs(BMHConstants.CITYID) != null && !app.getFromPrefs(BMHConstants.CITYID).isEmpty()) {
            mStringBuilder.append("&city_id=");
            mStringBuilder.append(app.getFromPrefs(BMHConstants.CITYID));
        }
        if (app.getFromPrefs(BMHConstants.USERID_KEY) != null && !app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
            mStringBuilder.append("&user_id=");
            mStringBuilder.append(app.getFromPrefs(BMHConstants.USERID_KEY));
        }
        if (mStringBuilder != null && mIntentDataObject.getData() != null) {
            HashMap<String, String> map = mIntentDataObject.getData();
            if (map.get(ParamsConstants.P_TYPE) != null) {
                mStringBuilder.append("&" + ParamsConstants.P_TYPE + "=");
                mStringBuilder.append(map.get(ParamsConstants.P_TYPE));
            }
            if (map.get(ParamsConstants.TYPE) != null) {
                mStringBuilder.append("&" + ParamsConstants.TYPE + "=");
                mStringBuilder.append(map.get(ParamsConstants.TYPE));
            }
            if (map.get(ParamsConstants.LOCATION_ID) != null) {
                mStringBuilder.append("&" + ParamsConstants.LOCATION_ID + "=");
                mStringBuilder.append(map.get(ParamsConstants.LOCATION_ID));
            }
            if (map.get(ParamsConstants.SUB_LOCATION_ID) != null) {
                mStringBuilder.append("&" + ParamsConstants.SUB_LOCATION_ID + "=");
                mStringBuilder.append(map.get(ParamsConstants.SUB_LOCATION_ID));
            }
            if (map.get(ParamsConstants.BUILDER_ID) != null) {
                mStringBuilder.append("&" + ParamsConstants.BUILDER_ID + "=");
                mStringBuilder.append(map.get(ParamsConstants.BUILDER_ID));
            }
            if (map.get(ParamsConstants.SPECIAL_CATEGORY) != null) {
                mStringBuilder.append("&category_type=");
                mStringBuilder.append(map.get(ParamsConstants.SPECIAL_CATEGORY));
            }
        }

        return mStringBuilder.toString();
    }

    private void sendAlertRequest(String params) {
        if (params == null || params.isEmpty()) return;
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_ALERT);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_ALERT));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private boolean isValidAlertData(View dialogView) {
        if (dialogView == null) return false;
        final EditText edUserName = (EditText) dialogView.findViewById(R.id.et_user_name);
        final EditText edUserEmail = (EditText) dialogView.findViewById(R.id.et_email);
        final EditText edUserPhone = (EditText) dialogView.findViewById(R.id.et_mobile);
        final CheckBox edCheck = (CheckBox) dialogView.findViewById(R.id.cb_tnc);
        if (edUserName.getText().toString().trim().isEmpty()) {
            showToast("Enter your name.");
            return false;
        }
        if (StringUtil.checkSpecialCharacter(edUserName.getText().toString().trim())) {
            showToast("Special character and digits are not allowed in Name.");
            app.shakeEdittext(edUserName);
            return false;
        } else if (edUserEmail.getText().toString().trim().isEmpty()) {
            showToast("Please enter Email.");
            app.shakeEdittext(edUserEmail);
            return false;
        } else if (edUserEmail.getText().toString().trim().isEmpty() || !Utils.isEmailValid(edUserEmail.getText().toString().trim())) {
            showToast("Please enter a valid Email.");
            app.shakeEdittext(edUserEmail);
            return false;
        } else if (edUserPhone.getText().toString().trim().isEmpty()) {
            showToast("Please enter Mobile Number.");
            app.shakeEdittext(edUserPhone);
        } else if (edUserPhone.getText().toString().trim().length() < 10) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(edUserPhone);
            return false;
        } else if (!checkMobileValidity(edUserPhone.getText().toString().trim().charAt(0))) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(edUserPhone);
            return false;
        } else if (!edCheck.isChecked()) {
            showToast("Please agree T&C");
            return false;
        }
        return true;
    }


    protected boolean checkMobileValidity(char firstCharacter) {
        boolean validNo = false;
        switch (firstCharacter) {
            case '7':
                validNo = true;
                break;
            case '8':
                validNo = true;
                break;
            case '9':
                validNo = true;
                break;
        }
        return validNo;
    }

    OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            Log.d(TAG, "List click");
            if (view.getTag(R.integer.project_item) != null && view.getTag(R.integer.project_item) instanceof PropertyCaraouselVO) {
                PropertyCaraouselVO model = (PropertyCaraouselVO) view.getTag(R.integer.project_item);
                IntentDataObject mIntentDataObject = new IntentDataObject();
                mIntentDataObject.putData(ParamsConstants.ID, model.getId());
                mIntentDataObject.putData(ParamsConstants.TYPE, ParamsConstants.BUY);
                Intent mIntent = new Intent(GetProjectListNotification.this, ProjectDetailActivity.class);
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivity(mIntent);
            }
        }
    };
    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnGetAlerts:
                    showEnquryDialog();
                    break;
                case R.id.btn_home:
                    onBackPressed();
                    break;
                case R.id.btn_blog:
                    break;
                case R.id.btn_user:

                    break;
                case R.id.btn_notification:
                    showToast("Under dev phase");
                    break;
               /* case R.id.ll_sort:
                    int visibility = (ll_sort_by_root.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                    ll_sort_by_root.setVisibility(visibility);
                    break;*/
                case R.id.ll_filter:
                    goToFilterScreen();
                    break;
                /*case R.id.btn_price_l_to_h:
                    sort_value = "priceltoh";
                    tv_sort_value.setText("Price: Low to High");
                    ll_sort_by_root.setVisibility(View.GONE);
                    sortByBtnState(btn_price_l_to_h);
                    sortDataRequest();
                    break;
                case R.id.btn_price_h_to_l:
                    sort_value = "pricehtol";
                    tv_sort_value.setText("Price: High to Low");
                    ll_sort_by_root.setVisibility(View.GONE);
                    sortByBtnState(btn_price_h_to_l);
                    sortDataRequest();
                    break;
                case R.id.btn_psf_l_to_h:
                    sort_value = "pricePSFltoh";
                    tv_sort_value.setText("Price PSF: Low to High");
                    ll_sort_by_root.setVisibility(View.GONE);
                    sortByBtnState(btn_psf_l_to_h);
                    sortDataRequest();
                    break;
                case R.id.btn_psf_h_to_l:
                    sort_value = "pricePSFhtol";
                    tv_sort_value.setText("Price PSF: High to Low");
                    ll_sort_by_root.setVisibility(View.GONE);
                    sortByBtnState(btn_psf_h_to_l);
                    sortDataRequest();
                    break;
                case R.id.btn_possession_earlier:
                    sort_value = "Possessionltoh";
                    tv_sort_value.setText("Possession: Earlier");
                    ll_sort_by_root.setVisibility(View.GONE);
                    sortByBtnState(btn_possession_earlier);
                    sortDataRequest();
                    break;
                case R.id.btn_possession_later:
                    sort_value = "Possessionhtol";
                    tv_sort_value.setText("Possession: Later");
                    ll_sort_by_root.setVisibility(View.GONE);
                    sortByBtnState(btn_possession_later);
                    sortDataRequest();
                    break;
                case R.id.ll_sort_by_root:
                    ll_sort_by_root.setVisibility(View.GONE);
                    break;*/
                case R.id.ll_sort_sub_root:
                    //TODO: do nothing
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    public void onBackPressed() {
        /*if(ll_sort_by_root.getVisibility() == View.VISIBLE){
            ll_sort_by_root.setVisibility(View.GONE);
        } else {*/
        //super.onBackPressed();
        finish();
    }


    private void getProjectsData(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_PROJECT_LIST_NOTIFICATION);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_PROJECT_LIST_NOTIFICATION));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(GetProjectListNotification.this, mOnCancelListener);
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
            } else
                {

                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case GET_PROJECT_LIST_NOTIFICATION:
                        if (mBean.getJson() != null) {
                            PropertyCaraouselListVO propVo = null;
                            try {
                                JSONObject jsonObj = new JSONObject(mBean.getJson());
                                if (jsonObj != null) {
                                    propVo = new PropertyCaraouselListVO(jsonObj);
                                }
                                if (propVo != null && propVo.isSuccess()) {
                                    propertyListVO = propVo.getPropertiesArr();
                                                if (propertyListVO != null && !propertyListVO.isEmpty()) { setPropertyListAdapter(propertyListVO);
                                        if (isInitialState) {
                                            selectedBuilders = getBuilders(propertyListVO);
                                            supportInvalidateOptionsMenu();
                                            isInitialState = false;
                                        }
                                    } else {
                                        propertyListVO = null;
                                        setPropertyListAdapter(null);
                                    }

                                        }
                                     else {
                                    propertyListVO = null;
                                    setPropertyListAdapter(null);
                                    /*if(mBean.getHttpResp() != HttpURLConnection.HTTP_OK){
										if(ConnectivityReceiver.isConnected()){
											getProjectsData(getRequestParams());
										}
									}*/
                                }
                            } catch (JSONException e) {
                                propertyListVO = null;
                                setPropertyListAdapter(null);
                                if (ConnectivityReceiver.isConnected()) {
                                    //TODO: network call
                                    //getProjectsData(getRequestParams());
                                    //DO Nothng.
                                } else {
                                    mNetworkErrorObject = Utils.showNetworkErrorDialog(GetProjectListNotification.this, UIEventType.RETRY_SEARCH_PROJECTS,
                                            new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (ConnectivityReceiver.isConnected()) {
                                                        //TODO: network call
                                                        getProjectsData(getRequestParams());
                                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                                        mNetworkErrorObject = null;
                                                    } else {
                                                        Utils.showToast(GetProjectListNotification.this, getString(R.string.check_your_internet_connection));
                                                    }
                                                }
                                            });
                                }
                            }

                        } else {
                            propertyListVO = null;
                            setPropertyListAdapter(null);

                        }
                        break;
                    case FAV_PROJECT:
                        if (propertyAdapter != null && mBean.getRequestObj() != null && mBean.getRequestObj() instanceof String) {
                            String id = (String) mBean.getRequestObj();
                            propertyAdapter.toggleFav(id);
                        }
                        break;
                    case GET_ALERT:
                        BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.GET_ALERT, mBean.getJson());
                        if (baseRespModel != null) {
                            if (baseRespModel.isSuccess()) {
                                showToast(baseRespModel.getMessage());
                                //TODO:
                            } else {
                                showToast(baseRespModel.getMessage());
                            }
                        } else {
                            showToast(getString(R.string.unable_to_connect_server));
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });

    private void updateReadStatus() {
        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            String notification_id = (mIntentDataObject.getMap() != null) ? mIntentDataObject.getMap().get("notification_id") : BMHConstants.BUILDER_NAME;
            if (mIntentDataObject.getMap() != null) {
                ReqRespBean mBeanBlog = new ReqRespBean();
                mBeanBlog.setApiType(APIType.READ_STATUS);
                mBeanBlog.setRequestmethod(WEBAPI.POST);
                mBeanBlog.setUrl(WEBAPI.getWEBAPI(APIType.READ_STATUS));
                mBeanBlog.setMimeType(WEBAPI.contentTypeFormData);
                String userIdBlog = app.getFromPrefs(BMHConstants.USERID_KEY);
                StringBuilder mStringBuilderBlog = new StringBuilder("");
                mStringBuilderBlog.append(ParamsConstants.USER_ID);
                mStringBuilderBlog.append("=");
                mStringBuilderBlog.append(userIdBlog);
                mStringBuilderBlog.append("&");
                mStringBuilderBlog.append(ParamsConstants.BUILDER_ID);
                mStringBuilderBlog.append("=");
                mStringBuilderBlog.append(BMHConstants.CURRENT_BUILDER_ID);
                mStringBuilderBlog.append("&");
                mStringBuilderBlog.append("notification_id");
                mStringBuilderBlog.append("=");
                mStringBuilderBlog.append(notification_id);
                mBeanBlog.setJson(mStringBuilderBlog.toString());
                mBeanBlog.setmHandler(mHandler);
                mBeanBlog.setCtx(this);
                mAsyncThread = new AsyncThread();
                mAsyncThread.initProgressDialog(this, mOnCancelListener);
                mAsyncThread.execute(mBeanBlog);
                mAsyncThread = null;
            }
        } else {
            mIntentDataObject = new IntentDataObject();
            mIntentDataObject.putData("priceltoh", sort_value);
            mIntentDataObject.putData(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
            //toolbar.setTitle(BMHConstants.BUILDER_NAME);
        }
    }

    private ArrayList<Builder> getBuilders(ArrayList<PropertyCaraouselVO> propertyListVO) {
        if (propertyListVO == null || propertyListVO.size() == 0) return null;
        ArrayList<Builder> mBuilders = new ArrayList<>();
        ArrayList<String> tempBuilderIds = new ArrayList<>();
        for (PropertyCaraouselVO model : propertyListVO) {
            if (model != null && model.getBuilder_id() != null && !tempBuilderIds.contains(model.getBuilder_id())) {
                tempBuilderIds.add(model.getBuilder_id());
                mBuilders.add(new Builder(model.getBuilder_id(), model.getBuilder_name(), false));
            }
        }
        return mBuilders;

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_SEARCH_PROJECTS:
                    getProjectsData(getRequestParams());
                    break;
                case RETRY_APPLY_FILTER:
                    if (mStringBuilderForFilter != null) {
                        getProjectsData(mStringBuilderForFilter.toString());
                    }
                    break;
                case RETRY_FEV:
                    favRequest(favProjId);
                    break;
                case RETRY_ALERT:
                    sendAlertRequest(getAlertParams);
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }


    @Override
    public void setSelectedFragment(BaseFragment fragment) {
        this.selectedFragment = fragment;

    }

    public BaseFragment getSelectedFragment() {
        return this.selectedFragment;
    }

    @Override
    public void popBackStack() {
        // TODO Auto-generated method stub
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void popBackStackTillTag(String tag) {
        // TODO Auto-generated method stub
        getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean withAnimation) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (withAnimation) {
            // TO ENABLE FRAGMENT ANIMATION
            // Format: setCustomAnimations(old_frag_exit, new_frag_enter,
            // old_frag_enter, new_frag_exit);
            ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left,
                    R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
        }

        ft.replace(R.id.container, fragment, fragment.getTagText());
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            ft.addToBackStack(fragment.getTagText());
        }
        ft.commit();
    }

    @Override
    public void addMultipleFragments(BaseFragment[] fragments) {
    }

    public void clearBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager != null && fragmentManager.getBackStackEntryCount() != 0) {
            try {
                fragmentManager.popBackStackImmediate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void sortByBtnState(Button btn) {
        btn_price_l_to_h.setSelected(false);
        btn_price_h_to_l.setSelected(false);
        btn_psf_l_to_h.setSelected(false);
        btn_psf_h_to_l.setSelected(false);
        btn_possession_earlier.setSelected(false);
        btn_possession_later.setSelected(false);
        btn.setSelected(true);
    }


    private void sortDataRequest() {
        if (!sort_value.equals("")) {
            if (appliedFilterStateMap == null) {
                appliedFilterStateMap = new HashMap<>();
                for (Map.Entry<String, String> entry : mIntentDataObject.getData().entrySet()) {
                    appliedFilterStateMap.put(entry.getKey(), entry.getValue());
                }
            }
            appliedFilterStateMap.remove("priceltoh");
            appliedFilterStateMap.remove("pricehtol");
            appliedFilterStateMap.remove("possessionltoh");
            appliedFilterStateMap.remove("possessionhtol");
            appliedFilterStateMap.remove("psfltoh");
            appliedFilterStateMap.remove("psfhtol");
            if (sort_value.equals("priceltoh")) {
                appliedFilterStateMap.put("priceltoh", sort_value);
            } else if (sort_value.equals("pricehtol")) {
                appliedFilterStateMap.put("pricehtol", sort_value);
            } else if (sort_value.equals("Possessionltoh")) {
                appliedFilterStateMap.put("possessionltoh", sort_value);
            } else if (sort_value.equals("Possessionhtol")) {
                appliedFilterStateMap.put("possessionhtol", sort_value);
            } else if (sort_value.equals("pricePSFltoh")) {
                appliedFilterStateMap.put("psfltoh", sort_value);
            } else if (sort_value.equals("pricePSFhtol")) {
                appliedFilterStateMap.put("psfhtol", sort_value);
            }
            StringBuilder mStringBuilder = new StringBuilder("");
            for (Map.Entry<String, String> entry : appliedFilterStateMap.entrySet()) {
                if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                    mStringBuilder.append(entry.getKey());
                    mStringBuilder.append("=");
                    mStringBuilder.append(entry.getValue());
                    mStringBuilder.append("&");
                }
            }
            getProjectsData(mStringBuilder.toString());
        }
    }


    private int getAppliedFilterCount() {
        int count = 0;
        if (appliedFilterStateMap == null || appliedFilterStateMap.size() == 0)
            return count;
        else {
            if (appliedFilterStateMap.containsKey("project_status")) count++;
            if (appliedFilterStateMap.containsKey(ParamsConstants.P_TYPE)) count++;
            if (appliedFilterStateMap.containsKey("min-p") || appliedFilterStateMap.containsKey("max-p"))
                count++;
            if (appliedFilterStateMap.containsKey("min_area_range") || appliedFilterStateMap.containsKey("max_area_range"))
                count++;
            if (appliedFilterStateMap.containsKey("min_psf") || appliedFilterStateMap.containsKey("max_psf"))
                count++;
            if (appliedFilterStateMap.containsKey("bhk")) count++;
            if (appliedFilterStateMap.containsKey("lifestyles")) count++;
            //if(appliedFilterStateMap.containsKey("posession") ) count ++;
            return count;
        }
    }

}
