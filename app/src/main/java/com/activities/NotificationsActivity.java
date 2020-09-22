package com.activities;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.AppEnums.APIType;
import com.VO.NewLaunchVO;
import com.VO.PropertyVO;
import com.adapters.NotificationsListAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.VolleyRequestApi;
import com.appnetwork.WEBAPI;
import com.fragments.BaseFragment;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.HostActivityInterface;
import com.jsonparser.JsonParser;
import com.model.NotificationsListRespModel;
import com.nex3z.notificationbadge.NotificationBadge;
import com.sp.BMHApplication;
import com.sp.BaseFragmentActivity;
import com.sp.BlogActivity;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsActivity extends BaseFragmentActivity implements HostActivityInterface {
    private BMHApplication app;
    //	private Activity ctx;
    public static final String TAG = NotificationsActivity.class.getSimpleName();
    private NewLaunchVO vo;
    private PropertyVO propertyDetails;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView_notification;
    ReqRespBean mBean;
    private Typeface font;
    private Toolbar toolbar;
    private BaseFragment selectedFragment;
    private NotificationsListAdapter notificationsListAdapter = null;
    private final int LOGIN_REQ_CODE = 451;
    private ImageView iv_blog, iv_transactions, iv_chat, iv_projects;
    private TextView tv_notification;
    NotificationBadge mBadge;
    private VolleyRequestApi requestApi;
    private NotificationsListRespModel.Data.TargetData mTargetInfo;
    private AsyncThread mAsyncThread = null;
    // ProgressDialog mProgressDialog;
    //  SwipeRefreshLayout swipeContainer;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification_list);
        font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        toolbar = setDrawerAndToolbar();
        toolbar.setTitle(BMHConstants.NOTIFICATION);
        //   swipeContainer = findViewById(R.id.swipe_refresh);
        initView();
        //  getBadgeCount();
        if (Connectivity.isConnected(this)) {
            requestApi = new VolleyRequestApi(this);
            getBadgeCountVolley();
            getNotificationsVolley();
        } else {
            recyclerView_notification.setBackgroundResource(R.drawable.no_internet);
        }
      /*  swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBadgeCountVolley();
                getNotificationsVolley();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
*/
    }

    private void initView() {
        app = (BMHApplication) getApplication();
        recyclerView_notification = findViewById(R.id.lv_notifications);
        iv_projects = findViewById(R.id.iv_projects);
        iv_blog = findViewById(R.id.iv_blog);
        iv_transactions = findViewById(R.id.iv_transactions);
        iv_chat = findViewById(R.id.iv_chat);
        tv_notification = findViewById(R.id.tv_notification);
        mBadge = findViewById(R.id.badge_count);
        iv_projects.setOnClickListener(mOnClickListener);
        iv_blog.setOnClickListener(mOnClickListener);
        iv_transactions.setOnClickListener(mOnClickListener);
        iv_chat.setOnClickListener(mOnClickListener);
        tv_notification.setOnClickListener(mOnClickListener);
    }

    private void getBadgeCountVolley() {

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

    private void getNotificationsVolley() {
      /*  mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.NOTIFICATIONS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    /*    if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }*/
                        NotificationsListRespModel notificationsListRespModel = (NotificationsListRespModel) JsonParser.convertJsonToBean(APIType.NOTIFICATIONS, response);
                        if (notificationsListRespModel != null) {
                            if (notificationsListRespModel.isSuccess() && notificationsListRespModel.getData() != null) {
                                final ArrayList<NotificationsListRespModel.Data> mArrayList = notificationsListRespModel.getData();
                                notificationsListAdapter = new NotificationsListAdapter(NotificationsActivity.this, mArrayList);
                                recyclerView_notification.setAdapter(notificationsListAdapter);
                                recyclerView_notification.addItemDecoration(new MyDividerItemDecoration(NotificationsActivity.this, LinearLayoutManager.VERTICAL, 0));
                                recyclerView_notification.setItemAnimator(new DefaultItemAnimator());
                                layoutManager = new LinearLayoutManager(NotificationsActivity.this);
                                recyclerView_notification.setLayoutManager(layoutManager);
                            } else {
                                recyclerView_notification.setBackgroundResource(R.drawable.no_notification);
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }
                        // swipeContainer.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }*/
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.DEVICE_ID, Utils.getDeviceId(NotificationsActivity.this));
                params.put(ParamsConstants.DEVICE_TYPE, BMHConstants.DEVICE_TYPE);
                params.put(ParamsConstants.USER_TYPE, BMHConstants.SALES_PERSON);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }


    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("hh location alert fragment acti result");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String projectId = app.getFromPrefs(BMHConstants.VALUE);
            String key = app.getFromPrefs(BMHConstants.KEY);
            if (projectId != null && !projectId.isEmpty() && key != null && !key.isEmpty()) {
                getNotificationsVolley();
                app.saveIntoPrefs(BMHConstants.VALUE, "");
            }
            //TODO
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBadgeCountVolley();
        getNotificationsVolley();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_projects:
                    Intent projectsIntent = new Intent(NotificationsActivity.this, ProjectsListActivity.class);

                    startActivity(projectsIntent);
                    finish();
                    break;
                case R.id.iv_blog:
                    Intent blogIntent = new Intent(NotificationsActivity.this, BlogActivity.class);
                    startActivity(blogIntent);
                    finish();
                    break;
                case R.id.iv_transactions:
                    Intent transactionIntent = new Intent(NotificationsActivity.this, SalesActivity.class);
                    startActivity(transactionIntent);
                    finish();
                    break;
                case R.id.iv_chat:
                    //TODO: do nothing
                    Intent chatIntent = new Intent(NotificationsActivity.this, MyChat.class);
                    startActivity(chatIntent);
                    finish();
                    break;
                case R.id.iv_notification:
                    //TODO: do nothing
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
