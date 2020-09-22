package com.sp;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.sp.CustomAsyncTask.AsyncListner;
import com.VO.BoundariesVO;
import com.VO.LegendsVO;
import com.VO.LocationVO;
import com.VO.LocationsVO;
import com.adapters.LocationCrouselPagerAdapter;
import com.exception.BMHException;
import com.filter.LocalFilter;
import com.helper.BMHConstants;
import com.interfaces.CrouselOnItemClickListner;
import com.interfaces.PagerListner;
import com.model.LocationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationMapActivity extends BaseFragmentActivity implements CrouselOnItemClickListner, PagerListner {

    private GoogleMap map;
    private Activity ctx = LocationMapActivity.this, LocalFilter;
    private LocationsVO locationsVo;
    private String[] colorArr = new String[]{"EF676B", "EF676B", "EF676B", "FAB97F", "FAB97F", "FEE581", "FEE581",
            "90C97A", "90C97A", "64BD7D", "64BD7D"};
    // private ArrayList<Polygon> arrLocationPolygons ;
    private ArrayList<Polygon> arrSectorPolygons;
    // public CityVO cityvo;
    // public LocationVO locationvo;
    public ViewPager pager;
    private float currentZoom = 12.0f;
    private final int INFRA = 0, NEEDS = 1, LIFESTYLE = 2, PRICE = 3, RETURNS = 4;
    private LinearLayout llLineInfra, llLineNeeds, llLineLife, llLinePrice, llLineReturns;
    private TextView tvInfra, tvNeeds, tvLife, tvPrice, tvReturns, tv1, tv2, tv3, tv4, tv5;
    private ObjectAnimator translateAnimation, translateAnimation2, translateAnimation3, translateAnimation4,
            translateAnimation5;
    boolean isanimated = false;
    private int value = 100;
    private int duration = 800;
    private Polygon lastPolygon;
    private ImageView imgLegends;

    public HashMap<String, String> searchMap = new HashMap<String, String>();
    // private Filter filter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        // findViewById(R.id.llRootFooterFilers).setVisibility(View.GONE);

        // Add image loader
        View includeView = findViewById(R.id.inclide_view);
        ImageView imageView = (ImageView) includeView.findViewById(R.id.img);
        imageView.setBackgroundResource(R.drawable.progress_loader);
        AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        anim.start();

        Toolbar toolbar = setToolBar();
        // toolbar.setLogo(R.drawable.arrownav);
        toolbar.setTitle("Select Locality");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgLegends = (ImageView) findViewById(R.id.imgLegends);
        // findViewById(R.id.llRootFooterFilers).setVisibility(View.GONE);

        // final LinearLayout llLegends = (LinearLayout)
        // findViewById(R.id.llLegends);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);

        LinearLayout llInfra = (LinearLayout) findViewById(R.id.llInfra);
        LinearLayout llNeeds = (LinearLayout) findViewById(R.id.llNeeds);
        LinearLayout llLifestyle = (LinearLayout) findViewById(R.id.llLifestyle);
        LinearLayout llPrice = (LinearLayout) findViewById(R.id.llPrice);
        LinearLayout llReturns = (LinearLayout) findViewById(R.id.llReturns);

        tvInfra = (TextView) findViewById(R.id.tvInfra);
        tvNeeds = (TextView) findViewById(R.id.tvNeeds);
        tvLife = (TextView) findViewById(R.id.tvLife);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvReturns = (TextView) findViewById(R.id.tvReturns);

        llLineInfra = (LinearLayout) findViewById(R.id.llUnderlineInfra);
        llLineNeeds = (LinearLayout) findViewById(R.id.llUnderlineNeeds);
        llLineLife = (LinearLayout) findViewById(R.id.llUnderlineLifestyle);
        llLinePrice = (LinearLayout) findViewById(R.id.llUnderlinePrice);
        llLineReturns = (LinearLayout) findViewById(R.id.llUnderlineReturns);

        llInfra.setOnClickListener(filterListner);
        llNeeds.setOnClickListener(filterListner);
        llLifestyle.setOnClickListener(filterListner);
        llPrice.setOnClickListener(filterListner);
        llReturns.setOnClickListener(filterListner);

        // cityvo = (CityVO) getIntent().getSerializableExtra("cityvo");
        // locationvo =
        // (LocationVO)getIntent().getSerializableExtra("locationvo");

        if (getIntent().hasExtra("searchParams")) {
            searchMap = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
        }

        initMap("");
        //startLocationsTask("");

        if (map != null) {
            map.setOnMapClickListener(new OnMapClickListener() {
                @Override
                public void onMapClick(LatLng arg0) {

                    // for (int i = 0; i < arrLocationPolygons.size(); i++) {
                    // if(isPointInPolygon(arg0,
                    // arrLocationPolygons.get(i).getPoints())){
                    // Toast.makeText(LocationMapActivity.this,
                    // locationsVo.getArrLocation().get(i).getName(),
                    // 1000).show();
                    // launchProjectMapActivity(i);
                    // sendBackResult(locationsVo.getArrLocation().get(i));
                    // }
                    // }
                }
            });

            map.setOnCameraChangeListener(new OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition pos) {
                    if (pos.zoom != currentZoom) {
                        currentZoom = pos.zoom;
                    }
                }

            });
        }
        imgLegends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value = tv1.getHeight();
                if (!isanimated) {
                    isanimated = true;
                    translateAnimation = ObjectAnimator.ofFloat(tv1, View.TRANSLATION_Y, value);
                    translateAnimation.setInterpolator(new OvershootInterpolator());
                    translateAnimation.setDuration(duration);
                    translateAnimation.start();

                    translateAnimation2 = ObjectAnimator.ofFloat(tv2, View.TRANSLATION_Y, value * 2);
                    translateAnimation2.setDuration(duration);
                    translateAnimation2.setInterpolator(new OvershootInterpolator());
                    translateAnimation2.start();

                    translateAnimation3 = ObjectAnimator.ofFloat(tv3, View.TRANSLATION_Y, value * 3);
                    translateAnimation3.setDuration(duration);
                    translateAnimation3.setInterpolator(new OvershootInterpolator());
                    translateAnimation3.start();

                    translateAnimation4 = ObjectAnimator.ofFloat(tv4, View.TRANSLATION_Y, value * 4);
                    translateAnimation4.setDuration(duration);
                    translateAnimation4.setInterpolator(new OvershootInterpolator());
                    translateAnimation4.start();

                    translateAnimation5 = ObjectAnimator.ofFloat(tv5, View.TRANSLATION_Y, value * 5);
                    translateAnimation5.setDuration(duration);
                    translateAnimation5.setInterpolator(new OvershootInterpolator());
                    translateAnimation5.start();
                } else {
                    isanimated = false;

                    translateAnimation.reverse();
                    translateAnimation2.reverse();
                    translateAnimation3.reverse();
                    translateAnimation4.reverse();
                    translateAnimation5.reverse();

                }
            }
        });

        imgLegends.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                // getActionBar().hide();
                return false;
            }
        });
    }

    OnClickListener filterListner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Resources r = getResources();
            switch (v.getId()) {
                case R.id.llInfra:
                    llLineInfra.setBackgroundColor(r.getColor(R.color.selected_underline));
                    llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));

                    tvInfra.setTextColor(r.getColor(R.color.selected_tv));
                    tvLife.setTextColor(r.getColor(R.color.unselected_tv));
                    tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
                    tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
                    tvReturns.setTextColor(r.getColor(R.color.unselected_tv));

                    tv1.setText("0 - 2");
                    tv2.setText("2 - 4");
                    tv3.setText("4 - 6");
                    tv4.setText("6 - 8");
                    tv5.setText("8 - 10");
                    changePolygonColor(INFRA);

                    break;
                case R.id.llNeeds:
                    llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineNeeds.setBackgroundColor(r.getColor(R.color.selected_underline));
                    llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));

                    tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
                    tvLife.setTextColor(r.getColor(R.color.unselected_tv));
                    tvNeeds.setTextColor(r.getColor(R.color.selected_tv));
                    tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
                    tvReturns.setTextColor(r.getColor(R.color.unselected_tv));

                    tv1.setText("0 - 2");
                    tv2.setText("2 - 4");
                    tv3.setText("4 - 6");
                    tv4.setText("6 - 8");
                    tv5.setText("8 - 10");

                    changePolygonColor(NEEDS);
                    break;
                case R.id.llLifestyle:
                    llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineLife.setBackgroundColor(r.getColor(R.color.selected_underline));
                    llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));

                    tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
                    tvLife.setTextColor(r.getColor(R.color.selected_tv));
                    tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
                    tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
                    tvReturns.setTextColor(r.getColor(R.color.unselected_tv));

                    tv1.setText("0 - 2");
                    tv2.setText("2 - 4");
                    tv3.setText("4 - 6");
                    tv4.setText("6 - 8");
                    tv5.setText("8 - 10");

                    changePolygonColor(LIFESTYLE);
                    break;
                case R.id.llPrice:

                    llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLinePrice.setBackgroundColor(r.getColor(R.color.selected_underline));
                    llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));

                    tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
                    tvLife.setTextColor(r.getColor(R.color.unselected_tv));
                    tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
                    tvPrice.setTextColor(r.getColor(R.color.selected_tv));
                    tvReturns.setTextColor(r.getColor(R.color.unselected_tv));

                    ArrayList<LegendsVO> aar = locationsVo.getArrPriceConditions();
                    if (aar != null) {
                        tv1.setText(aar.get(0).getMin() + " - " + aar.get(0).getMax() + " Psf");
                        tv2.setText(aar.get(1).getMin() + " - " + aar.get(1).getMax() + " Psf");
                        tv3.setText(aar.get(2).getMin() + " - " + aar.get(2).getMax() + " Psf");
                        tv4.setText(aar.get(3).getMin() + " - " + aar.get(3).getMax() + " Psf");
                        tv5.setText(aar.get(4).getMin() + " - " + aar.get(4).getMax() + " Psf");
                    }

                    changePolygonColor(PRICE);

                    break;
                case R.id.llReturns:

                    llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
                    llLineReturns.setBackgroundColor(r.getColor(R.color.selected_underline));

                    tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
                    tvLife.setTextColor(r.getColor(R.color.unselected_tv));
                    tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
                    tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
                    tvReturns.setTextColor(r.getColor(R.color.selected_tv));

                    ArrayList<LegendsVO> aarR = locationsVo.getArrReturnsConditions();
                    if (aarR != null) {
                        tv1.setText(aarR.get(0).getMin() + " - " + aarR.get(0).getMax());
                        tv2.setText(aarR.get(1).getMin() + " - " + aarR.get(1).getMax());
                        tv3.setText(aarR.get(2).getMin() + " - " + aarR.get(2).getMax());
                        tv4.setText(aarR.get(3).getMin() + " - " + aarR.get(3).getMax());
                        tv5.setText(aarR.get(4).getMin() + " - " + aarR.get(4).getMax());
                    }

                    changePolygonColor(RETURNS);

                    break;
                default:
                    break;
            }
        }

    };

    private void changePolygonColor(int filter) {
        if (locationsVo != null) {
            if (arrSectorPolygons != null) {
                for (int i = 0; i < arrSectorPolygons.size(); i++) {
                    arrSectorPolygons.get(i).remove();
                }
            }
            ArrayList<LocationVO> arrLocations = locationsVo.getArrLocation();
            for (int i = 0; i < arrLocations.size(); i++) {
                LocationVO itemLocation = arrLocations.get(i);
                if (itemLocation.getArrBoundries() != null) {
                    // DrawLocationPolygon(itemLocation.getArrBoundries(), i);
                    for (int j = 0; j < itemLocation.getArrSectors().size(); j++) {
                        int value = 0;
                        switch (filter) {
                            case INFRA:
                                value = itemLocation.getArrSectors().get(j).getInfra();
                                break;
                            case NEEDS:
                                value = itemLocation.getArrSectors().get(j).getNeeds();
                                break;
                            case LIFESTYLE:
                                value = itemLocation.getArrSectors().get(j).getLifestyle();
                                break;
                            case PRICE:

                                value = itemLocation.getArrSectors().get(j).getPrice();
                                ArrayList<LegendsVO> arP = locationsVo.getArrPriceConditions();
                                for (int k = 0; k < arP.size(); k++) {
                                    int min = arP.get(k).getMin();
                                    int max = arP.get(k).getMax();
                                    if (value >= min && value < max) {
                                        value = k;
                                        break;
                                    }

                                    if (k == arP.size() - 1 && value >= min && value <= max) {
                                        value = k;
                                    }
                                }
                                break;
                            case RETURNS:
                                value = itemLocation.getArrSectors().get(j).getReturns();
                                ArrayList<LegendsVO> arrR = locationsVo.getArrReturnsConditions();
                                for (int l = 0; l < arrR.size(); l++) {
                                    int min = arrR.get(l).getMin();
                                    int max = arrR.get(l).getMax();
                                    if (value >= min && value < max) {
                                        value = l;
                                        break;
                                    }

                                    if (l == arrR.size() - 1 && value >= min && value <= max) {
                                        value = l;
                                    }
                                }
                                break;
                            default:
                                break;
                        }

                        DrawSectorsPolygon(itemLocation.getArrSectors().get(j).getArrSectorCords(), value);
                    }

                }
            }
        }
    }

    private boolean isPointInPolygon(LatLng tap, List<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (LineIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }
        return (intersectCount % 2) == 1; // odd = inside, even = outside;
    }

    private boolean LineIntersect(LatLng tap, LatLng vertA, LatLng vertB) {
        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;
        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false;
        }
        double m = (aY - bY) / (aX - bX);
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m;
        return x > pX;
    }

    private void DrawLocationPolygon(ArrayList<BoundariesVO> arr, int pos) {
        // if(arrLocationPolygons == null){
        // arrLocationPolygons = new ArrayList<Polygon>();
        // }
        if (lastPolygon != null) {
            lastPolygon.remove();
        }

        PolygonOptions polyOpt = new PolygonOptions();
        for (int i = 0; i < arr.size(); i++) {
            polyOpt.strokeColor(Color.parseColor("#000000"));
            polyOpt.strokeWidth(3);
            polyOpt.fillColor(Color.parseColor("#66000000"));
            Double d1 = Double.parseDouble(arr.get(i).getLat());
            Double d2 = Double.parseDouble(arr.get(i).getLon());
            polyOpt.add(new LatLng(d1, d2));

        }
        // arrLocationPolygons.add(map.addPolygon(polyOpt));

        try {
            lastPolygon = map.addPolygon(polyOpt);

        } catch (Exception e) {

            // TODO: handle exception
        }

        if (arr.size() > 0)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(arr.get(0).getLat()), Double.parseDouble(arr.get(0).getLon())),
                    12.0f));

    }

    private void DrawSectorsPolygon(ArrayList<BoundariesVO> arr, int pos) {
        if (arrSectorPolygons == null)
            arrSectorPolygons = new ArrayList<Polygon>();

        PolygonOptions polyOpt = new PolygonOptions();
        polyOpt.strokeWidth(2);

        for (int i = 0; i < arr.size(); i++) {
            polyOpt.strokeColor(Color.parseColor("#" + colorArr[pos]));
            polyOpt.fillColor(Color.parseColor("#cc" + colorArr[pos]));
            Double d1 = Double.parseDouble(arr.get(i).getLat());
            Double d2 = Double.parseDouble(arr.get(i).getLon());
            polyOpt.add(new LatLng(d1, d2));

        }
        try {
            arrSectorPolygons.add(map.addPolygon(polyOpt));

        } catch (Exception e) {
            // TODO: handle exception
        }

        // if(arr.size()>0)
        // map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
        // LatLng(Double.parseDouble(arr.get(0).getLat()),
        // Double.parseDouble(arr.get(0).getLon())), 14.0f));

    }

    private void initMap(String filter_type) {
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
                        if (ActivityCompat.checkSelfPermission(LocationMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(LocationMapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LocationMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BMHConstants.ACCESS_FINE_LOCATION);
                            return;
                        }
                    }
                });
                setMapAndZoom(filter_type);
            }
        } else {
            setMapAndZoom(filter_type);
        }
    }

    private void setMapAndZoom(String filter_type) {
        if (map != null) {
            map.clear();
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomControlsEnabled(false);
            map.setPadding(0, 0, 0, getResources().getInteger(R.integer.map_padding));
            setCustomZomm();

            // Set condition "True & False" use Sorting of Locality Map

            startLocationsTask(filter_type);

            // if (true) {
            // startActivity(new Intent(this, LocationMapActivity.class));
            // } else {
            // startActivity(new Intent(this, LocalFilter.class));
            // }

        } else {
            System.out.println("hh map is null");
        }
    }

    private void setCustomZomm() {
        ImageView plus = (ImageView) findViewById(R.id.imgPlus);
        ImageView minus = (ImageView) findViewById(R.id.imgMinus);
        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentZoom < map.getMaxZoomLevel()) {
                    currentZoom = currentZoom + 1;
                    map.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));
                }
            }
        });
        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentZoom > map.getMinZoomLevel()) {
                    currentZoom = currentZoom - 1;
                    map.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));
                }
            }
        });

        map.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition pos) {
                if (pos.zoom != currentZoom) {
                    currentZoom = pos.zoom;
                }
            }
        });
    }

    private void setPagerView(ArrayList<LocationVO> locations) {

        pager = (ViewPager) findViewById(R.id.myviewpager);
        LocationCrouselPagerAdapter adapter = new LocationCrouselPagerAdapter(LocationMapActivity.this,
                this.getSupportFragmentManager(), locations);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                onPageChnaged(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(getResources().getInteger(R.integer.offScreenLimit));
        pager.setPageMargin(getResources().getInteger(R.integer.pager_padding));
        // pager.setPageMargin(100);

        if (locations.get(0) != null && locations.get(0).getArrBoundries() != null
                && !locations.get(0).getArrBoundries().isEmpty() && locations.get(0).getArrBoundries().get(0) != null) {
            map.animateCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(
                                    new LatLng(Double.parseDouble(locations.get(0).getArrBoundries().get(0).getLat()),
                                            Double.parseDouble(locations.get(0).getArrBoundries().get(0).getLon())),
                                    12.0f));
        }

        // for(int i=0;i<adapter.getCount();i++){
        // adapter,
        // }

        // -----------------------------Location Crousel visible and hide

        // String[] Projects =
        // sectorsList.get(position).getTotal_projects().split(" ");
        //
        // System.out.println("krishna
        // "+sectorsList.get(position).getTotal_projects());
        //
        // try{
        // Projects[0] = Projects[0].replace(" "," ");
        // int propertyCount = Integer.parseInt(Projects[0]);
        // if(propertyCount <= 0){
        // // hide box
        // LinearLayout llUnit = (LinearLayout)convertView.
        // findViewById(R.id.all_details);
        //// llUnit.setVisibility(1);
        // llUnit.setVisibility(View.GONE);
        // System.out.println("Hidding");
        // }
        // }catch(Exception e){
        // // hide box
        // LinearLayout llUnit = (LinearLayout)convertView.
        // findViewById(R.id.all_details);
        // llUnit.setVisibility(View.GONE);
        // System.out.println(e.getMessage());
        // }

        // -------------------------------------------------------------

    }

    // Add "Boolean filter" on location Task start

    private void startLocationsTask(final String filter_type) {
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {

            @Override
            public void OnBackgroundTaskCompleted() {
                if (locationsVo != null) {
                    Intent i = new Intent(ctx, LocationListActivity.class);
                    i.putExtra("locationvo", locationsVo);
                    i.putParcelableArrayListExtra("locations", locationsVo.getArrLocation());
                    i.putExtra("searchParams", searchMap);
                    i.putExtra("isFromSearch", true);
                    i.putExtra("city_id", getIntent().getStringExtra("city_id"));
                    i.putExtra("id", getIntent().getStringExtra("id"));
                    i.putExtra("type", getIntent().getStringExtra("type"));
                    i.putExtra("p_type", getIntent().getStringExtra("p_type"));
                    i.putExtra("isFromSearch", true);
                    startActivityForResult(i, BMHConstants.PICK_LOCATION_LIST);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_down_in);
                    finish();
                } else {
                    //TODO:
                    app.showToast(getString(R.string.data_not_found_for_selected_city));
                    finish();
                }

                // if (locationsVo == null) {
                // // app.showAppMessage(ctx,
                // // "Something went wrong, Please Try again.");
                // app.showSnackBarError(ctx, new SnackBarCallback() {
                // @Override
                // public void onActionButtonClick(Parcelable token) {
                //
                // // Add the "filter " start
                // startLocationsTask(filter_type);
                // }
                // });
                // } else {
                // if (locationsVo.isSuccess() &&
                // locationsVo.getArrLocation().size() > 0) {
                // ArrayList<LocationVO> arrLocations = locationsVo
                // .getArrLocation();
                // setPagerView(arrLocations);
                //
                // for (int i = 0; i < arrLocations.size(); i++) {
                //
                // // for locations
                // LocationVO itemLocation = arrLocations .get(i);
                // if (itemLocation.getArrBoundries() != null) {
                // try {
                // for (int j = 0; j < itemLocation .getArrSectors().size();
                // j++) {
                // // for sectors of a specific location
                // int infra = itemLocation.getArrSectors().get(j).getInfra();
                // DrawSectorsPolygon(itemLocation.getArrSectors().get(j).getArrSectorCords(),
                // infra);
                // }
                // } catch (Exception e) {
                // // TODO: handle exception
                // }
                // }
                // }
                // DrawLocationPolygon(arrLocations.get(0).getArrBoundries(),
                // 0);
                // new Handler().postDelayed(new Runnable() {
                // @Override
                // public void run() {
                // imgLegends.performClick();
                // }
                // }, 1000);
                // } else {
                // // setPagerView(new ArrayList<LocationVO>());
                // app.showSnackBar(ctx, locationsVo.getMessage(),
                // SnackBar.LONG_SNACK);
                // }
                // }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                LocationModel model = new LocationModel();
                try {
                    String pType = getIntent().getStringExtra("p_type");
                    String id = getIntent().getStringExtra("id");
                    String type = getIntent().getStringExtra("type");
                    String city_id = getIntent().getStringExtra("city_id");


                    locationsVo = model.getLocations(app.getFromPrefs(BMHConstants.CITYID), app.getFromPrefs(BMHConstants.USERID_KEY),
                            // add Key "filter" on Model view
                            pType, "Buy", filter_type, id, type);

//					locationsVo = model.getLocations(locationvo.getId(), app.getFromPrefs(BMHConstants.USERID_KEY), 
//						searchMap.get("p_type"), searchMap.get("type"), filter_type);


                } catch (BMHException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {

            }
        });
        loadingTask.dontShowProgressDialog();
        loadingTask.execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.location_map, menu);
        // getMenuInflater().inflate(R.menu.location_filter, menu);
        getMenuInflater().inflate(R.menu.tower_map, menu);
        getMenuInflater().inflate(R.menu.unit_image_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_list:
                Intent i = new Intent(ctx, LocationListActivity.class);
                i.putParcelableArrayListExtra("locations", locationsVo.getArrLocation());
                i.putExtra("searchParams", searchMap);
                i.putExtra("city_id", getIntent().getStringExtra("city_id"));
                i.putExtra("id", getIntent().getStringExtra("id"));
                i.putExtra("type", getIntent().getStringExtra("type"));
                i.putExtra("p_type", getIntent().getStringExtra("p_type"));
                i.putExtra("isFromSearch", true);
                startActivityForResult(i, BMHConstants.PICK_LOCATION_LIST);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_in);
                break;
            case android.R.id.home:
                finish();
                break;
            // case R.id.action_list:
            // Intent i = new Intent(ctx, LocationListActivity.class);
            // i.putParcelableArrayListExtra("locations",locationsVo.getArrLocation());
            // i.putExtra("searchParams", searchMap);
            // startActivityForResult(i, BMHConstants.PICK_LOCATION_LIST);
            // break;
            default:
                Intent intent = new Intent(ctx, LocalFilter.class);
                if (locationsVo != null)
                    intent.putParcelableArrayListExtra("locations", locationsVo.getArrLocation());
                intent.putExtra("searchParams", searchMap);

                startActivityForResult(intent, BMHConstants.PICK_LOCATION_FILTER);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
        }
        // if (id == R.id.action_list) {
        // Intent i = new Intent(ctx, LocationListActivity.class);
        // i.putParcelableArrayListExtra("locations",locationsVo.getArrLocation());
        // i.putExtra("searchParams", searchMap);
        // startActivityForResult(i, BMHConstants.PICK_LOCATION_LIST);
        //
        // } else {
        //
        // Intent i = new Intent(ctx, LocalFilter.class);
        // //
        // i.putParcelableArrayListExtra("locations",locationsVo.getArrLocation());
        // i.putExtra("searchParams", searchMap);
        // startActivityForResult(i, BMHConstants.PICK_LOCATION_LIST);
        // overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        // return true;
        //
        // }
        return super.onOptionsItemSelected(item);
    }

    // ==================================

    // public boolean onOptionItemSelected(MenuItem item) {
    // int id = item.getItemId();
    // if (id == R.id.action_filter) {
    // Intent i = new Intent(ctx, LocalFilter.class);
    // //
    // i.putParcelableArrayListExtra("locations",locationsVo.getArrLocation());
    // i.putExtra("searchParams", searchMap);
    // startActivityForResult(i,BMHConstants.PICK_LOCATION);
    // return true;
    // }
    // // return super.onOptionItemSelected(item);
    // }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("hh location map acti result");
        if (requestCode == BMHConstants.PICK_LOCATION_FILTER) {
            if (resultCode == RESULT_OK) {
                String filer_type = data.getStringExtra("filter_type");
                if (filer_type != null) {
                    initMap(filer_type);
                    // sendBackResult(mUnitDetails);
                    // Intent i = new Intent(ctx, ProjectMapActivity.class);
                    // i.putExtra("locationvo", locationsVo);
                    // searchMap.put("location",locationsVo.getArrLocation().get(pos).getId());
                    // i.putExtra("searchParams", searchMap);
                    // i.putExtra("pos", pos);
                    // startActivity(i);
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onclickCrouselItem(View v) {
        int pos = (Integer) v.getTag();
        launchProjectMapActivity(pos);
    }

    // private void sendBackResult(LocationVO mUnitDetails){
    // Intent returnIntent = new Intent();
    // returnIntent.putExtra("result",mUnitDetails);
    // setResult(RESULT_OK,returnIntent);
    // finish();
    // }

    private void launchProjectMapActivity(int pos) {
        Intent i = new Intent(ctx, ProjectMapActivity.class);
        i.putExtra("locationvo", locationsVo);
        System.out.println("hh sending location id = " + locationsVo.getArrLocation().get(pos).getId());
        searchMap.put("location", locationsVo.getArrLocation().get(pos).getId());
        i.putExtra("searchParams", searchMap);
        i.putExtra("pos", pos);
        startActivity(i);

    }

    @Override
    public void onPageChnaged(int position) {
        if (locationsVo != null && position < locationsVo.getArrLocation().size()) {

            DrawLocationPolygon(locationsVo.getArrLocation().get(position).getArrBoundries(), position);

            try {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(getCentroid(lastPolygon.getPoints()), currentZoom));

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    private LatLng getCentroid(List<LatLng> points) {
        double[] centroid = {0.0, 0.0};

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

    // =========================================

}
