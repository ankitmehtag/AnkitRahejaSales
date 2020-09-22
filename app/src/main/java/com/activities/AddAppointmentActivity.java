package com.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.CustomSpinnerAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.database.entity.AddAppointmentEntity;
import com.database.task.InsertAddAppointmentTask;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.model.AppointmentModel;
import com.model.CorporateActivityModel;
import com.model.LeadStatus;
import com.model.NotInterestedLead;
import com.model.Projects;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddAppointmentActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener,
        View.OnClickListener {
    private RadioGroup select_activity;
    private RadioButton corporate_activity, self, with_broker;
    String mSelectedRadioButton = "Self";
    LinearLayout linearLayoutCorporate, linearLayoutSelf, linearLayoutWithBroker;
    LinearLayout layout_select_lead, layout_select_lead_ca;
    private ArrayList<NotInterestedLead> mBrokerList;
    private ArrayList<LeadStatus> mLeadStatusList;
    private ArrayList<Projects> projectsMasterList;
    private Spinner spinner_select_broker, spinner_project_name, mSpinnerMinBudget, mSpinnerMaxBudget, spinner_lead_status, spinner_no_of_persons_self;
    //Corporate Spinner
    private Spinner spinner_corporate_activity, spinner_project_name_ca, spinner_min_budget_ca, spinner_max_budget_ca, spinner_lead_status_ca, spinner_no_of_persons_ca;
    ArrayAdapter minAdapter, maxAdapter, personAdapter;
    String mMinBudget, mMaxBudget;
    private boolean isInitialMinBudget = false;
    private boolean isInitialMaxBudget = false;
    ArrayList<String> budgetMin = new ArrayList<>();
    ArrayList<String> budgetMax = new ArrayList<>();
    private ArrayList<String> spBrokerList = new ArrayList<>();
    private ArrayList<String> spCorporateActivityList = new ArrayList<>();
    private ArrayList<String> spCorporateActivityProjectList = new ArrayList<>();
    private ArrayList<String> spProjectList = new ArrayList<>();
    private ArrayList<String> spLeadStatusList = new ArrayList<>();
    private ArrayList<String> spCaLeadStatusList = new ArrayList<>();
    private NotInterestedLead notInterestedModel;
    private LeadStatus leadStatus;
    String leadType, leadTypeCA;
    private Projects projects;
    private CustomSpinnerAdapter brokerListAdapter, projectListAdapeter, leadStatusAdapter, leadCAStatusAdapter, corporateActivityAdapter, corporateActivityProjectAdapter;
    private String brokerId, brokerName, projectName, projectId, lead_status, ca_lead_status, ca_lead_status_id, lead_status_id, brokerAddress,
            corporateActivity = "", corporateActivityProject = "", corporateActivityProjectId = "", corporateActivityId = "",
            builderId = "", userId = "";
    int noOfPersons, noOfPersonsCA;
    private String ca_min_budget, ca_max_budget;
    private int defaultPosition = 0;
    private boolean isInitialSubStatus = false;
    private TextView tv_meeting_date_val, tv_meeting_time_val;
    int mYear = 0, mMonth = 0, mDay = 0;
    int mHour = 0, mMinute = 0;
    int isSynced = 1;
    String dateString, timeString, mTimeText;
    DatePickerDialog fromDateDialog;
    TimePickerDialog mTimePicker;
    final String dateFormat = Utils.dateFormat;
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    private Calendar currentCal;
    private Calendar selectedCal;
    Date currentDate = null, selectedDate = null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
    private Button btn_submit_appointment;
    private EditText tv_broker_meeting_address_val, tv_remark_val;
    BMHApplication app;
    private ProgressDialog dialog;
    private static final String TAG = FollowUpSalesDetailActivity.class.getSimpleName();
    ArrayList<Projects> projectsList = new ArrayList<>();
    ArrayList<Projects> corporateActivityProjectsList = new ArrayList<>();
    ArrayList<CorporateActivityModel> corporateActivityList = new ArrayList<>();
    CorporateActivityModel corporateActivityModel;
    String appointmentType, customerName, emailId, mobileNo, selectedProject, selectedProjectId, alternateMobileNo,
            budget, currentLeadStatus, currentLeadStatusId,
            leadStatusDateAndTime, address, remark, currentLeadType, no_of_persons, activity_id, activity_name;

    //Self Views
    EditText tv_customer_name_val, tv_email_id_val, tv_mobile_no_val, tv_alternate_mobile_no_val;
    EditText tv_customer_name_val_ca, tv_email_id_val_ca, tv_mobile_no_val_ca, tv_alternate_mobile_no_val_ca;

    //Dynamic Views Id
    private LinearLayout layout_address,
            layout_lead_type_divider, layout_no_of_persons_self, layout_no_of_persons_self_divider;
    RadioGroup radioGroup;
    RadioButton radioButton_hot, radioButton_warm, radioButton_cold;
    EditText edit_text_remark;
    TextView tv_date_self, tv_date_val_self, tv_time_val_self, tv_time_self, tv_address_self, tv_address_val_self;
    //Dynamic Views Id
    private LinearLayout layout_date_ca, layout_address_ca, layout_time_divider_ca, layout_address_divider_ca,
            layout_lead_type_divider_ca, layout_lead_type_ca, layout_no_of_persons_ca, layout_no_of_persons_ca_divider;
    private LinearLayout layout_time_ca;
    RadioGroup radioGroup_ca;
    RadioButton radioButton_hot_ca, radioButton_warm_ca, radioButton_cold_ca;
    EditText edit_text_remark_ca;
    TextView tv_date_ca, tv_date_val_ca, tv_time_val_ca, tv_time_ca, tv_address_ca, tv_address_val_ca;
    String selectedCorporateActivity, customerNameCA, emailIdCA, mobileNoCA, selectedProjectCA,
            selectedProjectIdCA, alternateMobileNoCA, budgetCA, currentLeadStatusCA, currentLeadStatusIdCA,
            leadStatusDateAndTimeCA, addressCA, remarkCA, currentLeadTypeCA, no_of_personsCA, selectedCorporateActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        app = (BMHApplication) getApplication();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Appointment");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        select_activity = findViewById(R.id.radio_group_add_appointment);
        corporate_activity = findViewById(R.id.corporate_activity);
        self = findViewById(R.id.self);
        with_broker = findViewById(R.id.with_broker);
        linearLayoutSelf = findViewById(R.id.linearLayoutSelf);
        linearLayoutCorporate = findViewById(R.id.layout_corporate_activity);
        linearLayoutWithBroker = findViewById(R.id.linearLayoutWithBroker);
        layout_select_lead = findViewById(R.id.layout_select_lead_self);
        spinner_select_broker = findViewById(R.id.spinner_select_broker);
        spinner_project_name = findViewById(R.id.spinner_project_name);
        tv_meeting_date_val = findViewById(R.id.tv_meeting_date_val);
        tv_meeting_time_val = findViewById(R.id.tv_meeting_time_val);
        mSpinnerMinBudget = findViewById(R.id.spinner_min_budget);
        mSpinnerMaxBudget = findViewById(R.id.spinner_max_budget);
        spinner_lead_status = findViewById(R.id.spinner_lead_status);
        spinner_no_of_persons_self = findViewById(R.id.spinner_no_of_persons_self);
        tv_broker_meeting_address_val = findViewById(R.id.tv_broker_meeting_address_val);
        tv_remark_val = findViewById(R.id.tv_remark_val);
        btn_submit_appointment = findViewById(R.id.btn_submit_appointment);
        //Self Views Id
        tv_customer_name_val = findViewById(R.id.tv_customer_name_val);
        tv_email_id_val = findViewById(R.id.tv_email_id_val);
        tv_mobile_no_val = findViewById(R.id.tv_mobile_no_val);
        tv_alternate_mobile_no_val = findViewById(R.id.tv_alternate_mobile_no_val);

        //getting id for making dynamic views
        layout_address = findViewById(R.id.layout_address_self);
        layout_lead_type_divider = findViewById(R.id.layout_lead_type_divider_self);
        layout_no_of_persons_self = findViewById(R.id.layout_no_of_persons_self);
        layout_no_of_persons_self_divider = findViewById(R.id.layout_no_of_persons_self_divider);
        radioGroup = findViewById(R.id.radioGroup_self);
        radioButton_hot = findViewById(R.id.radioButton_hot_self);
        radioButton_warm = findViewById(R.id.radioButton_warm_self);
        radioButton_cold = findViewById(R.id.radioButton_cold_self);
        edit_text_remark = findViewById(R.id.edit_text_remark_self);
        tv_date_self = findViewById(R.id.tv_date_self);
        tv_date_val_self = findViewById(R.id.tv_date_val_self);
        tv_time_val_self = findViewById(R.id.tv_time_val_self);
        tv_time_self = findViewById(R.id.tv_time_self);
        tv_address_self = findViewById(R.id.tv_address_self);
        tv_address_val_self = findViewById(R.id.tv_address_val_self);


        //Id for corporate Activity
        tv_customer_name_val_ca = findViewById(R.id.tv_customer_name_val_ca);
        tv_email_id_val_ca = findViewById(R.id.tv_email_id_val_ca);
        tv_mobile_no_val_ca = findViewById(R.id.tv_mobile_no_val_ca);
        tv_alternate_mobile_no_val_ca = findViewById(R.id.tv_alternate_mobile_no_val_ca);
        layout_select_lead_ca = findViewById(R.id.layout_select_lead_ca);

        spinner_corporate_activity = findViewById(R.id.spinner_corporate_activity);
        spinner_project_name_ca = findViewById(R.id.spinner_project_name_ca);
        spinner_min_budget_ca = findViewById(R.id.spinner_min_budget_ca);
        spinner_max_budget_ca = findViewById(R.id.spinner_max_budget_ca);
        spinner_lead_status_ca = findViewById(R.id.spinner_lead_status_ca);
        spinner_no_of_persons_ca = findViewById(R.id.spinner_no_of_persons_ca);

        layout_date_ca = findViewById(R.id.layout_date_ca);
        layout_address_divider_ca = findViewById(R.id.layout_address_divider_ca);
        layout_address_ca = findViewById(R.id.layout_address_ca);
        layout_time_divider_ca = findViewById(R.id.layout_time_divider_ca);
        layout_lead_type_divider_ca = findViewById(R.id.layout_lead_type_divider_ca);
        layout_no_of_persons_ca = findViewById(R.id.layout_no_of_persons_ca);
        layout_no_of_persons_ca_divider = findViewById(R.id.layout_no_of_persons_ca_divider);
        layout_lead_type_ca = findViewById(R.id.layout_lead_type_ca);
        layout_time_ca = findViewById(R.id.layout_time_ca);
        radioGroup_ca = findViewById(R.id.radioGroup_ca);
        radioButton_hot_ca = findViewById(R.id.radioButton_hot_ca);
        radioButton_warm_ca = findViewById(R.id.radioButton_warm_ca);
        radioButton_cold_ca = findViewById(R.id.radioButton_cold_ca);
        edit_text_remark_ca = findViewById(R.id.edit_text_remark_ca);
        tv_date_ca = findViewById(R.id.tv_date_ca);
        tv_date_val_ca = findViewById(R.id.tv_date_val_ca);
        tv_time_val_ca = findViewById(R.id.tv_time_val_ca);
        tv_time_ca = findViewById(R.id.tv_time_ca);
        tv_address_ca = findViewById(R.id.tv_address_ca);
        tv_address_val_ca = findViewById(R.id.tv_address_val_ca);
        self.setChecked(true);
        setView();
        selectedCal = Calendar.getInstance();
        currentCal = Calendar.getInstance();
        Intent intent = getIntent();
        mBrokerList = intent.getParcelableArrayListExtra("broker_list");
        mLeadStatusList = intent.getParcelableArrayListExtra("lead_status_list");
        corporateActivityList = intent.getParcelableArrayListExtra("corporate_list");

        projectsMasterList = intent.getParcelableArrayListExtra("master_project_list");
        projectsList.clear();
        if (projectsMasterList.size() > 0)
            projectsList.addAll(projectsMasterList);
        setUiData();

        radioGroup.setOnCheckedChangeListener(this);
        radioGroup_ca.setOnCheckedChangeListener(this);
        select_activity.setOnCheckedChangeListener(this);
        tv_meeting_date_val.setOnClickListener(this);
        tv_meeting_time_val.setOnClickListener(this);
        tv_date_val_self.setOnClickListener(this);
        tv_time_val_self.setOnClickListener(this);
        tv_time_val_ca.setOnClickListener(this);
        tv_date_val_ca.setOnClickListener(this);
        btn_submit_appointment.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedId;
        switch (checkedId) {
            case R.id.corporate_activity:
                selectedId = group.getCheckedRadioButtonId();
                corporate_activity = findViewById(selectedId);
                mSelectedRadioButton = corporate_activity.getText().toString();
                setView();
                break;
            case R.id.self:
                selectedId = group.getCheckedRadioButtonId();
                self = findViewById(selectedId);
                mSelectedRadioButton = self.getText().toString();
                setView();
                break;
            case R.id.with_broker:
                selectedId = group.getCheckedRadioButtonId();
                with_broker = findViewById(selectedId);
                mSelectedRadioButton = with_broker.getText().toString();
                setView();
                break;
            case R.id.radioButton_hot_self:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot = findViewById(selectedId);
                leadType = radioButton_hot.getText().toString();
                break;
            case R.id.radioButton_warm_self:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm = findViewById(selectedId);
                leadType = radioButton_warm.getText().toString();
                break;
            case R.id.radioButton_cold_self:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold = findViewById(selectedId);
                leadType = radioButton_cold.getText().toString();
                break;
            case R.id.radioButton_hot_ca:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot_ca = findViewById(selectedId);
                leadTypeCA = radioButton_hot_ca.getText().toString();
                break;
            case R.id.radioButton_warm_ca:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm_ca = findViewById(selectedId);
                leadTypeCA = radioButton_warm_ca.getText().toString();
                break;
            case R.id.radioButton_cold_ca:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold_ca = findViewById(selectedId);
                leadTypeCA = radioButton_cold_ca.getText().toString();
                break;
        }
    }

    private void setView() {
        linearLayoutCorporate.setVisibility(View.GONE);
        linearLayoutWithBroker.setVisibility(View.GONE);
        linearLayoutSelf.setVisibility(View.GONE);
        if (mSelectedRadioButton.equalsIgnoreCase("Corporate Activity")) {
            linearLayoutCorporate.setVisibility(View.VISIBLE);
            linearLayoutWithBroker.setVisibility(View.GONE);
            linearLayoutSelf.setVisibility(View.GONE);
        } else if (mSelectedRadioButton.equalsIgnoreCase("self")) {
            linearLayoutSelf.setVisibility(View.VISIBLE);
            linearLayoutWithBroker.setVisibility(View.GONE);
            linearLayoutCorporate.setVisibility(View.GONE);
        } else {
            linearLayoutWithBroker.setVisibility(View.VISIBLE);
            linearLayoutCorporate.setVisibility(View.GONE);
            linearLayoutSelf.setVisibility(View.GONE);
        }
    }

    private void getAppointmentDetails() {
        if (linearLayoutCorporate.getVisibility() == View.VISIBLE) {
            getCorporateActivityData();
        } else if (linearLayoutSelf.getVisibility() == View.VISIBLE) {
            getSelfLeadData();
        } else if (linearLayoutWithBroker.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(brokerId)) {
                Utils.showToast(this, getString(R.string.txt_please_select_broker));
                return;
            }
            if (TextUtils.isEmpty(tv_broker_meeting_address_val.getText().toString())) {
                Utils.showToast(this, getString(R.string.please_enter_address));
                return;
            }
            if (TextUtils.isEmpty(tv_meeting_date_val.getText().toString())) {
                Utils.showToast(this, getString(R.string.please_select_date_first));
                return;
            }
            if (TextUtils.isEmpty(tv_meeting_time_val.getText().toString())) {
                Utils.showToast(this, getString(R.string.please_select_time_first));
                return;
            }
            if (TextUtils.isEmpty(tv_remark_val.getText().toString())) {
                Utils.showToast(this, getString(R.string.enter_remark));
                return;
            }
            addBrokerMeeting();
        }
    }

    private void getCorporateActivityData() {
        appointmentType = "corporate";
        selectedCorporateActivity = corporateActivity;
        if (TextUtils.isEmpty(selectedCorporateActivity) || selectedCorporateActivity.equalsIgnoreCase(getString(R.string.select_corporate_activity))) {
            Utils.showToast(this, getString(R.string.select_corporate_activity));
            return;
        }
        selectedCorporateActivityId = corporateActivityId;
        customerNameCA = tv_customer_name_val_ca.getText().toString();
        if (TextUtils.isEmpty(customerNameCA)) {
            Utils.showToast(this, getString(R.string.toast_msg_enter_customer_name));
            return;
        }
        mobileNoCA = tv_mobile_no_val_ca.getText().toString();
        if (TextUtils.isEmpty(mobileNoCA)) {
            Utils.showToast(this, getString(R.string.enter_mobile));
            return;
        } else if (!Utils.isValidMobileNumber(mobileNoCA) || mobileNoCA.length() != 10) {
            Utils.showToast(this, getString(R.string.enter_valid_mobile));
            return;
        }
        emailIdCA = tv_email_id_val_ca.getText().toString();
        if (!Utils.isEmailValid(emailIdCA)) {
            Utils.showToast(this, getString(R.string.enter_valid_email));
            return;
        }
        selectedProjectCA = corporateActivityProject;
        if (TextUtils.isEmpty(selectedProjectCA) || selectedProjectCA.equalsIgnoreCase(getString(R.string.select_project))) {
            Utils.showToast(this, getString(R.string.select_one_project));
            return;
        }
        selectedProjectIdCA = corporateActivityProjectId;
        alternateMobileNoCA = tv_alternate_mobile_no_val_ca.getText().toString();

        if (Integer.valueOf(ca_min_budget) >= Integer.valueOf(ca_max_budget)) {
            Toast.makeText(AddAppointmentActivity.this, getString(R.string.toast_msg_min_budget_could_not_greater), Toast.LENGTH_SHORT).show();
            return;
        }
        budgetCA = ca_min_budget + "-" + ca_max_budget;

        currentLeadStatusCA = ca_lead_status;
        if (TextUtils.isEmpty(currentLeadStatusCA) || currentLeadStatusCA.equalsIgnoreCase(getString(R.string.txt_select_status))) {
            Utils.showToast(this, getString(R.string.txt_select_status_choose));
            return;
        }
        currentLeadStatusIdCA = ca_lead_status_id;
        leadStatusDateAndTimeCA = tv_date_val_ca.getText().toString() + " " + tv_time_val_ca.getText().toString();
        if (layout_time_ca.getVisibility() == View.VISIBLE && layout_date_ca.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(tv_date_val_ca.getText().toString()) || TextUtils.isEmpty(tv_time_val_ca.getText().toString())) {
                Utils.showToast(this, "Choose Date and Time");
                return;
            }
        }
        no_of_personsCA = String.valueOf(noOfPersonsCA);
        if (layout_no_of_persons_ca.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(no_of_personsCA)) {
                Utils.showToast(this, "Please Choose No Persons");
                return;
            }
        }
        addressCA = tv_address_val_ca.getText().toString();
        if (layout_address_ca.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(addressCA)) {
                Utils.showToast(this, "Please Enter Address");
                return;
            }
        }
        currentLeadTypeCA = leadTypeCA;
        if (layout_lead_type_ca.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(leadTypeCA)) {
                Utils.showToast(this, "Choose Lead Type");
                return;
            }
        }
        remarkCA = edit_text_remark_ca.getText().toString();
        if (TextUtils.isEmpty(remarkCA)) {
            Utils.showToast(this, getString(R.string.enter_remark));
            return;
        }
        AppointmentModel model = new AppointmentModel();
        model.setType(appointmentType);
        model.setActivity_name(selectedCorporateActivity);
        model.setActivity_id(selectedCorporateActivityId);
        model.setCustomer_name(customerNameCA);
        model.setEmail_id(emailIdCA);
        model.setMobile_no(mobileNoCA);
        model.setProject(selectedProjectIdCA);
        model.setAlternate_no(alternateMobileNoCA);
        model.setBudget(ca_min_budget + "-" + ca_max_budget);
        model.setStatus_title(currentLeadStatusCA);
        model.setStatus_id(currentLeadStatusIdCA);
        model.setDate_time(leadStatusDateAndTimeCA);
        model.setRemark(remarkCA);
        model.setAddress(addressCA);
        model.setLead_type(currentLeadTypeCA);
        model.setNo_of_persons(no_of_personsCA);
        addAppointment(model);
    }

    private void getSelfLeadData() {
        appointmentType = "self";
        customerName = tv_customer_name_val.getText().toString();
        if (TextUtils.isEmpty(customerName)) {
            Utils.showToast(this, getString(R.string.toast_msg_enter_customer_name));
            return;
        }
        emailId = tv_email_id_val.getText().toString();
        if (!Utils.isEmailValid(emailId)) {
            Utils.showToast(this, getString(R.string.enter_valid_email));
            return;
        }
        mobileNo = tv_mobile_no_val.getText().toString();
        if (TextUtils.isEmpty(mobileNo)) {
            Utils.showToast(this, getString(R.string.enter_mobile));
            return;
        } else if (!Utils.isValidMobileNumber(mobileNo) || mobileNo.length() != 10) {
            Utils.showToast(this, getString(R.string.enter_valid_mobile));
            return;
        }
        selectedProject = projectName;
        if (TextUtils.isEmpty(selectedProject) || selectedProject.equalsIgnoreCase(getString(R.string.select_project))) {
            Utils.showToast(this, getString(R.string.select_one_project));
            return;
        }
        selectedProjectId = projectId;
        alternateMobileNo = tv_alternate_mobile_no_val.getText().toString();

        if (Integer.valueOf(mMinBudget) >= Integer.valueOf(mMaxBudget)) {
            Toast.makeText(AddAppointmentActivity.this, getString(R.string.toast_msg_min_budget_could_not_greater), Toast.LENGTH_SHORT).show();
            return;
        }
        budget = mMinBudget + "-" + mMaxBudget;
        currentLeadStatus = lead_status;
        if (TextUtils.isEmpty(currentLeadStatus) || currentLeadStatus.equalsIgnoreCase(getString(R.string.txt_select_status))) {
            Utils.showToast(this, getString(R.string.txt_select_status_choose));
            return;
        }
        currentLeadStatusId = lead_status_id;
        leadStatusDateAndTime = tv_date_val_self.getText().toString() + " " + tv_time_val_self.getText().toString();
        if (TextUtils.isEmpty(tv_date_val_self.getText().toString()) || TextUtils.isEmpty(tv_time_val_self.getText().toString())) {
            Utils.showToast(this, "Choose Date and Time");
            return;
        }
        no_of_persons = String.valueOf(noOfPersons);
        if (layout_no_of_persons_self.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(no_of_persons)) {
                Utils.showToast(this, "Please Choose No Persons");
                return;
            }
        }
       /* address = tv_address_val_self.getText().toString();
        if (layout_address.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(address)) {
                Utils.showToast(this, "Please Enter Address");
                return;
            }
        }*/

       //uncomment to above code to make address mandatory.
        currentLeadType = leadType;
        if (TextUtils.isEmpty(currentLeadType)) {
            Utils.showToast(this, "Choose Lead Type");
            return;
        }
        remark = edit_text_remark.getText().toString();
        if (TextUtils.isEmpty(remark)) {
            Utils.showToast(this, getString(R.string.enter_remark));
            return;
        }
        activity_id = "";
        activity_name = "";

        AppointmentModel model = new AppointmentModel();
        model.setType(appointmentType);
        model.setCustomer_name(customerName);

        model.setEmail_id(emailId);
        model.setMobile_no(mobileNo);
        model.setProject(selectedProjectId);
        model.setAlternate_no(alternateMobileNo);
        model.setBudget(mMinBudget + "-" + mMaxBudget);
        model.setStatus_title(currentLeadStatus);
        model.setStatus_id(currentLeadStatusId);
        model.setDate_time(leadStatusDateAndTime);
        model.setRemark(remark);
        model.setAddress(address);
        model.setLead_type(currentLeadType);
        model.setNo_of_persons(no_of_persons);
        model.setActivity_name(activity_name);
        model.setActivity_id(activity_id);
        addAppointment(model);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void addAppointment(final AppointmentModel updateData) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        if (!isFinishing())
            dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.ADD_LEADS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success")) {
                                Utils.showToast(AddAppointmentActivity.this, jsonObject.optString("message"));
                                app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                                app.saveTabIntoPrefs(getString(R.string.key_navigation), getString(R.string.tab_follow_up));
                                onBackPressed();
                            } else {
                                Utils.showToast(AddAppointmentActivity.this, getString(R.string.something_went_wrong));
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
                        if (!Connectivity.isConnected(AddAppointmentActivity.this)) {
                            app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                            app.saveTabIntoPrefs(getString(R.string.key_navigation), getString(R.string.tab_follow_up));
                            onBackPressed();
                        }
                        Log.d(TAG, "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                   String builderId = BMHConstants.CURRENT_BUILDER_ID;
                    String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
                params.put(ParamsConstants.BUILDER_ID, builderId);
                params.put(ParamsConstants.USER_ID, userId);
                params.put(ParamsConstants.USER_DESIGNATION, app.getFromPrefs(BMHConstants.USER_DESIGNATION));
                params.put("type", updateData.getType());
                params.put("customer_name", updateData.getCustomer_name());
                params.put("email_id", updateData.getEmail_id());
                params.put("mobile_no", updateData.getMobile_no());
                params.put("project", updateData.getProject());
                params.put("alternate_no", updateData.getAlternate_no());
                params.put("budget", updateData.getBudget());
                params.put("status_id", updateData.getStatus_id());
                params.put("status_title", updateData.getStatus_title());
                params.put("date_time", updateData.getDate_time());
                params.put("remark", updateData.getRemark());
                params.put("no_of_persons", updateData.getNo_of_persons());
                params.put("address", updateData.getAddress());
                params.put("lead_type", updateData.getLead_type());
                params.put("activity_id", updateData.getActivity_id());
                params.put("activity_name", updateData.getActivity_name());

                if (Connectivity.isConnected(AddAppointmentActivity.this))
                    isSynced = 0;
                AddAppointmentEntity appointEntity =
                        new AddAppointmentEntity(builderId, userId, updateData.getType(), updateData.getCustomer_name(),
                                "", updateData.getEmail_id(),
                                updateData.getMobile_no(), updateData.getProject(), updateData.getAlternate_no(),
                                updateData.getBudget(), updateData.getStatus_id(), updateData.getStatus_title(),
                                updateData.getDate_time(), updateData.getRemark(), updateData.getNo_of_persons(),
                                updateData.getAddress(), updateData.getLead_type(), updateData.getActivity_id(),
                                updateData.getActivity_name(), isSynced,
                                Utils.getCurrentDateTime());
                new InsertAddAppointmentTask(AddAppointmentActivity.this, appointEntity).execute();
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

    private void addBrokerMeeting() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BROKER_MEETING),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success")) {
                                Utils.showToast(AddAppointmentActivity.this, jsonObject.optString("message"));
                                app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                                app.saveTabIntoPrefs(getString(R.string.key_navigation), getString(R.string.tab_follow_up));
                                onBackPressed();
                            } else {
                                Utils.showToast(AddAppointmentActivity.this, getString(R.string.something_went_wrong));
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
                        if (!Connectivity.isConnected(AddAppointmentActivity.this)) {
                            app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                            app.saveTabIntoPrefs(getString(R.string.key_navigation), getString(R.string.tab_follow_up));
                            onBackPressed();
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
                params.put(ParamsConstants.BROKER_ID, brokerId);
                params.put("address", tv_broker_meeting_address_val.getText().toString());
                params.put("remark", tv_remark_val.getText().toString());
                params.put("datetime", tv_meeting_date_val.getText().toString() + " " + tv_meeting_time_val.getText().toString());

                if (Connectivity.isConnected(AddAppointmentActivity.this))
                    isSynced = 0;
                AddAppointmentEntity appointEntity =
                        new AddAppointmentEntity(builderId, userId, "broker_meeting", brokerName, brokerId, "",
                                "", "", "", "", "", "",
                                tv_meeting_date_val.getText().toString() + " " + tv_meeting_time_val.getText().toString(),
                                tv_remark_val.getText().toString(), "", tv_broker_meeting_address_val.getText().toString(),
                                "", "", "", isSynced, Utils.getCurrentDateTime());
                new InsertAddAppointmentTask(AddAppointmentActivity.this, appointEntity).execute();

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

    private void setUiData() {
        builderId = BMHConstants.CURRENT_BUILDER_ID;
        userId = app.getFromPrefs(BMHConstants.USERID_KEY);

        if (mBrokerList != null) {
            for (int i = 0; i < mBrokerList.size(); i++) {
                notInterestedModel = mBrokerList.get(i);
                spBrokerList.add(notInterestedModel.getTitle());
            }
        }
        spBrokerList.add(0, getString(R.string.txt_select_broker));
        brokerListAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spBrokerList);
        brokerListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_select_broker.setAdapter(brokerListAdapter);
        spinner_select_broker.setOnItemSelectedListener(this);


        if (corporateActivityList != null) {
            for (int i = 0; i < corporateActivityList.size(); i++) {
                corporateActivityModel = corporateActivityList.get(i);
                spCorporateActivityList.add(corporateActivityModel.getActivity_name());
            }
        }
        spCorporateActivityList.add(0, getString(R.string.txt_select_corporate_activity));
        corporateActivityAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spCorporateActivityList);
        corporateActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_corporate_activity.setAdapter(corporateActivityAdapter);
        spinner_corporate_activity.setOnItemSelectedListener(this);

        spLeadStatusList.add(0, getString(R.string.txt_select_status));
        spLeadStatusList.add(1, getString(R.string.txt_meeting));
        spLeadStatusList.add(2, getString(R.string.text_callback));
        spLeadStatusList.add(3, getString(R.string.txt_site_visit));
      // spLeadStatusList.add(4, getString(R.string.text_marketing_calls));
        leadStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLeadStatusList);
        leadStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_lead_status.setAdapter(leadStatusAdapter);
        spinner_lead_status.setOnItemSelectedListener(this);


        spCaLeadStatusList.add(0, getString(R.string.txt_select_status));
        spCaLeadStatusList.add(1, getString(R.string.txt_meeting));
        spCaLeadStatusList.add(2, getString(R.string.text_callback));
       spCaLeadStatusList.add(3, getString(R.string.txt_site_visit));
        spCaLeadStatusList.add(4, getString(R.string.text_not_interested));
         spCaLeadStatusList.add(5, getString(R.string.text_just_enquiry));
        leadCAStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spCaLeadStatusList);
        leadCAStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_lead_status_ca.setAdapter(leadCAStatusAdapter);
        spinner_lead_status_ca.setOnItemSelectedListener(this);


        String[] minArray = getResources().getStringArray(R.array.array_min_budget);
        String[] maxArray = getResources().getStringArray(R.array.array_max_budget);

        budgetMin.addAll(Arrays.asList(minArray));
        budgetMax.addAll(Arrays.asList(maxArray));
        budgetMin.add(0, getString(R.string.txt_min_budget));
        budgetMax.add(0, getString(R.string.txt_max_budget));
        mSpinnerMinBudget.setPadding(0, 0, 50, 0);


        minAdapter = new ArrayAdapter(AddAppointmentActivity.this, R.layout.textview_spinner, budgetMin);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerMinBudget.setAdapter(minAdapter);
        mSpinnerMinBudget.setOnItemSelectedListener(this);


        spinner_min_budget_ca.setAdapter(minAdapter);
        spinner_min_budget_ca.setOnItemSelectedListener(this);

        maxAdapter = new ArrayAdapter(AddAppointmentActivity.this, R.layout.textview_spinner, budgetMax);
        maxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerMaxBudget.setPadding(0, 0, 50, 0);
        mSpinnerMaxBudget.setAdapter(maxAdapter);
        mSpinnerMaxBudget.setOnItemSelectedListener(this);

        spinner_max_budget_ca.setAdapter(maxAdapter);
        spinner_max_budget_ca.setOnItemSelectedListener(this);

        ArrayList<Integer> personArray = new ArrayList<>();
        personArray.add(1);
        personArray.add(2);
        personArray.add(3);
        personArray.add(4);
        personArray.add(5);
        personArray.add(6);
        spinner_no_of_persons_self.setPadding(0, 0, 50, 0);
        personAdapter = new ArrayAdapter(AddAppointmentActivity.this, R.layout.textview_spinner, personArray);
        personAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_no_of_persons_self.setAdapter(personAdapter);
        spinner_no_of_persons_self.setOnItemSelectedListener(this);

        //set CA no of person spinner
        spinner_no_of_persons_ca.setAdapter(personAdapter);
        spinner_no_of_persons_ca.setOnItemSelectedListener(this);

        if (projectsList != null) {
            for (int i = 0; i < projectsList.size(); i++) {
                projects = projectsList.get(i);
                spProjectList.add(projects.getProject_name());
            }
        }
        spProjectList.add(0, getString(R.string.txt_select_project));
        projectListAdapeter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spProjectList);
        projectListAdapeter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_project_name.setAdapter(projectListAdapeter);
        spinner_project_name.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_select_broker:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                brokerName = parent.getItemAtPosition(position).toString();
                getBrokerId(brokerName);
                tv_broker_meeting_address_val.setText(brokerAddress);
                spinner_select_broker.setSelection(position);
                break;
            case R.id.spinner_corporate_activity:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                corporateActivity = parent.getItemAtPosition(position).toString();
                getCorporateActivityProjectList(corporateActivity);
                getCorporateActivityId(corporateActivity);
                spinner_corporate_activity.setSelection(position);
                break;
            case R.id.spinner_project_name_ca:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                corporateActivityProject = parent.getItemAtPosition(position).toString();
                getProjectId(corporateActivityProject);
                spinner_project_name_ca.setSelection(position);
                break;
            case R.id.spinner_min_budget_ca:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                Object objCaMin = parent.getItemAtPosition(position).toString();
                if (objCaMin.equals(getString(R.string.txt_min_budget)))
                    ca_min_budget = objCaMin.toString();
                else {
                    ca_min_budget = String.valueOf(Utils.convertStringToMoney(objCaMin.toString()));

                    if (!TextUtils.isEmpty(ca_max_budget) && !ca_max_budget.equals(getString(R.string.txt_max_budget))) {
                        if (Integer.valueOf(ca_min_budget) < Integer.valueOf(ca_max_budget)) {
                            spinner_min_budget_ca.setSelection(position);
                        } else {
                            Toast.makeText(AddAppointmentActivity.this, getString(R.string.toast_msg_min_budget_could_not_greater), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        spinner_min_budget_ca.setSelection(position);
                    }
                }

                break;
            case R.id.spinner_max_budget_ca:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
               /* ca_max_budget = parent.getItemAtPosition(position).toString();
                spinner_max_budget_ca.setSelection(position);*/

                Object objCaMax = parent.getItemAtPosition(position).toString();
                if (objCaMax.equals(getString(R.string.txt_max_budget)))
                    ca_max_budget = objCaMax.toString();
                else {
                    ca_max_budget = String.valueOf(Utils.convertStringToMoney(objCaMax.toString()));
                    if (Integer.valueOf(ca_min_budget) < Integer.valueOf(ca_max_budget)) {
                        spinner_max_budget_ca.setSelection(position);
                    } else {
                        // mSpinnerMaxBudget.setSelection(0);
                        Toast.makeText(AddAppointmentActivity.this, getString(R.string.toast_msg_max_budget_could_not_less), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.spinner_project_name:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                projectName = parent.getItemAtPosition(position).toString();
                getProjectId(projectName);
                spinner_project_name.setSelection(position);
                break;
            case R.id.spinner_lead_status:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                lead_status = parent.getItemAtPosition(position).toString();
                getLeadStatusId(lead_status);
                spinner_lead_status.setSelection(position);
                setDynamicViews(lead_status);
                break;
            case R.id.spinner_lead_status_ca:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                ca_lead_status = parent.getItemAtPosition(position).toString();
                getLeadStatusId(ca_lead_status);
                spinner_lead_status_ca.setSelection(position);
                setDynamicViews(ca_lead_status);
                break;
            case R.id.spinner_min_budget:
                if (isInitialMinBudget) {
                    position = defaultPosition;
                    isInitialMinBudget = false;
                }
                Object objMin = parent.getItemAtPosition(position).toString();
                if (objMin.equals(getString(R.string.txt_min_budget)))
                    mMinBudget = objMin.toString();
                else {
                    mMinBudget = String.valueOf(Utils.convertStringToMoney(objMin.toString()));
                    //  mSpinnerMinBudget.setSelection(position);

                    if (!TextUtils.isEmpty(mMaxBudget) && !mMaxBudget.equals(getString(R.string.txt_max_budget))) {
                        if (Integer.valueOf(mMinBudget) < Integer.valueOf(mMaxBudget)) {
                            mSpinnerMinBudget.setSelection(position);
                        } else {
                            // mSpinnerMinBudget.setSelection(0);
                            Toast.makeText(AddAppointmentActivity.this, getString(R.string.toast_msg_min_budget_could_not_greater), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mSpinnerMinBudget.setSelection(position);
                    }
                }
                break;

            case R.id.spinner_max_budget:
                if (isInitialMaxBudget) {
                    position = defaultPosition;
                    isInitialMaxBudget = false;
                }
                Object objMax = parent.getItemAtPosition(position).toString();
                if (objMax.equals(getString(R.string.txt_max_budget)))
                    mMaxBudget = objMax.toString();
                else {
                    mMaxBudget = String.valueOf(Utils.convertStringToMoney(objMax.toString()));
                    if (Integer.valueOf(mMinBudget) < Integer.valueOf(mMaxBudget)) {
                        mSpinnerMaxBudget.setSelection(position);
                    } else {
                        Toast.makeText(AddAppointmentActivity.this, getString(R.string.toast_msg_max_budget_could_not_less), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.spinner_no_of_persons_self:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                noOfPersons = (Integer) parent.getItemAtPosition(position);
                spinner_no_of_persons_self.setSelection(position);
                break;
            case R.id.spinner_no_of_persons_ca:
                if (isInitialSubStatus) {
                    position = defaultPosition;
                    isInitialSubStatus = false;
                }
                noOfPersonsCA = (Integer) parent.getItemAtPosition(position);
                spinner_no_of_persons_ca.setSelection(position);
                break;
        }
    }

    private void setDynamicViews(String leadType) {
        if (linearLayoutSelf.getVisibility() == View.VISIBLE) {
            if (leadType.equalsIgnoreCase(getString(R.string.txt_select_status))) {
                layout_select_lead.setVisibility(View.GONE);
            } else if (leadType.equalsIgnoreCase(getString(R.string.txt_meeting))) {
                layout_select_lead.setVisibility(View.VISIBLE);
                layout_address.setVisibility(View.VISIBLE);
                layout_lead_type_divider.setVisibility(View.VISIBLE);
                layout_no_of_persons_self.setVisibility(View.GONE);
                layout_no_of_persons_self_divider.setVisibility(View.GONE);
                tv_date_self.setText(getString(R.string.text_meeting_date));
                tv_time_self.setText(getString(R.string.txt_meeting_time));
                tv_address_self.setText(getString(R.string.txt_meeting_address));
            } else if (leadType.equalsIgnoreCase(getString(R.string.text_callback))) {
                layout_select_lead.setVisibility(View.VISIBLE);
                layout_address.setVisibility(View.GONE);
                layout_lead_type_divider.setVisibility(View.GONE);
                layout_no_of_persons_self.setVisibility(View.GONE);
                layout_no_of_persons_self_divider.setVisibility(View.GONE);
                tv_date_self.setText(getString(R.string.txt_callback_date));
                tv_time_self.setText(getString(R.string.txt_callback));
            } else if (leadType.equalsIgnoreCase(getString(R.string.txt_site_visit))) {
                layout_select_lead.setVisibility(View.VISIBLE);
                layout_address.setVisibility(View.VISIBLE);
                layout_lead_type_divider.setVisibility(View.VISIBLE);
                layout_no_of_persons_self.setVisibility(View.VISIBLE);
                layout_no_of_persons_self_divider.setVisibility(View.VISIBLE);
                tv_date_self.setText(getString(R.string.text_site_visit_date));
                tv_time_self.setText(getString(R.string.text_site_visit_time));
                tv_address_self.setText(getString(R.string.txt_pickup_address));
            }
        } else if (linearLayoutCorporate.getVisibility() == View.VISIBLE) {
            if (leadType.equalsIgnoreCase(getString(R.string.txt_select_status))) {
                layout_select_lead_ca.setVisibility(View.GONE);
            } else if (leadType.equalsIgnoreCase(getString(R.string.txt_meeting))) {
                layout_select_lead_ca.setVisibility(View.VISIBLE);
                layout_address_ca.setVisibility(View.VISIBLE);
                layout_date_ca.setVisibility(View.VISIBLE);
                layout_time_ca.setVisibility(View.VISIBLE);
                layout_time_divider_ca.setVisibility(View.VISIBLE);
                layout_address_divider_ca.setVisibility(View.VISIBLE);
                layout_lead_type_divider_ca.setVisibility(View.VISIBLE);
                layout_no_of_persons_ca_divider.setVisibility(View.VISIBLE);
                layout_lead_type_ca.setVisibility(View.VISIBLE);
                layout_lead_type_divider_ca.setVisibility(View.VISIBLE);
                layout_no_of_persons_ca.setVisibility(View.GONE);
                layout_no_of_persons_ca_divider.setVisibility(View.GONE);
                tv_date_ca.setText(getString(R.string.text_meeting_date));
                tv_time_ca.setText(getString(R.string.txt_meeting_time));
                tv_address_ca.setText(getString(R.string.txt_meeting_address));
            } else if (leadType.equalsIgnoreCase(getString(R.string.text_callback))) {
                layout_select_lead_ca.setVisibility(View.VISIBLE);
                layout_address_ca.setVisibility(View.GONE);
                layout_date_ca.setVisibility(View.VISIBLE);
                layout_time_ca.setVisibility(View.VISIBLE);
                layout_lead_type_ca.setVisibility(View.VISIBLE);
                layout_time_divider_ca.setVisibility(View.VISIBLE);
                layout_address_divider_ca.setVisibility(View.VISIBLE);
                layout_lead_type_divider_ca.setVisibility(View.VISIBLE);
                layout_no_of_persons_ca_divider.setVisibility(View.VISIBLE);
                layout_lead_type_divider_ca.setVisibility(View.GONE);
                layout_no_of_persons_ca.setVisibility(View.GONE);
                layout_no_of_persons_ca_divider.setVisibility(View.GONE);
                tv_date_ca.setText(getString(R.string.txt_callback_date));
                tv_time_ca.setText(getString(R.string.txt_callback));
            } else if (leadType.equalsIgnoreCase(getString(R.string.txt_site_visit))) {
                layout_select_lead_ca.setVisibility(View.VISIBLE);
                layout_address_ca.setVisibility(View.VISIBLE);
                layout_date_ca.setVisibility(View.VISIBLE);
                layout_time_ca.setVisibility(View.VISIBLE);
                layout_lead_type_ca.setVisibility(View.VISIBLE);
                layout_time_divider_ca.setVisibility(View.VISIBLE);
                layout_address_divider_ca.setVisibility(View.VISIBLE);
                layout_lead_type_divider_ca.setVisibility(View.VISIBLE);
                layout_no_of_persons_ca_divider.setVisibility(View.VISIBLE);
                layout_lead_type_divider_ca.setVisibility(View.VISIBLE);
                layout_no_of_persons_ca.setVisibility(View.VISIBLE);
                layout_no_of_persons_ca_divider.setVisibility(View.VISIBLE);
                tv_date_ca.setText(getString(R.string.text_site_visit_date));
                tv_time_ca.setText(getString(R.string.text_site_visit_time));
                tv_address_ca.setText(getString(R.string.txt_pickup_address));
            } else {
                layout_date_ca.setVisibility(View.GONE);
                layout_time_ca.setVisibility(View.GONE);
                layout_lead_type_ca.setVisibility(View.GONE);
                layout_address_ca.setVisibility(View.GONE);
                layout_no_of_persons_ca.setVisibility(View.GONE);
                layout_time_divider_ca.setVisibility(View.GONE);
                layout_address_divider_ca.setVisibility(View.GONE);
                layout_lead_type_divider_ca.setVisibility(View.GONE);
                layout_no_of_persons_ca_divider.setVisibility(View.GONE);
                layout_select_lead_ca.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getBrokerId(String selectedSubStatus) {
        if (!selectedSubStatus.equalsIgnoreCase(getString(R.string.txt_select_broker))) {
            for (int i = 0; i < mBrokerList.size(); i++) {
                notInterestedModel = mBrokerList.get(i);
                if (selectedSubStatus.equalsIgnoreCase(notInterestedModel.getTitle())) {
                    brokerId = notInterestedModel.getId();
                    brokerAddress = notInterestedModel.getAddress();
                    return;
                }
            }
        } else {
            brokerId = "";
        }
    }

    private void getProjectId(String selectedSubStatus) {
        if (!selectedSubStatus.equalsIgnoreCase(getString(R.string.txt_select_project))) {
            for (int i = 0; i < projectsList.size(); i++) {
                projects = projectsList.get(i);
                if (selectedSubStatus.equalsIgnoreCase(projects.getProject_name())) {
                    if (linearLayoutCorporate.getVisibility() == View.VISIBLE) {
                        corporateActivityProjectId = projects.getProject_id();
                    } else if (linearLayoutSelf.getVisibility() == View.VISIBLE) {
                        projectId = projects.getProject_id();
                    }
                    return;
                }
            }
        } else {
            projectId = "";
        }
    }

    private void getCorporateActivityProjectList(String selectedCorporateActivity) {
        if (!selectedCorporateActivity.equalsIgnoreCase(getString(R.string.txt_select_corporate_activity))) {
            for (int i = 0; i < corporateActivityList.size(); i++) {
                corporateActivityModel = corporateActivityList.get(i);
                if (selectedCorporateActivity.equalsIgnoreCase(corporateActivityModel.getActivity_name())) {
                    corporateActivityProjectsList = corporateActivityModel.getProject();
                    setSelectedCorporateActivityProject(corporateActivityProjectsList);
                    return;
                }
            }
        } else {
            setSelectedCorporateActivityProject(projectsList);
        }
    }

    private void getCorporateActivityId(String selectedCorporateActivity) {
        if (!selectedCorporateActivity.equalsIgnoreCase(getString(R.string.txt_select_corporate_activity))) {
            for (int i = 0; i < corporateActivityList.size(); i++) {
                corporateActivityModel = corporateActivityList.get(i);
                if (selectedCorporateActivity.equalsIgnoreCase(corporateActivityModel.getActivity_name())) {
                    corporateActivityId = corporateActivityModel.getActivity_id();
                    return;
                }
            }
        } else {
            corporateActivityId = "";
        }
    }

    private void setSelectedCorporateActivityProject(ArrayList<Projects> selectedCorporateActivityProjectsList) {
        if (spCorporateActivityProjectList.size() > 0) {
            spCorporateActivityProjectList.clear();
        }
        if (selectedCorporateActivityProjectsList != null) {
            for (int i = 0; i < selectedCorporateActivityProjectsList.size(); i++) {
                projects = selectedCorporateActivityProjectsList.get(i);
                spCorporateActivityProjectList.add(projects.getProject_name());
            }
        }
        spCorporateActivityProjectList.add(0, getString(R.string.select_project));
        corporateActivityProjectAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spCorporateActivityProjectList);
        corporateActivityProjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_project_name_ca.setAdapter(corporateActivityProjectAdapter);
        spinner_project_name_ca.setOnItemSelectedListener(this);
    }

    private void getLeadStatusId(String selectedLeadStatus) {
        if (!selectedLeadStatus.equalsIgnoreCase(getString(R.string.txt_select_project))) {
            for (int i = 0; i < mLeadStatusList.size(); i++) {
                leadStatus = mLeadStatusList.get(i);
                if (selectedLeadStatus.equalsIgnoreCase(leadStatus.getTitle())) {
                    if (linearLayoutSelf.getVisibility() == View.VISIBLE) {
                        lead_status_id = leadStatus.getDisposition_id();
                    } else if (linearLayoutCorporate.getVisibility() == View.VISIBLE) {
                        ca_lead_status_id = leadStatus.getDisposition_id();
                    }
                    return;
                }
            }
        } else {
            lead_status_id = "";
            ca_lead_status_id = "";
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_meeting_date_val:
                dateString = tv_meeting_date_val.getText().toString();
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

                fromDateDialog = new DatePickerDialog(AddAppointmentActivity.this, dateChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_meeting_time_val:
                if (TextUtils.isEmpty(tv_meeting_date_val.getText().toString())) {
                    Toast.makeText(AddAppointmentActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                    return;
                }
                timeString = tv_meeting_time_val.getText().toString().trim();
                if (!TextUtils.isEmpty(timeString)) {
                    String[] timeArray = timeString.split(":");
                    mHour = Integer.valueOf(timeArray[0]);
                    mMinute = Integer.valueOf(timeArray[1]);
                } else {
                    mHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    mMinute = currentCal.get(Calendar.MINUTE);
                }

                mTimePicker = new TimePickerDialog(AddAppointmentActivity.this, onTimeSetListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.tv_date_val_self:
                dateString = tv_date_val_self.getText().toString();
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

                fromDateDialog = new DatePickerDialog(AddAppointmentActivity.this, dateSelfChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_date_val_ca:
                dateString = tv_date_val_ca.getText().toString();
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

                fromDateDialog = new DatePickerDialog(AddAppointmentActivity.this, dateCorporateActivityChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_time_val_ca:
                if (TextUtils.isEmpty(tv_date_val_ca.getText().toString())) {
                    Toast.makeText(AddAppointmentActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                    return;
                }
                timeString = tv_time_val_ca.getText().toString();
                if (!TextUtils.isEmpty(timeString)) {
                    String[] timeArray = timeString.split(":");
                    mHour = Integer.valueOf(timeArray[0]);
                    mMinute = Integer.valueOf(timeArray[1]);
                } else {
                    mHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    mMinute = currentCal.get(Calendar.MINUTE);
                }

                mTimePicker = new TimePickerDialog(AddAppointmentActivity.this, onCorporateActivityTimeSetListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.tv_time_val_self:
                if (TextUtils.isEmpty(tv_date_val_self.getText().toString())) {
                    Toast.makeText(AddAppointmentActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                    return;
                }
                timeString = tv_time_val_self.getText().toString();
                if (!TextUtils.isEmpty(timeString)) {
                    String[] timeArray = timeString.split(":");
                    mHour = Integer.valueOf(timeArray[0]);
                    mMinute = Integer.valueOf(timeArray[1]);
                } else {
                    mHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    mMinute = currentCal.get(Calendar.MINUTE);
                }

                mTimePicker = new TimePickerDialog(AddAppointmentActivity.this, onSelfTimeSetListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.btn_submit_appointment:
                getAppointmentDetails();
        }
    }

    DatePickerDialog.OnDateSetListener dateChangeListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            setSelectedDate(year, month, dayOfMonth, tv_meeting_date_val);
        }
    };
    DatePickerDialog.OnDateSetListener dateSelfChangeListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setSelectedDate(year, month, dayOfMonth, tv_date_val_self);
        }
    };
    DatePickerDialog.OnDateSetListener dateCorporateActivityChangeListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setSelectedDate(year, month, dayOfMonth, tv_date_val_ca);
        }
    };

    private void setSelectedDate(int year, int month, int dayOfMonth, TextView textview) {
        selectedCal.set(Calendar.YEAR, year);
        selectedCal.set(Calendar.MONTH, month);
        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate = currentCal.getTime();
        selectedDate = selectedCal.getTime();
        if (selectedCal.getTimeInMillis() >= currentCal.getTimeInMillis()) {
            CharSequence charSequence = simpleDateFormat.format(selectedDate);
            textview.setText(charSequence);
            //     mDateText = charSequence.toString();
            if (selectedCal.getTimeInMillis() == currentCal.getTimeInMillis()) {
                mTimeText = "";
                if (mSelectedRadioButton.equalsIgnoreCase(getString(R.string.txt_self)))
                    tv_time_val_self.setText("");
                if (mSelectedRadioButton.equalsIgnoreCase(getString(R.string.txt_with_broker)))
                    tv_meeting_time_val.setText("");
                if (mSelectedRadioButton.equalsIgnoreCase(getString(R.string.txt_corporate_activity)))
                    tv_time_val_ca.setText("");
            }
        } else {
            Toast.makeText(AddAppointmentActivity.this, getString(R.string.date_picker_error), Toast.LENGTH_LONG).show();
        }
    }

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
            Toast.makeText(AddAppointmentActivity.this, getString(R.string.invalid_time_picker_error), Toast.LENGTH_SHORT).show();
        }
    }

         TimePickerDialog.OnTimeSetListener onCorporateActivityTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentDate = currentCal.getTime();
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            setSelectedTime(hourOfDay, minute, tv_time_val_ca);
        }
    };

         TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentDate = currentCal.getTime();
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            setSelectedTime(hourOfDay, minute, tv_meeting_time_val);
        }
    };
    TimePickerDialog.OnTimeSetListener onSelfTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            currentDate = currentCal.getTime();
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            setSelectedTime(hourOfDay, minute, tv_time_val_self);
        }
    };

    @Override
    public void onBackPressed() {
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
}
