package com.sp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.AppEnums.APIType;
import com.AppEnums.Neighbourhoods;
import com.VO.PlacesBaseVO;
import com.VO.PlacesLocationVO;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FullScreenMapActivity extends BaseFragmentActivity implements OnMapReadyCallback {

    private static final String TAG = FullScreenMapActivity.class.getSimpleName();
    private final int DEF_REDIUS_CONS = 4000;
    private final int COTIANT = 9;
    private final int DEF_ZOOM_CONS = 6;
    private Activity ctx = this;
    private GoogleMap googleMap;
    private PlacesBaseVO basevo;
    ;
    private int selectedRadius = 5000;
    private float zoomLevel = 14;
    private Polyline polyline = null;
    Button btn_landmarks, btn_airports, btn_railways, btn_busstands, btn_texi_service, btn_schools, btn_hospitals, btn_shopping_malls, btn_dept_stores,
            btn_pharmacies, btn_atms, btn_restaurants, btn_banks, btn_parks, btn_hotels, btn_movie_theaters, btn_night_clubs;

    private Neighbourhoods CURRENT_NEIGHBOURHOOD = Neighbourhoods.LANDMARKS;
    private AsyncThread mAsyncThread = null;
    private LatLng projectLatLng = new LatLng(0, 0);
    private HashMap<String, String> intentParams = null;
    private Marker PROJECTLOCATION = null;
    private Marker CURRENT_CLICKED_MARKER = null;
    private ArrayList<Marker> markers = new ArrayList<>();
    private Toolbar toolbar = null;
    private HorizontalScrollView hs_footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_map);
        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            if (mIntentDataObject.getData() != null) {
                intentParams = mIntentDataObject.getData();
                if (intentParams.get(ParamsConstants.LAT) != null && intentParams.get(ParamsConstants.LNG) != null) {
                    projectLatLng = new LatLng(Utils.toDouble(intentParams.get(ParamsConstants.LAT)), Utils.toDouble(intentParams.get(ParamsConstants.LNG)));
                }

            }
        }
        initViews();
        setListeners();
    }// End of onCreate()


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_screen_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String setActionBarTitle() {
        return "Near By";
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(markerClickListener);
        String projname = "";
        String projAddress = "";
        if (intentParams != null) {
            if (intentParams.get(ParamsConstants.TITLE) != null) {
                projname = intentParams.get(ParamsConstants.TITLE);
                toolbar.setTitle(Html.fromHtml(projname));
            }
            if (intentParams.get(ParamsConstants.LOCATION) != null) {
                projAddress = intentParams.get(ParamsConstants.LOCATION);
            }
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(projectLatLng, zoomLevel));
        PROJECTLOCATION = googleMap.addMarker(new MarkerOptions().position(projectLatLng).title(projname).snippet(projAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.landmarks_marker)));
        btn_landmarks.performClick();
    }


    private void setListeners() {
        btn_landmarks.setOnClickListener(mOnClickListener);
        btn_airports.setOnClickListener(mOnClickListener);
        btn_railways.setOnClickListener(mOnClickListener);
        btn_busstands.setOnClickListener(mOnClickListener);
        btn_texi_service.setOnClickListener(mOnClickListener);
        btn_schools.setOnClickListener(mOnClickListener);
        btn_hospitals.setOnClickListener(mOnClickListener);
        btn_shopping_malls.setOnClickListener(mOnClickListener);
        btn_dept_stores.setOnClickListener(mOnClickListener);
        btn_pharmacies.setOnClickListener(mOnClickListener);
        btn_parks.setOnClickListener(mOnClickListener);
        btn_banks.setOnClickListener(mOnClickListener);
        btn_atms.setOnClickListener(mOnClickListener);
        btn_restaurants.setOnClickListener(mOnClickListener);
        btn_hotels.setOnClickListener(mOnClickListener);
        btn_movie_theaters.setOnClickListener(mOnClickListener);
        btn_night_clubs.setOnClickListener(mOnClickListener);
    }

    private void initViews() {
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapId);
        fm.getMapAsync(this);

        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        hs_footer = (HorizontalScrollView) findViewById(R.id.hs_footer);
        btn_landmarks = (Button) findViewById(R.id.btn_landmarks);
        btn_airports = (Button) findViewById(R.id.btn_airports);
        btn_railways = (Button) findViewById(R.id.btn_railways);
        btn_busstands = (Button) findViewById(R.id.btn_busstands);
        btn_texi_service = (Button) findViewById(R.id.btn_texi_service);
        btn_schools = (Button) findViewById(R.id.btn_schools);
        btn_hospitals = (Button) findViewById(R.id.btn_hospitals);
        btn_shopping_malls = (Button) findViewById(R.id.btn_shopping_malls);
        btn_dept_stores = (Button) findViewById(R.id.btn_dept_stores);
        btn_pharmacies = (Button) findViewById(R.id.btn_pharmacies);
        btn_parks = (Button) findViewById(R.id.btn_parks);
        btn_banks = (Button) findViewById(R.id.btn_banks);
        btn_atms = (Button) findViewById(R.id.btn_atms);
        btn_restaurants = (Button) findViewById(R.id.btn_restaurants);
        btn_hotels = (Button) findViewById(R.id.btn_hotels);
        btn_movie_theaters = (Button) findViewById(R.id.btn_movie_theaters);
        btn_night_clubs = (Button) findViewById(R.id.btn_night_clubs);
    }

    private void UIState(View selectedView) {
        btn_landmarks.setSelected(false);
        btn_airports.setSelected(false);
        btn_railways.setSelected(false);
        btn_busstands.setSelected(false);
        btn_texi_service.setSelected(false);
        btn_schools.setSelected(false);
        btn_hospitals.setSelected(false);
        btn_shopping_malls.setSelected(false);
        btn_dept_stores.setSelected(false);
        btn_pharmacies.setSelected(false);
        btn_parks.setSelected(false);
        btn_banks.setSelected(false);
        btn_atms.setSelected(false);
        btn_restaurants.setSelected(false);
        btn_hotels.setSelected(false);
        btn_movie_theaters.setSelected(false);
        btn_night_clubs.setSelected(false);
        if (selectedView != null) selectedView.setSelected(true);
    }

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker != null) {
                if (PROJECTLOCATION != null && marker.equals(PROJECTLOCATION)) {
                    PROJECTLOCATION.showInfoWindow();

                } else {
                    CURRENT_CLICKED_MARKER = marker;
                    getDirection(cereateDirectionUrl(projectLatLng, marker.getPosition()));
                }
            }
            return true;
        }
    };
    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_landmarks:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.LANDMARKS;
                    getNeighbourhood(Neighbourhoods.LANDMARKS);
                    break;
                case R.id.btn_airports:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.AIRPORTS;
                    getNeighbourhood(Neighbourhoods.AIRPORTS);
                    break;
                case R.id.btn_railways:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.RAILWAYS;
                    getNeighbourhood(Neighbourhoods.RAILWAYS);
                    break;
                case R.id.btn_busstands:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.BUS_STANDS;
                    getNeighbourhood(Neighbourhoods.BUS_STANDS);
                    break;
                case R.id.btn_texi_service:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.TAXI_SERVICES;
                    getNeighbourhood(Neighbourhoods.TAXI_SERVICES);
                    break;
                case R.id.btn_schools:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.SCHOOLS;
                    getNeighbourhood(Neighbourhoods.SCHOOLS);
                    break;
                case R.id.btn_hospitals:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.HOSPITALS;
                    getNeighbourhood(Neighbourhoods.HOSPITALS);
                    break;
                case R.id.btn_shopping_malls:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.SHOPPING_MALLS;
                    getNeighbourhood(Neighbourhoods.SHOPPING_MALLS);
                    break;
                case R.id.btn_dept_stores:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.DEPARTMENTAL_STORS;
                    getNeighbourhood(Neighbourhoods.DEPARTMENTAL_STORS);
                    break;
                case R.id.btn_pharmacies:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.PHARMACIES;
                    getNeighbourhood(Neighbourhoods.PHARMACIES);
                    break;
                case R.id.btn_parks:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.PARKS;
                    getNeighbourhood(Neighbourhoods.PARKS);
                    break;
                case R.id.btn_banks:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.BANKS;
                    getNeighbourhood(Neighbourhoods.BANKS);
                    break;
                case R.id.btn_atms:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.ATMS;
                    getNeighbourhood(Neighbourhoods.ATMS);
                    break;
                case R.id.btn_restaurants:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.RESTAURANTS;
                    getNeighbourhood(Neighbourhoods.RESTAURANTS);
                    break;
                case R.id.btn_hotels:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.HOTELS;
                    getNeighbourhood(Neighbourhoods.HOTELS);
                    break;
                case R.id.btn_movie_theaters:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.MOVIE_THEATERS;
                    getNeighbourhood(Neighbourhoods.MOVIE_THEATERS);
                    break;
                case R.id.btn_night_clubs:
                    UIState(v);
                    CURRENT_NEIGHBOURHOOD = Neighbourhoods.NIGHT_CLUBS;
                    getNeighbourhood(Neighbourhoods.NIGHT_CLUBS);
                    break;

            }
            scrollToCenter(v);
        }
    };

    private void scrollToCenter(View child) {
        int scrollX = (child.getLeft() - (app.getWidth(this) / 2)) + (child.getWidth() / 2);
        hs_footer.smoothScrollTo(scrollX, 0);
    }

    public String cereateDirectionUrl(LatLng src, LatLng dest) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(src.latitude));
        urlString.append(",");
        urlString.append(Double.toString(src.longitude));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(dest.latitude));
        urlString.append(",");
        urlString.append(Double.toString(dest.longitude));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=" + getResources().getString(R.string.map_api_key));
        return urlString.toString();
    }

    public void drawPath(JSONObject json) {
        try {
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject legObj = legs.getJSONObject(0);
            String endAddress = legObj.getString("end_address");
            String startAddress = legObj.getString("start_address");
            JSONObject distObj = legObj.getJSONObject("distance");
            String distText = distObj.getString("text");
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = Utils.decodePoly(encodedString);
            if (polyline != null) polyline.remove();
            polyline = googleMap.addPolyline(new PolylineOptions().addAll(list).width(10).color(Color.parseColor("#05b1fb")).geodesic(true));
            if (CURRENT_CLICKED_MARKER != null) {
                //CURRENT_CLICKED_MARKER.setTitle(endAddress);
                CURRENT_CLICKED_MARKER.setSnippet("Distance: " + distText);
                CURRENT_CLICKED_MARKER.showInfoWindow();
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());

        }
    }


    private void setPropertiesOnMap(ArrayList<PlacesLocationVO> arr) {
        if (googleMap == null || arr == null) return;
        clearMapExceptProjectLocation();
        for (int i = 0; i < arr.size(); i++) {
            if (CURRENT_NEIGHBOURHOOD == Neighbourhoods.AIRPORTS) {
                if (arr.get(i).getName().contains("International Airport")) {
                    LatLng latlong = new LatLng(arr.get(i).getLat(), arr.get(i).getLon());
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latlong).title(arr.get(i).getName()).snippet(arr.get(i).getVicinity()).icon(BitmapDescriptorFactory.fromResource(getMarker())));
                    if (markers != null) markers.add(marker);
                }
            } else {
                LatLng latlong = new LatLng(arr.get(i).getLat(), arr.get(i).getLon());
                Marker marker = googleMap.addMarker(new MarkerOptions().position(latlong).title(arr.get(i).getName()).snippet(arr.get(i).getVicinity()).icon(BitmapDescriptorFactory.fromResource(getMarker())));
                if (markers != null) markers.add(marker);
            }
        }
        if (markers != null && markers.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            if (PROJECTLOCATION != null) builder.include(PROJECTLOCATION.getPosition());
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            googleMap.animateCamera(cu);
        }
    }

    private void clearMapExceptProjectLocation() {
        if (googleMap != null && markers != null && PROJECTLOCATION != null) {
            if (polyline != null) polyline.remove();
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
    }

    private int getMarker() {
        switch (CURRENT_NEIGHBOURHOOD) {
            case LANDMARKS:
                return R.drawable.landmarks;
            case AIRPORTS:
                return R.drawable.airport_marker;
            case RAILWAYS:
                return R.drawable.railways_marker;
            case BUS_STANDS:
                return R.drawable.bus_stand_marker;
            case TAXI_SERVICES:
                return R.drawable.taxi_services_marker;
            case SCHOOLS:
                return R.drawable.school_marker;
            case HOSPITALS:
                return R.drawable.hospital_marker;
            case SHOPPING_MALLS:
                return R.drawable.shopping_malls_marker;
            case DEPARTMENTAL_STORS:
                return R.drawable.departmental_stores_marker;
            case PHARMACIES:
                return R.drawable.pharmacies_marker;
            case PARKS:
                return R.drawable.park_marker;
            case BANKS:
                return R.drawable.bank_marker;
            case ATMS:
                return R.drawable.atm_marker;
            case RESTAURANTS:
                return R.drawable.restaurants_marker;
            case HOTELS:
                return R.drawable.hotels_marker;
            case MOVIE_THEATERS:
                return R.drawable.movie_theaters_marker;
            case NIGHT_CLUBS:
                return R.drawable.night_clubs_marker;
        }
        return R.drawable.landmarks;
    }

    private float getZoomViaRaius(float radius) {
        radius = radius / DEF_REDIUS_CONS;
        float zoom = COTIANT - (radius);
        zoom = zoom + DEF_ZOOM_CONS;
        return zoom;

    }


    private void getNeighbourhood(Neighbourhoods neighbourhood) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.NEIGHBOURHOOD);
        mBean.setRequestmethod(WEBAPI.POST);
        if (neighbourhood == Neighbourhoods.AIRPORTS) selectedRadius = 50000;
        else selectedRadius = 5000;
        StringBuffer params = new StringBuffer(UrlFactory.getPlacesUrl());
        params.append("?location=" + projectLatLng.latitude + "," + projectLatLng.longitude);
        params.append("&type=" + neighbourhood.value);
        params.append("&radius=" + selectedRadius);
        params.append("&key=" + getString(R.string.map_api_key));
        params.append("&sensor=false");
        mBean.setUrl(params.toString());
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(FullScreenMapActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void getDirection(String directionUrl) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.DIRECTION);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(directionUrl);
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(FullScreenMapActivity.this, mOnCancelListener);
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
                    case NEIGHBOURHOOD:
                        if (mBean.getJson() != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(mBean.getJson());
                                if (jsonObj != null) {
                                    basevo = new PlacesBaseVO(jsonObj);
                                    if (basevo == null && app != null) {
                                    } else {
                                        if (basevo.getStatus().equalsIgnoreCase("OK")) {
                                            ArrayList<PlacesLocationVO> arr = basevo.getArrLocations();
                                            setPropertiesOnMap(arr);
                                        } else if (basevo.getStatus().equalsIgnoreCase("ZERO_RESULTS")) {
                                            clearMapExceptProjectLocation();
                                            showToast("No result found.");
                                        } else {
                                            showToast("Something went wrong. Please try later");
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showToast("Something went wrong. Please try later");
                            }
                        } else {
                            showToast("Something went wrong. Please try later");
                        }
                        break;
                    case DIRECTION:
                        if (mBean.getJson() != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(mBean.getJson());
                                if (jsonObj != null) {
                                    drawPath(jsonObj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showToast("Something went wrong. Please try later");
                            }
                        } else {
                            showToast("Something went wrong. Please try later");
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });
}
