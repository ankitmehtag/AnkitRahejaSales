package com.sp;

import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.adapters.PaymentMethodsPagerAdapter;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.model.NetworkErrorObject;
import com.model.UpdatePersonalDetailRespModel;

import java.util.HashMap;

public class PayUPaymentProcessActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {

    private PaymentMethodsPagerAdapter mPaymentMethodsPagerAdapter;
    private ViewPager mViewPager;
    private  TabLayout tabLayout;
    private Toolbar toolbar;
    private NetworkErrorObject mNetworkErrorObject = null;
    public UpdatePersonalDetailRespModel mUpdatePersonalDetailRespModel;


    @Override
    protected String setActionBarTitle() {
        return getString(R.string.payment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_upayment_process);
        initViews();
        setListeners();
        setTypeface();
        mPaymentMethodsPagerAdapter = new PaymentMethodsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPaymentMethodsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null
                && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
            IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            mUpdatePersonalDetailRespModel = (UpdatePersonalDetailRespModel) mIntentDataObject.getObj();
           HashMap<String,String> map = mIntentDataObject.getData();
            if(map != null && !map.isEmpty()){
                String unitId = map.get(ParamsConstants.UNIT_ID);
                String displayName = map.get(ParamsConstants.DISPLAY_NAME);
                toolbar.setTitle(displayName);
                toolbar.setSubtitle(unitId);
            }

        }
    }

    private void initViews(){
        toolbar = setToolBar();
        toolbar.setTitle(getString(R.string.payment));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


    }
    private void setListeners() {

    }
    private void setTypeface() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
    }

    public boolean isValidData() {
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        app = (BMHApplication) getApplication();
        BMHApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            showToast("Network available");
            if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
            switch (mNetworkErrorObject.getUiEventType()){
                case RETRY_LOGIN:
                    break;
                }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }else{
            showToast("Network not available");
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay_upayment_process, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
