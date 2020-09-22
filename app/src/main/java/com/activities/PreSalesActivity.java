package com.activities;

import android.Manifest;
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
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.appnetwork.VolleyMultipartRequest;
import com.appnetwork.WEBAPI;
import com.async.CustomVolleyRequest;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.SpMasterEntity;
import com.database.task.CustomerInfoTask;
import com.database.task.FetchPreSalesLeadsByTabIdTask;
import com.database.task.GetPreSalesLeadDetailsTask;
import com.database.task.InsertProjectMasterTask;
import com.database.task.InsertSpMasterTask;
import com.database.task.InsertStatusMasterTask;
import com.database.task.SyncDataTask;
import com.database.task.SyncRecordingTask;
import com.database.task.UpdateCallRecordingTask;
import com.database.task.UpdateSyncDataTask;
import com.database.AppDatabase;
import com.database.entity.CallRecordingEntity;
import com.database.entity.CustomerInfoEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.StatusMasterEntity;
import com.fragments.AsmAssignedFragment;
import com.fragments.AsmUnAssignedFragment;
import com.fragments.SpPendingFragment;
import com.fragments.SpUpdatedFragment;
import com.google.gson.JsonParseException;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.OnLeadSaveToDbListener;
import com.interfaces.OnMenuItemClickListener;
import com.model.Assign_To;
import com.model.Details;
import com.model.LeadStatus;
import com.model.NotificationDataModel;
import com.model.PreSalesAsmModel;
import com.model.PreSalesSpModel;
import com.model.Projects;
import com.services.AlarmJobServices;
import com.sp.BMHApplication;
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
import java.util.Objects;

public class PreSalesActivity extends AppCompatActivity implements
        TabLayout.OnTabSelectedListener, FetchPreSalesLeadsByTabIdTask.ILeadCommunicator, OnLeadSaveToDbListener,
        SyncDataTask.ISyncDataCommunicator, OnMenuItemClickListener, SyncRecordingTask.ISyncRecordingCommunicator,
        GetPreSalesLeadDetailsTask.IPreSalesLeadDetailsCommunicator {

    private static final String TAG = PreSalesActivity.class.getCanonicalName();
    private PreSalesActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private BMHApplication app;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public Bundle masterBundle;
    private ProgressDialog dialog;
    private String userDesignation;
    public AppDatabase mDbInstance;
    private List<CustomerInfoEntity> mCustomerList = new ArrayList<>();
    private List<StatusMasterEntity> mStatusMasterList = new ArrayList<>();
    private List<ProjectMasterEntity> mProjectsMasterList = new ArrayList<>();
    private List<SpMasterEntity> mSpMasterList = new ArrayList<>();
    private int hasExtCallPermission;
    private List<String> permissionsCall = new ArrayList<>();
    private String mobileNo;
    SpMasterEntity spMasterEntity;
    StatusMasterEntity statusMasterEntity;
    ProjectMasterEntity projectMasterEntity;

    public ArrayList<PreSalesSpModel> mSpPendingList1;
    public ArrayList<PreSalesSpModel> mSpUpdatedList1;

    public ArrayList<PreSalesAsmModel> mAsmAssignedList1;
    public ArrayList<PreSalesAsmModel> mAsmUnAssignedList1;

    public ArrayList<Details> mDetailsList1 = new ArrayList<>();
    public static List<Projects> mProjectsList = new ArrayList<>();
    public ArrayList<Projects> mSelectedProjList = new ArrayList<>();
    public static ArrayList<Assign_To> mSpList = new ArrayList<>();
    public static ArrayList<LeadStatus> mLeadsList = new ArrayList<>();
    PreSalesLeadDetailsEntity ldEntity;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transactions);
        tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        app = (BMHApplication) getApplication();
        mDbInstance = AppDatabase.getInstance();
        dialog = new ProgressDialog(PreSalesActivity.this);
        userDesignation = app.getFromPrefs((BMHConstants.USER_DESIGNATION));
        String toolbarSubTitle;
        if (userDesignation.equalsIgnoreCase("0")) {
            toolbarSubTitle = getString(R.string.txt_sp);
            toolbar.setTitle(getString(R.string.txt_toolbar_pre_sales, toolbarSubTitle));
        } else {
            toolbarSubTitle = getString(R.string.txt_asm);
            toolbar.setTitle(getString(R.string.txt_toolbar_pre_sales, toolbarSubTitle));
        }
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (tabLayout.getTabCount() > 0)
            tabLayout.removeAllTabs();
        if (userDesignation.equalsIgnoreCase("0")) {
            tabLayout.addTab(tabLayout.newTab().setText(PreSalesActivity.this.getResources().getString(R.string.tab_pending)));
            tabLayout.addTab(tabLayout.newTab().setText(PreSalesActivity.this.getResources().getString(R.string.tab_updated)));
        } else {
            tabLayout.addTab(tabLayout.newTab().setText(PreSalesActivity.this.getResources().getString(R.string.tab_un_assigned)));
            tabLayout.addTab(tabLayout.newTab().setText(PreSalesActivity.this.getResources().getString(R.string.tab_assigned)));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                //do nothing
            }
        }
        createTableSchema();
        refreshLeadData();
       /* mySyncAdapter = new MySyncAdapter(getApplicationContext(), true);
        mySyncAdapter.initializeSyncAdapter();*/
    }


    private void refreshLeadData() {
        if (Connectivity.isConnected(this)) {
            // CHECK OFFLINE CHANGES AVAILABILITY
            new SyncDataTask(PreSalesActivity.this, BMHConstants.LEAD_PHASE_PRE_SALES).execute();
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
            new FetchPreSalesLeadsByTabIdTask(this, getString(R.string.txt_sp)).execute();
        } else {
            // ASM USER
            new FetchPreSalesLeadsByTabIdTask(this, getString(R.string.txt_asm)).execute();
        }
    }

    private void createTableSchema() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
            jsonObject.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_LEADS_MASTER_TABLE),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.getBoolean("success")) {
                            parseTableJson(jsonObject);
                        } else {
                            Utils.showToast(PreSalesActivity.this, jsonObject.optString("messaage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PreSalesActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            private void parseTableJson(JSONObject jsonObject) throws JSONException {
                JSONObject jsonObj;
                try {
                    jsonObj = jsonObject.optJSONObject("data");
                    JSONArray assignArray = jsonObj.optJSONArray("Assign_To");
                    JSONArray leadArray = jsonObj.optJSONArray("lead_status");
                    JSONArray projectArray = jsonObj.optJSONArray("Project_List");

                    if (assignArray != null) {
                        for (int i = 0; i < assignArray.length(); i++) {

                            JSONObject object = assignArray.getJSONObject(i);
                            String salesPersonId = object.optString("salesperson_id");
                            String salesPersonName = object.optString("salesperson_name");
                            spMasterEntity = new SpMasterEntity(salesPersonId, salesPersonName);
                            mSpMasterList.add(spMasterEntity);
                        }
                        // create worker thread to insert data into database
                        new InsertSpMasterTask(mSpMasterList).execute();

                    }
                    if (leadArray != null) {
                        for (int i = 0; i < leadArray.length(); i++) {

                            JSONObject object = leadArray.getJSONObject(i);
                            String dispositionId = object.optString("disposition_id");
                            String name = object.optString("title");
                            statusMasterEntity = new StatusMasterEntity(name, dispositionId, 0);
                            mStatusMasterList.add(statusMasterEntity);
                        }
                        new InsertStatusMasterTask(mStatusMasterList).execute();
                    }
                    if (projectArray != null) {
                        for (int i = 0; i < projectArray.length(); i++) {

                            JSONObject object = projectArray.getJSONObject(i);
                            String projectId = object.optString("project_id");
                            String projectName = object.optString("project_name");
                            projectMasterEntity = new ProjectMasterEntity(projectId, projectName);
                            mProjectsMasterList.add(projectMasterEntity);
                        }
                        new InsertProjectMasterTask(mProjectsMasterList).execute();
                    }
                } catch (JsonParseException e) {
                    throw new JsonParseException(e.getMessage());
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


          /*  @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //  params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                //  params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                    jsonObject.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                    params.put("data", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }*/


            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
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
                dialog = new ProgressDialog(PreSalesActivity.this);
            refreshLeadData();
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

    private void getPreSalesData() {
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        if (!isFinishing())
            dialog.show();
        //     Utils.showToast(this, getString(R.string.txt_device_data_synced));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_USER_PRE_SALES_LIST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                        Toast.makeText(PreSalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (mDbInstance != null)
                                        insertToDb(jsonObject);
                                    masterBundle = new Bundle();
                                    masterBundle.putString("JSON_STRING", response);
                                    Utils.showToast(PreSalesActivity.this, getString(R.string.txt_server_data_synced));
                                    prepareTabs();
                                } else {
                                    Toast.makeText(PreSalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(PreSalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_DESIGNATION, userDesignation);
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

    private void prepareTabs() {
        if (dialog == null) {
            dialog = new ProgressDialog(PreSalesActivity.this);
        }


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.sky_blue));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

        tabLayout.setTabTextColors(
                ContextCompat.getColor(PreSalesActivity.this, R.color.black_alpha_40),
                ContextCompat.getColor(PreSalesActivity.this, R.color.black)
        );

        tabLayout.addOnTabSelectedListener(PreSalesActivity.this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if (app.getTabFromPrefs("TAB_NAVIGATED_FROM").equalsIgnoreCase(getString(R.string.tab_assigned))
                || app.getTabFromPrefs("TAB_NAVIGATED_FROM").equalsIgnoreCase(getString(R.string.tab_updated))) {
            mViewPager.setCurrentItem(1);
            mSectionsPagerAdapter.getItem(1);
            app.clearTabPref();
        }
        app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, false);
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void insertToDb(JSONObject jsonObject) throws JSONException {
        CustomerInfoEntity customer;
        JSONArray jsonArray = jsonObject.optJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String custMob = object.optString("customer_mobile");
            String enquiryId = object.optString("enquiry_id");
            String custName = object.optString("customer_name");
            String assignType = String.valueOf(object.optInt("isAssigned"));
            //  String leadPhase = assignType.equalsIgnoreCase("0") ? BMHConstants.LEAD_PHASE_PRE_SALES : BMHConstants.LEAD_PHASE_SALES;
            customer = new CustomerInfoEntity(custMob, enquiryId, custName, BMHConstants.LEAD_PHASE_PRE_SALES, assignType);
            // create worker thread to insert data into database
            mCustomerList.add(customer);

        }
        new CustomerInfoTask(mCustomerList).execute();
    }

    @Override
    public void callbackLeadsData(List<ProjectMasterEntity> projectMasterList,
                                  List<StatusMasterEntity> leadStatusMasterList,
                                  List<SpMasterEntity> spMasterEntityList,
                                  List<PreSalesLeadDetailsEntity> entityList) {

        // CLEAR STATIC INITIALIZATION
        if (mProjectsList.size() > 0)
            mProjectsList.clear();
        if (mLeadsList.size() > 0)
            mLeadsList.clear();
        if (mSpList.size() > 0)
            mSpList.clear();

        for (int i = 0; i < projectMasterList.size(); i++) {
            String text = projectMasterList.get(i).getProjectName();
            if (!TextUtils.isEmpty(text))
                mProjectsList.add(new Projects(projectMasterList.get(i).getProjectId(), projectMasterList.get(i).getProjectName()));
        }

        for (int i = 0; i < leadStatusMasterList.size(); i++) {
            mLeadsList.add(new LeadStatus(leadStatusMasterList.get(i).getStatusId(), leadStatusMasterList.get(i).getStatusName()));
        }

        for (int i = 0; i < spMasterEntityList.size(); i++) {
            mSpList.add(new Assign_To(spMasterEntityList.get(i).getSpId(), spMasterEntityList.get(i).getSpName()));
        }

        // SP USER
        if (userDesignation.equalsIgnoreCase("0")) {
            mSpPendingList1 = new ArrayList<>();
            mSpUpdatedList1 = new ArrayList<>();
            if (entityList != null) {
                for (int i = 0; i < entityList.size(); i++) {
                    ldEntity = entityList.get(i);
                   /* String dateAndTime = "";
                    if (!TextUtils.isEmpty(ldEntity.getDate())) {
                        String[] overdueDate = ldEntity.getDate().trim().split(",");
                        // dateAndTime = Utils.getDateFromString(overdueDate[1]) + " " + ldEntity.getTime();
                        dateAndTime = overdueDate[1] + " | " + ldEntity.getTime();
                    }*/
                    if (ldEntity.getIsAssigned() == 0) {
                        mSpPendingList1.add(new PreSalesSpModel(ldEntity.getEnquiryId(), ldEntity.getCustomerName(), ldEntity.getCustomerMobile(),
                                ldEntity.getCustomerEmail(), ldEntity.getCampaignName(), ldEntity.getCampaignDate(),
                                ldEntity.getCurrentStatus(), Utils.getMultiSelectedProject(mSelectedProjList), ldEntity.getRemark(), ldEntity.getDate(),
                                ldEntity.getTime(), ldEntity.getAssignedTo(), ldEntity.getIsAssigned(), ldEntity.getScheduledatetime(),
                                ldEntity.getCustomerAlternateMobile(), ldEntity.getBudget(), ldEntity.getBudgetMin(), ldEntity.getBudgetMax(),
                                ldEntity.getLastupdatedon()));

                    } else {
                        mSpUpdatedList1.add(new PreSalesSpModel(ldEntity.getEnquiryId(), ldEntity.getCustomerName(), ldEntity.getCustomerMobile(),
                                ldEntity.getCustomerEmail(), ldEntity.getCampaignName(), ldEntity.getCampaignDate(),
                                ldEntity.getCurrentStatus(), Utils.getMultiSelectedProject(mSelectedProjList), ldEntity.getRemark(), ldEntity.getDate(),
                                ldEntity.getTime(), ldEntity.getAssignedTo(), ldEntity.getIsAssigned(), ldEntity.getScheduledatetime(),
                                ldEntity.getCustomerAlternateMobile(), ldEntity.getBudget(), ldEntity.getBudgetMin(), ldEntity.getBudgetMax(),
                                ldEntity.getLastupdatedon()));
                    }
                }
            }
        }
        // ASM USER
        else {
            mAsmAssignedList1 = new ArrayList<>();
            mAsmUnAssignedList1 = new ArrayList<>();
            if (entityList != null) {
                for (int i = 0; i < entityList.size(); i++) {
                    ldEntity = entityList.get(i);
                   /* String dateAndTime = "";
                    if (!TextUtils.isEmpty(ldEntity.getDate())) {
                        String[] overdueDate = ldEntity.getDate().trim().split(",");
                       // dateAndTime = Utils.getDateFromString(overdueDate[1]) + " " + ldEntity.getTime();
                        dateAndTime = overdueDate[1] + " | " + ldEntity.getTime();
                    }*/
                    if (ldEntity.getIsAssigned() == 0) {
                        mAsmUnAssignedList1.add(new PreSalesAsmModel(ldEntity.getEnquiryId(),
                                ldEntity.getCustomerName(), ldEntity.getCustomerMobile(),
                                ldEntity.getCustomerEmail(), ldEntity.getCustomerAlternateMobile(),
                                ldEntity.getCampaignName(), ldEntity.getCampaignDate(),
                                ldEntity.getCurrentStatus(), Utils.getMultiSelectedProject(mSelectedProjList), ldEntity.getRemark(),
                                ldEntity.getBudget(), ldEntity.getBudgetMin(), ldEntity.getBudgetMax(),
                                ldEntity.getDate(), ldEntity.getTime(), ldEntity.getAssignedTo(),
                                ldEntity.getIsAssigned(), ldEntity.getScheduledatetime(), ldEntity.getLastupdatedon(), null));
                    } else {
                        mAsmAssignedList1.add(new PreSalesAsmModel(ldEntity.getEnquiryId(),
                                ldEntity.getCustomerName(), ldEntity.getCustomerMobile(),
                                ldEntity.getCustomerEmail(), ldEntity.getCustomerAlternateMobile(),
                                ldEntity.getCampaignName(), ldEntity.getCampaignDate(),
                                ldEntity.getCurrentStatus(), Utils.getMultiSelectedProject(mSelectedProjList), ldEntity.getRemark(),
                                ldEntity.getBudget(), ldEntity.getBudgetMin(), ldEntity.getBudgetMax(),
                                ldEntity.getDate(), ldEntity.getTime(), ldEntity.getAssignedTo(),
                                ldEntity.getIsAssigned(), ldEntity.getScheduledatetime(), ldEntity.getLastupdatedon(), null));
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
        mSectionsPagerAdapter.getItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onMenuClick(MenuItem item, Object fragment) {
        if (Connectivity.isConnected(this)) {
            new SyncDataTask(PreSalesActivity.this, BMHConstants.LEAD_PHASE_PRE_SALES).execute();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        //  Thread.sleep(1000);
                        new SyncRecordingTask(PreSalesActivity.this).execute();

                    }
                         catch (Exception e)
                             {
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
    public void getSyncDataCallback(final JSONObject jsonObject) {

        if (Connectivity.isConnected(this)) {
            if (jsonObject.optJSONArray("data_list").length() > 0)
                startOfflineSync(jsonObject);
            else {
                getPreSalesData();
            }
        }
           else {

            Toast.makeText(this, R.string.txt_no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void callbackCallRecordings(List<CallRecordingEntity> recordingEList) {

        if (recordingEList != null && recordingEList.size() > 0) {
            CallRecordingEntity recordingEntity;
            for (int i = 0; i < recordingEList.size(); i++) {
                recordingEntity = recordingEList.get(i);
                File file = Utils.getCallRecordingDir(PreSalesActivity.this, recordingEntity.getRecFileName());
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
        getPreSalesData();
    }

    private void syncUniversalRecording(final String mobileNo, final String filePath, final String remark, final String timeStamp) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_UNIVERSAL_RECORDING),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String resultResponse = new String(response.data);
                            JSONObject result = new JSONObject(resultResponse);
                            if (result.optBoolean("success")) {
                                Toast.makeText(PreSalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
                                new UpdateCallRecordingTask(result).execute();
                            } else {
                                Toast.makeText(PreSalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("uploadedfile", new DataPart(new File(filePath).getName(), Utils.getFileInBytes(filePath, PreSalesActivity.this)));

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
                            JSONObject result = new JSONObject(resultResponse);
                            if (result.optBoolean("success")) {
                                Toast.makeText(PreSalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
                                new UpdateCallRecordingTask(result).execute();
                            } else {
                                Toast.makeText(PreSalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("uploadedfile", new DataPart(new File(filePath).getName(), Utils.getFileInBytes(filePath, PreSalesActivity.this)));

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


    private void customSync(JSONObject jsonObject) {

        //  dialog = new ProgressDialog(PreSalesActivity.this);
        dialog.setMessage("Sync started...");
        if (!isFinishing())
            dialog.show();

        CustomVolleyRequest jsObjRequest = new CustomVolleyRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_OFFLINE_LEADS),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (jsonObject != null) {
                    if (jsonObject.optBoolean("success")) {
                        Toast.makeText(PreSalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PreSalesActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PreSalesActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "" + error);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        BMHApplication.getInstance().addToRequestQueue(jsObjRequest);
    }


    private void startOfflineSync(final JSONObject jsonObject) {

        dialog.setMessage(getString(R.string.txt_device_sync_started));
        if (!isFinishing())
            dialog.show();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_OFFLINE_LEADS),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response != null) {
                    try {
                        if (response.optBoolean("success")) {
                            new UpdateSyncDataTask(BMHConstants.LEAD_PHASE_PRE_SALES, response.optJSONArray("data")).execute();
                            Toast.makeText(PreSalesActivity.this, getString(R.string.txt_device_data_synced), Toast.LENGTH_SHORT).show();
                            getPreSalesData();
                            // createTableSchema();
                        } else {
                            Toast.makeText(PreSalesActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            // FETCH DATA FROM DB IF SYNC FAIL.
                            loadDataFromDatabase();
                        }
                    } catch (Exception e) {
                        Toast.makeText(PreSalesActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PreSalesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "pre_sales_sync");
    }

    @Override
    public void getPreSalesLeadsDetail(List<PreSalesLeadDetailsEntity> detailsEntityList) {
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
                    /*if ((System.currentTimeMillis() <= cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_24_HOUR)
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
                startIntent.putExtra(BMHConstants.USER_DESIGNATION, app.getFromPrefs(ParamsConstants.USER_DESIGNATION));
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
    public void callbackInsertLead(boolean b) {
        if (b) {
            startAlarmService();

            }
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
            if (userDesignation.equalsIgnoreCase(BMHConstants.SP_DESIGNATION)) {
                switch (position) {
                    case 0:
                        return new SpPendingFragment();
                    case 1:
                        return new SpUpdatedFragment();
                }

             }
                 else {
                switch (position) {
                    case 0:
                        return new AsmUnAssignedFragment();
                    case 1:
                        return new AsmAssignedFragment();
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
        new GetPreSalesLeadDetailsTask(this).execute();

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