package com.activities;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.AppEnums.APIType;
import com.appnetwork.AsyncThread;
import com.appnetwork.VolleyRequestApi;
import com.appnetwork.WEBAPI;
import com.sp.BMHApplication;
import com.sp.BaseFragmentActivity;
import com.sp.R;

public class TermsAndConditionActivity extends BaseFragmentActivity {
    private View rootView;
    private BMHApplication app;
    private WebView web;
    public static final String TAG = TermsAndConditionActivity.class.getSimpleName();
    private Toolbar toolbar;
    private AsyncThread mAsyncThread = null;


    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        app = (BMHApplication) getApplication();
        updateReadStatus();
        toolbar = setToolBar();
        toolbar.setTitle("Terms And Conditions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        web = (WebView) findViewById(R.id.webViewAboutUs);
        String type = getIntent().getStringExtra("target_info");
        if (type != null) {
            web.loadUrl(type);
            web.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                    viewx.loadUrl(urlx);
                    return false;
                }
            });
        } else {
            return;
        }
    }

    private void updateReadStatus() {
        String notificationId = getIntent().getStringExtra("notification_id");
        VolleyRequestApi requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), TermsAndConditionActivity.this);
        requestApi.updateNotificationReadStatus();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //MenuItem item = menu.findItem(R.id.action_filter);
        //item.setIcon(R.drawable.ic_more_vert_white_24dp);
        return true;
        //item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
