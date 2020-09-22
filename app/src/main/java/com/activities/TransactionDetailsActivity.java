package com.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helper.BMHConstants;
import com.model.MyTransactionsRespModel;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.StringUtil;


public class TransactionDetailsActivity extends AppCompatActivity {
    TextView tv_coapplicant_val, tv_paymentPlan_val, tv_coupenCode_val;
    LinearLayout linear_layout_coapplicant, linear_layout_view;
    private BMHApplication app;
    LinearLayout view_divider_brokerage_term, brokerage_term;
    TextView tv_brokerage_terms, tv_brokerage_terms_val, tv_transactionAmount_val;
    String projectName, brokerName, brokerageTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        app = BMHApplication.getInstance();
        view_divider_brokerage_term = findViewById(R.id.view_divider_brokerage_term);
        brokerage_term = findViewById(R.id.brokerage_term);
        tv_brokerage_terms = findViewById(R.id.tv_brokerage_terms);
        tv_brokerage_terms_val = findViewById(R.id.tv_brokerage_terms_val);
        tv_transactionAmount_val = findViewById(R.id.tv_transactionAmount_val);
        tv_coapplicant_val = findViewById(R.id.tv_coapplicant_val);
        tv_paymentPlan_val = findViewById(R.id.tv_paymentPlan_val);
        tv_coupenCode_val = findViewById(R.id.tv_coupenCode_val);
        linear_layout_coapplicant = findViewById(R.id.linear_layout_coapplicant);
        linear_layout_view = findViewById(R.id.linear_layout_view);
        final MyTransactionsRespModel.Data mData = getIntent().getParcelableExtra("ChannelTransactionModel");
        String tagName = getIntent().getStringExtra("TAG_NAME");
        String customerName = mData.getCustomer_Name();
        String customerMobileNo = mData.getCustomer_Mobile_no();
        String orderId = mData.getOrder_no();

        toolbar.setTitle("Order Id (" + orderId + ")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String transactionId = mData.getTransaction_no();
        String unitId = mData.getUnit_no();
        projectName = mData.getProject_name();
        String unitSize = mData.getSize();
        String unitDisplayName = mData.getDisplay_name();
        String transactionDatetime = mData.getTransaction_datetime();
        String transactionAmount = mData.getPaid_amount();
        String coApplicant = mData.getCoapplicant();
        final String paymentPlan = mData.getPayment_plan();
        final String coupenCode = mData.getCoupen_code();
        String brokerCode = mData.getBroker_code();
        brokerName = mData.getBroker_name();
        String paymentMode = mData.getPayment_mode();
        String paymentStatus = mData.getStatus();
        brokerageTerm = mData.getBrokerage_terms();
        if (!TextUtils.isEmpty(brokerageTerm)) {
            addReadMore(getString(R.string.colon, brokerageTerm), (TextView) findViewById(R.id.tv_brokerage_terms_val));
        } else {
            brokerage_term.setVisibility(View.GONE);
            view_divider_brokerage_term.setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.tv_customerName_val)).setText(getString(R.string.colon, customerName));
        ((TextView) findViewById(R.id.tv_customerMobileNo_val)).setText(getString(R.string.colon, customerMobileNo));
        ((TextView) findViewById(R.id.tv_orderId_val)).setText(getString(R.string.colon, orderId));
        ((TextView) findViewById(R.id.tv_transactionId_val)).setText(getString(R.string.colon, transactionId));
        ((TextView) findViewById(R.id.tv_unitId_val)).setText(getString(R.string.colon, unitId));
        ((TextView) findViewById(R.id.tv_projectName_val)).setText(getString(R.string.colon, projectName));
        ((TextView) findViewById(R.id.tv_unitSize_val)).setText(getString(R.string.colon, unitSize));
        ((TextView) findViewById(R.id.tv_unitDisplayName_val)).setText(getString(R.string.colon, unitDisplayName));
        ((TextView) findViewById(R.id.tv_transactionDatetime_val)).setText(getString(R.string.colon, transactionDatetime));
        Typeface rupeeTypeFace = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
        tv_transactionAmount_val.setTypeface(rupeeTypeFace);
        if (TextUtils.isEmpty(transactionAmount)) {
            tv_transactionAmount_val.setText(getString(R.string.colon, getString(R.string.txt_not_available)));
        } else {
            if (transactionAmount.equalsIgnoreCase(getString(R.string.txt_not_available)))
                tv_transactionAmount_val.setText(getString(R.string.colon, transactionAmount));
            else {
                tv_transactionAmount_val.setText(getString(R.string.colon_rs, StringUtil.amountCommaSeparate(transactionAmount)));
            }
        }
        // tv_transactionAmount_val.setText(getString(R.string.colon_rs, transactionAmount));
        if (!TextUtils.isEmpty(coApplicant)) {
            tv_coapplicant_val.setText(getString(R.string.colon, coApplicant));
        } else {
            linear_layout_coapplicant.setVisibility(View.GONE);
            linear_layout_view.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(paymentPlan)) {
            tv_paymentPlan_val.setText(getString(R.string.colon, paymentPlan));
            tv_paymentPlan_val.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent coupon_code_intent = new Intent(TransactionDetailsActivity.this
                            , CouponCodeDetailActivity.class);
                    coupon_code_intent.putExtra("coupon_code", paymentPlan);
                    coupon_code_intent.putExtra("coupon_code_desc", mData.getPayment_plan_desc());
                    coupon_code_intent.putExtra("Project_name", projectName);
                    startActivity(coupon_code_intent);
                }
            });
        } else {
            (findViewById(R.id.layout_payment_plan_divider)).setVisibility(View.GONE);
            (findViewById(R.id.layout_payment_plan)).setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mData.getPayment_plan_desc())) {
            tv_paymentPlan_val.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tv_paymentPlan_val.setEnabled(false);
        }
        tv_coupenCode_val.setText(getString(R.string.colon, coupenCode));
        if (TextUtils.isEmpty(mData.getCoupon_code_description()) || coupenCode.equalsIgnoreCase("No Coupon Applied")) {
            tv_coupenCode_val.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tv_coupenCode_val.setEnabled(false);
        }
        if (!TextUtils.isEmpty(coupenCode)) {
            tv_coupenCode_val.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!coupenCode.equalsIgnoreCase("No Coupon Applied")) {
                        Intent coupon_code_intent = new Intent(TransactionDetailsActivity.this
                                , CouponCodeDetailActivity.class);
                        coupon_code_intent.putExtra("coupon_code", coupenCode);
                        coupon_code_intent.putExtra("coupon_code_desc", mData.getCoupon_code_description());
                        coupon_code_intent.putExtra("Project_name", projectName);
                        startActivity(coupon_code_intent);
                    }
                }
            });
        }

        if (tagName.equalsIgnoreCase("CHANNEL") &&
                app.getFromPrefs(BMHConstants.USER_DESIGNATION).equalsIgnoreCase("0")) {
            (findViewById(R.id.layout_brokerName_divider)).setVisibility(View.VISIBLE);
            (findViewById(R.id.layout_brokerCode_divider)).setVisibility(View.VISIBLE);
            (findViewById(R.id.layout_brokerName)).setVisibility(View.VISIBLE);
            (findViewById(R.id.layout_brokerCode)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_brokerCode_val)).setText(getString(R.string.colon, brokerCode));
            ((TextView) findViewById(R.id.tv_brokerName_val)).setText(getString(R.string.colon, brokerName));
        }
        ((TextView) findViewById(R.id.tv_payment_mode_val)).setText(getString(R.string.txt_broker_status_and_type, paymentMode, paymentStatus));
    }

    private void addReadMore(final String text, final TextView textView) {
        if (text.length() >= BMHConstants.BROKERAGE_MAX_TEXT_LENGTH) {
            SpannableString ss = new SpannableString(text.substring(0, BMHConstants.BROKERAGE_MAX_TEXT_LENGTH) + "... read more");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    // addReadLess(text, textView);
                    Intent intent = new Intent(TransactionDetailsActivity.this, CouponCodeDetailActivity.class);
                    intent.putExtra("coupon_code", brokerName);
                    intent.putExtra("Project_name", projectName);
                    intent.putExtra("coupon_code_desc", brokerageTerm);
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ds.setColor(getResources().getColor(R.color.blue_color, getTheme()));
                    } else {
                        ds.setColor(getResources().getColor(R.color.blue_color));
                    }
                }
            };
            ss.setSpan(clickableSpan, ss.length() - 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(ss);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        } else
            textView.setText(text);
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
}
