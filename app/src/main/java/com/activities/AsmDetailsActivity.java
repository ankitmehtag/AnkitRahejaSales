package com.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.database.entity.CustomerInfoEntity;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.SpMasterEntity;
import com.database.task.ChangeRecordingStatusTask;
import com.database.task.CustomerInfoTask;
import com.database.task.GetPreSalesLeadDetailsTask;
import com.database.task.GetPreSalesLeadDetailTask;
import com.database.task.GetMultipleProjectTask;
import com.database.task.GetSpMasterTask;
import com.database.task.GetStatusMasterTask;
import com.database.task.InsertCallbackTask;
import com.database.task.InsertPreSalesLeadDetailsTask;
import com.database.task.InsertMeetingTask;
import com.database.task.InsertMultiSelectTask;
import com.database.task.InsertSiteVisitTask;
import com.database.entity.CallbackEntity;
import com.database.entity.MeetingEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SiteVisitEntity;
import com.database.entity.StatusMasterEntity;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.model.Assign_To;
import com.model.Details;
import com.model.LeadStatus;
import com.model.NotificationDataModel;
import com.model.PreSalesAsmModel;
import com.model.Projects;
import com.model.SelectableProject;
import com.services.AlarmJobServices;
import com.sp.BMHApplication;
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

           public class AsmDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
                       View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnTouchListener, GetSpMasterTask.ISpMasterCommunicator,
                    GetMultipleProjectTask.IMultiProjCommunicator, GetStatusMasterTask.IStatusMasterCommunicator,
                       MultiSelectAdapter.OnItemSelectedListener, GetPreSalesLeadDetailTask.ILeadDetailCommunicator, GetPreSalesLeadDetailsTask.IPreSalesLeadDetailsCommunicator {

    private static final String TAG = AsmDetailsActivity.class.getSimpleName();
    private PreSalesAsmModel mAsmModel;
    private Details mDetailsModel;
    private Projects projectsModel;
    private Assign_To assignToModel;
    private LeadStatus leadStatusModel;
    private CustomSpinnerAdapter assignToAdapter, leadStatusAdapter;
    private Spinner mProjectsSpinner, mAssignToSpinner, mLeadStatusSpinner, mLocationSpinner, mSpinnerMinBudget, mSpinnerMaxBudget;
    private LinearLayout layout_Assigned;
    private ArrayList<String> spProjectList;
    private ArrayList<String> spAssignList;
    private ArrayList<String> spLeadStatusList;
      private ArrayList<String> spLocationList = new ArrayList<>();
    private List<Projects> mProjectsList;
    private ArrayList<Assign_To> mAssignList;
    private ArrayList<LeadStatus> mLeadStatusList;
    ArrayList<String> budgetMin = new ArrayList<>();
    ArrayList<String> budgetMax = new ArrayList<>();
     private List<String> mLocListKey;
     private List<String> mLocListValue;
    ArrayAdapter locationAdapter, minAdapter, maxAdapter;
    MultiSelectAdapter mAdapter;
    private ProgressDialog dialog;
    private BMHApplication app;
    private int defaultPosition = 0;
    private int defaultPosAssignTo;
    private int defaultPosMinBudget;
    private int defaultPosMaxBudget;
    private int defaultPosLeadStatus, alarmIndex;
    private boolean isInitialAssignTo = false;
    private boolean isInitialPersonCount = false;
    private boolean isInitialLeadStatus = false;
    private boolean isInitialMinBudget = false;
    private boolean isInitialMaxBudget = false;
    private boolean isMultiSpinner = false;
    LinearLayout layout_lead_type, layout_address, layout_location, layout_time, layout_date, layout_last_updated_on;
    ConstraintLayout layout_select_lead, layout_callback_update, layout_meeting_update, layout_site_visit_update;
    AlertDialog.Builder alertDialog;
    LinearLayout layout_date_divider, layout_time_divider, layout_location_divider,
            layout_address_divider, layout_last_updated_on_divider, layout_lead_type_divider;
    TextView tv_date, tv_date_val, tv_time, tv_time_val, tv_project_name, tv_select_location, tv_address,
            tv_campaign_val, tv_campaign_date_val, tv_mobile_no_val, tv_budget_val,
            tv_last_updated_on_val, textView_projects;
    EditText tv_customer_name_val, tv_email_id_val, tv_alternate_mobile_no_val, tv_address_val;
    RadioButton radioButton_hot, radioButton_warm, radioButton_cold;
    ImageView img_send_email;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button_update;
    EditText edtRemark;
    private int mPersonCount = 0, isAssigned = 0, updateFromAlarmNotification = 0;
    View convertView;
    private Calendar selectedCal;
    private Calendar currentCal;
    private String mSelectedTab = "", tagReceivedFrom = "", mDateText = "", mTimeText = "", strBudgetRange = "",
            enquiryId = "", mRecFilePath = "", mSelectedProjectId = "", mSelectedAssignToId = "", mSelectedLeadStatusId = "0",
            mSelectedAssignedTo = "", mSelectedProjectName = "", mSelectedLeadStatus = "", mSelectedRadioButton = "",
            mMinBudget = "", mMaxBudget = "", mobileNo = "", mSelectedPersonCount = "";
    Date currentDate = null, selectedDate = null;
    final String dateFormat = Utils.dateFormat;
    LinearLayout linear_layout_email, tv_email_id_divider, tv_campaign_id_divider, layout_budget_spinner, layout_assignTo, layout_assignTo_divider,
            linear_layout_campaign_date, tv_alternate_mobile_no_divider, linear_layout_alternate_mobile, linear_layout_campaign_name;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    private LinkedList<Projects> selectedProjList = new LinkedList<>();
    private Set<Projects> set = null;
    ImageView iv_callback_plus, iv_meeting_plus, iv_site_visit_plus;
        TextView lead_status;
    private int hasExtCallPermission;
    private List<String> permissionsCall = new ArrayList<>();
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};

    public AsmDetailsActivity() {
        spProjectList = new ArrayList<>();
        spAssignList = new ArrayList<>();
        spLeadStatusList = new ArrayList<>();
    }

    @Override
           protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asm_details);
        lead_status=findViewById(R.id.tv_current_status_val);
        tv_address_val = findViewById(R.id.tv_address_val);
        edtRemark = findViewById(R.id.edit_text_remark);
        Intent resultIntent=getIntent();
        String s5 = resultIntent.getStringExtra("address");
        tv_address_val.setText(s5);
        String s7 = resultIntent.getStringExtra("remark");
        edtRemark.setText(s7);

        layout_Assigned = findViewById(R.id.layout_button_update);
        button_update = findViewById(R.id.button_update);
        mProjectsSpinner = findViewById(R.id.spinner_project_name);
        mAssignToSpinner = findViewById(R.id.spinner_assign_to);
        mLeadStatusSpinner = findViewById(R.id.spinner_lead_status);
        mLocationSpinner = findViewById(R.id.spinner_select_location);
        mSpinnerMinBudget = findViewById(R.id.spinner_min_budget);
        mSpinnerMaxBudget = findViewById(R.id.spinner_max_budget);


        layout_last_updated_on = findViewById(R.id.layout_last_updated_on);
        tv_last_updated_on_val = findViewById(R.id.tv_last_updated_on_val);
        layout_lead_type = findViewById(R.id.layout_lead_type);
        layout_address = findViewById(R.id.layout_address);
        layout_location = findViewById(R.id.layout_location);
        layout_time = findViewById(R.id.layout_time);
        layout_date = findViewById(R.id.layout_date);

        layout_last_updated_on_divider = findViewById(R.id.layout_last_updated_on_divider);
        layout_date_divider = findViewById(R.id.layout_date_divider);
        layout_time_divider = findViewById(R.id.layout_time_divider);
        layout_location_divider = findViewById(R.id.layout_location_divider);
        layout_address_divider = findViewById(R.id.layout_address_divider);
        layout_lead_type_divider = findViewById(R.id.layout_lead_type_divider);
        layout_select_lead = findViewById(R.id.layout_select_lead);
        layout_callback_update = findViewById(R.id.layout_callback_update);
        layout_meeting_update = findViewById(R.id.layout_meeting_update);
        layout_site_visit_update = findViewById(R.id.layout_site_visit_update);
        linear_layout_email = findViewById(R.id.linear_layout_email);
        tv_email_id_divider = findViewById(R.id.tv_email_id_divider);
        tv_campaign_id_divider = findViewById(R.id.tv_campaign_id_divider);
        linear_layout_campaign_date = findViewById(R.id.linear_layout_campaign_date);
        tv_alternate_mobile_no_divider = findViewById(R.id.tv_alternate_mobile_no_divider);
        linear_layout_alternate_mobile = findViewById(R.id.linear_layout_alternate_mobile);
        linear_layout_campaign_name = findViewById(R.id.linear_layout_campaign_name);
        layout_budget_spinner = findViewById(R.id.layout_budget_spinner);
        layout_assignTo = findViewById(R.id.layout_assignTo);
        layout_assignTo_divider = findViewById(R.id.layout_assignTo_divider);

        tv_campaign_val = findViewById(R.id.tv_campaign_val);
        tv_campaign_date_val = findViewById(R.id.tv_campaign_date_val);
        tv_customer_name_val = findViewById(R.id.tv_customer_name_val);
        tv_email_id_val = findViewById(R.id.tv_email_id_val);
        img_send_email = findViewById(R.id.img_send_email);
        tv_mobile_no_val = findViewById(R.id.tv_mobile_no_val);
        tv_alternate_mobile_no_val = findViewById(R.id.tv_alternate_mobile_no_val);

        textView_projects = findViewById(R.id.textView_projects);
        tv_date = findViewById(R.id.tv_date);
        tv_date_val = findViewById(R.id.tv_date_val);
        tv_time = findViewById(R.id.tv_time);
        tv_time_val = findViewById(R.id.tv_time_val);
        tv_project_name = findViewById(R.id.tv_project_name);
        tv_select_location = findViewById(R.id.tv_select_location);
        tv_address = findViewById(R.id.tv_address);

        tv_budget_val = findViewById(R.id.tv_budget_val);
        tv_address_val.setFocusableInTouchMode(true);

        radioGroup = findViewById(R.id.radioGroup);
        radioButton_hot = findViewById(R.id.radioButton_hot);
        radioButton_warm = findViewById(R.id.radioButton_warm);
        radioButton_cold = findViewById(R.id.radioButton_cold);
        //rl_callback = findViewById(R.id.rl_callback);
        //rl_meeting = findViewById(R.id.rl_meeting);
       // rl_site_visit = findViewById(R.id.rl_site_visit);
        iv_callback_plus = findViewById(R.id.iv_callback_plus);
        iv_meeting_plus = findViewById(R.id.iv_meeting_plus);
        iv_site_visit_plus = findViewById(R.id.iv_site_visit_plus);


      //  String s1 = resultIntent.getStringExtra("lead_status");
       /* String s2 = resultIntent.getStringExtra("date");
        tv_date_val.setText(s2);
        String s3 = resultIntent.getStringExtra("time");
        tv_time_val.setText(s3);
       // String s4 = resultIntent.getStringExtra("no_of_persons");
        String s5 = resultIntent.getStringExtra("address");
        tv_address_val.setText(s5);*/
      //  String s6 = resultIntent.getStringExtra("lead_type");
       /* String s7 = resultIntent.getStringExtra("remark");
        edtRemark.setText(s7);*/

       // lead_status.setText(s1);
       // tv_address_val.setText(s5);


       // layout_lead_type.setTag(s6);
      //  edtRemark.setText(s7);





        tv_mobile_no_val.setOnClickListener(this);
        img_send_email.setOnClickListener(this);
        tv_date_val.setOnClickListener(this);
        tv_time_val.setOnClickListener(this);
        radioButton_hot.setOnClickListener(this);
        radioButton_warm.setOnClickListener(this);
        radioButton_cold.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        button_update.setOnClickListener(this);

        initView();




    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            isInitialPersonCount = true;
            isInitialAssignTo = true;
            isInitialLeadStatus = true;
            isInitialMinBudget = true;
            isInitialMaxBudget = true;
            selectedCal = Calendar.getInstance();
            currentCal = Calendar.getInstance();
            app = (BMHApplication) getApplication();

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
                mAsmModel = intent.getParcelableExtra(BMHConstants.ASM_MODEL_DATA);
                enquiryId = mAsmModel.getEnquiry_id();
                //     mCustomerName = mAsmModel.getCustomer_name();
            }
            if (Connectivity.isConnected(this)) {
                mDetailsModel = intent.getParcelableExtra(BMHConstants.ASM_DETAIL_DATA);

                if (mDetailsModel == null && !TextUtils.isEmpty(enquiryId))
                    // GET OFFLINE DATA FROM DB
                    new GetPreSalesLeadDetailTask(AsmDetailsActivity.this, enquiryId).execute();
                else
                    setLeadDetails(mDetailsModel);
            } else {
                // GET OFFLINE DATA FROM DB
                if (!TextUtils.isEmpty(enquiryId))
                    new GetPreSalesLeadDetailTask(this, enquiryId).execute();
            }
        }
    }

    private void setLeadDetails(Details mDetailsModel) {
        if (mDetailsModel != null) {
            init();
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.toolbar_txt_broker_status_and_type, mDetailsModel.getName(), mDetailsModel.getEnquiry_ID()));
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            tv_date_val.setText(mDetailsModel.getDate());
            tv_time_val.setText(mDetailsModel.getTime());
            edtRemark.setText(mDetailsModel.getRemark());
            tv_address_val.setText(mDetailsModel.getAddress());

            mProjectsList = mDetailsModel.getProjectList();
            List<Projects> mSelectedMultiProjList = getSelectedProject(mDetailsModel.getSelectedProjList());
            mAssignList = mDetailsModel.getAssignToList();
            mLeadStatusList = mDetailsModel.getLeadStatusList();
            //    mLocListKey = mDetailsModel.getLocListKey();
            //   mLocListValue = mDetailsModel.getLocListValue();

            if (TextUtils.isEmpty(mAsmModel.getSalesperson_name()) || mAsmModel.getSalesperson_name().equalsIgnoreCase("null")) {
               // spAssignList.add(0, getString(R.string.spinner_default_txt));
                mSelectedAssignedTo = "NA";
            } else {
                mSelectedAssignedTo = mAsmModel.getSalesperson_name();
                // getSelectedAssignTo(spAssignList, mAsmModel.getSalesperson_name());
            }

            if (mSelectedMultiProjList.size() > 0) {
                for (int i = 0; i < mSelectedMultiProjList.size(); i++) {
                    projectsModel = mSelectedMultiProjList.get(i);
                    spProjectList.add(projectsModel.getProject_name());
                }
            }

            if (mAssignList.size() > 0) {
                spAssignList.add(0, getString(R.string.spinner_default_txt));
                for (int i = 0; i < mAssignList.size(); i++) {
                    assignToModel = mAssignList.get(i);
                    spAssignList.add(assignToModel.getSalesperson_name());
                }
                getSelectedAssignTo(spAssignList, mSelectedAssignedTo);
            } else {
                new GetSpMasterTask(this).execute();
            }

            if (TextUtils.isEmpty(mAsmModel.getStatus()) || mAsmModel.getStatus().equalsIgnoreCase("")
                    || mAsmModel.getStatus().equalsIgnoreCase("NA")) {
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
            } else if (mAsmModel.getStatus().equalsIgnoreCase(getString(R.string.spinner_select_status))) {
                mAsmModel.setStatus("");
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
            }
            if (mAsmModel.getStatus().equalsIgnoreCase(getString(R.string.text_closure))) {
                if (mLeadStatusList.size() > 0)
                    mLeadStatusList.clear();
                spLeadStatusList.add(0, mAsmModel.getStatus());
                mLeadStatusSpinner.setSelection(0);
            } else {
                if (spLeadStatusList.size() == 0)
                    spLeadStatusList.add(0, mAsmModel.getStatus());

                if (mLeadStatusList.size() > 0) {
                    if (spLeadStatusList.size() > 0)
                        spLeadStatusList.clear();
                    spLeadStatusList.add(0, getString(R.string.spinner_select_status));
                    for (int i = 0; i < mLeadStatusList.size(); i++) {
                        leadStatusModel = mLeadStatusList.get(i);
                        spLeadStatusList.add(leadStatusModel.getTitle());
                    }
                    for (int i = 0; i < spLeadStatusList.size(); i++) {
                        if (spLeadStatusList.get(i).equalsIgnoreCase(getString(R.string.text_closure)))
                            spLeadStatusList.remove(i);
                    }
                    if (spLeadStatusList.size() > 0)
                        getSelectedLeadStatus(mLeadStatusList, mAsmModel.getStatus());
                } else {
                    new GetStatusMasterTask(this).execute();
                }
            }
            for (int i = 0; i < mSelectedMultiProjList.size(); i++) {
                projectsModel = mSelectedMultiProjList.get(i);
                spProjectList.add(projectsModel.getProject_name());
            }


            //spLocationList.addAll(mLocListKey);
          /*  if (mSelectedTab.equalsIgnoreCase(getString(R.string.tab_un_assigned))) {
                spProjectList.add(0, getString(R.string.spinner_select_project));
                spAssignList.add(0, getString(R.string.spinner_default_txt));
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
                mProjectsSpinner.setSelection(0);
                mAssignToSpinner.setSelection(0);
                mLeadStatusSpinner.setSelection(0);
                getSelectedProject(spProjectList, mDetailsModel.getProjectName());
            } else {*/
            if (mSelectedMultiProjList != null && mSelectedMultiProjList.size() == 0) {
                spProjectList.add(0, getString(R.string.spinner_select_project));
                textView_projects.setText(spProjectList.get(0));
                mSelectedProjectName = spProjectList.get(0);
            } else {
                //  getSelectedProject(spProjectList, mDetailsModel.getProjectName());
                String strMulti = Utils.getMultiSelectedProject(mSelectedMultiProjList);
                textView_projects.setText(strMulti);
                mSelectedProjectName = strMulti;
                mSelectedProjectId = Utils.getSelectedMultiProjectIds(mSelectedMultiProjList);
            }

            if (!TextUtils.isEmpty(mSelectedProjectName)) {
                if (!mSelectedProjectName.equalsIgnoreCase(getString(R.string.spinner_select_project))) {
                    for (int i = 0; i < mProjectsList.size(); i++) {
                        if (mSelectedProjectName.contains(mProjectsList.get(i).getProject_name()))
                            selectedProjList.add(mProjectsList.get(i));
                    }
                }
            }
            textView_projects.setText(mSelectedProjectName);
            mProjectsSpinner.setOnTouchListener(this);

            assignToAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spAssignList);
            assignToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            mAssignToSpinner.setAdapter(assignToAdapter);
            mAssignToSpinner.setOnItemSelectedListener(this);

            leadStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLeadStatusList);
            leadStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mLeadStatusSpinner.setAdapter(leadStatusAdapter);
            mLeadStatusSpinner.setOnItemSelectedListener(this);

          /*  locationAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLocationList);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            mLocationSpinner.setAdapter(locationAdapter);*/
            mLocationSpinner.setOnItemSelectedListener(this);

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
                budgetMin.add(0, getString(R.string.txt_min_budget));
                budgetMax.add(0, getString(R.string.txt_max_budget));

            }

            mSpinnerMinBudget.setPadding(0, 0, 50, 0);
            minAdapter = new ArrayAdapter(AsmDetailsActivity.this, R.layout.textview_spinner, budgetMin);
            minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerMinBudget.setAdapter(minAdapter);
            mSpinnerMinBudget.setOnItemSelectedListener(this);

            maxAdapter = new ArrayAdapter(AsmDetailsActivity.this, R.layout.textview_spinner, budgetMax);
            maxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerMaxBudget.setPadding(0, 0, 50, 0);
            mSpinnerMaxBudget.setAdapter(maxAdapter);
            mSpinnerMaxBudget.setOnItemSelectedListener(this);

            if (mAsmModel.getIsAssigned() == 0) {
                layout_last_updated_on_divider.setVisibility(View.GONE);
                layout_last_updated_on.setVisibility(View.GONE);
                mSelectedTab = getString(R.string.tab_un_assigned);
                isAssigned = 0;
            } else {
                mSelectedTab = getString(R.string.tab_assigned);
                isAssigned = 1;
            }
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

    private void init() {
        String campaignName = mDetailsModel.getCampaign();
        if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals(""))) {
            tv_campaign_val.setText(getString(R.string.txt_campaign_pro, campaignName));
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
        //((TextView) findViewById(R.id.tv_project_name_val)).setText(mDetailsModel.getProject_Name());
        if (!TextUtils.isEmpty(mDetailsModel.getAlternate_Mobile_No()) && mDetailsModel.getAlternate_Mobile_No() != null) {
            tv_alternate_mobile_no_val.setText(mDetailsModel.getAlternate_Mobile_No());
        } else {
            tv_alternate_mobile_no_divider.setVisibility(View.GONE);
            linear_layout_alternate_mobile.setVisibility(View.GONE);
        }

       // ((TextView) findViewById(R.id.tv_current_status_val)).setText(mDetailsModel.getCurrent_Status());
   /*     if (mDetailsModel.getCurrent_Status().equalsIgnoreCase("NA"))
            ((TextView) findViewById(R.id.tv_callback_date_time)).setText(getString(R.string.txt_date_time));
        else
            ((TextView) findViewById(R.id.tv_callback_date_time)).setText(getString(R.string.txt_callback_date_time, mDetailsModel.getCurrent_Status()));

        ((TextView) findViewById(R.id.tv_callback_date_time_val)).setText(mDetailsModel.getDate_And_Time());*/
        if (mAsmModel.getIsAssigned() == 1)
            tv_last_updated_on_val.setText(mAsmModel.getLastupdatedon());
        tv_campaign_date_val.setText(mDetailsModel.getCampaign_Date());


        // SELECTED TAB - FIRST
        // SELECTED TAB - SECOND
        layout_Assigned.setVisibility(View.VISIBLE);
    }

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

    private String getSelectedAssignTo(ArrayList<String> spAssignList, String assignTo) {
        if (TextUtils.isEmpty(assignTo) || assignTo.equalsIgnoreCase("") || assignTo.equalsIgnoreCase("NA"))
            return null;
        for (int i = 0; i < spAssignList.size(); i++) {
            String spName = spAssignList.get(i);
            if (spName.equalsIgnoreCase(assignTo)) {
                mAssignToSpinner.setSelection(i);
                defaultPosAssignTo = i;
                break;
            }
        }
        assignToAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spAssignList);
        assignToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mAssignToSpinner.setAdapter(assignToAdapter);
        mAssignToSpinner.setSelection(defaultPosAssignTo);

        return null;
    }

    private String getSelectedLeadStatus(ArrayList<LeadStatus> leadStatusList, String
            leadStatus) {
        if (TextUtils.isEmpty(leadStatus) || leadStatus.equalsIgnoreCase("")
                || leadStatus.equalsIgnoreCase("NA")) {
            mLeadStatusSpinner.setSelection(0);
            return null;
        }
        /*if (leadStatus.equalsIgnoreCase(getString(R.string.text_closure))) {
            if (leadStatusList.size() > 0)
                leadStatusList.clear();
            leadStatusList.add(0, new LeadStatus("0", mAsmModel.getStatus()));
        } else {*/

        for (int i = 0; i < leadStatusList.size(); i++) {
            if (leadStatusList.get(i).getTitle().equalsIgnoreCase(mAsmModel.getStatus())) {
                mLeadStatusSpinner.setSelection(i);
                defaultPosLeadStatus = i;
                break;
            }
        }
        //  }
        if (spLeadStatusList.size() > 0) {
            spLeadStatusList.clear();
            for (int i = 0; i < leadStatusList.size(); i++) {
                spLeadStatusList.add(leadStatusList.get(i).getTitle());
            }
        }
        leadStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLeadStatusList);
        mLeadStatusSpinner.setAdapter(leadStatusAdapter);
        mLeadStatusSpinner.setSelection(defaultPosLeadStatus);
        return null;
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

    @Override
    public void onBackPressed() {
        navigateToPrevious();
        super.onBackPressed();
    }

    private void navigateToPrevious() {
        app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
        if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
            Intent intent = new Intent(AsmDetailsActivity.this, PreSalesActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spinner_assign_to:
                if (isInitialAssignTo) {
                    position = defaultPosAssignTo;
                    isInitialAssignTo = false;
                }
                mSelectedAssignedTo = parent.getItemAtPosition(position).toString();
                getSelectedSpId(mSelectedAssignedTo);
                mAssignToSpinner.setSelection(position);
                break;

            case R.id.spinner_lead_status:
                if (isInitialLeadStatus) {
                    position = defaultPosLeadStatus;
                    isInitialLeadStatus = false;
                }
                mSelectedLeadStatus = parent.getItemAtPosition(position).toString();
                getLeadStatusId(mSelectedLeadStatus);
                mLeadStatusSpinner.setSelection(position);
                if (!mSelectedLeadStatus.equalsIgnoreCase(mAsmModel.getStatus())) {
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

                    if ((!TextUtils.isEmpty(mDetailsModel.getLead_type()) || !mDetailsModel.getLead_type().equals(""))
                            && (!mDetailsModel.getLead_type().equalsIgnoreCase("null"))) {
                        radioButton = selectLeadTypeStatus(mDetailsModel.getLead_type());
                        radioButton.setChecked(true);
                        mSelectedRadioButton = radioButton.getText().toString();
                    }
                }
                inflateDynamicView(mSelectedLeadStatus);
                break;

            case R.id.spinner_select_location:
                if (isInitialPersonCount) {
                    position = defaultPosition;
                    isInitialPersonCount = false;
                    if (mDetailsModel.getNoOfPersons() > 0) {
                        mPersonCount = mDetailsModel.getNoOfPersons();
                        //  mLocationSpinner.setSelection(mPersonCount);
                        position = mPersonCount - 1;
                    }
                }
                mSelectedPersonCount = parent.getItemAtPosition(position).toString();
                mLocationSpinner.setSelection(position);
                mPersonCount = Integer.valueOf(mSelectedPersonCount);


                // if(mSelectedPersonCount.equalsIgnoreCase(""))
                break;

            case R.id.spinner_min_budget:
                if (isInitialMinBudget) {
                    position = defaultPosMinBudget;
                    isInitialMinBudget = false;
                }
                Object objMin = parent.getItemAtPosition(position).toString();
                if (objMin.equals(getString(R.string.txt_min_budget)))
                    mMinBudget = objMin.toString();
                else {
                    mMinBudget = String.valueOf(Utils.convertStringToMoney(objMin.toString()));
                    if (!TextUtils.isEmpty(mMaxBudget) && !mMaxBudget.equals(getString(R.string.txt_max_budget))) {
                        if (Integer.valueOf(mMinBudget) < Integer.valueOf(mMaxBudget)) {
                            mSpinnerMinBudget.setSelection(position);
                        } else {
                            // mSpinnerMinBudget.setSelection(0);
                            Toast.makeText(AsmDetailsActivity.this, getString(R.string.toast_msg_min_budget_could_not_greater), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mSpinnerMinBudget.setSelection(position);
                    }
                }
                break;

            case R.id.spinner_max_budget:
                if (isInitialMaxBudget) {
                    position = defaultPosMaxBudget;
                    isInitialMaxBudget = false;
                }
                Object objMax = parent.getItemAtPosition(position).toString();
                if (mMinBudget.equalsIgnoreCase(getString(R.string.txt_min_budget))) {
                    Toast.makeText(AsmDetailsActivity.this, getString(R.string.toast_select_min_budget_first), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (((String) objMax).equalsIgnoreCase(getString(R.string.txt_max_budget)))
                    mMaxBudget = objMax.toString();
                else {
                    mMaxBudget = String.valueOf(Utils.convertStringToMoney(objMax.toString()));

                    if (Integer.valueOf(mMinBudget) < Integer.valueOf(mMaxBudget)) {
                        mSpinnerMaxBudget.setSelection(position);
                    } else {
                        // mSpinnerMaxBudget.setSelection(0);
                        Toast.makeText(AsmDetailsActivity.this, getString(R.string.toast_msg_max_budget_could_not_less), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("StringFormatInvalid")
    private void inflateDynamicView(String lead) {
        // radioGroup.clearCheck();
        layout_date.setVisibility(View.GONE);
        layout_time.setVisibility(View.GONE);
        layout_location.setVisibility(View.GONE);
        layout_address.setVisibility(View.GONE);
        layout_lead_type.setVisibility(View.GONE);
        layout_assignTo.setVisibility(View.VISIBLE);
        edtRemark.setVisibility(View.GONE);

        layout_date_divider.setVisibility(View.GONE);
        layout_time_divider.setVisibility(View.GONE);
        layout_location_divider.setVisibility(View.GONE);
        layout_address_divider.setVisibility(View.GONE);
        layout_lead_type_divider.setVisibility(View.GONE);
        layout_assignTo_divider.setVisibility(View.VISIBLE);

        /*
          Not Interested
         */
        if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[0])) {
            edtRemark.setVisibility(View.VISIBLE);
            //  mRemarkText = edtRemark.getText().toString();
            layout_assignTo.setVisibility(View.GONE);
            layout_assignTo_divider.setVisibility(View.GONE);
            return;
        }
        /*
           MARKETING CALL
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[1])) {
            edtRemark.setVisibility(View.VISIBLE);
            // mRemarkText = edtRemark.getText().toString();
            layout_assignTo.setVisibility(View.VISIBLE);
            layout_assignTo_divider.setVisibility(View.VISIBLE);
            return;
        }
        /*
           JUST ENQUIRY
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[2])) {
            edtRemark.setVisibility(View.VISIBLE);
            layout_lead_type_divider.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);

            // mRemarkText = edtRemark.getText().toString();
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
            layout_assignTo_divider.setVisibility(View.VISIBLE);
            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);
            layout_assignTo.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));
            mDateText = tv_date_val.getText().toString();
            tv_time.setText(getString(R.string.txt_typed_time, lead));
            mTimeText = tv_time_val.getText().toString();
            //  mRemarkText = edtRemark.getText().toString();
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
            layout_assignTo_divider.setVisibility(View.VISIBLE);
            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_location.setVisibility(View.GONE);
            layout_address.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            layout_assignTo.setVisibility(View.VISIBLE);
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
            // switchSpinnerList(true);
            tv_address_val.setText(getString(R.string.txt_typed_address, lead));
            edtRemark.setText(getString(R.string.txt_enter_remarks,lead));



            /* mSelectedRadioButton */
            //  mRemarkText = edtRemark.getText().toString();
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
            layout_assignTo_divider.setVisibility(View.VISIBLE);
            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_location.setVisibility(View.VISIBLE);
            layout_address.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            layout_assignTo.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));
            mDateText = tv_date_val.getText().toString();
            tv_time.setText(getString(R.string.txt_typed_time, lead));
            mTimeText = tv_time_val.getText().toString();
            /* mSelectedProject */
            tv_select_location.setText(getString(R.string.txt_select_person));
            switchSpinnerList();
            tv_address.setText(getString(R.string.txt_pickup_address));
            //  mRemarkText = edtRemark.getText().toString();
        } else if (lead.equalsIgnoreCase(getString(R.string.text_closure))) {
            edtRemark.setVisibility(View.VISIBLE);
            img_send_email.setClickable(false);
            mLeadStatusSpinner.setSelection(defaultPosLeadStatus);
            tv_customer_name_val.setEnabled(false);
            tv_email_id_val.setEnabled(false);
            mProjectsSpinner.setEnabled(false);
            mSpinnerMinBudget.setEnabled(false);
            mSpinnerMaxBudget.setEnabled(false);
            mAssignToSpinner.setEnabled(false);
            mLeadStatusSpinner.setEnabled(false);
            layout_date.setClickable(false);
            layout_time.setClickable(false);
            radioGroup.setClickable(false);
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
            //personArray.add(0, mDetailsModel.getNoOfPersons());
            mPersonCount = mDetailsModel.getNoOfPersons();
        }
        mLocationSpinner.setPadding(0, 0, 50, 0);
        locationAdapter = new ArrayAdapter(AsmDetailsActivity.this, R.layout.textview_spinner, personArray);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationSpinner.setAdapter(locationAdapter);
    }

    private String getSelectedProjectId(String selectedProject) {
        for (int i = 0; i < mProjectsList.size(); i++) {
            projectsModel = mProjectsList.get(i);
            if (selectedProject.equalsIgnoreCase(projectsModel.getProject_name())) {
                return projectsModel.getProject_id();
            }
        }
        return null;
    }

    private String getSelectedSpId(String selectedSp) {
        for (int i = 0; i < mAssignList.size(); i++) {
            assignToModel = mAssignList.get(i);
            if (selectedSp.equalsIgnoreCase(assignToModel.getSalesperson_name())) {
                return mSelectedAssignToId = assignToModel.getSalesperson_id();
            }
        }
        return null;
    }

    private String getLeadStatusId(String selectedSp) {
        if (TextUtils.isEmpty(selectedSp))
            return "";
        for (int i = 0; i < mLeadStatusList.size(); i++) {
            leadStatusModel = mLeadStatusList.get(i);
            if (selectedSp.equalsIgnoreCase(leadStatusModel.getTitle())) {
                return mSelectedLeadStatusId = leadStatusModel.getDisposition_id();
            }
        }
        return null;
    }

   /* private String getLocListValue(String locListKey) {
        for (int i = 0; i < mLocListKey.size(); i++) {
            if (locListKey.equalsIgnoreCase(mLocListKey.get(i))) {
                tv_address_val.setText("");
                tv_address_val.setText(mLocListValue.get(i));
                return mSelectedLocation = mLocListValue.get(i);
            }
        }
        return null;
    }*/

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm = findViewById(selectedId);
                mSelectedRadioButton = radioButton_warm.getText().toString();
                break;
            case R.id.radioButton_cold:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold = findViewById(selectedId);
                mSelectedRadioButton = radioButton_cold.getText().toString();
                break;
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

    private void setSelectedTime(int hourOfDay, int minute, TextView textView) {
        currentCal = Calendar.getInstance();
        selectedCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedCal.set(Calendar.MINUTE, minute);

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
            Toast.makeText(AsmDetailsActivity.this, getString(R.string.invalid_time_picker_error), Toast.LENGTH_SHORT).show();
        }
    }

    DatePickerDialog.OnDateSetListener dateChangeListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setSelectedDate(year, month, dayOfMonth, tv_date_val);
        }
    };

    private void setSelectedDate(int year, int month, int dayOfMonth, TextView textview) {
        selectedCal.set(Calendar.YEAR, year);
        selectedCal.set(Calendar.MONTH, month);
        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate = currentCal.getTime();
        selectedDate = selectedCal.getTime();
        CharSequence charSequence = simpleDateFormat.format(selectedDate);
        textview.setText(charSequence);
        mDateText = charSequence.toString();
        if (selectedCal.getTimeInMillis() <= currentCal.getTimeInMillis()) {
            tv_time_val.setText("");
            mTimeText = "";
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
        int id = v.getId();
        switch (id) {
            case R.id.tv_mobile_no_val:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermissions()) {
                        actionCall(mAsmModel.getCustomer_mobile());
                    } else {
                        requestPermissions();
                    }
                } else {
                    actionCall(mAsmModel.getCustomer_mobile());
                }
                break;
            case R.id.img_send_email:
                if (TextUtils.isEmpty(mAsmModel.getCustomer_email())) {
                    Utils.showToast(this, "Email id not available");
                } else {
                    String email = mAsmModel.getCustomer_email();
                    Utils.openMailClient(this, "Sales Person App Email", new String[]{email}, "");
                }
                break;
            case R.id.button_update:

                if (TextUtils.isEmpty(tv_customer_name_val.getText().toString())) {
                    Toast.makeText(this, R.string.toast_msg_enter_customer_name, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Utils.isValidUserName(tv_customer_name_val.getText().toString())) {
                    Toast.makeText(this, R.string.enter_valid_user, Toast.LENGTH_LONG).show();
                    return;
                }

                /*if (!Utils.isEmailValid(tv_email_id_val.getText().toString())) {
                    Toast.makeText(this, R.string.enter_valid_email, Toast.LENGTH_LONG).show();
                    return;
                }*/
                // LEAD STATUS - NOT INTERESTED (REMARK IS MANDATORY)
                if ((!mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[0]))
                        && (!mSelectedLeadStatus.equalsIgnoreCase(getString(R.string.text_closure)))) {

                    // PROJECT IS NOT MANDATORY IN CALLBACK
                    //   if (!mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {
                    if (TextUtils.isEmpty(mSelectedProjectId)) {
                        Toast.makeText(this, R.string.spinner_select_project, Toast.LENGTH_LONG).show();
                        return;
                    }
                    //   }
                    if (TextUtils.isEmpty(mMinBudget) || mMinBudget.equalsIgnoreCase(getString(R.string.txt_min_budget))
                            && (TextUtils.isEmpty(mMinBudget) || mMinBudget.equalsIgnoreCase(getString(R.string.txt_min_budget)))) {
                        Toast.makeText(this, R.string.toast_msg_budget_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (Integer.valueOf(mMinBudget) >= Integer.valueOf(mMaxBudget)) {
                        Toast.makeText(AsmDetailsActivity.this, getString(R.string.toast_msg_min_budget_could_not_greater), Toast.LENGTH_SHORT).show();
                        return;
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

                   /* if (TextUtils.isEmpty(mSelectedAssignToId)) {
                        Toast.makeText(this, R.string.toast_msg_please_select_sp, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mSelectedLeadStatusId)) {
                        Toast.makeText(this, R.string.spinner_select_status, Toast.LENGTH_LONG).show();
                        return;
                    }*/

                    // MANDATORY VALIDATION IN MEETING AND SITE VISIT
                    if ((mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4]))
                            || (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5]))) {
                        // MEETING
                        if (!mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {
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
                  /*  if (mSelectedLeadStatus.equalsIgnoreCase(String.valueOf(mAsmModel.getStatus()))
                            && mSelectedProjectName.equalsIgnoreCase(mAsmModel.getProject_name())
                            && mSelectedAssignedTo.equalsIgnoreCase(mAsmModel.getSalesperson_name())) {
                        Toast.makeText(this, R.string.toast_msg_project_assign_to_lead_status_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }*/
                } else {
                    if (TextUtils.isEmpty(edtRemark.getText().toString())) {
                        Toast.makeText(this, R.string.toast_msg_remark_mandatory, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                updateAsmInfo(mAsmModel.getEnquiry_id(), mSelectedAssignedTo, mSelectedTab, tagReceivedFrom);
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

                DatePickerDialog fromDateDialog = new DatePickerDialog(AsmDetailsActivity.this, dateChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_time_val:
                if (TextUtils.isEmpty(tv_date_val.getText().toString())) {
                    Toast.makeText(AsmDetailsActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
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

                TimePickerDialog mTimePicker = new TimePickerDialog(AsmDetailsActivity.this, onTimeSetListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time ");
                mTimePicker.show();
                break;
            case R.id.rl_callback:
                if (layout_callback_update.getVisibility() == View.GONE) {
                    layout_callback_update.setVisibility(View.VISIBLE);
                    iv_callback_plus.setImageResource(R.drawable.minus);
                } else {
                    layout_callback_update.setVisibility(View.GONE);
                    iv_callback_plus.setImageResource(R.drawable.plus);
                }
                break;
            case R.id.rl_meeting:
                if (layout_meeting_update.getVisibility() == View.GONE) {
                    layout_meeting_update.setVisibility(View.VISIBLE);
                    iv_meeting_plus.setImageResource(R.drawable.minus);
                } else {
                    layout_meeting_update.setVisibility(View.GONE);
                    iv_meeting_plus.setImageResource(R.drawable.plus);
                }
                break;
            case R.id.rl_site_visit:
                if (layout_site_visit_update.getVisibility() == View.GONE) {
                    layout_site_visit_update.setVisibility(View.VISIBLE);
                    iv_site_visit_plus.setImageResource(R.drawable.minus);
                } else {
                    layout_site_visit_update.setVisibility(View.GONE);
                    iv_site_visit_plus.setImageResource(R.drawable.plus);
                }
                break;
            default:
                break;
        }

    }

    private void updateAsmInfo(final String enquiryId, final String assignedTo,
                               final String mSelectedTab, final String tagReceivedFrom) {
        if (Connectivity.isConnected(AsmDetailsActivity.this)) {
            dialog = new ProgressDialog(AsmDetailsActivity.this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPDATE_ASM_DETAILS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (Connectivity.isConnected(AsmDetailsActivity.this)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        if (response != null) {
                            try {
                                //    new SyncDataTask(AsmDetailsActivity.this).execute();

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    Toast.makeText(AsmDetailsActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                    app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
                                    app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                                    if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                                        Intent intent = new Intent(AsmDetailsActivity.this, PreSalesActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(AsmDetailsActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(AsmDetailsActivity.this, PreSalesActivity.class);
                                        startActivity(intent);
                                    }

                                    finish();
                                }
                                if (!jsonObject.getBoolean("success")) {
                                    Toast.makeText(AsmDetailsActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AsmDetailsActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (Connectivity.isConnected(AsmDetailsActivity.this)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        if (!Connectivity.isConnected(AsmDetailsActivity.this)) {
                            app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
                            app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                            onBackPressed();
                        }
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
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.ENQUIRY_ID, enquiryId);
                params.put(ParamsConstants.ASSIGNED_TO, getSelectedSpId(assignedTo));
                params.put(ParamsConstants.IS_UPDATED, String.valueOf(0));
                params.put(ParamsConstants.STATUS_ID, getLeadStatusId(mSelectedLeadStatus));
                params.put(ParamsConstants.LEAD_STATUS, mSelectedLeadStatus);
                params.put("budget_min", mMinBudget);
                params.put("budget_max", mMaxBudget);
                params.put("date", mDateText);
                params.put("time", mTimeText);
                params.put("project_name", mSelectedProjectName);
                params.put("project_id", mSelectedProjectId);
                //       params.put("location", mSelectedLocation);
                params.put("address", tv_address_val.getText().toString());
                params.put("no_of_persons", String.valueOf(mPersonCount));
                params.put("lead_type", mSelectedRadioButton);
                params.put("remark", edtRemark.getText().toString());
                params.put("recordingFilePath", mRecFilePath);

                if (mMinBudget.equalsIgnoreCase(getString(R.string.txt_min_budget)))
                    mMinBudget = "0";
                if (mMaxBudget.equalsIgnoreCase(getString(R.string.txt_max_budget)))
                    mMaxBudget = "0";
                if (assignedTo.equalsIgnoreCase(getString(R.string.spinner_default_txt)))
                    mSelectedAssignedTo = "";
                if (mSelectedLeadStatus.equalsIgnoreCase(getString(R.string.spinner_select_status)))
                    mSelectedLeadStatus = "";

                int isSynced;
                if (Connectivity.isConnected(AsmDetailsActivity.this))
                    isSynced = 0;
                          else
                    isSynced = 1;

                if ((Utils.isChanged(mDetailsModel.getName(), tv_customer_name_val.getText()))

                        || Utils.isChanged(mDetailsModel.getEmail_ID(), tv_email_id_val.getText())

                        || (Utils.isChanged(mDetailsModel.getAlternate_Mobile_No(), tv_alternate_mobile_no_val.getText()))

                        || Utils.isBudgetChanged(mDetailsModel.getBudget_min(), mMinBudget)

                        || Utils.isBudgetChanged(mDetailsModel.getBudget_max(), mMaxBudget)) {

                    flagBuilder.append(101);
                }
                if (Utils.isChanged(mAsmModel.getSalesperson_name(), mSelectedAssignedTo)) {
                    if (TextUtils.isEmpty(flagBuilder.toString())) {
                        flagBuilder.append(102);
                    } else {
                        flagBuilder.append(",").append(102);
                    }
                    if (isAssigned == 0)
                        isAssigned = 1;
                    if (mSelectedAssignedTo.equalsIgnoreCase("Self")) {
                        CustomerInfoEntity customerEntity = new CustomerInfoEntity(mDetailsModel.getMobile_No(), mDetailsModel.getEnquiry_ID(), mDetailsModel.getName(),
                                BMHConstants.LEAD_PHASE_SALES, "1");
                        new CustomerInfoTask(customerEntity, true).execute();
                    }
                }
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
                if ((Utils.isChanged(mAsmModel.getStatus(), mLeadStatusSpinner.getSelectedItem().toString())) ||
                        (Utils.isChanged(mDetailsModel.getAddress(), tv_address_val.getText().toString())) ||
                        (Utils.isChanged(String.valueOf(mDetailsModel.getNoOfPersons()), String.valueOf(mPersonCount))) ||
                        (Utils.isChanged(mDetailsModel.getDate(), mDateText)) ||
                        (Utils.isChanged(mDetailsModel.getTime(), mTimeText)) ||
                        (Utils.isChanged(mDetailsModel.getLead_type(), mSelectedRadioButton)) ||
                        (Utils.isChanged(mDetailsModel.getRemark(), edtRemark.getText().toString()))) {

                    if (TextUtils.isEmpty(flagBuilder.toString())) {
                        flagBuilder.append(104);
                    } else
                        flagBuilder.append(",").append(104);
                }
                if (TextUtils.isEmpty(flagBuilder)) {
                    if (Connectivity.isConnected(AsmDetailsActivity.this)) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AsmDetailsActivity.this, R.string.txt_msg_lead_not_updated, Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (BMHApplication.getInstance().getRequestQueue() != null) {
                        BMHApplication.getInstance().getRequestQueue().cancelAll(getString(R.string.tag_update_lead));
                    }
                   /* if (Connectivity.isConnected(AsmDetailsActivity.this)) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AsmDetailsActivity.this, R.string.txt_no_internet_connection, Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent = new Intent(AsmDetailsActivity.this, PreSalesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }*/
                    return params;
                }
                params.put(BMHConstants.UPDATE_FLAG, flagBuilder.toString());

                if (!TextUtils.isEmpty(mRecFilePath)) {
                    Uri uri = Uri.parse(mRecFilePath);
                    new ChangeRecordingStatusTask(uri.getLastPathSegment()).execute();
                }

                if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {
                    CallbackEntity callbackEntity = new CallbackEntity(enquiryId, "",
                            Utils.getCurrentDateTime(), mDateText, mTimeText, "",
                            edtRemark.getText().toString(), mSelectedRadioButton, isSynced,
                            mRecFilePath, Utils.getCurrentDateTime());
                    new InsertCallbackTask(callbackEntity).execute();
                }
                if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {
                    MeetingEntity meetingEntity = new MeetingEntity(Calendar.getInstance().getTimeInMillis(), enquiryId, mSelectedLeadStatus, mTimeText,
                            mDateText, tv_address_val.getText().toString(), mSelectedRadioButton, edtRemark.getText().toString(),
                            Utils.getCurrentDateTime(), Utils.getCurrentDateTime(), isSynced,
                            mRecFilePath, Utils.getCurrentDateTime());
                    new InsertMeetingTask(meetingEntity).execute();
                }
                if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5])) {
                    SiteVisitEntity siteVisitEntity = new SiteVisitEntity(enquiryId, mSelectedLeadStatus, tv_address_val.getText().toString(),
                            mDateText, mTimeText, tv_address_val.getText().toString(), mSelectedRadioButton,
                            String.valueOf(mPersonCount), edtRemark.getText().toString(), "",
                            "", Utils.getCurrentDateTime(), Utils.getCurrentDateTime(), "",
                            isSynced, mRecFilePath, Utils.getCurrentDateTime());
                    new InsertSiteVisitTask(siteVisitEntity).execute();
                }

                if (!TextUtils.isEmpty(mSelectedProjectName)) {
                    SelectMultipleProjectsEntity multiSelectEntity =
                            new SelectMultipleProjectsEntity(enquiryId, mSelectedProjectId, mSelectedProjectName,
                                    isSynced, mRecFilePath, Utils.getCurrentDateTime());
                    new InsertMultiSelectTask(multiSelectEntity, isSynced).execute();
                }

                String scheduleDateTime = "";
                if (!TextUtils.isEmpty(mDateText)) {
                    String[] dateArr = mDateText.trim().split(",");
                    // dateAndTime = Utils.getDateFromString(overdueDate[1]) + " " + newTime;
                    scheduleDateTime = dateArr[1] + " | " + mTimeText;
                }
                PreSalesLeadDetailsEntity preSalesLeadDetailsEntity = new PreSalesLeadDetailsEntity(
                        enquiryId, tv_customer_name_val.getText().toString(),
                        tv_mobile_no_val.getText().toString(), tv_email_id_val.getText().toString(), tv_alternate_mobile_no_val.getText().toString(),
                        Integer.valueOf(mSelectedLeadStatusId), mSelectedLeadStatus, scheduleDateTime,
                        mSelectedProjectId, edtRemark.getText().toString(), strBudgetRange, mDateText, mTimeText, mMinBudget, mMaxBudget,
                        "", mSelectedAssignedTo, getSelectedSpId(mSelectedAssignedTo), isAssigned, tv_campaign_val.getText().toString(),
                        tv_campaign_date_val.getText().toString(), 0,
                        updateFromAlarmNotification, isSynced, Utils.getCurrentDateTime(), getString(R.string.txt_asm), mRecFilePath);
                new InsertPreSalesLeadDetailsTask(AsmDetailsActivity.this, preSalesLeadDetailsEntity, true).execute();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                            AlarmJobServices services = new AlarmJobServices();
                            services.stopRepeatingAlarm(alarmIndex, AsmDetailsActivity.this);
                        }
                    }
                });
                startAlarmService();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, getString(R.string.tag_update_lead));
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
                  /*  if ((System.currentTimeMillis() <= cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_24_HOUR)
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
                //   startIntent.putExtra(BMHConstants.MOBILE_NO, number);
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
    public void getLeadsDetailCallback(List<SelectMultipleProjectsEntity> selectMultiProList,
                                       CallbackEntity callbackEntity, MeetingEntity meetingEntity,
                                       SiteVisitEntity siteVisitEntity, PreSalesLeadDetailsEntity ldEntity) {

        String address = "";
        String remark = "";
        String leadType = "";
        String budget = "";
        int noOfPerson = 0;

        List<Projects> mProjectsList = PreSalesActivity.mProjectsList;
        ArrayList<Assign_To> mSpList = PreSalesActivity.mSpList;
        ArrayList<LeadStatus> mLeadsList = PreSalesActivity.mLeadsList;
        budget = ldEntity.getBudgetMin() + "-" + ldEntity.getBudgetMax();

        ArrayList<Projects> mSelectedProjList = new ArrayList<>();
        for (int i = 0; i < selectMultiProList.size(); i++) {
            mSelectedProjList.add(new Projects(selectMultiProList.get(i).getProjectId(), selectMultiProList.get(i).getProjectName()));
        }
        if (mSelectedProjList.size() > 0)
            mSelectedProjectName = Utils.getMultiSelectedProject(mSelectedProjList);
        else {
            try {
                new GetMultipleProjectTask(this, enquiryId).execute();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

      /*  if (mLeadsList.size() <= 0) {
            new GetStatusMasterTask(this).execute();
        }
        if (mSpList.size() <= 0) {
            new GetSpMasterTask(this).execute();
        }
*/
        mAsmModel = new PreSalesAsmModel(ldEntity.getEnquiryId(), ldEntity.getCustomerName(), ldEntity.getCustomerMobile(),
                ldEntity.getCustomerEmail(), ldEntity.getCustomerAlternateMobile(), ldEntity.getCampaignName(),
                ldEntity.getCampaignDate(), ldEntity.getCurrentStatus(),
                mSelectedProjectName, ldEntity.getRemark(), budget, ldEntity.getBudgetMin(), ldEntity.getBudgetMax(),
                ldEntity.getDate(), ldEntity.getTime(), ldEntity.getAssignedTo(),
                ldEntity.getIsAssigned(), ldEntity.getScheduledatetime(), ldEntity.getLastupdatedon(), null);
        mSelectedLeadStatus = ldEntity.getCurrentStatus();
        // Callback
        if (mAsmModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {
            if (callbackEntity != null) {
                remark = callbackEntity.getRemark();
                leadType = callbackEntity.getLeadType();
            }
            mDetailsModel = new Details(mAsmModel.getEnquiry_id(), mAsmModel.getCampaign_name(), mAsmModel.getCampaign_Date(),
                    mAsmModel.getCustomer_name(), mAsmModel.getCustomer_email(), mAsmModel.getCustomer_mobile(),
                    mAsmModel.getCustomer_alt_mobile(), mAsmModel.getBudget(), mAsmModel.getStatus(),
                    mAsmModel.getScheduledatetime(), mAsmModel.getUpdate_date(), mAsmModel.getUpdate_time(),
                    "", mSelectedProjectName, mProjectsList, mSelectedProjList,
                    mSpList, mLeadsList, remark, leadType,
                    mAsmModel.getBudgetMin(), mAsmModel.getBudgetMax(), 0, mAsmModel.getLastupdatedon());
            setLeadDetails(mDetailsModel);
            return;
        }

        // Meeting
        if (mAsmModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {

            if (meetingEntity != null) {
                address = meetingEntity.getAddress();
                remark = meetingEntity.getRemark();
                leadType = meetingEntity.getLeadType();
            }
            mDetailsModel = new Details(mAsmModel.getEnquiry_id(), mAsmModel.getCampaign_name(), mAsmModel.getCampaign_Date(),
                    mAsmModel.getCustomer_name(), mAsmModel.getCustomer_email(), mAsmModel.getCustomer_mobile(),
                    mAsmModel.getCustomer_alt_mobile(), mAsmModel.getBudget(), mAsmModel.getStatus(),
                    mAsmModel.getScheduledatetime(), mAsmModel.getUpdate_date(), mAsmModel.getUpdate_time(),
                    address, mSelectedProjectName, mProjectsList, mSelectedProjList,
                    mSpList, mLeadsList, remark, leadType,
                    mAsmModel.getBudgetMin(), mAsmModel.getBudgetMax(), 0, mAsmModel.getLastupdatedon());
            setLeadDetails(mDetailsModel);
            return;
        }
        //SiteVisit
        if (mAsmModel.getStatus().equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5])) {
            if (siteVisitEntity != null) {
                address = siteVisitEntity.getAddress();
                remark = siteVisitEntity.getRemark();
                leadType = siteVisitEntity.getLeadType();
                noOfPerson = Integer.valueOf(siteVisitEntity.getNoOfPersonVisited());
            }

            mDetailsModel = new Details(mAsmModel.getEnquiry_id(), mAsmModel.getCampaign_name(), mAsmModel.getCampaign_Date(),
                    mAsmModel.getCustomer_name(), mAsmModel.getCustomer_email(), mAsmModel.getCustomer_mobile(),
                    mAsmModel.getCustomer_alt_mobile(), mAsmModel.getBudget(), mAsmModel.getStatus(),
                    mAsmModel.getScheduledatetime(), mAsmModel.getUpdate_date(), mAsmModel.getUpdate_time(),
                    address, mSelectedProjectName, mProjectsList, mSelectedProjList,
                    mSpList, mLeadsList, remark, leadType, mAsmModel.getBudgetMin(), mAsmModel.getBudgetMax(),
                    noOfPerson, mAsmModel.getLastupdatedon());
            setLeadDetails(mDetailsModel);
            return;
        }

        mDetailsModel = new Details(mAsmModel.getEnquiry_id(), mAsmModel.getCampaign_name(), mAsmModel.getCampaign_Date(),
                mAsmModel.getCustomer_name(), mAsmModel.getCustomer_email(), mAsmModel.getCustomer_mobile(),
                mAsmModel.getCustomer_alt_mobile(), mAsmModel.getBudget(), mAsmModel.getStatus(),
                mAsmModel.getScheduledatetime(), mAsmModel.getUpdate_date(), mAsmModel.getUpdate_time(),
                "", mSelectedProjectName, mProjectsList, mSelectedProjList,
                mSpList, mLeadsList, ldEntity.getRemark(), "",
                mAsmModel.getBudgetMin(), mAsmModel.getBudgetMax(), 0, mAsmModel.getLastupdatedon());
        setLeadDetails(mDetailsModel);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isMultiSpinner != true) {
            alertDialog = new AlertDialog.Builder(AsmDetailsActivity.this);
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
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    new GetMultipleProjectTask(this, enquiryId).execute();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            alertDialog.setCancelable(false)
                    .setPositiveButton(getString(R.string.txt_button_done), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            isMultiSpinner = false;
                            textView_projects.setText("");
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
            // create an alert dialog
            //   alertDialog.show();
            // Create the alert dialog
            AlertDialog dialog = alertDialog.create();
            // Finally, display the alert dialog
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

    private void setMultiSelectedAdapter(View convertView, Set<Projects> set, List<Projects> mProjectsList) {
        if (convertView != null) {
            RecyclerView recyclerView = convertView.findViewById(R.id.proMulti_recyclerView);
            mAdapter = new MultiSelectAdapter(AsmDetailsActivity.this, mProjectsList, set);
            selectedProjList.clear();
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(AsmDetailsActivity.this, LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AsmDetailsActivity.this);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void onProjectSelected(SelectableProject item) {
        // List<Projects> selectedItems = mAdapter.getSelectedItems();
        if (item.isSelected() && !selectedProjList.contains(item)) {
            selectedProjList.add(item);
        } else {
            //  selectedProjList.remove(item);
            item.setSelected(false);
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
        getSelectedLeadStatus(mLeadStatusList, mAsmModel.getStatus());
    }

    @Override
    public void getSpMasterCallback(List<SpMasterEntity> spMasterList) {
        spAssignList.clear();
        if (mAssignList == null)
            mAssignList = new ArrayList<>();
        mAssignList.clear();
        spAssignList.add(0, getString(R.string.spinner_default_txt));
        for (int i = 0; i < spMasterList.size(); i++) {
            spAssignList.add(spMasterList.get(i).getSpName());
            Assign_To assign = new Assign_To(spMasterList.get(i).getSpId(), spMasterList.get(i).getSpName());
            mAssignList.add(assign);
        }
        getSelectedAssignTo(spAssignList, mAsmModel.getSalesperson_name());
    }
}
