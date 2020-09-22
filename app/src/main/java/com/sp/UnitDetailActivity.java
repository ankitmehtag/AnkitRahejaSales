package com.sp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AppEnums.CommentType;
import com.AppEnums.UIEventType;
import com.VO.AmenitiesVO;
import com.VO.BaseVO;
import com.VO.CommentsVO;
import com.VO.FacitlitiesVO;
import com.VO.FlooringFittingWallsVO;
import com.VO.RecreationVO;
import com.VO.SafetyVO;
import com.VO.ServicesVO;
import com.VO.UnitDetailVO;
import com.adapters.GalleryFragmentAdapter;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.interfaces.GalleryCallback;
import com.model.NetworkErrorObject;
import com.model.PropertyModel;
import com.pwn.CommonLib;
import com.squareup.picasso.Picasso;
import com.utils.Connectivity;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnitDetailActivity extends BaseFragmentActivity implements GalleryCallback, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = UnitDetailActivity.class.getSimpleName();
    private LinearLayout llRating, llFav, ll_overview_container_1, ll_overview_container_2;
    private TextView tvAddress, tvRsPerSqFt, tvSize, tvPricePsf,
            tvPLCRate, tvPLCPTotal, tvUnitNumber, tvUnitNo_title, UBilderName, Ufloor, UtvRoomDetails, readdeveloper,
            tvBuilderDescription;
    private Activity ctx = this;
    private String id, title, total_types, flat_typology, unitType, priceSqft;
    private UnitDetailVO unitDetailVO;
    private TextView tvRating;
    private final int LOGIN_REQ_CODE = 451;
    // public HashMap<String, String> searchParams;
    TextView tvTimer;
    private Button btnBookNow, btn_Prevoius, btn_Next, btn_Prevoius1, btn_Next1, btn_Prevoius2, btn_Next2,
            btn_Prevoius3, btn_Next3, btn_Prevoius6, btn_Next6;

    private HorizontalScrollView hsv, hsv1, hsv2, hsv3, hsv4;
    private final int PAYMENT_REQ = 014;
    private TextView tvUnitPrice, tvCur, specification_view;
    private GoogleMap map;
    private HashMap<String, String> searchParams;
    private String propertyId;
    ImageView imageViewFav;
    boolean temp = false;
    boolean showingFirst = true;
    private Activity toolbar;
    private LinearLayout ll_unit_spec_root_view, llWalles, llFittings, llFlooring, ll_overview_root;
    private RelativeLayout rl_walls_root, rl_flooring_root, rl_fittings_root;
    private TextView btnShowAll;
    private NetworkErrorObject mNetworkErrorObject = null;
    private String uri;
    private int hasExtCallPermission;
    private List<String> permissions = new ArrayList<String>();
    ImageView ShareImgView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_detail);
        ll_unit_spec_root_view = (LinearLayout) findViewById(R.id.ll_unit_spec_root_view);
        ll_overview_root = (LinearLayout) findViewById(R.id.ll_overview_root);
        specification_view = (TextView) findViewById(R.id.specification_view);
        rl_walls_root = (RelativeLayout) findViewById(R.id.rl_walls_root);
        rl_flooring_root = (RelativeLayout) findViewById(R.id.rl_flooring_root);
        rl_fittings_root = (RelativeLayout) findViewById(R.id.rl_fittings_root);
        llWalles = (LinearLayout) findViewById(R.id.llWalles);
        llFittings = (LinearLayout) findViewById(R.id.llFittings);
        llFlooring = (LinearLayout) findViewById(R.id.llFlooring);
        btnShowAll = (TextView) findViewById(R.id.btnShowAll);
        id = getIntent().getStringExtra("unitId");
        title = getIntent().getStringExtra("unitTitle");
        total_types = getIntent().getStringExtra("bhkType");
        unitType = getIntent().getStringExtra("unitType");
        priceSqft = getIntent().getStringExtra("priceSqft");
        searchParams = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
        System.out.println("hh geting id = " + propertyId);
        ShareImgView = (ImageView)findViewById(R.id.shareImage);
        if (ConnectivityReceiver.isConnected()) {
            //TODO: network call
            getUnitDetail();
        } else {
            mNetworkErrorObject = Utils.showNetworkErrorDialog(UnitDetailActivity.this, UIEventType.RETRY_UNIT_DETAILS,
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ConnectivityReceiver.isConnected()) {
                                //TODO: network call
                                getUnitDetail();
                                mNetworkErrorObject.getAlertDialog().dismiss();
                                mNetworkErrorObject = null;
                            } else {
                                Utils.showToast(UnitDetailActivity.this, getString(R.string.check_your_internet_connection));
                            }
                        }
                    });
        }

        Toolbar toolbar = setToolBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnSubmit = (Button) findViewById(R.id.btn_submit_comment);
        final EditText edComment = (EditText) findViewById(R.id.ed_commnet_description);
        final RatingBar ratingBarUnit = (RatingBar) findViewById(R.id.ratingBarUnit);
        LayerDrawable stars = null;//(LayerDrawable) ratingBarUnit.getProgressDrawable();
        if (ratingBarUnit.getProgressDrawable() instanceof LayerDrawable) {
            stars = (LayerDrawable) ratingBarUnit.getProgressDrawable();
        } else if (ratingBarUnit.getProgressDrawable() instanceof DrawableWrapper) {
            DrawableWrapper wrapper = (DrawableWrapper) ratingBarUnit.getProgressDrawable();
            if (wrapper.getWrappedDrawable() instanceof LayerDrawable) {
                stars = (LayerDrawable) wrapper.getWrappedDrawable();
            }
        }

        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.starFullySelected), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.starPartiallySelected), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starNotSelected), PorterDuff.Mode.SRC_ATOP);

        //imageView

                ShareImgView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        share.putExtra(Intent.EXTRA_SUBJECT, "Unit Detail Link");
                        share.putExtra(Intent.EXTRA_TEXT, unitDetailVO.getShare_url());
                        startActivity(Intent.createChooser(share, "Share Unit Detail..."));

                    }
                });

                btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
                String comment = edComment.getText().toString().trim();
                float rat = ratingBarUnit.getRating();
                if (comment.isEmpty()) {
                    app.shakeEdittext(edComment);
                    app.showToastAtCenter(ctx, "Please Enter comment");
                }
                if (rat == 0f) {
                    app.showToast("Please select rating");
                } else {
                    if (userid != "") {
                        postComment(userid, comment, title, rat + "");
                        edComment.setText("");
                        ratingBarUnit.setRating(0F);
                    } else {
                        Intent i = new Intent(ctx, LoginActivity.class);
                        startActivityForResult(i, LOGIN_REQ_CODE);
                    }
                }
            }
        });

        ImageView back_img = (ImageView) findViewById(R.id.image_back);
        back_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageViewFav = (ImageView) findViewById(R.id.imageViewFav);
        imageViewFav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (Connectivity.isConnectedWithDoalog(ctx)) {
                    CommonLib commonLib = new CommonLib();
                    commonLib.makeFavorite(imageViewFav, ctx, app, unitDetailVO);
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }

    private void getUnitDetail() {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
            @Override
            public void OnBackgroundTaskCompleted() {
                if (unitDetailVO == null) {
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                        //Do nothing
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(UnitDetailActivity.this, UIEventType.RETRY_UNIT_DETAILS,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            getUnitDetail();
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(UnitDetailActivity.this, getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }

                } else {
                    if (unitDetailVO.isSuccess()) {
                        setViewsAndData();
                    } else {
                        showToast(unitDetailVO.getMessage());
                    }
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                PropertyModel model = new PropertyModel();
                try {
                    String uesrid = app.getFromPrefs(BMHConstants.USERID_KEY);
                    unitDetailVO = model.getUnitDetail(id, uesrid);
                } catch (BMHException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {
            }
        });
        loadingTask.execute("");
    }

    private boolean checkPermissions() {
        hasExtCallPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
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
        callIntent.setData(Uri.parse(uri));
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

    private void setViewsAndData() {
        if (unitDetailVO == null) return;
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        String[] arrLastDate = unitDetailVO.getAuction_date_end().split(" ");
        if (arrLastDate != null && arrLastDate.length > 0) {
            Date curDate = Calendar.getInstance().getTime();
            String[] arr2 = arrLastDate[0].split("-");
            if (arr2 != null && arr2.length > 1) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, Integer.parseInt(arr2[0]));
                c.set(Calendar.MONTH, Integer.parseInt(arr2[1]));
                c.set(Calendar.DATE, Integer.parseInt(arr2[2]));

                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);

                long difference = c.getTimeInMillis() - curDate.getTime();
                CounterClass timer = new CounterClass(difference, 1000);
                timer.start();
            }
        }
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        //tvDirection = (TextView) findViewById(R.id.tvDirection);
        //tvBalconies = (TextView) findViewById(R.id.tvBalconies);
        //tvParking = (TextView) findViewById(R.id.tvParking);
        //tvCoveredParking = (TextView) findViewById(R.id.tvCoveredParking);
        //UToilate = (TextView) findViewById(R.id.tvAreaRange);
        ll_overview_container_1 = (LinearLayout) findViewById(R.id.ll_overview_container_1);
        ll_overview_container_2 = (LinearLayout) findViewById(R.id.ll_overview_container_2);

        readdeveloper = (TextView) findViewById(R.id.read_developer);
        tvPricePsf = (TextView) findViewById(R.id.tvPricePsf);
        tvPLCRate = (TextView) findViewById(R.id.tvPlcPsf);
        tvPLCPTotal = (TextView) findViewById(R.id.tvPLCPRate);
        tvUnitNumber = (TextView) findViewById(R.id.tvUnitNo);
        tvUnitNo_title = (TextView) findViewById(R.id.tvUnitNo_title);
        UBilderName = (TextView) findViewById(R.id.tvBuilderName);
        Ufloor = (TextView) findViewById(R.id.tvFloorNo);
        UtvRoomDetails = (TextView) findViewById(R.id.tvRoomDetails);
        tvBuilderDescription = (TextView) findViewById(R.id.tv_builder_description);
        tvBuilderDescription.setMaxLines(3);
        hsv = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        hsv1 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview1);
        hsv2 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview2);
        hsv3 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview3);
        hsv4 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview6);
        TextView tvBspLabel = (TextView) findViewById(R.id.bspLabel);
        tvUnitPrice = (TextView) findViewById(R.id.tvUnitBspPrice);
        TextView tvBuilderContactNo = (TextView) findViewById(R.id.tvBuilderContactNo);
        tvBuilderContactNo.setText(unitDetailVO.getBuilder_contactno());
        LinearLayout llCall = (LinearLayout) findViewById(R.id.llcallBuilder);
        LinearLayout llTimerLabel = (LinearLayout) findViewById(R.id.llTimerLabel);

        llCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uri = "tel:" + unitDetailVO.getBuilder_contactno().trim();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermissions()) {
                            actionCall();
                        } else {
                            requestPermissions();
                        }
                    } else {
                        actionCall();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button buttonEnquire = (Button) findViewById(R.id.buttonEnquire);
        buttonEnquire.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnquryDialog();
            }
        });

        //String stPrice = "0";
		/*try {
			stPrice = (unitDetailVO.getBsp());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}*/
        specification_view.setOnClickListener(new OnClickListener() {
            boolean unitSpecificationOpen = false;

            public void onClick(View v) {
                ll_unit_spec_root_view.setVisibility((ll_unit_spec_root_view.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                int imgResource = -1;
                if (unitSpecificationOpen == true) {
                    imgResource = R.drawable.intrested;
                    unitSpecificationOpen = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    unitSpecificationOpen = true;
                }
                specification_view.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);
            }
        });

        // ===================== Overview View and Hide

        final LinearLayout overview = (LinearLayout) findViewById(R.id.Overview);
        overview.setVisibility(View.VISIBLE);

        final TextView tv_overview = (TextView) findViewById(R.id.overview_text);
        tv_overview.setOnClickListener(new OnClickListener() {

            boolean reviewView = false;

            public void onClick(View v) {
                overview.setVisibility((overview.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                int imgResource = -1;
                if (reviewView == true) {
                    imgResource = R.drawable.intrested;
                    reviewView = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    reviewView = true;
                }
                tv_overview.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);

            }
        });

        // =============== Facility Visible and hide
        final LinearLayout facility = (LinearLayout) findViewById(R.id.llrootFacility);
        facility.setVisibility(View.VISIBLE);

        final TextView tv_facility = (TextView) findViewById(R.id.facility);
        tv_facility.setOnClickListener(new OnClickListener() {

            boolean reviewView = false;

            public void onClick(View v) {
                facility.setVisibility((facility.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                int imgResource = -1;
                if (reviewView == true) {
                    imgResource = R.drawable.intrested;
                    reviewView = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    reviewView = true;
                }
                tv_facility.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);

            }
        });

        // ================== Specification visible and Hide

        final LinearLayout specification = (LinearLayout) findViewById(R.id.spec_view);
        specification.setVisibility(View.VISIBLE);
        final TextView tv_specification = (TextView) findViewById(R.id.specification);
        tv_specification.setOnClickListener(new OnClickListener() {

            boolean reviewView = false;

            public void onClick(View v) {
                specification.setVisibility((specification.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                int imgResource = -1;
                if (reviewView == true) {
                    imgResource = R.drawable.intrested;
                    reviewView = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    reviewView = true;
                }
                tv_specification.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);

            }
        });

        // ========================== Insights Visible and Hide

        final LinearLayout Insights = (LinearLayout) findViewById(R.id.insight_view);
        Insights.setVisibility(View.VISIBLE);

        final TextView tv_Insights = (TextView) findViewById(R.id.insight);
        tv_Insights.setOnClickListener(new OnClickListener() {

            boolean reviewView = false;

            public void onClick(View v) {
                Insights.setVisibility((Insights.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                int imgResource = -1;
                if (reviewView == true) {
                    imgResource = R.drawable.intrested;
                    reviewView = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    reviewView = true;
                }
                tv_Insights.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);

            }
        });

        /// =================================== price history visible and hide

        final LinearLayout history = (LinearLayout) findViewById(R.id.price_graph);
        history.setVisibility(View.VISIBLE);

        final TextView tv_history = (TextView) findViewById(R.id.price_history);
        tv_history.setOnClickListener(new OnClickListener() {

            boolean reviewView = false;

            public void onClick(View v) {
                history.setVisibility((history.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                int imgResource = -1;
                if (reviewView == true) {
                    imgResource = R.drawable.intrested;
                    reviewView = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    reviewView = true;
                }
                tv_history.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);

            }
        });

        // ============ About Builder visible and hide

        final LinearLayout builder = (LinearLayout) findViewById(R.id.tv_builder);
        builder.setVisibility(View.GONE);

        final TextView tv_builder = (TextView) findViewById(R.id.builder_about);
        tv_builder.setOnClickListener(new OnClickListener() {

            boolean reviewView = false;

            public void onClick(View v) {
                builder.setVisibility((builder.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                int imgResource = -1;
                if (reviewView == true) {
                    imgResource = R.drawable.intrested;
                    reviewView = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    reviewView = true;
                }
                tv_builder.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);

            }
        });

        // ============= Review visible and hide

        final LinearLayout review = (LinearLayout) findViewById(R.id.llcomments);
        review.setVisibility(View.VISIBLE);

        final TextView tv_review = (TextView) findViewById(R.id.review);
        tv_review.setOnClickListener(new OnClickListener() {

            boolean reviewView = false;

            public void onClick(View v) {
                review.setVisibility((review.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

                int imgResource = -1;
                if (reviewView == true) {
                    imgResource = R.drawable.intrested;
                    reviewView = false;
                } else {
                    imgResource = R.drawable.circle_plus;
                    reviewView = true;
                }
                tv_review.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);

            }
        });

        btn_Prevoius = (Button) findViewById(R.id.btnPrevoius);
        btn_Prevoius.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hsv.scrollTo((int) hsv.getScrollX() - 500, (int) hsv.getScrollY());

            }
        });

        btn_Next = (Button) findViewById(R.id.btnNext);
        btn_Next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hsv.scrollTo((int) hsv.getScrollX() + 500, (int) hsv.getScrollY());

            }
        });

        btn_Prevoius1 = (Button) findViewById(R.id.btnPrevoius1);
        btn_Prevoius1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hsv1.scrollTo((int) hsv1.getScrollX() - 500, (int) hsv1.getScrollY());

            }
        });

        btn_Next1 = (Button) findViewById(R.id.btnNext1);
        btn_Next1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hsv1.scrollTo((int) hsv1.getScrollX() + 500, (int) hsv1.getScrollY());

            }
        });

        btn_Prevoius2 = (Button) findViewById(R.id.btnPrevoius2);
        btn_Prevoius2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hsv2.scrollTo((int) hsv2.getScrollX() - 500, (int) hsv2.getScrollY());

            }
        });

        btn_Next2 = (Button) findViewById(R.id.btnNext2);
        btn_Next2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hsv2.scrollTo((int) hsv2.getScrollX() + 500, (int) hsv2.getScrollY());

            }
        });

        btn_Prevoius3 = (Button) findViewById(R.id.btnPrevoius3);
        btn_Prevoius3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hsv3.scrollTo((int) hsv3.getScrollX() - 500, (int) hsv3.getScrollY());

            }
        });

        btn_Next3 = (Button) findViewById(R.id.btnNext3);
        btn_Next3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hsv3.scrollTo((int) hsv3.getScrollX() + 500, (int) hsv3.getScrollY());

            }
        });

        btn_Prevoius6 = (Button) findViewById(R.id.btnPrevoius6);
        btn_Prevoius6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hsv4.scrollTo((int) hsv4.getScrollX() - 500, (int) hsv4.getScrollY());

            }
        });

        btn_Next6 = (Button) findViewById(R.id.btnNext6);
        btn_Next6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hsv4.scrollTo((int) hsv4.getScrollX() + 500, (int) hsv4.getScrollY());

            }
        });
        // ============================================================================================================

        // tvUnitdesc.setText(unitDetailVO.getDescription());

        // ImageView fav = (ImageView) llFav.findViewById(R.id.imageViewFav);
        // if (unitDetailVO.isUser_favourite()) {
        // fav.setImageResource(R.drawable.favorite_filled);
        // } else {
        // fav.setImageResource(R.drawable.favorite_outline);
        // }


        if (unitDetailVO.isUser_favourite()) {
            imageViewFav.setImageResource(R.drawable.favorite_filled);
        } else {
            imageViewFav.setImageResource(R.drawable.favorite_outline);
        }
        String rating = "0/5";
        if (unitDetailVO.getRatings_average() != null && !unitDetailVO.getRatings_average().isEmpty()) {
            rating = unitDetailVO.getRatings_average() + "/5";
        }
        //tvRating.setText(rating);
        tvAddress.setText(unitDetailVO.getAddress());
        tvPricePsf.setText(unitDetailVO.getSize() + " " + unitDetailVO.getSize_unit());
        String psfTxt = unitDetailVO.getPrice_SqFt() + " " + unitDetailVO.getPrice_SqFt_unit();
        float psfPrice = Utils.toFloat(unitDetailVO.getPrice_SqFt());
        if (psfPrice > 0)
            psfTxt = Utils.priceFormat(psfPrice) + " " + unitDetailVO.getPrice_SqFt_unit();
        tvPLCRate.setText(psfTxt);
        tvPLCPTotal.setText(unitDetailVO.getplc());
        if (unitDetailVO.getunitNo() != null && !unitDetailVO.getunitNo().isEmpty()) {
            tvUnitNo_title.setVisibility(View.VISIBLE);
            tvUnitNumber.setVisibility(View.VISIBLE);
            tvUnitNumber.setText(unitDetailVO.getunitNo());
        } else {
            tvUnitNo_title.setVisibility(View.GONE);
            tvUnitNumber.setVisibility(View.GONE);
        }
        UBilderName.setText(unitDetailVO.getBuilderName());
        Ufloor.setText(unitDetailVO.getUnitFloor());
        UtvRoomDetails.setText(unitDetailVO.getBedRoom());
        tvBuilderDescription.setText(unitDetailVO.getBuilder_description());
        readdeveloper.setOnClickListener(new OnClickListener() {
            // boolean temp = false;

            @Override
            public void onClick(View v) {

                if (temp) {
                    temp = !temp;
                    tvBuilderDescription.setMaxLines(3);
                    readdeveloper.setText("Read More");
                } else {
                    temp = !temp;
                    tvBuilderDescription.setMaxLines(100);
                    readdeveloper.setText("Read Less");
                }

            }
        });

        // =================================================================================

//		float unitprice = 0;
//		try {
//			unitprice = Float.parseFloat(unitDetailVO.getBsp());
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		}

        // tvRsPerSqFt.setText(unitDetailVO.getPrice_SqFt()+" Per Sq Ft");
        // tvSize.setText(unitDetailVO.getSize()+" Sq Ft");

        ArrayList<String> titleMap = new ArrayList<>(5);
        ArrayList<String> valueMap = new ArrayList<>(5);
        if (unitDetailVO.getDirection() != null && !unitDetailVO.getDirection().equalsIgnoreCase("N/A") && !unitDetailVO.getDirection().equalsIgnoreCase("NA")) {
            titleMap.add("Direction");
            valueMap.add(unitDetailVO.getDirection());
        }
        if (unitDetailVO.getBalconies() != null && Utils.toInt(unitDetailVO.getBalconies()) != 0) {
            titleMap.add("Balconies");
            valueMap.add(unitDetailVO.getBalconies());
        }
        if (unitDetailVO.getOpenparking() != null && Utils.toInt(unitDetailVO.getOpenparking()) != 0) {
            titleMap.add("Open Parking");
            valueMap.add(unitDetailVO.getOpenparking());
        }
        if (unitDetailVO.getCovered_parking() != null && Utils.toInt(unitDetailVO.getCovered_parking()) != 0) {
            titleMap.add("Covered Parking");
            valueMap.add(unitDetailVO.getCovered_parking());
        }
        if (unitDetailVO.getToilet() != null && Utils.toInt(unitDetailVO.getToilet()) != 0) {
            titleMap.add("Toilets");
            valueMap.add(unitDetailVO.getToilet());
        }
        ll_overview_container_1.removeAllViews();
        ll_overview_container_2.removeAllViews();
        if (titleMap.size() == 0) {
            ll_overview_root.setVisibility(View.GONE);
        } else {
            ll_overview_root.setVisibility(View.VISIBLE);
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            ArrayList<List<String>> overViewTitleList = Utils.chunks(titleMap, 3);
            ArrayList<List<String>> overViewValueList = Utils.chunks(valueMap, 3);
            LinearLayout.LayoutParams overViewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
            overViewParams.weight = 0.33f;
            for (int i = 0; i < overViewTitleList.size(); i++) {
                for (int j = 0; j < overViewTitleList.get(i).size(); j++) {
                    if (i == 0) {
                        LinearLayout mView = (LinearLayout) mLayoutInflater.inflate(R.layout.unit_detail_overview_item, null);
                        mView.setLayoutParams(overViewParams);
                        TextView tv_title = (TextView) mView.findViewById(R.id.tv_title);
                        TextView tv_value = (TextView) mView.findViewById(R.id.tv_value);
                        tv_title.setText((overViewTitleList.get(i)).get(j));
                        tv_value.setText((overViewValueList.get(i)).get(j));
                        ll_overview_container_1.addView(mView);
                    }
                    if (i == 1) {
                        LinearLayout mView = (LinearLayout) mLayoutInflater.inflate(R.layout.unit_detail_overview_item, null);
                        mView.setLayoutParams(overViewParams);
                        TextView tv_title = (TextView) mView.findViewById(R.id.tv_title);
                        TextView tv_value = (TextView) mView.findViewById(R.id.tv_value);
                        tv_title.setText((overViewTitleList.get(i)).get(j));
                        tv_value.setText((overViewValueList.get(i)).get(j));
                        ll_overview_container_2.addView(mView);
                    }
                }
            }
            if (overViewValueList.size() > 1) {
                LinearLayout mView = (LinearLayout) mLayoutInflater.inflate(R.layout.unit_detail_overview_item, null);
                mView.setLayoutParams(overViewParams);
                ll_overview_container_2.addView(mView);
            }
        }
        //Tem Jugaad


        // LinearLayout llWOW = (LinearLayout) findViewById(R.id.llWOW);
        // ArrayList<String> arrWOW = unitDetailVO.getWow();
        // for (int i = 0; i < arrWOW.size(); i++) {
        // View v = getLayoutInflater().inflate(R.layout.wow_item, null);
        // TextView tv = (TextView) v.findViewById(R.id.tvWOW);
        // tv.setText(arrWOW.get(i));
        // llWOW.addView(v);
        // }

        // setPieView();
        // setKitchenViews();
        // setAirConditionViews();

        setFacilities();
        setFlooringViews();
        setFittingViews();
        setWallsViews();
        setAmenitiesViews();
        setServicesViews();
        setSafetyViews();
        setRecreationViews();
        if (unitDetailVO != null) {
            if ((unitDetailVO.getFlooring() != null && unitDetailVO.getFlooring().size() > 0)
                    || (unitDetailVO.getFittings() != null && unitDetailVO.getFittings().size() > 0)
                    || (unitDetailVO.getWalls() != null && unitDetailVO.getWalls().size() > 0)
                    ) {
                specification_view.setVisibility(View.VISIBLE);
            } else {
                specification_view.setVisibility(View.GONE);
            }
        }

        // ================ webView Graph

        WebView web = (WebView) findViewById(R.id.webViewGraph);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        String priceTrends = String.format(unitDetailVO.getPrice_Trends());
        priceTrends = priceTrends.replace("price_trend_value", "price_trends");

        if (priceTrends.length() <= 2) {
            TextView ln = (TextView) findViewById(R.id.price_history);
            LinearLayout ln1 = (LinearLayout) findViewById(R.id.price_graph);
            ln.setVisibility(View.GONE);
            ln1.setVisibility(View.GONE);

        } else {

            System.out.println(priceTrends + " Price Trends");
            web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            web.loadUrl(UrlFactory.getPriceTrendsGraphUrl() + priceTrends);

            System.out.println(UrlFactory.getPriceTrendsGraphUrl() + priceTrends);

        }

        // =====================================

        ViewPager viewPager = (ViewPager) findViewById(R.id.pagerDeveloper);
        GalleryFragmentAdapter adapter = new GalleryFragmentAdapter(this, getSupportFragmentManager(), unitDetailVO.getMediaGellary());
        viewPager.setAdapter(adapter);

        btnBookNow = (Button) findViewById(R.id.buttonBookNow);
        if (unitDetailVO.getSold_status().equalsIgnoreCase("not sold")) {
            btnBookNow.setEnabled(true);
            if (unitDetailVO.getType().equalsIgnoreCase("E-Auction")) {
                btnBookNow.setText("Bid Now");
                tvBspLabel.setText("Starting Bid");
                float stratBid = 0;
                try {
                    stratBid = Float.parseFloat(unitDetailVO.getAuction_start_price());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                float currentBid = 0;
                try {
                    currentBid = Float.parseFloat(unitDetailVO.getAuction_current_price());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                tvUnitPrice.setText(app.getDecimalFormatedPrice(stratBid));
                LinearLayout llAuction = (LinearLayout) findViewById(R.id.llCurrentBid);
                llAuction.setVisibility(View.VISIBLE);
                llTimerLabel.setVisibility(View.VISIBLE);
                tvCur = (TextView) findViewById(R.id.tvUnitCurrentBid);
                tvCur.setText(app.getDecimalFormatedPrice(currentBid));
            } else if (unitDetailVO.getType().equalsIgnoreCase("Rent")) {
                btnBookNow.setText("Rent");
                tvBspLabel.setText("Rent");
                float rent = 0;
                try {
                    rent = Float.parseFloat(unitDetailVO.getTotal_rent());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                TextView tvPerMonth = (TextView) findViewById(R.id.tvLabelPerMonth);
                tvPerMonth.setVisibility(View.VISIBLE);
                tvUnitPrice.setText(getResources().getString(R.string.Rs) + " " + app.getDecimalFormatedPrice(rent));
            } else {
//				tvUnitPrice.setText(app.getDecimalFormatedPrice(stPrice));
                String priceTxt = unitDetailVO.getBsp();
                float intPrice = Utils.toFloat(unitDetailVO.getBsp());
                if (intPrice > 0) priceTxt = app.getDecimalFormatedPrice(intPrice) + "+";
                tvUnitPrice.setText(priceTxt + "+");
                llTimerLabel.setVisibility(View.GONE);
                tvTimer.setVisibility(View.GONE);
                btnBookNow.setText("Book Now");
            }

        } else {
            btnBookNow.setText("Reserved");
            btnBookNow.setEnabled(false);
            tvUnitPrice.setText(" - ");
        }

        btnBookNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnectedWithDoalog(ctx)) {
                    String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
                    if (userid != null && !userid.isEmpty()) {
                        if (btnBookNow.getText().toString().equalsIgnoreCase("bid now")) {
                            Log.i(TAG, "token: " + app.getBooleanFromPrefs(BMHConstants.TOKEN_MONEY_KEY));
                            if (app.getBooleanFromPrefs(BMHConstants.TOKEN_MONEY_KEY)) {
                                showPlaceBidDialog();
                            } else {
                                Intent i = new Intent(ctx, PayTokenActivity.class);
                                i.putExtra("unitvo", unitDetailVO);
                                startActivityForResult(i, PAYMENT_REQ);
                            }
                        } else if (btnBookNow.getText().toString().equalsIgnoreCase("Rent")) {
                            Intent i = new Intent(ctx, PaymentProccessActivity.class);
                            i.putExtra("type", "Rent");
                            i.putExtra("unitvo", unitDetailVO);
                            startActivity(i);

                        } else if (btnBookNow.getText().toString().equalsIgnoreCase("book now")) {
                            Intent i = new Intent(ctx, PaymentProccessActivity.class);
                            i.putExtra("type", "Buy");
                            i.putExtra("unitvo", unitDetailVO);
                            startActivity(i);
                        }

                    } else {
                        Intent i = new Intent(ctx, LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE);
                    }
                }
            }
        });

        ActionBar bar = getSupportActionBar();

        String displayname = unitDetailVO.getDisplay_name();
        title = getIntent().getStringExtra("unitTitle");
        if (displayname != null && displayname.length() > 0)
            bar.setTitle(Html.fromHtml("<font color='#ffffff'>" + displayname + " (" + unitDetailVO.getSize() + " " + unitDetailVO.getSize_unit() + ")" + "</font>"));
        else
            bar.setTitle(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));

        if (unitDetailVO.is_comment_list_needed()) {
            btnShowAll.setVisibility(View.VISIBLE);
        } else {
            btnShowAll.setVisibility(View.GONE);
        }
        LinearLayout llComments = (LinearLayout) findViewById(R.id.llcomments);
        llComments.removeAllViews();
        ArrayList<CommentsVO> comments = unitDetailVO.getComments_detail();
        if (comments != null && !comments.isEmpty()) {
            if (comments.size() == 0) {
                LinearLayout see = (LinearLayout) findViewById(R.id.see);
                see.setVisibility(View.GONE);

            }
            for (int j = 0; j < comments.size(); j++) {
                CommentsVO vo = comments.get(j);
                View v = getLayoutInflater().inflate(R.layout.comment_row, null);
                TextView tvname = (TextView) v.findViewById(R.id.tvcomment_username);
                TextView tvDate = (TextView) v.findViewById(R.id.tvcommentDate);
                TextView tvTitle = (TextView) v.findViewById(R.id.tv_comment_title);
                TextView tvDescription = (TextView) v.findViewById(R.id.tv_comment_description);
                RatingBar ratingbar = (RatingBar) v.findViewById(R.id.ratingBarComment);

                if (j != 0) {
                    LinearLayout devider = new LinearLayout(ctx);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
                    devider.setLayoutParams(params);
                    devider.setBackgroundColor(getResources().getColor(R.color.pie_strock));
                    llComments.addView(devider);
                }

                tvname.setText(vo.getComment_user_name());
                tvDate.setText(vo.getComment_date());
                tvTitle.setText(vo.getComment_title());
                tvDescription.setText(vo.getComment_description());
                float rat = 0;
                try {
                    rat = Float.parseFloat(vo.getRating());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                ratingbar.setRating(rat);

                llComments.addView(v);
            }
            btnShowAll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(ctx, CommentListActivity.class);
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    mIntentDataObject.putData(ParamsConstants.ID, unitDetailVO.getId());
                    mIntentDataObject.putData(ParamsConstants.COMMENT_TYPE, CommentType.UNIT_COMMENT.value);
                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    startActivity(mIntent);
                }
            });
        } else {
//			llComments.setVisibility(View.GONE);
            TextView view_com = (TextView) findViewById(R.id.review);
            view_com.setVisibility(View.GONE);
        }
        initMap();
    }

    private void setFacilities() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.llrootFacility);
        LinearLayout llSafety = (LinearLayout) findViewById(R.id.llFacilities);
        FacitlitiesVO facilities = unitDetailVO.getFacitlities();
        Boolean dataExist = false;
        if (facilities.isWardrobs()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.wardrobe);
            tv.setText("Wardrobes");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isServent_room()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.servent);
            tv.setText("Servent Room");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isPooja_room()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.pooja);
            tv.setText("Pooja Room");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isWashing_area()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.washing_area);
            tv.setText("Washing Area");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isPiped_gas()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.pipe_gas);
            tv.setText("Piped Gas");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isLifts()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.lift);
            tv.setText("Lifts");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isStores()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.store_room);
            tv.setText("Store Room");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isFurnished()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.furnished);
            tv.setText("Furnished");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isAutomation()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.home_automation);
            tv.setText("Home Automation");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isCentralwifi()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.central_wifi);
            tv.setText("Central Wifi");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (facilities.isHighspeedinternet()) {
            View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
            TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
            img.setImageResource(R.drawable.highspeed_internet);
            tv.setText("High Speed Internet");
            //v.setOnClickListener(clickListner);
            llSafety.addView(v);
            ll.setVisibility(View.VISIBLE);
            dataExist = true;
        }
        if (!dataExist) {
            ll.setVisibility(View.GONE);
            TextView tv_fac = (TextView) findViewById(R.id.facility);
            tv_fac.setVisibility(View.GONE);

        }
    }

    // ======================= New Add Project Specification section

    private void setAmenitiesViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llAmenitiesRoot);
        LinearLayout llAmenities = (LinearLayout) findViewById(R.id.llAmenities);
        llAmenities.removeAllViews();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        AmenitiesVO amenities = unitDetailVO.getAmenitiesVO();
        if (amenities != null && amenities.getAmenities() != null && amenities.getAmenities().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            for (AmenitiesVO.Amenity amenity : amenities.getAmenities()) {
                if (amenity != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(amenity.getTitle());
                    if (amenity.getImage() != null && !amenity.getImage().isEmpty()) {
                        String imgUrl = amenity.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + amenity.getImage() : UrlFactory.IMG_BASEURL + amenity.getImage();
                        Picasso.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);

                    }

                    llAmenities.addView(v);
                }
            }
        } else {
            llRoot.setVisibility(View.GONE);
        }
    }

    private void setRecreationViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llRecreationRoot);
        LinearLayout llRecreation = (LinearLayout) findViewById(R.id.llRecreation);
        RecreationVO recreationVO = unitDetailVO.getRecreationVO();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

        if (recreationVO != null && recreationVO.getRecreations() != null && recreationVO.getRecreations().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            for (RecreationVO.Recreation recreation : recreationVO.getRecreations()) {
                if (recreation != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(recreation.getTitle());
                    if (recreation.getImage() != null && !recreation.getImage().isEmpty()) {
                        String imgUrl = recreation.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + recreation.getImage() : UrlFactory.IMG_BASEURL + recreation.getImage();
                        Picasso.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);
                    }

                    llRecreation.addView(v);
                }
            }
        } else {
            llRoot.setVisibility(View.GONE);
        }

    }

    private void setSafetyViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llSafetyRoot);
        LinearLayout llSafety = (LinearLayout) findViewById(R.id.llSafety);
        SafetyVO safetyVo = unitDetailVO.getSafetyVO();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        if (safetyVo != null && safetyVo.getSefties() != null && safetyVo.getSefties().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            for (SafetyVO.Sefty sefty : safetyVo.getSefties()) {
                if (sefty != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(sefty.getTitle());
                    if (sefty.getImage() != null && !sefty.getImage().isEmpty()) {
                        String imgUrl = sefty.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + sefty.getImage() : UrlFactory.IMG_BASEURL + sefty.getImage();
                        Picasso.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);
                    }

                    llSafety.addView(v);
                }
            }
        } else {
            llRoot.setVisibility(View.GONE);
        }
    }

    private void setServicesViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llServicesRoot);
        LinearLayout llServices = (LinearLayout) findViewById(R.id.llServices);
        ServicesVO services = unitDetailVO.getServicesVO();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

        if (services != null && services.getServices() != null && services.getServices().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            for (ServicesVO.Service service : services.getServices()) {
                if (service != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(service.getTitle());
                    if (service.getImage() != null && !service.getImage().isEmpty()) {
                        String imgUrl = service.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + service.getImage() : UrlFactory.IMG_BASEURL + service.getImage();
                        Picasso.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);
                    }

                    llServices.addView(v);
                }
            }
        } else {
            llRoot.setVisibility(View.GONE);
        }
    }

    private void setFlooringViews() {
        ArrayList<FlooringFittingWallsVO> vo = unitDetailVO.getFlooring();
        if (vo != null && vo.size() > 0) {
            rl_flooring_root.setVisibility(View.VISIBLE);
            llFlooring.removeAllViews();
            for (int i = 0; i < vo.size(); i++) {
                FlooringFittingWallsVO item = vo.get(i);
                View v = getLayoutInflater().inflate(R.layout.flooring_item, null);
                TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                tv.setText(item.getName());
                TextView tvtype = (TextView) v.findViewById(R.id.tvType);
                tvtype.setText(item.getType());
                //v.setOnClickListener(clickListner);
                llFlooring.addView(v);
            }
        } else {
            rl_flooring_root.setVisibility(View.GONE);
        }
    }

    private void setFittingViews() {
        ArrayList<FlooringFittingWallsVO> vo = unitDetailVO.getFittings();
        if (vo != null && vo.size() > 0) {
            rl_fittings_root.setVisibility(View.VISIBLE);
            llFittings.removeAllViews();
            for (int i = 0; i < vo.size(); i++) {
                FlooringFittingWallsVO item = vo.get(i);
                View v = getLayoutInflater().inflate(R.layout.flooring_item, null);
                TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                tv.setText(item.getName());
                TextView tvtype = (TextView) v.findViewById(R.id.tvType);
                tvtype.setText(item.getType());
                //v.setOnClickListener(clickListner);
                llFittings.addView(v);
            }
        } else {
            rl_fittings_root.setVisibility(View.GONE);
        }
    }

    private void setWallsViews() {
        ArrayList<FlooringFittingWallsVO> vo = unitDetailVO.getWalls();
        if (vo != null && vo.size() > 0) {
            rl_walls_root.setVisibility(View.VISIBLE);
            llWalles.removeAllViews();
            for (int i = 0; i < vo.size(); i++) {
                FlooringFittingWallsVO item = vo.get(i);
                View v = getLayoutInflater().inflate(R.layout.flooring_item, null);
                TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                tv.setText(item.getName());
                TextView tvtype = (TextView) v.findViewById(R.id.tvType);
                tvtype.setText(item.getType());
                //v.setOnClickListener(clickListner);
                llWalles.addView(v);
            }
        } else {
            rl_walls_root.setVisibility(View.GONE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.unit_detail, menu);
        llFav = (LinearLayout) menu.findItem(R.id.actionFav).getActionView();
        menu.getItem(2).setVisible(false);
        llRating = (LinearLayout) menu.findItem(R.id.actionRating).getActionView();
        menu.getItem(1).setVisible(false);
        tvRating = (TextView) llRating.findViewById(R.id.tv_rating);
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_comment) {
            String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
            if (userid != null && !userid.isEmpty()) {
                showCommetDialog();
            } else {
                app.showToastAtCenter(ctx, "You need to login first to post a Comment.");
                Intent i = new Intent(ctx, LoginActivity.class);
                i.putExtra("classToStart", "noclass");
                startActivity(i);
            }
            return true;
        } else if (id == R.id.actionRating) {
            return true;
        } else if (id == R.id.home) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // ============================== Place Google Map

    private void initMap() {
        if (map == null) {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapId);
            if (fm != null) {
                fm.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        map.setTrafficEnabled(true);
                        map.setIndoorEnabled(true);
                        map.setBuildingsEnabled(true);
                        map.getUiSettings().setZoomControlsEnabled(true);
                        if (ActivityCompat.checkSelfPermission(UnitDetailActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(UnitDetailActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(UnitDetailActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BMHConstants.ACCESS_FINE_LOCATION);
                            return;
                        }
                        setMapAndZoom(map);
                    }
                });
            }
        } else {
            setMapAndZoom(map);
        }

        TextView explore_location = (TextView) findViewById(R.id.explore_location);
        TextView tv_landmarks = (TextView) findViewById(R.id.tv_landmarks);
        TextView tv_needs = (TextView) findViewById(R.id.tv_needs);
        TextView tv_transport = (TextView) findViewById(R.id.tv_transport);

        explore_location.setTag(BMHConstants.NEWALL);
        tv_landmarks.setTag(BMHConstants.LANDMARK);
        tv_needs.setTag(BMHConstants.NEEDS);
        tv_transport.setTag(BMHConstants.TRANSPORT);

        explore_location.setOnClickListener(placesOnclick);
        tv_landmarks.setOnClickListener(placesOnclick);
        tv_needs.setOnClickListener(placesOnclick);
        tv_transport.setOnClickListener(placesOnclick);

    }

    OnClickListener placesOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
			/*int a = (Integer) v.getTag();
			Intent i = new Intent(ctx, FullScreenUnitMapActivity.class);
			if (a == BMHConstants.LANDMARK) {
				if (unitDetailVO.getArrLandmarks() != null && !unitDetailVO.getArrLandmarks().isEmpty()) {
					i.putParcelableArrayListExtra("btn_landmarks", unitDetailVO.getArrLandmarks());
				} else {
					app.showSnackBar(ctx, "No landmark available.", SnackBar.LONG_SNACK);
					return;
				}
			}
			i.putExtra("lat_long", unitDetailVO.getLat_lng());
			i.putExtra("type", a);
			i.putExtra("name", unitDetailVO.getProj_name());
			i.putExtra("address", unitDetailVO.getAddress());
			startActivity(i);*/

            if (unitDetailVO != null && unitDetailVO.getLat_lng() != null) {
                Intent mIntent = new Intent(ctx, FullScreenMapActivity.class);
                IntentDataObject mIntentDataObject = new IntentDataObject();
                String lat_lng = unitDetailVO.getLat_lng();
                String[] latLngArray = lat_lng.split(",");
                if (latLngArray != null && latLngArray.length == 2) {
                    mIntentDataObject.putData(ParamsConstants.LAT, latLngArray[0]);
                    mIntentDataObject.putData(ParamsConstants.LNG, latLngArray[1]);
                }
                mIntentDataObject.putData(ParamsConstants.TITLE, unitDetailVO.getProject_name());
                mIntentDataObject.putData(ParamsConstants.LOCATION, unitDetailVO.getAddress());
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivity(mIntent);
            } else {
                showToast("Project location is unavailable at this time.");
            }
        }
    };

    private void showIndiaLocation(GoogleMap map) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BMHConstants.INDIA_LAT, BMHConstants.INDIA_LNG), 10f));
    }

    private void setMapAndZoom(GoogleMap map) {
        float ZOOM_LEVEL = 13.0f;
        if (map != null) {
            map.clear();
            showIndiaLocation(map);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setScrollGesturesEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);
            try {
                String[] arrLat = unitDetailVO.getLat_lng().split(",");
                if (arrLat.length >= 2) {
                    double lat = Double.parseDouble(arrLat[0]);
                    double lon = Double.parseDouble(arrLat[1]);
                    LatLng l = new LatLng(lat, lon);
                    map.clear();
                    MarkerOptions markerOption = new MarkerOptions().position(l)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    map.addMarker(markerOption);
                    markerOption.position(l);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(l, ZOOM_LEVEL));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected String setActionBarTitle() {
        return "";
    }

    private void addChild(int src, String name, LinearLayout llParent) {
        View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
        ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
        TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
        img.setImageResource(src);
        tv.setText(name);
        //v.setOnClickListener(clickListner);
        llParent.addView(v);
    }


    private void showCommetDialog() {
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View dialogView = factory.inflate(R.layout.comment_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBarUnit);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.starFullySelected),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.starPartiallySelected),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starNotSelected), PorterDuff.Mode.SRC_ATOP);

        Button btnSubmit = (Button) dialogView.findViewById(R.id.btn_submit_comment);
        final EditText edComment = (EditText) dialogView.findViewById(R.id.ed_commnet_description);
        final EditText edTitle = (EditText) dialogView.findViewById(R.id.ed_commnet_title);

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
                String comment = edComment.getText().toString().trim();
                String title = edTitle.getText().toString().trim();
                float rat = ratingBar.getRating();

                if (rat == 0f) {
                    app.showToast("Please select rating");
                } else if (comment.isEmpty()) {
                    app.shakeEdittext(edComment);
                    app.showToastAtCenter(ctx, "Please Enter comment");
                } else if (title.isEmpty()) {
                    app.shakeEdittext(edTitle);
                    app.showToastAtCenter(ctx, "Please Enter Title");
                } else {
                    dialog.dismiss();
                    postComment(userid, comment, title, rat + "");
                }
            }
        });

        dialog.setView(dialogView);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    private void postComment(final String userid, final String comment, final String title, final String rating) {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
            BaseVO vo;

            @Override
            public void OnBackgroundTaskCompleted() {
                if (app != null) {
                    if (vo == null) {
                        showToast(getString(R.string.something_went_wrong));
                    } else {
                        if (vo.isSuccess()) {
                            getUnitDetail();
                            showToast(vo.getMessage());
                        } else {
                            showToast(vo.getMessage());
                        }
                    }
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                PropertyModel model = new PropertyModel();
                try {
                    vo = model.postComment(unitDetailVO.getId(), userid, comment, title, rating, "unit");
                } catch (BMHException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {
            }
        });
        loadingTask.execute("");
    }

    private void MakeFavorite(final String userId, final ImageView fav) {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
            BaseVO vo;

            @Override
            public void OnBackgroundTaskCompleted() {
                if (vo == null) {
                    showToast(getString(R.string.unable_to_connect_server));
                } else {
                    if (vo.isSuccess()) {
                        // ImageView fav = (ImageView)
                        // llFav.findViewById(R.id.imageViewFav);
                        if (unitDetailVO.isUser_favourite()) {
                            unitDetailVO.setUser_favourite(false);
                            fav.setImageResource(R.drawable.favorite_outline);
                        } else {
                            unitDetailVO.setUser_favourite(true);
                            fav.setImageResource(R.drawable.favorite_filled);
                        }
                        // app.showAppMessage(ctx, mUnitDetails.getMessage());
                    } else {
                        showToast(vo.getMessage());
                    }
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                PropertyModel model = new PropertyModel();
                try {
                    vo = model.Favorite(userId, unitDetailVO.getId(), "unit");
                } catch (BMHException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {
            }
        });
        loadingTask.execute("");
    }

    @Override
    public void onPageClicked(int pos, int fileType) {
        switch (fileType) {
            case BMHConstants.TYPE_PDF:
                Intent service = new Intent(this, DownloadService.class);
                service.putExtra("pdfname", "broucher");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(service);
                } else {
                    startService(service);
                }
                break;
            default:
                Intent i = new Intent(ctx, ProjectGalleryActivity.class);
                i.putExtra("pos", pos);
                i.putExtra("arrImages", unitDetailVO.getMediaGellary());
                i.putExtra("pdfname", "broucher");
                startActivity(i);
                break;
        }
    }

    private void showEnquryDialog() {
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View dialogView = factory.inflate(R.layout.dialog_enquiry, null);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();

        TextView tvLabelName = (TextView) dialogView.findViewById(R.id.tvLabelName);
        tvLabelName.setText("Unit Name");
        final TextView edProjectName = (TextView) dialogView.findViewById(R.id.tv_project_name);
        final EditText edUserName = (EditText) dialogView.findViewById(R.id.et_user_name);
        final EditText edEnqury = (EditText) dialogView.findViewById(R.id.edwrite_enquiry);
        Button btnSend = (Button) dialogView.findViewById(R.id.btnSend);
        edProjectName.setText(Html.fromHtml(unitDetailVO.getDisplay_name() + " of " + unitDetailVO.getProject_name()));

        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName = edProjectName.getText().toString().trim();
                String uName = edUserName.getText().toString().trim();
                String query = edEnqury.getText().toString().trim();
                if (pName.isEmpty()) {
                    app.showToastAtCenter(ctx, "Enter the project name.");
                } else if (uName.isEmpty()) {
                    app.showToastAtCenter(ctx, "Enter your name.");
                } else if (query.isEmpty()) {
                    app.showToastAtCenter(ctx, "Enter your enquiry.");
                } else {
                    dialog.dismiss();
                    sendEmail("info@builder.com", "Enquiry for unit " + unitDetailVO.getDisplay_name() + " of " + pName, query);
                }

            }
        });
        dialog.setView(dialogView);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    private void showPlaceBidDialog() {
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View dialogView = factory.inflate(R.layout.dialog_place_bid, null);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();

        final EditText edBid = (EditText) dialogView.findViewById(R.id.edBid);
        TextView tvStartBid = (TextView) dialogView.findViewById(R.id.tvUnitStartBid);
        TextView tvCurrentBid = (TextView) dialogView.findViewById(R.id.tvUnitCurrentBid);
        final TextView tvUsrsBid = (TextView) dialogView.findViewById(R.id.tvUnitUsersBid);
        tvStartBid.setText(tvUnitPrice.getText().toString());
        tvCurrentBid.setText(tvCur.getText().toString());

        Button btnSend = (Button) dialogView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String bidAmt = edBid.getText().toString().trim();
                long amt = Long.parseLong(bidAmt);

                float currentBid = 0;
                try {
                    currentBid = Float.parseFloat(unitDetailVO.getAuction_current_price());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (bidAmt.isEmpty()) {
                    app.showToastAtCenter(ctx, "Please enter bid amount.");
                } else if (amt < currentBid) {
                    app.showToastAtCenter(ctx, "Bid amount should be higher than the current bid.");
                } else {
                    dialog.dismiss();
                    SubmitBid(bidAmt);
                }

            }
        });
        edBid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = s.toString().trim();
                if (ss.length() < 4) {
                    tvUsrsBid.setText(ss);
                } else {
                    float uBid = 0;
                    try {
                        uBid = Float.parseFloat(ss);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    tvUsrsBid.setText(app.getDecimalFormatedPrice(uBid));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dialog.setView(dialogView);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    private void sendEmail(String to, String sub, String msg) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        i.putExtra(Intent.EXTRA_SUBJECT, sub);
        i.putExtra(Intent.EXTRA_TEXT, msg);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            app.showToastAtCenter(ctx, "There are no email clients installed.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE && resultCode == RESULT_OK) {
            if (btnBookNow.getText().toString().equalsIgnoreCase("book now")) {
                // payment activity
                app.showToastAtCenter(ctx, "Reirecting to payment getaway.");
                Intent i = new Intent(ctx, PaymentProccessActivity.class);
                i.putExtra("unitvo", unitDetailVO);
                i.putExtra("type", "Buy");
                startActivity(i);
            } else if (btnBookNow.getText().toString().equalsIgnoreCase("bid now")) {
                app.showToastAtCenter(ctx, "Reirecting to payment getaway.");
            } else {

            }
        } else if (requestCode == PAYMENT_REQ && resultCode == RESULT_OK) {
            app.showToastAtCenter(ctx, "Token Money paid... Place your bid");
        }
    }

    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                    TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            // System.out.println(hms);
            tvTimer.setText(hms);
        }

        @Override
        public void onFinish() {
            // tvTimer.setText("Completed.");
        }
    }

    private void SubmitBid(final String bidAmount) {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
            BaseVO vo;

            @Override
            public void OnBackgroundTaskCompleted() {
                if (vo == null) {
                    showToast("Something went wrong, Try again.");
                } else {
                    if (vo.isSuccess()) {
                        app.showToastAtCenter(ctx, "Bid placed successfully.");
                        float currentBid = 0;
                        try {
                            currentBid = Float.parseFloat(bidAmount);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        tvCur.setText(app.getDecimalFormatedPrice(currentBid));
                    } else {
                        app.showToastAtCenter(ctx, vo.getMessage());
                    }
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                PropertyModel model = new PropertyModel();
                try {
                    vo = model.submitBid(unitDetailVO.getAuction_id(), bidAmount,
                            app.getFromPrefs(BMHConstants.USERNAME_KEY), app.getFromPrefs(BMHConstants.USER_EMAIL));
                } catch (BMHException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {
            }
        });
        loadingTask.execute("");
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_UNIT_DETAILS:
                    getUnitDetail();
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }
}
