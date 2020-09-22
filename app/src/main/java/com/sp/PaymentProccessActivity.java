package com.sp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.VO.UnitDetailVO;
import com.adapters.PaymentFragmentsAdapter;
import com.helper.BMHConstants;
import com.views.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.List;

public class PaymentProccessActivity extends BaseFragmentActivity {
    // private Activity ctx = this;
    private LinearLayout locateBtnLayout;
    private LinearLayout userBtnLayout;
    private LinearLayout profileLayout;
    private LinearLayout breakupLayout;

    private TextView locateBtn;
    private TextView userBtn;
    private TextView profileBtn;
    private TextView breakupBtn;

    public UnitDetailVO unitDetailVO;
    private int hasExtCallPermission;
    private List<String> permissions = new ArrayList<String>();
    public NonSwipeableViewPager pager;
    public String projectId;
    public static String getPaymentPlan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_on);

        Toolbar toolbar = setToolBar();
        // toolbar.setLogo(R.drawable.arrownav);
        toolbar.setTitle("Checkout");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String type = getIntent().getStringExtra("type");
        unitDetailVO = getIntent().getParcelableExtra("unitvo");
        projectId = unitDetailVO.getProperty_id();
        locateBtnLayout = (LinearLayout) findViewById(R.id.locateBtnLayout);
        userBtnLayout = (LinearLayout) findViewById(R.id.userBtnLayout);
        profileLayout = (LinearLayout) findViewById(R.id.profileBtnLayout);
        breakupLayout = (LinearLayout) findViewById(R.id.BreakUpBtnLayout);

        locateBtn = (TextView) findViewById(R.id.locateBtn);
        userBtn = (TextView) findViewById(R.id.userBtn);
        profileBtn = (TextView) findViewById(R.id.profileBtn);
        breakupBtn = (TextView) findViewById(R.id.BreakUpBtn);

        pager = (NonSwipeableViewPager) findViewById(R.id.pagerDeveloper);
        int count = 4;
        if (!unitDetailVO.isIsformavailable()) {
            count = 3;
            profileLayout.setVisibility(View.GONE);
        }
        PaymentFragmentsAdapter adapter = new PaymentFragmentsAdapter(getSupportFragmentManager(), this, unitDetailVO, count, type);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(4);

        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setStyle(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.testing_on, menu);
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (unitDetailVO != null && unitDetailVO.getBuilder_contactno() != null && !unitDetailVO.getBuilder_contactno().isEmpty()) {
            menu.findItem(R.id.help).setTitle(getString(R.string.helpline) + " " + unitDetailVO.getBuilder_contactno());
        } else {
            menu.findItem(R.id.help).setTitle(getString(R.string.action_name));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.help:
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
            case android.R.id.home:
                finish();
                break;
        }
        // if (id == R.id.action_settings) {
        // return true;
        // }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermissions() {
        hasExtCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
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

    public void actionCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        if (unitDetailVO != null && unitDetailVO.getBuilder_contactno() != null && !unitDetailVO.getBuilder_contactno().isEmpty()) {
            callIntent.setData(Uri.parse("tel:" + unitDetailVO.getBuilder_contactno()));
        } else {
            callIntent.setData(Uri.parse(BMHConstants.CUSTOME_CARE));
        }
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
        startActivity(callIntent);
    }

    @Override
    protected String setActionBarTitle() {
        return "Payment";
    }

    private void setStyle(int position) {
        int selectedTabColor = getResources().getColor(R.color.blue_color);

        if (position == 0) {
            breakupLayout.setBackgroundColor(selectedTabColor);
            breakupBtn.setTextColor(selectedTabColor);
            locateBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            locateBtn.setTextColor(Color.parseColor("#454545"));
            userBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            userBtn.setTextColor(Color.parseColor("#454545"));
            profileLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            profileBtn.setTextColor(Color.parseColor("#454545"));

        } else if (position == 1) {

            breakupLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            breakupBtn.setTextColor(Color.parseColor("#454545"));
            locateBtnLayout.setBackgroundColor(selectedTabColor);
            locateBtn.setTextColor(selectedTabColor);
            userBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            userBtn.setTextColor(Color.parseColor("#454545"));
            profileLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            profileBtn.setTextColor(Color.parseColor("#454545"));
        } else if (position == 2) {
            locateBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            locateBtn.setTextColor(Color.parseColor("#454545"));
            userBtnLayout.setBackgroundColor(selectedTabColor);
            userBtn.setTextColor(selectedTabColor);
            profileLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            profileBtn.setTextColor(Color.parseColor("#454545"));
        } else if (position == 3) {
            locateBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            locateBtn.setTextColor(Color.parseColor("#454545"));
            userBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            userBtn.setTextColor(Color.parseColor("#454545"));
            profileLayout.setBackgroundColor(selectedTabColor);
            profileBtn.setTextColor(selectedTabColor);
        } else {
            locateBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            locateBtn.setTextColor(Color.parseColor("#454545"));
            userBtnLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            userBtn.setTextColor(Color.parseColor("#454545"));
            profileLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            profileBtn.setTextColor(Color.parseColor("#454545"));
        }
    }

    @Override
    public void onBackPressed() {
        switch (pager.getCurrentItem()) {
            case 0:
                super.onBackPressed();
                break;
            case 1:
                pager.setCurrentItem(0, true);
                break;
            case 2:
                pager.setCurrentItem(1, true);
                break;
            case 3:
                pager.setCurrentItem(2, true);
                break;
            case 4:
                pager.setCurrentItem(3, true);
                break;
            default:
                super.onBackPressed();
                break;
        }

    }
}
