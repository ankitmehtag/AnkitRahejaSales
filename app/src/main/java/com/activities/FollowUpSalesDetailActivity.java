package com.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
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
import android.widget.RelativeLayout;
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
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.database.UpdateStatusConverter;
import com.database.entity.ClosureMasterEntity;
import com.database.entity.CustomerInfoEntity;
import com.database.entity.NotInterestedMasterEntity;
import com.database.entity.SalesBrokerMasterEntity;
import com.database.entity.SalesClosureLeadsDetailEntity;
import com.database.entity.StatusMasterEntity;
import com.database.task.CustomerInfoTask;
import com.database.task.GetMultipleProjectTask;
import com.database.task.GetSalesLeadDetailTask;
import com.database.task.InsertClosureLeadsDetailTask;
import com.database.task.InsertMultiSelectTask;
import com.database.task.InsertSalesLeadDetailsTask;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusClosureEntity;
import com.database.entity.SubStatusMeetingEntity;
import com.database.entity.SubStatusNotInterestedEntity;
import com.database.entity.SubStatusSiteVisitEntity;
import com.database.task.InsertSubStatusCallbackTask;
import com.database.task.InsertSubStatusClosureTask;
import com.database.task.InsertSubStatusMeetingTask;
import com.database.task.InsertSubStatusNotInterestedTask;
import com.database.task.InsertSubStatusSiteVisitTask;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.AsmSalesLeadDetailModel;
import com.model.AsmSalesModel;
import com.model.LeadStatus;
import com.model.NotInterestedLead;
import com.model.NotificationDataModel;
import com.model.Projects;
import com.model.SelectableProject;
import com.model.SubStatus;
import com.services.AlarmJobServices;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

                  public class FollowUpSalesDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
                View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnTouchListener,
                  GetMultipleProjectTask.IMultiProjCommunicator,GetSalesLeadDetailTask.ISalesLeadDetailCommunicator,
                MultiSelectAdapter.OnItemSelectedListener,
                  GetSalesLeadDetailTask.ISalesLeadCommunicator {

    private static final String TAG = FollowUpSalesDetailActivity.class.getSimpleName();
    private AsmSalesModel mAsmModel;
    private AsmSalesLeadDetailModel mDetailsModel;
    private Projects projectsModel;
    private LeadStatus leadStatusModel;
    private SubStatus subStatusModel;
    private NotInterestedLead notInterestedModel;
    private Spinner mProjectsSpinner, mSpinnerClosureProject, mLeadStatusSpinner, spinner_sub_status, mLocationSpinner, spinner_assign_to_broker;
    private LinearLayout layout_Assigned;
    private Button button_update;
    private ArrayList<String> spClosureProjectList = new ArrayList<>();
    private ArrayList<String> spLeadStatusList = new ArrayList<>();
    private ArrayList<String> spSubStatusList = new ArrayList<>();
    private ArrayList<String> spNotInterestedList = new ArrayList<>();
    private ArrayList<String> spClosureList = new ArrayList<>();
    private ArrayList<String> spProjectList = new ArrayList<>();
    private ArrayList<String> spBrokerList = new ArrayList<>();
    private List<Projects> mProjectsList;
    private List<Projects> mSelectedMultiProjList;
    private List<NotInterestedLead> mNotInterestedList;
    private List<NotInterestedLead> mClosureList;
    private List<NotInterestedLead> mBrokerList;
    private ArrayList<SubStatus> updateStatusList;
    private ArrayList<LeadStatus> mLeadStatusList;
    ArrayAdapter locationAdapter, noOfPersonAdapter;
    MultiSelectAdapter mAdapter;
    private CustomSpinnerAdapter closureProjectAdapter, leadStatusAdapter, brokerListAdapter, subStatusAdapter, notInterestedAdapter, closureAdapter;
    private ProgressDialog dialog;
    private BMHApplication app;
    private int hasExtCallPermission;
    private List<String> permissionsCall = new ArrayList<>();
    private int defaultPosSubStatus = 0;
    private int defaultPosBroker;
    private int defaultPosProject = 0, alarmIndex, updateFromAlarmNotification = 0;
    private int defaultPosLeadStatus;
    private boolean isInitial = false;
    private boolean isInitialProject = false;
    private boolean isInitialSubStatus = false;
    private boolean isInitialBrokerName = false;
    private boolean isInitialLeadStatus = false;
    private boolean isMultiSpinner = false;
    LinearLayout layout_lead_type, layout_address, layout_location, layout_time, layout_date, layout_last_updated_on;
    ConstraintLayout layout_select_lead, layout_callback_update, layout_meeting_update, layout_site_visit_update, layout_closure_update, layout_not_interested_update;
    AlertDialog.Builder alertDialog;
    LinearLayout layout_date_divider, layout_time_divider, layout_location_divider, linear_layout_budget_divider, linear_layout_budget,
            layout_address_divider, layout_last_updated_on_divider, layout_lead_type_divider;
    TextView tv_date, tv_date_val, tv_time, tv_time_val, tv_project_name, tv_select_location, tv_address,
            tv_campaign_val, tv_campaign_date_val, tv_mobile_no_val, tv_budget_val,
            tv_last_updated_on_val, textView_projects, tv_new_email_id_val;
    EditText tv_customer_name_val, tv_alternate_mobile_no_val, tv_address_val;
    RadioButton radioButton_hot, radioButton_warm, radioButton_cold;
    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText edtRemark;
    private int mPersonCount = 0;
    private int defaultPos = 0;
    private Calendar selectedCal;
    private Calendar currentCal;
    Date currentDate = null, selectedDate = null;
    final String dateFormat = Utils.dateFormat;
    LinearLayout linear_layout_email, tv_email_id_divider, tv_campaign_id_divider, layout_budget_spinner,
            linear_layout_campaign_date, tv_alternate_mobile_no_divider, linear_layout_alternate_mobile, linear_layout_campaign_name;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    private LinkedList<Projects> selectedProjList = new LinkedList<>();
    Set<Projects> set = null;
    RelativeLayout rl_callback, rl_meeting, rl_site_visit, rl_closure, rl_not_interested;
    ImageView iv_callback_plus, iv_meeting_plus, iv_site_visit_plus, iv_closure_plus, iv_not_interested_plus;
    LinearLayout linearLayoutSubStatus, linear_layout_next_status, layout_tv_project_name, layout_tv_project_name_divider,
            layout_sub_status_divider;
    int mYear = 0, mMonth = 0, mDay = 0;
    int mHour = 0, mMinute = 0;
    DatePickerDialog fromDateDialog;
    TimePickerDialog mTimePicker;
    //For Callback status
    TextView tv_callback_date_val_new, tv_callback_time_val_new;
    EditText edit_text_remark_callback_new;
    RadioGroup radioGroupCallback;
    RadioButton radioButton_hot_callback_new, radioButton_warm_callback_new, radioButton_cold_callback_new;
    //For Meeting status
    TextView tv_meeting_date_val_new, tv_meeting_time_val_new;
    EditText tv_meeting_address_val_new, edit_text_remark_new_meeting;
    RadioGroup radioGroupMeeting;
    RadioButton radioButton_hot_meeting, radioButton_warm_meeting, radioButton_cold_meeting;
    //For Site Visits status
    TextView tv_site_visit_date_val, tv_site_visit_time_val;
    Spinner spinner_site_visit_select_person_new;
    EditText tv_site_visit_address_val, edit_text_remark_site_visit;
    RadioGroup radioGroupSiteVisit;
    RadioButton radioButton_hot_site_visit, radioButton_warm_site_visit, radioButton_cold_site_visit;
    //For Not Interested status
    Spinner spinner_select_not_interested_new;
    RadioGroup radioGroupNotInterested;
    RadioButton radioButton_hot_not_interested_new, radioButton_warm_not_interested_new, radioButton_cold_not_interested_new;
    EditText edit_text_remark_not_interested_new;
    //For Not Closure status
    Spinner spinner_select_closure_new;
    RadioGroup radioGroupClosure;
    RadioButton radioButton_hot_closure_new, radioButton_warm_closure_new, radioButton_cold_closure_new;
    EditText edit_text_remark_closure_new, tv_cheque_number_val, tv_bank_name_val, tv_amount_val, tv_unit_no_val, tv_tower_no_val;
    View convertView;
    private String mSelectedProjectId = "", mClosureProjectId = "", mobileNo = "", mPersonCountString = "";
    private String mClosureProjectName = "", mSelectedProjectName = "", mSelectedLeadStatus = "", mSelectedRadioButton = "";
    private String mSelectedTab = "", mTimeText = "", strBudgetRange = "", tagReceivedFrom = "", enquiryId = "",
            dateString = "", timeString = "";
    private String update_status_id = "", update_status = "", new_status_id = "0", new_lead_status = "",
            new_remark = "", new_lead_type = "", new_address = "", new_date = "", no_of_persons = "0", new_time = "",
            new_sub_status = "", new_sub_status_id = "";
    private String sub_status_not_interested = "", sub_status_closure = "", sub_status_not_interested_id = "",
            sub_status_closure_id = "";
    private String lead_type_callback = "", lead_type_meeting = "", lead_type_site_visit = "",
            lead_type_closure = "", lead_type_not_interested = "", mRecFilePath = "";
    private String userDesignation = "", brokerName = "", brokerId = "", selectedImagePath = "";
    private String cheque_no = "", bank_name = "", amount = "", unit_no = "", cheque_date = "",
            tower_no = "";
    ArrayList<Projects> projectsList = new ArrayList<>();
    ArrayList<LeadStatus> leadsList = new ArrayList<>();
    ArrayList<NotInterestedLead> notInterestedList = new ArrayList<>();
    ArrayList<NotInterestedLead> closureList = new ArrayList<>();
    LinearLayout tv_assign_to_broker_separator, layout_broker_list, layout_select_sub_status;
    TextView tv_bank_name, tv_cheque_number, tv_cheque_date, tv_cheque_date_val_closure;
    Button btn_upload_cheque_or_dd;
    private int hasExtStoragePermission;
    private int hasExtCameraPermission;
    private List<String> permissions = new ArrayList<>();
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    Bitmap bitmap;
    ImageView cheque_preview;
    private Calendar SELECTED_CALENDER;
    private int mSelectedDay, mSelectedMonth, mSelectedYear;
    private DateFormat df_ddMMyyyy = null;
    android.app.DatePickerDialog datePicker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asm_sales_lead_details);
        layout_Assigned = findViewById(R.id.layout_button_update);
        button_update = findViewById(R.id.button_update);
        btn_upload_cheque_or_dd = findViewById(R.id.btn_upload_cheque_or_dd);
        cheque_preview = findViewById(R.id.cheque_preview);

        btn_upload_cheque_or_dd = findViewById(R.id.btn_upload_cheque_or_dd);
        cheque_preview = findViewById(R.id.cheque_preview);

        mProjectsSpinner = findViewById(R.id.spinner_project_name);
        mLeadStatusSpinner = findViewById(R.id.spinner_lead_status);
        mLocationSpinner = findViewById(R.id.spinner_select_location);
        spinner_assign_to_broker = findViewById(R.id.spinner_assign_to_broker);

        layout_last_updated_on = findViewById(R.id.layout_last_updated_on);
        tv_last_updated_on_val = findViewById(R.id.tv_last_updated_on_val);
        layout_lead_type = findViewById(R.id.layout_lead_type);
        layout_address = findViewById(R.id.layout_address);
        layout_location = findViewById(R.id.layout_location);
        layout_time = findViewById(R.id.layout_time);
        layout_date = findViewById(R.id.layout_date);

        linear_layout_budget_divider = findViewById(R.id.linear_layout_budget_divider);
        linear_layout_budget = findViewById(R.id.linear_layout_budget);
        layout_tv_project_name_divider = findViewById(R.id.layout_tv_project_name_divider);
        layout_sub_status_divider = findViewById(R.id.layout_sub_status_divider);
        layout_last_updated_on_divider = findViewById(R.id.layout_last_updated_on_divider);
        layout_date_divider = findViewById(R.id.layout_date_divider);
        layout_time_divider = findViewById(R.id.layout_time_divider);
        layout_location_divider = findViewById(R.id.layout_location_divider);
        layout_address_divider = findViewById(R.id.layout_address_divider);
        layout_lead_type_divider = findViewById(R.id.layout_lead_type_divider);
        layout_tv_project_name = findViewById(R.id.layout_tv_project_name);
        layout_select_lead = findViewById(R.id.layout_select_lead);
        layout_callback_update = findViewById(R.id.layout_callback_update);
        layout_meeting_update = findViewById(R.id.layout_meeting_update);
        layout_site_visit_update = findViewById(R.id.layout_site_visit_update);
        layout_closure_update = findViewById(R.id.layout_closure_update);
        layout_not_interested_update = findViewById(R.id.layout_not_interested_update);
        linear_layout_email = findViewById(R.id.linear_layout_email);
        tv_email_id_divider = findViewById(R.id.tv_email_id_divider);
        tv_campaign_id_divider = findViewById(R.id.tv_campaign_id_divider);
        linear_layout_campaign_date = findViewById(R.id.linear_layout_campaign_date);
        tv_alternate_mobile_no_divider = findViewById(R.id.tv_alternate_mobile_no_divider);
        linear_layout_alternate_mobile = findViewById(R.id.linear_layout_alternate_mobile);
        linear_layout_campaign_name = findViewById(R.id.linear_layout_campaign_name);
        layout_budget_spinner = findViewById(R.id.layout_budget_spinner);
        linearLayoutSubStatus = findViewById(R.id.layout_sub_status);
        linear_layout_next_status = findViewById(R.id.linear_layout_next_status);
        tv_assign_to_broker_separator = findViewById(R.id.tv_assign_to_broker_separator);
        layout_broker_list = findViewById(R.id.layout_broker_list);
        layout_select_sub_status = findViewById(R.id.layout_select_sub_status);

        tv_campaign_val = findViewById(R.id.tv_campaign_val);
        tv_campaign_date_val = findViewById(R.id.tv_campaign_date_val);
        tv_customer_name_val = findViewById(R.id.tv_customer_name_val);
        tv_new_email_id_val = findViewById(R.id.tv_new_email_id_val);
        tv_mobile_no_val = findViewById(R.id.tv_mobile_no_val);
        tv_alternate_mobile_no_val = findViewById(R.id.tv_alternate_mobile_no_val);
        tv_cheque_number = findViewById(R.id.tv_cheque_number);
        tv_bank_name = findViewById(R.id.tv_bank_name);
        tv_cheque_date = findViewById(R.id.tv_cheque_date);
        tv_cheque_date_val_closure = findViewById(R.id.tv_cheque_date_val_closure);

        textView_projects = findViewById(R.id.textView_projects);
        tv_date = findViewById(R.id.tv_date);
        tv_date_val = findViewById(R.id.tv_date_val);
        tv_time = findViewById(R.id.tv_time);
        spinner_sub_status = findViewById(R.id.spinner_sub_status);
        tv_time_val = findViewById(R.id.tv_time_val);
        tv_project_name = findViewById(R.id.tv_project_name);
        tv_select_location = findViewById(R.id.tv_select_location);
        tv_address = findViewById(R.id.tv_address);
        tv_address_val = findViewById(R.id.tv_address_val);
        tv_budget_val = findViewById(R.id.tv_budget_val);
        tv_address_val.setFocusableInTouchMode(true);
        edtRemark = findViewById(R.id.edit_text_remark);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_hot = findViewById(R.id.radioButton_hot);
        radioButton_warm = findViewById(R.id.radioButton_warm);
        radioButton_cold = findViewById(R.id.radioButton_cold);
        rl_callback = findViewById(R.id.rl_callback);
        rl_meeting = findViewById(R.id.rl_meeting);
        rl_site_visit = findViewById(R.id.rl_site_visit);
        rl_closure = findViewById(R.id.rl_closure);
        rl_not_interested = findViewById(R.id.rl_not_interested);
        iv_callback_plus = findViewById(R.id.iv_callback_plus);
        iv_meeting_plus = findViewById(R.id.iv_meeting_plus);
        iv_site_visit_plus = findViewById(R.id.iv_site_visit_plus);
        iv_closure_plus = findViewById(R.id.iv_closure_plus);
        iv_not_interested_plus = findViewById(R.id.iv_not_interested_plus);


        //for Callback status update Views Id
        tv_callback_date_val_new = findViewById(R.id.tv_callback_date_val_new);
        tv_callback_time_val_new = findViewById(R.id.tv_callback_time_val_new);
        edit_text_remark_callback_new = findViewById(R.id.edit_text_remark_callback_new);
        radioGroupCallback = findViewById(R.id.radioGroupCallback);
        radioButton_hot_callback_new = findViewById(R.id.radioButton_hot_callback_new);
        radioButton_warm_callback_new = findViewById(R.id.radioButton_warm_callback_new);
        radioButton_cold_callback_new = findViewById(R.id.radioButton_cold_callback_new);

        //for meeting status update Views Id
        tv_meeting_date_val_new = findViewById(R.id.tv_meeting_date_val_new);
        tv_meeting_time_val_new = findViewById(R.id.tv_meeting_time_val_new);
        tv_meeting_address_val_new = findViewById(R.id.tv_meeting_address_val_new);
        edit_text_remark_new_meeting = findViewById(R.id.edit_text_remark_new_meeting);
        radioGroupMeeting = findViewById(R.id.radioGroupMeeting);
        radioButton_hot_meeting = findViewById(R.id.radioButton_hot_meeting);
        radioButton_warm_meeting = findViewById(R.id.radioButton_warm_meeting);
        radioButton_cold_meeting = findViewById(R.id.radioButton_cold_meeting);

        //for meeting status update Views Id
        tv_site_visit_date_val = findViewById(R.id.tv_site_visit_date_val);
        tv_site_visit_time_val = findViewById(R.id.tv_site_visit_time_val);
        tv_site_visit_address_val = findViewById(R.id.tv_site_visit_address_val);
        edit_text_remark_site_visit = findViewById(R.id.edit_text_remark_site_visit);
        radioGroupSiteVisit = findViewById(R.id.radioGroupSiteVisit);
        radioButton_hot_site_visit = findViewById(R.id.radioButton_hot_site_visit);
        radioButton_warm_site_visit = findViewById(R.id.radioButton_warm_site_visit);
        radioButton_cold_site_visit = findViewById(R.id.radioButton_cold_site_visit);
        spinner_site_visit_select_person_new = findViewById(R.id.spinner_site_visit_select_person_new);
        ArrayList<String> personArrayNew = new ArrayList<>();
        personArrayNew.add(getString(R.string.txt_select_no_of_person));
        personArrayNew.add("1");
        personArrayNew.add("2");
        personArrayNew.add("3");
        personArrayNew.add("4");
        personArrayNew.add("5");
        personArrayNew.add("6");
        //spinner_site_visit_select_person_new.setPadding(0, 0, 50, 0);
        noOfPersonAdapter = new ArrayAdapter<>(FollowUpSalesDetailActivity.this, R.layout.textview_spinner, personArrayNew);
        noOfPersonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_site_visit_select_person_new.setAdapter(noOfPersonAdapter);

        //for not interested status update Views Id
        edit_text_remark_not_interested_new = findViewById(R.id.edit_text_remark_not_interested_new);
        radioGroupNotInterested = findViewById(R.id.radioGroupNotInterested);
        radioButton_hot_not_interested_new = findViewById(R.id.radioButton_hot_not_interested_new);
        radioButton_warm_not_interested_new = findViewById(R.id.radioButton_warm_not_interested_new);
        radioButton_cold_not_interested_new = findViewById(R.id.radioButton_cold_not_interested_new);
        spinner_select_not_interested_new = findViewById(R.id.spinner_select_not_interested_new);
        spNotInterestedList.add(0, getString(R.string.spinner_select));
        notInterestedAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spNotInterestedList);
        notInterestedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_select_not_interested_new.setAdapter(notInterestedAdapter);
        spinner_select_not_interested_new.setOnItemSelectedListener(this);

        //for closure status update Views Id
        edit_text_remark_closure_new = findViewById(R.id.edit_text_remark_closure_new);
        tv_cheque_number_val = findViewById(R.id.tv_cheque_number_val);
        tv_bank_name_val = findViewById(R.id.tv_bank_name_val);
        tv_amount_val = findViewById(R.id.tv_amount_val);
        tv_unit_no_val = findViewById(R.id.tv_unit_no_val);
        tv_tower_no_val = findViewById(R.id.tv_tower_no_val);
        radioGroupClosure = findViewById(R.id.radioGroupClosure);
        radioButton_hot_closure_new = findViewById(R.id.radioButton_hot_closure_new);
        radioButton_warm_closure_new = findViewById(R.id.radioButton_warm_closure_new);
        radioButton_cold_closure_new = findViewById(R.id.radioButton_cold_closure_new);
        spinner_select_closure_new = findViewById(R.id.spinner_select_closure_new);
        spClosureList.add(0, getString(R.string.spinner_select));
        closureAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spClosureList);
        closureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_select_closure_new.setAdapter(closureAdapter);
        spinner_select_closure_new.setOnItemSelectedListener(this);

        mSpinnerClosureProject = findViewById(R.id.spinner_closure_project_name);
        spClosureProjectList.add(0, getString(R.string.spinner_select_any_project));
        closureProjectAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spClosureProjectList);
        closureProjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerClosureProject.setAdapter(closureProjectAdapter);
        mSpinnerClosureProject.setOnItemSelectedListener(this);


        mLeadStatusSpinner.setEnabled(false);
        mLocationSpinner.setEnabled(false);

        tv_date_val.setOnClickListener(this);
        tv_time_val.setOnClickListener(this);
        radioButton_hot.setOnClickListener(this);
        radioButton_warm.setOnClickListener(this);
        radioButton_cold.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroupCallback.setOnCheckedChangeListener(this);
        radioGroupMeeting.setOnCheckedChangeListener(this);
        radioGroupSiteVisit.setOnCheckedChangeListener(this);
        radioGroupNotInterested.setOnCheckedChangeListener(this);
        radioGroupClosure.setOnCheckedChangeListener(this);
        button_update.setOnClickListener(this);
        //button_history.setOnClickListener(this);
        btn_upload_cheque_or_dd.setOnClickListener(this);
        rl_callback.setOnClickListener(this);
        rl_meeting.setOnClickListener(this);
        rl_site_visit.setOnClickListener(this);
        rl_closure.setOnClickListener(this);
        rl_not_interested.setOnClickListener(this);
        tv_callback_date_val_new.setOnClickListener(this);
        tv_site_visit_date_val.setOnClickListener(this);
        tv_callback_time_val_new.setOnClickListener(this);
        tv_site_visit_time_val.setOnClickListener(this);
        tv_meeting_date_val_new.setOnClickListener(this);
        tv_meeting_time_val_new.setOnClickListener(this);
        tv_cheque_date_val_closure.setOnClickListener(this);
        tv_mobile_no_val.setOnClickListener(this);

        initView();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initView() {
        app = (BMHApplication) getApplication();
        userDesignation = app.getFromPrefs((BMHConstants.USER_DESIGNATION));
        if (userDesignation.equalsIgnoreCase("0")) {
            layout_broker_list.setVisibility(View.VISIBLE);
            tv_assign_to_broker_separator.setVisibility(View.VISIBLE);
        }
        df_ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // cal.add(Calendar.DAY_OF_YEAR, -7); // to get one week ego date
        calendar.add(Calendar.MONTH, -2); // to get two month ego date

        SELECTED_CALENDER = calendar;
        mSelectedYear = SELECTED_CALENDER.get(Calendar.YEAR);
        mSelectedMonth = SELECTED_CALENDER.get(Calendar.MONTH);
        mSelectedDay = SELECTED_CALENDER.get(Calendar.DAY_OF_MONTH);
        datePicker = new android.app.DatePickerDialog(this, onDateSet, mSelectedYear, mSelectedMonth, mSelectedDay);
        datePicker.getDatePicker().setMinDate(SELECTED_CALENDER.getTimeInMillis());
        Intent intent = getIntent();
        if (intent != null) {
            isInitialProject = true;
            isInitialLeadStatus = true;
            isInitialBrokerName = true;
            isInitial = true;
            selectedCal = Calendar.getInstance();
            currentCal = Calendar.getInstance();
            tagReceivedFrom = intent.getStringExtra(BMHConstants.PATH);
            if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                String enquiryId = intent.getStringExtra(BMHConstants.ENQUIRY_ID);
                alarmIndex = intent.getIntExtra(BMHConstants.ALARM_INDEX, 0);
                updateFromAlarmNotification = 1;
                //  getLeadDetails(enquiryId, userDesignation);
                if (Connectivity.isConnected(this)) {
                    getLeadDetails(enquiryId, userDesignation);
                } else {
                    // GET OFFLINE DATA FROM DB
                    if (!TextUtils.isEmpty(enquiryId))
                        new GetSalesLeadDetailTask(this, enquiryId, "").execute();
                }
                return;
            } else if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(AlertDialogActivity.TAG)) {
                mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
                enquiryId = intent.getStringExtra(BMHConstants.ENQUIRY_ID);
                mRecFilePath = intent.getStringExtra(BMHConstants.RECORDING_FILE_PATH);
                //  getLeadDetails(enquiryId, userDesignation);

                if (Connectivity.isConnected(this)) {
                    getLeadDetails(enquiryId, userDesignation);
                } else {
                    // GET OFFLINE DATA FROM DB
                    if (!TextUtils.isEmpty(enquiryId))
                        new GetSalesLeadDetailTask(this, enquiryId, "").execute();
                }
                return;
            } else {
                mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
                if (mSelectedTab.equalsIgnoreCase("closure")) {
                    button_update.setVisibility(View.GONE);
                    layout_select_lead.setVisibility(View.GONE);
                }
                mAsmModel = intent.getParcelableExtra(BMHConstants.ASM_MODEL_DATA);
                enquiryId = mAsmModel.getEnquiry_id();
            }
            if (Connectivity.isConnected(this)) {
                mDetailsModel = intent.getParcelableExtra(BMHConstants.ASM_DETAIL_DATA);
                mLeadStatusList = intent.getParcelableArrayListExtra(BMHConstants.LEAD_LIST);
                mProjectsList = intent.getParcelableArrayListExtra(BMHConstants.PROJECT_LIST);
                mNotInterestedList = intent.getParcelableArrayListExtra(BMHConstants.NOT_INTERESTED_LIST);
                mClosureList = intent.getParcelableArrayListExtra(BMHConstants.CLOSURE_LIST);
                mBrokerList = intent.getParcelableArrayListExtra(BMHConstants.BROKER_LIST);
                setLeadDetails(mDetailsModel);
              /*  if (mDetailsModel == null && !TextUtils.isEmpty(enquiryId))
                    // GET OFFLINE DATA FROM DB
                    new GetSalesLeadDetailTask(this, enquiryId).execute();*/
            } else {
                // GET OFFLINE DATA FROM DB
                mSelectedTab = intent.getStringExtra(BMHConstants.SELECTED_TAB_NAME);
                mAsmModel = intent.getParcelableExtra(BMHConstants.ASM_MODEL_DATA);
                mDetailsModel = intent.getParcelableExtra(BMHConstants.ASM_DETAIL_DATA);
                mLeadStatusList = intent.getParcelableArrayListExtra(BMHConstants.LEAD_LIST);
                mProjectsList = intent.getParcelableArrayListExtra(BMHConstants.PROJECT_LIST);
                mNotInterestedList = intent.getParcelableArrayListExtra(BMHConstants.NOT_INTERESTED_LIST);
                mClosureList = intent.getParcelableArrayListExtra(BMHConstants.CLOSURE_LIST);
                mBrokerList = intent.getParcelableArrayListExtra(BMHConstants.BROKER_LIST);
                setLeadDetails(mDetailsModel);

               /* enquiryId = mAsmModel.getEnquiry_id();
                if (!TextUtils.isEmpty(enquiryId))
                    new GetSalesLeadDetailTask(this, enquiryId).execute();*/
            }
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
                            if (jsonObject.optString("message").equalsIgnoreCase("No Data Found"))
                                return;
                            JSONObject data = jsonObject.optJSONObject("data");
                            AsmSalesModel mDetailsModelNotification = (AsmSalesModel) JsonParser.convertJsonToBean(APIType.LEAD_DETAIL_ASM, data.toString());
                            mAsmModel = mDetailsModelNotification;
                            mDetailsModel = mDetailsModelNotification.getDetails();
                            JSONArray projectArray = jsonObject.optJSONArray("projects");
                            if (projectArray != null) {
                                int projectLength = projectArray.length();
                                for (int j = 0; j < projectLength; j++) {
                                    JSONObject obj = projectArray.optJSONObject(j);
                                    projectsList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                                    mProjectsList = projectsList;
                                }
                            }
                            JSONArray arrayLead = jsonObject.optJSONArray("lead_status");
                            if (arrayLead != null) {
                                int leadLength = arrayLead.length();
                                for (int j = 0; j < leadLength; j++) {
                                    JSONObject obj = arrayLead.optJSONObject(j);
                                    leadsList.add(new LeadStatus(obj.optString("disposition_id"), obj.optString("title")));
                                    mLeadStatusList = leadsList;
                                }
                            }
                            JSONArray notInterestedLead = jsonObject.optJSONArray("not_interested");
                            if (notInterestedLead != null) {
                                int leadLength = notInterestedLead.length();
                                for (int j = 0; j < leadLength; j++) {
                                    JSONObject obj = notInterestedLead.optJSONObject(j);
                                    notInterestedList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
                                    mNotInterestedList = notInterestedList;
                                }
                            }
                            JSONArray closureLead = jsonObject.optJSONArray("closure");
                            if (closureLead != null) {
                                int leadLength = closureLead.length();
                                for (int j = 0; j < leadLength; j++) {
                                    JSONObject obj = closureLead.optJSONObject(j);
                                    closureList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
                                    mClosureList = closureList;
                                }
                            }
                            JSONArray broker = jsonObject.optJSONArray("brokers");
                            if (broker != null) {
                                mBrokerList = new ArrayList<>();
                                int leadLength = broker.length();
                                ArrayList<NotInterestedLead> brokerList = new ArrayList<>();
                                for (int j = 0; j < leadLength; j++) {
                                    JSONObject obj = broker.optJSONObject(j);
                                    brokerList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
                                }
                                mBrokerList.addAll(brokerList);
                            }
                            setLeadDetails(mDetailsModel);
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

    private void setLeadDetails(AsmSalesLeadDetailModel mDetailsModel) {
        if (mDetailsModel != null) {
            init();
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.toolbar_txt_broker_status_and_type, mDetailsModel.getName(), mDetailsModel.getEnquiry_ID()));
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            tv_date_val.setText(mDetailsModel.getDate());
            tv_date_val.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tv_time_val.setText(mDetailsModel.getTime());
            tv_time_val.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            edtRemark.setText(mDetailsModel.getRemark());
            tv_address_val.setText(mDetailsModel.getAddress());

            //  mAssignToSpinner.setSelection(0, false);
            mSelectedMultiProjList = mDetailsModel.getSelectedProjList();
            updateStatusList = mDetailsModel.getSubStatusList();
            if (mSelectedMultiProjList != null) {
                for (int i = 0; i < mSelectedMultiProjList.size(); i++) {
                    projectsModel = mSelectedMultiProjList.get(i);
                    spProjectList.add(projectsModel.getProject_name());
                }
            }

            for (int i = 0; i < mLeadStatusList.size(); i++) {
                leadStatusModel = mLeadStatusList.get(i);
                spLeadStatusList.add(leadStatusModel.getTitle());
            }
            if (updateStatusList.size() == 0) {
                updateStatusList = new ArrayList<>();
                if (mDetailsModel.getCurrent_Status().equalsIgnoreCase(getString(R.string.txt_meeting))) {
                    updateStatusList.add(new SubStatus("1", "DONE"));
                    updateStatusList.add(new SubStatus("2", "NOT DONE"));
                }
                if (mDetailsModel.getCurrent_Status().equalsIgnoreCase(getString(R.string.txt_site_visit))) {
                    updateStatusList.add(new SubStatus("4", "DONE"));
                    updateStatusList.add(new SubStatus("5", "NOT DONE"));
                }

            }
            for (int i = 0; i < updateStatusList.size(); i++) {
                subStatusModel = updateStatusList.get(i);
                spSubStatusList.add(subStatusModel.getTitle());
            }

            for (int i = 0; i < mNotInterestedList.size(); i++) {
                notInterestedModel = mNotInterestedList.get(i);
                spNotInterestedList.add(notInterestedModel.getTitle());
            }
            for (int i = 0; i < mClosureList.size(); i++) {
                notInterestedModel = mClosureList.get(i);
                spClosureList.add(notInterestedModel.getTitle());
            }
            if (mBrokerList != null) {
                for (int i = 0; i < mBrokerList.size(); i++) {
                    notInterestedModel = mBrokerList.get(i);
                    spBrokerList.add(notInterestedModel.getTitle());
                }
            }
            if (mSelectedMultiProjList != null && mSelectedMultiProjList.size() == 0) {
                spProjectList.add(0, getString(R.string.spinner_select_project));
                textView_projects.setText(spProjectList.get(0));
                mSelectedProjectName = spProjectList.get(0);
            } else {
                String strMulti = Utils.getMultiSelectedProject(mSelectedMultiProjList);
                textView_projects.setText(strMulti);
                mSelectedProjectName = strMulti;
                mSelectedProjectId = Utils.getSelectedMultiProjectIds(mSelectedMultiProjList);
            }
            if (!mDetailsModel.getCurrent_Status().equalsIgnoreCase("callback")) {
                linearLayoutSubStatus.setVisibility(View.VISIBLE);
            } else {
                linear_layout_next_status.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(mAsmModel.getStatus()) || mAsmModel.getStatus().equalsIgnoreCase("")
                    || mAsmModel.getStatus().equalsIgnoreCase("NA")) {
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
            } else if (mAsmModel.getStatus().equalsIgnoreCase(getString(R.string.spinner_select_status))) {
                mAsmModel.setStatus("");
                spLeadStatusList.add(0, getString(R.string.spinner_select_status));
            } else {
                if (spLeadStatusList.size() == 0)
                    spLeadStatusList.add(0, mAsmModel.getStatus());
                getSelectedLeadStatus(spLeadStatusList, mAsmModel.getStatus());
            }

            if (!TextUtils.isEmpty(mSelectedProjectName)) {
                if (!mSelectedProjectName.equalsIgnoreCase(getString(R.string.spinner_select_project))) {
                    if (mProjectsList != null) {
                        for (int i = 0; i < mProjectsList.size(); i++) {
                            if (mSelectedProjectName.contains(mProjectsList.get(i).getProject_name()))
                                selectedProjList.add(mProjectsList.get(i));
                        }
                    }
                }
            }
            textView_projects.setText(mSelectedProjectName);
            mProjectsSpinner.setOnTouchListener(this);

            if (selectedProjList != null && selectedProjList.size() == 0) {
                spClosureProjectList.add(0, getString(R.string.spinner_select_project));
                textView_projects.setText(spClosureProjectList.get(0));
                mSelectedProjectName = spClosureProjectList.get(0);
            } else {
                for (int i = 0; i < selectedProjList.size(); i++) {
                    projectsModel = selectedProjList.get(i);
                    spClosureProjectList.add(projectsModel.getProject_name());
                }
            }

            closureProjectAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spClosureProjectList);
            closureProjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            mSpinnerClosureProject.setAdapter(closureProjectAdapter);
            mSpinnerClosureProject.setOnItemSelectedListener(this);

            spSubStatusList.add(0, getString(R.string.spinner_select));
            subStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spSubStatusList);
            subStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_sub_status.setAdapter(subStatusAdapter);
            spinner_sub_status.setOnItemSelectedListener(this);


            spBrokerList.add(0, getString(R.string.txt_broker_assign_to));
            if (!TextUtils.isEmpty(mDetailsModel.getBroker_name())) {
                getSelectedBrokerName(spBrokerList, mDetailsModel.getBroker_name());
            }
            brokerListAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spBrokerList);
            brokerListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_assign_to_broker.setAdapter(brokerListAdapter);
            spinner_assign_to_broker.setOnItemSelectedListener(this);


            leadStatusAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLeadStatusList);
            leadStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mLeadStatusSpinner.setAdapter(leadStatusAdapter);
            mLeadStatusSpinner.setOnItemSelectedListener(this);

          /*  locationAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLocationList);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            mLocationSpinner.setAdapter(locationAdapter);*/
            mLocationSpinner.setOnItemSelectedListener(this);
            spinner_site_visit_select_person_new.setOnItemSelectedListener(this);

            strBudgetRange = mDetailsModel.getBudget();
            if (!TextUtils.isEmpty(strBudgetRange)) {
                tv_budget_val.setText(strBudgetRange);
            }

            if (mAsmModel.getIsAssigned() == 0) {
                layout_last_updated_on_divider.setVisibility(View.GONE);
                layout_last_updated_on.setVisibility(View.GONE);
            }
            setDynamicWhatsNewLayouts();
        }
    }

    private void setDynamicWhatsNewLayouts() {
        String callbackId = getLeadStatusId("Callback");
        if (callbackId == null) {
            layout_callback_update.setVisibility(View.GONE);
        }
        String meetingId = getLeadStatusId("Meeting");
        if (meetingId == null) {
            layout_meeting_update.setVisibility(View.GONE);
        }
        String siteVisitId = getLeadStatusId("Site Visit");
        if (siteVisitId == null) {
            layout_callback_update.setVisibility(View.GONE);
        }
        String closureId = getLeadStatusId("Closure");
        if (closureId == null) {
            layout_callback_update.setVisibility(View.GONE);
        }
        String notInterestedId = getLeadStatusId("Not Interested");
        if (notInterestedId == null) {
            layout_callback_update.setVisibility(View.GONE);
        }
    }

    public void getUpdateValues() {

        StringBuilder flagBuilder = new StringBuilder();
        String projectStr = Utils.getSelectedMultiProjectIds(mDetailsModel.getSelectedProjList());
        List<String> proList = Utils.getMultiSelectedProject(projectStr);
        List<String> selProList = Utils.getMultiSelectedProject(mSelectedProjectId);

        if (selProList != null && proList != null) {
            // Sort and compare the two lists
            Collections.sort(proList);
            Collections.sort(selProList);

            if (!proList.equals(selProList)) {
                if (TextUtils.isEmpty(flagBuilder.toString()))
                    flagBuilder.append(101);
                else
                    flagBuilder.append(",").append(101);
            }
        }
        if (userDesignation.equalsIgnoreCase("0")) {
            if (Utils.isChanged(mDetailsModel.getBroker_name(), brokerName)) {
                if (TextUtils.isEmpty(flagBuilder.toString()))
                    flagBuilder.append(102);
                else
                    flagBuilder.append(",").append(102);
            }
        }

       /* if (mDetailsModel.getCurrent_Status().equalsIgnoreCase(getString(R.string.text_callback))) {
            if (TextUtils.isEmpty(flagBuilder.toString()))
                flagBuilder.append(103);
            else
                flagBuilder.append(",").append(103);
        } else {*/
        if (!spinner_sub_status.getSelectedItem().toString().equalsIgnoreCase("Select") &&
                !TextUtils.isEmpty(update_status)) {
            if (TextUtils.isEmpty(flagBuilder.toString()))
                flagBuilder.append(103);
            else
                flagBuilder.append(",").append(103);
        }
        //}
        if ((Utils.isChanged(mDetailsModel.getLead_type(), mSelectedRadioButton)) ||
                (Utils.isChanged(mDetailsModel.getRemark(), edtRemark.getText().toString()))) {
            if (TextUtils.isEmpty(flagBuilder.toString()))
                flagBuilder.append(104);
            else
                flagBuilder.append(",").append(104);
        }

        if (layout_callback_update.getVisibility() == View.VISIBLE) {
            new_lead_status = "Callback";
            new_status_id = getLeadStatusId(new_lead_status);
            new_date = tv_callback_date_val_new.getText().toString();
            if (TextUtils.isEmpty(new_date)) {
                Utils.showToast(this, "Please Select Callback Date First");
                return;
            }
            new_time = tv_callback_time_val_new.getText().toString();
            if (TextUtils.isEmpty(new_time)) {
                Utils.showToast(this, "Please Select Callback Time");
                return;
            }
            new_lead_type = lead_type_callback;
            /*if (TextUtils.isEmpty(new_lead_type) && TextUtils.isEmpty(lead_type_callback)) {
                Utils.showToast(this, getString(R.string.toast_msg_lead_type_mandatory));
                return;
            }*/
            new_remark = edit_text_remark_callback_new.getText().toString();
            if (TextUtils.isEmpty(new_remark)) {
                Utils.showToast(this, getString(R.string.toast_msg_remark_mandatory));
                return;
            }
            new_address = "";
            no_of_persons = "";
            new_sub_status = "";
            new_sub_status_id = "";
            cheque_no = "";
            bank_name = "";
            amount = "";
            unit_no = "";
            tower_no = "";
            cheque_date = "";
           /* if (!TextUtils.isEmpty(flagBuilder)) {
                updateAsmInfo(mAsmModel.getEnquiry_id(), "", mSelectedTab);
            }*/
        } else if (layout_meeting_update.getVisibility() == View.VISIBLE) {
            new_lead_status = "Meeting";
            new_status_id = getLeadStatusId(new_lead_status);
            new_date = tv_meeting_date_val_new.getText().toString();
            if (TextUtils.isEmpty(new_date)) {
                Utils.showToast(this, getString(R.string.please_select_meeting_date_first));
                return;
            }
            new_time = tv_meeting_time_val_new.getText().toString();
            if (TextUtils.isEmpty(new_time)) {
                Utils.showToast(this, getString(R.string.please_select_meeting_time));
                return;
            }
            new_address = tv_meeting_address_val_new.getText().toString();
            if (TextUtils.isEmpty(new_address)) {
                Utils.showToast(this, getString(R.string.please_enter_address));
                return;
            }
            new_lead_type = lead_type_meeting;
            /*if (TextUtils.isEmpty(new_lead_type) && TextUtils.isEmpty(lead_type_meeting)) {
                Utils.showToast(this, getString(R.string.toast_msg_lead_type_mandatory));
                return;
            }*/
            new_remark = edit_text_remark_new_meeting.getText().toString();
            if (TextUtils.isEmpty(new_remark)) {
                Utils.showToast(this, getString(R.string.toast_msg_remark_mandatory));
                return;
            }
            no_of_persons = "";
            new_sub_status = "";
            new_sub_status_id = "";
            cheque_no = "";
            bank_name = "";
            amount = "";
            mClosureProjectName = "";
            unit_no = "";
            tower_no = "";
            cheque_date = "";
            /*if (!TextUtils.isEmpty(flagBuilder)) {
                updateAsmInfo(mAsmModel.getEnquiry_id(), "", mSelectedTab);
            }*/
        } else if (layout_site_visit_update.getVisibility() == View.VISIBLE) {
            new_lead_status = "Site Visit";
            new_status_id = getLeadStatusId(new_lead_status);
            new_date = tv_site_visit_date_val.getText().toString();
            if (TextUtils.isEmpty(new_date)) {
                Utils.showToast(this, getString(R.string.please_select_site_visit_date));
                return;
            }
            new_time = tv_site_visit_time_val.getText().toString();
            if (TextUtils.isEmpty(new_time)) {
                Utils.showToast(this, getString(R.string.please_select_site_visit_time));
                return;
            }
            no_of_persons = mPersonCountString;
            if (TextUtils.isEmpty(no_of_persons) || no_of_persons.equalsIgnoreCase("Select No Of Persons")) {
                Utils.showToast(this, getString(R.string.txt_select_no_of_person_site_visit));
                return;
            }
            new_address = tv_site_visit_address_val.getText().toString();
           /* if (TextUtils.isEmpty(new_address)) {
                Utils.showToast(this, getString(R.string.enter_site_visit_address));
                return;
            }*/

            new_lead_type = lead_type_site_visit;
            /*if (TextUtils.isEmpty(new_lead_type) && TextUtils.isEmpty(lead_type_site_visit)) {
                Utils.showToast(this, getString(R.string.toast_msg_lead_type_mandatory));
                return;
                      }*/

            new_remark = edit_text_remark_site_visit.getText().toString();
            if (TextUtils.isEmpty(new_remark)) {
                Utils.showToast(this, getString(R.string.toast_msg_remark_mandatory));
                return;
            }

            new_sub_status = "";
            new_sub_status_id = "";
            cheque_no = "";
            bank_name = "";
            amount = "";
            mClosureProjectName = "";
            unit_no = "";
            tower_no = "";
            cheque_date = "";
           /* if (!TextUtils.isEmpty(flagBuilder)) {
                updateAsmInfo(mAsmModel.getEnquiry_id(), "", mSelectedTab);
            }*/
        } else if (layout_closure_update.getVisibility() == View.VISIBLE) {
            new_lead_status = "Closure";
            new_status_id = getLeadStatusId(new_lead_status);
            new_sub_status = sub_status_closure;
            if (TextUtils.isEmpty(new_sub_status) || new_sub_status.equalsIgnoreCase("select")) {
                Utils.showToast(this, getString(R.string.select_sub_status_first));
                return;
            }
            new_sub_status_id = sub_status_closure_id;
            new_date = "";
            new_time = "";
            new_lead_type = lead_type_closure;
            cheque_no = tv_cheque_number_val.getText().toString();
            bank_name = tv_bank_name_val.getText().toString();
            if (sub_status_closure.equalsIgnoreCase("cheque collected")) {
                if (TextUtils.isEmpty(cheque_no) || cheque_no.length() != 6) {
                    Utils.showToast(this, getString(R.string.enter_valid_cheque_no));
                    return;
                }
                if (TextUtils.isEmpty(bank_name)) {
                    Utils.showToast(this, getString(R.string.txt_enter_bank_name));
                    return;
                }
                if (StringUtil.checkSpecialCharacter(bank_name)) {
                    Utils.showToast(this, "Special character and digits are not allowed");
                    return;
                }
            } else {
                if (TextUtils.isEmpty(cheque_no)) {
                    Utils.showToast(this, "Please Enter Transaction No");
                    return;
                }
                if (TextUtils.isEmpty(bank_name)) {
                    Utils.showToast(this, "Please Enter Payment Mode");
                    return;
                }
            }
            amount = tv_amount_val.getText().toString();
            if (TextUtils.isEmpty(amount)) {
                Utils.showToast(this, "Please Enter Amount");
                return;
            }
            mClosureProjectName = mSpinnerClosureProject.getSelectedItem().toString();
            if (TextUtils.isEmpty(mClosureProjectName) || mClosureProjectName.equalsIgnoreCase(getString(R.string.spinner_select_any_project))) {
                Utils.showToast(this, getString(R.string.spinner_select_closure_project));
                return;
            }
            unit_no = tv_unit_no_val.getText().toString();
            if (TextUtils.isEmpty(unit_no)) {
                Utils.showToast(this, getString(R.string.txt_enter_unit_no));
                return;
            }
            tower_no = tv_tower_no_val.getText().toString();
            new_remark = edit_text_remark_closure_new.getText().toString();
            cheque_date = tv_cheque_date_val_closure.getText().toString();
            new_address = "";
            no_of_persons = "0";
          /*  if (!TextUtils.isEmpty(flagBuilder)) {
                updateAsmInfo(mAsmModel.getEnquiry_id(), "", mSelectedTab);
            }*/
        } else if (layout_not_interested_update.getVisibility() == View.VISIBLE) {
            new_lead_status = getString(R.string.text_not_interested);
            new_status_id = getLeadStatusId(new_lead_status);
            new_sub_status = sub_status_not_interested;
            if (TextUtils.isEmpty(new_sub_status) || new_sub_status.equalsIgnoreCase("select")) {
                Utils.showToast(this, "Please Select Sub Status First");
                return;
            }
            new_sub_status_id = sub_status_not_interested_id;
            new_date = "";
            new_time = "";
            new_lead_type = lead_type_not_interested;
            new_remark = edit_text_remark_not_interested_new.getText().toString();
            if (new_sub_status.equalsIgnoreCase("others") ||
                    TextUtils.isEmpty(new_sub_status) || new_sub_status.equalsIgnoreCase("select")) {

                if (TextUtils.isEmpty(new_remark)) {
                    Utils.showToast(this, getString(R.string.toast_msg_remark_mandatory));
                    return;
                }
            }
            new_address = "";
            no_of_persons = "0";
            cheque_no = "";
            bank_name = "";
            amount = "";
            mClosureProjectName = "";
            unit_no = "";
            tower_no = "";
            cheque_date = "";
           /* if (!TextUtils.isEmpty(flagBuilder)) {
                updateAsmInfo(mAsmModel.getEnquiry_id(), "", mSelectedTab);
            }*/
        }
        if (!TextUtils.isEmpty(flagBuilder)) {
            if (flagBuilder.toString().contains("103") &&
                    (TextUtils.isEmpty(new_lead_status) || new_lead_status.equalsIgnoreCase(""))) {
                Utils.showToast(this, getString(R.string.txt_whats_next));
                return;
            }
            if (isClosureProjectSelected()) return;
            updateAsmInfo(mAsmModel.getEnquiry_id(), "", mSelectedTab);
        } else if (!TextUtils.isEmpty(mDetailsModel.getCurrent_Status()) &&
                mDetailsModel.getCurrent_Status().equalsIgnoreCase(getString(R.string.text_callback))) {
            if (TextUtils.isEmpty(new_lead_status) || new_lead_status.equalsIgnoreCase("")) {
                Utils.showToast(this, getString(R.string.txt_whats_next));
                return;
            }
            if (isClosureProjectSelected())
                return;
            updateAsmInfo(mAsmModel.getEnquiry_id(), "", mSelectedTab);
        } else {
            Utils.showToast(this, getString(R.string.txt_msg_lead_not_updated));
        }
    }

    private boolean isClosureProjectSelected() {
        if (new_lead_status.equalsIgnoreCase(getString(R.string.text_closure))) {
            if (TextUtils.isEmpty(mClosureProjectName) || mClosureProjectName.equalsIgnoreCase(getString(R.string.spinner_select_any_project))) {
                Utils.showToast(this, getString(R.string.select_closure_project));
                return true;
            }
        }
        return false;
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
            tv_new_email_id_val.setText(mDetailsModel.getEmail_ID());
            tv_new_email_id_val.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(mAsmModel.getCustomer_email())) {
                        Utils.showToast(FollowUpSalesDetailActivity.this, "Email id not available");
                    } else {
                        String email = mAsmModel.getCustomer_email();
                        Utils.openMailClient(FollowUpSalesDetailActivity.this, "", new String[]{email}, "");
                    }
                }
            });
        }
         else {
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

    private String getSelectedLeadStatus(ArrayList<String> spLeadStatusList, String
            leadStatus) {
        if (!TextUtils.isEmpty(leadStatus) || !leadStatus.equalsIgnoreCase("")
                || !leadStatus.equalsIgnoreCase("NA")) {
            for (int i = 0; i < spLeadStatusList.size(); i++) {
                //    leadStatusModel = mLeadStatusList.get(i);
                if (spLeadStatusList.get(i).equalsIgnoreCase(mAsmModel.getStatus())) {
                    mLeadStatusSpinner.setSelection(i);
                    defaultPosLeadStatus = i;
                    break;
                }
            }
        }
        return null;
    }

    private void getSelectedBrokerName(ArrayList<String> spBrokerNameList, String
            brokerName) {
        for (int i = 0; i < spBrokerNameList.size(); i++) {
            //leadStatusModel = mLeadStatusList.get(i);
            if (spBrokerNameList.get(i).equalsIgnoreCase(brokerName)) {
                spinner_assign_to_broker.setSelection(i);
                defaultPosBroker = i;
                break;
            }
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

    @Override
    public void onBackPressed() {
        navigateToPrevious();
        super.onBackPressed();
    }

    private void navigateToPrevious() {
        app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
        if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
            Intent intent = new Intent(FollowUpSalesDetailActivity.this, SalesActivity.class);
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

            case R.id.spinner_closure_project_name:
                if (isInitialProject) {
                    position = defaultPosProject;
                    isInitialProject = false;
                }
                mClosureProjectName = parent.getItemAtPosition(position).toString();
                if (!mClosureProjectName.equalsIgnoreCase(getString(R.string.spinner_select_any_project))) {
                    getClosureProjectId(mClosureProjectName);
                }
                mSpinnerClosureProject.setSelection(position);
                break;

            case R.id.spinner_sub_status:
                if (isInitialSubStatus) {
                    position = defaultPosSubStatus;
                    isInitialSubStatus = false;
                }
                update_status = parent.getItemAtPosition(position).toString();
                if (!update_status.equalsIgnoreCase("select")) {
                    linear_layout_next_status.setVisibility(View.VISIBLE);
                    update_status_id = getSubStatusId(update_status);
                } else {
                    linear_layout_next_status.setVisibility(View.GONE);
                    update_status_id = "";
                }
                spinner_sub_status.setSelection(position);
                break;
            case R.id.spinner_select_not_interested_new:
                if (isInitialSubStatus) {
                    position = defaultPosSubStatus;
                    isInitialSubStatus = false;
                }
                sub_status_not_interested = parent.getItemAtPosition(position).toString();
                if (!sub_status_not_interested.equalsIgnoreCase("select")) {
                    getNewSubStatusId(sub_status_not_interested);
                } else {
                    sub_status_not_interested_id = "";
                }
                spinner_select_not_interested_new.setSelection(position);
                break;
            case R.id.spinner_select_closure_new:
                if (isInitialSubStatus) {
                    position = defaultPosSubStatus;
                    isInitialSubStatus = false;
                }
                sub_status_closure = parent.getItemAtPosition(position).toString();
                if (!sub_status_closure.equalsIgnoreCase("select")) {
                    getNewSubStatusClosureId(sub_status_closure);
                    //layout_select_sub_status.setVisibility(View.VISIBLE);
                    if (sub_status_closure.equalsIgnoreCase("cheque collected")) {
                        tv_cheque_number.setText(getResources().getString(R.string.cheque_number));
                        setEditTextMaxLength(tv_cheque_number_val, 6);
                        tv_cheque_number_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                        tv_bank_name.setText(getResources().getString(R.string.bank_name));
                        tv_cheque_date.setText(getResources().getString(R.string.cheque_date));
                        edit_text_remark_closure_new.setText("");
                        tv_cheque_number_val.setText("");
                        tv_bank_name_val.setText("");
                        tv_amount_val.setText("");
                        mSpinnerClosureProject.setSelection(0);
                        tv_unit_no_val.setText("");
                        tv_tower_no_val.setText("");
                        tv_cheque_date_val_closure.setText("");
                    } else {
                        tv_cheque_number.setText("Transaction Number");
                        tv_bank_name.setText("Payment Mode");
                        tv_cheque_date.setText("Transaction Date");
                        setEditTextMaxLength(tv_cheque_number_val, 25);
                        tv_cheque_number_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edit_text_remark_closure_new.setText("");
                        tv_cheque_number_val.setText("");
                        tv_bank_name_val.setText("");
                        tv_amount_val.setText("");
                        mSpinnerClosureProject.setSelection(0);
                        tv_unit_no_val.setText("");
                        tv_tower_no_val.setText("");
                        tv_cheque_date_val_closure.setText("");
                    }
                } else {
                    sub_status_closure_id = "";
                    setEditTextMaxLength(tv_cheque_number_val, 25);
                    tv_cheque_number_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                    edit_text_remark_closure_new.setText("");
                    tv_cheque_number_val.setText("");
                    tv_bank_name_val.setText("");
                    tv_amount_val.setText("");
                    mSpinnerClosureProject.setSelection(0);
                    tv_unit_no_val.setText("");
                    tv_tower_no_val.setText("");
                    tv_cheque_date_val_closure.setText("");
                    //layout_select_sub_status.setVisibility(View.GONE);
                }
                spinner_select_closure_new.setSelection(position);
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
                    tv_address_val.setEnabled(false);
                    if (!TextUtils.isEmpty(mDetailsModel.getLead_type()) &&
                            !mDetailsModel.getLead_type().equals("") &&
                            !mDetailsModel.getLead_type().equalsIgnoreCase("null")) {
                        if (!mDetailsModel.getLead_type().equalsIgnoreCase("1")) {
                            radioButton = selectLeadTypeStatus(mDetailsModel.getLead_type());
                            radioButton.setChecked(true);
                            mSelectedRadioButton = radioButton.getText().toString();
                        }
                    } else {
                        //do nothing
                    }
                }
                if (mDetailsModel.getLead_type().equalsIgnoreCase(BMHConstants.LEAD_TYPE_BROKER)) {
                    layout_tv_project_name_divider.setVisibility(View.GONE);
                    layout_sub_status_divider.setVisibility(View.GONE);
                    layout_tv_project_name.setVisibility(View.GONE);
                    layout_broker_list.setVisibility(View.GONE);
                    tv_assign_to_broker_separator.setVisibility(View.GONE);
                    linearLayoutSubStatus.setVisibility(View.GONE);
                    layout_lead_type_divider.setVisibility(View.GONE);
                    layout_lead_type.setVisibility(View.GONE);
                    edtRemark.setEnabled(false);
                    edtRemark.setVisibility(View.VISIBLE);
                    linear_layout_budget_divider.setVisibility(View.GONE);
                    linear_layout_budget.setVisibility(View.GONE);
                    layout_location.setVisibility(View.GONE);
                    layout_location_divider.setVisibility(View.GONE);
                    mSelectedRadioButton = "";
                    tv_date.setText(getString(R.string.txt_typed_date, mSelectedLeadStatus));
                    button_update.setVisibility(View.GONE);
                    tv_mobile_no_val.setEnabled(true);
                } else {
                    inflateDynamicView(mSelectedLeadStatus);
                }
                break;
            case R.id.spinner_assign_to_broker:
                if (isInitialBrokerName) {
                    position = defaultPosBroker;
                    isInitialBrokerName = false;
                }
                brokerName = parent.getItemAtPosition(position).toString();
                getBrokerId(brokerName);
                spinner_assign_to_broker.setSelection(position);
                break;

            case R.id.spinner_select_location:
                if (isInitial) {
                    position = defaultPos;
                    isInitial = false;
                }
                if (tv_select_location.getText().toString().equalsIgnoreCase(getString(R.string.txt_select_person))) {
                    mPersonCount = (Integer) mLocationSpinner.getSelectedItem();
                }/* else {
                    if (mSelectedLeadStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {
                        mSelectedLocation = parent.getItemAtPosition(position).toString();
                        getLocListValue(mSelectedLocation);
                        mLocationSpinner.setSelection(position);
                    }
                }*/
                break;
            case R.id.spinner_site_visit_select_person_new:
                if (isInitial) {
                    position = defaultPos;
                    isInitial = false;
                }
                mPersonCountString = (String) spinner_site_visit_select_person_new.getSelectedItem();
                if (mPersonCountString.equalsIgnoreCase("Select No Of Persons")) {
                    mPersonCountString = "";
                }
                break;
            default:
                break;
        }
    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
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
            //  mRemarkText = edtRemark.getText().toString();
            return;
        }
        /*
           MARKETING CALL
         */
        else if (lead.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[1])) {
            edtRemark.setVisibility(View.VISIBLE);
            // mRemarkText = edtRemark.getText().toString();
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
            layout_lead_type_divider.setVisibility(View.GONE);
            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));

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

            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_location.setVisibility(View.GONE);
            layout_address.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));

            tv_time.setText(getString(R.string.txt_typed_time, lead));
            mTimeText = tv_time_val.getText().toString();
            /*
            mSelectedProject
            mLocationName
            */
            tv_select_location.setText(getString(R.string.txt_select_meeting_location));
            switchSpinnerList(true);
            tv_address.setText(getString(R.string.txt_typed_address, lead));
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

            layout_date.setVisibility(View.VISIBLE);
            layout_time.setVisibility(View.VISIBLE);
            layout_location.setVisibility(View.VISIBLE);
            layout_address.setVisibility(View.VISIBLE);
            layout_lead_type.setVisibility(View.VISIBLE);
            edtRemark.setVisibility(View.VISIBLE);

            tv_date.setText(getString(R.string.txt_typed_date, lead));

            tv_time.setText(getString(R.string.txt_typed_time, lead));
            mTimeText = tv_time_val.getText().toString();
            /* mSelectedProject */
            tv_select_location.setText(getString(R.string.txt_select_person));
            switchSpinnerList(false);
            tv_address.setText(getString(R.string.txt_pickup_address));
            //  mRemarkText = edtRemark.getText().toString();
        }
    }

    /*
     * LOCATIONS
     */
    private void switchSpinnerList(boolean isMeeting) {
      /*  if (spLocationList != null && spLocationList.size() > 0)
            spLocationList.clear();*/

        if (!isMeeting) {
           /* if (TextUtils.isEmpty(mDetailsModel.getAddressType())) {
                spLocationList.addAll(mLocListKey);
                spLocationList.add(0, getString(R.string.select_location));
                mLocationSpinner.setSelection(0);
            } else {
                spLocationList.add(0, mDetailsModel.getAddressType());
                mLocationSpinner.setSelection(0);
            }
            locationAdapter = new CustomSpinnerAdapter(this, R.layout.textview_spinner, spLocationList);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mLocationSpinner.setAdapter(locationAdapter);
        } else {*/


            ArrayList<Integer> personArray = new ArrayList<>();
            if (mDetailsModel.getNo_of_persons() > 0) {
                personArray.add(0, mDetailsModel.getNo_of_persons());
                mPersonCount = mDetailsModel.getNo_of_persons();
            } else {
                personArray.add(1);
                personArray.add(2);
                personArray.add(3);
                personArray.add(4);
                personArray.add(5);
                personArray.add(6);
            }
            mLocationSpinner.setPadding(0, 0, 50, 0);
            locationAdapter = new ArrayAdapter(FollowUpSalesDetailActivity.this, R.layout.textview_spinner, personArray);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mLocationSpinner.setAdapter(locationAdapter);
        }
    }

    private String getClosureProjectId(String projectName) {
        for (int i = 0; i < selectedProjList.size(); i++) {
            projectsModel = selectedProjList.get(i);
            if (projectName.equalsIgnoreCase(projectsModel.getProject_name())) {
                return mClosureProjectId = projectsModel.getProject_id();
            }
        }
        return null;
    }

    private String getLeadStatusId(String selectedSp) {
        for (int i = 0; i < mLeadStatusList.size(); i++) {
            leadStatusModel = mLeadStatusList.get(i);
            if (selectedSp.equalsIgnoreCase(leadStatusModel.getTitle())) {
                return leadStatusModel.getDisposition_id();
            }
        }
        return null;
    }

    private String getSubStatusId(String selectedSubStatus) {
        for (int i = 0; i < updateStatusList.size(); i++) {
            subStatusModel = updateStatusList.get(i);
            if (selectedSubStatus.equalsIgnoreCase(subStatusModel.getTitle())) {
                return subStatusModel.getId();
            }
        }
        return null;
    }

    private void getNewSubStatusId(String selectedSubStatus) {
        for (int i = 0; i < mNotInterestedList.size(); i++) {
            notInterestedModel = mNotInterestedList.get(i);
            if (selectedSubStatus.equalsIgnoreCase(notInterestedModel.getTitle())) {
                sub_status_not_interested_id = notInterestedModel.getId();
                return;
            }
        }
    }

    private void getNewSubStatusClosureId(String selectedSubStatus) {
        for (int i = 0; i < mClosureList.size(); i++) {
            notInterestedModel = mClosureList.get(i);
            if (selectedSubStatus.equalsIgnoreCase(notInterestedModel.getTitle())) {
                sub_status_closure_id = notInterestedModel.getId();
                return;
            }
        }
    }

    private void getBrokerId(String selectedSubStatus) {
        if (!selectedSubStatus.equalsIgnoreCase("Select Broker To Assign")) {
            for (int i = 0; i < mBrokerList.size(); i++) {
                notInterestedModel = mBrokerList.get(i);
                if (selectedSubStatus.equalsIgnoreCase(notInterestedModel.getTitle())) {
                    brokerId = notInterestedModel.getId();
                    return;
                }
            }
        } else {
            brokerId = "";
        }
    }

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
            case R.id.radioButton_hot_callback_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot_callback_new = findViewById(selectedId);
                lead_type_callback = radioButton_hot_callback_new.getText().toString();
                break;
            case R.id.radioButton_warm_callback_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm_callback_new = findViewById(selectedId);
                lead_type_callback = radioButton_warm_callback_new.getText().toString();
                break;
            case R.id.radioButton_cold_callback_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold_callback_new = findViewById(selectedId);
                lead_type_callback = radioButton_cold_callback_new.getText().toString();
                break;
            case R.id.radioButton_hot_meeting:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot_meeting = findViewById(selectedId);
                lead_type_meeting = radioButton_hot_meeting.getText().toString();
                break;
            case R.id.radioButton_warm_meeting:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm_meeting = findViewById(selectedId);
                lead_type_meeting = radioButton_warm_meeting.getText().toString();
                break;
            case R.id.radioButton_cold_meeting:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold_meeting = findViewById(selectedId);
                lead_type_meeting = radioButton_cold_meeting.getText().toString();
                break;
            case R.id.radioButton_hot_site_visit:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot_site_visit = findViewById(selectedId);
                lead_type_site_visit = radioButton_hot_site_visit.getText().toString();
                break;
            case R.id.radioButton_warm_site_visit:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm_site_visit = findViewById(selectedId);
                lead_type_site_visit = radioButton_warm_site_visit.getText().toString();
                break;
            case R.id.radioButton_cold_site_visit:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold_site_visit = findViewById(selectedId);
                lead_type_site_visit = radioButton_cold_site_visit.getText().toString();
                break;
            case R.id.radioButton_hot_not_interested_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot_not_interested_new = findViewById(selectedId);
                lead_type_not_interested = radioButton_hot_not_interested_new.getText().toString();
                break;
            case R.id.radioButton_warm_not_interested_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm_not_interested_new = findViewById(selectedId);
                lead_type_not_interested = radioButton_warm_not_interested_new.getText().toString();
                break;
            case R.id.radioButton_cold_not_interested_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold_not_interested_new = findViewById(selectedId);
                lead_type_not_interested = radioButton_cold_not_interested_new.getText().toString();
                break;
            case R.id.radioButton_hot_closure_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_hot_closure_new = findViewById(selectedId);
                lead_type_closure = radioButton_hot_closure_new.getText().toString();
                break;
            case R.id.radioButton_warm_closure_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_warm_closure_new = findViewById(selectedId);
                lead_type_closure = radioButton_warm_closure_new.getText().toString();
                break;
            case R.id.radioButton_cold_closure_new:
                selectedId = group.getCheckedRadioButtonId();
                radioButton_cold_closure_new = findViewById(selectedId);
                lead_type_closure = radioButton_cold_closure_new.getText().toString();
                break;
        }
    }

    TimePickerDialog.OnTimeSetListener onCallbackTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentDate = currentCal.getTime();
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            setSelectedTime(hourOfDay, minute, tv_callback_time_val_new);
        }
    };

    TimePickerDialog.OnTimeSetListener onMeetingTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentDate = currentCal.getTime();
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            setSelectedTime(hourOfDay, minute, tv_meeting_time_val_new);
        }
    };
    TimePickerDialog.OnTimeSetListener onSiteVisitTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentDate = currentCal.getTime();
            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            setSelectedTime(hourOfDay, minute, tv_site_visit_time_val);
        }
    };

    private void setSelectedTime(int hourOfDay, int minute, TextView textView) {

        currentCal = Calendar.getInstance();
        selectedCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedCal.set(Calendar.MINUTE, minute);

        if (selectedCal.getTimeInMillis() >= currentCal.getTimeInMillis()) {
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
            Toast.makeText(FollowUpSalesDetailActivity.this, getString(R.string.invalid_time_picker_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSelectedDate(int year, int month, int dayOfMonth, TextView textview) {
        selectedCal.set(Calendar.YEAR, year);
        selectedCal.set(Calendar.MONTH, month);
        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate = currentCal.getTime();
        selectedDate = selectedCal.getTime();
        // if (selectedCal.getTimeInMillis() >= currentCal.getTimeInMillis()) {
        CharSequence charSequence = simpleDateFormat.format(selectedDate);
        textview.setText(charSequence);
        if (selectedCal.getTimeInMillis() == currentCal.getTimeInMillis()) {
            tv_time_val.setText("");
            mTimeText = "";
        }
        //  }
    }

    DatePickerDialog.OnDateSetListener callbackDateChangeListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setSelectedDate(year, month, dayOfMonth, tv_callback_date_val_new);
        }
    };

    DatePickerDialog.OnDateSetListener meetingDateChangeListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setSelectedDate(year, month, dayOfMonth, tv_meeting_date_val_new);
        }
    };
    DatePickerDialog.OnDateSetListener siteVisitDateChangeListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setSelectedDate(year, month, dayOfMonth, tv_site_visit_date_val);
        }
    };

    public boolean checkPermissions(String mobile_no) {
        mobileNo = mobile_no;
        hasExtCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        return hasExtCallPermission == PackageManager.PERMISSION_GRANTED;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions() {
        if (hasExtCallPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsCall.add(Manifest.permission.CALL_PHONE);
        }
        if (!permissionsCall.isEmpty()) {
            requestPermissions(permissionsCall.toArray(new String[permissionsCall.size()]), BMHConstants.PERMISSION_CODE_CALL);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_update:
                if (TextUtils.isEmpty(mSelectedProjectName) || mSelectedProjectName.equalsIgnoreCase("select")) {
                    Utils.showToast(this, getString(R.string.select_one_project));
                    return;
                }
              /*  if (mDetailsModel.getBroker_name().equalsIgnoreCase(brokerName)) {
                    Utils.showToast(this, "Please choose new broker to assign");
                    return;
                }*/
                if (mDetailsModel.getCurrent_Status().equalsIgnoreCase("callback")) {
                    if (TextUtils.isEmpty(edtRemark.getText().toString())) {
                        Utils.showToast(this, getString(R.string.toast_msg_remark_mandatory));
                        return;
                    }
                } else {
                    if (mDetailsModel.getCurrent_Status().equalsIgnoreCase("meeting")
                            || mDetailsModel.getCurrent_Status().equalsIgnoreCase("site visit")) {
                        if (TextUtils.isEmpty(edtRemark.getText().toString())) {
                            Utils.showToast(this, getString(R.string.toast_msg_remark_mandatory));
                            return;
                        }
                    }
                }
                getUpdateValues();
                break;
          /*  case R.id.button_history:
                Intent intent = new Intent(this, AsmHistoryActivity.class);
                intent.putExtra(BMHConstants.ENQUIRY_ID, mAsmModel.getEnquiry_id());
                this.startActivity(intent);
                break;*/
            case R.id.tv_mobile_no_val:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermissions(mAsmModel.getCustomer_mobile()))
                        makeCallAction(mAsmModel.getCustomer_mobile());
                    else
                        requestPermissions();

                } else {
                    makeCallAction(mAsmModel.getCustomer_mobile());
                }
                break;
            case R.id.tv_new_email_id_val:

                break;
            case R.id.btn_upload_cheque_or_dd:
                capturePicture();
                break;
            case R.id.tv_cheque_date_val_closure:
                datePicker.show();
                break;
            case R.id.tv_callback_date_val_new:
                dateString = tv_callback_date_val_new.getText().toString();
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

                fromDateDialog = new DatePickerDialog(FollowUpSalesDetailActivity.this, callbackDateChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_callback_time_val_new:
                if (TextUtils.isEmpty(tv_callback_date_val_new.getText().toString())) {
                    Toast.makeText(FollowUpSalesDetailActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                    return;
                }
                timeString = tv_callback_time_val_new.getText().toString();
                if (!TextUtils.isEmpty(timeString)) {
                    String[] timeArray = timeString.split(":");
                    mHour = Integer.valueOf(timeArray[0]);
                    mMinute = Integer.valueOf(timeArray[1]);
                } else {
                    mHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    mMinute = currentCal.get(Calendar.MINUTE);
                }

                mTimePicker = new TimePickerDialog(FollowUpSalesDetailActivity.this, onCallbackTimeListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.tv_meeting_date_val_new:
                dateString = tv_meeting_date_val_new.getText().toString();
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

                fromDateDialog = new DatePickerDialog(FollowUpSalesDetailActivity.this, meetingDateChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_meeting_time_val_new:
                if (TextUtils.isEmpty(tv_meeting_date_val_new.getText().toString())) {
                    Toast.makeText(FollowUpSalesDetailActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                    return;
                }
                timeString = tv_meeting_time_val_new.getText().toString();
                if (!TextUtils.isEmpty(timeString)) {
                    String[] timeArray = timeString.split(":");
                    mHour = Integer.valueOf(timeArray[0]);
                    mMinute = Integer.valueOf(timeArray[1]);
                } else {
                    mHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    mMinute = currentCal.get(Calendar.MINUTE);
                }

                mTimePicker = new TimePickerDialog(FollowUpSalesDetailActivity.this, onMeetingTimeSetListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.tv_site_visit_date_val:
                dateString = tv_site_visit_date_val.getText().toString();
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

                fromDateDialog = new DatePickerDialog(FollowUpSalesDetailActivity.this, siteVisitDateChangeListener, mYear, mMonth, mDay);
                fromDateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                fromDateDialog.show();
                break;
            case R.id.tv_site_visit_time_val:
                if (TextUtils.isEmpty(tv_site_visit_date_val.getText().toString())) {
                    Toast.makeText(FollowUpSalesDetailActivity.this, getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                    return;
                }
                timeString = tv_site_visit_time_val.getText().toString();
                if (!TextUtils.isEmpty(timeString)) {
                    String[] timeArray = timeString.split(":");
                    mHour = Integer.valueOf(timeArray[0]);
                    mMinute = Integer.valueOf(timeArray[1]);
                } else {
                    mHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    mMinute = currentCal.get(Calendar.MINUTE);
                }

                mTimePicker = new TimePickerDialog(FollowUpSalesDetailActivity.this, onSiteVisitTimeSetListener, mHour,
                        mMinute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.rl_callback:
                if (layout_callback_update.getVisibility() == View.GONE) {
                    layout_callback_update.setVisibility(View.VISIBLE);
                    layout_meeting_update.setVisibility(View.GONE);
                    layout_site_visit_update.setVisibility(View.GONE);
                    layout_closure_update.setVisibility(View.GONE);
                    layout_not_interested_update.setVisibility(View.GONE);
                    iv_callback_plus.setImageResource(R.drawable.minus);
                    iv_meeting_plus.setImageResource(R.drawable.plus);
                    iv_site_visit_plus.setImageResource(R.drawable.plus);
                    iv_closure_plus.setImageResource(R.drawable.plus);
                    iv_not_interested_plus.setImageResource(R.drawable.plus);
                } else {
                    layout_callback_update.setVisibility(View.GONE);
                    iv_callback_plus.setImageResource(R.drawable.plus);
                }
                break;
            case R.id.rl_meeting:
                if (layout_meeting_update.getVisibility() == View.GONE) {
                    layout_meeting_update.setVisibility(View.VISIBLE);
                    layout_callback_update.setVisibility(View.GONE);
                    layout_site_visit_update.setVisibility(View.GONE);
                    layout_closure_update.setVisibility(View.GONE);
                    layout_not_interested_update.setVisibility(View.GONE);
                    iv_meeting_plus.setImageResource(R.drawable.minus);
                    iv_callback_plus.setImageResource(R.drawable.plus);
                    iv_site_visit_plus.setImageResource(R.drawable.plus);
                    iv_closure_plus.setImageResource(R.drawable.plus);
                    iv_not_interested_plus.setImageResource(R.drawable.plus);
                } else {
                    layout_meeting_update.setVisibility(View.GONE);
                    iv_meeting_plus.setImageResource(R.drawable.plus);
                }
                break;
            case R.id.rl_site_visit:
                if (layout_site_visit_update.getVisibility() == View.GONE) {
                    layout_site_visit_update.setVisibility(View.VISIBLE);
                    layout_callback_update.setVisibility(View.GONE);
                    layout_meeting_update.setVisibility(View.GONE);
                    layout_closure_update.setVisibility(View.GONE);
                    layout_not_interested_update.setVisibility(View.GONE);
                    iv_site_visit_plus.setImageResource(R.drawable.minus);
                    iv_callback_plus.setImageResource(R.drawable.plus);
                    iv_meeting_plus.setImageResource(R.drawable.plus);
                    iv_closure_plus.setImageResource(R.drawable.plus);
                    iv_not_interested_plus.setImageResource(R.drawable.plus);
                } else {
                    layout_site_visit_update.setVisibility(View.GONE);
                    iv_site_visit_plus.setImageResource(R.drawable.plus);
                }
                break;
            case R.id.rl_closure:
                if (layout_closure_update.getVisibility() == View.GONE) {
                    layout_closure_update.setVisibility(View.VISIBLE);
                    layout_callback_update.setVisibility(View.GONE);
                    layout_meeting_update.setVisibility(View.GONE);
                    layout_site_visit_update.setVisibility(View.GONE);
                    layout_not_interested_update.setVisibility(View.GONE);
                    iv_closure_plus.setImageResource(R.drawable.minus);
                    iv_site_visit_plus.setImageResource(R.drawable.plus);
                    iv_callback_plus.setImageResource(R.drawable.plus);
                    iv_meeting_plus.setImageResource(R.drawable.plus);
                    iv_not_interested_plus.setImageResource(R.drawable.plus);
                } else {
                    layout_closure_update.setVisibility(View.GONE);
                    iv_closure_plus.setImageResource(R.drawable.plus);
                }
                break;
            case R.id.rl_not_interested:
                if (layout_not_interested_update.getVisibility() == View.GONE) {
                    layout_not_interested_update.setVisibility(View.VISIBLE);
                    layout_callback_update.setVisibility(View.GONE);
                    layout_meeting_update.setVisibility(View.GONE);
                    layout_site_visit_update.setVisibility(View.GONE);
                    layout_closure_update.setVisibility(View.GONE);
                    iv_not_interested_plus.setImageResource(R.drawable.minus);
                    iv_site_visit_plus.setImageResource(R.drawable.plus);
                    iv_callback_plus.setImageResource(R.drawable.plus);
                    iv_meeting_plus.setImageResource(R.drawable.plus);
                    iv_closure_plus.setImageResource(R.drawable.plus);
                } else {
                    layout_not_interested_update.setVisibility(View.GONE);
                    iv_not_interested_plus.setImageResource(R.drawable.plus);
                }
                break;
            default:
                break;
        }

    }

    private DatePickerDialog.OnDateSetListener onDateSet = new android.app.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;
            SELECTED_CALENDER.set(mSelectedYear, mSelectedMonth, mSelectedDay, 0, 0);
            tv_cheque_date_val_closure.setText(df_ddMMyyyy.format(SELECTED_CALENDER.getTime()));
        }
    };

    private void makeCallAction(String mobileNo) {
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

    private void capturePicture() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkGalleryPermissions()) {
                                openGallery();
                            } else {
                                requestGalleryPermissions();
                            }
                        } else {
                            openGallery();
                        }
                    }
                });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkCameraPermissions()) {
                        openCamera();
                    } else {
                        requestCameraPermissions();
                    }
                } else {
                    openCamera();
                }
            }
        });
        myAlertDialog.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
    }

    private boolean checkGalleryPermissions() {
        hasExtStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasExtStoragePermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean checkCameraPermissions() {
        hasExtCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (hasExtCameraPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermissions() {
        if (hasExtCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.CAMERA);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestGalleryPermissions() {
        if (hasExtStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.GALLERY_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case BMHConstants.PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG, "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            case BMHConstants.GALLERY_PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG, "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            case BMHConstants.PERMISSION_CODE_CALL:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        makeCallAction(mobileNo);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG, "Permission Denied: " + permissions[i]);
                    }
                }
                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        selectedImagePath = null;
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = Utils.getImageUri(this, photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(Utils.getRealPathFromURI(this, tempUri));

            // Save a file: path for use with ACTION_VIEW intents
            selectedImagePath = finalFile.getAbsolutePath();

            Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            byte[] decodeResponse = Base64.decode(encodedImage, Base64.DEFAULT | Base64.NO_WRAP);
            bitmap = BitmapFactory.decodeByteArray(decodeResponse, 0, decodeResponse.length); // load
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            cheque_preview.setVisibility(View.VISIBLE);
            cheque_preview.setImageBitmap(bitmap);

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {
                Uri selectedImage = data.getData();
                selectedImagePath = Utils.getPath(this, selectedImage);
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();
                if (selectedImagePath != null) {
                    bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    cheque_preview.setImageBitmap(bitmap);
                    cheque_preview.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateAsmInfo(final String enquiryId, String image, final String mSelectedTab) {
        if (Connectivity.isConnected(this)) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.show();
        }
        SimpleMultiPartRequest stringRequest = new SimpleMultiPartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPDATE_LEAD_STATUS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (Connectivity.isConnected(FollowUpSalesDetailActivity.this)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        if (!response.equalsIgnoreCase("null")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    Toast.makeText(FollowUpSalesDetailActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                    app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
                                    app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                                    if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                                        Intent intent = new Intent(FollowUpSalesDetailActivity.this, SalesActivity.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                                if (!jsonObject.getBoolean("success")) {
                                    Toast.makeText(FollowUpSalesDetailActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(FollowUpSalesDetailActivity.this, R.string.txt_msg_lead_not_updated, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (Connectivity.isConnected(FollowUpSalesDetailActivity.this)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        if (!Connectivity.isConnected(FollowUpSalesDetailActivity.this)) {
                            app.saveTabIntoPrefs("TAB_NAVIGATED_FROM", mSelectedTab);
                            app.saveIntoPrefs(BMHConstants.IS_BACK_FROM_UPDATE, true);
                            onBackPressed();
                        }
                    }
                });
        stringRequest.addStringParam(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
        stringRequest.addStringParam(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
        stringRequest.addStringParam(ParamsConstants.USER_DESIGNATION, app.getFromPrefs((BMHConstants.USER_DESIGNATION)));
        stringRequest.addStringParam(ParamsConstants.ENQUIRY_ID, enquiryId);
        //Current Status Updating
        stringRequest.addStringParam(ParamsConstants.STATUS_ID, getLeadStatusId(mDetailsModel.getCurrent_Status()));
        stringRequest.addStringParam(ParamsConstants.LEAD_STATUS, mDetailsModel.getCurrent_Status());
        stringRequest.addStringParam("date", tv_date_val.getText().toString());
        stringRequest.addStringParam("time", tv_time_val.getText().toString());
        stringRequest.addStringParam("project_name", mSelectedProjectName);
        stringRequest.addStringParam("project_id", mSelectedProjectId);
        if (userDesignation.equalsIgnoreCase("0")) {
            stringRequest.addStringParam("broker_id", brokerId);
        } else { // TODO CHANGE LATTER
            stringRequest.addStringParam("broker_id", brokerId);
        }
        stringRequest.addStringParam("address", tv_address_val.getText().toString());
        stringRequest.addStringParam("no_of_persons", String.valueOf(mPersonCount));

        stringRequest.addStringParam("lead_type", mSelectedRadioButton);
        if (mSelectedProjectName.equalsIgnoreCase(Utils.getMultiSelectedProject(mDetailsModel.getSelectedProjList()))) {
            stringRequest.addStringParam("flag", String.valueOf(0));
        } else {
            stringRequest.addStringParam("flag", String.valueOf(1));
        }
        stringRequest.addStringParam("sub_status", update_status_id);
        stringRequest.addStringParam("sub_status_title", update_status);
        stringRequest.addStringParam("remark", edtRemark.getText().toString());

        // Parameter for new status update
        stringRequest.addStringParam("new_status_id", new_status_id);
        stringRequest.addStringParam("new_lead_status", new_lead_status);
        stringRequest.addStringParam("new_remark", new_remark);
        stringRequest.addStringParam("new_lead_type", new_lead_type);
        stringRequest.addStringParam("new_address", new_address);
        stringRequest.addStringParam("new_date", new_date);
        stringRequest.addStringParam("no_of_persons", no_of_persons);
        stringRequest.addStringParam("new_time", new_time);
        stringRequest.addStringParam("new_sub_status_title", new_sub_status);
        stringRequest.addStringParam("new_sub_status", new_sub_status_id);

        stringRequest.addFile("image", image);
        stringRequest.addStringParam("payment_mode", bank_name);
        stringRequest.addStringParam("unit_no", unit_no);
        stringRequest.addStringParam("tower_no", tower_no);
        stringRequest.addStringParam("closure_project_id", mClosureProjectId);
        stringRequest.addStringParam("amount", amount);
        stringRequest.addStringParam("cheque_number", cheque_no);
        stringRequest.addStringParam("cheque_date", cheque_date);

        int isSynced = 1;
        if (Connectivity.isConnected(FollowUpSalesDetailActivity.this))
            isSynced = 0;
        String oldDate = tv_date_val.getText().toString();
        String oldTime = tv_time_val.getText().toString();
        String oldAddress = tv_address_val.getText().toString();
        String oldRemark = edtRemark.getText().toString();
        String oldStatus = mDetailsModel.getCurrent_Status();
        String oldStatusId = getLeadStatusId(mDetailsModel.getCurrent_Status());

        // CALLBACK
        if (new_lead_status.equalsIgnoreCase(getString(R.string.text_callback))) {
            SubStatusCallbackEntity entity = new SubStatusCallbackEntity(enquiryId, "", mSelectedProjectName, mSelectedProjectId,
                    brokerName, brokerId, oldStatus, oldStatusId, new_lead_status, new_status_id, update_status_id,
                    UpdateStatusConverter.fromArrayList(updateStatusList),
                    new_date, new_time, mSelectedRadioButton, new_remark, Utils.getCurrentDateTime());
            new InsertSubStatusCallbackTask(entity).execute();
            //   mDetailsModel.setCurrent_Status(new_lead_status);
            mDetailsModel.setDate(new_date);
            mDetailsModel.setTime(new_time);
            edtRemark.setText(new_remark);
        }
        //MEETING
        if (new_lead_status.equalsIgnoreCase(getString(R.string.txt_meeting))) {
            SubStatusMeetingEntity meetingEntity = new SubStatusMeetingEntity(enquiryId, "", mSelectedProjectName,
                    mSelectedProjectId, brokerName, brokerId, oldStatus, oldStatusId, new_lead_status,
                    new_status_id, update_status_id, UpdateStatusConverter.fromArrayList(updateStatusList), new_sub_status, new_sub_status_id, new_address,
                    new_date, new_time, mSelectedRadioButton, new_remark, Utils.getCurrentDateTime());
            //  mDetailsModel.setCurrent_Status(new_            new InsertSubStatusMeetingTask(meetingEntity).execute();lead_status);
            mDetailsModel.setDate(new_date);
            mDetailsModel.setTime(new_time);
            tv_address_val.setText(new_address);
            edtRemark.setText(new_remark);
        }
        // SITE VISIT
        if (new_lead_status.equalsIgnoreCase(getString(R.string.txt_site_visit))) {
            SubStatusSiteVisitEntity siteVisitEntity = new SubStatusSiteVisitEntity(enquiryId, "", mSelectedProjectName,
                    mSelectedProjectId, brokerName, brokerId, oldStatus, oldStatusId, new_lead_status,
                    new_status_id, update_status_id, UpdateStatusConverter.fromArrayList(updateStatusList), new_sub_status, new_sub_status_id, Integer.valueOf(no_of_persons),
                    new_address, new_date, new_time, mSelectedRadioButton, new_remark, Utils.getCurrentDateTime());
            new InsertSubStatusSiteVisitTask(siteVisitEntity).execute();
            //  mDetailsModel.setCurrent_Status(new_lead_status);
            mDetailsModel.setDate(new_date);
            mDetailsModel.setTime(new_time);
            mPersonCount = Integer.valueOf(no_of_persons);
            tv_address_val.setText(new_address);
            edtRemark.setText(new_remark);
        }
        // CLOSURE
        if (new_lead_status.equalsIgnoreCase(getString(R.string.text_closure))) {
            SubStatusClosureEntity closureEntity = new SubStatusClosureEntity(enquiryId, "",
                    mSelectedProjectName, mSelectedProjectId, brokerName, brokerId, oldStatus, oldStatusId, new_lead_status,
                    new_status_id="8", update_status_id, UpdateStatusConverter.fromArrayList(updateStatusList), new_sub_status,
                    new_sub_status_id, cheque_no, bank_name,
                    cheque_date, amount, mClosureProjectName, mClosureProjectId, tower_no, unit_no, "",
                    mSelectedRadioButton, new_remark, Utils.getCurrentDateTime());
            new InsertSubStatusClosureTask(closureEntity).execute();
            //  mDetailsModel.setCurrent_Status(new_lead_status);
            mDetailsModel.setDate(new_date);
            mDetailsModel.setTime(new_time);
            //   mPersonCount = Integer.valueOf(no_of_persons);
            tv_address_val.setText(new_address);
            edtRemark.setText(new_remark);

            CustomerInfoEntity customerEntity = new CustomerInfoEntity(mDetailsModel.getMobile_No(), mDetailsModel.getEnquiry_ID(), mDetailsModel.getName(),
                    BMHConstants.LEAD_PHASE_SALES,  "2");
            new CustomerInfoTask(customerEntity, true).execute();
        }
        // NOT INTERESTED
        if (new_lead_status.equalsIgnoreCase(getString(R.string.text_not_interested))) {
            if (!Connectivity.isConnected(this)) {
                SubStatusNotInterestedEntity notInterestedEntity = new SubStatusNotInterestedEntity(enquiryId, "",
                        mSelectedProjectName, mSelectedProjectId, brokerName, brokerId, oldStatus, oldStatusId, new_lead_status,
                        new_status_id, update_status_id, UpdateStatusConverter.fromArrayList(updateStatusList), new_sub_status,
                        new_sub_status_id, mSelectedRadioButton, new_remark, Utils.getCurrentDateTime());
                new InsertSubStatusNotInterestedTask(notInterestedEntity).execute();
                //    mDetailsModel.setCurrent_Status(new_lead_status);
                edtRemark.setText(new_remark);
            }
        }
        StringBuilder flagBuilder = new StringBuilder();
        String projectStr = Utils.getSelectedMultiProjectIds(mDetailsModel.getSelectedProjList());
        List<String> proList = Utils.getMultiSelectedProject(projectStr);
        List<String> selProList = Utils.getMultiSelectedProject(mSelectedProjectId);

        if (selProList != null && proList != null) {
            // Sort and compare the two lists
            Collections.sort(proList);
            Collections.sort(selProList);

            if (!proList.equals(selProList)) {
                flagBuilder.append(201);
            }
        }
        if (userDesignation.equalsIgnoreCase("0")) {
            if (Utils.isChanged(mDetailsModel.getBroker_name(), brokerName)) {
                if (TextUtils.isEmpty(flagBuilder.toString()))
                    flagBuilder.append(202);
                else
                    flagBuilder.append(",").append(202);
            }
        }
        if ((Utils.isChanged(mDetailsModel.getLead_type(), mSelectedRadioButton)) ||
                (Utils.isChanged(mDetailsModel.getRemark(), oldRemark))) {
            if (TextUtils.isEmpty(flagBuilder.toString()))
                flagBuilder.append(203);
            else
                flagBuilder.append(",").append(203);
        }
        if (!TextUtils.isEmpty(new_lead_status)) {
            // SUB STATUS CHANGE CHECK- ALWAYS HAVE LATEST VALUE
            if (!TextUtils.isEmpty(new_status_id)) {
                if (TextUtils.isEmpty(flagBuilder.toString()))
                    flagBuilder.append(204);
                else
                    flagBuilder.append(",").append(204);
                mDetailsModel.setCurrent_Status(new_lead_status);
            }
        }
        stringRequest.addStringParam(BMHConstants.UPDATE_FLAG, flagBuilder.toString());
        BMHApplication.getInstance().addToRequestQueue(stringRequest, getString(R.string.tag_update_lead));

        int newPersonCount = no_of_persons.equals("") ? 0 : mPersonCount;

        int newStatusId = new_status_id.equals("") ? 0 : Integer.valueOf(new_status_id);
        String isLeadTypeValue = mDetailsModel.getIsLeadType() == null ? "" : mDetailsModel.getIsLeadType();

        String scheduleDate = new_date.equalsIgnoreCase("") ? oldDate : new_date;
        String scheduleTime = new_time.equalsIgnoreCase("") ? oldTime : new_time;




        String scheduleDateTime = "";
        if (!TextUtils.isEmpty(scheduleDate)) {
            String[] dateArr = scheduleDate.trim().split(",");
            // dateAndTime = Utils.getDateFromString(overdueDate[1]) + " " + newTime;
            scheduleDateTime = dateArr[1] + " | " + scheduleTime;
        }
        if (new_lead_status.equalsIgnoreCase(getString(R.string.text_closure))) {
            SalesClosureLeadsDetailEntity closureEntity = new SalesClosureLeadsDetailEntity(enquiryId, mDetailsModel.getName(),
                    mDetailsModel.getMobile_No(), mDetailsModel.getEmail_ID(),
                    mDetailsModel.getBudget(), mClosureProjectName, oldStatus, new_sub_status, unit_no,
                    scheduleDateTime, amount, tower_no, cheque_no, cheque_date,
                    bank_name, new_remark, 1, isSynced, Utils.getCurrentDateTime(), userDesignation);

            new InsertClosureLeadsDetailTask(this, closureEntity).execute();
        }

        SalesLeadDetailEntity detailEntity = new SalesLeadDetailEntity(enquiryId, mDetailsModel.getName(),
                mDetailsModel.getEmail_ID(), mDetailsModel.getMobile_No(), mDetailsModel.getAlternate_Mobile_No(),
                Utils.getMultiSelectedProject(selectedProjList), mSelectedProjectId, mDetailsModel.getBudget(), brokerName,
                brokerId, isLeadTypeValue, oldStatus, Utils.getLeadStatusId(mLeadStatusList,
                oldStatus), scheduleDateTime, oldDate, oldTime, mAsmModel.getIsAssigned(), oldAddress,
                mPersonCount, update_status_id, UpdateStatusConverter.fromArrayList(updateStatusList), unit_no, amount,
                tower_no, cheque_no, cheque_date, bank_name,
                mSelectedRadioButton, oldRemark, newStatusId, new_lead_status,
                new_date, new_time, new_sub_status, new_sub_status_id, mClosureProjectId, newPersonCount, new_address,
                new_remark, "",
                mDetailsModel.getCampaign(), mDetailsModel.getCampaign_Date(), 0, updateFromAlarmNotification, isSynced,
                Utils.getCurrentDateTime(), getString(R.string.txt_sp), mAsmModel.getIsLeadAccepted(), mRecFilePath);
        new InsertSalesLeadDetailsTask(this, detailEntity, true).execute();

        if (selectedProjList != null && selectedProjList.size() > 0) {
            SelectMultipleProjectsEntity multiSelectEntity =
                    new SelectMultipleProjectsEntity(enquiryId, mSelectedProjectId, Utils.getMultiSelectedProject(selectedProjList),
                            isSynced, mRecFilePath, Utils.getCurrentDateTime());
            new InsertMultiSelectTask(multiSelectEntity, isSynced).execute();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(tagReceivedFrom) && tagReceivedFrom.equalsIgnoreCase(getString(R.string.tag_notification))) {
                    AlarmJobServices services = new AlarmJobServices();
                    services.stopRepeatingAlarm(alarmIndex, FollowUpSalesDetailActivity.this);
                }
            }
        });
        startAlarmService();
    }

    private void startAlarmService() {
        new GetSalesLeadDetailTask(this).execute();
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
                    /*if (System.currentTimeMillis() <= cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_ONE_HOUR) {
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
    public void salesLeadDetailsCallback(List<ProjectMasterEntity> projectMasterList, List<StatusMasterEntity> leadStatusEntityList,
                                         List<SalesBrokerMasterEntity> brokerMasterList, List<ClosureMasterEntity> closureMasterList,
                                         List<NotInterestedMasterEntity> notInterestedMasterList,
                                         List<SelectMultipleProjectsEntity> selectedProjectsList, SalesLeadDetailEntity detailsEntity) {

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

            if (!TextUtils.isEmpty(tagReceivedFrom)) {
                mProjectsList = new ArrayList<>();
                spSubStatusList = new ArrayList<>();
                mSelectedMultiProjList = new ArrayList<>();
                mLeadStatusList = new ArrayList<>();
                updateStatusList = new ArrayList<>();
                mNotInterestedList = new ArrayList<>();
                mClosureList = new ArrayList<>();
                mBrokerList = new ArrayList<>();

                if (projectMasterList != null && projectMasterList.size() > 0) {
                    int masterProSize = projectMasterList.size();
                    for (int j = 0; j < masterProSize; j++) {
                        mProjectsList.add(new Projects(projectMasterList.get(j).getProjectId(), projectMasterList.get(j).getProjectName()));
                    }
                }
                if (selectedProjectsList != null && selectedProjectsList.size() > 0) {
                    int selectedLength = selectedProjectsList.size();
                    for (int j = 0; j < selectedLength; j++) {
                        mSelectedMultiProjList.add(new Projects(selectedProjectsList.get(j).getProjectId(), selectedProjectsList.get(j).getProjectName()));
                    }
                }
                if (leadStatusEntityList != null && leadStatusEntityList.size() > 0) {
                    int leadStatusSize = leadStatusEntityList.size();
                    for (int j = 0; j < leadStatusSize; j++) {
                        mLeadStatusList.add(new LeadStatus(leadStatusEntityList.get(j).getStatusId(), leadStatusEntityList.get(j).getStatusName()));
                    }
                }
                if (!TextUtils.isEmpty(detailsEntity.getUpdateStatusList())) {
                  /*  String[] strArr = detailsEntity.getSubStatus().trim().split(",");
                    String[] strIdArr = detailsEntity.getSubStatusId().trim().split(",");
                    for (int i = 0; i < strArr.length; i++) {
                        SubStatus sStatus = new SubStatus(strIdArr[i], strArr[i]);
                        subStatus.add(sStatus);
                    }*/

                    updateStatusList = UpdateStatusConverter.fromString(detailsEntity.getUpdateStatusList());
                }

                if (notInterestedMasterList != null && notInterestedMasterList.size() > 0) {
                    int notInterLength = notInterestedMasterList.size();
                    for (int j = 0; j < notInterLength; j++) {
                        mNotInterestedList.add(new NotInterestedLead(notInterestedMasterList.get(j).getId(), notInterestedMasterList.get(j).getTitle(),
                                ""));
                    }
                }

                if (closureMasterList != null && closureMasterList.size() > 0) {
                    int closureSize = closureMasterList.size();
                    for (int j = 0; j < closureSize; j++) {
                        mClosureList.add(new NotInterestedLead(closureMasterList.get(j).getId(), closureMasterList.get(j).getTitle(),
                                ""));
                    }
                }

                if (brokerMasterList != null && brokerMasterList.size() > 0) {
                    int brokerSize = brokerMasterList.size();
                    for (int j = 0; j < brokerSize; j++) {
                        mBrokerList.add(new NotInterestedLead(brokerMasterList.get(j).getId(), brokerMasterList.get(j).getTitle(),
                                brokerMasterList.get(j).getAddress()));
                    }
                }

                mDetailsModel = new AsmSalesLeadDetailModel(detailsEntity.getEnquiryId(), detailsEntity.getCampaignName(),
                        detailsEntity.getCampaignDate(), detailsEntity.getCustomerName(), detailsEntity.getCustomerEmail(),
                        detailsEntity.getCustomerMobile(), detailsEntity.getCustomerAlternateMobile(), detailsEntity.getBudget(),
                        detailsEntity.getCurrentStatus(), detailsEntity.getLastupdatedon(), detailsEntity.getDate(), detailsEntity.getTime(),
                        detailsEntity.getNew_sub_status(), detailsEntity.getNew_sub_status_id(),
                        detailsEntity.getClosure_project_id(), detailsEntity.getAddress(), mSelectedMultiProjList,
                        updateStatusList, detailsEntity.getRemark(), detailsEntity.getLeadType(), detailsEntity.getNoOfPerson(),
                        detailsEntity.getAssignedTo(), detailsEntity.getIsLeadType(), detailsEntity.getNew_sub_status(), detailsEntity.getUnitNo(),
                        detailsEntity.getAmount(), "", "", "", ""
                        , detailsEntity.getLastupdatedon());

                mAsmModel = new AsmSalesModel(detailsEntity.getEnquiryId(), detailsEntity.getCustomerName(), detailsEntity.getCustomerMobile(),
                        detailsEntity.getCustomerEmail(), detailsEntity.getCampaignName(), detailsEntity.getCurrentStatus(),
                        detailsEntity.getProjects(), detailsEntity.getScheduledDateTime(), detailsEntity.getIsLeadType(),
                        detailsEntity.getIsAssigned(), detailsEntity.getUserAction(), detailsEntity.getLastupdatedon(), mDetailsModel);
                setLeadDetails(mDetailsModel);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isMultiSpinner != true) {
            alertDialog = new AlertDialog.Builder(FollowUpSalesDetailActivity.this);
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
            if (!TextUtils.isEmpty(mSelectedProjectName) && (!mSelectedProjectName.equalsIgnoreCase(getString(R.string.spinner_select_project)))) {
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

            spClosureProjectList.clear();
            spClosureProjectList.add(0, getString(R.string.spinner_select_any_project));
            for (int i = 0; i < selectedProjList.size(); i++) {
                spClosureProjectList.add(selectedProjList.get(i).getProject_name());
            }
            closureProjectAdapter.notifyDataSetChanged();

        } else {
            mSelectedProjectName = "";
            mSelectedProjectId = "";
            textView_projects.setText(getString(R.string.select_project));
        }
    }

    private void setMultiSelectedAdapter(View convertView, Set<Projects> set, List<Projects> mProjectsList) {
        if (convertView != null) {
            RecyclerView recyclerView = convertView.findViewById(R.id.proMulti_recyclerView);
            mAdapter = new MultiSelectAdapter(FollowUpSalesDetailActivity.this, mProjectsList, set);
            selectedProjList.clear();
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(FollowUpSalesDetailActivity.this, LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FollowUpSalesDetailActivity.this);
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

        spClosureProjectList.clear();
        for (int i = 0; i < mSeleMulti.size(); i++) {
            spClosureProjectList.add(mSeleMulti.get(i).getProject_name());
        }
        closureProjectAdapter.notifyDataSetChanged();
    }
}
