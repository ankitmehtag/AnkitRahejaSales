package com.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
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

import com.AppEnums.APIType;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.database.entity.SalesClosureLeadsDetailEntity;
import com.database.task.GetClosureLeadDetailTask;
import com.database.task.InsertClosureLeadsDetailTask;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClosureSalesDetailActivity extends AppCompatActivity implements GetClosureLeadDetailTask.IClosureLeadDetailCommunicator {
    private static final String TAG = ClosureSalesDetailActivity.class.getSimpleName();
    private ProgressDialog dialog;
    private BMHApplication app;
    private TextView tv_closure_customerName_val, tv_closure_customerMobileNo_val, tv_closure_email_id_val, tv_closure_budget_val,
            tv_closure_projectName_val, tv_closure_sub_status_val, tv_closure_unit_no_val,
            tv_closure_transactionDatetime_val, tv_closure_transactionAmount_val, tv_closure_tower_no_val, tv_closure_cheque_no_val,
            tv_closure_cheque_date_val, tv_closure_bank_name_val, tv_closure_cheque_no, tv_closure_cheque_date;
    private EditText edt_closure_remark_val;
    private Button button_save;
    Toolbar toolbar;
    String enquiryId, userDesignation, prevRemarkText = "";
    LinearLayout layout_bankName, layout_bankName_divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_closure_details);
        initViews();
        app = (BMHApplication) getApplication();
        userDesignation = app.getFromPrefs((BMHConstants.USER_DESIGNATION));
        enquiryId = getIntent().getStringExtra(BMHConstants.ENQUIRY_ID);
       /* if (Connectivity.isConnected(this)) {
            getCloseLeadDetails(enquiryId);
        } else {*/
        new GetClosureLeadDetailTask(this, enquiryId).execute();
        // }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tv_closure_customerName_val = findViewById(R.id.tv_closure_customerName_val);
        tv_closure_customerMobileNo_val = findViewById(R.id.tv_closure_customerMobileNo_val);
        tv_closure_email_id_val = findViewById(R.id.tv_closure_email_id_val);
        tv_closure_budget_val = findViewById(R.id.tv_closure_budget_val);
        tv_closure_projectName_val = findViewById(R.id.tv_closure_projectName_val);
        tv_closure_sub_status_val = findViewById(R.id.tv_closure_sub_status_val);
        tv_closure_unit_no_val = findViewById(R.id.tv_closure_unit_no_val);
        tv_closure_transactionDatetime_val = findViewById(R.id.tv_closure_transactionDatetime_val);
        tv_closure_transactionAmount_val = findViewById(R.id.tv_closure_transactionAmount_val);
        tv_closure_tower_no_val = findViewById(R.id.tv_closure_tower_no_val);
        tv_closure_cheque_no = findViewById(R.id.tv_closure_cheque_no);
        tv_closure_cheque_no_val = findViewById(R.id.tv_closure_cheque_no_val);
        tv_closure_cheque_date = findViewById(R.id.tv_closure_cheque_date);
        tv_closure_cheque_date_val = findViewById(R.id.tv_closure_cheque_date_val);
        tv_closure_bank_name_val = findViewById(R.id.tv_closure_bank_name_val);
        edt_closure_remark_val = findViewById(R.id.tv_closure_remark_val);
        button_save = findViewById(R.id.button_save);
        layout_bankName = findViewById(R.id.layout_bankName);
        layout_bankName_divider = findViewById(R.id.layout_bankName_divider);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Typeface rupeeTypeFace = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
        tv_closure_budget_val.setTypeface(rupeeTypeFace);
        tv_closure_transactionAmount_val.setTypeface(rupeeTypeFace);


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // API CALL TO SAVE THE REMARK
                if (Utils.isChanged(prevRemarkText, edt_closure_remark_val.getText().toString())) {
                    if (TextUtils.isEmpty(edt_closure_remark_val.getText().toString())) {
                        Utils.showToast(ClosureSalesDetailActivity.this, getString(R.string.txt_enter_remarks));
                        return;
                    }
                    String remarks = edt_closure_remark_val.getText().toString();
                    int isSynced = 1;
                    if (Connectivity.isConnected(ClosureSalesDetailActivity.this)) {
                        saveLeadRemark(enquiryId, remarks);
                        isSynced = 0;
                    }
                    new InsertClosureLeadsDetailTask(ClosureSalesDetailActivity.this, enquiryId, remarks, isSynced).execute();
                    finish();
                } else {
                    Utils.showToast(ClosureSalesDetailActivity.this, getString(R.string.txt_msg_lead_not_updated));
                }
            }
        });
    }

  /*  private void getCloseLeadDetails(final String enquiry_id) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.CLOSE_LEADS_DETAILS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (response != null) {
                            ClosureDetailsModel closureDetailsModel = (ClosureDetailsModel) JsonParser.convertJsonToBean(APIType.CLOSE_LEADS_DETAILS, response);
                            if (closureDetailsModel.isSuccess()) {
                                setLeadData(closureDetailsModel);
                            } else {
                                Utils.showToast(ClosureSalesDetailActivity.this, getString(R.string.txt_no_data_found));
                            }
                        } else {
                            Utils.showToast(ClosureSalesDetailActivity.this, getString(R.string.something_went_wrong));
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
                params.put(ParamsConstants.USER_DESIGNATION, app.getFromPrefs(BMHConstants.USER_DESIGNATION));
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
    }*/

    private void saveLeadRemark(final String enquiryId, final String remarkText) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SAVE_REMARK),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Utils.showToast(ClosureSalesDetailActivity.this, jsonObject.optString("message"));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.showToast(ClosureSalesDetailActivity.this, getString(R.string.something_went_wrong));
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
                params.put(ParamsConstants.USER_DESIGNATION, app.getFromPrefs(BMHConstants.USER_DESIGNATION));
                params.put(ParamsConstants.ENQUIRY_ID, enquiryId);
                params.put(ParamsConstants.REMARK_TEXT, remarkText);
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

   /* private void setLeadData(ClosureDetailsModel closureDetailsModel) {
        ClosureDetailsModel.Data mData = closureDetailsModel.getData();

        toolbar.setTitle(getString(R.string.toolbar_txt_broker_status_and_type, mData.getName(), mData.getEnquiry_ID()));
        tv_closure_customerName_val.setText(getString(R.string.colon, mData.getName()));
        tv_closure_customerMobileNo_val.setText(getString(R.string.colon, mData.getMobile_No()));
        tv_closure_email_id_val.setText(getString(R.string.colon, mData.getEmail_ID()));
        if (mData.getBudget().equalsIgnoreCase(getString(R.string.txt_not_available)))
            tv_closure_budget_val.setText(getString(R.string.txt_budget_not_available, mData.getBudget()));
        else
            tv_closure_budget_val.setText(getString(R.string.rs) + mData.getBudget());
        tv_closure_projectName_val.setText(getString(R.string.colon, mData.getProject_name()));
        tv_closure_sub_status_val.setText(getString(R.string.colon, mData.getSubStatus()));
        tv_closure_unit_no_val.setText(getString(R.string.colon, mData.getUnit_no()));
        tv_closure_transactionDatetime_val.setText(getString(R.string.colon, mData.getLastupdatedon()));
        if (mData.getAmount().equalsIgnoreCase(getString(R.string.txt_not_available)))
            tv_closure_transactionAmount_val.setText(getString(R.string.colon, mData.getAmount()));
        else
            tv_closure_transactionAmount_val.setText(getString(R.string.colon_rs, StringUtil.amountCommaSeparate(mData.getAmount())));
        tv_closure_tower_no_val.setText(getString(R.string.colon, mData.getTower_no()));
        tv_closure_cheque_no_val.setText(getString(R.string.colon, mData.getCheque_number()));
        tv_closure_cheque_date_val.setText(getString(R.string.colon, mData.getCheque_date()));
        tv_closure_bank_name_val.setText(getString(R.string.colon, mData.getBank_name()));
        prevRemarkText = mData.getRemark();
        edt_closure_remark_val.setText(prevRemarkText);

    }*/

    private void setViewData(SalesClosureLeadsDetailEntity leadDetail) {

        toolbar.setTitle(getString(R.string.toolbar_txt_broker_status_and_type, leadDetail.getCustomerName(), leadDetail.getEnquiryId()));

        tv_closure_customerName_val.setText(getString(R.string.colon, leadDetail.getCustomerName()));
        tv_closure_customerMobileNo_val.setText(getString(R.string.colon, leadDetail.getCustomerMobile()));
        tv_closure_email_id_val.setText(getString(R.string.colon, leadDetail.getCustomerEmail()));
        if (leadDetail.getBudget().equalsIgnoreCase(getString(R.string.txt_not_available)))
            tv_closure_budget_val.setText(getString(R.string.txt_budget_not_available, leadDetail.getBudget()));
        else
            tv_closure_budget_val.setText(getString(R.string.rs) + leadDetail.getBudget());
        tv_closure_projectName_val.setText(getString(R.string.colon, leadDetail.getProjects()));
        tv_closure_sub_status_val.setText(getString(R.string.colon, leadDetail.getSubStatus()));
        tv_closure_unit_no_val.setText(getString(R.string.colon, leadDetail.getUnitNo()));
        tv_closure_transactionDatetime_val.setText(getString(R.string.colon, leadDetail.getLastupdatedon()));
        if (leadDetail.getTransactionAmount().equalsIgnoreCase(getString(R.string.txt_not_available)))
            tv_closure_transactionAmount_val.setText(getString(R.string.colon, leadDetail.getTransactionAmount()));
        else
            tv_closure_transactionAmount_val.setText(getString(R.string.colon_rs, StringUtil.amountCommaSeparate(leadDetail.getTransactionAmount())));
        tv_closure_tower_no_val.setText(getString(R.string.colon, leadDetail.getTowerNo()));
        if (leadDetail.getSubStatus().equalsIgnoreCase(getString(R.string.txt_online_transaction))) {
            layout_bankName.setVisibility(View.GONE);
            layout_bankName_divider.setVisibility(View.GONE);
            tv_closure_cheque_no.setText(getString(R.string.txt_transaction_No));
            tv_closure_cheque_no_val.setText(getString(R.string.colon, leadDetail.getChequeNo()));
            tv_closure_cheque_date.setText(getString(R.string.txt_transaction_date));
            tv_closure_cheque_date_val.setText(getString(R.string.colon, leadDetail.getChequeDate()));
        } else {
            layout_bankName.setVisibility(View.VISIBLE);
            layout_bankName_divider.setVisibility(View.VISIBLE);
            tv_closure_cheque_no_val.setText(getString(R.string.colon, leadDetail.getChequeNo()));
            tv_closure_cheque_date_val.setText(getString(R.string.colon, leadDetail.getChequeDate()));
            tv_closure_bank_name_val.setText(getString(R.string.colon, leadDetail.getBankName()));
        }
        prevRemarkText = leadDetail.getRemark();
        edt_closure_remark_val.setText(prevRemarkText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void closureLeadDetailsCallback(SalesClosureLeadsDetailEntity leadDetail) {
        if (leadDetail != null)
            setViewData(leadDetail);
    }
}
