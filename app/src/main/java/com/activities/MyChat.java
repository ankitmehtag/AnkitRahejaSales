package com.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.MyChatUnitsListAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.fragments.BaseFragment;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.HostActivityInterface;
import com.jsonparser.JsonParser;
import com.model.MyChatUnitsRespModel;
import com.nex3z.notificationbadge.NotificationBadge;
import com.sp.BMHApplication;
import com.sp.BaseFragmentActivity;
import com.sp.BlogActivity;
import com.sp.LoginActivity;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.sp.SearchPropertyActivity;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyChat extends BaseFragmentActivity implements HostActivityInterface {
    private BMHApplication app;
    public static final String TAG = MyChat.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyChatUnitsListAdapter myChatUnitsListAdapter = null;
    Toolbar toolbar;
    private BaseFragment selectedFragment;
    private ImageView iv_projects, iv_transactions, iv_blog, iv_notification;
    private TextView iv_chat;
    String id;
    NotificationBadge mBadge;
    private Typeface font;
    private ProgressDialog dialog;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);
        app = (BMHApplication) getApplication();
        initViews();
        setListeners();
        font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        toolbar = setDrawerAndToolbar();
        toolbar.setTitle("Chats");
        if (Connectivity.isConnected(this)) {
            getBadgeCount();
            getMyChatUnits();
        } else {
            recyclerView.setBackgroundResource(R.drawable.no_internet);
        }
    }

    private void getMyChatUnits() {

        dialog.setMessage("Please wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.MY_CHAT_UNITS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                        Toast.makeText(MyChat.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    MyChatUnitsRespModel myChatUnitsRespModel = (MyChatUnitsRespModel) JsonParser.convertJsonToBean(APIType.MY_CHAT_UNITS, response);
                                    if (myChatUnitsRespModel != null) {
                                        final ArrayList<MyChatUnitsRespModel.Data> chatList;
                                        final MyChatUnitsRespModel.Data[] mData = new MyChatUnitsRespModel.Data[1];
                                        if (myChatUnitsRespModel.isSuccess() && myChatUnitsRespModel.getData() != null) {
                                            chatList = myChatUnitsRespModel.getData();
                                            myChatUnitsListAdapter = new MyChatUnitsListAdapter(MyChat.this, chatList);

                                            recyclerView.setAdapter(myChatUnitsListAdapter);
                                            recyclerView.addItemDecoration(new MyDividerItemDecoration(MyChat.this, LinearLayoutManager.VERTICAL, 5));
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                                            layoutManager = new LinearLayoutManager(MyChat.this);
                                            recyclerView.setLayoutManager(layoutManager);

                                        } else {
                                            recyclerView.setBackgroundResource(R.drawable.no_chat_initiated);
                                        }
                                    } else {
                                        Toast.makeText(MyChat.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    recyclerView.setBackgroundResource(R.drawable.no_chat_initiated);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MyChat.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_TYPE, BMHConstants.SALES_PERSON);
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
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initViews() {
        dialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.chat_recyclerView);
        iv_projects = findViewById(R.id.iv_projects);
        iv_blog = findViewById(R.id.iv_blog);
        iv_transactions = findViewById(R.id.iv_transactions);
        mBadge = findViewById(R.id.badge_count);
        iv_chat = findViewById(R.id.iv_chat);
        iv_notification = findViewById(R.id.iv_notification);
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
                    Intent projectsIntent = new Intent(MyChat.this, ProjectsListActivity.class);
                    startActivity(projectsIntent);
                    finish();
                    break;
                case R.id.iv_blog:
                    Intent blogIntent = new Intent(MyChat.this, BlogActivity.class);
                    startActivity(blogIntent);
                    finish();
                    break;
                case R.id.iv_transactions:
                    Intent transactionIntent = new Intent(MyChat.this, SalesActivity.class);
                    startActivity(transactionIntent);
                    finish();
                    break;
                case R.id.iv_chat:
                    //TODO: do nothing
                    break;
                case R.id.iv_notification:
                    //TODO: do nothing
                    id = app.getFromPrefs(BMHConstants.USERID_KEY);
                    if (!id.isEmpty()) {
                        Intent notifyIntent = new Intent(MyChat.this, NotificationsActivity.class);
                        startActivity(notifyIntent);
                        finish();
                    } else {
                        app.showToastAtCenter(MyChat.this, "Please login.");
                        Intent i = new Intent(MyChat.this, LoginActivity.class);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                break;

        }
        return super.onOptionsItemSelected(item);
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
}
