package com.sp;

import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.adapters.LeadTransactionProfilePagerAdapter;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.model.NetworkErrorObject;
import com.model.UpdatePersonalDetailRespModel;

public class LeadsTransactionProileTabsActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {

    private LeadTransactionProfilePagerAdapter mLeadTransactionProfilePagerAdapter;
    private ViewPager mViewPager;
    private  TabLayout tabLayout;
    private Toolbar toolbar;
    private NetworkErrorObject mNetworkErrorObject = null;
    public UpdatePersonalDetailRespModel mUpdatePersonalDetailRespModel;
    public String brokerId = "",brokerCode = "";

    @Override
    protected String setActionBarTitle() {
        return getString(R.string.payment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_transacion_profile);
        initViews();
        setListeners();
        setTypeface();
        if(getIntent()!= null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
            toolbar.setTitle(getIntent().getStringExtra(ParamsConstants.TITLE));
            IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            if(mIntentDataObject.getData() != null){
                brokerId = mIntentDataObject.getData().get(ParamsConstants.BROKER_ID);
                brokerCode = mIntentDataObject.getData().get(ParamsConstants.BROKER_CODE);
            }
        }
        mLeadTransactionProfilePagerAdapter = new LeadTransactionProfilePagerAdapter(brokerId,brokerCode,getSupportFragmentManager());
        mViewPager.setAdapter(mLeadTransactionProfilePagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initViews(){
        toolbar = setToolBar();
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
