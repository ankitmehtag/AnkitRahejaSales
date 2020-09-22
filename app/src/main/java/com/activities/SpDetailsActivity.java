package com.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.CustomSpinnerAdapter;
import com.adapters.MultiSelectAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.database.task.ChangeRecordingStatusTask;
import com.database.task.GetPreSalesLeadDetailsTask;
import com.database.task.GetStatusMasterTask;
import com.database.task.GetPreSalesLeadDetailTask;
import com.database.task.GetMultipleProjectTask;
import com.database.task.InsertCallbackTask;
import com.database.task.InsertPreSalesLeadDetailsTask;
import com.database.task.InsertMeetingTask;
import com.database.task.InsertMultiSelectTask;
import com.database.task.InsertSiteVisitTask;
import com.database.entity.CallbackEntity;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.MeetingEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SiteVisitEntity;
import com.database.entity.StatusMasterEntity;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.Assign_To;
import com.model.Details;
import com.model.LeadStatus;
import com.model.NotificationDataModel;
import com.model.PreSalesSpModel;
import com.model.Projects;
import com.model.SelectableProject;
import com.services.AlarmJobServices;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SpDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnTouchListener,
        GetMultipleProjectTask.IMultiProjCommunicator, GetStatusMasterTask.IStatusMasterCommunicator,
        MultiSelectAdapter.OnItemSelectedListener, GetPreSalesLeadDetailTask.ILeadDetailCommunicator,
        GetPreSalesLeadDetailsTask.IPreSalesLeadDetailsCommunicator {

    static final String TAG = SpDetailsActivity.class.getSimpleName();
    PreSalesSpModel mSpModel;
    Details mDetailsModel;
    Projects projectsModel;
    LeadStatus leadStatusModel;
    Button btnUpdate;
    ArrayList<String> spProjectList = new ArrayList<>();
    ArrayList<String> spLeadStatusList = new ArrayList<>();
    ProgressDialog dialog;
    BMHApplication app;
    int defaultPos = 0, alarmIndex;
    private int defaultPosLeadStatus;
    private int defaultPosMinBudget;
    private int defaultPosMaxBudget;
    boolean isInitialProject = false;
    boolean isInitialLeadStatus = false;
    boolean isInitialPersonCount = false;
    private int defaultPosPersonCount;
    private boolean isInitialMinBudget = false;
    private boolean isInitialMaxBudget = false;
    ArrayAdapter locationAdapter, minAdapter, maxAdapter;
    Spinner mSpinnerProject, mSpinnerStatus, mSpinnerLocation, mSpinnerMinBudget, mSpinnerMaxBudget;
    LinearLayout layout_lead_type, layout_address, layout_location, layout_time, layout_date, layout_button_update;
    ConstraintLayout layout_select_lead;
    LinearLayout layout_date_divider, layout_time_divider, layout_location_divider, layout_address_divider, layout_lead_type_divider, layout_budget_spinner;
    TextView tv_date, tv_date_val, tv_time, tv_time_val, tv_project_name, tv_select_location, tv_address, tv_budget_val,
            tv_campaign_val, tv_campaign_date_val, tv_mobile_no_val, textView_projects;
    RadioButton radioButton_hot, radioButton_warm, radioButton_cold;
    RadioGroup radioGroup;
    ImageView img_send_email;
    EditText edtRemark, tv_alternate_mobile_no_val, tv_customer_name_val, tv_email_id_val, tv_address_val;
    CustomSpinnerAdapter leadStatusAdapter;
    List<Projects> mProjectsList = new ArrayList<>();
    ArrayList<Projects> mSelectedProjList = new ArrayList<>();
    ArrayList<String> budgetMin = new ArrayList<>();
    ArrayList<String> budgetMax = new ArrayList<>();
    String mMinBudget, mMaxBudget;
    List<Projects> mSelectedMultiProjList;
    ArrayList<LeadStatus> mLeadStatusList;
    boolean isMultiSpinner = false;
    AlertDialog.Builder alertDialog;
    LinkedList<Projects> selectedProjList = new LinkedList<>();
    private Set<Projects> set = null;
    RadioButton radioButton;
    MultiSelectAdapter mAdapter;
    String enquiryId = "", mRecFilePath = "", strBudgetRange, tagReceivedFrom;
    String mCustomerName, tagReceived, mRemarkText, mSelectedLeadStatus, mSelectedLeadStatusId = "0", mDateText, mTimeText, mSelectedTab = "";
    String mSelectedProjectName, mSelectedProjectId = "0", mSelectedLocation, mSelectedRadioButton, timeFormat, mSelectedPersonCount = "";
    int mPersonCount = 0, updateFromAlarmNotification = 0;
    Calendar selectedCal;
    Calendar currentCal;
    Calendar mCurrentTime_180;
    Date currentDate = null, selectedDate = null;
    int mYear, mMonth, mDay;
    final String dateFormat = Utils.dateFormat;
    LinearLayout linear_layout_email, tv_email_id_divider, tv_campaign_id_divider,
            linear_layout_campaign_date, tv_alternate_mobile_no_divider, linear_layout_alternate_mobile, linear_layout_campaign_name;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    private int hasExtCallPermission;
    private List<String> permissionsCall = new ArrayList<>();
    private String userDesignation, mobileNo;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};

    View convertView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_details);

        btnUpdate = findViewById(R.id.button_update);
        mSpinnerProject = findViewById(R.id.spinner_select_project);
        mSpinnerStatus = findViewById(R.id.spinner_select_status);
        mSpinnerLocation = findViewById(R.id.spinner_select_location);
        mSpinnerMinBudget = findViewById(R.id.spinner_min_budget);
        mSpinnerMaxBudget = findViewById(R.id.spinner_max_budget);

        layout_button_update = findViewById(R.id.layout_button_update);
        layout_lead_type = findViewById(R.id.layout_lead_type);
        layout_address = findViewById(R.id.layout_address);
        layout_location = findViewById(R.id.layout_location);
        layout_time = findViewById(R.id.layout_time);
        layout_date = findViewById(R.id.layout_date);
        layout_budget_spinner = findViewById(R.id.layout_budget_spinner);
        textView_projects = findViewById(R.id.textView_projects);
        layout_date_divider = findViewById(R.id.layout_date_divider);
        layout_time_divider = findViewById(R.id.layout_time_divider);
        layout_location_divider = findViewById(R.id.layout_location_divider);
        layout_address_divider = findViewById(R.id.layout_address_divider);
        layout_lead_type_divider = findViewById(R.id.layout_lead_type_divider);
        layout_select_lead = findViewById(R.id.layout_select_lead);
        linear_layout_email = findViewById(R.id.linear_layout_email);
        tv_email_id_divider = findViewById(R.id.tv_email_id_divider);
        tv_campaign_id_divider = findViewById(R.id.tv_campaign_id_divider);
        linear_layout_campaign_date = findViewById(R.id.linear_layout_campaign_date);
        tv_alternate_mobile_no_divider = findViewById(R.id.tv_alternate_mobile_no_divider);
        linear_layout_alternate_mobile = findViewById(R.id.linear_layout_alternate_mobile);
        linear_layout_campaign_name = findViewById(R.id.linear_layout_campaign_name);

        tv_campaign_val = findViewById(R.id.tv_campaign_val);
        tv_campaign_date_val = findViewById(R.id.tv_campaign_date_val);
        tv_customer_name_val = findViewById(R.id.tv_customer_name_val);
        tv_email_id_val = findViewById(R.id.tv_email_id_val);
        img_send_email = findViewById(R.id.img_send_email);
        tv_mobile_no_val = findViewById(R.id.tv_mobile_no_val);
        tv_alternate_mobile_no_val = findViewById(R.id.tv_alternate_mobile_no_val);

        tv_date = findViewById(R.id.tv_date);
        tv_date_val = findViewById(R.id.tv_date_val);
        tv_time = findViewById(R.id.tv_time);
        tv_time_val = findViewById(R.id.tv_time_val);
        tv_project_name = findViewById(R.id.tv_project_name);
        tv_select_location = findViewById(R.id.tv_select_location);
        tv_address = findViewById(R.id.tv_address);
        tv_address_val = findViewById(R.id.tv_address_val);
        tv_budget_val = findViewById(R.id.tv_budget_val);

        edtRemark = findViewById(R.id.edit_text_remark);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_hot = findViewById(R.id.radioButton_hot);
        radioButton_warm = findViewById(R.id.radioButton_warm);
        radioButton_cold = findViewById(R.id.radioButton_cold);

         /*   tv_date_val.setText(mDetailsModel.getDate());
            tv_time_val.setText(mDetailsModel.getTime());
            edtRemark.setText(mDetailsModel.getRemark());
            if (!TextUtils.isEmpty(mDetailsModel.getLead_type()) || !mDetailsModel.getLead_type().equals("")) {
                int leadType = getLeadType(mDetailsModel.getLead_type());
                radioGroup.check(leadType);
            }*/

        mSpinnerProject.setOnItemSelectedListener(this);
        mSpinnerStatus.setOnItemSelectedListener(this);
        mSpinnerLocation.setOnItemSelectedListener(this);
        mSpinnerMinBudget.setOnItemSelectedListener(this);
        mSpinnerMaxBudget.setOnItemSelectedListener(this);
        tv_mobile_no_val.setOnClickListener(this);
        img_send_email.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        tv_date_val.setOnClickListener(this);
        tv_time_val.setOnClickListener(this);
        radioButton_hot.setOnClickListener(this);
        radioButton_warm.setOnClickListener(this);
        radioButton_cold.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        init();

    }

    @Override
    public void getLeadsDetailCallback(List<SelectMultipleProjectsEntity> multipleProjectsEntityList,
                                       CallbackEntity callbackEntity, MeetingEntity meetingEntity,
                                       SiteVisitEntity siteVisitEntity, PreSalesLeadDetailsEntity ldEntity) {
        String address = "";
        String remark = "";
        String leadType = "";
        String date = "";
        String time = "";
        String budget = "";
        int noOfPerson = 0;

        List<Projects> mProjectsList = PreSalesActivity.mProjectsList;
        ArrayList<LeadStatus> mLeadsList = PreSalesActivity.mLeadsList;
        ArrayList<Assign_To> mSpList = PreSalesActivity.mSpList;
        budget = ldEntity.getBudgetMin() + "-" + ldEntity.getBudgetMax();

        mSelectedProjList = new ArrayList<>();
        for (int i = 0; i < multipleProjectsEntityList.size(); i++) {
            mSelectedProjList.add(new Projects(multipleProjectsEntityList.get(i).getProjectId(), multipleProjectsEntityList.get(i).getProjectName()));
        }

        if (mSelectedProjList.size() > 0)
            mSelectedProjectName = Utils.getMultiSelectedProject(mSelectedProjList);
        else {
            try {
                new GetMultipleProjectTask(this, enquiryId).execute();
                //  Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mSpModel = new PreSalesSpModel(ldEntity.getEnquiryId(), ldEntity.getCustomerName(), ldEntity.getCustomerMobile(),
                ldEntity.getCustomerEmail(), ldEntity.getCampaignName(), ldEntity.getCampaignDate(), ldEntity.getCurrentStatus(),
                mSelectedProjectName, ldEntity.getRemark(), ldEntity.getDate(), ldEntity.getTime(), "",
                ldEntity.getIsAssigned(), ldEntity.getScheduledatetime(), ldEntity.getCustomerAlternateMobile(), budget,
                ldEntity.getBudgetMin(), ldEntity.getBudgetMax(), ldEntity.getLastupdatedon());
        mSelectedLeadStatus = ldEntity.getCurrentStatus();
        //CALLBACK
        if (mSpModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {

            if (callbackEntity != null) {
                remark = callbackEntity.getRemark();
                leadType = callbackEntity.getLeadType();
                date = callbackEntity.getDate();
                time = callbackEntity.getTime();
            }

            mDetailsModel = new Details(mSpModel.getEnquiry_id(), mSpModel.getCampaign_name(), mSpModel.getCampaign_date(),
                    mSpModel.getCustomer_name(), mSpModel.getCustomer_email(), mSpModel.getCustomer_mobile(),
                    mSpModel.getCustomer_alt_mobile(), mSpModel.getBudget(), mSpModel.getStatus(),
                    mSpModel.getScheduledatetime(), date, time, "", mSelectedProjectName, mProjectsList, mSelectedProjList,
                    mSpList, mLeadsList, remark, leadType, mSpModel.getBudgetMin(), mSpModel.getBudgetMax(), 0,
                    mSpModel.getLastupdatedon());

            setLeadDetails(mDetailsModel);
            return;
        }
        //MEETING
        if (mSpModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {

            if (meetingEntity != null) {
                address = meetingEntity.getAddress();
                remark = meetingEntity.getRemark();
                leadType = meetingEntity.getLeadType();
                date = meetingEntity.getMeetingDate();
                time = meetingEntity.getMeetingTime();
            }
            mDetailsModel = new Details(mSpModel.getEnquiry_id(), mSpModel.getCampaign_name(), mSpModel.getCampaign_date(),
                    mSpModel.getCustomer_name(), mSpModel.getCustomer_email(), mSpModel.getCustomer_mobile(),
                    mSpModel.getCustomer_alt_mobile(), mSpModel.getBudget(), mSpModel.getStatus(),
                    mSpModel.getScheduledatetime(), date, time, address, mSelectedProjectName, mProjectsList, mSelectedProjList,
                    mSpList, mLeadsList, remark, leadType, mSpModel.getBudgetMin(), mSpModel.getBudgetMax(), 0,
                    mSpModel.getLastupdatedon());
            setLeadDetails(mDetailsModel);
            return;
        }
        //SITE VISIT
        if (mSpModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5])) {

            if (siteVisitEntity != null) {
                address = siteVisitEntity.getAddress();
                remark = siteVisitEntity.getRemark();
                leadType = siteVisitEntity.getLeadType();
                date = siteVisitEntity.getDate();
                time = siteVisitEntity.getTime();
                noOfPerson = Integer.valueOf(siteVisitEntity.getNoOfPersonVisited());
            }

            mDetailsModel = new Details(mSpModel.getEnquiry_id(), mSpModel.getCampaign_name(), mSpModel.getCampaign_date(),
                    mSpModel.getCustomer_name(), mSpModel.getCustomer_email(), mSpModel.getCustomer_mobile(),
                    mSpModel.getCustomer_alt_mobile(), mSpModel.getBudget(), mSpModel.getStatus(),
                    mSpModel.getScheduledatetime(), date, time, address, mSelectedProjectName, mProjectsList, mSelectedProjList,
                    mSpList, mLeadsList, remark, leadType, mSpModel.getBudgetMin(), mSpModel.getBudgetMax(),
                    noOfPerson, mSpModel.getLastupdatedon());
            setLeadDetails(mDetailsModel);
            return;
        } else {
            mDetailsModel = new Details(mSpModel.getEnquiry_id(), mSpModel.getCampaign_name(), mSpModel.getCampaign_date(),
                    mSpModel.getCustomer_name(), mSpModel.getCustomer_email(), mSpModel.getCustomer_mobile(),
                    mSpModel.getCustomer_alt_mobile(), mSpModel.getBudget(), mSpModel.getStatus(),
                    mSpModel.getScheduledatetime(), date, time, "", mSelectedProjectName, mProjectsList, mSelectedProjList,
                    mSpList, mLeadsList, mSpModel.getRemark(), "", mSpModel.getBudgetMin(), mSpModel.getBudgetMax(),
                    0, mSpModel.getLastupdatedon());
            setLeadDetails(mDetailsModel);
        }

    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            isInitialProject = true;
            isInitialPersonCount = true;
            isInitialLeadStatus = true;
            isInitialMinBudget = true;
            isInitialMaxBudget = true;
            selectedCal = Calendar.getInstance();
            currentCal = Calendar.getInstance();
            app = (BMHApplication) getApplication();
            userDesignation = app.getFromPrefs((BMHConstants.USER_DESIGNATION));
            tagReceivedFrom = intent.getStringExtra(BMHConstants.PATH);
            if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                enquiryId = intent.getStringExtra(BMHConstants.ENQUIRY_ID);
                alarmIndex = intent.getIntExtra(BMHConstants.ALARM_INDEX, 0);
                updateFromAlarmNotification = 1;
                // getLeadDetails(enquiryId, userDesignation);
            } else if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(AlertDialogActivity.TAG)) {
                mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
                enquiryId = intent.getStringExtra(BMHConstants.ENQUIRY_ID);
                mRecFilePath = intent.getStringExtra(BMHConstants.RECORDING_FILE_PATH);
            } else {
                mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
                mSpModel = intent.getParcelableExtra(BMHConstants.SP_MODEL);
                enquiryId = mSpModel.getEnquiry_id();
                mCustomerName = mSpModel.getCustomer_name();
            }
            if (ConnectivityReceiver.isConnected()) {
                //API CALL - GET_SP_LEADS_DETAILS
                mDetailsModel = intent.getParcelableExtra(BMHConstants.SP_DETAIL_DATA);
                if (mDetailsModel == null && !TextUtils.isEmpty(enquiryId))
                    // GET OFFLINE DATA FROM DB
                    new GetPreSalesLeadDetailTask(SpDetailsActivity.this, enquiryId).execute();
                else
                    setLeadDetails(mDetailsModel);
            } else {
                // GET OFFLINE DATA FROM DB
                if (!TextUtils.isEmpty(enquiryId)) {
                    new GetPreSalesLeadDetailTask(SpDetailsActivity.this, enquiryId).execute();
                }
            }
        }
    }

    private void getLeadDetails(final String enquiry_id, final String designation) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.PRE_SALES_LEAD_DETAIL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject data = jsonObject.optJSONObject("data");
                                if (data != null) {
                                    PreSalesSpModel preSalesSpModel = (PreSalesSpModel) JsonParser.convertJsonToBean(APIType.PRE_SALES_DATA, data.toString());
                                    mSpModel = preSalesSpModel;
                                    JSONObject object = data.getJSONObject("details");
                                    Details mDetailsModelNotification = (Details) JsonParser.convertJsonToBean(APIType.PRE_SALES_LEAD_DETAIL, object.toString());
                                    mDetailsModel = mDetailsModelNotification;
                                    setLeadDetails(mDetailsModel);
                                } else {
                                    Utils.showToast(SpDetailsActivity.this, "No lead Data Available");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.showToast(SpDetailsActivity.this, getString(R.string.something_went_wrong));
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

    private void setLeadDetails(Details mDetailsModel) {
        if (mDetailsModel != null) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.toolbar_txt_broker_status_and_type, mDetailsModel.getName(), mDetailsModel.getEnquiry_ID()));
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            String campaignName = mDetailsModel.getCampaign();
            if (campaignName != null && !TextUtils.isEmpty(campaignName)) {
                tv_campaign_val.setText(getString(R.string.txt_sp_campaign_detail, campaignName));
                tv_campaign_date_val.setText(mDetailsModel.getCampaign_Date());
            } else {
                linear_layout_campaign_name.setVisibility(View.GONE);
                linear_layout_campaign_date.setVisibility(View.GONE);
                tv_campaign_id_divider.setVisibility(View.GONE);
            }
            tv_customer_name_val.setText(mDetailsModel.getName());
            if (!TextUtils.isEmpty(mDetailsModel.getEmail_ID()) && mDetailsModel.getEmail_ID() != null) {
                tv_email_id_val.setText(mDetailsModel.getEmail_ID());
            } else {
                linear_layout_email.setVisibility(View.GONE);
                tv_email_id_divider.setVisibility(View.GONE);
            }
            tv_mobile_no_val.setText(mDetailsModel.getMobile_No());
            //   ((TextView) findViewById(R.id.tv_project_name_val)).setText(mDetailsModel.getProject_Name());
            tv_alternate_mobile_no_val.setText(mDetailsModel.getAlternate_Mobile_No());
            //   ((TextView) findViewById(R.id.tv_budget_val)).setText(model.getB());

            tv_date_val.setText(mDetailsModel.getDate());
            tv_time_val.setText(mDetailsModel.getTime());
            edtRemark.setText(mDetailsModel.getRemark());
            tv_address_val.setText(mDetailsModel.getAddress());

            mProjectsList = mDetailsModel.getProjectList();
            mSelectedMultiProjList = getSelectedProject(mDetailsModel.getSelectedProjList());
            mLeadStatusList = mDetailsModel.getLeadStatusList();
            //   mLocListKey = mDetailsModel.getLocListKey();
            //   mLocListValue = mDetailsModel.getLocListValue();

            if (mLeadStatusList.size() > 0) {
                for (int i = 0; i < mLeadStatusList.size(); i++) {
                    leadStatusModel = mLeadStatusList.get(i);
                    spLeadStatusList.add(leadStatusModel.getTitle());
                }
                if (spLeadStatusList.size() > 0)
                    getSelectedLeadStatus(mLeadStatusList, mSpModel.getStatus());
            } else {
                new GetStatusMasterTask(this).execute();
            }

            //  spLocationList.addAll(mLocListKey);
            /*
               SPINNER PROJECT
             */
           /* for (int i = 0; i < mProjectsList.size(); i++) {
                String text = mProjectsList.get(i).getProject_name();
                if (!TextUtils.isEmpty(text))
                    spProjectList.add(text);
            }*/
            for (int i = 0; i < mSelectedMultiProjList.size(); i++) {
                projectsModel = mSelectedMultiProjList.get(i);
                spProjectList.add(projectsModel.getProject_name());
            }

           /* if ((mSpModel != null && mSpModel.getIsAssigned() == 0)
                    || !TextUtils.isEmpty(tagReceived)) {
                spProjectList.add(0, getString(R.string.spinner_select_project));
                mSpinnerProject.setSelection(0);
                getSelectedProject(spProjectList, mSpModel.getProject_name());
            } else {
                String SelectMultipleProjectsEntity = mSpModel.getProject_name();
                int projectSize = spProjectList.size();
                for (int i = 0; i < projectSize; i++) {
                    if (spProjectList.get(i).equalsIgnoreCase(SelectMultipleProjectsEntity)) {
                        mSpinnerProject.setSelection(i);
                        return;
                    }
                }
            }
           */
            if (mSelectedMultiProjList != null && mSelectedMultiProjList.size() == 0) {
                spProjectList.add(0, getString(R.string.spinner_select_project));
                textView_projects.setText(spProjectList.get(0).toString());
                mSelectedProjectName = spProjectList.get(0).toString();
            } else {
                String strMulti = Utils.getMultiSelectedProject(mSelectedMultiProjList);
                textView_projects.setText(strMulti);
                mSelectedProjectName = strMulti;
                mSelectedProjectId = Utils.getSelectedMultiProjectIds(mSelectedMultiProjList);
            }

           /* if (((mSpModel != null && mSpModel.getIsAssigned() == 0)
                    || !TextUtils.isEmpty(tagReceived)) || (mSelectedMultiProjList != null && mSelectedMultiProjList.size() == 0)) {
                spProjectList.add(0, getString(R.string.spinner_select_project));
                textView_projects.setText(spProjectList.get(0));
                mSelectedProjectName = spProjectList.get(0).toString();
            } else {
                //  getSelectedProject(spProjectList, mDetailsModel.getProjectName());
                getMultiSelectedProject(mSelectedMultiProjList);
                mSelectedProjectId = getSelectedMultiProjectIds(mSelectedMultiProjList);
            }*/



           /* CustomSpinnerAdapter projectAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spProjectList);
            projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            mSpinnerProject.setAdapter(projectAdapter);*/

            if (!TextUtils.isEmpty(mSelectedProjectName)) {
                if (!mSelectedProjectName.equalsIgnoreCase(getString(R.string.spinner_select_project))) {

                    for (int i = 0; i < mProjectsList.size(); i++) {
                        if (mSelectedProjectName.contains(mProjectsList.get(i).getProject_name()))
                            selectedProjList.add(mProjectsList.get(i));
                    }
                }
            }
            textView_projects.setText(mSelectedProjectName);
            mSpinnerProject.setOnTouchListener(this);

            /*
               SPINNER LEADS STATUS
             */

            /*for (int i = 0; i < leadsList.size(); i++) {
                leadStatusModel = leadsList.get(i);
                spLeadStatusList.add(leadStatusModel.getTitle());
            }*/

            if (TextUtils.isEmpty(mSpModel.getStatus()) || mSpModel.getStatus().equalsIgnoreCase("")
                    || mSpModel.getStatus().equalsIgnoreCase("NA") || !TextUtils.isEmpty(tagReceived)) {
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
            } else if (mSpModel.getStatus().equalsIgnoreCase(getString(R.string.spinner_select_status))) {
                mSpModel.setStatus("");
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
            } else {
                if (spLeadStatusList.size() == 0) {
                    new GetStatusMasterTask(this).execute();
                }
            }

            if (mLeadStatusList.size() > 0) {
                if (spLeadStatusList.size() > 0)
                    spLeadStatusList.clear();
                for (int i = 0; i < mLeadStatusList.size(); i++) {
                    leadStatusModel = mLeadStatusList.get(i);
                    spLeadStatusList.add(leadStatusModel.getTitle());
                }
                getSelectedLeadStatus(mLeadStatusList, mSpModel.getStatus());
            } else {
                try {
                    new GetStatusMasterTask(this).execute();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            }

            for (int i = 0; i < mSelectedMultiProjList.size(); i++) {
                projectsModel = mSelectedMultiProjList.get(i);
                spProjectList.add(projectsModel.getProject_name());
            }


          /*  if ((mSpModel != null && mSpModel.getIsAssigned() == 0)
                    || !TextUtils.isEmpty(tagReceived)) {
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
                mSpinnerStatus.setSelection(0);
            } else {
                String selectedStatus = mSpModel.getStatus();
                int leadSize = spLeadStatusList.size();
                for (int i = 0; i < leadSize; i++) {
                    leadStatusModel = mLeadStatusList.get(i);
                    if (leadStatusModel.getTitle().equalsIgnoreCase(selectedStatus)) {
                        mSpinnerStatus.setSelection(i);
                        return;
                    }
                }
            }*/
            leadStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLeadStatusList);
            leadStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            /* Setting the ArrayAdapter data on the Spinner */
            mSpinnerStatus.setAdapter(leadStatusAdapter);
            mSpinnerStatus.setOnItemSelectedListener(this);

          /*  locationAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLocationList);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            mSpinnerLocation.setAdapter(locationAdapter);*/
            mSpinnerLocation.setOnItemSelectedListener(this);

            if (mSpModel != null && mSpModel.getIsAssigned() == 1) {
                if (mSpModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])
                        || mSpModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])
                        || mSpModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5])) {
                    layout_button_update.setVisibility(View.GONE);
                    mSpinnerProject.setEnabled(false);
                    mSpinnerStatus.setEnabled(false);
                    mSpinnerMinBudget.setEnabled(false);
                    mSpinnerMaxBudget.setEnabled(false);
                    mSpinnerLocation.setEnabled(false);
                    tv_alternate_mobile_no_val.setFocusable(false);
                    tv_customer_name_val.setFocusable(false);
                    tv_email_id_val.setFocusable(false);
                    tv_date_val.setFocusable(false);
                    tv_date_val.setClickable(false);
                    tv_time_val.setFocusable(false);
                    tv_time_val.setClickable(false);
                    tv_address_val.setFocusable(false);
                    edtRemark.setFocusable(false);
                    radioButton_hot.setClickable(false);
                    radioButton_warm.setClickable(false);
                    radioButton_cold.setClickable(false);
                } else {
                    layout_button_update.setVisibility(View.VISIBLE);
                    mSpinnerProject.setEnabled(true);
                    mSpinnerStatus.setEnabled(true);
                    mSpinnerMinBudget.setEnabled(true);
                    mSpinnerMaxBudget.setEnabled(true);
                    mSpinnerLocation.setEnabled(true);
                    tv_alternate_mobile_no_val.setFocusable(true);
                    tv_customer_name_val.setFocusable(true);
                    tv_email_id_val.setFocusable(true);
                    tv_date_val.setFocusable(true);
                    tv_date_val.setClickable(true);
                    tv_time_val.setFocusable(true);
                    tv_time_val.setClickable(true);
                    tv_address_val.setFocusable(true);
                    edtRemark.setFocusable(true);
                    radioGroup.setActivated(true);
                    radioButton_hot.setClickable(true);
                    radioButton_warm.setClickable(true);
                    radioButton_cold.setClickable(true);
                }
                mSelectedTab = getString(R.string.tab_updated);
            } else
                mSelectedTab = getString(R.string.tab_pending);
            String[] minArray = getResources().getStringArray(R.array.array_min_budget);
            String[] maxArray = getResources().getStringArray(R.array.array_max_budget);

            budgetMin.addAll(Arrays.asList(minArray));
            budgetMax.addAll(Arrays.asList(maxArray));

            strBudgetRange = mDetailsModel.getBudget();
            if (!TextUtils.isEmpty(strBudgetRange)) {
                layout_budget_spinner.setVisibility(View.VISIBLE);
                tv_budget_val.setVisibility(View.GONE);

                String[] arrayBudget = strBudgetRange.split("-");
                //   budgetMin.add(Utils.integerToStringMoney(arrayBudget[0]));
                String strMin = Utils.integerToStringMoney(arrayBudget[0]);
                for (int i = 0; i < minArray.length; i++) {
                    if (minArray[i].equalsIgnoreCase(strMin)) {
                        mSpinnerMinBudget.setSelection(i);
                        defaultPosMinBudget = i;
                        break;
                    }
                }
                String strMax = Utils.integerToStringMoney(arrayBudget[1]);
                // budgetMax.add(Utils.integerToStringMoney(arrayBudget[1]));

                for (int i = 0; i < maxArray.length; i++) {
                    if (maxArray[i].equalsIgnoreCase(strMax)) {
                        mSpinnerMaxBudget.setSelection(i);
                        defaultPosMaxBudget = i;
                        break;
                    }
                }
            } else {

               /* layout_budget_spinner.setVisibility(View.GONE);
                tv_budget_val.setText("Not Specified");
                tv_budget_val.setPadding(0, 0, 50, 0);
                tv_budget_val.setVisibility(View.VISIBLE);*/

                budgetMin.add(0, getString(R.string.txt_min_budget));
                budgetMax.add(0, getString(R.string.txt_max_budget));

            }

            mSpinnerMinBudget.setPadding(0, 0, 50, 0);
            minAdapter = new ArrayAdapter(SpDetailsActivity.this, R.layout.textview_spinner, budgetMin);
            minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerMinBudget.setAdapter(minAdapter);
            mSpinnerMinBudget.setOnItemSelectedListener(this);

            maxAdapter = new ArrayAdapter(SpDetailsActivity.this, R.layout.textview_spinner, budgetMax);
            maxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerMaxBudget.setPadding(0, 0, 50, 0);
            mSpinnerMaxBudget.setAdapter(maxAdapter);
            mSpinnerMaxBudget.setOnItemSelectedListener(this);
        }
    }

    /* private String getSelectedProject(ArrayList<String> spProjectList, String projectName) {
         if (!TextUtils.isEmpty(projectName) || !projectName.equalsIgnoreCase("") || !projectName.equalsIgnoreCase("NA"))
             for (int i = 0; i < spProjectList.size(); i++) {
                 if (spProjectList.get(i).equalsIgnoreCase(projectName)) {
                     mSpinnerProject.setSelection(i);
                     defaultPos = i;
                     break;
                 }
             }
         return null;
     }
 */
    private List<Projects> getSelectedProject(List<Projects> selProjList) {
        LinkedList<Projects> selProList = new LinkedList<>();
        int size = selProjList.size();
        if (size > 0) {
            selectedProjList.clear();
            for (int i = 0; i < size; i++) {
                String projectName = selProjList.get(i).getProject_name();
                if (!TextUtils.isEmpty(projectName) && !projectName.equalsIgnoreCase("null")) {
                    Projects model = new Projects(selProjList.get(i).getProject_id(), projectName);
                    selProList.add(model);
                }
            }
            set = new HashSet<>(selProList);
            selectedProjList.addAll(set);
        }
        return selectedProjList;
    }

    private String getSelectedLeadStatus(ArrayList<LeadStatus> leadStatusList, String
            leadStatus) {
        if (TextUtils.isEmpty(leadStatus) || leadStatus.equalsIgnoreCase("")
                || leadStatus.equalsIgnoreCase("NA")) {
            mSpinnerStatus.setSelection(0);
            return null;
        }

        for (int i = 0; i < leadStatusList.size(); i++) {
            if (leadStatusList.get(i).getTitle().equalsIgnoreCase(mSpModel.getStatus())) {
                mSpinnerStatus.setSelection(i);
                defaultPosLeadStatus = i;
                break;
            }
        }
        if (spLeadStatusList.size() > 0) {
            spLeadStatusList.clear();
            for (int i = 0; i < leadStatusList.size(); i++) {
                spLeadStatusList.add(leadStatusList.get(i).getTitle());
            }
        }
        leadStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLeadStatusList);
        mSpinnerStatus.setAdapter(leadStatusAdapter);
        mSpinnerStatus.setSelection(defaultPosLeadStatus);
        return null;
    }


   /* private void getSpLeadsDetails(final String enquiryId) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_SP_LEADS_DETAILS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    parseJson(jsonObject);
                                } else {
                                    Utils.showToast(SpDetailsActivity.this, jsonObject.optString("messaage"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SpDetailsActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
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
                params.put(ParamsConstants.ENQUIRY_ID, enquiryId);
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void parseJson(JSONObject jsonObject) throws JSONException {

        JSONObject object = jsonObject.optJSONObject("data");
        String custMob = object.optString("Mobile_No");
        String custAlternateMob = object.optString("Alternate_Mobile_No");
        String enquiryId = object.optString("Enquiry_ID");
        String custName = object.optString("Name");
        String custEmail = object.optString("Email_ID");
        String campaignName = object.optString("Campaign");
        String campaignDate = object.optString("Campaign_Date");
        String budget = object.optString("Budget");
        String currentStatus = object.optString("Current_Status");
        String dateAndTime = object.optString("Date_And_Time");
        String date = object.optString("date");
        String time = object.optString("time");
        String address = object.optString("Address");
        String projectName = object.optString("Project_Name");
        String remark = object.optString("remark");
        String leadType = object.optString("lead_type");
        String budget_min = object.optString("budget_min");
        String budget_max = object.optString("budget_max");
        int noOfPersons = object.optInt("no_of_persons");

        ArrayList<Assign_To> assignList = new ArrayList<>();
        JSONArray projectArray = object.optJSONArray("Project_List");
        if (projectArray != null) {
            int projectLength = projectArray.length();
            for (int j = 0; j < projectLength; j++) {
                JSONObject obj = projectArray.optJSONObject(j);
                mProjectsList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
            }
        }
        JSONArray selectedList = object.optJSONArray("selected_project");
        if (selectedList != null) {
            int selectedLength = selectedList.length();
            for (int j = 0; j < selectedLength; j++) {
                JSONObject obj = selectedList.optJSONObject(j);
                selectedProjList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
            }
        }
        JSONArray array = object.optJSONArray("Assign_To");
        if (array != null) {
            int assignLength = array.length();
            for (int j = 0; j < assignLength; j++) {
                JSONObject obj = array.optJSONObject(j);
                assignList.add(new Assign_To(obj.optString("salesperson_id"), obj.optString("salesperson_name")));
            }
        }
        JSONArray arrayLead = object.optJSONArray("lead_status");
        if (arrayLead != null) {
            JSONObject objLeads = null;
            int leadLength = arrayLead.length();
            for (int j = 0; j < leadLength; j++) {
                objLeads = arrayLead.optJSONObject(j);
                leadsList.add(new LeadStatus(objLeads.optString("disposition_id"), objLeads.optString("title")));
            }
        }

        mDetailsModel = new Details(enquiryId, campaignName, campaignDate, custName, custEmail, custMob, custAlternateMob,
                budget, currentStatus, dateAndTime, date, time, address, projectName, mProjectsList, selectedProjList,
                assignList, leadsList, remark, leadType, budget_min, budget_max, noOfPersons);

        setLeadDetails(mDetailsModel);
    }*/

    @Override
    public void onBackPressed() {
        navigateToPrevious();
        super.onBackPressed();
    }

    private void navigateToPrevious() {
        app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
        if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
            Intent intent = new Intent(SpDetailsActivity.this, PreSalesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing
                navigateToPrevious();
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {

            case R.id.tv_mobile_no_val:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermissions()) {
                        actionCall(mSpModel.getCustomer_mobile());
                    } else {
                        requestPermissions();
                    }
                } else {
                    actionCall(mSpModel.getCustomer_mobile());
                }
                break;
            case R.id.img_send_email:
                if (TextUtils.isEmpty(mSpModel.getCustomer_email())) {
                    Utils.showToast(this, "Email id not available");
                } else {
                    String email = mSpModel.getCustomer_email();
                    Utils.openMailClient(this, "Sales Person App Email", new String[]{email}, "");
                }
                break;
            case R.id.tv_date_val:

                int mYear = 0, mMonth = 0, mDay = 0;
                String dateString = tv_date_val.getText().toString();
                if (!TextUtils.isEmpty(dateString)) {
                    try {
                        Calendar cal = Utils.getDateFromTextView(formatter, dateString);
                        mYear = cal.get(Calendar.YEAR);
                        mMonth = cal.get(Calendar.MONTH);
                        mDay = cal.get(Calendar.DAY_OF_MONTH);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    mYear = currentCal.get(Calendar.YEAR);
                    mMonth = currentCal.get(Calendar.MONTH);
                    mDay = currentCal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog fromDateDialog = new DatePickerDialog(SpDetailsActivity.this, dateChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_time_val:
                if (TextUtils.isEmpty(tv_date_val.getText().toString())) {
                    Toast.makeText(SpDetailsActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                    return;
                }
                int mHour = 0, mMinute = 0;
                String timeString = tv_time_val.getText().toString().trim();
                if (!TextUtils.isEmpty(timeString)) {
                    String[] timeArray = timeString.split(":");
                    mHour = Integer.valueOf(timeArray[0]);
                    mMinute = Integer.valueOf(timeArray[1]);
                } else {
                    mHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    mMinute = currentCal.get(Calendar.MINUTE);
                }

                TimePickerDialog mTimePicker = new TimePickerDialog(SpDetailsActivity.this, onTimeSetListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time ");
                mTimePicker.show();

                break;
            case R.id.button_update:

                if (TextUtils.isEmpty(tv_customer_name_val.getText().toString())) {
                    Toast.makeText(this, R.string.toast_msg_enter_customer_name, Toast.LENGTH_LONG).show();
                    return;
                }
                /*if (!Utils.isValidUserName(tv_customer_name_val.getText().toString())) {
                    Toast.makeText(this, R.string.enter_valid_user, Toast.LENGTH_LONG).show();
                    return;
                }*/
                if (!Utils.isEmailValid(tv_email_id_val.getText().toString())) {
                    Toast.makeText(this, R.string.enter_valid_email, Toast.LENGTH_LONG).show();
                    return;
                }
                // REMARK IS MANDATORY IN CLOSURE
                if (mSelectedLeadStatus.equalsIgnoreCase(getString(R.string.text_closure))) {
                    if (TextUtils.isEmpty(edtRemark.getText().toString())) {
                        Toast.makeText(this, R.string.toast_msg_remark_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                // PROJECT IS NOT MANDATORY IN CALLBACK
                if (!mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {
                    if (TextUtils.isEmpty(mSelectedProjectId)) {
                        Toast.makeText(this, R.string.spinner_select_project, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {
                    if (TextUtils.isEmpty(mDateText)) {
                        Toast.makeText(this, R.string.toast_msg_date_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mTimeText)) {
                        Toast.makeText(this, R.string.toast_msg_time_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mSelectedRadioButton)) {
                        Toast.makeText(this, R.string.toast_msg_lead_type_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(edtRemark.getText().toString())) {
                        Toast.makeText(this, R.string.toast_msg_remark_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
              /*  if (TextUtils.isEmpty(mSelectedLeadStatusId)) {
                    Toast.makeText(this, R.string.spinner_select_status, Toast.LENGTH_LONG).show();
                    return;
                }*/
                // MANDATORY VALIDATION IN MEETING AND SITE VISIT
                if ((mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4]))
                        || (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5]))) {
                    // MEETING
                    if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {
                       /* if (TextUtils.isEmpty(mSelectedLocation)) {
                            Toast.makeText(this, R.string.toast_msg_select_location, Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {*/
                        if (mPersonCount < 1) {
                            Toast.makeText(this, R.string.toast_msg_person_mandatory, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if (TextUtils.isEmpty(mDateText)) {
                        Toast.makeText(this, R.string.toast_msg_date_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mTimeText)) {
                        Toast.makeText(this, R.string.toast_msg_time_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(tv_address_val.getText().toString())) {
                        String text = getString(R.string.txt_typed_address, mSelectedLeadStatus);
                        Toast.makeText(this, getString(R.string.toast_msg_address_mandatory, text), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mSelectedRadioButton)) {
                        Toast.makeText(this, R.string.toast_msg_lead_type_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(edtRemark.getText().toString())) {
                        Toast.makeText(this, R.string.toast_msg_remark_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

               /* if (mSelectedLeadStatus.equalsIgnoreCase(String.valueOf(leadStatusModel.getTitle()))
                        && mSelectedProjectName.equalsIgnoreCase(mSpModel.getProject_name())) {
                    Toast.makeText(this, R.string.toast_msg_project_lead_status_mandatory, Toast.LENGTH_LONG).show();
                    return;
                }*/
                updateLeadStatus();
                break;
        }
    }

    private void setSelectedTime(int hourOfDay, int minute, TextView textView) {
        currentCal = Calendar.getInstance();
        selectedCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedCal.set(Calendar.MINUTE, minute);
        //  selectedDate = selectedCal.getTime();

        if (selectedCal.getTimeInMillis() > currentCal.getTimeInMillis()) {
            //it's after current
            int hour = hourOfDay % 12;
            String timeText = String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                    minute, hourOfDay < 12 ? "AM" : "PM");
            try {
                mTimeText = Utils.convertStringTo24Format(timeText);
                textView.setText(mTimeText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            //it's before current'
            Toast.makeText(SpDetailsActivity.this, getString(R.string.invalid_time_picker_error), Toast.LENGTH_SHORT).show();
        }
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentDate = currentCal.getTime();
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            setSelectedTime(hourOfDay, minute, tv_time_val);
        }
    };

    DatePickerDialog.OnDateSetListener dateChangeListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            selectedCal.set(Calendar.YEAR, year);
            selectedCal.set(Calendar.MONTH, month);
            selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            currentDate = currentCal.getTime();
            selectedDate = selectedCal.getTime();
            //  if (selectedCal.getTimeInMillis() >= currentCal.getTimeInMillis()) {
            CharSequence charSequence = simpleDateFormat.format(selectedDate);
            tv_date_val.setText(charSequence);
            mDateText = charSequence.toString();
            if (selectedCal.getTimeInMillis() == currentCal.getTimeInMillis()) {
                tv_time_val.setText("");
                mTimeText = "";
            }
        }
    };


    private void updateLeadStatus() {
        if (Connectivity.isConnected(SpDetailsActivity.this)) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPDATE_ENQUIRY_STATUS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
                                    app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                                    Toast.makeText(SpDetailsActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                                if (!jsonObject.getBoolean("success")) {
                                    Toast.makeText(SpDetailsActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SpDetailsActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!Connectivity.isConnected(SpDetailsActivity.this)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        if (!Connectivity.isConnected(SpDetailsActivity.this)) {
                            app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
                            app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                            onBackPressed();
                        }
                        Log.d(TAG, "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                StringBuilder flagBuilder = new StringBuilder();
                params.put(ParamsConstants.USER_DESIGNATION, app.getFromPrefs((BMHConstants.USER_DESIGNATION)));
                params.put(ParamsConstants.CUSTOMER_NAME, tv_customer_name_val.getText().toString());
                params.put(ParamsConstants.CUSTOMER_EMAIL, tv_email_id_val.getText().toString());
                params.put(ParamsConstants.ALTERNATE_MOBILE_NO, tv_alternate_mobile_no_val.getText().toString());
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.ENQUIRY_ID, enquiryId);
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put("lead_type", mSelectedRadioButton);
                params.put("remark", edtRemark.getText().toString());
                params.put("lead_status", mSelectedLeadStatus);
                params.put("status_id", mSelectedLeadStatusId);
                params.put("budget_min", mMinBudget);
                params.put("budget_max", mMaxBudget);
                params.put("date", mDateText);
                params.put("time", mTimeText);
                params.put("project_name", mSelectedProjectName);
                params.put("project_id", mSelectedProjectId);
                params.put("location", mSelectedLocation);
                params.put("address", tv_address_val.getText().toString());
                params.put("no_of_persons", String.valueOf(mPersonCount));
                params.put("recordingFilePath", mRecFilePath);

                int isSynced;
                if (Connectivity.isConnected(SpDetailsActivity.this))
                    isSynced = 0;
                else
                    isSynced = 1;

                if (mMinBudget.equalsIgnoreCase(getString(R.string.txt_min_budget)))
                    mMinBudget = "0";
                if (mMaxBudget.equalsIgnoreCase(getString(R.string.txt_max_budget)))
                    mMaxBudget = "0";
                if (mSelectedLeadStatus.equalsIgnoreCase(getString(R.string.spinner_select_status)))
                    mSelectedLeadStatus = "";
                if ((Utils.isChanged(mDetailsModel.getName(), tv_customer_name_val.getText()))

                        || Utils.isChanged(mDetailsModel.getEmail_ID(), tv_email_id_val.getText())

                        || (Utils.isChanged(mDetailsModel.getAlternate_Mobile_No(), tv_alternate_mobile_no_val.getText()))

                        || Utils.isBudgetChanged(mDetailsModel.getBudget_min(), mMinBudget)

                        || Utils.isBudgetChanged(mDetailsModel.getBudget_max(), mMaxBudget)) {

                    flagBuilder.append(101);
                }
                /*
                if (!TextUtils.isEmpty(mSpModel.getSalesperson_name()) && !mSpModel.getSalesperson_name().equalsIgnoreCase(mSelectedAssignedTo)) {
                    if (TextUtils.isEmpty(flagBuilder.toString())) {
                        flagBuilder.append(102);
                    } else {
                        flagBuilder.append(",").append(102);
                    }
                }*/

                String projectStr = Utils.getSelectedMultiProjectIds(mDetailsModel.getSelectedProjList());
                List<String> proList = Utils.getMultiSelectedProject(projectStr);
                List<String> selProList = Utils.getMultiSelectedProject(mSelectedProjectId);

                if (selProList != null && proList != null) {
                    // Sort and compare the two lists
                    Collections.sort(proList);
                    Collections.sort(selProList);
                    if (!proList.equals(selProList)) {
                        if (TextUtils.isEmpty(flagBuilder.toString()))
                            flagBuilder.append(103);
                        else
                            flagBuilder.append(",").append(103);
                    }
                }
                if ((Utils.isChanged(mSpModel.getStatus(), mSpinnerStatus.getSelectedItem().toString())) ||
                        (Utils.isChanged(mDetailsModel.getAddress(), tv_address_val.getText())) ||
                        (Utils.isChanged(mDetailsModel.getDate(), mDateText)) ||
                        (Utils.isChanged(mDetailsModel.getTime(), mTimeText)) ||
                        (Utils.isChanged(mDetailsModel.getLead_type(), mSelectedRadioButton)) ||
                        (Utils.isChanged(mDetailsModel.getRemark(), edtRemark.getText()))) {

                    if (TextUtils.isEmpty(flagBuilder.toString())) {
                        flagBuilder.append(104);
                    } else
                        flagBuilder.append(",").append(104);
                }
                if (TextUtils.isEmpty(flagBuilder)) {
                    if (!Connectivity.isConnected(SpDetailsActivity.this)) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SpDetailsActivity.this, R.string.txt_msg_lead_not_updated, Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (BMHApplication.getInstance().getRequestQueue() != null) {
                        BMHApplication.getInstance().getRequestQueue().cancelAll(getString(R.string.tag_update_lead));
                    }
                }
                params.put("update_flag", flagBuilder.toString());

                if (!TextUtils.isEmpty(mRecFilePath)) {
                    Uri uri = Uri.parse(mRecFilePath);
                    new ChangeRecordingStatusTask(uri.getLastPathSegment()).execute();
                }

                if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {
                    CallbackEntity callbackEntity = new CallbackEntity(enquiryId, "",
                            "", mDateText, mTimeText, "",
                            edtRemark.getText().toString(), mSelectedRadioButton, isSynced, mRecFilePath,
                            Utils.getCurrentDateTime());
                    new InsertCallbackTask(callbackEntity).execute();
                }
                if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {
                    MeetingEntity meetingEntity = new MeetingEntity(Calendar.getInstance().getTimeInMillis(), enquiryId, mSelectedLeadStatus, mTimeText,
                            mDateText, tv_address_val.getText().toString(), mSelectedRadioButton, edtRemark.getText().toString(),
                            Utils.getCurrentDateTime(), Utils.getCurrentDateTime(), isSynced, mRecFilePath,
                            Utils.getCurrentDateTime());
                    new InsertMeetingTask(meetingEntity).execute();
                }
                if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5])) {
                    SiteVisitEntity siteVisitEntity = new SiteVisitEntity(enquiryId, mSelectedLeadStatus, tv_address_val.getText().toString(),
                            mDateText, mTimeText, tv_address_val.getText().toString(), mSelectedRadioButton,
                            String.valueOf(mPersonCount), edtRemark.getText().toString(), "",
                            "", Utils.getCurrentDateTime(), "", "",
                            isSynced, mRecFilePath, Utils.getCurrentDateTime());
                    new InsertSiteVisitTask(siteVisitEntity).execute();
                }

                if (!TextUtils.isEmpty(mSelectedProjectName)) {
                    SelectMultipleProjectsEntity multiSelectEntity =
                            new SelectMultipleProjectsEntity(enquiryId, mSelectedProjectId, mSelectedProjectName,
                                    isSynced, mRecFilePath, Utils.getCurrentDateTime());
                    new InsertMultiSelectTask(multiSelectEntity, 1).execute();
                }

                PreSalesLeadDetailsEntity preSalesLeadDetailsEntity = new PreSalesLeadDetailsEntity(enquiryId, tv_customer_name_val.getText().toString(),
                        tv_mobile_no_val.getText().toString(), tv_email_id_val.getText().toString(), tv_alternate_mobile_no_val.getText().toString(),
                        Integer.valueOf(mSelectedLeadStatusId), mSelectedLeadStatus, Utils.getCurrentDateTime(),
                        mSelectedProjectId, edtRemark.getText().toString(), strBudgetRange, mDateText, mTimeText, mMinBudget, mMaxBudget,
                        "", "", "", 1, tv_campaign_val.getText().toString(),
                        tv_campaign_date_val.getText().toString(), 0,
                        updateFromAlarmNotification, isSynced, Utils.getCurrentDateTime(), getString(R.string.txt_asm), mRecFilePath);
                new InsertPreSalesLeadDetailsTask(SpDetailsActivity.this, preSalesLeadDetailsEntity, true).execute();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                            AlarmJobServices services = new AlarmJobServices();
                            services.stopRepeatingAlarm(alarmIndex, SpDetailsActivity.this);
                        }
                    }
                });
                startAlarmService();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void startAlarmService() {
        new GetPreSalesLeadDetailsTask(this).execute();
    }

    @Override
    public void getPreSalesLeadsDetail(List<PreSalesLeadDetailsEntity> detailsEntityList) {
        ArrayList<NotificationDataModel> notificationDataModelList = new ArrayList<>();
        String alertDate = "";
        String alertTime = "";
        String alertStatus = "";
        String enquiryId = "";
        String customerName = "";
        String mobNumber = "";
        int isUpdated;
        Calendar cal = Calendar.getInstance();
        if (detailsEntityList != null) {
            for (int i = 0; i < detailsEntityList.size(); i++) {
                alertTime = detailsEntityList.get(i).getTime();
                alertDate = detailsEntityList.get(i).getDate();
                alertStatus = detailsEntityList.get(i).getCurrentStatus();
                enquiryId = detailsEntityList.get(i).getEnquiryId();
                customerName = detailsEntityList.get(i).getCustomerName();
                mobNumber = detailsEntityList.get(i).getCustomerMobile();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_select_project:
                if (isInitialProject) {
                    position = defaultPos;
                    isInitialProject = false;
                }
                mSelectedProjectName = parent.getItemAtPosition(position).toString();
                getProjectId(mSelectedProjectName);
                mSpinnerProject.setSelection(position);
                break;
            case R.id.spinner_select_status:
                if (isInitialLeadStatus) {
                    position = defaultPosLeadStatus;
                    isInitialLeadStatus = false;
                }
                mSelectedLeadStatus = parent.getItemAtPosition(position).toString();
                getStatusId(mSelectedLeadStatus);
                mSpinnerStatus.setSelection(position);
                if (!mSelectedLeadStatus.equalsIgnoreCase(mDetailsModel.getCurrent_Status())) {
                    tv_date_val.setText("");
                    tv_time_val.setText("");
                    edtRemark.setText("");
                    tv_address_val.setText("");
                    radioGroup.clearCheck();
                } else {
                    tv_date_val.setText(mDetailsModel.getDate());
                    tv_time_val.setText(mDetailsModel.getTime());
                    edtRemark.setText(mDetailsModel.getRemark());
                    tv_address_val.setText(mDetailsModel.getAddress());

                    if (!TextUtils.isEmpty(mDetailsModel.getLead_type()) || !mDetailsModel.getLead_type().equals("")) {
                        radioButton = selectLeadTypeStatus(mDetailsModel.getLead_type());
                        radioButton.setChecked(true);
                        mSelectedRadioButton = radioButton.getText().toString();
                    }
                }
                inflateDynamicView(mSelectedLeadStatus);

                break;
            case R.id.spinner_select_location:
              /*  if (isInitialLocation) {
                    isInitialLocation = false;
                }
                if (tv_select_location.getText().toString().equalsIgnoreCase(getString(R.string.txt_select_person))) {
                    mPersonCount = (Integer) mSpinnerLocation.getSelectedItem();
                }*/
                if (isInitialPersonCount) {
                    position = defaultPosPersonCount;
                    isInitialPersonCount = false;
                    if (mDetailsModel.getNoOfPersons() > 0) {
                        mPersonCount = mDetailsModel.getNoOfPersons();
                        //  mLocationSpinner.setSelection(mPersonCount);
                        position = mPersonCount - 1;
                    }
                }
                mSelectedPersonCount = parent.getItemAtPosition(position).toString();
                mSpinnerLocation.setSelection(position);
                mPersonCount = Integer.valueOf(mSelectedPersonCount);
                break;
            case R.id.spinner_min_budget:
                if (isInitialMinBudget) {
                    position = defaultPosMinBudget;
                    isInitialMinBudget = false;
                }
                Object objMin = parent.getItemAtPosition(position).toString();
                if (objMin.equals(getString(R.string.txt_min_budget)))
                    mMinBudget = objMin.toString();
                else
                    mMinBudget = String.valueOf(Utils.convertStringToMoney(objMin.toString()));
                mSpinnerMinBudget.setSelection(position);
                break;

            case R.id.spinner_max_budget:
                if (isInitialMaxBudget) {
                    position = defaultPosMaxBudget;
                    isInitialMaxBudget = false;
                }
                Object objMax = parent.getItemAtPosition(position).toString();
                if (objMax.equals(getString(R.string.txt_max_budget)))
                    mMaxBudget = objMax.toString();
                else
                    mMaxBudget = String.valueOf(Utils.convertStringToMoney(objMax.toString()));
                mSpinnerMaxBudget.setSelection(position);
                break;
            default:


                break;
        }
    }

    private RadioButton selectLeadTypeStatus(String lead_type) {
        RadioButton button;
        if (lead_type.equalsIgnoreCase(getString(R.string.txt_hot))) {
            button = findViewById(R.id.radioButton_hot);
            return button;
        }
        if (lead_type.equalsIgnoreCase(getString(R.string.txt_warm))) {
            button = findViewById(R.id.radioButton_warm);
            return button;
        }
        if (lead_type.equalsIgnoreCase(getString(R.string.txt_cold))) {
            button = findViewById(R.id.radioButton_cold);
            return button;
        }
        return null;
    }

    private void inflateDynamicView(String lead) {
        // radioGroup.clearCheck();
        layout_date.setVisibility(View.GONE);
        layout_time.setVisibility(View.GONE);
        layout_location.setVisibility(View.GONE);
        layout_address.setVisibility(View.GONE);
        layout_lead_type.setVisibility(View.GONE);
        edtRemark.setVisibility(View.GONE);

        layout_date_divider.setVisibility(View.GONE);
        layout_time_divider.setVisibility(View.GONE);
        layout_location_divider.setVisibility(View.GONE);
        layout_address_divider.setVisibility(View.GONE);
        layout_lead_type_divider.setVisibility(View.GONE);
        /*
          Not Interested
         */

        if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[0])) {
            edtRemark.setVisibility(View.VISIBLE);
            mRemarkText = edtRemark.getText().toString();
            return;
        }
        /*
           MARKETING CALL
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[1])) {
            edtRemark.setVisibility(View.VISIBLE);
            mRemarkText = edtRemark.getText().toString();
            return;
        }
        /*
           JUST ENQUIRY
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[2])) {
            edtRemark.setVisibility(View.VISIBLE);
            layout_lead_type_divider.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            mRemarkText = edtRemark.getText().toString();
            /* mSelectedRadioButton */
            return;
        }
        /*
           CALLBACK
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {

            layout_date_divider.setVisibility(View.VISIBLE);
            layout_time_divider.setVisibility(View.VISIBLE);
            layout_lead_type_divider.setVisibility(View.VISIBLE);
            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));
            mDateText = tv_date_val.getText().toString();
            tv_time.setText(getString(R.string.txt_typed_time, lead));
            mTimeText = tv_time_val.getText().toString();
            mRemarkText = edtRemark.getText().toString();
            /* mSelectedRadioButton */
            return;
        }
        /*
           MEETING SCHEDULED
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {

            layout_date_divider.setVisibility(View.VISIBLE);
            layout_time_divider.setVisibility(View.VISIBLE);
            layout_location_divider.setVisibility(View.GONE);
            layout_address_divider.setVisibility(View.VISIBLE);
            layout_lead_type_divider.setVisibility(View.VISIBLE);

            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_location.setVisibility(View.GONE);
            layout_address.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));
            mDateText = tv_date_val.getText().toString();
            tv_time.setText(getString(R.string.txt_typed_time, lead));
            mTimeText = tv_time_val.getText().toString();
            /*
            mSelectedProject
            mLocationName
            */
            tv_select_location.setText(getString(R.string.txt_select_meeting_location));
            tv_address.setText(getString(R.string.txt_typed_address, lead));
            tv_address_val.getText().toString();
            /* mSelectedRadioButton */
            mRemarkText = edtRemark.getText().toString();
            return;
        }
        /*
           SITE VISIT SCHEDULED
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5])) { // position 4

            layout_date_divider.setVisibility(View.VISIBLE);
            layout_time_divider.setVisibility(View.VISIBLE);
            layout_location_divider.setVisibility(View.VISIBLE);
            layout_address_divider.setVisibility(View.VISIBLE);
            layout_lead_type_divider.setVisibility(View.VISIBLE);

            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_location.setVisibility(View.VISIBLE);
            layout_address.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));
            mDateText = tv_date_val.getText().toString();
            tv_time.setText(getString(R.string.txt_typed_time, lead));
            mTimeText = tv_time_val.getText().toString();
            /* mSelectedProject */
            tv_select_location.setText(getString(R.string.txt_select_person));
            mSpinnerLocation.setPadding(100, 0, 50, 0);
            switchSpinnerList();
            tv_address.setText(getString(R.string.txt_pickup_address));
            tv_address_val.getText().toString();
            mRemarkText = edtRemark.getText().toString();
        }
    }

    /*
     * LOCATIONS
     */
    private void switchSpinnerList() {

        ArrayList<Integer> personArray = new ArrayList<>();
        personArray.add(1);
        personArray.add(2);
        personArray.add(3);
        personArray.add(4);
        personArray.add(5);
        personArray.add(6);
        if (mDetailsModel.getNoOfPersons() > 0) {
            //  personArray.add(0, mDetailsModel.getNoOfPersons());
            mPersonCount = mDetailsModel.getNoOfPersons();
        }
        locationAdapter = new ArrayAdapter(SpDetailsActivity.this, R.layout.textview_spinner, personArray);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLocation.setAdapter(locationAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getProjectId(String selectedSp) {
        for (int i = 0; i < mProjectsList.size(); i++) {
            Projects model = mProjectsList.get(i);
            if (selectedSp.equalsIgnoreCase(model.getProject_name())) {
                mSelectedProjectId = model.getProject_id();
            }
        }
    }

    private void getStatusId(String statusName) {
        if (TextUtils.isEmpty(statusName))
            return;
        for (int i = 0; i < mLeadStatusList.size(); i++) {
            LeadStatus model = mLeadStatusList.get(i);
            if (statusName.equalsIgnoreCase(model.getTitle())) {
                mSelectedLeadStatusId = model.getDisposition_id();
            }
        }
    }

  /*  private String getLocListValue(String locListKey) {
        for (int i = 0; i < mLocListKey.size(); i++) {
            if (locListKey.equalsIgnoreCase(mLocListKey.get(i))) {
                tv_address_val.setText("");
                tv_address_val.setText(mLocListValue.get(i));
                return mSelectedLocation = mLocListValue.get(i);
            }
        }
        return null;
    }
*/

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedId;
        switch (checkedId) {
            case R.id.radioButton_hot:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot = findViewById(selectedId);
                mSelectedRadioButton = radioButton_hot.getText().toString();
                break;
            case R.id.radioButton_warm:
                selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton_warm = findViewById(selectedId);
                mSelectedRadioButton = radioButton_warm.getText().toString();
                break;
            case R.id.radioButton_cold:
                selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton_cold = findViewById(selectedId);
                mSelectedRadioButton = radioButton_cold.getText().toString();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isMultiSpinner != true) {
            alertDialog = new AlertDialog.Builder(SpDetailsActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_recyclerview, null);
            alertDialog.setView(convertView);
            //    alertDialog.setTitle(getString(R.string.select_project));
            // Initialize a new foreground color span instance
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
            // Initialize a new spannable string builder instance
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(getString(R.string.select_project));
            // Apply the text color span
            ssBuilder.setSpan(
                    foregroundColorSpan,
                    0,
                    getString(R.string.select_project).length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            // Set the alert dialog title using spannable string builder
            alertDialog.setTitle(ssBuilder);
            /*   RecyclerView recyclerView = convertView.findViewById(R.id.recyclerView);
             *//*if (selectedProjList.size() > 0) {
                mProjectsList = new ArrayList<>(selectedProjList);
                for (Projects strProject : mProjectsList) {
                    mProjectsList.add(strProject);
                }
            }*//*
            set = new HashSet<>(selectedProjList);
            mAdapter = new MultiSelectAdapter(SpDetailsActivity.this, mProjectsList, set);
            selectedProjList.clear();
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(SpDetailsActivity.this, LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SpDetailsActivity.this);
            recyclerView.setLayoutManager(layoutManager);*/


            if (!TextUtils.isEmpty(mSelectedProjectName)) {

                int projectListSize = mProjectsList.size();
                if (!mSelectedProjectName.equalsIgnoreCase(getString(R.string.spinner_select_project)) && projectListSize > 0) {
                    for (int i = 0; i < projectListSize; i++) {
                        if (mSelectedProjectName.contains(mProjectsList.get(i).getProject_name()))
                            selectedProjList.add(mProjectsList.get(i));
                    }
                    if (selectedProjList.size() > 0) {
                        set = new HashSet<>(selectedProjList);
                        setMultiSelectedAdapter(convertView, set, mProjectsList);
                    }
                } else {
                    try {
                        new GetMultipleProjectTask(this, enquiryId).execute();
                        //  Thread.sleep(2000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    new GetMultipleProjectTask(this, enquiryId).execute();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            alertDialog.setCancelable(false)

                    .setPositiveButton(getString(R.string.txt_button_done), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            isMultiSpinner = false;
                            textView_projects.setText("");
                            StringBuilder nameBuilder = new StringBuilder();
                            LinkedList<Projects> checkProjList = new LinkedList<>();
                            checkProjList.addAll(selectedProjList);
                            for (int i = 0; i < checkProjList.size(); i++) {
                                SelectableProject item = (SelectableProject) checkProjList.get(i);
                                if (!item.isSelected()) {
                                    selectedProjList.remove(checkProjList.get(i));
                                }
                            }

                            //populate set
                            updateMultiSelectProject();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    isMultiSpinner = false;

                                    LinkedList<Projects> checkProjList = new LinkedList<>();
                                    checkProjList.addAll(selectedProjList);
                                    for (int i = 0; i < checkProjList.size(); i++) {
                                        SelectableProject item = (SelectableProject) checkProjList.get(i);
                                        if (!mSelectedProjectName.contains(item.getProject_name()) && item.isSelected()) {
                                            selectedProjList.remove(checkProjList.get(i));
                                        }
                                    }

                                    updateMultiSelectProject();
                                    dialog.cancel();
                                }
                            });
            isMultiSpinner = true;
            AlertDialog dialog = alertDialog.create();
            dialog.show();
            // Change the alert dialog background color
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            // Get the alert dialog buttons reference
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            // Change the alert dialog buttons text and background color
            positiveButton.setTextColor(Color.parseColor("#1793f2"));
            negativeButton.setTextColor(Color.parseColor("#17a3f2"));
        }
        return true;
    }

    private void setMultiSelectedAdapter(View convertView, Set<Projects> set, List<Projects> mProjectsList) {
        if (convertView != null) {
            RecyclerView recyclerView = convertView.findViewById(R.id.proMulti_recyclerView);
            mAdapter = new MultiSelectAdapter(SpDetailsActivity.this, mProjectsList, set);
            selectedProjList.clear();
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(SpDetailsActivity.this, LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SpDetailsActivity.this);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void onProjectSelected(SelectableProject item) {
        // List<Projects> selectedItems = mAdapter.getSelectedItems();
        if (item.isSelected() && !selectedProjList.contains(item)) {
            selectedProjList.add(item);
        } else {
            //   selectedProjList.remove(item);
            item.setSelected(false);
        }
    }

    private void updateMultiSelectProject() {
        textView_projects.setText("");
        if (selectedProjList.size() > 0) {
            StringBuilder nameBuilder = new StringBuilder();
            set = new HashSet<>(selectedProjList);

            for (Projects project : set) {
                nameBuilder.append(project.getProject_name() + ", ");
            }
            String selectedProj = StringUtil.removeLastComma(nameBuilder.toString());
            textView_projects.setText(selectedProj);
            mSelectedProjectName = selectedProj;
            mSelectedProjectId = Utils.getSelectedMultiProjectIds(selectedProjList);
        } else {
            mSelectedProjectName = "";
            mSelectedProjectId = "";
            textView_projects.setText(getString(R.string.select_project));
        }
    }

    @Override
    public void getMultiProjCallback(List<SelectMultipleProjectsEntity> multipleProjectsEntityList, List<ProjectMasterEntity> projectMasterList) {

        selectedProjList.clear();
        mProjectsList.clear();
        List<Projects> mSeleMulti = new ArrayList<>();

        for (int i = 0; i < multipleProjectsEntityList.size(); i++) {
            selectedProjList.add(new Projects(multipleProjectsEntityList.get(i).getProjectId(), multipleProjectsEntityList.get(i).getProjectName()));
        }
        for (int i = 0; i < projectMasterList.size(); i++) {
            mProjectsList.add(new Projects(projectMasterList.get(i).getProjectId(), projectMasterList.get(i).getProjectName()));
        }

        for (int i = 0; i < selectedProjList.size(); i++) {
            String projectName = selectedProjList.get(i).getProject_name();
            if (!TextUtils.isEmpty(projectName) && !projectName.equalsIgnoreCase("null")) {
                Projects model = new Projects(selectedProjList.get(i).getProject_id(), projectName);
                mSeleMulti.add(model);
            }
        }
        set = new HashSet<>(mSeleMulti);
        setMultiSelectedAdapter(this.convertView, set, mProjectsList);
    }

    @Override
    public void getStatusMasterCallback(List<StatusMasterEntity> statusMasterList) {
        mLeadStatusList.clear();
        for (int i = 0; i < statusMasterList.size(); i++) {
            LeadStatus statusModel = new LeadStatus(statusMasterList.get(i).getStatusId(), statusMasterList.get(i).getStatusName());
            mLeadStatusList.add(statusModel);
        }
        getSelectedLeadStatus(mLeadStatusList, mSpModel.getStatus());
    }
}
