package com.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.fragments.MyTransChannelFragment;
import com.fragments.MyTransDirectFragment;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyTransactionsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = MyTransactionsActivity.class.getCanonicalName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private BMHApplication app;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public Bundle masterBundle;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transactions);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        app = (BMHApplication) getApplication();
        tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.viewPager);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(Connectivity.isConnected(this)){
            mViewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            // API CALL
            getMyTransactionsList();
        }else {
           // mViewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mViewPager.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.no_internet));
            } else {
                mViewPager.setBackground(ContextCompat.getDrawable(this, R.drawable.no_internet));
            }
        }
    }

    private void getMyTransactionsList() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.MY_TRANSACTIONS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        if (response != null) {
                            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                            masterBundle = new Bundle();
                            masterBundle.putString("JSON_STRING", response);
                            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            mViewPager.setAdapter(mSectionsPagerAdapter);

                            tabLayout.getTabAt(0).setText(MyTransactionsActivity.this.getResources().getString(R.string.tab_direct));
                            tabLayout.getTabAt(1).setText(MyTransactionsActivity.this.getResources().getString(R.string.tab_channel));
                            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.sky_blue));
                            tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
                            tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

                            tabLayout.setTabTextColors(
                                    ContextCompat.getColor(MyTransactionsActivity.this, R.color.black_alpha_40),
                                    ContextCompat.getColor(MyTransactionsActivity.this, R.color.black)
                            );

                            tabLayout.addOnTabSelectedListener(MyTransactionsActivity.this);
                            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                        } else {
                            Toast.makeText(MyTransactionsActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
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
                params.put(ParamsConstants.EMP_CODE, app.getFromPrefs(BMHConstants.SP_CODE));
                params.put(ParamsConstants.USER_DESIGNATION, app.getFromPrefs(BMHConstants.USER_DESIGNATION));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings ? true : super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        mSectionsPagerAdapter.getItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        //integer to count number of tabs
        int tabCount;

        private SectionsPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount; }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    return new MyTransDirectFragment();
                case 1:
                    return new MyTransChannelFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
