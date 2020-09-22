package com.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.database.entity.CustomerInfoEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.task.CustomerInfoTask;
import com.database.task.GetSalesLeadDetailTask;
import com.database.task.InsertSalesLeadDetailsTask;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.AsmSalesLeadDetailModel;
import com.model.AsmSalesModel;
import com.model.BaseRespModel;
import com.model.NotificationDataModel;
import com.model.Projects;
import com.model.SubStatus;
import com.services.AlarmJobServices;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AssignedSalesDetailActivity extends AppCompatActivity implements GetSalesLeadDetailTask.ISalesAssignedLeadCommunicator {
    private String TAG = AssignedSalesDetailActivity.class.getSimpleName();
    private TextView tv_campaign_val, tv_campaign_date_val, tv_mobile_no_val, tv_project_name_val, tv_budget_val,
            tv_current_status_val, tv_status_date_time, tv_status_date_time_val, tv_status_address, tv_status_address_val;
    private EditText tv_customer_name_val, tv_email_id_val, tv_alternate_mobile_no_val;
    String userDesignation, mSelectedTab;
    private AsmSalesModel mAsmModel;
    private AsmSalesLeadDetailModel mDetailsModel;
    private Toolbar toolbar;
    private LinearLayout linear_layout_campaign_name, tv_campaign_id_divider, linear_layout_campaign_date, linear_layout_campaign_date_divider,
            linear_layout_alternate_mobile, tv_alternate_mobile_no_divider, layout_status_address_divider, layout_status_address;
    ProgressDialog dialog;
    BMHApplication app;
    private Button button_accept, button_reject;
    private String campaignName, campaignDate, alternateMobileNo, tagReceivedFrom;
    private int isSynced = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_sales_lead_details);
        app = (BMHApplication) getApplication();
        init();
        setUIData();
    }

    private void init() {
        tv_campaign_val = findViewById(R.id.tv_campaign_val);
        tv_campaign_date_val = findViewById(R.id.tv_campaign_date_val);
        tv_mobile_no_val = findViewById(R.id.tv_mobile_no_val);
        tv_project_name_val = findViewById(R.id.tv_project_name_val);
        tv_budget_val = findViewById(R.id.tv_budget_val);
        tv_current_status_val = findViewById(R.id.tv_current_status_val);
        tv_status_date_time = findViewById(R.id.tv_status_date_time);
        tv_status_date_time_val = findViewById(R.id.tv_status_date_time_val);
        tv_status_address = findViewById(R.id.tv_status_address);
        tv_status_address_val = findViewById(R.id.tv_status_address_val);
        tv_customer_name_val = findViewById(R.id.tv_customer_name_val);
        tv_email_id_val = findViewById(R.id.tv_email_id_val);
        tv_alternate_mobile_no_val = findViewById(R.id.tv_alternate_mobile_no_val);


        linear_layout_campaign_name = findViewById(R.id.linear_layout_campaign_name);
        tv_campaign_id_divider = findViewById(R.id.tv_campaign_id_divider);
        linear_layout_campaign_date = findViewById(R.id.linear_layout_campaign_date);
        linear_layout_campaign_date_divider = findViewById(R.id.linear_layout_campaign_date_divider);
        linear_layout_alternate_mobile = findViewById(R.id.linear_layout_alternate_mobile);
        tv_alternate_mobile_no_divider = findViewById(R.id.tv_alternate_mobile_no_divider);
        layout_status_address_divider = findViewById(R.id.layout_status_address_divider);
        layout_status_address = findViewById(R.id.layout_status_address);

        button_reject = findViewById(R.id.button_reject);
        button_accept = findViewById(R.id.button_accept);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setUIData() {
        Intent intent = getIntent();
        userDesignation = app.getFromPrefs((BMHConstants.USER_DESIGNATION));
        tagReceivedFrom = intent.getStringExtra(BMHConstants.PATH);
        if (tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
            String enquiryId = intent.getStringExtra(BMHConstants.ENQUIRY_ID);
            getLeadDetails(enquiryId, userDesignation);
            mSelectedTab = tagReceivedFrom;
        } else if (tagReceivedFrom.equalsIgnoreCase(AlertDialogActivity.TAG)) {
            mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
            String enquiryId = intent.getStringExtra(BMHConstants.ENQUIRY_ID);
            if (Connectivity.isConnected(this)) {
                getLeadDetails(enquiryId, userDesignation);
            } else {
                // GET OFFLINE DATA FROM DB
                if (!TextUtils.isEmpty(enquiryId))
                    new GetSalesLeadDetailTask(this, enquiryId, getString(R.string.tab_assigned)).execute();
            }
            return;
        } else {
            if (Connectivity.isConnected(this)) {
                mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
            } else {
                mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
                mAsmModel = intent.getParcelableExtra(BMHConstants.ASM_MODEL_DATA);
            }
            mDetailsModel = intent.getParcelableExtra(BMHConstants.ASM_DETAIL_DATA);
            setData(mDetailsModel);
        }
    }

    private void getLeadDetails(final String enquiry_id, final String designation) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.LEAD_DETAILS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                JSONObject data = jsonObject.optJSONObject("data");
                                if (data != null) {
                                    JSONObject object = data.getJSONObject("details");
                                    AsmSalesLeadDetailModel mDetailsModelNotification = (AsmSalesLeadDetailModel) JsonParser.convertJsonToBean(APIType.LEAD_DETAILS, object.toString());
                                    setData(mDetailsModelNotification);
                                } else {
                                    Utils.showToast(AssignedSalesDetailActivity.this, getString(R.string.txt_no_data_found));
                                }
                            } else {
                                Utils.showToast(AssignedSalesDetailActivity.this, getString(R.string.something_went_wrong));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put(ParamsConstants.USER_DESIGNATION, designation);
                params.put(ParamsConstants.ENQUIRY_ID, enquiry_id);
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


    @Override
    public void onBackPressed() {
        app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void acceptLead(final String enquiryId) {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.ACCEPT_LEAD),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    Toast.makeText(AssignedSalesDetailActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                    app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
                                    app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                                    if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                                        Intent intent = new Intent(AssignedSalesDetailActivity.this, SalesActivity.class);
                                        startActivity(intent);
                                    }
                                    onBackPressed();
                                }
                                if (!jsonObject.getBoolean("success")) {
                                    Toast.makeText(AssignedSalesDetailActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.showToast(AssignedSalesDetailActivity.this, getString(R.string.something_went_wrong));
                        }

                       /* BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.FORGOT_PASSWORD, response);
                        if (baseRespModel != null) {
                            if (baseRespModel.isSuccess()) {
                                Utils.showToast(AssignedSalesDetailActivity.this, baseRespModel.getMessage());
                                app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", "notification");
                                Intent intent = new Intent(AssignedSalesDetailActivity.this, SalesActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Utils.showToast(AssignedSalesDetailActivity.this, baseRespModel.getMessage());
                            }
                        } else {
                            Utils.showToast(AssignedSalesDetailActivity.this, getString(R.string.something_went_wrong));
                        }*/
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
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.ENQUIRY_ID, enquiryId);
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

    private void setData(final AsmSalesLeadDetailModel mDetailsModel) {
        campaignName = mDetailsModel.getCampaign();
        if (!TextUtils.isEmpty(campaignName)) {
            tv_campaign_val.setText(" : " + campaignName);

            campaignDate = mDetailsModel.getCampaign_Date();
            if (!TextUtils.isEmpty(campaignDate)) {
                tv_campaign_date_val.setText(" :" + campaignDate);
            } else {
                linear_layout_campaign_date.setVisibility(View.GONE);
                linear_layout_campaign_date_divider.setVisibility(View.GONE);
            }
        } else {
            linear_layout_campaign_name.setVisibility(View.GONE);
            tv_campaign_id_divider.setVisibility(View.GONE);
            linear_layout_campaign_date.setVisibility(View.GONE);
            linear_layout_campaign_date_divider.setVisibility(View.GONE);
        }

        tv_mobile_no_val.setText(" : " + mDetailsModel.getMobile_No());
        tv_project_name_val.setText(" : " + Utils.getMultiSelectedProject(mDetailsModel.getSelectedProjList()));
        tv_budget_val.setText(" : " + mDetailsModel.getBudget());
        tv_current_status_val.setText(" : " + mDetailsModel.getCurrent_Status());
        if (mDetailsModel.getCurrent_Status().equalsIgnoreCase("meeting")) {
            tv_status_address.setText("Meeting Address");
            tv_status_date_time.setText("Meeting Date and Time");
            tv_status_date_time_val.setText(" : " + mDetailsModel.getScheduledatetime());
            tv_status_address_val.setText(" : " + mDetailsModel.getAddress());
        } else if (mDetailsModel.getCurrent_Status().equalsIgnoreCase("site visit")) {
            tv_status_address.setText("Site Visit Address");
            tv_status_date_time.setText("Site Visit Date and Time");
            tv_status_date_time_val.setText(" : " + mDetailsModel.getScheduledatetime());
            tv_status_address_val.setText(mDetailsModel.getAddress());
        } else {
            tv_status_date_time.setText("Callback Date and Time");
            tv_status_date_time_val.setText(" : " + mDetailsModel.getScheduledatetime());
            tv_status_address_val.setText(" : " + mDetailsModel.getAddress());
            layout_status_address.setVisibility(View.GONE);
            layout_status_address_divider.setVisibility(View.GONE);
        }
        tv_customer_name_val.setText(" : " + mDetailsModel.getName());
        tv_email_id_val.setText(" : " + mDetailsModel.getEmail_ID());
        alternateMobileNo = mDetailsModel.getAlternate_Mobile_No();
        if (!TextUtils.isEmpty(alternateMobileNo)) {
            tv_alternate_mobile_no_val.setText(" : " + alternateMobileNo);
        } else {
            linear_layout_alternate_mobile.setVisibility(View.GONE);
            tv_alternate_mobile_no_divider.setVisibility(View.GONE);
        }
        toolbar.setTitle(getString(R.string.toolbar_txt_broker_status_and_type, mDetailsModel.getName(), mDetailsModel.getEnquiry_ID()));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(AssignedSalesDetailActivity.this)) {
                    acceptLead(mDetailsModel.getEnquiry_ID());
                    isSynced = 0;
                }
                CustomerInfoEntity customerEntity = new CustomerInfoEntity(mDetailsModel.getMobile_No(), mDetailsModel.getEnquiry_ID(), mDetailsModel.getName(),
                        BMHConstants.LEAD_PHASE_SALES, "1");
                new CustomerInfoTask(customerEntity, true).execute();
                new InsertSalesLeadDetailsTask(AssignedSalesDetailActivity.this, mDetailsModel.getEnquiry_ID(), BMHConstants.ACTION_ACCEPT, isSynced).execute();
                app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                onBackPressed();

            }
        });
        button_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(AssignedSalesDetailActivity.this)) {
                    rejectLead(mDetailsModel.getEnquiry_ID());
                    isSynced = 0;
                }
                CustomerInfoEntity customerEntity = new CustomerInfoEntity(mDetailsModel.getMobile_No(), mDetailsModel.getEnquiry_ID(), mDetailsModel.getName(),
                        BMHConstants.LEAD_PHASE_PRE_SALES, "0");
                new CustomerInfoTask(customerEntity, true).execute();

                new InsertSalesLeadDetailsTask(AssignedSalesDetailActivity.this, mDetailsModel.getEnquiry_ID(), BMHConstants.ACTION_REJECT, isSynced).execute();
                app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                onBackPressed();

            }
        });
    }

    private void rejectLead(final String enquiryId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.REJECT_LEAD),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.FORGOT_PASSWORD, response);
                        if (baseRespModel != null) {
                            if (baseRespModel.isSuccess()) {
                                Utils.showToast(AssignedSalesDetailActivity.this, baseRespModel.getMessage());
                                app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                                onBackPressed();
                            } else {
                                Utils.showToast(AssignedSalesDetailActivity.this, baseRespModel.getMessage());
                            }
                        } else {
                            Utils.showToast(AssignedSalesDetailActivity.this, getString(R.string.something_went_wrong));
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
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.ENQUIRY_ID, enquiryId);
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

    @Override
    public void getSalesAssignedDetail(SalesLeadDetailEntity detailsEntity, List<SelectMultipleProjectsEntity> multipleProjectsEntityList) {
        ArrayList<NotificationDataModel> notificationDataModelList = new ArrayList<>();
        String alertDate = "";
        String alertTime = "";
        String alertStatus = "";
        String enquiryId = "";
        String customerName = "";
        String mobileNo = "";
        int isUpdated;
        Calendar cal = Calendar.getInstance();
        if (detailsEntity != null) {
            // ALARM CODE
            alertTime = detailsEntity.getTime();
            alertDate = detailsEntity.getDate();
            alertStatus = detailsEntity.getCurrentStatus();
            enquiryId = detailsEntity.getEnquiryId();
            customerName = detailsEntity.getCustomerName();
            mobileNo = detailsEntity.getCustomerMobile();
            isUpdated = detailsEntity.getIsUpdated();
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
                if (System.currentTimeMillis() < cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_ONE_HOUR) {
                    notificationDataModelList.add(new NotificationDataModel(alertTime, alertDate, alertStatus, enquiryId, customerName, mobileNo, isUpdated));
                }
            }
           /* Intent intent = getIntent();
            mAsmModel = intent.getParcelableExtra(BMHConstants.ASM_MODEL_DATA);
            mDetailsModel = intent.getParcelableExtra(BMHConstants.ASM_DETAIL_DATA);
            mLeadStatusList = intent.getParcelableArrayListExtra(BMHConstants.LEAD_LIST);
            mProjectsList = intent.getParcelableArrayListExtra(BMHConstants.PROJECT_LIST);
            mNotInterestedList = intent.getParcelableArrayListExtra(BMHConstants.NOT_INTERESTED_LIST);
            mClosureList = intent.getParcelableArrayListExtra(BMHConstants.CLOSURE_LIST);
            mBrokerList = intent.getParcelableArrayListExtra(BMHConstants.BROKER_LIST);
            setLeadDetails(mDetailsModel);*/
        }

        if (tagReceivedFrom.equalsIgnoreCase(AlertDialogActivity.TAG)) {
            ArrayList<Projects> selectedProjList = new ArrayList<>();

            if (multipleProjectsEntityList != null && multipleProjectsEntityList.size() > 0) {
                int selectedLength = multipleProjectsEntityList.size();
                for (int j = 0; j < selectedLength; j++) {
                    selectedProjList.add(new Projects(multipleProjectsEntityList.get(j).getProjectId(), multipleProjectsEntityList.get(j).getProjectName()));
                }
            }

            mDetailsModel = new AsmSalesLeadDetailModel(enquiryId, detailsEntity.getCampaignName(), detailsEntity.getCampaignDate(),
                    customerName, detailsEntity.getCustomerEmail(), detailsEntity.getCustomerMobile(),
                    detailsEntity.getCustomerAlternateMobile(), detailsEntity.getBudget(), detailsEntity.getCurrentStatus(),
                    detailsEntity.getLastupdatedon(), detailsEntity.getDate(), detailsEntity.getTime(), "",
                    "", "", detailsEntity.getAddress(), selectedProjList,
                    new ArrayList<SubStatus>(), "", "", 0,
                    detailsEntity.getAssignedTo(), detailsEntity.getIsLeadType(), "", "",
                    "", "", "", "", "", "");
            setData(mDetailsModel);
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
