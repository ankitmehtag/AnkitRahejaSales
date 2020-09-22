package com.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.helper.BMHConstants;
import com.model.BrokerLeadsAssignedModel;
import com.sp.R;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeadAssignedDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String clientNumber;
    private String currentStatus;
    private String emailId;
    private int hasExtCallPermission;
    private List<String> permissions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_assigned_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        BrokerLeadsAssignedModel mData = getIntent().getParcelableExtra("LEADS_ASSIGNED_MODEL");
        String enquiry_code = mData.getEnquiry_code();
        toolbar.setTitle(getString(R.string.txt_enquiry_id_toolbar, enquiry_code));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String project_name = mData.getProject_name();
        emailId = mData.getEmail_id();
        currentStatus = mData.getEnquiry_status();
        clientNumber = mData.getMobile_number();
        String leadAddedOn = mData.getLead_added_on();
        String remark = mData.getRemark();
        String followUpDateTime = mData.getFollow_up_date_time();
        String customerName = mData.getCustomer_name();
        //     String address = mData.getAddress();

        ((TextView) findViewById(R.id.tv_projectName_val)).setText(getString(R.string.colon, project_name));
        ((TextView) findViewById(R.id.tv_current_status_val)).setText(getString(R.string.colon, currentStatus));
        ((TextView) findViewById(R.id.tv_client_number_val)).setText(getString(R.string.colon, clientNumber));
        ((TextView) findViewById(R.id.tv_lead_added_on_val)).setText(getString(R.string.colon, leadAddedOn));
        ((TextView) findViewById(R.id.tv_remarks_val)).setText(getString(R.string.colon, remark));
        ((TextView) findViewById(R.id.tv_followup_date_time_val)).setText(getString(R.string.colon, followUpDateTime));
        ((TextView) findViewById(R.id.tv_customerName_val)).setText(getString(R.string.colon, customerName));
        //   ((TextView) findViewById(R.id.tv_address_val)).setText(getString(R.string.colon, address));

        (findViewById(R.id.imageView_call)).setOnClickListener(this);
        (findViewById(R.id.imageView_message)).setOnClickListener(this);
        (findViewById(R.id.imageView_email)).setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_call:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermissions()) {
                        actionCall();
                    } else {
                        requestPermissions();
                    }
                } else {
                    actionCall();
                }
                break;
            case R.id.imageView_message:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + clientNumber));
                sendIntent.putExtra("sms_body", "Test SMS From BOOK MY HOUSE");
                startActivity(sendIntent);
                break;
            case R.id.imageView_email:
                //  sendEmail();
                if (TextUtils.isEmpty(emailId)) {
                    Utils.showToast(this, "Email id not available");
                } else {
                    Utils.openMailClient(this, "Sales Person App Email", new String[]{emailId}, "");
                }
                break;
        }
    }

    private boolean checkPermissions() {
        hasExtCallPermission = ContextCompat.checkSelfPermission(LeadAssignedDetailsActivity.this, Manifest.permission.CALL_PHONE);
        return hasExtCallPermission == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (hasExtCallPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CALL_PHONE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BMHConstants.PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        actionCall();
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void actionCall() {
        if (currentStatus.equalsIgnoreCase(getString(R.string.text_closure))) {
            Utils.showToast(this, getString(R.string.txt_lead_is_closed));
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + clientNumber));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }

    protected void sendEmail() {
        String[] TO = {emailId};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType(getString(R.string.txt_emial_format));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BMH EMAIL TEST");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        startActivity(emailIntent);
     /*   try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.txt_send_email)));
            finish();
            Log.i("Finished ", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(LeadAssignedDetailsActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }*/
    }
}
