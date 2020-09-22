package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fragments.AboutUsFragment;
import com.fragments.BaseFragment;
import com.fragments.ContactUsFragment;
import com.helper.BMHConstants;
import com.interfaces.HostActivityInterface;
import com.utils.Utils;


public class DashboardActivity extends BaseFragmentActivity implements HostActivityInterface{
    private final String TAG = DashboardActivity.class.getSimpleName();
    private ImageView iv_logo;
    private LinearLayout ll_about_us,ll_projects,ll_blog,ll_contact_us;
    private FloatingActionButton fab;
    private Button btn_logout;
    private LayoutInflater mLayoutInflater = null;
    private Typeface font;
    private BaseFragment selectedFragment;
    private Toolbar toolbar = null;




    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mLayoutInflater = LayoutInflater.from(this);
        font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        toolbar = setDrawerAndToolbar();
        initViews();
        includeView(ll_about_us,R.drawable.ic_subject_white_48dp,"","About Us");
        includeView(ll_projects,R.drawable.ic_business_white_48dp,"","Projects");
        includeView(ll_blog,R.drawable.ic_question_answer_white_48dp,"","Blog");
        includeView(ll_contact_us,R.drawable.ic_perm_contact_calendar_white_48dp,"","Contact Us");
        setListeners();
        toolbar.setTitle(BMHConstants.BUILDER_NAME);

        if (!Utils.isServiceRunning(DashboardActivity.this, SyncService.class)) {
            Intent serviceIntent = new Intent(DashboardActivity.this, SyncService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initViews() {
        btn_logout = (Button)findViewById(R.id.btn_logout);
        ll_about_us = (LinearLayout) findViewById(R.id.ll_about_us);
        ll_projects = (LinearLayout) findViewById(R.id.ll_projects);
        ll_blog = (LinearLayout) findViewById(R.id.ll_blog);
        ll_contact_us = (LinearLayout) findViewById(R.id.ll_contact_us);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }


    private View includeView(LinearLayout root,int icon,String titleTop,String titleBottom){
        View asssignedMeeting =  mLayoutInflater.inflate(R.layout.dashboard_item,root);
        ImageView iv_asgnd_mntng = (ImageView) asssignedMeeting.findViewById(R.id.iv_icon);
       // TextView tv_asgnd_mntng_count = (TextView) asssignedMeeting.findViewById(R.id.tv_count);
        TextView tv_asgnd_sv_title_top = (TextView) asssignedMeeting.findViewById(R.id.tv_title_top);
        TextView tv_asgnd_sv_title_bottom = (TextView) asssignedMeeting.findViewById(R.id.tv_title_bottom);

        tv_asgnd_sv_title_top.setTypeface(font);
//        tv_asgnd_mntng_count.setTypeface(font);
        tv_asgnd_sv_title_bottom.setTypeface(font);

        iv_asgnd_mntng.setImageResource(icon);
        // tv_asgnd_mntng_count.setText(count);
        tv_asgnd_sv_title_top.setText(titleTop);
        tv_asgnd_sv_title_bottom.setText(titleBottom);
        return asssignedMeeting;
    }

    public void setListeners() {
        fab.setOnClickListener(mOnClickListener);
        ll_about_us.setOnClickListener(mOnClickListener);
        ll_blog.setOnClickListener(mOnClickListener);
        ll_projects.setOnClickListener(mOnClickListener);
        ll_contact_us.setOnClickListener(mOnClickListener);
    }

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


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fab:
                    gotoNextActivity(AddLeadActivity.class,false);
                    break;
                case R.id.ll_about_us:
                    ((HostActivityInterface)DashboardActivity.this).addFragment(new AboutUsFragment(), true);
                    break;
                case R.id.ll_contact_us:
                    ((HostActivityInterface)DashboardActivity.this).addFragment(new ContactUsFragment(), true);
                    break;
                case R.id.ll_projects:
                    gotoNextActivity(ProjectsListActivity.class,false);
                    break;
                case R.id.ll_blog:
                    gotoNextActivity(BlogActivity.class,false);
                    break;
               }
        }
    };




    private void logoutAlert() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Exit");
        myAlertDialog.setMessage("Do you want to exit from app?");

        myAlertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        //TODO:
                    }
                });

        myAlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        myAlertDialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
   /*     handler.removeCallbacks(runnable);
        if(mDashboardRMCall != null)mDashboardRMCall.cancel();
        if(mLeadRMCall != null)mLeadRMCall.cancel();
        if(mActivityLeadRMCall != null)mActivityLeadRMCall.cancel();
   */ }

    protected void gotoNextActivity(Class<? extends Activity> nextUI, boolean isFinish){
        Intent i = new Intent(this, nextUI);
        startActivity(i);
        if(isFinish)finish();
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
