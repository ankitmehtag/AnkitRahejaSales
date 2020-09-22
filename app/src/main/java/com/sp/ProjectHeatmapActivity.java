package com.sp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.CitiesVO;
import com.adapters.ProjectHeatMCrouselAdptr;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.exception.BMHException;
import com.filter.Builder;
import com.fragments.BaseFragment;
import com.fragments.ProjectHeatMCrouselFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.interfaces.CrouselOnItemClickListner;
import com.interfaces.HostActivityInterface;
import com.jsonparser.JsonParser;
import com.model.LocationModel;
import com.model.NetworkErrorObject;
import com.model.ProjectsListRespModel;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.AppEnums.UIEventType.RETRY_GET_CITY;
import static com.helper.BMHConstants.CITYID;

public class ProjectHeatmapActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener,OnMapReadyCallback ,CrouselOnItemClickListner ,HostActivityInterface {

    private static final String TAG = ProjectHeatmapActivity.class.getSimpleName();

    private GoogleMap mMap;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private Button btn_infra,btn_needs,btn_price,btn_lifestyle,btn_returns;
    private ImageView iv_legends,iv_plus,iv_minus,iv_loading;
    private ViewPager heatmap_viewpager;

    private ObjectAnimator translateAnimation, translateAnimation2, translateAnimation3, translateAnimation4, translateAnimation5;
    private float currentZoom = 16.0f;
    private final int INFRA = 0, NEEDS = 1, LIFESTYLE = 2, PRICE = 3, RETURNS = 4;
    private Polygon lastPolygon;

    boolean isanimated = false;
    private int value = 30;
    private int duration = 800;
    private  AsyncThread mAsyncThread = null;
    private ProjectHeatMCrouselAdptr projectHeatMCrouselAdptr = null;
    private ProjectsListRespModel projectsListRespModel = null;
    private int curPagerPosition = 0;
    private ArrayList<Marker> projectMarkersList = new ArrayList<>();
    private HashMap<String,ProjectsListRespModel.Data> projectDataHashMap = new HashMap<>();
    private Marker curMarker = null;
    private ArrayList<String> defaultLegends = new ArrayList<>(5);
    private IntentDataObject mIntentDataObject = null;
    private final int LOGIN_REQ_CODE = 451;
    private static final int MULTI_FILTER = 452;
    private Toolbar toolbar = null;
    private HashMap<String,String> appliedFilterStateMap = null;
    private ArrayList<Builder> selectedBuilders = null;
    private String selectedBuilderIds = "";
    private boolean isInitialState = false;

    private BaseFragment selectedFragment;

    private TextView tv_toolbar_city;
    public View ll_toolbar_city;
    private static final int SELECT_CITY_REQ = 111;
    private CitiesVO citiesVo;
    private NetworkErrorObject mNetworkErrorObject = null;




    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
       /* if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null
                && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            selectedBuilders = (ArrayList<Builder>) mIntentDataObject.getObj();
        }*/


        mIntentDataObject = new IntentDataObject();
        mIntentDataObject.putData(BMHConstants.BUILDER_ID_KEY, BMHConstants.CURRENT_BUILDER_ID);


        isInitialState = true;
        initViews();
        setListeners();
        defaultUIState();
        setAnimation();
        toggleLegendsAnimation();
        getProjectsList(getRequestParams());
        tv_toolbar_city.setText(app.getFromPrefs(BMHConstants.CITYNAME));
        toolbar.setTitle(BMHConstants.BUILDER_NAME);

    }

    private void defaultUIState() {
        defaultLegends.add("0-2");
        defaultLegends.add("3-4");
        defaultLegends.add("5-6");
        defaultLegends.add("7-8");
        defaultLegends.add("9-10");
        btn_price.setSelected(true);
       /* iv_loading.setBackgroundResource(R.drawable.progress_loader);
        AnimationDrawable anim = (AnimationDrawable) iv_loading.getBackground();
        anim.start();*/
        if (mIntentDataObject == null || mIntentDataObject.getData() == null || toolbar == null) return;
        /*if (mIntentDataObject.getData().get(ParamsConstants.TITLE) != null) {
            toolbar.setTitle(mIntentDataObject.getData().get(ParamsConstants.TITLE));
        } else {
            toolbar.setTitle(getString(R.string.select_project));
        }*/
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_heatmap_menu, menu);
        //if(menu != null && menu.getItem(0) != null) {
          //  if (!app.getFromPrefs(BMHConstants.CITYID).equals("16")) {
                menu.getItem(0).setVisible(true);
                menu.getItem(0).setIcon(R.drawable.list);
            //} else {
              //  menu.getItem(0).setVisible(true);
            //}
        //}
        if(menu != null && menu.getItem(1) != null) {
            if (projectsListRespModel != null && projectsListRespModel.getData() != null && projectsListRespModel.getData().size() <= 1) {
                menu.getItem(1).setVisible(false);
            } else {
                menu.getItem(1).setVisible(true);
            }
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_filter:
                if(mIntentDataObject != null) {
                    Intent mIntent = new Intent(ProjectHeatmapActivity.this, FilterActivityActivity.class);
                    mIntentDataObject.setFilterStateMap(appliedFilterStateMap);
                    mIntentDataObject.setObj(selectedBuilders);
                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    startActivityForResult(mIntent, MULTI_FILTER);
                }
                break;
            case R.id.action_heatmap:
                finish();
                break;
        }
        return true;
    }

    private void initViews(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.heatmap);
        mapFragment.getMapAsync(this);
        heatmap_viewpager = (ViewPager)findViewById(R.id.heatmap_viewpager);
        btn_infra = (Button)findViewById(R.id.btn_infra);
        btn_needs = (Button)findViewById(R.id.btn_needs);
        btn_price = (Button)findViewById(R.id.btn_price);
        btn_lifestyle = (Button)findViewById(R.id.btn_lifestyle);
        btn_returns = (Button)findViewById(R.id.btn_returns);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        iv_legends = (ImageView)findViewById(R.id.iv_legends);
        iv_plus = (ImageView)findViewById(R.id.iv_plus);
        iv_minus = (ImageView)findViewById(R.id.iv_minus);
        iv_loading = (ImageView)findViewById(R.id.img);

        /*toolbar = setToolBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        toolbar = setDrawerAndToolbar();

        tv_toolbar_city = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ll_toolbar_city = (LinearLayout) toolbar.findViewById(R.id.ll_toolbar_city);

    }

    private void setListeners(){
        iv_legends.setOnClickListener(mOnClickListener);
        btn_infra.setOnClickListener(mOnClickListener);
        btn_needs.setOnClickListener(mOnClickListener);
        btn_price.setOnClickListener(mOnClickListener);
        btn_lifestyle.setOnClickListener(mOnClickListener);
        btn_returns.setOnClickListener(mOnClickListener);
        iv_plus.setOnClickListener(mOnClickListener);
        iv_minus.setOnClickListener(mOnClickListener);
        heatmap_viewpager.addOnPageChangeListener (mOnPageChangeListener);
        ll_toolbar_city.setOnClickListener(mOnClickListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng india = new LatLng(28.611643, 77.211034);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
        mMap.setOnMarkerClickListener(markerClickListener);
    }

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if(projectDataHashMap != null && projectDataHashMap.get(marker.getId()) != null && projectHeatMCrouselAdptr != null){
                ProjectsListRespModel.Data mData  = projectDataHashMap.get(marker.getId());
                //TODO: action what to do
                projectHeatMCrouselAdptr.setCurrentItem(mData.getID(),heatmap_viewpager);
            }
            marker.showInfoWindow();
            return true;
        }
    };

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_legends:
                    value = tv1.getHeight();
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_infra:
                    lifeStyleButtonState(btn_infra);
                    drawBlockPolygon();
                    legendsState(defaultLegends);
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_needs:
                    lifeStyleButtonState(btn_needs);
                    drawBlockPolygon();
                    legendsState(defaultLegends);
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_price:
                    lifeStyleButtonState(btn_price);
                    drawBlockPolygon();
                    if(projectsListRespModel != null){
                        /*ArrayList<String> rsLegends = new ArrayList<>(5);
                        for (String legend : projectsListRespModel.getLegend()){
                            rsLegends.add(getResources().getString(R.string.price_marker) + legend);
                        }*/
                        legendsState(projectsListRespModel.getLegend());
                    }
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_lifestyle:
                    lifeStyleButtonState(btn_lifestyle);
                    legendsState(defaultLegends);
                    drawBlockPolygon();
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_returns:
                    lifeStyleButtonState(btn_returns);
                    legendsState(defaultLegends);
                    drawBlockPolygon();
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.iv_plus:
                    if(mMap != null)
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    break;
                case R.id.iv_minus:
                    if(mMap != null)
                        mMap.animateCamera(CameraUpdateFactory.zoomOut());
                    break;
                case R.id.ll_toolbar_city:
                    if(citiesVo == null){
                        if(ConnectivityReceiver.isConnected()){
                            startCityTask();
                        }else{
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectHeatmapActivity.this, UIEventType.RETRY_GET_CITY, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(ConnectivityReceiver.isConnected()) {
                                        startCityTask();
                                    }else{
                                        showToast(getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                        }
                    }else{
                        CityButtonClick();
                    }
                    break;
            }
        }
    };


    private void setAnimation(){
        translateAnimation = ObjectAnimator.ofFloat(tv1, View.TRANSLATION_Y, Utils.dp2px(value,this));
        translateAnimation.setDuration(duration);
        translateAnimation.setInterpolator(new OvershootInterpolator());

        translateAnimation2 = ObjectAnimator.ofFloat(tv2, View.TRANSLATION_Y, Utils.dp2px(value*2,this));
        translateAnimation2.setDuration(duration);
        translateAnimation2.setInterpolator(new OvershootInterpolator());

        translateAnimation3 = ObjectAnimator.ofFloat(tv3, View.TRANSLATION_Y, Utils.dp2px(value*3,this));
        translateAnimation3.setDuration(duration);
        translateAnimation3.setInterpolator(new OvershootInterpolator());

        translateAnimation4 = ObjectAnimator.ofFloat(tv4, View.TRANSLATION_Y, Utils.dp2px(value*4,this));
        translateAnimation4.setDuration(duration);
        translateAnimation4.setInterpolator(new OvershootInterpolator());

        translateAnimation5 = ObjectAnimator.ofFloat(tv5, View.TRANSLATION_Y, Utils.dp2px(value*5,this));
        translateAnimation5.setDuration(duration);
        translateAnimation5.setInterpolator(new OvershootInterpolator());

        translateAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                isanimated = isanimated ? false : true;
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }
    private void toggleLegendsAnimation(){
        if (!isanimated) {
            iv_legends.setImageResource(R.drawable.arrow_up);
            translateAnimation.start();
            translateAnimation2.start();
            translateAnimation3.start();
            translateAnimation4.start();
            translateAnimation5.start();
        } else {
            iv_legends.setImageResource(R.drawable.arrow_down);
            translateAnimation.reverse();
            translateAnimation2.reverse();
            translateAnimation3.reverse();
            translateAnimation4.reverse();
            translateAnimation5.reverse();
        }
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Log.d(TAG,"onPageScrolled"+position);
        }

        @Override
        public void onPageSelected(int position) {
            curPagerPosition = position;
            drawBlockPolygon();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Log.d(TAG,"onPageScrollStateChanged");
        }
    };

    private void drawBlockPolygon() {
        if(projectMarkersList != null){
            for(Marker marker : projectMarkersList)if(marker != null)marker.remove();
            projectMarkersList.clear();
        }
        if(projectDataHashMap != null) projectDataHashMap.clear();
        if(projectHeatMCrouselAdptr == null)return;
        ProjectsListRespModel.Data mLocationList =  projectHeatMCrouselAdptr.modelArrayList.get(curPagerPosition);
        if(mLocationList == null || mLocationList.getID() == null) return;;
        LatLng mSecLocList = new LatLng(mLocationList.getLatitude(),mLocationList.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mSecLocList,currentZoom));
        if(projectHeatMCrouselAdptr.modelArrayList != null) {
            for (ProjectsListRespModel.Data dataList : projectHeatMCrouselAdptr.modelArrayList ) {
                int selectedLifStyleValue = 0;
                if (btn_infra.isSelected()) {
                    selectedLifStyleValue = dataList.getInfra_code();
                } else if (btn_needs.isSelected()) {
                    selectedLifStyleValue = dataList.getNeeds_code();
                } else if (btn_price.isSelected()) {
                    selectedLifStyleValue = dataList.getPsf_code();
                } else if (btn_lifestyle.isSelected()) {
                    selectedLifStyleValue = dataList.getLife_style_code();
                } else if (btn_returns.isSelected()) {
                    selectedLifStyleValue = dataList.getReturns_code();
                } else {
                    //TODO: error
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(dataList.getLatitude(), dataList.getLongitude()));
                Spanned title = Html.fromHtml(dataList.getDisplay_name());
                markerOptions.title(title.toString());
                if (selectedLifStyleValue == 0 || selectedLifStyleValue == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_0_2));
                } else if (selectedLifStyleValue == 2) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_3_4));
                } else if (selectedLifStyleValue == 3) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_5_6));
                } else if (selectedLifStyleValue == 4) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_7_8));
                } else if (selectedLifStyleValue == 5) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_9_10));
                }
                Marker marker = mMap.addMarker(markerOptions);
                projectMarkersList.add(marker);
                projectDataHashMap.put(marker.getId(), dataList);
                if (dataList.getID().equalsIgnoreCase(mLocationList.getID())) {
                    curMarker = marker;
                    curMarker.showInfoWindow();
                }
            }// End of loop
        }
    }


    private void lifeStyleButtonState(View selectedView){
        btn_infra.setSelected(false);
        btn_needs.setSelected(false);
        btn_price.setSelected(false);
        btn_lifestyle.setSelected(false);
        btn_returns.setSelected(false);
        if(selectedView != null)selectedView.setSelected(true);
    }

    private void legendsState(ArrayList<String>legends){
        if(legends != null && legends.size()>= 5) {
            tv1.setText(legends.get(0));
            tv2.setText(legends.get(1));
            tv3.setText(legends.get(2));
            tv4.setText(legends.get(3));
            tv5.setText(legends.get(4));
        }
    }

    private String getRequestParams(){
        if(mIntentDataObject == null || mIntentDataObject.getData() == null)return "";
        mIntentDataObject.putData("city_id",app.getFromPrefs(CITYID));
        HashMap<String ,String > mapParams =  mIntentDataObject.getData();
        if(mapParams == null)return "";
        StringBuilder mStringBuilder = new StringBuilder("");
        for (Map.Entry<String, String> entry : mIntentDataObject.getData().entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                mStringBuilder.append(entry.getKey());
                mStringBuilder.append("=");
                mStringBuilder.append(entry.getValue());
                mStringBuilder.append("&");
            }
        }
        /*if(selectedBuilders != null){
            for (Builder mBuilder : selectedBuilders){

            }
        }*/
        if (setSelectedBuilders(selectedBuilders) != null && selectedBuilderIds.length() >= 2) {
            mStringBuilder.append("&");
            mStringBuilder.append("builders=");
            mStringBuilder.append(selectedBuilderIds);
        }
        return Utils.removeLastAndSign(mStringBuilder.toString())!= null ? Utils.removeLastAndSign(mStringBuilder.toString()):mStringBuilder.toString();
    }

    private String setSelectedBuilders(ArrayList<Builder> selectedBuilders){
        if(selectedBuilders == null){
            Log.i(TAG, "Selected Builders is null");
        } else {
            ArrayList<String> builderText = new ArrayList<>();
            for (Builder tempBuilder : selectedBuilders) {
                if(tempBuilder.isChecked())builderText.add(tempBuilder.getName());
            }
            String temp = TextUtils.join(",", builderText);
            selectedBuilderIds = TextUtils.join(",", builderText);
        }
        return selectedBuilderIds;
    }
    private void getProjectsList(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.SEARCH_PROJECTS);
        mBean.setRequestmethod(WEBAPI.POST);
        if(params != null && params.length() > 0)
            mBean.setJson(params);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.SEARCH_PROJECTS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ProjectHeatmapActivity.this,mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
    }
    private void favRequest(String id){
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.FAV_PROJECT);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.FAV_PROJECT));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.USER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(userId);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.ID);
        mStringBuilder.append("=");
        mStringBuilder.append(id);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.TYPE);
        mStringBuilder.append("=");
        mStringBuilder.append(ParamsConstants.BUY);
        mBean.setJson(mStringBuilder.toString());
        mBean.setRequestObj(id);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if(mAsyncThread != null)mAsyncThread.cancel(true);
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
                    case SEARCH_PROJECTS:
                        projectsListRespModel = (ProjectsListRespModel) JsonParser.convertJsonToBean(APIType.SEARCH_PROJECTS, mBean.getJson());
                        ArrayList<ProjectsListRespModel.Data> dataList = null;
                        if (projectsListRespModel != null && projectsListRespModel.isSuccess() && projectsListRespModel.getData() != null) {
                            if(isInitialState) {
                                legendsState(projectsListRespModel.getLegend());

                            }
                            dataList = projectsListRespModel.getData();
                            selectedBuilders = getBuilders(dataList);
                            if (projectsListRespModel.getCity_location() != null) {
                                projectsListRespModel.setCityCodrs(projectsListRespModel.parseCords(projectsListRespModel.getCity_location()));
                            }
                        } else {
                            Log.e(TAG, "Heat map data is null");
                            dataList = new ArrayList<>(1);
                            ProjectsListRespModel.Data mData = new ProjectsListRespModel().new Data();
                            dataList.add(mData);
                        }
                        if(projectsListRespModel.getCityCodrs() != null) {
                            ArrayList<ProjectsListRespModel.Cordinates> cityCodrs = projectsListRespModel.getCityCodrs();
                            ArrayList<LatLng> mLatLngs = new ArrayList<>(cityCodrs.size());
                            for (ProjectsListRespModel.Cordinates latlng : cityCodrs) {
                                mLatLngs.add(new LatLng(latlng.getLat(), latlng.getLng()));
                            }
                            int mColor = getResources().getColor(R.color.blue_color);
                            int mFillColor = 0;//getResources().getColor(R.color.heatmap_bg);
                            drawPolygon(mLatLngs, 4, mColor, mFillColor);
                        }
                        if(projectHeatMCrouselAdptr != null)projectHeatMCrouselAdptr = null;
                        projectHeatMCrouselAdptr = new ProjectHeatMCrouselAdptr(ProjectHeatmapActivity.this, ProjectHeatmapActivity.this.getSupportFragmentManager(), dataList);
                        heatmap_viewpager.setAdapter(projectHeatMCrouselAdptr);
                        mOnPageChangeListener.onPageSelected(heatmap_viewpager.getCurrentItem());
                        heatmap_viewpager.setClipToPadding(false);
                        heatmap_viewpager.setPadding((int)Utils.dp2px(30,ProjectHeatmapActivity.this),0,(int)Utils.dp2px(30,ProjectHeatmapActivity.this),0);
                        if(isInitialState) {
                            invalidateOptionsMenu();
                        }
                        isInitialState = false;
                        break;
                    case FAV_PROJECT:
                        if(projectHeatMCrouselAdptr != null && mBean.getRequestObj() != null && mBean.getRequestObj() instanceof  String) {
                            String id = (String) mBean.getRequestObj();
                            projectHeatMCrouselAdptr.toggleFav(id);
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });

    /*private void showMicroMarketBoundry() {
        if(projectsListRespModel != null && projectsListRespModel.getCodrs() != null){
            ArrayList<LatLng> mList = new ArrayList<>(projectsListRespModel.getCodrs().size());
            for(LocationHeatmapRespModel.Cordinates mCordinates : projectsListRespModel.getCodrs()){
                mList.add(new LatLng(mCordinates.getLat(),mCordinates.getLng()));
            }
            int mColor = getResources().getColor(R.color.tab_underline_color);
            Polygon mPolygon = drawPolygon(mList,2 ,mColor,0);
            LatLng mLatLng = getCentroid(mList);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,currentZoom));

        }
    }*/

    public Polygon drawPolygon(ArrayList<LatLng> mList, int strokeWidth, int strokeColor, int fillColor) {
        if(mList == null || mList.size() == 0 || mMap == null)return null;
        PolygonOptions polygonOptions = new PolygonOptions();
        try {
            polygonOptions.addAll(mList);
            polygonOptions.strokeWidth(strokeWidth);
            polygonOptions.strokeColor(strokeColor);
            polygonOptions.fillColor(fillColor);
        } catch (Exception e) {
            Log.e(TAG,"drawPolygon():"+e);
        }
        return mMap.addPolygon(polygonOptions);
    }


    private LatLng getCentroid(List<LatLng> points) {
        double[] centroid = { 0.0, 0.0 };
        for (int i = 0; i < points.size(); i++) {
            centroid[0] += points.get(i).latitude;
            centroid[1] += points.get(i).longitude;
        }
        int totalPoints = points.size();
        centroid[0] = centroid[0] / totalPoints;
        centroid[1] = centroid[1] / totalPoints;
        LatLng l = new LatLng(centroid[0], centroid[1]);
        return l;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("hh location alert fragment acti result");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE && resultCode == RESULT_OK) {
            String projectId = app.getFromPrefs(BMHConstants.VALUE);
            if(projectId != null && projectId.length() > 0) {
                favRequest(projectId);
                app.saveIntoPrefs(BMHConstants.VALUE, "");
            }
        }else if(requestCode == MULTI_FILTER && resultCode == RESULT_OK){
            if(data != null && data.getSerializableExtra(IntentDataObject.OBJ) != null
                    && data.getSerializableExtra(IntentDataObject.OBJ) instanceof  IntentDataObject){
                IntentDataObject mIntentDataObject = (IntentDataObject) data.getSerializableExtra(IntentDataObject.OBJ);
                appliedFilterStateMap = mIntentDataObject.getData();
                if(mIntentDataObject.getObj() != null) selectedBuilders = (ArrayList<Builder>) mIntentDataObject.getObj();
                if( appliedFilterStateMap != null) {
                    StringBuilder mStringBuilder = new StringBuilder("");
                    for (Map.Entry<String, String> entry : appliedFilterStateMap.entrySet()) {
                        if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                            mStringBuilder.append(entry.getKey());
                            mStringBuilder.append("=");
                            mStringBuilder.append(entry.getValue());
                            mStringBuilder.append("&");
                        }
                    }
                    if (setSelectedBuilders(selectedBuilders) != null && selectedBuilderIds.length() >= 2) {
                        mStringBuilder.append("&");
                        mStringBuilder.append("builders=");
                        mStringBuilder.append(selectedBuilderIds);
                    }
                    getProjectsList(mStringBuilder.toString());
                }

            }else{
                //TODO: error
            }
        }else if (requestCode == SELECT_CITY_REQ && resultCode == Activity.RESULT_OK) {
            String city = data.getStringExtra("city");
            tv_toolbar_city .setText(app.getFromPrefs(BMHConstants.CITYNAME));
            if (ConnectivityReceiver.isConnected()) {
                //TODO: network call
                getProjectsList(getRequestParams());

            } else {
                mNetworkErrorObject = Utils.showNetworkErrorDialog(this, UIEventType.RETRY_SEARCH_PROJECTS,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ConnectivityReceiver.isConnected()) {
                                    //TODO: network call
                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                    mNetworkErrorObject = null;
                                } else {
                                    showToast(getString(R.string.check_your_internet_connection));
                                }
                            }
                        });
            }
        }

    }


    @Override
    public void onclickCrouselItem(View v) {
        if (v.getTag(R.integer.event) instanceof Integer) {
            int event = (int) v.getTag(R.integer.event);
            if (event == ProjectHeatMCrouselFragment.FAVORITE_CLICK) {
                if (v.getTag(R.integer.project_item) != null && v.getTag(R.integer.project_item) instanceof ProjectsListRespModel.Data) {
                    ProjectsListRespModel.Data data = (ProjectsListRespModel.Data) v.getTag(R.integer.project_item);
                    //Toast.makeText(this, "Fav:"+data.getDisplay_name(), Toast.LENGTH_SHORT).show();
                    if(app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0){
                        app.saveIntoPrefs(BMHConstants.VALUE,data.getID());
                        Intent i = new Intent(this, LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT,false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE);
                    }else{
                        favRequest(data.getID());
                    }
                }
            }else if (event == ProjectHeatMCrouselFragment.CAROUSEL_ITEM_CLICK) {
                if (v.getTag(R.integer.project_item) != null && v.getTag(R.integer.project_item) instanceof ProjectsListRespModel.Data) {
                    ProjectsListRespModel.Data model = (ProjectsListRespModel.Data) v.getTag(R.integer.project_item);
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    mIntentDataObject.putData(ParamsConstants.ID, model.getID());
                    mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
                    Intent mIntent = new Intent(ProjectHeatmapActivity.this, ProjectDetailActivity.class);
                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    startActivity(mIntent);
                }
            }
        }
    }

    private ArrayList<Builder> getBuilders(ArrayList<ProjectsListRespModel.Data> dataList) {
        if(dataList == null || dataList.size() == 0)return null;
        ArrayList<Builder> mBuilders = new ArrayList<>();
        ArrayList<String> tempBuilderIds = new ArrayList<>();
        for(ProjectsListRespModel.Data model : dataList){
            if(model != null && model.getBuilder_id() != null && !tempBuilderIds.contains(model.getBuilder_id())){
                tempBuilderIds.add(model.getBuilder_id());
                mBuilders.add(new Builder(model.getBuilder_id(),model.getBuilder_name(),false));
            }
        }
        return mBuilders;

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

    public void clearBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager != null && fragmentManager.getBackStackEntryCount() != 0) {
            try {
                fragmentManager.popBackStackImmediate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void startCityTask() {

        CustomAsyncTask loadingTask = new CustomAsyncTask(this, new CustomAsyncTask.AsyncListner() {

            @Override
            public void OnBackgroundTaskCompleted() {
                if (citiesVo == null) {
                    if(ConnectivityReceiver.isConnected()){
                        //TODO: network call
                        startCityTask();
                    }else{
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(ProjectHeatmapActivity.this, RETRY_GET_CITY,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(ConnectivityReceiver.isConnected()){
                                            //TODO: network call
                                            startCityTask();
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        }else{
                                            showToast(getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }

                } else {
                    if (citiesVo.isSuccess() && citiesVo.getArrCity().size() > 0) {
                        // setCityAutocompleteAdapter(citiesVo);
                        CityButtonClick();
                    } else {
                        showToast(citiesVo.getMessage());
                    }
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                LocationModel model = new LocationModel();
                try {
                    citiesVo = model.getCities(app);
                } catch (BMHException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {

            }
        });
        //loadingTask.dontShowProgressDialog();
        // loaderView.setVisibility(View.VISIBLE);
        loadingTask.execute("");
    }

    public void CityButtonClick() {
        Gson gson = new Gson();
        String modelData = gson.toJson(citiesVo);
        Intent intent = new Intent(this, CitySearchActivity.class);
        intent.putExtra("data", modelData);
        startActivityForResult(intent, SELECT_CITY_REQ);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
            switch (mNetworkErrorObject.getUiEventType()){
                case RETRY_SEARCH_PROJECTS:
                    getProjectsList(getRequestParams());
                    break;
                case RETRY_APPLY_FILTER:

                    break;
                case RETRY_FEV:
                    break;
                case RETRY_ALERT:
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }

}// End of class
