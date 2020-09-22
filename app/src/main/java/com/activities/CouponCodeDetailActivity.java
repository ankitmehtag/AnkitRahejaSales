package com.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;

import com.sp.BaseFragmentActivity;
import com.sp.R;

import java.util.Objects;

public class CouponCodeDetailActivity extends BaseFragmentActivity {

    String couponCodeDesc, couponCode, projectName;
    Intent intent;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_code_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        intent = getIntent();
        WebView webView = findViewById(R.id.webView_coupon);
        couponCode = intent.getStringExtra("coupon_code");
        projectName = intent.getStringExtra("Project_name");
        toolbar.setTitle(projectName);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        couponCodeDesc = intent.getStringExtra("coupon_code_desc");
        if (!TextUtils.isEmpty(couponCodeDesc)) {
            //   tv_coupon_code_desc.setText(Html.fromHtml(couponCodeDesc));
            webView.loadData(couponCodeDesc, "text/html", "UTF-8");
        }
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
    public void onBackPressed() {
        finish();
    }
}
