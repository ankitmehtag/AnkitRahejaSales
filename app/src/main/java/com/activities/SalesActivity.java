package com.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.appnetwork.VolleyMultipartRequest;
import com.appnetwork.WEBAPI;
import com.database.Converters;
import com.database.UpdateStatusConverter;
import com.database.entity.CorporateActivityMasterEntity;
import com.database.entity.SalesClosureLeadsDetailEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusClosureEntity;
import com.database.entity.SubStatusMeetingEntity;
import com.database.entity.SubStatusNotInterestedEntity;
import com.database.entity.SubStatusSiteVisitEntity;
import com.database.task.ClosureMasterTask;
import com.database.task.CustomerInfoTask;
import com.database.task.FetchSalesLeadsByUserTypeTask;
import com.database.task.GetSalesLeadDetailTask;
import com.database.task.InsertAddAppointmentTask;
import com.database.task.InsertBrokerMasterTask;
import com.database.task.InsertClosureLeadsDetailTask;
import com.database.task.InsertCorporateActivityTask;
import com.database.task.InsertMultiSelectTask;
import com.database.task.InsertProjectMasterTask;
import com.database.task.InsertSalesLeadDetailsTask;
import com.database.task.InsertStatusMasterTask;
import com.database.task.NotInterestedMasterTask;
import com.database.task.SyncDataTask;
import com.database.task.SyncRecordingTask;
import com.database.task.UpdateCallRecordingTask;
import com.database.task.UpdateSyncDataTask;
import com.database.AppDatabase;
import com.database.entity.CallRecordingEntity;
import com.database.entity.ClosureMasterEntity;
import com.database.entity.CustomerInfoEntity;
import com.database.entity.NotInterestedMasterEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SalesBrokerMasterEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.StatusMasterEntity;
import com.fragments.AssignedFragment;
import com.fragments.FollowUpFragment;
import com.fragments.SalesClosuresFragment;
import com.google.gson.JsonSyntaxException;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.OnMenuItemClickListener;
import com.jsonparser.JsonParser;
import com.model.AsmSalesLeadDetailModel;
import com.model.AsmSalesModel;
import com.model.CorporateActivityModel;
import com.model.CorporateModel;
import com.model.LeadStatus;
import com.model.NotInterestedLead;
import com.model.NotInterestedModel;
import com.model.NotificationDataModel;
import com.model.Projects;
import com.model.SubStatus;
import com.nex3z.notificationbadge.NotificationBadge;
import com.services.AlarmJobServices;
import com.sp.BMHApplication;
import com.sp.BaseFragmentActivity;
import com.sp.BlogActivity;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SalesActivity extends BaseFragmentActivity implements TabLayout.OnTabSelectedListener,
        OnMenuItemClickListener, SyncRecordingTask.ISyncRecordingCommunicator, FetchSalesLeadsByUserTypeTask.ISalesLeadCommunicator,
        SyncDataTask.ISyncDataCommunicator, GetSalesLeadDetailTask.ISalesLeadCommunicator,
        InsertAddAppointmentTask.IAppointmentCommunicator {
    Toolbar toolbar;
    private static final String TAG = SalesActivity.class.getCanonicalName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private BMHApplication app;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public Bundle masterBundle;

    public Bundle masterBundleAssigned,masterBundleFollowup,masterBundleClosure;

    private ProgressDialog dialog;
    private String userDesignation;
    NotificationBadge mBadge;
    public AppDatabase customerDb;
    private List<CustomerInfoEntity> mCustomerList;
    private List<SalesLeadDetailEntity> mLeadDetailList;
    private int hasExtCallPermission;
    private List<String> permissionsCall = new ArrayList<>();
    private String mobileNo;
    FloatingActionButton fab_add_appointment;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};
    private ImageView iv_blog, iv_notification, iv_chat, iv_projects;
    private TextView iv_sales;
    SalesBrokerMasterEntity salesBrokerMasterEntity;
    ProjectMasterEntity projectMasterEntity;
    CorporateActivityMasterEntity corporateMasterEntity;
    private StatusMasterEntity statusMasterEntity;
    private NotInterestedMasterEntity notInterestedMasterEntity;
    private ClosureMasterEntity closureMasterEntity;
    private List<CorporateActivityMasterEntity> mCorporateMasterList = new ArrayList<>();
    private List<SalesBrokerMasterEntity> mBrokerMasterList = new ArrayList<>();
    private List<ProjectMasterEntity> mProjectsMasterList = new ArrayList<>();
    private List<StatusMasterEntity> mStatusMasterList = new ArrayList<>();
    private List<NotInterestedMasterEntity> mNotInterestedMasterList = new ArrayList<>();
    private List<ClosureMasterEntity> mClosureMasterList = new ArrayList<>();

    ArrayList<NotInterestedModel> notInterestedList = new ArrayList<>();
    ArrayList<NotInterestedModel> closureList = new ArrayList<>();

    public static ArrayList<LeadStatus> spLeadStatusList = new ArrayList<>();
    public static ArrayList<Projects> projectsMasterList = new ArrayList<>();
    public static ArrayList<NotInterestedLead> spBrokersList = new ArrayList<>();
    public static ArrayList<NotInterestedLead> spClosureList = new ArrayList<>();
    public static ArrayList<NotInterestedLead> spNotInterestedList = new ArrayList<>();
    SalesLeadDetailEntity ldEntity;
    public ArrayList<AsmSalesModel> mSpAssignedList;
    public ArrayList<AsmSalesModel> mSpFollowUpList;
    public ArrayList<AsmSalesModel> mSpClosureLeadsList;
    public ArrayList<AsmSalesLeadDetailModel> mDetailsList;
    ArrayList<CorporateActivityModel> corporateActivityList = new ArrayList<>();
    SwipeRefreshLayout swipeContainer;

    @Override
    protected String setActionBarTitle() {
        return "";

    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        toolbar = findViewById(R.id.toolbar);
        iv_blog = findViewById(R.id.iv_blog);
        iv_notification = findViewById(R.id.iv_notification);
        iv_chat = findViewById(R.id.iv_chat);
        iv_projects = findViewById(R.id.iv_projects);
        iv_sales = findViewById(R.id.iv_sales);
        mBadge = findViewById(R.id.badge_count);
        fab_add_appointment = findViewById(R.id.fab_add_appointment);
        swipeContainer = findViewById(R.id.swipe_refresh);
        toolbar = setDrawerAndToolbar();
        tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.viewPager);
        app = (BMHApplication) getApplication();
        customerDb = AppDatabase.getInstance();
        dialog = new ProgressDialog(SalesActivity.this);
        getBadgeCount();

        userDesignation = app.getFromPrefs((BMHConstants.USER_DESIGNATION));
        if (!userDesignation.equalsIgnoreCase("0"))
            fab_add_appointment.setVisibility(View.VISIBLE);
        toolbar.setTitle(getString(R.string.txt_toolbar_sales, "All Leads"));

        if (tabLayout.getTabCount() > 0)
            tabLayout.removeAllTabs();
        if    (userDesignation.equalsIgnoreCase("0")) {
            tabLayout.addTab(tabLayout.newTab().setText(SalesActivity.this.getResources().getString(R.string.tab_assigned)));
            tabLayout.addTab(tabLayout.newTab().setText(SalesActivity.this.getResources().getString(R.string.tab_follow_up)));
            tabLayout.addTab(tabLayout.newTab().setText(SalesActivity.this.getResources().getString(R.string.tab_closure)));

        }
             else {
            tabLayout.addTab(tabLayout.newTab().setText(SalesActivity.this.getResources().getString(R.string.tab_follow_up)));
            tabLayout.addTab(tabLayout.newTab().setText(SalesActivity.this.getResources().getString(R.string.tab_closure)));
        }

        fab_add_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointmentIntent = new Intent(SalesActivity.this, AddAppointmentActivity.class);
                appointmentIntent.putParcelableArrayListExtra("broker_list", spBrokersList);
                appointmentIntent.putParcelableArrayListExtra("lead_status_list", spLeadStatusList);
                appointmentIntent.putParcelableArrayListExtra("master_project_list", projectsMasterList);
                appointmentIntent.putParcelableArrayListExtra("corporate_list", corporateActivityList);
                startActivity(appointmentIntent);
                }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                //do nothing
            }
        }
        // API CALL OF ADD APPOINTMENT
        getCorporateMasters();
        refreshLeadData();
        iv_projects.setOnClickListener(mOnClickListener);
        iv_blog.setOnClickListener(mOnClickListener);
        iv_sales.setOnClickListener(mOnClickListener);
        iv_chat.setOnClickListener(mOnClickListener);
        iv_notification.setOnClickListener(mOnClickListener);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        setToRefresh(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK && requestCode==2)
        {

            Bundle extras = data.getExtras();
            String result = extras.getString("abc");

            //data.getStringExtra("");
            //data.getStringExtra("Tabname");
            Log.d("xyz",result);

            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }

    }


    /* @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);


        if(resultCode==RESULT_OK)
        {

            Bundle extras = data.getExtras();
            String result = extras.getString("abc");

            //data.getStringExtra("");
            //data.getStringExtra("Tabname");
            Log.d("xyz",result);

            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }



    }

*/





  /*  @Override
    public void onActivityReenter(int resultCode, Intent data) {
       // datas
        super.onActivityReenter(resultCode,data);


        if(requestCode==RESULT_OK)
        {

            Bundle extras = data.getExtras();
            String result = extras.getString("abc");

            //data.getStringExtra("");
            //data.getStringExtra("Tabname");
            Log.d("xyz",result);

            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }



    }*/
   /* @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            // check if the request code is same as what is passed  here it is 2
            if(requestCode==2)
            {

                Bundle extras = data.getExtras();
                String result = extras.getString("abc");

                //data.getStringExtra("");
                //data.getStringExtra("Tabname");
                Log.d("xyz",result);

                //String message=data.getStringExtra("MESSAGE");
                // textView1.setText(message);
            }
        }*/

    private void setToRefresh(boolean refresh) {
        if (refresh) {
            swipeContainer.setEnabled(true);
            swipeContainer.setRefreshing(true);
        } else {
            swipeContainer.setRefreshing(false);
            swipeContainer.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

           }

    private void refreshLeadData() {
        if (Connectivity.isConnected(this)) {
            // CHECK OFFLINE CHANGES AVAILABILITY
            new SyncDataTask(SalesActivity.this, BMHConstants.LEAD_PHASE_SALES).execute();
        } else {
            // LOAD DATA FROM OFFLINE TABLES
            loadDataFromDatabase();
        }
      }

    private void loadDataFromDatabase() {
        dialog.setMessage("Please wait...loading offline data");
        dialog.setCancelable(false);
        if (!isFinishing())
            dialog.show();
        // SP USER
        if (userDesignation.equalsIgnoreCase("0")) {
            new FetchSalesLeadsByUserTypeTask(this, getString(R.string.txt_sp)).execute();
        } else {
            // ASM
            new FetchSalesLeadsByUserTypeTask(this, getString(R.string.txt_asm)).execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (app.getBooleanFromPrefs(BMHConstants.IS_BACK_FROM_UPDATE)) {
            if (dialog == null)
                dialog = new ProgressDialog(SalesActivity.this);
            refreshLeadData();
        }
    }

    private void getBadgeCount() {
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
        })
               {
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

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_projects:
                    Intent projectsIntent = new Intent(SalesActivity.this, ProjectsListActivity.class);
                    startActivity(projectsIntent);
                    finish();
                    break;
                case R.id.iv_blog:
                    Intent blogIntent = new Intent(SalesActivity.this, BlogActivity.class);
                    startActivity(blogIntent);
                    finish();
                    break;
                case R.id.iv_sales:
                    break;
                case R.id.iv_chat:
                    //TODO: do nothing
                    Intent chatIntent = new Intent(SalesActivity.this, MyChat.class);
                    startActivity(chatIntent);
                    finish();
                    break;
                case R.id.iv_notification:
                    //TODO: do nothing
                    Intent notifyIntent = new Intent(SalesActivity.this, NotificationsActivity.class);
                    startActivity(notifyIntent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    private void getCorporateMasters() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.MASTERS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                parseCorporateJson(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
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

    private void parseCorporateJson(JSONObject jsonObject) {
        CorporateModel corporateModel = (CorporateModel) JsonParser.convertJsonToBean(APIType.MASTERS, jsonObject.toString());
        if (corporateModel != null) {
            ArrayList<CorporateActivityModel> activityModelArrayList = corporateModel.getData();
            int activityLength = activityModelArrayList.size();
            if (activityLength > 0 && corporateActivityList.size() > 0)
                corporateActivityList.clear();
            for (int j = 0; j < activityLength; j++) {
                String activityId = corporateModel.getData().get(j).getActivity_id();
                String activityName = corporateModel.getData().get(j).getActivity_name();
                ArrayList<Projects> projects = corporateModel.getData().get(j).getProject();
                corporateActivityList.add(new CorporateActivityModel(activityId, activityName, projects));

                corporateMasterEntity = new CorporateActivityMasterEntity(activityId, activityName, Converters.fromArrayList(projects), Utils.getCurrentDateTime());
                mCorporateMasterList.add(corporateMasterEntity);
            }
            new InsertCorporateActivityTask(mCorporateMasterList).execute();
        }
    }

    private void getSalesData() {
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        if (!isFinishing())
            dialog.show();
String basee_url_followUP = "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.2/sales/getFollowupLeads.php";
String base_url_assigned = " http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.2/sales/getAssignedLeads.php";
String base_url_closure  = " http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.2/sales/getClosureLeads.php";

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.USER_SALES),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                        Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (customerDb != null)
                                        insertMastersToDb(jsonObject);
                                    // List<CustomerInfoEntity> mList= mDbInstance.getCustomerDao().getAll();
                                    masterBundle = new Bundle();
                                    masterBundle.putString("JSON_STRING", response);
                                    Utils.showToast(SalesActivity.this, getString(R.string.txt_server_data_synced));
                                    prepareTabs();

                                           }
                                    else {
                                    Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d(TAG, "VolleyError " + error);
                    }
                }
        )*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, base_url_assigned,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                      //  Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (customerDb != null)
                                        insertMastersToDb(jsonObject);
                                    // List<CustomerInfoEntity> mList= mDbInstance.getCustomerDao().getAll();
                                    masterBundleAssigned = new Bundle();
                                    masterBundleAssigned.putString("JSON_STRING", response);

                                   // masterBundle = new Bundle();
                                   // masterBundle.putString("JSON_STRING", response);
                                  //  Utils.showToast(SalesActivity.this, getString(R.string.txt_server_data_synced));
                                    prepareTabs();

                                }
                                else {
                                    Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d(TAG, "VolleyError " + error);
                    }
                }
        )

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                 params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                //params.put(ParamsConstants.USER_ID, "117");
                params.put(ParamsConstants.USER_DESIGNATION, userDesignation);
                params.put("page", "1");
                //params.put("searchkey", "kirti");
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


        StringRequest stringRequestFollowUP = new StringRequest(Request.Method.POST, basee_url_followUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                       // Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (customerDb != null)
                                        insertMastersToDb(jsonObject);
                                    // List<CustomerInfoEntity> mList= mDbInstance.getCustomerDao().getAll();
                                   // masterBundle = new Bundle();
                                    //masterBundle.putString("JSON_STRING", response);
                                    masterBundleFollowup = new Bundle();
                                    masterBundleFollowup.putString("JSON_STRING", response);

                                    Utils.showToast(SalesActivity.this, getString(R.string.txt_server_data_synced));
                                    prepareTabs();

                                }
                                else {
                                    Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d(TAG, "VolleyError " + error);
                    }
                }
        )

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                 params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
               // params.put(ParamsConstants.USER_ID, "117");
                params.put(ParamsConstants.USER_DESIGNATION, userDesignation);
                params.put("page", "1");
                //params.put("searchkey", "kirti");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequestFollowUP, "headerRequest");


        StringRequest stringRequestClosure = new StringRequest(Request.Method.POST, base_url_closure,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                       // Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (customerDb != null)
                                        insertMastersToDb(jsonObject);
                                    // List<CustomerInfoEntity> mList= mDbInstance.getCustomerDao().getAll();
                                    masterBundleClosure = new Bundle();
                                    masterBundleClosure.putString("JSON_STRING", response);

                                    // masterBundle = new Bundle();
                                    // masterBundle.putString("JSON_STRING", response);
                                    //  Utils.showToast(SalesActivity.this, getString(R.string.txt_server_data_synced));
                                    prepareTabs();

                                }
                                else {
                                    Toast.makeText(SalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d(TAG, "VolleyError " + error);
                    }
                }
        )

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
               // params.put(ParamsConstants.USER_ID, "117");
                params.put(ParamsConstants.USER_DESIGNATION, userDesignation);
                params.put("page", "1");
                //params.put("searchkey", "kirti");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequestClosure, "headerRequest");

    }

    private void prepareTabs() {
        if (dialog == null) {
            dialog = new ProgressDialog(SalesActivity.this);
        }
        dialog.show();
        mSectionsPagerAdapter = new SalesActivity.SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.sky_blue));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

        tabLayout.setTabTextColors(
                ContextCompat.getColor(SalesActivity.this, R.color.black_alpha_40),
                ContextCompat.getColor(SalesActivity.this, R.color.black)
        );
        tabLayout.addOnTabSelectedListener(SalesActivity.this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        if ((app.getTabFromPrefs(getString(R.string.key_navigation)).equalsIgnoreCase(getString(R.string.tag_notification)))) {
            mViewPager.setCurrentItem(1);
            mViewPager.setOffscreenPageLimit(2);
            mSectionsPagerAdapter.getItem(1);
        } else if (app.getTabFromPrefs(getString(R.string.key_navigation)).equalsIgnoreCase(getString(R.string.tab_assigned))) {
            if (userDesignation.equalsIgnoreCase("0")) {
                mViewPager.setCurrentItem(0);
                mViewPager.setOffscreenPageLimit(2);
                mSectionsPagerAdapter.getItem(0);
            }
        } else if (app.getTabFromPrefs(getString(R.string.key_navigation)).equalsIgnoreCase(getString(R.string.tab_follow_up))) {
            if (userDesignation.equalsIgnoreCase("0")) {
                mViewPager.setCurrentItem(1);
                mViewPager.setOffscreenPageLimit(2);
                mSectionsPagerAdapter.getItem(1);
            } else {
                mViewPager.setCurrentItem(0);
                mViewPager.setOffscreenPageLimit(2);
                mSectionsPagerAdapter.getItem(0);
            }
        } else if (app.getTabFromPrefs(getString(R.string.key_navigation)).equalsIgnoreCase(getString(R.string.tab_closure))) {
            if (userDesignation.equalsIgnoreCase("0")) {
                mViewPager.setCurrentItem(2);
                mViewPager.setOffscreenPageLimit(2);
                mSectionsPagerAdapter.getItem(2);
            } else {
                mViewPager.setCurrentItem(1);
                mViewPager.setOffscreenPageLimit(2);
                mSectionsPagerAdapter.getItem(1);
            }
        }
        app.clearTabPref();
        app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, false);
        if (dialog.isShowing())
            dialog.dismiss();
        setToRefresh(false);
    }

    private void insertMastersToDb(JSONObject jsonObject) throws JSONException {
        String projectName = "";
        JSONArray projectArray = jsonObject.optJSONArray("projects");
        if (projectArray != null) {
            if (projectsMasterList != null)
                projectsMasterList.clear();
            int projLength = projectArray.length();
            for (int i = 0; i < projLength; i++) {
                JSONObject object = projectArray.getJSONObject(i);
                String projectId = object.optString("project_id");
                projectName = object.optString("project_name");
                projectsMasterList.add(new Projects(projectId, projectName));
                projectMasterEntity = new ProjectMasterEntity(projectId, projectName);
                mProjectsMasterList.add(projectMasterEntity);
            }
            new InsertProjectMasterTask(mProjectsMasterList).execute();
        }
        JSONArray brokersArray = jsonObject.optJSONArray("brokers");
        if (brokersArray != null) {
            if (spBrokersList != null)
                spBrokersList.clear();
            int brokerLength = brokersArray.length();
            for (int i = 0; i < brokerLength; i++) {
                JSONObject object = brokersArray.getJSONObject(i);
                String salesId = object.optString("id");
                String salesName = object.optString("title");
                String address = object.optString("address");
                spBrokersList.add(new NotInterestedLead(salesId, salesName, address));
                salesBrokerMasterEntity = new SalesBrokerMasterEntity(salesId, salesName, address);
                mBrokerMasterList.add(salesBrokerMasterEntity);
            }
            new InsertBrokerMasterTask(mBrokerMasterList).execute();
        }
        JSONArray leadArray = jsonObject.optJSONArray("lead_status");
        if (leadArray != null) {
            if (spLeadStatusList != null)
                spLeadStatusList.clear();
            int leadLength = leadArray.length();
            for (int i = 0; i < leadLength; i++) {
                JSONObject object = leadArray.getJSONObject(i);
                String dispositionId = object.optString("disposition_id");
                String name = object.optString("title");
                spLeadStatusList.add(new LeadStatus(dispositionId, name));
                statusMasterEntity = new StatusMasterEntity(name, dispositionId, 0);
                mStatusMasterList.add(statusMasterEntity);
            }
            new InsertStatusMasterTask(mStatusMasterList).execute();
        }

        JSONArray notInterestedArray = jsonObject.optJSONArray("not_interested");
        if (leadArray != null) {
            int notInterestedLength = notInterestedArray.length();
            for (int i = 0; i < notInterestedLength; i++) {
                JSONObject object = notInterestedArray.getJSONObject(i);
                String id = object.optString("id");
                String name = object.optString("title");
                notInterestedList.add(new NotInterestedModel(id, name));
                notInterestedMasterEntity = new NotInterestedMasterEntity(id, name);
                mNotInterestedMasterList.add(notInterestedMasterEntity);
            }
            new NotInterestedMasterTask(mNotInterestedMasterList).execute();
        }

        JSONArray closureArray = jsonObject.optJSONArray("closure");
        if (leadArray != null) {
            int closureLength = closureArray.length();
            for (int i = 0; i < closureLength; i++) {
                JSONObject object = closureArray.getJSONObject(i);
                String id = object.optString("id");
                String name = object.optString("title");
                closureList.add(new NotInterestedModel(id, name));
                closureMasterEntity = new ClosureMasterEntity(id, name);
                mClosureMasterList.add(closureMasterEntity);
            }

                new ClosureMasterTask(mClosureMasterList).execute();
        }

        CustomerInfoEntity customer;
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray != null) {
            mCustomerList = new ArrayList<>();
            mLeadDetailList = new ArrayList<>();
            int arrayLength = jsonArray.length();
                 for (int i = 0; i < arrayLength; i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                String cust_mob = jObject.optString("customer_mobile");
                String enquiryId = jObject.optString("enquiry_id");
                String cust_name = jObject.optString("customer_name");
                int isAssigned = jObject.optInt("isAssigned");
                String isLeadType = jObject.optString("is_lead_type");
                int isLeadAccepted = jObject.optInt("isLeadAccepted");
                customer = new CustomerInfoEntity(cust_mob, enquiryId, cust_name, BMHConstants.LEAD_PHASE_SALES, String.valueOf(isAssigned));
                // create worker thread to insert data into database
                mCustomerList.add(customer);
                String lastUpdatedOn = jObject.optString("lastupdatedon");
                String scheduledDateTime = jObject.optString("scheduledatetime");

                JSONObject object = jObject.optJSONObject("details");
                ArrayList<Projects> selectedProjList = new ArrayList<>();

                String custMob = object.optString("Mobile_No");
                String custAlternateMob = object.optString("Alternate_Mobile_No");
                String custName = object.optString("Name");
                String custEmail = object.optString("Email_ID");
                String campaignName = object.optString("Campaign");
                String campaignDate = object.optString("Campaign_Date");
                String budget = object.optString("Budget");
                String currentStatus = object.optString("Current_Status");
                String date = object.optString("date");
                String time = object.optString("time");
                String address = object.optString("Address");
                String remark = object.optString("remark");
                String leadType = object.optString("lead_type");
                String broker_name = object.optString("broker_name");
                int noOfPersons = object.optInt("no_of_persons");
                String unitNo = object.optString("unit_no");
                String amount = object.optString("amount");
                String mProjectName = object.optString("project_name");
                String towerNo = object.optString("tower_no");
                String chequeNo = object.optString("cheque_number");
                String chequeDate = object.optString("cheque_date");
                String bankName = object.optString("bank_name");

                JSONArray selectedList = object.optJSONArray("selected_project");
                if (selectedList != null) {
                    int selectedLength = selectedList.length();
                    for (int j = 0; j < selectedLength; j++) {
                        JSONObject obj = selectedList.optJSONObject(j);
                        selectedProjList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                    }
                }
                String subStatusItem = "";
                ArrayList<SubStatus> updateStatusList = new ArrayList<>();
                if (isAssigned == 2) {
                    subStatusItem = object.optString("closureSubStatus");
                } else {
                    JSONArray subStatusArray = object.optJSONArray("subStatus");
                    if (subStatusArray != null) {
                        int selectedLength = subStatusArray.length();
                        for (int j = 0; j < selectedLength; j++) {
                            JSONObject obj = subStatusArray.optJSONObject(j);
                            updateStatusList.add(new SubStatus(obj.optString("id"), obj.optString("title")));
                        }
                    }
                }
                if (selectedProjList != null && selectedProjList.size() > 0) {
                    SelectMultipleProjectsEntity multiSelectEntity =
                            new SelectMultipleProjectsEntity(enquiryId, Utils.getSelectedMultiProjectIds(selectedProjList), Utils.getMultiSelectedProject(selectedProjList),
                                    0, "", lastUpdatedOn);
                    new InsertMultiSelectTask(multiSelectEntity, 0).execute();
                }

                String userType = userDesignation.equalsIgnoreCase("0") ? getString(R.string.txt_sp) : getString(R.string.txt_asm);
                String isLeadTypeValue = isLeadType == null ? "" : isLeadType;

                // CLOSURE LEADS DATA
                if (isAssigned == 2) {
                    SalesClosureLeadsDetailEntity closureEntity = new SalesClosureLeadsDetailEntity(enquiryId, custName, custMob, custEmail,
                            budget, mProjectName, currentStatus, subStatusItem, unitNo, scheduledDateTime, amount, towerNo, chequeNo, chequeDate,
                            bankName, remark, 0, 0, lastUpdatedOn, userDesignation);

                    new InsertClosureLeadsDetailTask(this, closureEntity).execute();
                }

                //int userAction = isLeadAccepted == 0 ? -1 : isLeadAccepted;

                if (isLeadAccepted == 0) {
                    isLeadAccepted = -1;
                }

                SalesLeadDetailEntity detailEntity = new SalesLeadDetailEntity(enquiryId, custName, custEmail, custMob,
                        custAlternateMob, Utils.getMultiSelectedProject(selectedProjList), Utils.getSelectedMultiProjectIds(selectedProjList),
                        budget, broker_name, Utils.getBrokerId(spBrokersList, broker_name), isLeadTypeValue, currentStatus,
                        Utils.getLeadStatusId(spLeadStatusList, currentStatus), scheduledDateTime, date, time, isAssigned, address, noOfPersons,
                        "", UpdateStatusConverter.fromArrayList(updateStatusList), unitNo, amount, towerNo, chequeNo, chequeDate, bankName, leadType, remark, -1,
                        "", "", "", "", "", "", 0,
                        "", "", "",
                        campaignName, campaignDate, 0, 0, 0, lastUpdatedOn,
                        userType, isLeadAccepted, "");
                mLeadDetailList.add(detailEntity);
            }
            new InsertSalesLeadDetailsTask(this, mLeadDetailList).execute();
            new CustomerInfoTask(mCustomerList).execute();
            startAlarmService();
        }
    }

    @Override
    public void onMenuClick(MenuItem item, Object fragment) {
        if (Connectivity.isConnected(this)) {
            if (fragment.equals(SalesClosuresFragment.TAG))
                app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", SalesActivity.this.getResources().getString(R.string.tab_closure));
            if (fragment.equals(AssignedFragment.TAG))
                app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", SalesActivity.this.getResources().getString(R.string.tab_assigned));

            // TODO CHANGE IT WHEN SP OFFLINE DONE
            new SyncDataTask(SalesActivity.this, BMHConstants.LEAD_PHASE_SALES).execute();

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        // Thread.sleep(1000);
                        new SyncRecordingTask(SalesActivity.this).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(r);
            thread.start();
        } else {
            Toast.makeText(this, R.string.txt_no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void callbackCallRecordings(List<CallRecordingEntity> recordingEList) {

        if (recordingEList != null && recordingEList.size() > 0) {
            CallRecordingEntity recordingEntity;
            for (int i = 0; i < recordingEList.size(); i++) {
                recordingEntity = recordingEList.get(i);
                File file = Utils.getCallRecordingDir(SalesActivity.this, recordingEntity.getRecFileName());
                if (file.exists()) {
                    // SYNC UNIVERSAL CALL RECORDINGS SYNC
                    if (TextUtils.isEmpty(recordingEntity.getEnquiryId())) {
                        syncUniversalRecording(recordingEntity.getMobileNo(), file.getPath(),
                                recordingEntity.getRemark(), recordingEntity.getTimeStamp());
                    } else {
                        // NORMAL CALL RECORDINGS SYNC
                        //File file = new File(recordingEntity.getRecFileName());
                        syncCallRecordings(recordingEntity.getEnquiryId(), recordingEntity.getMobileNo(), file.getPath(),
                                recordingEntity.getLeadUpdateStatus(), recordingEntity.getTimeStamp());
                    }
                }
            }
        }
    }

    private void syncUniversalRecording(final String mobileNo, final String filePath, final String remark, final String timeStamp) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_UNIVERSAL_RECORDING),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String resultResponse = new String(response.data);

                            if (resultResponse.equalsIgnoreCase("null")) {
                                Toast.makeText(SalesActivity.this, R.string.txt_sync_not_success, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject result = new JSONObject(resultResponse);
                            if (result.optBoolean("success")) {
                                Toast.makeText(SalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
                                new UpdateCallRecordingTask(result).execute();
                            } else {
                                Toast.makeText(SalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(BMHConstants.MOBILE_NO, mobileNo);
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(BMHConstants.TIME_STAMP, timeStamp);
                params.put(BMHConstants.REMARK, remark);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("uploadedfile", new DataPart(new File(filePath).getName(), Utils.getFileInBytes(filePath, SalesActivity.this)));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                //   headers.put("Content-Type", WEBAPI.contentTypeAudio);
                return headers;
            }
        };

        BMHApplication.getInstance().addToRequestQueue(multipartRequest, "call_recording_sync");
    }

    private void syncCallRecordings(final String enquiryId, final String mobileNo, final String filePath, final int leadUpdateStatus, final String timeStamp) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_CALL_RECORDING),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String resultResponse = new String(response.data);
                            if (resultResponse.equalsIgnoreCase("null")) {
                                Toast.makeText(SalesActivity.this, R.string.txt_sync_not_success, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject result = new JSONObject(resultResponse);
                            if (result.optBoolean("success")) {
                                Toast.makeText(SalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
                                new UpdateCallRecordingTask(result).execute();
                            } else {
                                Toast.makeText(SalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(BMHConstants.ENQUIRY_ID, enquiryId);
                params.put(BMHConstants.MOBILE_NO, mobileNo);
                params.put(BMHConstants.LEAD_UPDATE_STATUS, String.valueOf(leadUpdateStatus));
                params.put(BMHConstants.TIME_STAMP, timeStamp);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("uploadedfile", new DataPart(new File(filePath).getName(), Utils.getFileInBytes(filePath, SalesActivity.this)));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                //   headers.put("Content-Type", WEBAPI.contentTypeAudio);
                return headers;
            }
        };

        BMHApplication.getInstance().addToRequestQueue(multipartRequest, "call_recording_sync");
    }

    @Override
    public void getSyncDataCallback(JSONObject jsonObject) {
        if (Connectivity.isConnected(this)) {
            if ((jsonObject.optJSONArray("data_list").length() > 0) || (jsonObject.optJSONArray("closure_list").length() > 0)) {
                startOfflineSync(jsonObject);
            } else {
                new InsertAddAppointmentTask(SalesActivity.this).execute();
            }
        } else {
            Toast.makeText(this, R.string.txt_no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void startOfflineSync(final JSONObject jsonObject) {
        dialog.setMessage(getString(R.string.txt_device_sync_started));
        if (!isFinishing())
            dialog.show();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_OFFLINE_SALES_LEADS),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

               /* if (dialog.isShowing()) {
                    dialog.dismiss();
                }*/
                if (response != null) {
                    try {
                        if (response.optBoolean("success")) {
                            new UpdateSyncDataTask(BMHConstants.LEAD_PHASE_SALES, response.optJSONArray("data")).execute();
                            new InsertAddAppointmentTask(SalesActivity.this).execute();
                        } else {
                            Toast.makeText(SalesActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            // FETCH DATA FROM DB IF SYNC FAIL.
                            loadDataFromDatabase();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d(TAG, "" + error);
                    }
                }
        ) {
          /*  @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", jsonObject.toString());
                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeJson);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "sales_sync");
    }


    @Override
    public void getSyncAppointList(JSONObject jsonObject) {
        if (Connectivity.isConnected(this)) {
            if (jsonObject.optJSONArray("add_appointment_list").length() > 0) {
                syncAddAppointments(jsonObject);
            } else {
                getSalesData();
            }
        } else {
            Toast.makeText(this, R.string.txt_no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void syncAddAppointments(final JSONObject jsonObject) {

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_ADD_APPOINTMENTS),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.optBoolean("success")) {
                            new UpdateSyncDataTask(response.optJSONArray("data"), getString(R.string.txt_add_appointment)).execute();
                            Toast.makeText(SalesActivity.this, getString(R.string.txt_device_data_synced), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SalesActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                        getSalesData();
                    } catch (Exception e) {
                        Toast.makeText(SalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
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
          /*  @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", jsonObject.toString());
                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeJson);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "addAppointment_sync");
    }

    public boolean checkPermissions(String mobile_no) {
        mobileNo = mobile_no;
        hasExtCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        return hasExtCallPermission == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), BMHConstants.MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions() {
        if (hasExtCallPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsCall.add(Manifest.permission.CALL_PHONE);
        }
        if (!permissionsCall.isEmpty()) {
            requestPermissions(permissionsCall.toArray(new String[permissionsCall.size()]), BMHConstants.PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BMHConstants.MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissions) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;
                        }
                    }
                    // Show permissionsDenied
                    //updateViews();
                }
                return;
            }
            case BMHConstants.PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        actionCall(mobileNo);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
        }
    }

    public void actionCall(String mobileNo) {
        Intent call = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mobileNo, null));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(call);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings ? true : super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        mViewPager.setOffscreenPageLimit(2);
        mSectionsPagerAdapter.getItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void getAllSpDetails(List<SalesLeadDetailEntity> detailsEntityList) {
        ArrayList<NotificationDataModel> notificationDataModelList = new ArrayList<>();
        String alertDate = "";
        String alertTime = "";
        String alertStatus = "";
        String enquiryId = "";
        String customerName = "";
        String mobileNo = "";
        int isUpdated;
        Calendar cal = Calendar.getInstance();
        if (detailsEntityList != null) {
            for (int i = 0; i < detailsEntityList.size(); i++) {
                alertTime = detailsEntityList.get(i).getTime();
                alertDate = detailsEntityList.get(i).getDate();
                alertStatus = detailsEntityList.get(i).getCurrentStatus();
                enquiryId = detailsEntityList.get(i).getEnquiryId();
                customerName = detailsEntityList.get(i).getCustomerName();
                mobileNo = detailsEntityList.get(i).getCustomerMobile();
                isUpdated = detailsEntityList.get(i).getIsUpdated();
                if (!alertTime.equalsIgnoreCase("null") && (!TextUtils.isEmpty(alertTime) && !TextUtils.isEmpty(alertDate))) {
                    String time[] = alertTime.split(":");
                    int hour = Integer.parseInt(time[0]);
                    int minute = Integer.parseInt(time[1]);
                    String dateString[] = alertDate.split(",");
                    String day = dateString[0];
                    String date = String.valueOf(dateString[1]);
                    String newDate[] = date.split(" ");
                    int month = Utils.convertStringToInt(newDate[1]);
                    int alarmDate = Integer.parseInt(newDate[2]);
                    int year = Integer.parseInt(newDate[3]);
                    cal.set(year, month - 1, alarmDate, hour, minute);
                   /* if ((System.currentTimeMillis() <= cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_24_HOUR)
                            || (System.currentTimeMillis() >= cal.getTimeInMillis() + BMHConstants.FIVE_MINUTES_DURATION)) {
                        notificationDataModelList.add(new NotificationDataModel(alertTime, alertDate, alertStatus, enquiryId, customerName, mobileNo, isUpdated));
                    }*/
                    if ((cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_ONE_HOUR >= System.currentTimeMillis())
                            || (cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_24_HOUR >= System.currentTimeMillis())
                            || (cal.getTimeInMillis() + BMHConstants.FIVE_MINUTES_DURATION >= System.currentTimeMillis())) {
                        notificationDataModelList.add(new NotificationDataModel(alertTime, alertDate, alertStatus, enquiryId, customerName, mobileNo, isUpdated));
                    }
                }
            }
            if (notificationDataModelList.size() > 0) {
                Intent startIntent = new Intent(this,
                        AlarmJobServices.class);
                startIntent.putExtra(BMHConstants.USER_DESIGNATION, app.getFromPrefs(BMHConstants.USER_DESIGNATION));
                startIntent.putParcelableArrayListExtra("notification_data_list", notificationDataModelList);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.startForegroundService(startIntent);
                } else {
                    this.startService(startIntent);
                }
            }
        }
    }

    @Override
    public void callbackSalesLeads(List<ProjectMasterEntity> projectMasterList, List<StatusMasterEntity> leadStatusMasterList,
                                   List<CorporateActivityMasterEntity> corporateMasterList,
                                   List<SalesBrokerMasterEntity> spBrokerMasterEntityList,
                                   List<ClosureMasterEntity> spClosureMasterEntityList,
                                   List<NotInterestedMasterEntity> spNotInterestedMasterEntityList,
                                   List<SubStatusCallbackEntity> spSubStatusCallbackEntityList,
                                   List<SubStatusClosureEntity> spSubStatusClosureEntityList,
                                   List<SubStatusMeetingEntity> spSubStatusMeetingEntityList,
                                   List<SubStatusNotInterestedEntity> spSubStatusNotInterestedEntityList,
                                   List<SubStatusSiteVisitEntity> spSubStatusSiteVisitEntityList,
                                   List<SalesLeadDetailEntity> salesEntityList) {
        // CLEAR STATIC INITIALIZATION
        if (projectMasterList.size() > 0 && projectsMasterList.size() > 0)
            projectsMasterList.clear();
        if (leadStatusMasterList.size() > 0 && spLeadStatusList.size() > 0)
            spLeadStatusList.clear();
        if (spBrokerMasterEntityList.size() > 0 && spBrokersList.size() > 0)
            spBrokersList.clear();
        if (spClosureMasterEntityList.size() > 0 && spClosureList.size() > 0)
            spClosureList.clear();
        if (spNotInterestedMasterEntityList.size() > 0 && spNotInterestedList.size() > 0)
            spNotInterestedList.clear();
        if (corporateMasterList.size() > 0 && corporateActivityList.size() > 0)
            corporateActivityList.clear();

        mSpFollowUpList = new ArrayList<>();
        mSpClosureLeadsList = new ArrayList<>();
        AsmSalesLeadDetailModel detailModel;

        try {
            for (int i = 0; i < corporateMasterList.size(); i++) {
                corporateActivityList.add(new CorporateActivityModel(corporateMasterList.get(i).getActivityId(),
                        corporateMasterList.get(i).getActivityName(), Converters.fromString(corporateMasterList.get(i).getProjectList())));
            }
        } catch (JsonSyntaxException e) {
            e.getMessage();
        }

        for (int i = 0; i < projectMasterList.size(); i++) {
            String projectName = projectMasterList.get(i).getProjectName();
            if (!TextUtils.isEmpty(projectName))
                projectsMasterList.add(new Projects(projectMasterList.get(i).getProjectId(), projectName));
        }

        for (int i = 0; i < leadStatusMasterList.size(); i++) {
            spLeadStatusList.add(new LeadStatus(leadStatusMasterList.get(i).getStatusId(), leadStatusMasterList.get(i).getStatusName()));
        }

        for (int i = 0; i < spBrokerMasterEntityList.size(); i++) {
            spBrokersList.add(new NotInterestedLead(spBrokerMasterEntityList.get(i).getId(), spBrokerMasterEntityList.get(i).getTitle(), spBrokerMasterEntityList.get(i).getAddress()));
        }

        for (int i = 0; i < spClosureMasterEntityList.size(); i++) {
            spClosureList.add(new NotInterestedLead(spClosureMasterEntityList.get(i).getId(), spClosureMasterEntityList.get(i).getTitle(), ""));
        }

        for (int i = 0; i < spNotInterestedMasterEntityList.size(); i++) {
            spNotInterestedList.add(new NotInterestedLead(spNotInterestedMasterEntityList.get(i).getId(), spNotInterestedMasterEntityList.get(i).getTitle(), ""));
        }

        if (salesEntityList != null) {
            mSpAssignedList = new ArrayList<>();
            mDetailsList = new ArrayList<>();
            // SP USER
            if (userDesignation.equalsIgnoreCase("0")) {
                for (int i = 0; i < salesEntityList.size(); i++) {
                    ldEntity = salesEntityList.get(i);
                    ArrayList<SubStatus> updateStatusList = UpdateStatusConverter.fromString(ldEntity.getUpdateStatusList());
                    String newStatus = ldEntity.getNewLeadStatus().equalsIgnoreCase("") ? ldEntity.getCurrentStatus() : ldEntity.getNewLeadStatus();
                    String newDate = ldEntity.getNewDate().equalsIgnoreCase("") ? ldEntity.getDate() : ldEntity.getNewDate();
                    String newTime = ldEntity.getNewTime().equalsIgnoreCase("") ? ldEntity.getTime() : ldEntity.getNewTime();
                    String newAddress = ldEntity.getNewAddress().equalsIgnoreCase("") ? ldEntity.getAddress() : ldEntity.getNewAddress();
                    int newPersonCount = ldEntity.getNewPersonCount() == 0 ? ldEntity.getNoOfPerson() : ldEntity.getNewPersonCount();
                    String newRemark = ldEntity.getNewRemark().equalsIgnoreCase("") ? ldEntity.getRemark() : ldEntity.getNewRemark();

                    detailModel = new AsmSalesLeadDetailModel(ldEntity.getEnquiryId(), ldEntity.getCampaignName(),
                            ldEntity.getCampaignDate(), ldEntity.getCustomerName(), ldEntity.getCustomerEmail(), ldEntity.getCustomerMobile(),
                            ldEntity.getCustomerAlternateMobile(), ldEntity.getBudget(), newStatus, ldEntity.getScheduledDateTime(),
                            newDate, newTime, ldEntity.getNew_sub_status(), ldEntity.getNew_sub_status_id(), ldEntity.getClosure_project_id(),
                            newAddress, Utils.getAllSelectedProject(projectsMasterList, ldEntity.getProjects()), updateStatusList,
                            newRemark, ldEntity.getLeadType(), newPersonCount, ldEntity.getAssignedTo(), ldEntity.getIsLeadType(),
                            ldEntity.getNew_sub_status(), ldEntity.getUnitNo(), ldEntity.getAmount(), ldEntity.getTowerNo(),
                            ldEntity.getChequeNumber(), ldEntity.getChequeDate(), ldEntity.getPayment_mode(), ldEntity.getLastupdatedon());

                    mDetailsList.add(detailModel);

                    AsmSalesModel salesModel = new AsmSalesModel(ldEntity.getEnquiryId(), ldEntity.getCustomerName(),
                            ldEntity.getCustomerMobile(), ldEntity.getCustomerEmail(), ldEntity.getCampaignName(),
                            newStatus, ldEntity.getProjects(), ldEntity.getScheduledDateTime(),
                            ldEntity.getIsLeadType(), ldEntity.getIsAssigned(), ldEntity.getUserAction(), ldEntity.getLastupdatedon(), detailModel);

                    if (ldEntity.getIsAssigned() == 0) {
                        mSpAssignedList.add(salesModel);
                    } else if (ldEntity.getIsAssigned() == 1) {
                        mSpFollowUpList.add(salesModel);
                    } else if (ldEntity.getIsAssigned() == 2) {
                        mSpClosureLeadsList.add(salesModel);
                    }
                }
            }
            // ASM USER
            else {
                for (int k = 0; k < salesEntityList.size(); k++) {
                    ldEntity = salesEntityList.get(k);
                    ArrayList<SubStatus> updateStatusList = UpdateStatusConverter.fromString(ldEntity.getUpdateStatusList());
                    String newStatus = ldEntity.getNewLeadStatus().equalsIgnoreCase("") ? ldEntity.getCurrentStatus() : ldEntity.getNewLeadStatus();
                    String newDate = ldEntity.getNewDate().equalsIgnoreCase("") ? ldEntity.getDate() : ldEntity.getNewDate();
                    String newTime = ldEntity.getNewTime().equalsIgnoreCase("") ? ldEntity.getTime() : ldEntity.getNewTime();
                    String newAddress = ldEntity.getNewAddress().equalsIgnoreCase("") ? ldEntity.getAddress() : ldEntity.getNewAddress();
                    int newPersonCount = ldEntity.getNewPersonCount() == 0 ? ldEntity.getNoOfPerson() : ldEntity.getNewPersonCount();
                    String newRemark = ldEntity.getNewRemark().equalsIgnoreCase("") ? ldEntity.getRemark() : ldEntity.getNewRemark();

                    detailModel = new AsmSalesLeadDetailModel(ldEntity.getEnquiryId(), ldEntity.getCampaignName(),
                            ldEntity.getCampaignDate(), ldEntity.getCustomerName(), ldEntity.getCustomerEmail(), ldEntity.getCustomerMobile(),
                            ldEntity.getCustomerAlternateMobile(), ldEntity.getBudget(), newStatus, ldEntity.getScheduledDateTime(),
                            newDate, newTime, ldEntity.getNew_sub_status(), ldEntity.getNew_sub_status_id(), ldEntity.getClosure_project_id(), newAddress, Utils.getAllSelectedProject(projectsMasterList,
                            ldEntity.getProjects()), updateStatusList, newRemark, ldEntity.getLeadType(), newPersonCount,
                            ldEntity.getAssignedTo(), ldEntity.getIsLeadType(), ldEntity.getNew_sub_status(), ldEntity.getUnitNo(), ldEntity.getAmount(), ldEntity.getTowerNo()
                            , ldEntity.getChequeNumber(), ldEntity.getChequeDate(), ldEntity.getPayment_mode(), ldEntity.getLastupdatedon());
                    mDetailsList.add(detailModel);

                    AsmSalesModel salesModel = new AsmSalesModel(ldEntity.getEnquiryId(), ldEntity.getCustomerName(), ldEntity.getCustomerMobile(),
                            ldEntity.getCustomerEmail(), ldEntity.getCampaignName(), newStatus,
                            ldEntity.getProjects(), ldEntity.getScheduledDateTime(),
                            ldEntity.getIsLeadType(), ldEntity.getIsAssigned(), ldEntity.getUserAction(), ldEntity.getLastupdatedon(), detailModel);

                    if (ldEntity.getIsAssigned() == 1) {
                        mSpFollowUpList.add(salesModel);
                    } else if (ldEntity.getIsAssigned() == 2) {
                        mSpClosureLeadsList.add(salesModel);
                    }
                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prepareTabs();
            }
        });

    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        //integer to count number of tabs
        int tabCount;

        private SectionsPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;

        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs

            AssignedFragment assignedFragment = new AssignedFragment();
            FollowUpFragment followUpFragment = new FollowUpFragment();
            SalesClosuresFragment salesClosuresFragment = new SalesClosuresFragment();

            if (userDesignation.equalsIgnoreCase("0")) {
                switch (position) {
                    case 0:
                        return assignedFragment;

                    case 1:
                        return followUpFragment;

                    case 2:
                        return salesClosuresFragment;

                }
            } else {
                switch (position) {
                    case 0:
                        return new FollowUpFragment();
                    case 1:
                        return new SalesClosuresFragment();
                    default:
                        return null;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    private void startAlarmService() {
        new GetSalesLeadDetailTask(this).execute();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();

    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            if (ni != null && ni.isConnected())
                refreshLeadData();
        }
    };


}
