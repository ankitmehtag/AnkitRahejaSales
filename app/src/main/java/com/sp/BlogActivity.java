package com.sp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.activities.MyChat;
import com.activities.NotificationsActivity;
import com.activities.SalesActivity;
import com.adapters.BlogPagerAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.VolleyRequestApi;
import com.appnetwork.WEBAPI;
import com.fragments.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.HostActivityInterface;
import com.model.Category;
import com.nex3z.notificationbadge.NotificationBadge;
import com.utils.Connectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BlogActivity extends BaseFragmentActivity implements HostActivityInterface, TabLayout.OnTabSelectedListener {
    public static final String TAG = BlogActivity.class.getSimpleName();
    private ImageView iv_projects, iv_transactions, iv_chat, iv_notification;
    private TextView iv_blog;
    NotificationBadge mBadge;
    private BMHApplication app;
    private BlogPagerAdapter blogAdapter;
    private Activity ctx;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Gson gson;
    private Type listType;
    private BaseFragment selectedFragment;
    private ProgressDialog dialog;
    private ArrayList<Category> categoryList = new ArrayList<>();
    Toolbar toolbar;
    LinearLayout layout;
    View tabBelowLine;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        app = (BMHApplication) getApplication();
        toolbar = setDrawerAndToolbar();
        toolbar.setTitle("Blog");
        initViews();
        setListeners();
        if (Connectivity.isConnected(this)) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.show();
            String notificationIdBlog = getIntent().getStringExtra("notification_id");
            if (notificationIdBlog != null) {
                updateReadStatus();
            }
            tabBelowLine.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);

            gson = new Gson();
            listType = new TypeToken<ArrayList<Category>>() {
            }.getType();
            getBadgeCount();
            getBlog();
        } else {
            tabBelowLine.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                layout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.no_internet));
            } else {
                layout.setBackground(ContextCompat.getDrawable(this, R.drawable.no_internet));
            }
        }
        //web_blog.loadUrl(postUrl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void updateReadStatus() {
        String notificationId = getIntent().getStringExtra("notification_id");
        VolleyRequestApi requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), BlogActivity.this);
        requestApi.updateNotificationReadStatus();
    }

    private void getBadgeCount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BADGE_COUNT),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success")) {
                                JSONObject object = jsonObject.optJSONObject("data");
                                int badge_count = object.optInt("unread_count");
                                mBadge.setNumber(badge_count);
                            } else {
                                showToast(getString(R.string.something_went_wrong));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.APP_TYPE, BMHConstants.SALES_PERSON);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        BMHApplication.getInstance().

                addToRequestQueue(stringRequest, "headerRequest");
    }

    private void parseResponseJson(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObj = jsonObject.getJSONObject("data");
        JSONArray jsonArray = jsonObj.getJSONArray("category");
        categoryList = gson.fromJson(jsonArray.toString(), listType);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBlog();
    }

    public void getBlog() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.NEW_BLOG_API),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    parseResponseJson(jsonObject);
                                }
                                ArrayList<String> tabList = new ArrayList<>();
                                int categoryLength = categoryList.size();
                                if (categoryList != null) {
                                    String categoryName = null;
                                    for (int i = 0; i < categoryLength; i++) {
                                        categoryName = categoryList.get(i).getCategory_name();
                                        String str = categoryName.contains("amp;") ?
                                                categoryName.replaceAll("amp;", "") : categoryName;
                                        tabList.add(str);
                                    }
                                    blogAdapter = new BlogPagerAdapter(getSupportFragmentManager(), categoryLength, tabList, categoryList);
                                    mViewPager.setAdapter(blogAdapter);
                                    tabLayout.setupWithViewPager(mViewPager);
                                    tabLayout.addOnTabSelectedListener(BlogActivity.this);
                                    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                    String str = app.getTabFromPrefs("RECENT_BLOG_CATEGORY");
                                    if (!TextUtils.isEmpty(str)) {
                                        for (int i = 0; i < categoryLength; i++) {
                                            if (categoryList.get(i).getCategory_id().equalsIgnoreCase(str)) {
                                                mViewPager.setCurrentItem(i);
                                                blogAdapter.getItem(i);
                                                app.clearTabPref();
                                            }
                                        }
                                    }
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                } else
                                    Toast.makeText(BlogActivity.this, " No Data Found.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BlogActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, (BMHConstants.CURRENT_BUILDER_ID));
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_TYPE, BMHConstants.SALES_PERSON);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        stringRequest.setShouldCache(false);
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    public void initViews() {
        mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        iv_projects = findViewById(R.id.iv_projects);
        iv_blog = findViewById(R.id.iv_blog);
        iv_transactions = findViewById(R.id.iv_transactions);
        mBadge = findViewById(R.id.badge_count);
        iv_chat = findViewById(R.id.iv_chat);
        iv_notification = findViewById(R.id.iv_notification);
        layout = findViewById(R.id.blog_layout_view);
        tabBelowLine = findViewById(R.id.tab_line);
        // progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
    }


    public void setListeners() {
        iv_projects.setOnClickListener(mOnClickListener);
        iv_blog.setOnClickListener(mOnClickListener);
        iv_transactions.setOnClickListener(mOnClickListener);
        iv_chat.setOnClickListener(mOnClickListener);
        iv_notification.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_projects:
                    Intent projectsIntent = new Intent(BlogActivity.this, ProjectsListActivity.class);
                    startActivity(projectsIntent);
                    finish();
                    break;
                case R.id.iv_blog:
                    break;
                case R.id.iv_transactions:
                    Intent transactionIntent = new Intent(BlogActivity.this, SalesActivity.class);
                    startActivity(transactionIntent);
                    finish();
                    break;
                case R.id.iv_chat:
                    //TODO: do nothing
                    String id = app.getFromPrefs(BMHConstants.USERID_KEY);
                    id = app.getFromPrefs(BMHConstants.USERID_KEY);
                    if (!id.isEmpty()) {
                        Intent chatIntent = new Intent(BlogActivity.this, MyChat.class);
                        startActivity(chatIntent);
                        finish();
                    } else {
                        app.showToastAtCenter(BlogActivity.this, "Please login.");
                        Intent i = new Intent(BlogActivity.this, LoginActivity.class);
                        startActivityForResult(i, SearchPropertyActivity.FAV_REQ_CODE);
                    }
                    break;
                case R.id.iv_notification:
                    //TODO: do nothing
                    id = app.getFromPrefs(BMHConstants.USERID_KEY);
                    if (!id.isEmpty()) {
                        Intent notifyIntent = new Intent(BlogActivity.this, NotificationsActivity.class);
                        startActivity(notifyIntent);
                        finish();
                    } else {
                        app.showToastAtCenter(BlogActivity.this, "Please login.");
                        Intent i = new Intent(BlogActivity.this, LoginActivity.class);
                        startActivityForResult(i, SearchPropertyActivity.FAV_REQ_CODE);
                    }
                    break;

                default:
                    break;
            }

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings ? true : super.onOptionsItemSelected(item);
    }

    public boolean isValidData() {
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void setSelectedFragment(BaseFragment fragment) {
        this.selectedFragment = fragment;

    }

    public BaseFragment getSelectedFragment() {
        return this.selectedFragment;
    }

    @Override
    public void popBackStack() {
        // TODO Auto-generated method stub
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void popBackStackTillTag(String tag) {
        // TODO Auto-generated method stub
        getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean withAnimation) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (withAnimation) {
            // TO ENABLE FRAGMENT ANIMATION
            // Format: setCustomAnimations(old_frag_exit, new_frag_enter,
            // old_frag_enter, new_frag_exit);
            ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left,
                    R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
        }

        ft.replace(R.id.container, fragment, fragment.getTagText());
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            ft.addToBackStack(fragment.getTagText());
        }
        ft.commit();
    }

    @Override
    public void addMultipleFragments(BaseFragment[] fragments) {
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        blogAdapter = new BlogPagerAdapter(getSupportFragmentManager(), categoryList);
      /*  mViewPager.setCurrentItem(tab.getPosition());
        blogAdapter.getItem(tab.getPosition());*/
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}