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
import androidx.core.view.MenuItemCompat;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.BaseVO;
import com.VO.PropertyCaraouselListVO;
import com.VO.PropertyCaraouselVO;
import com.activities.MyChat;
import com.activities.NotificationsActivity;
import com.activities.SalesActivity;
import com.adapters.PropertyListAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.database.AppDatabase;
import com.database.entity.UniversalContactsEntity;
import com.database.task.InsertUniversalContactsTask;
import com.nex3z.notificationbadge.NotificationBadge;
import com.sp.CustomAsyncTask.AsyncListner;
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
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProjectsListActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener, HostActivityInterface {

    private static final String TAG = ProjectsListActivity.class.getSimpleName();
    private Activity ctx = ProjectsListActivity.this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Builder> selectedBuilders = null;
    private HashMap<String, String> mapParams = new HashMap<>();
    private int GET_ALERT_REQ = 548;
    private final int SELECT_BUILDER_REQ = 480;
    public final static int LOGIN_REQ_CODE = 451;
    private PropertyListAdapter propertyAdapter;
    private ArrayList<PropertyCaraouselVO> propertyListVO = null;
    private IntentDataObject mIntentDataObject = null;
    private AsyncThread mAsyncThread = null;
    private View currentFooterView = null;
    private Toolbar toolbar = null;
    private boolean isInitialState = false;
    private NetworkErrorObject mNetworkErrorObject = null;
    private StringBuilder mStringBuilderForFilter;
    private String favProjId = "";
    private String getAlertParams;
    NotificationBadge mBadge;
    private BaseFragment selectedFragment;
    private MenuItem item;
    private String searchText = "";
    private String getfilterkey="";

    public AppDatabase mDbInstance;
    private static final int MULTI_FILTER = 452;
    private static final int HEAT_MAP_REQ = 453;
    private HashMap<String, String> appliedFilterStateMap = null;

    private LinearLayout ll_sort, ll_filter, ll_sort_by_root, ll_sort_sub_root;
    private TextView tv_sort_value, tv_filter_count;
    private Button btn_price_l_to_h, btn_price_h_to_l, btn_psf_l_to_h, btn_psf_h_to_l, btn_possession_earlier, btn_possession_later;
    private String sort_value = "priceltoh";
    private ImageView iv_blog, iv_transactions, iv_chat, iv_notification;
    private TextView iv_projects;
    RelativeLayout layout;
    LinearLayout linearLayout, emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);
        mBadge = findViewById(R.id.badge_count);
        isInitialState = true;
        toolbar = setDrawerAndToolbar();
        toolbar.setTitle(BMHConstants.BUILDER_NAME);
        mDbInstance = AppDatabase.getInstance();
        initializeFilterView();
        setListeners();

    }// End of onCreate().

    @Override
    protected void onStart() {
        super.onStart();
        if (Connectivity.isConnected(this)) {
            linearLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            getBadgeCountVolley();
            mIntentDataObject = new IntentDataObject();
            mIntentDataObject.putData(BMHConstants.BUILDER_ID_KEY, BMHConstants.CURRENT_BUILDER_ID);
            defaultUIState();
            if (!getfilterkey.equalsIgnoreCase("filterkey")) {
                getProjectsData(getRequestParams());
            }
            sortByBtnState(btn_price_l_to_h);
            getUniversalContacts();
        } else {
            linearLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setBackgroundResource(R.drawable.no_internet);
        }
    }

        @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void getUniversalContacts() {
        //     Utils.showToast(this, getString(R.string.txt_device_data_synced));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UNIVERSAL_CONTACTS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success"))
                                {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                        return;
                                    }
                                    if (mDbInstance != null) {
                                        insertUniversalContact(jsonObject);
                                    }
                                } else {
                                    Toast.makeText(ProjectsListActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ProjectsListActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void insertUniversalContact(JSONObject jsonObject) throws JSONException {
        List<UniversalContactsEntity> mUniversalList = new ArrayList<>();
        UniversalContactsEntity entity;
        JSONArray jsonArray = jsonObject.optJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject object = jsonArray.getJSONObject(i);
            String custName = object.optString("customer_name");
            String custMob = object.optString("customer_mobile");

            entity = new UniversalContactsEntity(custMob, custName, Utils.getCurrentDateTime(), 0, "");
            mUniversalList.add(entity);

        }
        new InsertUniversalContactsTask(mUniversalList).execute();
    }

    private void defaultUIState() {
        if (mapParams == null || toolbar == null) return;

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
        ll_filter.setOnClickListener(mOnClickListener);
        ll_sort.setOnClickListener(mOnClickListener);

        btn_price_l_to_h.setOnClickListener(mOnClickListener);
        btn_price_h_to_l.setOnClickListener(mOnClickListener);
        btn_psf_l_to_h.setOnClickListener(mOnClickListener);
        btn_psf_h_to_l.setOnClickListener(mOnClickListener);
        btn_possession_earlier.setOnClickListener(mOnClickListener);
        btn_possession_later.setOnClickListener(mOnClickListener);
        ll_sort_by_root.setOnClickListener(mOnClickListener);
        ll_sort_sub_root.setOnClickListener(mOnClickListener);

        iv_projects.setOnClickListener(mOnClickListener);
        iv_blog.setOnClickListener(mOnClickListener);
        iv_transactions.setOnClickListener(mOnClickListener);
        iv_chat.setOnClickListener(mOnClickListener);
        iv_notification.setOnClickListener(mOnClickListener);


    }

    private void getBadgeCountVolley() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BADGE_COUNT),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success")) {
                                JSONObject object = jsonObject.optJSONObject("data");
                                int badge_count = object.optInt("unread_count");
                                mBadge.setNumber(badge_count);
                            } else {
                                showToast(getString(R.string.something_went_wrong));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.APP_TYPE, BMHConstants.SALES_PERSON);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        BMHApplication.getInstance().

                addToRequestQueue(stringRequest, "headerRequest");
    }

    private void initializeFilterView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        View footerV = getLayoutInflater().inflate(R.layout.row_alert_footer, null);
        TextView textViewAlertTitle = (TextView) footerV.findViewById(R.id.textViewAlertTitle);
        textViewAlertTitle.setText("Get notified as soon as a new property is added in this locality.");
        ll_filter = (LinearLayout) findViewById(R.id.ll_filter);
        ll_sort = (LinearLayout) findViewById(R.id.ll_sort);
        ll_sort_by_root = (LinearLayout) findViewById(R.id.ll_sort_by_root);
        ll_sort_sub_root = (LinearLayout) findViewById(R.id.ll_sort_sub_root);

        tv_sort_value = (TextView) findViewById(R.id.tv_sort_value);
        tv_filter_count = (TextView) findViewById(R.id.tv_filter_count);

        btn_price_l_to_h = (Button) findViewById(R.id.btn_price_l_to_h);
        btn_price_h_to_l = (Button) findViewById(R.id.btn_price_h_to_l);

        btn_psf_l_to_h = (Button) findViewById(R.id.btn_psf_l_to_h);
        btn_psf_h_to_l = (Button) findViewById(R.id.btn_psf_h_to_l);

        btn_possession_earlier = (Button) findViewById(R.id.btn_possession_earlier);
        btn_possession_later = (Button) findViewById(R.id.btn_possession_later);

        iv_projects = (TextView) findViewById(R.id.iv_projects);
        iv_blog = (ImageView) findViewById(R.id.iv_blog);
        iv_transactions = (ImageView) findViewById(R.id.iv_transactions);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        iv_notification = (ImageView) findViewById(R.id.iv_notification);
        layout = findViewById(R.id.layout_view);
        linearLayout = findViewById(R.id.ll_filter_root);
        emptyView = findViewById(R.id.empty_view);
    }

    private void filterSorting(String result) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.property_list, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (android.widget.SearchView) searchItem.getActionView();

        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Detect SearchView clear button click
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return true;

            }

                });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                //showToast("Expand");
                setItemsVisibility(menu, menuItem, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                //showToast("Collapse");
                setItemsVisibility(menu, menuItem, true);
                searchView.setQuery("", false);
                return true;
            }
        });

        searchView.setOnQueryTextListener(mOnQueryTextListener);
        return super.onCreateOptionsMenu(menu);

    }

    SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            searchText = newText;
            if (propertyAdapter != null) {
                propertyAdapter.performFiltering(newText);
            }
            return true;
        }
    };

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        this.item = item;
        switch (id) {
			case R.id.action_filter:
                if(mIntentDataObject != null) {
                    Intent mIntent = new Intent(ProjectsListActivity.this, FilterActivityActivity.class);
                    mIntentDataObject.setFilterStateMap(appliedFilterStateMap);
                    mIntentDataObject.setObj(selectedBuilders);
                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    startActivityForResult(mIntent, MULTI_FILTER);
                }
				break;
			case R.id.action_heatmap:
                Intent mIntent = new Intent(ProjectsListActivity.this, ProjectHeatmapActivity.class);
                mIntentDataObject.setFilterStateMap(appliedFilterStateMap);
                mIntentDataObject.setObj(selectedBuilders);
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivityForResult(mIntent, HEAT_MAP_REQ);
				break;
			case android.R.id.home:
				finish();
				break;

            case R.id.search:
                SearchView searchView = (android.widget.SearchView) item.getActionView();
                searchView.setIconified(false);
                break;
        }
        return super.onOptionsItemSelected(item);
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
              else {

          //  mRecyclerView.setBackgroundResource(R.drawable.no_lead_assigned);

        }
        //if (currentFooterView != null) mRecyclerView.removeFooterView(currentFooterView);
        if (propertyListVO == null || propertyListVO.size() == 0) {
          //    currentFooterView = getFooterView(true);
            //  mRecyclerView.addView(currentFooterView);
            emptyView.setVisibility(View.VISIBLE);

            emptyView.setBackgroundResource(R.drawable.no_item_searched);
          // propertyAdapter.updateListData(propertyListVO);
        } else {
            //   currentFooterView = (getFooterView(false));
            //  mRecyclerView.addView(currentFooterView);

            emptyView.setVisibility(View.GONE);
mRecyclerView.setClickable(false);

            /*propertyAdapter.notifyDataSetChanged();
            propertyAdapter.updateListData(propertyListVO);
            mRecyclerView.smoothScrollToPosition(0);*/
        }
        mOnQueryTextListener.onQueryTextChange(searchText);

    }

 /*   OnClickListener favClickListener = new OnClickListener() {
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
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectsListActivity.this, UIEventType.RETRY_FEV,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            favRequest(favProjId);
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(ProjectsListActivity.this, getString(R.string.check_your_internet_connection));
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
                mIntentDataObject.putData(ParamsConstants.TYPE, ParamsConstants.BUY);
                Intent mIntent = new Intent(ProjectsListActivity.this, ProjectDetailActivity.class);
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivity(mIntent);
            }
        }
    };*/

    public void favRequest(String id) {
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
        mBean.setJson(mStringBuilder.toString());
        mBean.setRequestObj(id);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    public void showEmptyView(boolean b) {
        if (b) {
            emptyView.setVisibility(View.GONE);
            emptyView.setBackgroundResource(R.drawable.no_item_searched);
        }


    }

  /*  public View getFooterView(boolean isEmptyView) {
        View footerView = getLayoutInflater().inflate(R.layout.row_alert_footer, null);
        Button getAlerts = (Button) footerView.findViewById(R.id.btnGetAlerts);
        getAlerts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnquryDialog();
            }
        });
        if (isEmptyView) {
            emptyView.setVisibility(View.VISIBLE);
            //  LinearLayout mView = (LinearLayout) findViewById(R.id.empty_view);
            LinearLayout.LayoutParams emptyViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            emptyViewParams.weight = 1.0f;//0.75f;
            emptyViewParams.setMargins(0, 0, 0, (int) Utils.dp2px(5f, this));
            LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            footerParams.weight = 0.0f;///0.25f;
            View emptView = getLayoutInflater().inflate(R.layout.search_empty_view, null);
            emptView.setLayoutParams(emptyViewParams);
            footerView.setLayoutParams(footerParams);
            emptyView.addView(emptView);
            //mView.addView(footerView);
            return emptyView;
        } else {
            return footerView;
        }
    }*/

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    private void submitAlert() {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
            BaseVO baseVo;

            @Override
            public void OnBackgroundTaskCompleted() {
                if (baseVo == null) {
                    //showToast("Something went wrong. Try again");
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
        } else if (requestCode == MULTI_FILTER && resultCode == RESULT_OK) {
            if (data != null && data.getSerializableExtra(IntentDataObject.OBJ) != null
                    && data.getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
                IntentDataObject mIntentDataObject = (IntentDataObject) data.getSerializableExtra(IntentDataObject.OBJ);
                appliedFilterStateMap = mIntentDataObject.getData();
                getfilterkey=data.getStringExtra("filterkey");
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
                    getProjectsData1(mStringBuilder.toString());
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
                                mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectsListActivity.this, UIEventType.RETRY_ALERT,
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (ConnectivityReceiver.isConnected()) {
                                                    //TODO: network call
                                                    sendAlertRequest(getAlertParams);
                                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                                    mNetworkErrorObject = null;
                                                } else {
                                                    Utils.showToast(ProjectsListActivity.this, getString(R.string.check_your_internet_connection));
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
                        Intent i = new Intent(ProjectsListActivity.this, TermsWebActivity.class);
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
                Intent mIntent = new Intent(ProjectsListActivity.this, ProjectDetailActivity.class);
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
                case R.id.ll_sort:
                    int visibility = (ll_sort_by_root.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                    ll_sort_by_root.setVisibility(visibility);
                    break;
                case R.id.ll_filter:
                    goToFilterScreen();
                    break;
                case R.id.btn_price_l_to_h:
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
                    break;
                case R.id.ll_sort_sub_root:
                    //TODO: do nothing
                    break;
                case R.id.iv_projects:
                    break;
                case R.id.iv_blog:
                    //TODO: do nothing
                    Intent blogIntent = new Intent(ProjectsListActivity.this, BlogActivity.class);
                    startActivity(blogIntent);
                    break;
                case R.id.iv_transactions:
                    Intent transactionIntent = new Intent(ProjectsListActivity.this, SalesActivity.class);
                    startActivity(transactionIntent);
                    break;
                case R.id.iv_chat:
                    String id = app.getFromPrefs(BMHConstants.USERID_KEY);
                    if (!id.isEmpty()) {
                        Intent chatIntent = new Intent(ProjectsListActivity.this, MyChat.class);
                        startActivity(chatIntent);
                    } else {
                        app.showToastAtCenter(ProjectsListActivity.this, "Please login.");
                        Intent i = new Intent(ProjectsListActivity.this, LoginActivity.class);
                        startActivityForResult(i, SearchPropertyActivity.FAV_REQ_CODE);
                    }
                    break;
                case R.id.iv_notification:
                    id = app.getFromPrefs(BMHConstants.USERID_KEY);
                    if (!id.isEmpty()) {
                        Intent notifyIntent = new Intent(ProjectsListActivity.this, NotificationsActivity.class);
                        startActivity(notifyIntent);
                    } else {
                        app.showToastAtCenter(ProjectsListActivity.this, "Please login.");
                        Intent i = new Intent(ProjectsListActivity.this, LoginActivity.class);
                        startActivityForResult(i, SearchPropertyActivity.FAV_REQ_CODE);
                    }
                    break;

                default:
                    break;
            }

        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getProjectsData1(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.SEARCH_PROJECTS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.SEARCH_PROJECTS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ProjectsListActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }
    private void getProjectsData(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.SEARCH_PROJECTS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.SEARCH_PROJECTS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ProjectsListActivity.this, mOnCancelListener);
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
                    case SEARCH_PROJECTS:
                        if (mBean.getJson() != null) {
                            PropertyCaraouselListVO propVo = null;
                            try {
                                JSONObject jsonObj = new JSONObject(mBean.getJson());
                                if (jsonObj != null) {
                                    propVo = new PropertyCaraouselListVO(jsonObj);
                                }
                                if (propVo != null && propVo.isSuccess()) {
                                    propertyListVO = propVo.getPropertiesArr();
                                    if (propertyListVO != null && !propertyListVO.isEmpty()) {
                                        setPropertyListAdapter(propertyListVO);
                                        if (isInitialState) {
                                            selectedBuilders = getBuilders(propertyListVO);
                                            supportInvalidateOptionsMenu();
                                            isInitialState = false;
                                        }
                                    } else {
                                        propertyListVO = null;
                                        setPropertyListAdapter(null);
                                    }
                                } else {
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

            if (appliedFilterStateMap.containsKey(ParamsConstants.CITY_ID)) count++;
            if (appliedFilterStateMap.containsKey(ParamsConstants.LOCATION)) count++;
            if (appliedFilterStateMap.containsKey(ParamsConstants.SUB_LOCATION_ID)) count++;

            //if(appliedFilterStateMap.containsKey("posession") ) count ++;
            return count;
        }
    }

    private void goToFilterScreen()

             {

        if (mIntentDataObject != null) {
            Intent mIntent = new Intent(this, FilterActivityActivity.class);
            mIntentDataObject.setFilterStateMap(appliedFilterStateMap);
            mIntentDataObject.setObj(selectedBuilders);
            mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
            startActivityForResult(mIntent, MULTI_FILTER);

        }
    }
}
