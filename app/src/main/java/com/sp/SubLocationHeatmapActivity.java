package com.sp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.adapters.SubLocHeatMCrouselAdptr;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.interfaces.CrouselOnItemClickListner;
import com.jsonparser.JsonParser;
import com.model.SubLocHeatMRespModel;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubLocationHeatmapActivity extends BaseFragmentActivity implements OnMapReadyCallback,CrouselOnItemClickListner{

    private static final String TAG = SubLocationHeatmapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private Button btn_infra,btn_needs,btn_price,btn_lifestyle,btn_returns;
    private ImageView iv_legends,iv_plus,iv_minus,iv_loading;
    private ViewPager heatmap_viewpager;

    private ObjectAnimator translateAnimation, translateAnimation2, translateAnimation3, translateAnimation4, translateAnimation5;
    private float currentZoom = 12.0f;
    private final int INFRA = 0, NEEDS = 1, LIFESTYLE = 2, PRICE = 3, RETURNS = 4;
    private Polygon lastPolygon;

    boolean isanimated = false;
    private int value = 30;
    private int duration = 800;
    private  AsyncThread mAsyncThread = null;
    private SubLocHeatMCrouselAdptr subLocHeatMCrouselAdptr = null;
    private  SubLocHeatMRespModel subLocHeatMRespModel = null;
    private int curPagerPosition = 0;
    private Polygon curSecPolygon;
    private HashMap<String,SubLocHeatMRespModel.SectorLists> mSectorPlygonMap = new HashMap<>();
    private Marker curMarker = null;
    private ArrayList<String> defaultLegends = new ArrayList<>(5);
    private IntentDataObject mIntentDataObject = null;
    private Toolbar toolbar = null;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
        if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null
                && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
        }

        initViews();
        setListeners();
        defaultUIState();

        setAnimation();
        toggleLegendsAnimation();
        getHeatMapData();

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
        if (mIntentDataObject.getData().get(ParamsConstants.TITLE) != null) {
            toolbar.setTitle(mIntentDataObject.getData().get(ParamsConstants.TITLE));
        } else {
            toolbar.setTitle(getString(R.string.select_locality));
        }
        invalidateOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_list, menu);
        menu.getItem(0).setVisible(true);
        menu.getItem(0).setIcon(R.drawable.list);
        menu.getItem(1).setVisible(false);
      /*  if (CURRENT_LIST_COUNT <= 1) {
            menu.getItem(1).setVisible(false);
        }else{
            menu.getItem(1).setVisible(true);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_heatmap:
                onBackPressed();
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

        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng india = new LatLng(28.611643, 77.211034);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
        mMap.setOnPolygonClickListener(mOnPolygonClickListener);
    }

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
                    drawSectorPolygon();
                    legendsState(defaultLegends);
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_needs:
                    lifeStyleButtonState(btn_needs);
                    drawSectorPolygon();
                    legendsState(defaultLegends);
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_price:
                    lifeStyleButtonState(btn_price);
                    drawSectorPolygon();
                    if(subLocHeatMRespModel != null) {
                       /* ArrayList<String> rsLegends = new ArrayList<>(5);
                        for (String legend : subLocHeatMRespModel.getLegend()){
                            rsLegends.add(getResources().getString(R.string.price_marker) + legend);
                        }*/
                        legendsState(subLocHeatMRespModel.getLegend());
                    }
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_lifestyle:
                    lifeStyleButtonState(btn_lifestyle);
                    legendsState(defaultLegends);
                    drawSectorPolygon();
                    isanimated = false;
                    toggleLegendsAnimation();
                    break;
                case R.id.btn_returns:
                    lifeStyleButtonState(btn_returns);
                    legendsState(defaultLegends);
                    drawSectorPolygon();
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
            String name =   subLocHeatMCrouselAdptr.modelArrayList.get(position).getSector_name();
            toolbar.setTitle(name);
            drawSectorPolygon();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Log.d(TAG,"onPageScrollStateChanged");
        }
    };

    private void drawSectorPolygon() {
        if(subLocHeatMCrouselAdptr == null)return;
        SubLocHeatMRespModel.SectorLists mSectorDetails =  subLocHeatMCrouselAdptr.modelArrayList.get(curPagerPosition);
        if(mSectorDetails == null || mSectorDetails.getSector_id() == null) return;
        ArrayList<LatLng> mSecLocList = new ArrayList<>(mSectorDetails.getSector_cord().size());
        for(SubLocHeatMRespModel.Coordinate mCordinates : mSectorDetails.getSector_cord()){
            mSecLocList.add(new LatLng(mCordinates.getLat(),mCordinates.getLng()));
        }
        int blockStrockColor = getResources().getColor(R.color.blue_color);
        int blockFillColor = getResources().getColor(R.color.grey_boarder);
        int selectedLifStyleValue = 0;
        if(btn_infra.isSelected()){
            selectedLifStyleValue = mSectorDetails.getInfra_code();
        }else if(btn_needs.isSelected()){
            selectedLifStyleValue = mSectorDetails.getNeeds_code();
        }else if(btn_price.isSelected()){
            selectedLifStyleValue = mSectorDetails.getAvg_pricePSF_code();
        }else if(btn_lifestyle.isSelected()){
            selectedLifStyleValue = mSectorDetails.getLifeStyle_code();
        }else if(btn_returns.isSelected()){
            selectedLifStyleValue = mSectorDetails.getReturn_code();
        }else{
            //TODO: error
        }
        if(selectedLifStyleValue == 0 || selectedLifStyleValue == 1){
            blockFillColor = getResources().getColor(R.color.legend_0_2);
        }else if(selectedLifStyleValue == 2 ){
            blockFillColor = getResources().getColor(R.color.legend_3_4);
        }else if(selectedLifStyleValue == 3 ){
            blockFillColor = getResources().getColor(R.color.legend_5_6);
        }else if(selectedLifStyleValue == 4){
            blockFillColor = getResources().getColor(R.color.legend_7_8);
        }else if(selectedLifStyleValue == 5){
            blockFillColor = getResources().getColor(R.color.legend_9_10);
        }

        if(curSecPolygon != null)curSecPolygon.remove();
        mSectorPlygonMap.clear();
        curSecPolygon = drawPolygon(mSecLocList,2 ,blockStrockColor,blockFillColor);
        if(curSecPolygon != null)
            mSectorPlygonMap.put(curSecPolygon.getId(),mSectorDetails);
        LatLng mLatLng = getCentroid(mSecLocList);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,currentZoom));
    }

    GoogleMap.OnPolygonClickListener mOnPolygonClickListener = new GoogleMap.OnPolygonClickListener() {
        @Override
        public void onPolygonClick(Polygon polygon) {
            if(curMarker != null)curMarker.remove();
            if(mSectorPlygonMap != null){
                SubLocHeatMRespModel.SectorLists mSectorDetails = mSectorPlygonMap.get(polygon.getId());
                if(mSectorDetails != null){
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(getCentroid(polygon.getPoints()))
                            .title(mSectorDetails.getSector_name()));
                    marker.setAlpha(0.0f);
                    marker.setInfoWindowAnchor(.5f,1.0f);
                    curMarker = marker;
                    marker.showInfoWindow();
                }
            }
        }
    };

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
        return Utils.removeLastAndSign(mStringBuilder.toString())!= null ? Utils.removeLastAndSign(mStringBuilder.toString()):mStringBuilder.toString();
    }
    private void getHeatMapData() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.HEATMAP);
        mBean.setRequestmethod(WEBAPI.POST);
        String params = getRequestParams();
        if(params != null || params.length() > 0) mBean.setJson(params);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.HEATMAP));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(SubLocationHeatmapActivity.this,mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
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
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case HEATMAP:
                        if(mIntentDataObject == null || mIntentDataObject.getData() == null)break;
                        HashMap<String ,String > mapParams =  mIntentDataObject.getData();
                        if(mapParams == null)break;
                        if(mapParams.get(ParamsConstants.SUB_LOCATION_ID) != null){
                            subLocHeatMRespModel = (SubLocHeatMRespModel) JsonParser.convertJsonToBean(APIType.GET_SUBLOCATIONS_HEATMAP, mBean.getJson());
                            ArrayList<SubLocHeatMRespModel.SectorLists> sectorList = null;
                            if(subLocHeatMRespModel!= null && subLocHeatMRespModel.isSuccess() && subLocHeatMRespModel.getSector_lists() != null) {
                                legendsState(subLocHeatMRespModel.getLegend());
                                sectorList = subLocHeatMRespModel.getSector_lists();
                                showMicroMarketBoundry();
                            }else{
                                sectorList = new ArrayList<>(1);
                                SubLocHeatMRespModel.SectorLists sector = new SubLocHeatMRespModel().new SectorLists();
                                sectorList.add(sector);
                            }
                            if(subLocHeatMCrouselAdptr != null)subLocHeatMCrouselAdptr = null;
                            subLocHeatMCrouselAdptr = new SubLocHeatMCrouselAdptr(SubLocationHeatmapActivity.this, SubLocationHeatmapActivity.this.getSupportFragmentManager(), sectorList,mapParams.get(ParamsConstants.SUB_LOCATION_NAME));
                            heatmap_viewpager.setAdapter(subLocHeatMCrouselAdptr);
                            mOnPageChangeListener.onPageSelected(heatmap_viewpager.getCurrentItem());
                            heatmap_viewpager.setClipToPadding(false);
                            heatmap_viewpager.setPadding((int)Utils.dp2px(30,SubLocationHeatmapActivity.this),0,(int)Utils.dp2px(30,SubLocationHeatmapActivity.this),0);
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });

    private void showMicroMarketBoundry() {
        if(subLocHeatMRespModel != null && subLocHeatMRespModel.getMicromarket_location() != null){
            ArrayList<LatLng> mList = new ArrayList<>(subLocHeatMRespModel.getMicromarket_location().size());
            for(SubLocHeatMRespModel.Coordinate mCordinates : subLocHeatMRespModel.getMicromarket_location()){
                mList.add(new LatLng(mCordinates.getLat(),mCordinates.getLng()));
            }
            int mColor = getResources().getColor(R.color.blue_color);
            Polygon mPolygon = drawPolygon(mList,4 ,mColor,0);
            LatLng mLatLng = getCentroid(mList);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,currentZoom));
        }
    }

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
    public void onclickCrouselItem(View v) {
        if(v.getTag() != null && v.getTag() instanceof SubLocHeatMRespModel.SectorLists){
            SubLocHeatMRespModel.SectorLists mSector = (SubLocHeatMRespModel.SectorLists)v.getTag();
            // Toast.makeText(this,mSector.getSector_name(),Toast.LENGTH_SHORT).show();
            IntentDataObject tempObj = new IntentDataObject();
            if (mIntentDataObject.getData() != null){
                for (Map.Entry<String, String> entry : mIntentDataObject.getData().entrySet()) {
                    tempObj.putData(entry.getKey(), entry.getValue());
                }
            }
            tempObj.putData(ParamsConstants.SUB_LOCATION_ID,mSector.getSector_id());
            tempObj.putData(ParamsConstants.TITLE, mSector.getSector_name());
            Intent mIntent = new Intent(SubLocationHeatmapActivity.this, ProjectHeatmapActivity.class);
            mIntent.putExtra(IntentDataObject.OBJ, tempObj);
            startActivity(mIntent);

        }
    }
}// End of class
