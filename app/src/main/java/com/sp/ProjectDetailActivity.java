package com.sp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.AppEnums.CommentType;
import com.AppEnums.UIEventType;
import com.VO.AmenitiesVO;
import com.VO.BaseVO;
import com.VO.CommentsVO;
import com.VO.FlooringFittingWallsVO;
import com.VO.PageVO;
import com.VO.PropertyVO;
import com.VO.RecreationVO;
import com.VO.SafetyVO;
import com.VO.ServicesVO;
import com.VO.UnitDetailVO;
import com.VO.UnitGallaryVO;
import com.VO.UnitType;
import com.VO.UnitTypesVO;
import com.adapters.GalleryFragmentAdapter;
import com.adapters.UnitTypeListAdapter;
import com.adapters.UnitTypePagerAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.fragments.UnitTypeFragment;
import com.galleryview.GalleryDemoActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeIntents;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.interfaces.FragmentClickListener;
import com.interfaces.GalleryCallback;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.GalleryRespData;
import com.model.NetworkErrorObject;
import com.model.PropertyModel;
import com.pwn.CommonLib;
import com.squareup.picasso.Picasso;
import com.utils.StringUtil;
import com.utils.Utils;
import com.views.TouchImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectDetailActivity extends BaseFragmentActivity implements GalleryCallback, FragmentClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    protected static final TextView ReadMore = null;
    private static final String TAG = ProjectDetailActivity.class.getSimpleName();
    private static final float ZOOM_LEVEL = 15f;
    private LinearLayout llRating, llFav;
    private ImageView llFavs, imageViewFav;
    private TextView tvRating, readtext, readlocality, readdeveloper;
    private String propertyId;
    private String unitId;
    private GoogleMap map;
    private PropertyVO propertyDetails;
    private Activity ctx = ProjectDetailActivity.this;
    private TextView tvStartPrice, tvAddress, tvPossessionDate, tvSampleFlat, tvProjDesc, tvAreaRange, tvPriceRange,
            tvPossessionEnd, status, priceyear, project_name, priceSQFT, project_unit, proj_infra, proj_needs,
            proj_lifeStyle, taotal_Unit, No_block, No_Floors, rating_status, textCount, unitavilable, developer_name, unit_sattic;

    private Button buttonBookNow, btnPrevoius, btnNext, btnPrevoius1, btnNext1, btnPrevoius2, btnNext2, btnPrevoius3,
            btnNext3, btnPrevoius4, booknow2dview, sitevisit, btnNext4, booknowvia, booknowview, button_count;
    private HashMap<String, String> searchParams;
    private String id, title;
    private HorizontalScrollView horizontal_scrollview, hsv1, hsv2, hsv3, hsv4;
    int currentScrollX = 0;
    boolean temp = false;
    private ImageButton img_top = null;

    // =================== Add Unit type
    private ProjectDetailActivity ctx1 = this;
    private Activity ctx2 = this;
    private String projectId;
    private ListView listViewUnitTypes;
    private UnitTypesVO vo;
    public HashMap<String, String> map1;
    private View c;
    ImageView img;
    boolean isPressed = false;
    private ArrayList<UnitGallaryVO> UnitGallaryVO;
    private ViewPager vp_img_gallery, viewPager1;
    private ViewPager vp_unit_img, vp_UnitTypes;
    ArrayList<String> unitsArrayList = new ArrayList<String>();
    ;
    private ScrollView scrolloftop;
    //Utils utils = new Utils();

    boolean showingFirst = true;
    private final int LOGIN_REQ_CODE = 451;
    private final int LOGIN_REQ_CODE_FOR_PAYMENT = 451;
    private LinearLayout list_parent;
    private UnitTypeListAdapter adapters;
    private LayoutParams params;
    private LinearLayout bottomPanel;
    private int listSelected = -1, ClickedPosition = 0;
    private boolean fromLoginPage = false;
    PageVO basevo;
    int type;
    // private Text ReadMore;
    private IntentDataObject mIntentDataObject = null;
    private View mHeader;
    private AsyncThread mAsyncThread = null;
    private TextView tv_title;
    private Button btn_submit_comment, tvBuilderContactNo, buttonEnquire, gal_img, bookFooter;
    private EditText et_comment_description = null;
    private RatingBar ratingBarUnit;
    private TextView tvUnitsForSale, tvLabelUnitsForSale, tvBuilderNameTop, tvLocalityInsights, tvBuilderName,
            tvBuilderEstablished, tvBuilderAreaDelivered, tvBuilderDescription, rating_status_value;
    private TextView tv_over, tv_spec, tv_life, tv_ame, tv_se, tv_sa, tv_re, tv_es, tv_na, tv_area_delivered_title, tv_ra;
    private TextView specification_view, text_build, tv_review, btnShowAllComments, insight_map, price_history;
    private LinearLayout ll_unitsForSale, unit_view, build_linear, review_view, insight_view, histry_view;
    private ImageView imgBuilderLogo, image_back, imageshare, imgVideoWalk, imgVideoWalkPlay;
    private RelativeLayout rl_walls_root, rl_flooring_root, rl_fittings_root;
    private LinearLayout llWalles, llFittings, llFlooring;
    private UnitType currentUnitType = null;
    private Resources mResources;

    private String commentText = "";
    private float ratingValue = 0.0f;
    private NetworkErrorObject mNetworkErrorObject = null;
    private String getAlertParams;
    private int hasExtCallPermission;
    private List<String> permissions = new ArrayList<String>();
    private UnitDetailVO unitDetailVO;
    private int currentGalleryPos = 0;

    ArrayAdapter<String> dataAdapter;
    Spinner spinner;
    TextView textView;
    private ArrayList<UnitType> arrUnitType;
    List<String> categories;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        mResources = getResources();
        initViews();
        setListeners();
        setTypeface();
        app = (BMHApplication) getApplication();

        bookFooter.setText(getString(R.string.book_now));
        //bottomPanel.setWeightSum(4);
        bookFooter.setVisibility(View.VISIBLE);

        categories = new ArrayList<String>();

        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            if (mIntentDataObject != null && mIntentDataObject.getData() != null) {
                searchParams = mIntentDataObject.getData();
                propertyId = searchParams.get(ParamsConstants.ID);//getIntent().getStringExtra("propertyId");
                if (ConnectivityReceiver.isConnected()) {
                    //TODO: network call
                    getProjectDetail();
                } else {
                    mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_PROJECT_DETAILS,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        //TODO: network call
                                        getProjectDetail();
                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                        mNetworkErrorObject = null;
                                    } else {
                                        Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                    }
                                }
                             });
                }

            } else {
                finish();
            }
        } else {
            finish();
        }
    }// End of onCreate()

    private void setTypeface() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        booknow2dview.setTypeface(font);
        buttonBookNow.setTypeface(font);
        sitevisit.setTypeface(font);
        btn_submit_comment.setTypeface(font);

        developer_name.setTypeface(font);
        project_name.setTypeface(font);
        tvAddress.setTypeface(font);
        tvPossessionDate.setTypeface(font);
        project_unit.setTypeface(font);
        priceSQFT.setTypeface(font);
        priceyear.setTypeface(font);
        proj_infra.setTypeface(font);
        proj_needs.setTypeface(font);
        proj_lifeStyle.setTypeface(font);
        // rating_status.setTypeface(font);
        status.setTypeface(font);
        tv_over.setTypeface(font);
        // tv_project_text.setTypeface(font);
        tvAreaRange.setTypeface(font);
        No_block.setTypeface(font);
        No_Floors.setTypeface(font);
        tvUnitsForSale.setTypeface(font);
        tv_spec.setTypeface(font);
        unitavilable.setTypeface(font);
        tv_life.setTypeface(font);
        tv_ame.setTypeface(font);
        tv_se.setTypeface(font);
        tv_sa.setTypeface(font);
        tv_re.setTypeface(font);
        tv_na.setTypeface(font);
        tv_es.setTypeface(font);
        tv_area_delivered_title.setTypeface(font);
        tv_ra.setTypeface(font);
        tvBuilderContactNo.setTypeface(font);

        specification_view.setTypeface(font);
        buttonEnquire.setTypeface(font);

        tvProjDesc.setTypeface(font);
        tvStartPrice.setTypeface(font);
        readtext.setTypeface(font);
        tv_review.setTypeface(font);
        text_build.setTypeface(font);
        price_history.setTypeface(font);
        tvBuilderName.setTypeface(font);
        tvBuilderEstablished.setTypeface(font);
        tvBuilderAreaDelivered.setTypeface(font);
        tvBuilderDescription.setTypeface(font);
        btnShowAllComments.setTypeface(font);
        btnShowAllComments.setTypeface(font);
        insight_map.setTypeface(font);
    }

    private void initViews() {
        mHeader = findViewById(R.id.rl_header);
        tv_title = (TextView) findViewById(R.id.tv_title);
        vp_unit_img = (ViewPager) findViewById(R.id.vp_unit_img);
        vp_UnitTypes = (ViewPager) findViewById(R.id.vp_UnitTypes);
        scrolloftop = (ScrollView) findViewById(R.id.scrolloftop);
        scrolloftop.requestFocus(View.FOCUS_UP);
        scrolloftop.smoothScrollTo(0, 0);

        vp_img_gallery = (ViewPager) findViewById(R.id.pager);
        sitevisit = (Button) findViewById(R.id.site_visit);
        img_top = (ImageButton) findViewById(R.id.imt_up);

        arrUnitType = new ArrayList<UnitType>();;

        et_comment_description = (EditText) findViewById(R.id.ed_commnet_description);
        ratingBarUnit = (RatingBar) findViewById(R.id.ratingBarUnit);
        LayerDrawable stars = (LayerDrawable) ratingBarUnit.getProgressDrawable();
        if (mResources == null) mResources = getResources();
        if (mResources != null) {
            stars.getDrawable(2).setColorFilter(mResources.getColor(R.color.starFullySelected), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(mResources.getColor(R.color.starPartiallySelected), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(mResources.getColor(R.color.starNotSelected), PorterDuff.Mode.SRC_ATOP);
        }
        rating_status_value = (TextView) findViewById(R.id.tv_rating_value);

        listViewUnitTypes = (ListView) findViewById(R.id.listViewUnitTypes);
        list_parent = (LinearLayout) findViewById(R.id.list_parent);
        btn_submit_comment = (Button) findViewById(R.id.btn_submit_comment);
        imageViewFav = (ImageView) findViewById(R.id.imageViewFav);
        buttonBookNow = (Button) findViewById(R.id.buttonBookNow);
        booknow2dview = (Button) findViewById(R.id.BookNowView);

        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAreaRange = (TextView) findViewById(R.id.tvAreaRange);
        tvPriceRange = (TextView) findViewById(R.id.tvPriceRange);
        tvPossessionDate = (TextView) findViewById(R.id.tvPossessionDate);
        status = (TextView) findViewById(R.id.tvStatus);
        button_count = (Button) findViewById(R.id.TextGalleryNumber);
        project_unit = (TextView) findViewById(R.id.tvFlats);
        priceSQFT = (TextView) findViewById(R.id.tvStart_Sqft);
        developer_name = (TextView) findViewById(R.id.developer_name);
        project_name = (TextView) findViewById(R.id.projectName);
        proj_infra = (TextView) findViewById(R.id.textViewInfra);
        proj_needs = (TextView) findViewById(R.id.textViewNeeds);
        proj_lifeStyle = (TextView) findViewById(R.id.textViewReturns);
        taotal_Unit = (TextView) findViewById(R.id.tvSampleFlat);
        No_block = (TextView) findViewById(R.id.tvNoOfBlock);
        No_Floors = (TextView) findViewById(R.id.tvNoOfFloors);
        priceyear = (TextView) findViewById(R.id.tvPriceinyear);
        unitavilable = (TextView) findViewById(R.id.unit_avilable);
        unit_sattic = (TextView) findViewById(R.id.static_unit);

        tvSampleFlat = (TextView) findViewById(R.id.tvSampleFlat);
        ll_unitsForSale = (LinearLayout) findViewById(R.id.ll_unitsForSale);

        tvUnitsForSale = (TextView) findViewById(R.id.tvUnitsForSale);
        tvLabelUnitsForSale = (TextView) findViewById(R.id.tvLabelunitsForSale);
        tvBuilderNameTop = (TextView) findViewById(R.id.tvBuilderName);
        tvLocalityInsights = (TextView) findViewById(R.id.localityInsights);
        tvLocalityInsights.setMaxLines(3);
        tvBuilderName = (TextView) findViewById(R.id.tv_builder_name);
        tvBuilderEstablished = (TextView) findViewById(R.id.tv_builder_established);
        tvBuilderAreaDelivered = (TextView) findViewById(R.id.tv_builder_areadelivered);
        tvBuilderDescription = (TextView) findViewById(R.id.tv_builder_description);
        tvBuilderDescription.setMaxLines(3);

        tvBuilderContactNo = (Button) findViewById(R.id.tvBuilderContactNo);
        imgBuilderLogo = (ImageView) findViewById(R.id.img_builder_logo);

        rl_walls_root = (RelativeLayout) findViewById(R.id.rl_walls_root);
        rl_flooring_root = (RelativeLayout) findViewById(R.id.rl_flooring_root);
        rl_fittings_root = (RelativeLayout) findViewById(R.id.rl_fittings_root);

        llWalles = (LinearLayout) findViewById(R.id.llWalles);
        llFittings = (LinearLayout) findViewById(R.id.llFittings);
        llFlooring = (LinearLayout) findViewById(R.id.llFlooring);

        tv_over = (TextView) findViewById(R.id.over);
        tv_spec = (TextView) findViewById(R.id.tv_spec);
        tv_life = (TextView) findViewById(R.id.tv_life);
        tv_ame = (TextView) findViewById(R.id.tv_ame);
        tv_se = (TextView) findViewById(R.id.tv_ser);
        tv_sa = (TextView) findViewById(R.id.tv_saf);
        tv_re = (TextView) findViewById(R.id.tv_re);
        tv_na = (TextView) findViewById(R.id.ab_na);
        tv_es = (TextView) findViewById(R.id.Ab_es);
        tv_area_delivered_title = (TextView) findViewById(R.id.tv_area_delivered_title);
        tv_ra = (TextView) findViewById(R.id.rat);

        buttonEnquire = (Button) findViewById(R.id.buttonEnquire);
        image_back = (ImageView) findViewById(R.id.image_back);
        gal_img = (Button) findViewById(R.id.TextGalleryNumber);
        imageshare = (ImageView) findViewById(R.id.imageshare);
        unit_view = (LinearLayout) findViewById(R.id.ll_unit_spec_root_view);
        unit_view.setVisibility(View.VISIBLE);
        specification_view = (TextView) findViewById(R.id.specification_view);

        tvStartPrice = (TextView) findViewById(R.id.tvStartPrice);
        tvProjDesc = (TextView) findViewById(R.id.projdesc);
        tvProjDesc.setMaxLines(3);
        readtext = (TextView) findViewById(R.id.read_text);
        readlocality = (TextView) findViewById(R.id.read_locality);
        readdeveloper = (TextView) findViewById(R.id.read_developer);
        build_linear = (LinearLayout) findViewById(R.id.build_linear);
        build_linear.setVisibility(View.GONE);
        text_build = (TextView) findViewById(R.id.text_build);
        review_view = (LinearLayout) findViewById(R.id.llcomments);
        review_view.setVisibility(View.GONE);
        tv_review = (TextView) findViewById(R.id.review);
        btnShowAllComments = (TextView) findViewById(R.id.btnShowAll);
        insight_view = (LinearLayout) findViewById(R.id.insight_view);
        insight_view.setVisibility(View.VISIBLE);
        insight_map = (TextView) findViewById(R.id.insight_map);

        bottomPanel = (LinearLayout) findViewById(R.id.bottom_panel);
        bookFooter = (Button) findViewById(R.id.BookNowFooter);
        imgVideoWalk = (ImageView) findViewById(R.id.imgVideoWalk);
        imgVideoWalkPlay = (ImageView) findViewById(R.id.imgVideoWalkPlay);

        price_history = (TextView) findViewById(R.id.price_history);
        histry_view = (LinearLayout) findViewById(R.id.histry_view);

        textView = findViewById(R.id.flooriddd);
        //spinner by ankit
        spinner =(Spinner)findViewById(R.id.comrcialSpinr);

    }

    private void setListeners() {
        imageViewFav.setOnClickListener(mOnClickListener);
        booknow2dview.setOnClickListener(mOnClickListener);
        buttonBookNow.setOnClickListener(mOnClickListener);
        btn_submit_comment.setOnClickListener(mOnClickListener);
        listViewUnitTypes.setOnItemClickListener(mOnItemClickListener);
        sitevisit.setOnClickListener(mOnClickListener);

        tvBuilderContactNo.setOnClickListener(mOnClickListener);
        buttonEnquire.setOnClickListener(mOnClickListener);
        image_back.setOnClickListener(mOnClickListener);
        gal_img.setOnClickListener(mOnClickListener);
        imageshare.setOnClickListener(mOnClickListener);
        specification_view.setOnClickListener(mOnClickListener);
        specification_view.setTag(false);
        text_build.setOnClickListener(mOnClickListener);
        text_build.setTag(false);
        tv_review.setOnClickListener(mOnClickListener);
        tv_review.setTag(false);
        insight_map.setOnClickListener(mOnClickListener);
        insight_map.setTag(false);
        price_history.setOnClickListener(mOnClickListener);
        price_history.setTag(false);

        bookFooter.setOnClickListener(mOnClickListener);
        img_top.setOnClickListener(mOnClickListener);

        listViewUnitTypes.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }

        });
        et_comment_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                scrolloftop.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        scrolloftop.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Rect scrollBounds = new Rect();
                scrolloftop.getHitRect(scrollBounds);
                if (rating_status_value.getLocalVisibleRect(scrollBounds)) {
                    img_top.setVisibility(View.INVISIBLE);
                    tv_title.setVisibility(View.GONE);
                    mHeader.setBackgroundResource(R.drawable.transparent);
                } else {
                    mHeader.setBackgroundResource(R.drawable.background_golden);
                    if (propertyDetails != null) {
                        tv_title.setText(Html.fromHtml(propertyDetails.getProj_name()));
                        tv_title.setVisibility(View.VISIBLE);
                    }
                    img_top.setVisibility(View.VISIBLE);
                }
            }

        });

    }

    @Override
    public void myOnClickListener(Fragment fragment, Object obj) {
        if (fragment instanceof UnitTypeFragment) {
            UnitType unit = (UnitType) obj;
            //Toast.makeText(this, unit.getTotal_units(), Toast.LENGTH_SHORT).show();
        }
    }


    protected void SetClickedViewOnPager(int pos) {
        // ClickedPosition = pos;

        if (propertyDetails.getProjectype().contains("Residential")) {


            try {
                JSONArray jsonArray = propertyDetails.getflat_img().get(pos);
                unitsArrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        unitsArrayList.add(jsonArray.getJSONObject(i).getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            vp_unit_img.setAdapter(new CustomPagerAdapter(ProjectDetailActivity.this, unitsArrayList));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }

    private void setViewsAndData() {
        if (propertyDetails.getTotal_units() == 0) {
            ll_unitsForSale.setVisibility(View.GONE);
        } else {
            ll_unitsForSale.setVisibility(View.VISIBLE);
            tvUnitsForSale.setText(String.valueOf(propertyDetails.getTotal_units()).trim());
        }
        if (propertyDetails.is_comment_list_needed()) {
            btnShowAllComments.setVisibility(View.VISIBLE);
        } else {
            btnShowAllComments.setVisibility(View.GONE);
        }
        //scrolloftop.setEnabled(false);
        listViewUnitTypes.setFocusable(false);
        //scrolloftop.setEnabled(true);


        if (propertyDetails.getProjectype() != null) {

            if (propertyDetails.getProjectype().contains("Commercial"))
            {

                spinner.setVisibility(View.VISIBLE);


            }else if(propertyDetails.getProjectype().contains("Residential")){
                spinner.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                adapters = new UnitTypeListAdapter(ctx2, searchParams, propertyDetails.getArrUnitType());
                listViewUnitTypes.setAdapter(adapters);

                adapters = new UnitTypeListAdapter(ctx2, searchParams, propertyDetails.getArrUnitType());
                vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                 vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, propertyDetails.getArrUnitType()));
                vp_UnitTypes.setClipToPadding(false);
                vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));
                currentUnitType = propertyDetails.getArrUnitType().get(0);
                vp_UnitTypes.setCurrentItem(0);
                //  mOnPageChangeListener.onPageSelected(0);
            }
        }


/*
        adapters = new UnitTypeListAdapter(ctx2, searchParams, propertyDetails.getArrUnitType());
        listViewUnitTypes.setAdapter(adapters);
        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, propertyDetails.getArrUnitType()));
        vp_UnitTypes.setClipToPadding(false);
        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));
        currentUnitType = propertyDetails.getArrUnitType().get(0);
        //vp_UnitTypes.setCurrentItem(0);
        mOnPageChangeListener.onPageSelected(0);
*/


        if (listViewUnitTypes.getChildAt(0) != null) {
            listViewUnitTypes.getChildAt(0).setSelected(true);
            if (mResources == null) mResources = getResources();
            if (mResources != null)
                listViewUnitTypes.getChildAt(0).setBackgroundColor(mResources.getColor(R.color.pressed));
            JSONArray aaaaaa = propertyDetails.getflat_img().get(0);

            unitsArrayList.clear();
            for (int i = 0; i < aaaaaa.length(); i++) {
                try {
                    unitsArrayList.add(aaaaaa.getJSONObject(i).getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            vp_unit_img.setAdapter(new CustomPagerAdapter(ProjectDetailActivity.this, unitsArrayList));
        }

        if (propertyDetails.isUser_favourite()) {
            imageViewFav.setImageResource(R.drawable.favorite_filled);
        } else {
            imageViewFav.setImageResource(R.drawable.favorite_outline);
        }
        tvProjDesc.setText(Html.fromHtml((String) propertyDetails.getDescription()).toString());
       // tvProjDesc.setText(Html.fromHtml((String) propertyDetails.getDescription()).toString());


        readtext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewPager pppj = (ViewPager) findViewById(R.id.pager);
                if (temp) {
                    temp = false;
                    tvProjDesc.setMaxLines(3);
//					webText.setMinimumWidth(3);
                    readtext.setText("Read More");
//					webText.loadData(propertyDetails.getDescription(), "text/html", "UTF-8");
                    tvProjDesc.setEnabled(true);
//					webText.setEnabled(true);
                    scrolloftop.smoothScrollTo(0, pppj.getHeight());

                } else {
                    temp = true;
                    // webText.loadData(propertyDetails.getDescription(),
                    // "text/html", "UTF-8");
                    tvProjDesc.setMaxLines(1000);
//					webText.setMinimumHeight(100);
                    readtext.setText("Read Less");

                }

            }
        });

        tvAddress.setText(propertyDetails.getExactlocation().trim());
        String areaRange = "Total Area : " + propertyDetails.getMin_area_range() + " - " + propertyDetails.getMax_area_range() + " " + propertyDetails.getStd_unit_size();
        tvAreaRange.setText(areaRange);
        tvPossessionDate.setText("Possession : " + propertyDetails.getPossession_dt());

        status.setText(propertyDetails.getStatus());
        priceyear.setText(propertyDetails.getPrice_year());
        priceyear.setVisibility(View.INVISIBLE);
        project_name.setText(Html.fromHtml((String) propertyDetails.getProj_name()).toString());
        String projectPrice = propertyDetails.getProp_price_persq() + " " + propertyDetails.getStd_project_price();
        float price = Utils.toFloat(propertyDetails.getProp_price_persq());
        if (price > 0) {
            projectPrice = Utils.priceFormat(price) + " " + propertyDetails.getStd_project_price();
        }
        priceSQFT.setText(projectPrice);
        developer_name.setText(propertyDetails.getDeveloper_name());

        project_unit.setText(propertyDetails.getProject_unit_type());
        proj_infra.setText(propertyDetails.getInfra());
        proj_needs.setText(propertyDetails.getNeeds());
        proj_lifeStyle.setText(propertyDetails.getLife_style());
        if (propertyDetails.getNo_Block() != null && !propertyDetails.getNo_Block().isEmpty()) {
            No_block.setVisibility(View.VISIBLE);
            No_block.setText("No of Blocks : " + propertyDetails.getNo_Block());
        } else {
            No_block.setVisibility(View.GONE);
        }
        if (propertyDetails.getFloors() != null && !propertyDetails.getFloors().isEmpty()) {
            No_Floors.setVisibility(View.VISIBLE);
            No_Floors.setText("No of Floors : " + propertyDetails.getFloors());
        } else {
            No_Floors.setVisibility(View.GONE);
        }
        button_count.setText(propertyDetails.getMediaCount() + " " + "\n" + "Click for more....");
        if (propertyDetails.getIsLotteryProject() != null && propertyDetails.getIsLotteryProject().equalsIgnoreCase("1")) {
            //unitavilable.setVisibility(View.GONE);
            unitavilable.setText("Units for Lottery");
        } else {
            if (propertyDetails.getAvailable_Unit() != null && !propertyDetails.getAvailable_Unit().isEmpty()) {
                //unitavilable.setVisibility(View.VISIBLE);
                unitavilable.setText("Units " + propertyDetails.getAvailable_Unit());
            } else {
                //unitavilable.setVisibility(View.GONE);
                unitavilable.setText("Units");
            }
        }
        tvStartPrice.setText(propertyDetails.getPrice());
        rating_status_value.setVisibility(View.INVISIBLE);
        if (propertyDetails.getRatings_average() != null && !propertyDetails.getRatings_average().isEmpty()) {
            rating_status_value.setText(propertyDetails.getRatings_average() + "/5");
        } else {
            rating_status_value.setText("0/5");
        }
        String[] avilableT = propertyDetails.getAvailable_Unit().split(" ");

        try {
            avilableT[0] = avilableT[0].replace("(", "");
            int propertyCount = Integer.parseInt(avilableT[0]);
            if (propertyCount <= 0) {
                LinearLayout llUnit = (LinearLayout) findViewById(R.id.unit_map_test);
                LinearLayout UnitCheck = (LinearLayout) findViewById(R.id.unit_check);
                // hide box
                //Button btn = (Button) findViewById(R.id.BookNowFooter);
                // llUnit.setVisibility(1);
                bottomPanel.setWeightSum(3);
                llUnit.setVisibility(View.GONE);
                UnitCheck.setVisibility(View.GONE);
                // btn.setEnabled(false);
                bookFooter.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // hide box
            LinearLayout llUnit = (LinearLayout) findViewById(R.id.unit_map_test);
            LinearLayout UnitCheck = (LinearLayout) findViewById(R.id.unit_check);
            //	Button btn = (Button) findViewById(R.id.BookNowFooter);
            llUnit.setVisibility(View.GONE);
            UnitCheck.setVisibility(View.GONE);
            bottomPanel.setWeightSum(4);
            bookFooter.setVisibility(View.GONE);
            System.out.println(e.getMessage());
        }
        if (propertyDetails.isSample_flat()) {
            tvSampleFlat.setText("Ready");
        } else {
            tvSampleFlat.setText("Not Ready");
        }
        String type = "";
        for (int i = 0; i < propertyDetails.getArrType().size(); i++) {
            type = propertyDetails.getArrType().get(i) + ", " + type;
        }

        if (type.length() > 0)
            // tvType.setText(type.substring(0, type.length()-1));
            tvLocalityInsights.setText(propertyDetails.getLocality_insights());

        // ======================== Possession End
        readlocality.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (temp) {
                    temp = !temp;
                    tvLocalityInsights.setMaxLines(3);
                    readlocality.setText("Read More");
                } else {
                    temp = !temp;
                    tvLocalityInsights.setMaxLines(100);
                    readlocality.setText("Read Less");

                }

            }
        });

        tvBuilderNameTop.setText("By " + propertyDetails.getBuilder_name().trim());
        tvBuilderName.setText(propertyDetails.getBuilder_name());
        tvBuilderEstablished.setText(propertyDetails.getEstablish_year());
        if (propertyDetails.getArea_delevered() != null && !propertyDetails.getArea_delevered().isEmpty()) {
            tvBuilderAreaDelivered.setText(propertyDetails.getArea_delevered());
            tvBuilderAreaDelivered.setVisibility(View.VISIBLE);
            tv_area_delivered_title.setVisibility(View.VISIBLE);
        } else {
            tvBuilderAreaDelivered.setVisibility(View.GONE);
            tv_area_delivered_title.setVisibility(View.GONE);
        }

        tvBuilderDescription
                .setText(Html.fromHtml(Html.fromHtml((String) propertyDetails.getBuilder_description()).toString()));
        // tvBuilderDescription.setText(propertyDetails.getBuilder_description());


        readdeveloper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (temp) {
                    temp = !temp;
                    tvBuilderDescription.setMaxLines(3);
                    int scrollT = tvBuilderDescription.getScrollY();
                    //LinearLayout ly = (LinearLayout) findViewById(R.id.linear_star);
                    //ly.smoothScrollTo(0, 25);
                    readdeveloper.setText("Read More");
                } else {
                    temp = !temp;
                    tvBuilderDescription.setMaxLines(Integer.MAX_VALUE);
                    readdeveloper.setText("Read Less");
                    // readdeveloper.setVisibility(View.GONE);

                }

            }
        });

        horizontal_scrollview = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        hsv1 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview2);
        hsv2 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview3);
        hsv3 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview4);
        hsv4 = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview5);

        btnPrevoius1 = (Button) findViewById(R.id.btnPrevoius1);
        btnNext1 = (Button) findViewById(R.id.btnNext1);
        btnPrevoius2 = (Button) findViewById(R.id.btnPrevoius2);
        btnNext2 = (Button) findViewById(R.id.btnNext2);
        btnPrevoius3 = (Button) findViewById(R.id.btnPrevoius3);
        btnNext3 = (Button) findViewById(R.id.btnNext3);
        btnPrevoius4 = (Button) findViewById(R.id.btnPrevoius4);
        btnNext4 = (Button) findViewById(R.id.btnNext4);

        btnPrevoius4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hsv4.smoothScrollTo((int) hsv4.getScrollX() - 500, (int) hsv4.getScrollY());
            }
        });

        btnNext4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hsv4.smoothScrollTo((int) hsv4.getScrollX() + 500, (int) hsv4.getScrollY());
            }
        });

        btnPrevoius3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hsv3.smoothScrollTo((int) hsv3.getScrollX() - 500, (int) hsv3.getScrollY());
            }
        });

        btnNext3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hsv3.smoothScrollTo((int) hsv3.getScrollX() + 500, (int) hsv3.getScrollY());
            }
        });

        btnPrevoius2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hsv2.smoothScrollTo((int) hsv2.getScrollX() - 500, (int) hsv2.getScrollY());
            }
        });

        btnNext2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hsv2.smoothScrollTo((int) hsv2.getScrollX() + 500, (int) hsv2.getScrollY());
            }
        });

        btnPrevoius1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hsv1.smoothScrollTo((int) hsv1.getScrollX() - 500, (int) hsv1.getScrollY());
            }
        });

        btnNext1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hsv1.smoothScrollTo((int) hsv1.getScrollX() + 500, (int) hsv1.getScrollY());
            }
        });

        btnPrevoius = (Button) findViewById(R.id.btnPrevoius);
        btnPrevoius.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontal_scrollview.smoothScrollTo((int) horizontal_scrollview.getScrollX() - 500, (int) horizontal_scrollview.getScrollY());
            }
        });

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontal_scrollview.smoothScrollTo((int) horizontal_scrollview.getScrollX() + 500, (int) horizontal_scrollview.getScrollY());
            }
        });

        LinearLayout llWOW = (LinearLayout) findViewById(R.id.llWOW);

        //flore list add by ankit
        ArrayList<String> arrFloreList = propertyDetails.getFloreList();

        for(int j=0;j<arrFloreList.size();j++){
            categories.add(arrFloreList.get(j));
        }


        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, propertyDetails.getKeyList());

        // Drop down layout style - list view with radio button
      //  dataAdapter.setDropDownViewResource(R.layout.layoutspinner);
       // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                LayoutInflater mLayoutInflater       = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                ImageView imagebyCode = new ImageView(ProjectDetailActivity.this);
                ArrayList<String> arrayListttt = new ArrayList<>();
             /*   imagebyCode.setImageResource(R.mipmap.ic_launcher);
                LinearLayout.LayoutParams params =  new LinearLayout
                        .LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                imagebyCode.setLayoutParams(params);
                LinearLayout myLayout = (LinearLayout)findViewById(R.id.ll_root);
                myLayout.addView(imagebyCode);
*/


             //   Toast.makeText(ProjectDetailActivity.this,""+position+" Floor",Toast.LENGTH_SHORT).show();



                //  Object object_zero =   propertyDetails.getHashMapArrayList().get("0 Floor");
                //    propertyDetails.getHashMapArrayList().keySet().toString();
                Object object_zero =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(position));
                //  propertyDetails.getKeyList().get(0);


                if(object_zero!=null)
                {
                    object_zero.getClass();
                    arrUnitType.clear();


                    JSONObject jsonObj_zero;

                    for (int h = 0; h < ((JSONArray) object_zero).length(); h++) {
                        try {
                            jsonObj_zero = (JSONObject) ((JSONArray) object_zero).get(h);
                            jsonObj_zero.keys();

                            arrUnitType.add(new UnitType(jsonObj_zero));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapters = new UnitTypeListAdapter(ctx2, searchParams, arrUnitType);

                    listViewUnitTypes.setAdapter(adapters);
                    vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                    vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                    vp_UnitTypes.setClipToPadding(false);
                    vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                    vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));
                    //  vp_UnitTypes.setCurrentItem(0);

                    // currentUnitType = propertyDetails.getArrUnitType().get(0);

                    currentUnitType = arrUnitType.get(0);
                    //   vp_UnitTypes.setCurrentItem(0);
                    mOnPageChangeListener.onPageSelected(0);
                    //   Picasso.with(ProjectDetailActivity.this).load(propertyDetails.getFloor_img_plan().get(0)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imagebyCode);

                    arrayListttt.add(propertyDetails.getFloor_img_plan().get(position));

                    vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this, arrayListttt));

                }




           /*     switch (position){
                    case 0:

                      //  Object object_zero =   propertyDetails.getHashMapArrayList().get("0 Floor");
                 //    propertyDetails.getHashMapArrayList().keySet().toString();
                        Object object_zero =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(0));
                      //  propertyDetails.getKeyList().get(0);


                      if(object_zero!=null)
                      {
                          object_zero.getClass();
                          arrUnitType.clear();


                          JSONObject jsonObj_zero;

                          for (int h = 0; h < ((JSONArray) object_zero).length(); h++) {
                              try {
                                  jsonObj_zero = (JSONObject) ((JSONArray) object_zero).get(h);
                                  jsonObj_zero.keys();

                                  arrUnitType.add(new UnitType(jsonObj_zero));

                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                          adapters = new UnitTypeListAdapter(ctx2, searchParams, arrUnitType);

                          listViewUnitTypes.setAdapter(adapters);
                          vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                          vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                          vp_UnitTypes.setClipToPadding(false);
                          vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                          vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));
                          //  vp_UnitTypes.setCurrentItem(0);

                          // currentUnitType = propertyDetails.getArrUnitType().get(0);

                          currentUnitType = arrUnitType.get(0);
                          //   vp_UnitTypes.setCurrentItem(0);
                          mOnPageChangeListener.onPageSelected(0);
                          //   Picasso.with(ProjectDetailActivity.this).load(propertyDetails.getFloor_img_plan().get(0)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imagebyCode);

                          arrayListttt.add(propertyDetails.getFloor_img_plan().get(0));

                          vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this, arrayListttt));

                      }
                        break;

                    *//* Object object =   propertyDetails.getHashMapArrayList().get("0 Floor");
                     object.getClass();
                        arrUnitType.clear();
                   //  JSONObject jsonArray = new JSONObject(object);
                        ///JSONArray jsonArray = new JSONArray(object.getClass());
                        //for loop wit object.size {



                        JSONObject jsonArrayy;
                        try {
                        jsonArrayy = (JSONObject) ((JSONArray) object).get(0);
                       jsonArrayy.keys();
                        //    arrUnitType = (ArrayList<UnitType>) jsonArrayy.keys();
                            System.out.println("casTing result "+ jsonArrayy.keys());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                     for(int h=0;h<((JSONArray) object).length();h++) {
                         try {
                             jsonArrayy = (JSONObject) ((JSONArray) object).get(h);
                             jsonArrayy.keys();

                             arrUnitType.add(new UnitType(jsonArrayy));

                             Iterator<String> iter = jsonArrayy.keys();
                             while (iter.hasNext()) {

                                 String key = iter.next();
                                 try {
                                     jsonArrayy.keys();
                                     Object value = jsonArrayy.get(key);
                                     arrUnitType.add(new UnitType(jsonArrayy.getJSONObject(key)));
                                     // JSONArray jsonArray = new JSONArray(value);
                                     // unitTypesoooo.

                                     //hashMapArrayList.put(key,value);
                                     //UnitType launch = new UnitType(arrJson.optJSONObject(i));
                                     // UnitType launch = new UnitType(jsonObject.optJSONArray(key).getJSONObject(i));
                                     //arrVO.add(launch);


                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }

                        //adapters = new UnitTypeListAdapter(ctx2, searchParams,  propertyDetails.getArrMainUnitType().get(0));
                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));
                        break;
                    *//*

                    case 1:
                     //   Object object_one =   propertyDetails.getHashMapArrayList().get("1 Floor");

                        Object object_one =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(1));


                        if(object_one!=null) {

                            object_one.getClass();
                            arrUnitType.clear();

                            JSONObject jsonObj_one;

                            for (int h = 0; h < ((JSONArray) object_one).length(); h++) {
                                try {
                                    jsonObj_one = (JSONObject) ((JSONArray) object_one).get(h);
                                    jsonObj_one.keys();

                                    arrUnitType.add(new UnitType(jsonObj_one));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapters = new UnitTypeListAdapter(ctx2, searchParams, arrUnitType);
                            listViewUnitTypes.setAdapter(adapters);
                            vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                            vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                            vp_UnitTypes.setClipToPadding(false);
                            vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                            vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));
                            vp_UnitTypes.setCurrentItem(0);
                            currentUnitType = arrUnitType.get(0);
                            mOnPageChangeListener.onPageSelected(0);

                            //SetClickedViewOnPager(1);
                            arrayListttt.add(propertyDetails.getFloor_img_plan().get(1));

                            vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this, arrayListttt));


                            //  SetClickedViewOnPager(1);
                            // vp_unit_img.setAdapter(new CustomPagerAdapter(ProjectDetailActivity.this, unitsArrayList));
                            //   Picasso.with(ProjectDetailActivity.this).load(unitsArrayList.get(1)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imageView);
                            // vp_unit_img.setAdapter(new CustomPagerAdapter(ProjectDetailActivity.this, unitsArrayList));

*//*
                        ImageView imagebyCode = new ImageView(ProjectDetailActivity.this);
                        imagebyCode.setImageResource(R.mipmap.ic_launcher);
                        LinearLayout.LayoutParams params =  new LinearLayout
                                .LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        imagebyCode.setLayoutParams(params);
                        LinearLayout myLayout = (LinearLayout)findViewById(R.id.ll_root);
                        myLayout.addView(imagebyCode);*//*

             *//*
                        View itemView = mLayoutInflater.inflate(R.layout.item_pager_image, (ViewGroup) view, false);
                        LinearLayout ll_root = (LinearLayout) itemView.findViewById(R.id.ll_root);
                        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Utils.dp2px(350, ProjectDetailActivity.this));
                        ImageView imageView = new TouchImageView(ProjectDetailActivity.this);
                        imageView.setLayoutParams(imgParams);
                        ll_root.addView(imageView);*//*



             *//*  ImageView imagebyCode = new ImageView(ProjectDetailActivity.this);
                        imagebyCode.setImageResource(R.mipmap.ic_launcher);
                        LinearLayout.LayoutParams params =  new LinearLayout
                                .LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        imagebyCode.setLayoutParams(params);
                        LinearLayout myLayout = (LinearLayout)findViewById(R.id.ll_root);
                        myLayout.addView(imagebyCode);
*//*
                        }
                        // Picasso.with(ProjectDetailActivity.this).load(propertyDetails.getFloor_img_plan().get(1)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imagebyCode);
                        break;
                    case 2:

                        Object object_two =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(2));

                        if(object_two!=null) {


                            //   Object object_two =   propertyDetails.getHashMapArrayList().get("2 Floor");
                            object_two.getClass();
                            arrUnitType.clear();


                            JSONObject jsonObj_two;

                            for (int h = 0; h < ((JSONArray) object_two).length(); h++) {
                                try {
                                    jsonObj_two = (JSONObject) ((JSONArray) object_two).get(h);
                                    jsonObj_two.keys();

                                    arrUnitType.add(new UnitType(jsonObj_two));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapters = new UnitTypeListAdapter(ctx2, searchParams, arrUnitType);

                            listViewUnitTypes.setAdapter(adapters);
                            vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                            vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                            vp_UnitTypes.setClipToPadding(false);
                            vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                            vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));
                            vp_UnitTypes.setCurrentItem(0);
                            currentUnitType = arrUnitType.get(0);
                            mOnPageChangeListener.onPageSelected(0);

                            //  SetClickedViewOnPager(2);

                            arrayListttt.add(propertyDetails.getFloor_img_plan().get(2));

                            vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this, arrayListttt));

                            //  Picasso.with(ProjectDetailActivity.this).load(unitsArrayList.get(2)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imageView);
                            // vp_unit_img.setAdapter(new CustomPagerAdapter(ProjectDetailActivity.this, unitsArrayList));

                            //  vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,propertyDetails.getFloor_img_plan()));

                            //Picasso.with(ProjectDetailActivity.this).load(propertyDetails.getFloor_img_plan().get(2)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imagebyCode);

                        }

                        break;
                    case 3:

                        Object object_three =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(3));


                        //Object object_three =   propertyDetails.getHashMapArrayList().get("3 Floor");
                        object_three.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_three;

                        for(int h=0;h<((JSONArray) object_three).length();h++) {
                            try {
                                jsonObj_three = (JSONObject) ((JSONArray) object_three).get(h);
                                jsonObj_three.keys();

                                arrUnitType.add(new UnitType(jsonObj_three));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);

                        //  SetClickedViewOnPager(3);
                        //  Picasso.with(ProjectDetailActivity.this).load(unitsArrayList.get(3)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imageView);
//                        Picasso.with(ProjectDetailActivity.this).load("http://services.bookmyhouse.com/"+propertyDetails.getFloor_img_plan().get(3)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imagebyCode);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(3));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));


                        break;
                    case 4:
                        // adapters = new UnitTypeListAdapter(ctx2, searchParams,  propertyDetails.getArrMainUnitType().get(4));

                        Object object_four =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(4));

                    //    Object object_four =   propertyDetails.getHashMapArrayList().get("4 Floor");
                        object_four.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_four;

                        for(int h=0;h<((JSONArray) object_four).length();h++) {
                            try {
                                jsonObj_four = (JSONObject) ((JSONArray) object_four).get(h);
                                jsonObj_four.keys();

                                arrUnitType.add(new UnitType(jsonObj_four));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);

                        // SetClickedViewOnPager(4);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(4));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));

                        // Picasso.with(ProjectDetailActivity.this).load("http://services.bookmyhouse.com/"+propertyDetails.getFloor_img_plan().get(4)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imagebyCode);

                        //  Picasso.with(ProjectDetailActivity.this).load(unitsArrayList.get(4)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imageView);
                        break;
                    case 5:

                        Object object_five =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(5));

                       // Object object_five =   propertyDetails.getHashMapArrayList().get("5 Floor");
                        object_five.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_five;

                        for(int h=0;h<((JSONArray) object_five).length();h++) {
                            try {
                                jsonObj_five = (JSONObject) ((JSONArray) object_five).get(h);
                                jsonObj_five.keys();

                                arrUnitType.add(new UnitType(jsonObj_five));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);

                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(5));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));


                        // Picasso.with(ProjectDetailActivity.this).load(propertyDetails.getFloor_img_plan().get(5)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imagebyCode);


                        // SetClickedViewOnPager(5);
                        //    Picasso.with(ProjectDetailActivity.this).load(unitsArrayList.get(1)).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imageView);

                        break;
                    case 6:
                        Object object_six =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(6));


                     // Object object_six =   propertyDetails.getHashMapArrayList().get("6 Floor");
                        object_six.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_Six;

                        for(int h=0;h<((JSONArray) object_six).length();h++) {
                            try {
                                jsonObj_Six = (JSONObject) ((JSONArray) object_six).get(h);
                                jsonObj_Six.keys();

                                arrUnitType.add(new UnitType(jsonObj_Six));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);
                        //SetClickedViewOnPager(6);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(6));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));


                        break;
                    case 7:

                        Object object_seven =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(7));

                      //  Object object_seven =   propertyDetails.getHashMapArrayList().get("7 Floor");
                        object_seven.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_Seven;

                        for(int h=0;h<((JSONArray) object_seven).length();h++) {
                            try {
                                jsonObj_Seven = (JSONObject) ((JSONArray) object_seven).get(h);
                                jsonObj_Seven.keys();

                                arrUnitType.add(new UnitType(jsonObj_Seven));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);mOnPageChangeListener.onPageSelected(0);
                        //SetClickedViewOnPager(7);

                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(7));
                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));

                        break;
                    case 8:

                        Object object_eight =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(8));


                      //  Object object_eight =   propertyDetails.getHashMapArrayList().get("8 Floor");
                        object_eight.getClass();
                        arrUnitType.clear();

                        JSONObject jsonObj_Eight;

                        for(int h=0;h<((JSONArray) object_eight).length();h++) {
                            try {
                                jsonObj_Eight = (JSONObject) ((JSONArray) object_eight).get(h);
                                jsonObj_Eight.keys();

                                arrUnitType.add(new UnitType(jsonObj_Eight));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);
                        listViewUnitTypes.setAdapter(adapters);

                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);

                        //    SetClickedViewOnPager(8);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(8));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));


                        break;
                    case 9:

                        Object object_nine =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(9));


                       // Object object_nine =   propertyDetails.getHashMapArrayList().get("9 Floor");
                        object_nine.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_Nine;

                        for(int h=0;h<((JSONArray) object_nine).length();h++) {
                            try {
                                jsonObj_Nine = (JSONObject) ((JSONArray) object_nine).get(h);
                                jsonObj_Nine.keys();

                                arrUnitType.add(new UnitType(jsonObj_Nine));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);
                        //  SetClickedViewOnPager(9);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(9));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));


                        break;
                    case 10:


                        Object object_Ten =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(10));


                       // Object object_Ten =   propertyDetails.getHashMapArrayList().get("10 Floor");
                        object_Ten.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_Ten;

                        for(int h=0;h<((JSONArray) object_Ten).length();h++) {
                            try {
                                jsonObj_Ten = (JSONObject) ((JSONArray) object_Ten).get(h);
                                jsonObj_Ten.keys();

                                arrUnitType.add(new UnitType(jsonObj_Ten));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);


                        //SetClickedViewOnPager(10);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(10));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));

                        break;
                    case 11:

                        Object object_Eleven =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(11));


                      //  Object object_Eleven =   propertyDetails.getHashMapArrayList().get("11 Floor");
                        object_Eleven.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_Eleven;

                        for(int h=0;h<((JSONArray) object_Eleven).length();h++) {
                            try {
                                jsonObj_Eleven = (JSONObject) ((JSONArray) object_Eleven).get(h);
                                jsonObj_Eleven.keys();

                                arrUnitType.add(new UnitType(jsonObj_Eleven));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);

                        // SetClickedViewOnPager(11);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(11));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));


                        break;
                    case 12:

                        Object object_Twelve =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(12));


                        //Object object_Twelve =   propertyDetails.getHashMapArrayList().get("12 Floor");
                        object_Twelve.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_Twelve;

                        for(int h=0;h<((JSONArray) object_Twelve).length();h++) {
                            try {
                                jsonObj_Twelve = (JSONObject) ((JSONArray) object_Twelve).get(h);
                                jsonObj_Twelve.keys();

                                arrUnitType.add(new UnitType(jsonObj_Twelve));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);

                        // SetClickedViewOnPager(12);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(12));

                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));


                        break;
                    case 13:

                        Object object_Thrteen =   propertyDetails.getHashMapArrayList().get(propertyDetails.getKeyList().get(13));


                     //   Object object_Thrteen =   propertyDetails.getHashMapArrayList().get("13 Floor");
                        object_Thrteen.getClass();
                        arrUnitType.clear();


                        JSONObject jsonObj_Thrteen;

                        for(int h=0;h<((JSONArray) object_Thrteen).length();h++) {
                            try {
                                jsonObj_Thrteen = (JSONObject) ((JSONArray) object_Thrteen).get(h);
                                jsonObj_Thrteen.keys();

                                arrUnitType.add(new UnitType(jsonObj_Thrteen));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }




                        adapters = new UnitTypeListAdapter(ctx2, searchParams,  arrUnitType);

                        listViewUnitTypes.setAdapter(adapters);
                        vp_UnitTypes.addOnPageChangeListener(mOnPageChangeListener);
                        vp_UnitTypes.setAdapter(new UnitTypePagerAdapter(getSupportFragmentManager(), searchParams, arrUnitType));
                        vp_UnitTypes.setClipToPadding(false);
                        vp_UnitTypes.setPadding((int) Utils.dp2px(30, ProjectDetailActivity.this), 0, (int) Utils.dp2px(30, ProjectDetailActivity.this), 0);
                        vp_UnitTypes.setPageMargin((int) Utils.dp2px(10, ProjectDetailActivity.this));

                        vp_UnitTypes.setCurrentItem(0);
                        currentUnitType = arrUnitType.get(0);
                        mOnPageChangeListener.onPageSelected(0);

                        // SetClickedViewOnPager(13);
                        arrayListttt.add(propertyDetails.getFloor_img_plan().get(13));
                        vp_unit_img.setAdapter(new CustomPagerAdapterFloorWise(ProjectDetailActivity.this,arrayListttt));

                        break;
                    default:
                        break;


                }
*/

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        ArrayList<String> arrWOW = propertyDetails.getWow();
        for (int i = 0; i < arrWOW.size(); i++) {
            View v = getLayoutInflater().inflate(R.layout.wow_item, null);
            LinearLayout line = new LinearLayout(this);
            line.setBackgroundColor(Color.parseColor("#CCCCCC"));
            LayoutParams mLayoutParams = new LayoutParams((int) Utils.dp2px(1f, this), LayoutParams.MATCH_PARENT);
            mLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            int margin = (int) Utils.dp2px(15, this);
            mLayoutParams.setMargins(margin, 0, margin, 0);
            line.setLayoutParams(mLayoutParams);
            TextView tv = (TextView) v.findViewById(R.id.tvWOW);
            tv.setText(arrWOW.get(i));
            llWOW.addView(v);
            llWOW.addView(line);
        }
        setAmenitiesViews();
        setServicesViews();
        setSafetyViews();
        setRecreationViews();
        setFlooringViews();
        setFittingViews();
        setWallsViews();
        if (propertyDetails != null) {
            if ((propertyDetails.getFlooring() != null && propertyDetails.getFlooring().size() > 0)
                    || (propertyDetails.getFittings() != null && propertyDetails.getFittings().size() > 0)
                    || (propertyDetails.getWalls() != null && propertyDetails.getWalls().size() > 0)
                    ) {
                specification_view.setVisibility(View.VISIBLE);
            } else {
                specification_view.setVisibility(View.GONE);
            }
        }

        GalleryFragmentAdapter adapter = new GalleryFragmentAdapter(this, getSupportFragmentManager(), propertyDetails.getMediaGellary());
        vp_img_gallery.setAdapter(adapter);
        WebView web = (WebView) findViewById(R.id.webViewGraph);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        String priceTrends = String.format(propertyDetails.getPrice_Trends());
        priceTrends = priceTrends.replace("price_trend_value", "price_trends");
        if (priceTrends.length() <= 2) {
            price_history.setVisibility(View.GONE);
            histry_view.setVisibility(View.GONE);

        } else {
            Log.i(TAG, "Price Trends:" + priceTrends);
            web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            web.loadUrl(UrlFactory.getPriceTrendsGraphUrl() + priceTrends);
            System.out.println(UrlFactory.getPriceTrendsGraphUrl() + priceTrends);
        }

        imgVideoWalkPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(ctx, propertyDetails.getVideo_walk_through(), true, false);
                startActivity(intent);
            }
        });

        if (!propertyDetails.getBuilder_logo().isEmpty()) {
            Glide.with(ctx).load(UrlFactory.getShortImageByWidthUrl(BMHConstants.LIST_IMAGE_WIDTH, propertyDetails.getBuilder_logo())).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(imgBuilderLogo);
        }
        Glide.with(ctx).load(app.getThumbUrlViaId(propertyDetails.getVideo_walk_through())).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(imgVideoWalk);
        imgVideoWalkPlay.setTag(propertyDetails.getVideo_walk_through());
        LinearLayout llComments = (LinearLayout) findViewById(R.id.llcomments);
        llComments.removeAllViews();
        ArrayList<CommentsVO> comments = propertyDetails.getComments_detail();
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
                    if (mResources == null) mResources = getResources();
                    if (mResources != null)
                        devider.setBackgroundColor(mResources.getColor(R.color.pie_strock));
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


            btnShowAllComments.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(ctx, CommentListActivity.class);
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    mIntentDataObject.putData(ParamsConstants.ID, propertyDetails.getId());
                    mIntentDataObject.putData(ParamsConstants.COMMENT_TYPE, CommentType.PROJECT_COMMENT.value);
                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    startActivity(mIntent);
                }
            });
        } else {

            // llComments.setVisibility(View.GONE);

            TextView view_com = (TextView) findViewById(R.id.review);
            TextView see_all = (TextView) findViewById(R.id.btnShowAll);
            view_com.setVisibility(View.GONE);
            see_all.setVisibility(View.GONE);
        }

        // means if property has static units
        if (propertyDetails.getIs_Static_Unit()) {
            //booknow2dview.setText(getString(R.string.enquiry));
            //Button tempBookNow = (Button) findViewById(R.id.BookNowFooter);
            bottomPanel.setWeightSum(3);
            bookFooter.setVisibility(View.GONE);
            // tempBookNow.setEnabled(true);
            // tempBookNow.setTextColor(Color.parseColor("#F2C67B"));
        }
        initMap();
        if (fromLoginPage) {
            imageViewFav.performClick();
            fromLoginPage = false;
        }
    }

    private void setAmenitiesViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llAmenitiesRoot);
        View view_line_amenities = findViewById(R.id.view_line_amenities);
        LinearLayout llAmenities = (LinearLayout) findViewById(R.id.llAmenities);
        llAmenities.removeAllViews();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        AmenitiesVO amenities = propertyDetails.getAmenitiesVO();
        if (amenities != null && amenities.getAmenities() != null && amenities.getAmenities().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            view_line_amenities.setVisibility(View.VISIBLE);
            for (AmenitiesVO.Amenity amenity : amenities.getAmenities()) {
                if (amenity != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(amenity.getTitle());
                    if (amenity.getImage() != null && !amenity.getImage().isEmpty()) {
                        String imgUrl = amenity.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + amenity.getImage() : UrlFactory.IMG_BASEURL + amenity.getImage();
                        Glide.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);
                    }

                    llAmenities.addView(v);
                }
            }
        } else {
            view_line_amenities.setVisibility(View.GONE);
            llRoot.setVisibility(View.GONE);
        }
    }

    private void setRecreationViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llRecreationRoot);
        View view_line_recreation = findViewById(R.id.view_line_recreation);
        LinearLayout llRecreation = (LinearLayout) findViewById(R.id.llRecreation);
        RecreationVO recreationVO = propertyDetails.getRecreationVO();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

        if (recreationVO != null && recreationVO.getRecreations() != null && recreationVO.getRecreations().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            view_line_recreation.setVisibility(View.VISIBLE);
            for (RecreationVO.Recreation recreation : recreationVO.getRecreations()) {
                if (recreation != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(recreation.getTitle());
                    if (recreation.getImage() != null && !recreation.getImage().isEmpty()) {
                        String imgUrl = recreation.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + recreation.getImage() : UrlFactory.IMG_BASEURL + recreation.getImage();
                        Glide.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);
                    }

                    llRecreation.addView(v);
                }
            }
        } else {
            llRoot.setVisibility(View.GONE);
            view_line_recreation.setVisibility(View.GONE);
        }
    }

    private void setSafetyViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llSafetyRoot);
        View view_line_safety = findViewById(R.id.view_line_safety);
        LinearLayout llSafety = (LinearLayout) findViewById(R.id.llSafety);
        SafetyVO safetyVo = propertyDetails.getSafetyVO();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        if (safetyVo != null && safetyVo.getSefties() != null && safetyVo.getSefties().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            view_line_safety.setVisibility(View.VISIBLE);
            for (SafetyVO.Sefty sefty : safetyVo.getSefties()) {
                if (sefty != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(sefty.getTitle());
                    if (sefty.getImage() != null && !sefty.getImage().isEmpty()) {
                        String imgUrl = sefty.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + sefty.getImage() : UrlFactory.IMG_BASEURL + sefty.getImage();
                        Glide.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);
                    }

                    llSafety.addView(v);
                }
            }
        } else {
            llRoot.setVisibility(View.GONE);
            view_line_safety.setVisibility(View.GONE);
        }
    }

    private void setServicesViews() {
        LinearLayout llRoot = (LinearLayout) findViewById(R.id.llServicesRoot);
        View view_line_service = findViewById(R.id.view_line_service);
        LinearLayout llServices = (LinearLayout) findViewById(R.id.llServices);
        ServicesVO services = propertyDetails.getServicesVO();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

        if (services != null && services.getServices() != null && services.getServices().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            view_line_service.setVisibility(View.VISIBLE);
            for (ServicesVO.Service service : services.getServices()) {
                if (service != null) {
                    View v = getLayoutInflater().inflate(R.layout.amenities_item, null);
                    ImageView img = (ImageView) v.findViewById(R.id.imgAmenities);
                    TextView tv = (TextView) v.findViewById(R.id.tvAmenities);
                    tv.setTypeface(font);
                    tv.setText(service.getTitle());
                    if (service.getImage() != null && !service.getImage().isEmpty()) {
                        String imgUrl = service.getImage().startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + service.getImage() : UrlFactory.IMG_BASEURL + service.getImage();
                        Glide.with(this).load(imgUrl).placeholder(BMHConstants.PLACE_HOLDER).into(img);
                    }

                    llServices.addView(v);
                }
            }
        } else {
            llRoot.setVisibility(View.GONE);
            view_line_service.setVisibility(View.GONE);
        }
    }


    private void setFlooringViews() {
        ArrayList<FlooringFittingWallsVO> vo = PropertyVO.getFlooring();
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
        ArrayList<FlooringFittingWallsVO> vo = PropertyVO.getFittings();
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

    OnClickListener mFittingViewClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:
        }
    };

    private void setWallsViews() {
        llWalles = (LinearLayout) findViewById(R.id.llWalles);
        ArrayList<FlooringFittingWallsVO> vo = PropertyVO.getWalls();
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

    /*OnClickListener mWallViewClick  = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:
        }
    };
*/
    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
			/*if(position == 0)
				vp_UnitTypes.setPadding((int)Utils.dp2px(0,ProjectDetailActivity.this),0,(int)Utils.dp2px(30,ProjectDetailActivity.this),0);
			else
				vp_UnitTypes.setPadding((int)Utils.dp2px(30,ProjectDetailActivity.this),0,(int)Utils.dp2px(30,ProjectDetailActivity.this),0);
*/
            if (propertyDetails != null) {


                if(propertyDetails.getProjectype().contains("Commercial")){
                    currentUnitType = arrUnitType.get(position);

                }else {
                    currentUnitType = propertyDetails.getArrUnitType().get(position);

                }


              //  currentUnitType = propertyDetails.getArrUnitType().get(position);
                if (currentUnitType != null && currentUnitType.getStatic_unit().equalsIgnoreCase("YES")) {
                    booknow2dview.setText(getString(R.string.enquiry));
                    bookFooter.setText(getString(R.string.enquiry));
                    bottomPanel.setWeightSum(3);
                    bookFooter.setVisibility(View.GONE);
                } else {
                    if (currentUnitType != null && currentUnitType.getIsLotteryProject() != null && currentUnitType.getIsLotteryProject().equalsIgnoreCase("1")) {
                        booknow2dview.setText(getString(R.string.apply_now));
                        bookFooter.setText(getString(R.string.apply_now));
                        bottomPanel.setWeightSum(4);
                        bookFooter.setVisibility(View.VISIBLE);
                    } else {
                        booknow2dview.setText(getString(R.string.book_now));
                        bookFooter.setText(getString(R.string.book_now));
                        bottomPanel.setWeightSum(4);
                        bookFooter.setVisibility(View.VISIBLE);
                    }
                }
            }
            ClickedPosition = position;
            SetClickedViewOnPager(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.listViewUnitTypes:
                    SetClickedViewOnPager(position);
                    break;
            }
        }
    };

    public void actionCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(BMHConstants.CUSTOME_CARE));
        if (ActivityCompat.checkSelfPermission(ProjectDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private boolean checkPermissions() {
        hasExtCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
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

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BookNowView:
                    if (propertyDetails != null) {
                        if (currentUnitType != null && currentUnitType.getStatic_unit().equalsIgnoreCase("YES")) {
                            if (ConnectivityReceiver.isConnected()) {
                                //TODO: network call
                                showEnquryDialog();
                            } else {
                                mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_ENQUIRY,
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (ConnectivityReceiver.isConnected()) {
                                                    //TODO: network call
                                                    showEnquryDialog();
                                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                                    mNetworkErrorObject = null;
                                                } else {
                                                    Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                                }
                                            }
                                        });
                            }
                            return;
                        } else {
                            if (currentUnitType.getIsLotteryProject() != null && currentUnitType.getIsLotteryProject().equalsIgnoreCase("1")) {


                                if (ConnectivityReceiver.isConnected()) {
                                    //TODO: network call
                                    getLotteryPriceBreakup();
                                } else {
                                    mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_LOTTERY_PROJECT,
                                            new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (ConnectivityReceiver.isConnected()) {
                                                        //TODO: network call
                                                        getLotteryPriceBreakup();
                                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                                        mNetworkErrorObject = null;
                                                    } else {
                                                        Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                                    }
                                                }
                                            });
                                }
                            } else {
                                if (ConnectivityReceiver.isConnected()) {
                                    //TODO: network call
                                    goToUnitsMap();
                                } else {
                                    mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_UNIT_MAP,
                                            new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (ConnectivityReceiver.isConnected()) {
                                                        //TODO: network call
                                                        goToUnitsMap();
                                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                                        mNetworkErrorObject = null;
                                                    } else {
                                                        Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                    break;
                case R.id.imageViewFav:
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                        CommonLib commonLib = new CommonLib();
                        commonLib.makeFavorite(imageViewFav, ctx, app, propertyDetails);
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_FEV,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            CommonLib commonLib = new CommonLib();
                                            commonLib.makeFavorite(imageViewFav, ctx, app, propertyDetails);
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }
                    break;
                case R.id.btn_submit_comment:
                    String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
                    commentText = et_comment_description.getText().toString().trim();
                    ratingValue = ratingBarUnit.getRating();
                    if (commentText.isEmpty()) {
                        app.shakeEdittext(et_comment_description);
                        app.showToastAtCenter(ctx, "Please Enter comment");
                        return;
                    } else if (ratingValue == 0f) {
                        app.showToast("Please select rating");
                        return;
                    } else {
                        if (userid != null && !userid.isEmpty()) {
                            if (ConnectivityReceiver.isConnected()) {
                                //TODO: network call
                                postComment(userid, commentText, title, ratingValue + "");
                            } else {
                                mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_POST_COMMENT,
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (ConnectivityReceiver.isConnected()) {
                                                    //TODO: network call
                                                    postComment(app.getFromPrefs(BMHConstants.USERID_KEY), commentText, title, ratingValue + "");
                                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                                    mNetworkErrorObject = null;
                                                } else {
                                                    Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                                }
                                            }
                                        });
                            }
                        } else {
                            Intent i = new Intent(ctx, LoginActivity.class);
                            i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                            startActivityForResult(i, LOGIN_REQ_CODE);
                        }
                    }
                    break;
                case R.id.buttonBookNow:
					/*Intent i = new Intent(ProjectDetailActivity.this, UnitTypeListActivity.class);
					i.putExtra("searchParams", searchParams);
					i.putExtra("projectId", propertyDetails.getId());
					i.putExtra("pro_plan_img", propertyDetails.getProject_plan_img());
					i.putExtra("se", propertyDetails.getSe());
					i.putExtra("nw", propertyDetails.getNw());
					i.putStringArrayListExtra("unitIds", propertyDetails.getArrUnit_ids());
					startActivity(i);*/
                    break;
                case R.id.site_visit:
                    if (propertyDetails != null) {
                        Intent siteVisitIntent = new Intent(ProjectDetailActivity.this, SiteVisit.class);
                        siteVisitIntent.putExtra("projectId", propertyDetails.getId());
                        siteVisitIntent.putExtra("pro_name", propertyDetails.getProj_name());
                        startActivity(siteVisitIntent);
                    }
                    break;
                case R.id.buttonEnquire:
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                        showEnquryDialog();
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_ENQUIRY,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            showEnquryDialog();
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }
                    break;
                case R.id.TextGalleryNumber:
                    Log.v("ads", propertyId);
                    Intent galleryDemoIntent = new Intent(ProjectDetailActivity.this, GalleryDemoActivity.class);
                    galleryDemoIntent.putExtra("id", propertyId);
                    startActivity(galleryDemoIntent);
                    break;
                case R.id.imageshare:
                    String shareURl = String.format("http:/" + propertyDetails.getProject_Url());
                    Intent shareIntent = new Intent(Intent.ACTION_SEND, Uri.parse(shareURl));
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareURl);
                    startActivity(Intent.createChooser(shareIntent, "Share " + propertyDetails.getProj_name()));
                    break;
                case R.id.ll_unit_spec_root_view:

                    break;
                case R.id.tvBuilderContactNo:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermissions()) {
                            actionCall();
                        } else {
                            requestPermissions();
                        }
                    } else {
                        actionCall();
                    }
                    break;
                case R.id.specification_view:
                    unit_view.setVisibility((unit_view.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                    int imgResource = -1;
                    if ((boolean) v.getTag()) {
                        imgResource = R.drawable.intrested;
                        v.setTag(false);
                    } else {
                        imgResource = R.drawable.circle_plus;
                        v.setTag(true);
                    }
                    specification_view.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);
                    break;
                case R.id.text_build:
                    build_linear.setVisibility((build_linear.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                    int imgResource1 = -1;
                    if ((boolean) text_build.getTag()) {
                        imgResource1 = R.drawable.intrested;
                        v.setTag(false);
                    } else {
                        imgResource1 = R.drawable.circle_plus;
                        v.setTag(true);
                    }
                    text_build.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource1, 0);
                    break;
                case R.id.review:
                    review_view.setVisibility((review_view.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                    int imgResource2 = -1;
                    if ((boolean) v.getTag()) {
                        imgResource2 = R.drawable.intrested;
                        v.setTag(false);
                    } else {
                        imgResource2 = R.drawable.circle_plus;
                        v.setTag(true);
                    }
                    tv_review.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource2, 0);
                    break;
                case R.id.price_history:
                    histry_view.setVisibility((histry_view.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                    int imgResource3 = -1;
                    if ((boolean) v.getTag()) {
                        imgResource3 = R.drawable.intrested;
                        v.setTag(false);
                    } else {
                        imgResource3 = R.drawable.circle_plus;
                        v.setTag(true);
                    }
                    price_history.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource3, 0);
                    break;
                case R.id.insight_map:
                    insight_view.setVisibility((insight_view.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                    int imgResource4 = -1;
                    if ((boolean) v.getTag()) {
                        imgResource4 = R.drawable.intrested;
                        v.setTag(false);
                    } else {
                        imgResource4 = R.drawable.circle_plus;
                        v.setTag(true);
                    }
                    insight_map.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource4, 0);
                    break;
                case R.id.BookNowFooter:
                    booknow2dview.setFocusable(true);
                    booknow2dview.setFocusableInTouchMode(true);
                    booknow2dview.requestFocus();
                    unitavilable.requestFocus();

                    int scrollTo = findViewById(R.id.pager).getHeight() + findViewById(R.id.textStart_Price).getHeight()
                            + findViewById(R.id.overview_title).getHeight() + findViewById(R.id.details_over).getHeight()
                            + findViewById(R.id.spec).getHeight() + findViewById(R.id.featu).getHeight();

                    scrolloftop.smoothScrollTo(0, scrollTo);
                    break;
                case R.id.imt_up:
                    int scrollToUp = findViewById(R.id.imageViewFav).getHeight();
                    scrolloftop.smoothScrollTo(0, scrollToUp);
                    break;
                case R.id.image_back:
                    onBackPressed();
                    break;
            }

        }
    };

    // ========================== Unit End

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.unit_detail, menu);

        llFav = (LinearLayout) menu.findItem(R.id.actionFav).getActionView();
        // llFav.setOnClickListener(favIconClick);
		/*llRating = (LinearLayout) menu.findItem(R.id.actionRating).getActionView();
		tvRating = (TextView) llRating.findViewById(R.id.tv_rating);

		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

		tvRating.setTypeface(font);*/

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
        }
        return super.onOptionsItemSelected(item);
    }

    // ============== Explore Location Start ======================

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
                        if (ActivityCompat.checkSelfPermission(ProjectDetailActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(ProjectDetailActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProjectDetailActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BMHConstants.ACCESS_FINE_LOCATION);
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

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

        explore_location.setTypeface(font);

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
            if (propertyDetails != null && propertyDetails.getLat_lng() != null) {
                Intent mIntent = new Intent(ctx, FullScreenMapActivity.class);
                IntentDataObject mIntentDataObject = new IntentDataObject();
                String lat_lng = propertyDetails.getLat_lng();
                String[] latLngArray = lat_lng.split(",");
                if (latLngArray != null && latLngArray.length == 2) {
                    mIntentDataObject.putData(ParamsConstants.LAT, latLngArray[0]);
                    mIntentDataObject.putData(ParamsConstants.LNG, latLngArray[1]);
                }
                mIntentDataObject.putData(ParamsConstants.TITLE, propertyDetails.getProj_name());
                mIntentDataObject.putData(ParamsConstants.LOCATION, propertyDetails.getLocation());
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
        if (map != null) {
            map.clear();
            showIndiaLocation(map);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setScrollGesturesEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);
            try {
                String[] arrLat = propertyDetails.getLat_lng().split(",");
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

    // =================== Explore Location End Function

    @Override
    public void onPageClicked(final int pos, int fileType) {
        currentGalleryPos = pos;
        switch (fileType) {
            case BMHConstants.TYPE_PDF:
                if (ConnectivityReceiver.isConnected()) {
                    //TODO: network call
                    Intent service = new Intent(this, DownloadService.class);
                    service.putExtra("pdfurl", propertyDetails.getMediaGellary().get(currentGalleryPos).getUrl());
                    service.putExtra("pdfname", propertyDetails.getProj_name());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(service);
                    } else {
                        startService(service);
                    }
                } else {
                    mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_PDF_DOWNLOAD,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        //TODO: network call
                                        Intent service = new Intent(ProjectDetailActivity.this, DownloadService.class);
                                        service.putExtra("pdfurl", propertyDetails.getMediaGellary().get(currentGalleryPos).getUrl());
                                        service.putExtra("pdfname", propertyDetails.getProj_name());
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            startForegroundService(service);
                                        } else {
                                            startService(service);
                                        }
                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                        mNetworkErrorObject = null;
                                    } else {
                                        Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                }
                break;
            default:
				/*Intent i = new Intent(ctx, ProjectGalleryActivity.class);
				i.putExtra("pos", pos);
				i.putExtra("arrImages", propertyDetails.getMediaGellary());
				i.putExtra("pdfname", propertyDetails.getProj_name());
				startActivity(i);*/
                final String params = "project_id=" + propertyDetails.getId();
                if (ConnectivityReceiver.isConnected()) {
                    //TODO: network call
                    getGalleryData(params);
                } else {
                    mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_GALLERY,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        //TODO: network call
                                        getGalleryData(params);
                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                        mNetworkErrorObject = null;
                                    } else {
                                        Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                }
                break;
        }
    }


    private void getGalleryData(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_GALLERY);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_GALLERY));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ProjectDetailActivity.this, mOnCancelListener);
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
                //TODO: Error
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case GET_GALLERY:
                        if (mBean.getJson() != null) {
                            GalleryRespData mGalleryRespData = (GalleryRespData) JsonParser.convertJsonToBean(APIType.GET_GALLERY, mBean.getJson());
                            if (mGalleryRespData != null && mGalleryRespData.getData() != null) {
                                IntentDataObject mIntentDataObject = new IntentDataObject();
                                mIntentDataObject.setObj(mGalleryRespData.getData());
                                if (propertyDetails != null)
                                    mIntentDataObject.putData(ParamsConstants.TITLE, propertyDetails.getProj_name());
                                if (propertyDetails != null && propertyDetails.getExactlocation() != null)
                                    mIntentDataObject.putData(ParamsConstants.SUB_TITLE, propertyDetails.getExactlocation().trim());
                                Intent mIntent = new Intent(ProjectDetailActivity.this, GalleryView.class);
                                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                                startActivity(mIntent);
                            } else {
                                showToast(getString(R.string.unable_to_fetch_gallery_data));
                            }
                        } else {
                            showToast(getString(R.string.unable_to_fetch_gallery_data));
                        }
                        break;
                    case GET_ENQUERY:
                        BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.GET_ENQUERY, mBean.getJson());
                        if (baseRespModel != null) {
                            if (baseRespModel.isSuccess()) {
                                showToast(baseRespModel.getMessage());
                                //TODO:
                            } else {
                                showToast(baseRespModel.getMessage());
                            }
                        } else {
                            showToast(getString(R.string.unable_to_connect_server));
                        }
                        break;
                    case LOTTERY_PROJECT:
						/*LotteryDetails lottery = (LotteryDetails)JsonParser.convertJsonToBean(APIType.LOTTERY_PROJECT,mBean.getJson());
						if(lottery != null){
							if(lottery.isSuccess()){
								unitDetailVO = getUnitDetailVO(lottery);
								if(unitDetailVO != null) {
									if (Connectivity.isConnectedWithDoalog(ctx)) {
										String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
										if (userid != null && !userid.isEmpty()) {
											Intent i = new Intent(ctx, PaymentProccessActivity.class);
											i.putExtra("type", "Buy");
											i.putExtra("unitvo", unitDetailVO);
											startActivity(i);
										} else {
											Intent i = new Intent(ctx, LoginActivity.class);
											i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
											startActivityForResult(i, LOGIN_REQ_CODE_FOR_PAYMENT);
										}
									}
								}
							}else{
								showToast(lottery.getMessage());
							}
						}else{
							showToast(getString(R.string.unable_to_connect_server));
						}*/

                        try {
                            JSONObject jsonObj = new JSONObject(mBean.getJson());

                            if (jsonObj != null) {
                               // jsonObj.getString("sharing_url");
                                unitDetailVO = new UnitDetailVO(jsonObj);
                                if (unitDetailVO != null) {
                                    String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
                                    if (userid != null && !userid.isEmpty()) {
                                        Intent i = new Intent(ctx, PaymentProccessActivity.class);
                                        i.putExtra("type", "Buy");
                                        i.putExtra("unitvo", unitDetailVO);
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(ctx, LoginActivity.class);
                                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                                        startActivityForResult(i, LOGIN_REQ_CODE_FOR_PAYMENT);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });

    /*private UnitDetailVO getUnitDetailVO(LotteryDetails mLotteryDetails){
        UnitDetailVO mUnitDetailVO = null;
        if(mLotteryDetails == null)return null;
        mUnitDetailVO = new UnitDetailVO();
        mUnitDetailVO.setIsLotteryProject("1");
        mUnitDetailVO.setProperty_id(propertyDetails.getId());
        mUnitDetailVO.setProject_name(propertyDetails.getProj_name());
        mUnitDetailVO.setDisplay_name(currentUnitType.getFlat_typology());
        mUnitDetailVO.setPossession_dt(mLotteryDetails.getData().getPossession_dt());
        mUnitDetailVO.setOrginalbsp(mLotteryDetails.getData().getOriginal_bsp());
        mUnitDetailVO.setBsp(mLotteryDetails.getData().getBsp());
        mUnitDetailVO.setPrice_SqFt(mLotteryDetails.getData().getPrice_SqFt());
        mUnitDetailVO.setPrice_SqFt_unit(mLotteryDetails.getData().getPrice_SqFt_unit());
        mUnitDetailVO.setDisplay_name(mLotteryDetails.getData().getDisplay_name());
        mUnitDetailVO.setSize(mLotteryDetails.getData().getSize());
        mUnitDetailVO.setSize_unit(mLotteryDetails.getData().getSize_unit());
        mUnitDetailVO.setplc(mLotteryDetails.getData().getPlc());
        mUnitDetailVO.setTotal_plc(mLotteryDetails.getData().getTotal_plc());
        mUnitDetailVO.setDiscount(mLotteryDetails.getData().getDiscount());
        mUnitDetailVO.setTotal_discounted_bsp(mLotteryDetails.getData().getTotal_discounted_bsp());
        mUnitDetailVO.setTotal_edc_idc(mLotteryDetails.getData().getEdc_idc());
        mUnitDetailVO.setTotal_edc_idc(mLotteryDetails.getData().getTotal_edc_idc());
        mUnitDetailVO.setTerm_condition(mLotteryDetails.getData().getTerm_condition());
        mUnitDetailVO.setBuilder_term_condition(mLotteryDetails.getData().getBuilder_term_condition());
        mUnitDetailVO.setParking_interval(mLotteryDetails.getData().getParking_interval());
        mUnitDetailVO.setMin_parking(mLotteryDetails.getData().getMin_parking());
        mUnitDetailVO.setMax_parking(mLotteryDetails.getData().getMax_parking());
        mUnitDetailVO.setPayment_plans(mLotteryDetails.getData().getPayment_plans());
        mUnitDetailVO.setPossession_plan(mLotteryDetails.getData().getPossession_plan());
        mUnitDetailVO.setConstruction_plan(mLotteryDetails.getData().getConstruction_plan());
        mUnitDetailVO.setDown_payment_plan(mLotteryDetails.getData().getDown_payment_plan());
        mUnitDetailVO.setTotal_plc(mLotteryDetails.getData().getTotal_plc());
        mUnitDetailVO.setTotal_ibms(mLotteryDetails.getData().getTotal_ibms());
        mUnitDetailVO.setTotal_ifms(mLotteryDetails.getData().getTotal_ifms());
        mUnitDetailVO.setClub_charges(mLotteryDetails.getData().getClub_charges());
        mUnitDetailVO.setParking_charges(mLotteryDetails.getData().getParking_charges());
        mUnitDetailVO.setParking_service_tax(mLotteryDetails.getData().getParking_service_tax());
        mUnitDetailVO.setSer_tax_club_plc(mLotteryDetails.getData().getSer_tax_club_plc());
        mUnitDetailVO.setTotal_gst_vat_tax(mLotteryDetails.getData().getTotal_gst_vat_tax());
        mUnitDetailVO.setBooking_fees(mLotteryDetails.getData().getBooking_fees());
        mUnitDetailVO.setTotal_bsp_tax(mLotteryDetails.getData().getTotal_bsp_tax());
        mUnitDetailVO.setTotal_parking_tax(mLotteryDetails.getData().getTotal_parking_tax());
        mUnitDetailVO.setGrand_total(mLotteryDetails.getData().getGrand_total());
        mUnitDetailVO.setGrand_total_int(mLotteryDetails.getData().getGrand_total_int());
        mUnitDetailVO.setUnit_image(mLotteryDetails.getData().getUnit_image());
        mUnitDetailVO.setBuilder_contactno(mLotteryDetails.getData().getBuilder_contactno());
        //mUnitDetailVO.setId(mLotteryDetails.getData().getUnit_image());
        return mUnitDetailVO;
    }
*/
    @Override
    protected String setActionBarTitle() {
        return "";
    }

    private void getProjectDetail() {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {

            @Override
            public void OnBackgroundTaskCompleted() {
                if (propertyDetails == null) {
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_PROJECT_DETAILS,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            getProjectDetail();
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }
                } else {
                    if (propertyDetails.isSuccess()) {
                        setViewsAndData();

                        if( propertyDetails.getProjectype().contains("Commercial")){
                            spinner.setVisibility(View.VISIBLE);

                        }else {
                            spinner.setVisibility(View.GONE);
                        }
                    } else {
                        showToast(propertyDetails.getMessage());
                    }
                }

                // =======
                listViewUnitTypes.setItemChecked(0, true);
                SetClickedViewOnPager(0);
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                PropertyModel model = new PropertyModel();
                try {
                    String uesrid = app.getFromPrefs(BMHConstants.USERID_KEY);
                    if (searchParams != null && searchParams.containsKey("type")) {
                        propertyDetails = model.getPropertyDetail(propertyId + "", uesrid, searchParams.get("type"));
                    } else {
                        propertyDetails = model.getPropertyDetail(propertyId + "", uesrid, "");
                    }

                } catch (BMHException e) {
                    System.out.println("hh BMH error =" + e.getErrorMessage(e));
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {

            }
        });

        try {
            loadingTask.execute("");
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void showCommetDialog() {
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View dialogView = factory.inflate(R.layout.comment_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBarUnit);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        if (mResources == null) mResources = getResources();
        if (mResources != null) {
            stars.getDrawable(2).setColorFilter(mResources.getColor(R.color.starFullySelected),
                    PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(mResources.getColor(R.color.starPartiallySelected),
                    PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(mResources.getColor(R.color.starNotSelected), PorterDuff.Mode.SRC_ATOP);
        }
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
                            et_comment_description.setText("");
                            ratingBarUnit.setRating(0F);
                            showToast(vo.getMessage());
                            if (ConnectivityReceiver.isConnected()) {
                                //TODO: network call
                                getProjectDetail();
                            } else {
                                mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_PROJECT_DETAILS,
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (ConnectivityReceiver.isConnected()) {
                                                    //TODO: network call
                                                    getProjectDetail();
                                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                                    mNetworkErrorObject = null;
                                                } else {
                                                    Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                                }
                                            }
                                        });
                            }
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
                    vo = model.postComment(propertyDetails.getId(), userid, comment, title, rating, "project");
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


    private void showEnquryDialog() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View dialogView = factory.inflate(R.layout.dialog_enquiry, null);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        final TextView edProjectName = (TextView) dialogView.findViewById(R.id.tv_project_name);
        final EditText edUserName = (EditText) dialogView.findViewById(R.id.et_user_name);
        final EditText edUserEmail = (EditText) dialogView.findViewById(R.id.et_email);
        final EditText edUserPhone = (EditText) dialogView.findViewById(R.id.et_mobile);
        final EditText edEnqury = (EditText) dialogView.findViewById(R.id.edwrite_enquiry);
        final CheckBox cb_tnc = (CheckBox) dialogView.findViewById(R.id.cb_tnc);
        final TextView terms_conition = (TextView) dialogView.findViewById(R.id.terms_conition);
        final Button btnSend = (Button) dialogView.findViewById(R.id.btnSend);
        final ImageButton img_close = (ImageButton) dialogView.findViewById(R.id.img_close);
        if (propertyDetails != null) {
            edProjectName.setText(Html.fromHtml(propertyDetails.getProj_name()));
        }
        if (app.getFromPrefs(BMHConstants.USERNAME_KEY) != null)
            edUserName.setText(app.getFromPrefs(BMHConstants.USERNAME_KEY));
        if (app.getFromPrefs(BMHConstants.USER_EMAIL) != null)
            edUserEmail.setText(app.getFromPrefs(BMHConstants.USER_EMAIL));


        edProjectName.setTypeface(font);
        edUserName.setTypeface(font);
        edUserEmail.setTypeface(font);
        edUserPhone.setTypeface(font);
        edEnqury.setTypeface(font);
        btnSend.setTypeface(font);
        terms_conition.setTypeface(font);
        OnClickListener dialogViewsClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSend:
                        if (isValidAlertData(dialogView)) {
                            getAlertParams = getAlertParams(edProjectName.getText().toString().trim(), edUserName.getText().toString(), edUserPhone.getText().toString(), edUserEmail.getText().toString(), edEnqury.getText().toString());
                            if (getAlertParams == null || getAlertParams.isEmpty()) return;
                            if (ConnectivityReceiver.isConnected()) {
                                //TODO: network call
                                sendAlertRequest(getAlertParams);
                            } else {
                                mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectDetailActivity.this, UIEventType.RETRY_ALERT,
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (ConnectivityReceiver.isConnected()) {
                                                    //TODO: network call
                                                    sendAlertRequest(getAlertParams);
                                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                                    mNetworkErrorObject = null;
                                                } else {
                                                    Utils.showToast(ProjectDetailActivity.this, getString(R.string.check_your_internet_connection));
                                                }
                                            }
                                        });
                            }
                            dialog.dismiss();
                        }
                        break;
                    case R.id.img_close:
                        dialog.dismiss();
                        break;
                    case R.id.terms_conition:
                        Intent i = new Intent(ProjectDetailActivity.this, TermsWebActivity.class);
                        i.putExtra("pageType", 0);
                        startActivity(i);
                        break;
                }
            }
        };
        img_close.setOnClickListener(dialogViewsClick);
        terms_conition.setOnClickListener(dialogViewsClick);
        btnSend.setOnClickListener(dialogViewsClick);
        dialog.setView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    private boolean isValidAlertData(View dialogView) {
        if (dialogView == null) return false;
        final EditText edUserName = (EditText) dialogView.findViewById(R.id.et_user_name);
        final EditText edUserEmail = (EditText) dialogView.findViewById(R.id.et_email);
        final EditText edUserPhone = (EditText) dialogView.findViewById(R.id.et_mobile);
        final EditText edEnqury = (EditText) dialogView.findViewById(R.id.edwrite_enquiry);
        final CheckBox cb_tnc = (CheckBox) dialogView.findViewById(R.id.cb_tnc);
        if (edUserName.getText().toString().trim().isEmpty()) {
            showToast("Enter your name.");
            return false;
        }
        if (StringUtil.checkSpecialCharacter(edUserName.getText().toString().trim())) {
            showToast("Special character and digits are not allowed in Name.");
            app.shakeEdittext(edUserName);
            return false;
        } else if (edUserEmail.getText().toString().trim().isEmpty()) {
            showToast("Please enter Email.");
            app.shakeEdittext(edUserEmail);
            return false;
        } else if (edUserEmail.getText().toString().trim().isEmpty() || !Utils.isEmailValid(edUserEmail.getText().toString().trim())) {
            showToast("Please enter a valid Email.");
            app.shakeEdittext(edUserEmail);
            return false;
        } else if (edUserPhone.getText().toString().trim().isEmpty()) {
            showToast("Please enter Mobile Number.");
            app.shakeEdittext(edUserPhone);
        } else if (edUserPhone.getText().toString().trim().length() < 10) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(edUserPhone);
            return false;
        } else if (!checkMobileValidity(edUserPhone.getText().toString().trim().charAt(0))) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(edUserPhone);
            return false;
        } else if (edEnqury.getText().toString().trim().isEmpty()) {
            showToast("Enquiry field can not be blank");
            app.shakeEdittext(edEnqury);
            return false;
        } else if (!cb_tnc.isChecked()) {
            showToast("Agree to our terms & conditions");
            return false;
        }
        return true;
    }

    private String getAlertParams(String projectName, String name, String phone, String email, String query) {
        if (projectName == null || projectName.isEmpty() || name == null || name.isEmpty() || phone == null
                || phone.isEmpty() || email == null || email.isEmpty() || query == null || query.isEmpty())
            return "";
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("projectName=");
        mStringBuilder.append(projectName);
        mStringBuilder.append("&name=");
        mStringBuilder.append(name);
        mStringBuilder.append("&contactno=");
        mStringBuilder.append(phone);
        mStringBuilder.append("&email=");
        mStringBuilder.append(email);
        mStringBuilder.append("&enquiry=");
        mStringBuilder.append(query);
        String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
        mStringBuilder.append("&").append(ParamsConstants.USER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(userId);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.BUILDER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.CURRENT_BUILDER_ID);
		/*if(app.getFromPrefs(BMHConstants.CITYID) != null && !app.getFromPrefs(BMHConstants.CITYID).isEmpty() ){
			mStringBuilder.append("&city_id=");
			mStringBuilder.append(app.getFromPrefs(BMHConstants.CITYID));
		}
		if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && !app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty() ){
			mStringBuilder.append("&user_id=");
			mStringBuilder.append(app.getFromPrefs(BMHConstants.USERID_KEY));
		}*/
		/*if(mStringBuilder != null && mIntentDataObject.getData() != null) {
			HashMap<String,String> map = mIntentDataObject.getData();
			if (map.get(ParamsConstants.P_TYPE) != null) {
				mStringBuilder.append("&" + ParamsConstants.P_TYPE + "=");
				mStringBuilder.append(map.get(ParamsConstants.P_TYPE));
			}if (map.get(ParamsConstants.TYPE) != null) {
				mStringBuilder.append("&" + ParamsConstants.TYPE + "=");
				mStringBuilder.append(map.get(ParamsConstants.TYPE));
			}if (map.get(ParamsConstants.LOCATION_ID) != null) {
				mStringBuilder.append("&" + ParamsConstants.LOCATION_ID + "=");
				mStringBuilder.append(map.get(ParamsConstants.LOCATION_ID));
			}if (map.get(ParamsConstants.SUB_LOCATION_ID) != null) {
				mStringBuilder.append("&" + ParamsConstants.SUB_LOCATION_ID + "=");
				mStringBuilder.append(map.get(ParamsConstants.SUB_LOCATION_ID));
			}if (map.get(ParamsConstants.BUILDER_ID) != null) {
				mStringBuilder.append("&" + ParamsConstants.BUILDER_ID + "=");
				mStringBuilder.append(map.get(ParamsConstants.BUILDER_ID));
			}if (map.get(ParamsConstants.SPECIAL_CATEGORY) != null) {
				mStringBuilder.append("&category_type=");
				mStringBuilder.append(map.get(ParamsConstants.SPECIAL_CATEGORY));
			}
		}
*/
        return mStringBuilder.toString();
    }

    private void sendAlertRequest(String params) {
        if (params == null || params.isEmpty()) return;
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_ENQUERY);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_ENQUERY));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ProjectDetailActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void getLotteryPriceBreakup() {
        if (propertyDetails == null) return;
        StringBuilder mLotteryBreakupparams = new StringBuilder("");
        if (currentUnitType.getUnit_ids() != null) {
            String[] unitIds = currentUnitType.getUnit_ids().split(",");
            if (unitIds != null && unitIds.length > 0) {
                mLotteryBreakupparams.append(ParamsConstants.ID);
                mLotteryBreakupparams.append("=");
                mLotteryBreakupparams.append(unitIds[0]);
            }
            String uesrid = app.getFromPrefs(BMHConstants.USERID_KEY);
            if (uesrid != null && !uesrid.isEmpty()) {
                mLotteryBreakupparams.append("&");
                mLotteryBreakupparams.append(ParamsConstants.USER_ID);
                mLotteryBreakupparams.append("=");
                mLotteryBreakupparams.append(uesrid);
            }

		/*mLotteryBreakupparams.append(ParamsConstants.PROJECT_ID);
		mLotteryBreakupparams.append("=");
		mLotteryBreakupparams.append(propertyDetails.getId());
		mLotteryBreakupparams.append("&");
		mLotteryBreakupparams.append(ParamsConstants.DISPLAY_NAME);
		mLotteryBreakupparams.append("=");
		mLotteryBreakupparams.append(currentUnitType.getFlat_typology());
		mLotteryBreakupparams.append("&");
		mLotteryBreakupparams.append(ParamsConstants.SIZE);
		mLotteryBreakupparams.append("=");
		mLotteryBreakupparams.append(currentUnitType.getArea_range());*/
            ReqRespBean mBean = new ReqRespBean();
            mBean.setApiType(APIType.LOTTERY_PROJECT);
            mBean.setRequestmethod(WEBAPI.POST);
            mBean.setUrl(WEBAPI.getWEBAPI(APIType.LOTTERY_PROJECT));
            mBean.setMimeType(WEBAPI.contentTypeFormData);
            mBean.setJson(mLotteryBreakupparams.toString());
            mBean.setmHandler(mHandler);
            mBean.setCtx(this);
            mAsyncThread = new AsyncThread();
            mAsyncThread.initProgressDialog(ProjectDetailActivity.this, mOnCancelListener);
            mAsyncThread.execute(mBean);
            mAsyncThread = null;
        }
    }

    private void goToUnitsMap() {
        if (propertyDetails == null) return;
        Intent i = new Intent(ProjectDetailActivity.this, UnitMapActivity.class);
        i.putExtra("searchParams", searchParams);
        i.putExtra("projectId", propertyDetails.getId());
        i.putExtra("pro_plan_img", propertyDetails.getProject_plan_img());
        i.putExtra("se", propertyDetails.getSe());
        i.putExtra("nw", propertyDetails.getNw());


        if(propertyDetails.getProjectype().contains("Residential")){
            i.putExtra("unitIds", propertyDetails.getArrUnitType().get(ClickedPosition).getUnit_ids());
            i.putExtra("flat_typology", propertyDetails.getArrUnitType().get(ClickedPosition).getFlat_typology());
            i.putExtra("project_type",propertyDetails.getProjectype());
           // i.putExtra("Residentail_type",propertyDetails.getProjectype());
        }else  if (propertyDetails.getProjectype().contains("Commercial")){
            i.putExtra("unitIds", arrUnitType.get(ClickedPosition).getUnit_ids());
            i.putExtra("flat_typology", arrUnitType.get(ClickedPosition).getFlat_typology());
            i.putExtra("project_type",propertyDetails.getProjectype());
        }


        //i.putExtra("unitIds", propertyDetails.getArrUnitType().get(ClickedPosition).getUnit_ids());
       // i.putExtra("flat_typology", propertyDetails.getArrUnitType().get(ClickedPosition).getFlat_typology());
        startActivity(i);
    }

    protected boolean checkMobileValidity(char firstCharacter) {
        boolean validNo = false;
        switch (firstCharacter) {
            case '7':
                validNo = true;
                break;
            case '8':
                validNo = true;
                break;
            case '9':
                validNo = true;
                break;
        }
        return validNo;
    }

    class CustomPagerAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> unitsUrlList;

        public CustomPagerAdapter(Context context, ArrayList<String> arr) {
            mContext = context;
            unitsUrlList = arr;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return unitsUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_pager_image, container, false);
            LinearLayout ll_root = (LinearLayout) itemView.findViewById(R.id.ll_root);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            ImageView imageView = new TouchImageView(ProjectDetailActivity.this);
            imageView.setLayoutParams(imgParams);
            ll_root.addView(imageView);
            //ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
            //new DownloadImageTask(imageView).execute(unitsUrlList.get(position));
            String urldisplay = unitsUrlList.get(position);
            final String imgUrl = urldisplay.startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + urldisplay : UrlFactory.IMG_BASEURL + urldisplay;
            Glide.with(ProjectDetailActivity.this).load(imgUrl).centerCrop().placeholder(BMHConstants.PLACE_HOLDER).into(imageView);
            container.addView(itemView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUnitImageDialog("Unit Image", imgUrl);
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


    class CustomPagerAdapterFloorWise extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> unitsUrlList;

        public CustomPagerAdapterFloorWise(Context context, ArrayList<String> arr) {
            mContext = context;
            unitsUrlList = arr;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return unitsUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_pager_image, container, false);
            LinearLayout ll_root = (LinearLayout) itemView.findViewById(R.id.ll_root);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Utils.dp2px(350, ProjectDetailActivity.this));
            ImageView imageView = new TouchImageView(ProjectDetailActivity.this);
            imageView.setLayoutParams(imgParams);
            ll_root.addView(imageView);
            //ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
            //new DownloadImageTask(imageView).execute(unitsUrlList.get(position));
            String urldisplay = unitsUrlList.get(position);
            final String imgUrl = urldisplay.startsWith("/") ? Utils.removeLastSlashSign(UrlFactory.IMG_BASEURL) + urldisplay : UrlFactory.IMG_BASEURL + urldisplay;
            Picasso.with(ProjectDetailActivity.this).load(imgUrl).fit().placeholder(BMHConstants.PLACE_HOLDER).into(imageView);
            container.addView(itemView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUnitImageDialog("Unit Image", imgUrl);
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE && resultCode == RESULT_OK) {
            postComment(app.getFromPrefs(BMHConstants.USERID_KEY), commentText, title, ratingValue + "");
        }
        if (requestCode == LOGIN_REQ_CODE_FOR_PAYMENT && resultCode == RESULT_OK) {
            if (unitDetailVO != null) {
                app.showToastAtCenter(ctx, "Redirecting to payment getaway.");
                Intent i = new Intent(ctx, PaymentProccessActivity.class);
                i.putExtra("unitvo", unitDetailVO);
                i.putExtra("type", "Buy");
                startActivity(i);
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_PROJECT_DETAILS:
                    getProjectDetail();
                    break;
                case RETRY_FEV:
                    CommonLib commonLib = new CommonLib();
                    commonLib.makeFavorite(imageViewFav, ctx, app, propertyDetails);
                    break;
                case RETRY_ALERT:
                    sendAlertRequest(getAlertParams);
                    break;
                case RETRY_POST_COMMENT:
                    postComment(app.getFromPrefs(BMHConstants.USERID_KEY), commentText, title, ratingValue + "");
                    break;
                case RETRY_PDF_DOWNLOAD:
                    Intent service = new Intent(this, DownloadService.class);
                    service.putExtra("pdfurl", propertyDetails.getMediaGellary().get(currentGalleryPos).getUrl());
                    service.putExtra("pdfname", propertyDetails.getProj_name());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(service);
                    } else {
                        startService(service);
                    }
                    break;
                case RETRY_GALLERY:
                    String params = "project_id=" + propertyDetails.getId();
                    getGalleryData(params);
                    break;
                case RETRY_ENQUIRY:
                    showEnquryDialog();
                    break;
                case RETRY_LOTTERY_PROJECT:
                    getLotteryPriceBreakup();
                    break;
                case RETRY_UNIT_MAP:
                    goToUnitsMap();
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }

    private void showUnitImageDialog(String title, String url) {
        if (url == null || url.isEmpty()) return;
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        LayoutInflater factory = LayoutInflater.from(ctx);
        final View dialogView = factory.inflate(R.layout.unit_image_full_view_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
        final TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        tv_title.setTypeface(font);
        final ImageButton img_close = (ImageButton) dialogView.findViewById(R.id.img_close);
        LinearLayout ll_image_root = (LinearLayout) dialogView.findViewById(R.id.ll_image_root);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ImageView imageView = new TouchImageView(ProjectDetailActivity.this);
        imageView.setLayoutParams(imgParams);
        ll_image_root.addView(imageView);
        Glide.with(ProjectDetailActivity.this).load(url).placeholder(BMHConstants.PLACE_HOLDER).into(imageView);
        OnClickListener dialogViewsClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_close:
                        dialog.dismiss();
                        break;
                }
            }
        };
        img_close.setOnClickListener(dialogViewsClick);
        dialog.setView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }


}
