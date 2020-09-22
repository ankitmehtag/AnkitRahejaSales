package com.sp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.BoundaryUnitVO;
import com.VO.UnitCaraouselListVO;
import com.VO.UnitCaraouselVO;
import com.adapters.UnitCrouselPagerAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.filter.UnitFilter;
import com.fragments.UnitCrouselFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.helper.BMHConstants;
import com.helper.ContentLoader;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.interfaces.CrouselOnItemClickListner;
import com.interfaces.PagerListner;
import com.model.NetworkErrorObject;
import com.model.PropertyModel;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitMapActivity extends BaseFragmentActivity implements CrouselOnItemClickListner,
        PagerListner, OnMapReadyCallback, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = UnitMapActivity.class.getSimpleName();
    public ViewPager pager;
    private GoogleMap map;
    private String projId, projPlanImg, sE, nW, FlatTypology, TotalTypes, PriceSqft,projectType,proj_residential;
    private String unitIds = "";
    private UnitCaraouselListVO basevo;
    private Polygon lastPolygon = null;
    private float ZOMM_LEVEL = 19.0f;
    private int totalFloors = 0;
    public HashMap<String, String> searchParams;
    private Toolbar toolbar;
    private ImageView imgPlus, imgMinus;
    private AsyncThread mAsyncThread = null;
    private final int LOGIN_REQ_CODE = 451, UNIT_LIST = 452, UNIT_DETAILS = 453;
    private UnitCrouselPagerAdapter unitCrouselPagerAdapter;
    private ArrayList<UnitCaraouselVO> unitList;
    private IntentDataObject mIntentDataObject;
    private NetworkErrorObject mNetworkErrorObject = null;
    private String favUnitId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_map);
        initViews();
        setListeners();
        Intent i = getIntent();
        searchParams = (HashMap<String, String>) i.getSerializableExtra("searchParams");
        projId = i.getStringExtra("projectId");

        projectType = i.getStringExtra("project_type");

      //  proj_residential = i.getStringExtra("Residentail_type");
        if(projectType!=null)
            projPlanImg = i.getStringExtra("pro_plan_img");



        // projPlanImg = i.getStringExtra("pro_plan_img");
        sE = i.getStringExtra("se");
        nW = i.getStringExtra("nw");
        unitIds = i.getStringExtra("unitIds");
        FlatTypology = i.getStringExtra("flat_typology");
        TotalTypes = i.getStringExtra("total_types");
        PriceSqft = i.getStringExtra("price_range");
        toolbar.setTitle(Html.fromHtml("Select Unit " + "(" + i.getStringExtra("flat_typology") + ")"));
        getData();
        if (unitIds == null || unitIds.length() == 0) unitIds = "";
        if (sE != null && nW != null && sE.length() > 0 && nW.length() > 0)
            startDownloadImageTask();

    }

    private void initViews() {
        toolbar = setToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapId);
        mapFragment.getMapAsync(this);
        pager = (ViewPager) findViewById(R.id.myviewpager);
        imgPlus = (ImageView) findViewById(R.id.imgPlus);
        imgMinus = (ImageView) findViewById(R.id.imgMinus);
    }

    private void setListeners() {
        pager.addOnPageChangeListener(mOnPageChangeListener);
        imgMinus.setOnClickListener(mOnClickListener);
        imgPlus.setOnClickListener(mOnClickListener);
    }

    private void setPagerView(ArrayList<UnitCaraouselVO> units) {
        unitCrouselPagerAdapter = new UnitCrouselPagerAdapter(UnitMapActivity.this, this.getSupportFragmentManager(), units);
        pager.setAdapter(unitCrouselPagerAdapter);
        pager.setCurrentItem(0);
        pager.setClipToPadding(false);
        pager.setPadding((int) Utils.dp2px(30, UnitMapActivity.this), 0, (int) Utils.dp2px(30, UnitMapActivity.this), 0);
        if (units.get(0) != null && units.get(0).getArrUnitBoundary() != null)
            DrawPolygon(units.get(0).getArrUnitBoundary());

        if(projectType.contains("Commercial")) {
            projPlanImg = units.get(0).getImage_unit();
            startDownloadImageTask();
        }
    }

    OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            onPageChnaged(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgPlus:
                    if (map != null)
                        map.animateCamera(CameraUpdateFactory.zoomIn());
                    break;
                case R.id.imgMinus:
                    if (map != null)
                        map.animateCamera(CameraUpdateFactory.zoomOut());
                    break;
            }
        }
    };

    private void DrawPolygon(ArrayList<BoundaryUnitVO> mModelList) {
        if (lastPolygon != null) lastPolygon.remove();
        if (mModelList == null || mModelList.size() == 0) return;
        ArrayList<LatLng> mLatLngs = new ArrayList<>(mModelList.size());
        for (BoundaryUnitVO model : mModelList) {
            if (model != null) mLatLngs.add(new LatLng(model.getLat(), model.getLon()));
        }
        PolygonOptions polyOpt = new PolygonOptions();
        polyOpt.strokeColor(Color.parseColor("#ff0000"));
        polyOpt.strokeWidth(2);
        polyOpt.fillColor(Color.parseColor("#66ff0000"));
        polyOpt.addAll(mLatLngs);
        if (map != null) {
            lastPolygon = map.addPolygon(polyOpt);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mModelList.get(0).getLat(), mModelList.get(0).getLon()), ZOMM_LEVEL));
        }
    }


    private void setProjectImageOnMap(Bitmap bitmap) {
        if (bitmap == null || sE == null || nW == null || map == null) return;
        String[] seArr = sE.split(",");
        String[] nWArr = nW.split(",");
        try {
            double seLat = Double.parseDouble(seArr[0]);
            double seLng = Double.parseDouble(seArr[1]);
            double nwLat = Double.parseDouble(nWArr[0]);
            double nwLng = Double.parseDouble(nWArr[1]);
            LatLngBounds imageBounds = new LatLngBounds(new LatLng(seLat, seLng), new LatLng(nwLat, nwLng));
            GroundOverlayOptions newarkMap = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromBitmap(bitmap)).positionFromBounds(imageBounds);
            map.addGroundOverlay(newarkMap);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            app.showToast("Lat Long of project not found.");
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            app.showToast("Lat Long of project not found.");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.unit_image_filter, menu);
        /*if(menu != null && menu.getItem(0) != null) {
			if (unitList != null && unitList.size() <= 1) {
				menu.getItem(0).setVisible(false);
			} else {
				menu.getItem(0).setVisible(true);
			}
		}*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_list:
                Intent mUnitList = new Intent(this, UnitListActivity.class);
                mUnitList.putParcelableArrayListExtra("units", basevo.getArrUnits());
                mUnitList.putExtra("searchParams", searchParams);
                startActivityForResult(mUnitList, UNIT_LIST);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_in);
                break;
            case R.id.action_unit_filter:
                Intent intent = new Intent(this, UnitFilter.class);
                intent.putExtra("searchParams", searchParams);
                intent.putExtra("totalFloors", totalFloors);
                intent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivityForResult(intent, BMHConstants.FILTER_ACTION);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            case android.R.id.home:
                finish();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BMHConstants.FILTER_ACTION) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
                    mIntentDataObject = (IntentDataObject) data.getSerializableExtra(IntentDataObject.OBJ);
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                        getData();
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(UnitMapActivity.this, UIEventType.RETRY_UNIT_DETAILS,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            getData();
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(UnitMapActivity.this, getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == LOGIN_REQ_CODE && resultCode == RESULT_OK) {
            String unitId = app.getFromPrefs(BMHConstants.VALUE);
            if (unitId != null && !unitId.isEmpty()) {
                favUnitId = unitId;
                if (ConnectivityReceiver.isConnected()) {
                    //TODO: network call
                    favRequest(favUnitId);
                } else {
                    mNetworkErrorObject = Utils.showNetworkErrorDialog(UnitMapActivity.this, UIEventType.RETRY_FEV,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        //TODO: network call
                                        favRequest(favUnitId);
                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                        mNetworkErrorObject = null;
                                    } else {
                                        Utils.showToast(UnitMapActivity.this, getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                }

                app.saveIntoPrefs(BMHConstants.VALUE, "");
            }
        } else if ((requestCode == UNIT_LIST || requestCode == UNIT_DETAILS) && resultCode == RESULT_OK) {
            if (ConnectivityReceiver.isConnected()) {
                //TODO: network call
                getData();
            } else {
                mNetworkErrorObject = Utils.showNetworkErrorDialog(UnitMapActivity.this, UIEventType.RETRY_UNIT_DETAILS,
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ConnectivityReceiver.isConnected()) {
                                    //TODO: network call
                                    getData();
                                    mNetworkErrorObject.getAlertDialog().dismiss();
                                    mNetworkErrorObject = null;
                                } else {
                                    Utils.showToast(UnitMapActivity.this, getString(R.string.check_your_internet_connection));
                                }
                            }
                        });
            }
        }
    }

    private void getData() {
        StringBuilder mStringBuilder = new StringBuilder("");
        if (mIntentDataObject != null && mIntentDataObject.getData() != null) {
            if (mIntentDataObject.getData().get(ParamsConstants.PRICE) != null) {
                if (mIntentDataObject.getData().get(ParamsConstants.PRICE).equalsIgnoreCase(ParamsConstants.PRICE_LTOH)) {
                    //mStringBuilder.append("&"+ParamsConstants.PRICE_LTOH+"=");
                    mStringBuilder.append(mIntentDataObject.getData().get(ParamsConstants.PRICE));
                } else if (mIntentDataObject.getData().get(ParamsConstants.PRICE).equalsIgnoreCase(ParamsConstants.PRICE_HTOL)) {
                    //mStringBuilder.append("&"+ParamsConstants.PRICE_HTOL+"=");
                    mStringBuilder.append(mIntentDataObject.getData().get(ParamsConstants.PRICE));
                }
            }
            if (mIntentDataObject.getData().get(ParamsConstants.MINFLOOR) != null) {
                mStringBuilder.append("&" + ParamsConstants.MINFLOOR + "=");
                mStringBuilder.append(mIntentDataObject.getData().get(ParamsConstants.MINFLOOR));
            }
            if (mIntentDataObject.getData().get(ParamsConstants.MAXFLOOR) != null) {
                mStringBuilder.append("&" + ParamsConstants.MAXFLOOR + "=");
                mStringBuilder.append(mIntentDataObject.getData().get(ParamsConstants.MAXFLOOR));
            }
        }
        startUnitListTask(mStringBuilder.toString());
    }

    @Override
    protected String setActionBarTitle() {
        return "Select Unit";
    }

    @Override
    public void onPageChnaged(int position) {
        try {
            if (lastPolygon != null) lastPolygon.remove();
            if (basevo != null && basevo.getArrUnits().get(position) != null && basevo.getArrUnits().get(position).getArrUnitBoundary() != null)
                DrawPolygon(basevo.getArrUnits().get(position).getArrUnitBoundary());
        } catch (Exception e) {

        }
    }

    @Override
    public void onclickCrouselItem(View v) {
		/*if (Connectivity.isConnectedWithDoalog(this)) {
			int pos = (Integer) v.getTag();
			if(basevo != null && basevo.getArrUnits() != null && basevo.getArrUnits().get(pos) != null
					&& !basevo.getArrUnits().get(pos).getSold_status().equalsIgnoreCase("sold")) {
				Intent unitDetailsIntent = new Intent(this, UnitDetailActivity.class);
				unitDetailsIntent.putExtra("unitId", basevo.getArrUnits().get(pos).getUnit_id());
				unitDetailsIntent.putExtra("unitTitle", basevo.getArrUnits().get(pos).getUnit_title());
				unitDetailsIntent.putExtra("searchParams", searchParams);
				unitDetailsIntent.putExtra("bhkType", basevo.getArrUnits().get(pos).getFlat_typology());
				unitDetailsIntent.putExtra("unitType", basevo.getArrUnits().get(pos).getTotal_types());
				unitDetailsIntent.putExtra("priceSqft", basevo.getArrUnits().get(pos).getPrice_SqFt());
				startActivity(unitDetailsIntent);
			}else{
				showToast(getString(R.string.unit_reserved));
			}
			//startActivityForResult(i, 1111);
		}*/

        if (v.getTag(R.integer.unit) != null && v.getTag(R.integer.unit) instanceof UnitCaraouselVO) {
            UnitCaraouselVO mUnitCaraouselVO = (UnitCaraouselVO) v.getTag(R.integer.unit);
            if (v.getTag(R.integer.event) != null) {
                if ((int) v.getTag(R.integer.event) == UnitCrouselFragment.CAROUSEL_ITEM_CLICK) {
                    if (mUnitCaraouselVO != null && !mUnitCaraouselVO.getSold_status().equalsIgnoreCase("sold")) {
                        Intent unitDetailsIntent = new Intent(this, UnitDetailActivity.class);
                        unitDetailsIntent.putExtra("unitId", mUnitCaraouselVO.getUnit_id());
                        unitDetailsIntent.putExtra("unitTitle", mUnitCaraouselVO.getUnit_title());
                        unitDetailsIntent.putExtra("searchParams", searchParams);
                        unitDetailsIntent.putExtra("bhkType", mUnitCaraouselVO.getFlat_typology());
                        unitDetailsIntent.putExtra("unitType", mUnitCaraouselVO.getTotal_types());
                        unitDetailsIntent.putExtra("priceSqft", mUnitCaraouselVO.getPrice_SqFt());
                        startActivityForResult(unitDetailsIntent, UNIT_DETAILS);
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_down_in);
                    } else {
                        showToast(getString(R.string.unit_reserved));
                    }
                } else if ((int) v.getTag(R.integer.event) == UnitCrouselFragment.FAVORITE_CLICK) {
                    if (app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        app.saveIntoPrefs(BMHConstants.VALUE, mUnitCaraouselVO.getUnit_id());
                        Intent i = new Intent(this, LoginActivity.class);
                        i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                        startActivityForResult(i, LOGIN_REQ_CODE);
                    } else {
                        favUnitId = mUnitCaraouselVO.getUnit_id();
                        if (ConnectivityReceiver.isConnected()) {
                            //TODO: network call
                            favRequest(favUnitId);
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(UnitMapActivity.this, UIEventType.RETRY_FEV,
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ConnectivityReceiver.isConnected()) {
                                                //TODO: network call
                                                favRequest(favUnitId);
                                                mNetworkErrorObject.getAlertDialog().dismiss();
                                                mNetworkErrorObject = null;
                                            } else {
                                                Utils.showToast(UnitMapActivity.this, getString(R.string.check_your_internet_connection));
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    // Add " boolean filter " on UnitMap page

    private void startUnitListTask(final String filter_type) {
        CustomAsyncTask loadingTask = new CustomAsyncTask(this, new AsyncListner() {
            @Override
            public void OnBackgroundTaskCompleted() {
                if (basevo == null) {
                    showToast(getString(R.string.unable_to_connect_server));
                } else {
                    if (basevo.isSuccess()) {
                        unitList = basevo.getArrUnits();
                        if (unitList != null && unitList.size() > 0) {
                            supportInvalidateOptionsMenu();
                            setPagerView(unitList);
                        }
                    } else {
                        //app.showSnackBar(UnitMapActivity.this, basevo.getMessage());
                        supportInvalidateOptionsMenu();
                        unitList = new ArrayList<>(1);
                        UnitCaraouselVO mData = new UnitCaraouselVO(null);
                        unitList.add(mData);
                        setPagerView(unitList);
                    }
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                PropertyModel model = new PropertyModel();
                try {
                    basevo = model.getUnitList(projId, unitIds, filter_type, app.getFromPrefs(BMHConstants.USERID_KEY));
                    try {
                        totalFloors = Integer.parseInt(basevo.getArrUnits().get(0).getTotal_floor());

                    } catch (Exception e) {
                    }
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

    private void startDownloadImageTask() {

        CustomAsyncTask loadingTask = new CustomAsyncTask(this, new AsyncListner() {
            Bitmap bitmap = null;

            @Override
            public void OnBackgroundTaskCompleted() {
                if (bitmap != null) {
                    try {
                        setProjectImageOnMap(bitmap);
                    } catch (Exception e) {
                        Log.e(TAG, "OnBackgroundTaskCompleted():Exception to download project plan image:" + e);
                    }
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                try {
                    bitmap = ContentLoader.getImage(UrlFactory.getShortImageByWidthUrl(600, projPlanImg));
                } catch (BMHException e) {
                    Log.e(TAG, "DoBackgroundTask():Exception to download project plan image:" + e);
                }
            }

            @Override
            public void OnPreExec() {
            }
        });
        loadingTask.setLogingMsg("Downloading project plan...");
        loadingTask.execute("");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setPadding(0, 0, 0, getResources().getInteger(R.integer.map_padding));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BMHConstants.INDIA_LAT, BMHConstants.INDIA_LNG), ZOMM_LEVEL));
        if (ConnectivityReceiver.isConnected()) {
            //TODO: network call
            getData();
        } else {
            mNetworkErrorObject = Utils.showNetworkErrorDialog(UnitMapActivity.this, UIEventType.RETRY_UNIT_DETAILS,
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ConnectivityReceiver.isConnected()) {
                                //TODO: network call
                                getData();
                                mNetworkErrorObject.getAlertDialog().dismiss();
                                mNetworkErrorObject = null;
                            } else {
                                Utils.showToast(UnitMapActivity.this, getString(R.string.check_your_internet_connection));
                            }
                        }
                    });
        }
        try {
            String[] seArr = sE.split(",");
            String[] nWArr = nW.split(",");
            double l1 = Double.parseDouble(seArr[0]);
            double l2 = Double.parseDouble(seArr[1]);
            double l3 = Double.parseDouble(nWArr[0]);
            double l4 = Double.parseDouble(nWArr[1]);
            double project_center_lat = 0.0;
            double project_center_long = 0.0;
            project_center_lat = (l1 + l3) / 2;
            project_center_long = (l2 + l4) / 2;
            System.out.println("cordinates " + project_center_lat + "," + project_center_long);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(project_center_lat, project_center_long), ZOMM_LEVEL));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e(TAG, "onMapReady():" + e);
        }
    }

    @Override
    protected void onResume() {
        BMHApplication.getInstance().setConnectivityListener(this);
        super.onResume();
    }
/*
	private void getUnitListData() {
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.HEATMAP);
		mBean.setRequestmethod(WEBAPI.POST);
		//String params = getRequestParams();
		//if(params != null || params.length() > 0) mBean.setJson(params);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.HEATMAP));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.initProgressDiasDlog(UnitMapActivity.this,mOnCancelListener);
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
		//    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
	}*/

    private void favRequest(String id) {
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
        mStringBuilder.append(ParamsConstants.UNIT_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(id);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.TYPE);
        mStringBuilder.append("=");
        mStringBuilder.append(ParamsConstants.BUY);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.USER_TYPE);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.SALES_PERSON);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.BUILDER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.CURRENT_BUILDER_ID);
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
            if (mAsyncThread != null) mAsyncThread.cancel(true);
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

                        break;
                    case FAV_PROJECT:
                        if (unitCrouselPagerAdapter != null && mBean.getRequestObj() != null && mBean.getRequestObj() instanceof String) {
                            String id = (String) mBean.getRequestObj();
                            unitCrouselPagerAdapter.toggleFav(id);
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_UNIT_DETAILS:
                    getData();
                    break;
                case RETRY_FEV:
                    favRequest(favUnitId);
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }

}
