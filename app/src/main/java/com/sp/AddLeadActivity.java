package com.sp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.jsonparser.JsonParser;
import com.model.AddLeadReqModel;
import com.model.AddLeadData;
import com.model.AddLeadRespModel;
import com.model.EnquiryProjectRespData;
import com.model.ProjectsData;
import com.utils.SharePrefUtil;
import com.utils.Utils;

import java.util.ArrayList;

public class AddLeadActivity extends BaseFragmentActivity {

    private static final String TAG = AddLeadActivity.class.getSimpleName();
    private static final int SEARCH_PROJECT = 1;
    private Toolbar toolbar;
    private Button btn_update, btn_cancel;
    private EditText et_name, et_email, et_mobile, et_alternate_number, et_budget,et_remark;
    private TextView tv_project;
    private String title = "Add Enquiry";
    private String columnName = "";
    private Typeface regular;
    public static ArrayList<ProjectsData> projectsDataArrayList;
    private AsyncThread mAsyncThread = null;
    private String SELECTED_PROJECT_ID = "";
    AlertDialog alertDialog;
    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lead);
        regular = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

        initViews();
        setListeners();
        setTypeface();
        getSupportActionBar().setTitle("Add Enquiry");
        if(projectsDataArrayList == null) {
            getProjects();
        }

    }


    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        tv_project = (TextView) findViewById(R.id.tv_project);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_alternate_number = (EditText) findViewById(R.id.et_alternate_number);
        et_budget = (EditText) findViewById(R.id.et_budget);
        et_remark = (EditText)findViewById(R.id.et_remark);
    }

    public void setListeners() {
        btn_update.setOnClickListener(mOnClickListener);
        btn_cancel.setOnClickListener(mOnClickListener);
        tv_project.setOnClickListener(mOnClickListener);
    }

    public void setTypeface() {
        btn_update.setTypeface(regular);
        btn_cancel.setTypeface(regular);
        et_name.setTypeface(regular);
        et_email.setTypeface(regular);
        et_mobile.setTypeface(regular);
        et_alternate_number.setTypeface(regular);
        et_budget.setTypeface(regular);
        et_remark.setTypeface(regular);
        tv_project.setTypeface(regular);
    }

    public boolean isValidData() {
        if (!Utils.isAutoDateTimeAndTimeZone(this)) {
            somethingWentWrongDialog(getString(R.string.error_auto_update_time));
            return false;
        } else if (et_name.getText().toString().trim().isEmpty()) {
            et_name.setSelection(et_name.getText().length());
            et_name.requestFocus();
            showToast(getString(R.string.enter_name));
            return false;
        }/*else if(et_email.getText().toString().trim().isEmpty()){
            et_email.setSelection(et_email.getText().length());
            et_email.requestFocus();
            showToast(getString(R.string.enter_email));
            return false;
        }*/ else if (!et_email.getText().toString().trim().isEmpty() && !Utils.isEmailValid(et_email.getText().toString().trim())) {
            et_email.setSelection(et_email.getText().length());
            et_email.requestFocus();
            showToast(getString(R.string.enter_valid_email));
            return false;
        } else if (et_mobile.getText().toString().trim().isEmpty()) {
            et_mobile.setSelection(et_mobile.getText().length());
            et_mobile.requestFocus();
            showToast(getString(R.string.enter_mobile));
            return false;
        } else if (et_mobile.getText().toString().trim().length() != 10) {
            et_mobile.setSelection(et_mobile.getText().length());
            et_mobile.requestFocus();
            showToast(getString(R.string.enter_valid_mobile));
            return false;
        } else if (tv_project.getText().toString().trim().equalsIgnoreCase(getString(R.string.select_project))) {
            showToast(getString(R.string.select_project));
            return false;
        }/*else if(et_alternate_number.getText().toString().isEmpty()){
            et_alternate_number.setSelection(et_alternate_number.getText().length());
            et_alternate_number.requestFocus();
            showToast(getString(R.string.enter_alternate_number));
            return false;
        }*/ else if (et_budget.getText().toString().trim().isEmpty()) {
            et_budget.setSelection(et_budget.getText().length());
            et_budget.requestFocus();
            showToast(getString(R.string.enter_budget));
            return false;
        }/*else if (et_remark.getText().toString().trim().isEmpty()) {
            et_remark.setSelection(et_remark.getText().length());
            et_remark.requestFocus();
            showToast(getString(R.string.enter_remark));
            return false;
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearForm() {
        et_name.setText("");
        et_email.setText("");
        et_mobile.setText("");
        tv_project.setText(getString(R.string.select_project));
        et_alternate_number.setText("");
        et_budget.setText("");
        et_remark.setText("");
        et_name.requestFocus();

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_update:
                    if(isValidData() && Utils.isAutoDateTimeAndTimeZone(AddLeadActivity.this)){
                     /* String json = dataReq();
                        addLead(json);*/

                        addDataInSharePref();
                        clearForm();
                        showToast("Enquiry added successfully");
                    }
                    break;
                case R.id.btn_cancel:
                    finish();
                    break;
                case R.id.tv_project:
                    if (projectsDataArrayList == null || projectsDataArrayList.size() == 0) {
                        showToast(getString(R.string.project_not_found));
                    } else {
                        Intent mIntent = new Intent(AddLeadActivity.this, SearchStatusActivity.class);
                        Bundle informacion = new Bundle();
                        informacion.putSerializable(BMHConstants.PROJECT_KEY, projectsDataArrayList);
                        mIntent.putExtra(BMHConstants.BUNDLE, informacion);
                        startActivityForResult(mIntent, SEARCH_PROJECT);
                    }
                    break;
            }
        }
    };

    private void addDataInSharePref(){
        String dataArray = SharePrefUtil.getEnquiry(this);
        ArrayList<AddLeadData> mAddLeadDatas = new ArrayList<>();
        if(dataArray != null && !dataArray.isEmpty()){
            mAddLeadDatas  = (ArrayList<AddLeadData>) JsonParser.convertJsonToBean(APIType.LEAD_DATA,dataArray);
        }

        AddLeadData mAddLeadData = new AddLeadData();
        mAddLeadData.setClient_name(et_name.getText().toString().trim());
        mAddLeadData.setClient_email(et_email.getText().toString().trim());
        mAddLeadData.setClient_number(et_mobile.getText().toString().trim());
        mAddLeadData.setClient_alternate_no(et_alternate_number.getText().toString().trim());
        mAddLeadData.setBudget(et_budget.getText().toString().trim());
        mAddLeadData.setRemark(et_remark.getText().toString().trim());
        mAddLeadData.setProject(tv_project.getText().toString().trim());
        mAddLeadData.setProject_id(SELECTED_PROJECT_ID);
        mAddLeadData.setBuilder_id(BMHConstants.CURRENT_BUILDER_ID);
        mAddLeadData.setUpdated_time(System.currentTimeMillis());
        mAddLeadDatas.add(mAddLeadData);

        String enqListData = JsonParser.convertBeanToJson(mAddLeadDatas);
        SharePrefUtil.setEnquiry(this,enqListData);
    }

    private String dataReq() {
        String json = "";
        AddLeadReqModel addLeadReqModel = new AddLeadReqModel();
        ArrayList<AddLeadData> mAddLeadDatas = new ArrayList<>();
        AddLeadData mAddLeadData = new AddLeadData();
        mAddLeadData.setClient_name(et_name.getText().toString().trim());
        mAddLeadData.setClient_email(et_email.getText().toString().trim());
        mAddLeadData.setClient_number(et_mobile.getText().toString().trim());
        mAddLeadData.setClient_alternate_no(et_alternate_number.getText().toString().trim());
        mAddLeadData.setBudget(et_budget.getText().toString().trim());
        mAddLeadData.setRemark(et_remark.getText().toString().trim());
        mAddLeadData.setProject(tv_project.getText().toString().trim());
        mAddLeadData.setBuilder_id(BMHConstants.CURRENT_BUILDER_ID);
        mAddLeadData.setUpdated_time(System.currentTimeMillis());

        mAddLeadDatas.add(mAddLeadData);
        addLeadReqModel.setDatalist(mAddLeadDatas);
        addLeadReqModel.setUserid(app.getFromPrefs(BMHConstants.USERID_KEY));
        json = JsonParser.convertBeanToJson(addLeadReqModel);

        return json;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_PROJECT) {
            if (resultCode == RESULT_OK) {
                tv_project.setText(data.getStringExtra(BMHConstants.PROJECT_NAME));
               SELECTED_PROJECT_ID = data.getStringExtra(BMHConstants.PROJECT_ID);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected void somethingWentWrongDialog(String msg){
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AddLeadActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    private void getProjects(){
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_BUILDER_PROJECTS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_BUILDER_PROJECTS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String dataParams = "builder_id="+BMHConstants.CURRENT_BUILDER_ID;
        if(dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(AddLeadActivity.this,mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
    private void addLead(String dataParams){
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.ADD_LEAD);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.ADD_LEAD));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if(dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(AddLeadActivity.this,mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }



    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if(mAsyncThread != null)mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                Log.i(TAG,mBean.getJson());
                switch (mBean.getApiType()) {
                    case GET_BUILDER_PROJECTS:
                        if(mBean.getJson() != null) {
                            final EnquiryProjectRespData mEnquiryProjectRespData = (EnquiryProjectRespData) com.jsonparser.JsonParser.convertJsonToBean(APIType.GET_BUILDER_PROJECTS, mBean.getJson());
                            if (mEnquiryProjectRespData != null) {
                                if(mEnquiryProjectRespData.isSuccess() && mEnquiryProjectRespData.getData() != null){
                                    projectsDataArrayList = mEnquiryProjectRespData.getData();
                                }else{
                                    showToast(getString(R.string.something_went_wrong));
                                    projectsDataArrayList = null;
                                }

                            }else{
                                showToast(getString(R.string.something_went_wrong));
                                projectsDataArrayList = null;
                            }
                        }else{
                            projectsDataArrayList = null;
                        }

                        break;
                    case ADD_LEAD:
                        if(mBean.getJson() != null) {
                            final AddLeadRespModel addLeadRespModel = (AddLeadRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.ADD_LEAD, mBean.getJson());
                            if (addLeadRespModel != null) {
                                if(addLeadRespModel.isSuccess()){
                                    clearForm();
                                    if(addLeadRespModel.getData() != null){
                                        String enqData = SharePrefUtil.getEnquiry(AddLeadActivity.this);
                                        ArrayList<AddLeadData> leadDataList = (ArrayList<AddLeadData>) JsonParser.convertJsonToBean(APIType.LEAD_DATA,enqData);
                                        if(leadDataList != null) {
                                            for (AddLeadData temp : addLeadRespModel.getData()) {
                                                for (int i = 0; i < leadDataList.size(); i++) {
                                                    if(temp.getUpdated_time() == leadDataList.get(i).getUpdated_time()){
                                                        leadDataList.remove(i);
                                                        break;
                                                    }
                                                }
                                                //TODO: remove from db
                                                showToast("Enquiry added successfully");
                                            }
                                            SharePrefUtil.setEnquiry(AddLeadActivity.this,JsonParser.convertBeanToJson(leadDataList));
                                        }
                                    }
                                }else{
                                    showToast(addLeadRespModel.getMessage());
                                }

                            }else{
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }else{
                            showToast(getString(R.string.something_went_wrong));
                        }

                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });


}
