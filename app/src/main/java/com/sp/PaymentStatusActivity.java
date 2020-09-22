package com.sp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.activities.CouponCodeDetailActivity;
import com.activities.NotificationsActivity;
import com.appnetwork.AsyncThread;
import com.appnetwork.VolleyRequestApi;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.utils.StringUtil;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentStatusActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView iv_status_image;
    private Button btn_done;
    private LinearLayout ll_container;
    private IntentDataObject mIntentDataObject = null;
    private LayoutInflater mLayoutInflater;
    private Typeface fonts = null;
    private TextView tv_processing_title, tv_processing_subtitle, tv_cheque_amount_val, tv_paymentPlan_val, tv_coupon_code_val;
    private Typeface typeface = null;
    private ScrollView scroll_view;
    private int hasExtStoragePermission;
    EditText tv_customer_email_val;
    private List<String> permissions = new ArrayList<String>();
    private BMHApplication app;
    private String navigationTag;
    private AsyncThread mAsyncThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        updateReadStatus();
        app = (BMHApplication) getApplication();
        typeface = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.payment_status));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scroll_view = findViewById(R.id.scroll_view);
        btn_done = findViewById(R.id.btn_done);
        btn_done.setTypeface(typeface);
        iv_status_image = findViewById(R.id.iv_status_image);
        tv_customer_email_val = findViewById(R.id.tv_customer_email_val);
        tv_processing_title = findViewById(R.id.tv_processing_title);
        tv_processing_subtitle = findViewById(R.id.tv_processing_subtitle);
        tv_paymentPlan_val = findViewById(R.id.tv_paymentPlan_val);
        tv_coupon_code_val = findViewById(R.id.tv_coupon_code_val);
        btn_done.setOnClickListener(mOnClickListener);

        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            navigationTag = getIntent().getStringExtra(NotificationsActivity.TAG);
            if (mIntentDataObject != null && mIntentDataObject.getData() != null) {
                if (TextUtils.isEmpty(navigationTag))
                    btn_done.setVisibility(View.VISIBLE);
                else if (navigationTag.equalsIgnoreCase(NotificationsActivity.TAG))
                    btn_done.setVisibility(View.GONE);
                else
                    btn_done.setVisibility(View.VISIBLE);
                final HashMap<String, String> searchParams = mIntentDataObject.getData();
                if (searchParams != null) {
                    //   mLayoutInflater = LayoutInflater.from(this);
                    final String paymentPlan = searchParams.get(getString(R.string.txt_payment_plan_key));
                    final String paymentDesc = searchParams.get(getString(R.string.txt_payment_plan_desc_key));
                    final String projectName = searchParams.get(getString(R.string.txt_project_name));
                    final String couponCode = searchParams.get(getString(R.string.txt_coupon_code));

                    ((TextView) findViewById(R.id.tv_order_no_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_order_no))));
                    ((TextView) findViewById(R.id.tv_unit_no_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_unit_no))));
                    ((TextView) findViewById(R.id.tv_project_name_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_project_name))));
                    ((TextView) findViewById(R.id.tv_payment_status_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.payment_status))));
                    ((TextView) findViewById(R.id.tv_payment_mode_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_payment_mode))));

                    String mode = searchParams.get(getString(R.string.txt_payment_mode));
                    if (!TextUtils.isEmpty(mode)) {
                        /**
                         *  PAYMENT MODE - NET BANKING, DEBIT CARD, CREDIT CARD
                         */
                        if (mode.equalsIgnoreCase(getString(R.string.txt_net_banking))) {
                            tv_processing_subtitle.setText(getString(R.string.unit_reserve_request));
                            ((TextView) findViewById(R.id.tv_cheque_no)).setText(getString(R.string.txt_transaction_no));
                            ((TextView) findViewById(R.id.tv_cheque_amount)).setText(getString(R.string.txt_transaction_amount));
                            ((TextView) findViewById(R.id.tv_cheque_date)).setText(getString(R.string.txt_transaction_date));

                        } else if (mode.equalsIgnoreCase(getString(R.string.txt_debit_card))) {
                            tv_processing_subtitle.setText(getString(R.string.unit_reserve_request));
                            ((TextView) findViewById(R.id.tv_cheque_no)).setText(getString(R.string.txt_transaction_no));
                            ((TextView) findViewById(R.id.tv_cheque_amount)).setText(getString(R.string.txt_transaction_amount));
                            ((TextView) findViewById(R.id.tv_cheque_date)).setText(getString(R.string.txt_transaction_date));

                        } else if (mode.equalsIgnoreCase(getString(R.string.txt_credit_card))) {
                            tv_processing_subtitle.setText(getString(R.string.unit_reserve));
                            ((TextView) findViewById(R.id.tv_cheque_no)).setText(getString(R.string.txt_transaction_no));
                            ((TextView) findViewById(R.id.tv_cheque_amount)).setText(getString(R.string.txt_transaction_amount));
                            ((TextView) findViewById(R.id.tv_cheque_date)).setText(getString(R.string.txt_transaction_date));
                        } else {
                            /**
                             *  PAYMENT MODE - CHEQUE/DD
                             */
                            tv_processing_subtitle.setText(getString(R.string.unit_reserve_request));
                            ((TextView) findViewById(R.id.tv_cheque_no)).setText(getString(R.string.txt_cheque_no));
                            ((TextView) findViewById(R.id.tv_cheque_amount)).setText(getString(R.string.txt_cheque_amount));
                            ((TextView) findViewById(R.id.tv_cheque_date)).setText(getString(R.string.txt_cheque_date));
                        }
                    }
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
                    ((TextView) findViewById(R.id.tv_cheque_no_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_cheque_no))));
                    tv_cheque_amount_val = findViewById(R.id.tv_cheque_amount_val);
                    tv_cheque_amount_val.setTypeface(face);
                    tv_cheque_amount_val.setText(getResources().getString(R.string.rs) + StringUtil.amountCommaSeparate(searchParams.get(getString(R.string.txt_cheque_amount))));
                    ((TextView) findViewById(R.id.tv_cheque_date_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_cheque_date))));

                    ((TextView) findViewById(R.id.tv_customer_name_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_customer_name))));
                    String email = searchParams.get(getString(R.string.txt_customer_email));
                    if (TextUtils.isEmpty(email))
                        email = app.getFromPrefs(ParamsConstants.EMAIL_ID);
                    tv_customer_email_val.setText(getString(R.string.colon, email));
                    tv_customer_email_val.setKeyListener(null);
                    ((TextView) findViewById(R.id.tv_customer_mobile_no_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_customer_mobile_no))));
                    ((TextView) findViewById(R.id.tv_pan_aadhar_no_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_pan_aadhar_no))));

                    //    ((TextView) findViewById(R.id.tv_coupon_code_val)).setText(getString(R.string.colon, searchParams.get(getString(R.string.txt_coupon_code))));
                    if (!TextUtils.isEmpty(couponCode))
                        tv_coupon_code_val.setText(getString(R.string.colon, couponCode));
                    else {
                        (findViewById(R.id.tv_coupon_code_divider)).setVisibility(View.GONE);
                        tv_coupon_code_val.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(paymentPlan)) {
                        tv_paymentPlan_val.setText(getString(R.string.colon, paymentPlan));
                        tv_paymentPlan_val.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent coupon_code_intent = new Intent(PaymentStatusActivity.this
                                        , CouponCodeDetailActivity.class);
                                coupon_code_intent.putExtra("coupon_code", paymentPlan);
                                coupon_code_intent.putExtra("coupon_code_desc", paymentDesc);
                                coupon_code_intent.putExtra("Project_name", projectName);
                                startActivity(coupon_code_intent);
                            }
                        });
                    } else {
                        (findViewById(R.id.layout_payment_plan_divider)).setVisibility(View.GONE);
                        (findViewById(R.id.layout_payment_plan)).setVisibility(View.GONE);
                    }
                    if (TextUtils.isEmpty(paymentDesc)) {
                        tv_paymentPlan_val.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        tv_paymentPlan_val.setEnabled(false);
                    }

                    String status = searchParams.get(getString(R.string.payment_status));
                    if (!TextUtils.isEmpty(status) && (status.equalsIgnoreCase(getString(R.string.txt_success)) || status.equalsIgnoreCase(getString(R.string.txt_transaction_success)) || status.equalsIgnoreCase(getString(R.string.txt_under_process)))) {
                        iv_status_image.setImageResource(R.drawable.success);
                        iv_status_image.setVisibility(View.VISIBLE);
                        tv_processing_title.setVisibility(View.VISIBLE);
                        tv_processing_subtitle.setVisibility(View.VISIBLE);
                    } else {
                        iv_status_image.setImageResource(R.drawable.failure);
                        iv_status_image.setVisibility(View.VISIBLE);
                        tv_processing_title.setVisibility(View.GONE);
                        tv_processing_subtitle.setVisibility(View.GONE);
                    }
                }
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void updateReadStatus() {
        String notificationId = getIntent().getStringExtra("notification_id");
        VolleyRequestApi requestApi = new VolleyRequestApi(notificationId,
                WEBAPI.getWEBAPI(APIType.READ_STATUS), PaymentStatusActivity.this);
        requestApi.updateNotificationReadStatus();
    }

    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_done:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermissions()) {
                            saveScreenShot();
                        } else {
                            requestPermissions();
                        }
                    } else {
                        saveScreenShot();
                    }
                    break;
            }
        }
    };

    private boolean checkPermissions() {
        hasExtStoragePermission = ContextCompat.checkSelfPermission(PaymentStatusActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasExtStoragePermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (hasExtStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.PERMISSION_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.payment_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                if (TextUtils.isEmpty(navigationTag))
                    Utils.gotToHome(PaymentStatusActivity.this);
                else if (navigationTag.equalsIgnoreCase(NotificationsActivity.TAG))
                    super.onBackPressed();
                else
                    Utils.gotToHome(PaymentStatusActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BMHConstants.PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        saveScreenShot();
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

    private void saveScreenShot() {
        scroll_view.getChildAt(0).setBackgroundResource(R.color.white);
        Bitmap bmp = Bitmap.createBitmap(scroll_view.getChildAt(0).getWidth(), scroll_view.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        scroll_view.getChildAt(0).draw(c);
        //Bitmap bmp = Utils.takeScreenshot(PaymentStatusActivity.this,R.id.scroll_view);
        if (bmp != null) {
            if (!Utils.saveBitmap(PaymentStatusActivity.this, bmp)) {

            }
        }
        Utils.gotToHome(PaymentStatusActivity.this);
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(navigationTag))
            Utils.gotToHome(PaymentStatusActivity.this);
        else if (navigationTag.equalsIgnoreCase(NotificationsActivity.TAG))
            super.onBackPressed();
        else
            Utils.gotToHome(PaymentStatusActivity.this);
    }
}
