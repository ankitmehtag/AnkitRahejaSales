package com.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.activities.BlogFavActivity;
import com.sp.IVRLeads;
import com.activities.InventoriesActivity;
import com.activities.MyTransactionsActivity;
import com.activities.PreSalesActivity;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.database.task.ClearDatabaseTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.sp.BMHApplication;
import com.sp.BrokersListActivity;
import com.sp.ContactUsActivity;
import com.sp.FavActivity;
import com.sp.LoginActivity;
import com.sp.MyDealsActivity;
import com.sp.ProfileActivity;
import com.sp.R;

import java.io.File;


public class DrawerFragment extends Fragment {

    public ActionBarDrawerToggle mDrawerToggle;
    private BMHApplication app;
    private DrawerLayout drawerlayout;
    private TextView tv_user_name, tv_user_id, tv_user_firm, tv_user_verified, tv_pre_sales, tv_deals, tv_my_leads, tv_my_transactions,tv_IVR, tv_invite_friends, tv_favourite,
            tv_share_broker_app, tv_contact_us, tv_login_logout, tv_mgr_title, tv_mgr_name, tv_mgr_email, tv_mgr_mob, btn_blog_fav;
    private ImageView iv_profile_pic;
    private View v;
    private int selectedId = 1;
    private String tagBeforeSearch = "";
    public int lastClickedId = R.id.tv_deals;
    private AsyncThread mAsyncThread = null;
    private ProgressBar progressBar;
    private final int LOGIN_REQ_CODE_FOR_FAV = 451;
    private final int LOGIN_REQ_CODE_FOR_PROFILE = 452;
    private final int LOGIN_REQ_CODE_FOR_MY_DEALS = 453;
    private final int LOGIN_REQ_CODE_FOR_BROKERS = 454;
    private final int LOGIN_REQ_CODE_FOR_PRE_SALES = 455;
    private final int LOGIN_REQ_CODE = 456;

    private LinearLayout ll_rm_details, layout_pre_sales;



    public DrawerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.left_slide_list_layout, container, false);
        app = (BMHApplication) getActivity().getApplication();
        iv_profile_pic = v.findViewById(R.id.iv_profile_pic);
        tv_user_name = v.findViewById(R.id.tv_user_name);
        tv_user_id = v.findViewById(R.id.tv_user_id);
        tv_user_firm = v.findViewById(R.id.tv_user_firm);
        tv_user_verified = v.findViewById(R.id.tv_user_verified);

        tv_deals = v.findViewById(R.id.tv_deals);
        layout_pre_sales = v.findViewById(R.id.layout_pre_sales);
        tv_pre_sales = v.findViewById(R.id.tv_pre_sales);
        tv_my_leads = v.findViewById(R.id.tv_my_leads);
        tv_my_transactions = v.findViewById(R.id.tv_my_transactions);
         tv_IVR=v.findViewById(R.id.ivr_calls);
        tv_invite_friends = v.findViewById(R.id.tv_brokers);
        tv_favourite = v.findViewById(R.id.tv_favourite);
        tv_share_broker_app = v.findViewById(R.id.tv_share_broker_app);
        tv_contact_us = v.findViewById(R.id.tv_contact_us);
        tv_login_logout = v.findViewById(R.id.tv_login_logout);
        ll_rm_details = v.findViewById(R.id.ll_rm_details);
        tv_mgr_title = v.findViewById(R.id.tv_mgr_title);
        tv_mgr_name = v.findViewById(R.id.tv_mgr_name);
        tv_mgr_email = v.findViewById(R.id.tv_mgr_email);
        btn_blog_fav = (TextView) v.findViewById(R.id.btn_blog_note);
        tv_mgr_mob = v.findViewById(R.id.tv_mgr_mob);
        progressBar = v.findViewById(R.id.progressBar);

        iv_profile_pic.setOnClickListener(mOnClickListener);
        tv_deals.setOnClickListener(mOnClickListener);
        tv_my_leads.setOnClickListener(mOnClickListener);
        if (app.getBooleanFromPrefs(BMHConstants.IS_PRE_SALES)) {
            layout_pre_sales.setVisibility(View.VISIBLE);
            tv_pre_sales.setOnClickListener(mOnClickListener);
        } else {
            layout_pre_sales.setVisibility(View.GONE);
            tv_pre_sales.setOnClickListener(null);
        }
        tv_IVR.setOnClickListener(mOnClickListener);
        tv_my_transactions.setOnClickListener(mOnClickListener);
        tv_invite_friends.setOnClickListener(mOnClickListener);
        tv_favourite.setOnClickListener(mOnClickListener);
        tv_share_broker_app.setOnClickListener(mOnClickListener);
        tv_contact_us.setOnClickListener(mOnClickListener);
        tv_login_logout.setOnClickListener(mOnClickListener);
        btn_blog_fav.setOnClickListener(mOnClickListener);

        getActivity().invalidateOptionsMenu();
        selectedId = 1;
        return v;

    }


    @SuppressLint("NewApi")
    public void setUp(DrawerLayout drawerlayout, Toolbar toolbar) {
        this.drawerlayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerlayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @SuppressLint("NewApi")
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (selectedId == R.id.iv_profile_pic) {
                    selectedId = 1;
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_PROFILE);
                    }
                    lastClickedId = R.id.iv_profile_pic;
                } else if (selectedId == R.id.tv_deals) {
                    selectedId = 1;
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent i = new Intent(getActivity(), MyDealsActivity.class);
                        i.putExtra(ParamsConstants.TITLE, "My Deals");
                        IntentDataObject mIntentDataObject = new IntentDataObject();
                        i.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                        startActivity(i);
                    }
                          else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_MY_DEALS);
                    }
                    lastClickedId = R.id.tv_deals;
                } else if (selectedId == R.id.tv_pre_sales) {
                    selectedId = 1;
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent intent = new Intent(getActivity(), PreSalesActivity.class);
                        startActivity(intent);
                    } else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_PRE_SALES);
                    }
                    lastClickedId = R.id.tv_pre_sales;
                } else if (selectedId == R.id.tv_my_leads) {
                    selectedId = 1;
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent i = new Intent(getActivity(), InventoriesActivity.class);
                        i.putExtra(ParamsConstants.TITLE, getString(R.string.txt_inventories));
                        IntentDataObject mIntentDataObject = new IntentDataObject();
                        i.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_MY_DEALS);
                    }
                    lastClickedId = R.id.tv_my_leads;
                } else if (selectedId == R.id.tv_my_transactions) {
                    selectedId = 1;
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {

                        Intent i = new Intent(getActivity(), MyTransactionsActivity.class);
                        i.putExtra(ParamsConstants.TITLE, getString(R.string.txt_my_transactions));
                        IntentDataObject mIntentDataObject = new IntentDataObject();
                        i.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_MY_DEALS);
                    }
                       lastClickedId = R.id.tv_my_transactions;

                }

                else if (selectedId == R.id.ivr_calls) {

                    selectedId = 1;
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent ivrleads = new Intent(getActivity(), IVRLeads.class);
                       /* i.putExtra(ParamsConstants.TITLE, getString(R.string.txt_my_ivrleads));
                        IntentDataObject mIntentDataObject = new IntentDataObject();
                        i.putExtra(IntentDataObject.OBJ, mIntentDataObject);*/
                        startActivity(ivrleads);

                           }


                          else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_MY_DEALS);

                             }

                             lastClickedId = R.id.ivr_calls;

                }


                else if (selectedId == R.id.btn_blog_note) {
                    selectedId = 1;
                    if (app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0) {
                        Intent blogList = new Intent(getActivity(), BlogFavActivity.class);
                        startActivity(blogList);
                    } else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE);
                    }
                    lastClickedId = R.id.btn_blog_note;
                } else if (selectedId == R.id.tv_brokers) {
                    selectedId = 1;
                    if (app.getFromPrefs(BMHConstants.USERID_KEY) != null && !app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent brokersList = new Intent(getActivity(), BrokersListActivity.class);
                        startActivity(brokersList);
                    } else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_BROKERS);
                    }
                    lastClickedId = R.id.tv_brokers;
                } else if (selectedId == R.id.tv_favourite) {
                    selectedId = 1;
                    if (app.getFromPrefs(BMHConstants.USERID_KEY) != null && !app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent fav = new Intent(getActivity(), FavActivity.class);
                        startActivity(fav);
                    } else {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_FAV);
                    }
                    lastClickedId = R.id.tv_favourite;
                } else if (selectedId == R.id.tv_share_broker_app) {
                    selectedId = 1;

                    inviteFriends(getString(R.string.link_broker_app));
                    lastClickedId = R.id.tv_share_broker_app;
                } else if (selectedId == R.id.tv_contact_us) {
                    selectedId = 1;
                    Intent i = new Intent(getActivity(), ContactUsActivity.class);
                    startActivity(i);
                    lastClickedId = R.id.tv_contact_us;
                } else if (selectedId == R.id.tv_login_logout) {
                    selectedId = 1;
                    if (tv_login_logout.getText().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.login))) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.putExtra("fromSplash", true);
                        startActivity(i);
                    } else if (tv_login_logout.getText().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.logout))) {
                        // clearLoginData();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("fromSplash", true);
                        intent.putExtra("classToStart", BMHConstants.HOME_ACTI);
                        new ClearDatabaseTask().execute();
                      /*  try {
                            File file = Utils.createFileDirectory(getContext());
                            //  Utils.deleteDirectory(file);
                            deleteRecursive(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        SharedPreferences p = getActivity().getSharedPreferences(BMHConstants.PREF_NAME, Activity.MODE_PRIVATE);
                        p.edit().clear().commit();
                        startActivity(intent);
                        getActivity().finish();
                    }
                    lastClickedId = R.id.tv_login_logout;
                }
            }
        };

        drawerlayout.setDrawerListener(mDrawerToggle);
        drawerlayout.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        System.out.println("hh onAttach");
    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        String username = app.getFromPrefs(BMHConstants.USERNAME_KEY);
        if (!username.isEmpty()) {
            tv_user_name.setVisibility(View.VISIBLE);
            tv_user_name.setText(username);
        }
        String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
        if (userid.isEmpty()) {
            tv_login_logout.setText(getActivity().getResources().getString(R.string.login));
        } else {
            tv_login_logout.setText(getActivity().getResources().getString(R.string.logout));
        }
    }


    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseFragment f = null;
            switch (v.getId()) {
                case R.id.iv_profile_pic:
                    selectedId = R.id.iv_profile_pic;
                    break;
                case R.id.tv_deals:
                    selectedId = R.id.tv_deals;
                    break;
                case R.id.tv_pre_sales:
                    selectedId = R.id.tv_pre_sales;
                    break;
                case R.id.tv_my_leads:
                    selectedId = R.id.tv_my_leads;
                    break;
                case R.id.tv_my_transactions:
                    selectedId = R.id.tv_my_transactions;
                    break;
                case R.id.ivr_calls:
                    selectedId = R.id.ivr_calls;
                    break;

                case R.id.tv_brokers:
                    selectedId = R.id.tv_brokers;
                    break;
                case R.id.tv_favourite:
                    selectedId = R.id.tv_favourite;
                    break;
                case R.id.tv_share_broker_app:
                    selectedId = R.id.tv_share_broker_app;
                    break;
                case R.id.btn_blog_note:
                    selectedId = R.id.btn_blog_note;
                    break;
                case R.id.tv_contact_us:
                    selectedId = R.id.tv_contact_us;
                    break;
                case R.id.tv_login_logout:
                    selectedId = R.id.tv_login_logout;
                    break;

            }
            drawerlayout.closeDrawers();
        }
    };


    @Override
    public void onResume() {
        progressBar.setVisibility(View.GONE);
		/*if(BMHConstants.PROFILE_IMG_URL != null && !BMHConstants.PROFILE_IMG_URL.isEmpty()) {
			progressBar.setVisibility(View.VISIBLE);
			Picasso.Builder builder = new Picasso.Builder(getActivity());
			builder.listener(new Picasso.Listener() {
				@Override
				public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
					exception.printStackTrace();
					progressBar.setVisibility(View.GONE);
				}

			});
			builder.build().load(BMHConstants.PROFILE_IMG_URL).placeholder(R.drawable.com_facebook_profile_picture_blank_portrait).error(R.drawable.com_facebook_profile_picture_blank_portrait).into(iv_profile_pic,new com.squareup.picasso.Callback() {
				@Override
				public void onSuccess() {
					progressBar.setVisibility(View.GONE);
				}
				@Override
				public void onError() {
					progressBar.setVisibility(View.GONE);
				}
			});
		}
		String username = app.getFromPrefs(BMHConstants.USERNAME_KEY);
		tv_user_name.setVisibility(View.VISIBLE);
		tv_user_name.setText(username);*/
        showDrawerData();
        super.onResume();
    }


    private void fetchUserDetails() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.PROFILE_DETAILS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.PROFILE_DETAILS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.USER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(userId);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(getActivity());
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(getActivity(), mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                //showConfirmAlert("","Unable to connect Server.Please try later");
                // showToast("Server error");
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case PROFILE_DETAILS:
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LOGIN_REQ_CODE_FOR_FAV:
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent intent = new Intent(getActivity(), FavActivity.class);
                        startActivity(intent);
                    }
                    break;
                case LOGIN_REQ_CODE_FOR_BROKERS:
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent intent = new Intent(getActivity(), BrokersListActivity.class);
                        startActivity(intent);
                    }
                    break;
                case LOGIN_REQ_CODE_FOR_PROFILE:
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        startActivity(intent);
                    }
                    break;
                case LOGIN_REQ_CODE_FOR_MY_DEALS:
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent intent = new Intent(getActivity(), MyDealsActivity.class);
                        startActivity(intent);
                    }
                    break;
                case LOGIN_REQ_CODE:
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent fav = new Intent(getActivity(), BlogFavActivity.class);
                        startActivity(fav);
                    }
                    break;
                case LOGIN_REQ_CODE_FOR_PRE_SALES:
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent intent = new Intent(getActivity(), PreSalesActivity.class);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void inviteFriends(String data) {
        Intent mailer = new Intent(Intent.ACTION_SEND);
        mailer.setType("text/plain");
        mailer.putExtra(Intent.EXTRA_TEXT, data);
        startActivity(Intent.createChooser(mailer, "Share"));
    }

    private void downloadApp() {
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    private void showDrawerData() {
        if (app.getFromPrefs(BMHConstants.USERNAME_KEY).isEmpty()) {
            tv_login_logout.setText(getActivity().getResources().getString(R.string.login));
        } else {
            tv_login_logout.setText(getActivity().getResources().getString(R.string.logout));
        }
        if (!app.getFromPrefs(BMHConstants.USERNAME_KEY).isEmpty()) {
            tv_user_name.setText(app.getFromPrefs(BMHConstants.USERNAME_KEY));
        } else {
            tv_user_name.setText("Guest");
        }
		/*if(!app.getFromPrefs(BMHConstants.USERID_KEY) .isEmpty()){
			tv_user_id.setVisibility(View.VISIBLE);
			tv_user_id.setText("( "+app.getFromPrefs(BMHConstants.BROKER_CODE)+" )");
		}else{
			tv_user_id.setVisibility(View.GONE);
		}
		if(!app.getFromPrefs(BMHConstants.FIRM_NAME).isEmpty()){
			tv_user_firm.setVisibility(View.VISIBLE);
			tv_user_firm.setText("Firm name "+app.getFromPrefs(BMHConstants.FIRM_NAME));
		}else{
			tv_user_firm.setVisibility(View.GONE);
		}
		if(Utils.toInt(app.getFromPrefs(BMHConstants.IS_VERIFIED_USER)) == BMHConstants.VERIFIED){
			tv_user_verified.setVisibility(View.VISIBLE);
		}
		else {
			tv_user_verified.setVisibility(View.GONE);
		}
		if(!app.getFromPrefs(BMHConstants.RM_NAME).isEmpty()){
			ll_rm_details.setVisibility(View.VISIBLE);
			tv_mgr_name.setText(app.getFromPrefs(BMHConstants.RM_NAME) + "( "+app.getFromPrefs(BMHConstants.RM_CODE)+" )");
		}else{
			tv_mgr_name.setVisibility(View.GONE);
			ll_rm_details.setVisibility(View.GONE);
		}
		if(!app.getFromPrefs(BMHConstants.RM_EMAIL_ID).isEmpty()){
			tv_mgr_email.setVisibility(View.VISIBLE);
			tv_mgr_email.setText("Email: "+app.getFromPrefs(BMHConstants.RM_EMAIL_ID));
		}else{
			tv_mgr_email.setVisibility(View.GONE);
		}
		if(!app.getFromPrefs(BMHConstants.RM_MOBILE_NO).isEmpty()){
			tv_mgr_mob.setVisibility(View.VISIBLE);
			tv_mgr_mob.setText("Mobile: "+app.getFromPrefs(BMHConstants.RM_MOBILE_NO));
		}else{
			tv_mgr_mob.setVisibility(View.GONE);
		}*/
        updateProfilePic(app.getFromPrefs(BMHConstants.USER_IMAGE_URL));

    }

    private void clearLoginData() {
        app.saveIntoPrefs(BMHConstants.USERNAME_KEY, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.USERID_KEY, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.BROKER_CODE, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.FIRM_NAME, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.IS_VERIFIED_USER, "0");
        app.saveIntoPrefs(BMHConstants.RM_NAME, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.RM_CODE, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.RM_EMAIL_ID, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.RM_MOBILE_NO, BMHConstants.DEFAULT_VALUE);
        app.saveIntoPrefs(BMHConstants.USER_IMAGE_URL, BMHConstants.DEFAULT_VALUE);
    }


    private void updateProfilePic(String url) {
        if (url == null || url.isEmpty() || progressBar == null || iv_profile_pic == null) return;
        Glide.with(getActivity())
                .load(UrlFactory.IMG_BASEURL + url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
              //  .placeholder(R.drawable.ic_profile_circle)
                .error(R.drawable.no_image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(iv_profile_pic);
    }

}
