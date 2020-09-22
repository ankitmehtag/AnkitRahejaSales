package com.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.database.task.CallRecordingTask;
import com.database.entity.CallRecordingEntity;
import com.helper.BMHConstants;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Utils;

public class AlertDialogActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AlertDialogActivity.class.getSimpleName();
    Button btnCancel, btnUpdate;
    ImageView img_close;
    private BMHApplication app;
    TextView txtDescription, tv_customer_name, tv_customer_mobile;
    private String customerName, enquiryId, mobileNo, currentPhase, assignType, mRecordingPath;
    private String userDesignation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        app = (BMHApplication) getApplication();
        btnCancel = findViewById(R.id.btnCancel);
        img_close = findViewById(R.id.img_close);
        btnUpdate = findViewById(R.id.btnUpdate);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_customer_mobile = findViewById(R.id.tv_customer_mobile);

        btnCancel.setOnClickListener(this);
        img_close.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        userDesignation = app.getFromPrefs((BMHConstants.USER_DESIGNATION));

        Intent intent = getIntent();
        enquiryId = intent.getStringExtra(BMHConstants.ENQUIRY_ID);
        customerName = intent.getStringExtra(BMHConstants.CUSTOMER_NAME);
        mobileNo = intent.getStringExtra(BMHConstants.MOBILE_NO);
        currentPhase = intent.getStringExtra(BMHConstants.CURRENT_PHASE);
        assignType = intent.getStringExtra(BMHConstants.ASSIGN_TYPE);
        mRecordingPath = intent.getStringExtra(BMHConstants.RECORDING_FILE_PATH);

        txtDescription = findViewById(R.id.tv_alert_description);
        txtDescription.setText(getString(R.string.text_alert_description));
        tv_customer_name.setText(customerName);
        tv_customer_mobile.setText(mobileNo);
        insertCallRecording(0);
    }

    private void insertCallRecording(int isStatusUpdate) {
        Uri fileUri = Uri.parse(mRecordingPath);
        new CallRecordingTask(this, new CallRecordingEntity(fileUri.getLastPathSegment(), enquiryId, mobileNo, 1, isStatusUpdate,
                "", Utils.getCurrentDateTime())).execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnCancel:
            case R.id.img_close:
                finish();
                break;

            case R.id.btnUpdate:
                Intent intent;
                if (currentPhase.equalsIgnoreCase(BMHConstants.LEAD_PHASE_PRE_SALES)) {
                    if (userDesignation.equalsIgnoreCase("0")) {
                        intent = new Intent(AlertDialogActivity.this, SpDetailsActivity.class);
                    } else {
                        intent = new Intent(AlertDialogActivity.this, AsmDetailsActivity.class);
                    }
                    intent.putExtra(BMHConstants.ENQUIRY_ID, enquiryId);
                    intent.putExtra(BMHConstants.RECORDING_FILE_PATH, mRecordingPath);
                    intent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    // intent.putExtra(BMHConstants.ALERT_DIALOG_TAG, TAG);
                    intent.putExtra(BMHConstants.PATH, TAG);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else { // SALES DIALOG
                    /*if (userDesignation.equalsIgnoreCase(BMHConstants.ASM_DESIGNATION)) {
                        intent = new Intent(AlertDialogActivity.this, AsmDetailsActivity.class);
                    } else {*/
                    if (assignType.equalsIgnoreCase("0")) {
                        intent = new Intent(AlertDialogActivity.this, AssignedSalesDetailActivity.class);
                        intent.putExtra(BMHConstants.SELECTED_TAB_NAME, getString(R.string.tab_assigned));
                    } else if (assignType.equalsIgnoreCase("1")) {
                        intent = new Intent(AlertDialogActivity.this, FollowUpSalesDetailActivity.class);
                        intent.putExtra(BMHConstants.SELECTED_TAB_NAME, getString(R.string.tab_follow_up));
                    } else {
                        intent = new Intent(AlertDialogActivity.this, ClosureSalesDetailActivity.class);
                        intent.putExtra(BMHConstants.SELECTED_TAB_NAME, getString(R.string.tab_closure));
                    }
                    //   }
                    intent.putExtra(BMHConstants.ENQUIRY_ID, enquiryId);
                    intent.putExtra(BMHConstants.RECORDING_FILE_PATH, mRecordingPath);
                    intent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    //   intent.putExtra(BMHConstants.ALERT_DIALOG_TAG, TAG);
                    intent.putExtra(BMHConstants.USER_DESIGNATION, userDesignation);
                    intent.putExtra(BMHConstants.PATH, TAG);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;


            default:
                finish();
                break;
        }

    }


}
