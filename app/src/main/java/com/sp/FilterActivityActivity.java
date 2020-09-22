package com.sp;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.AppEnums.ProjectType;
import com.AppEnums.PropertyType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.filter.Builder;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.model.CityDataModel;
import com.model.CityRespData;
import com.model.MicroMarketData;
import com.model.MicroMarketRespData;
import com.model.SectorData;
import com.model.SectorRespData;
import com.utils.Utils;
import com.views.RangeSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.helper.BMHConstants.COMMERCIAL_PROPERTY;
import static com.helper.BMHConstants.RESIDENTIAL_PROPERTY;

public class FilterActivityActivity  extends BaseFragmentActivity{

    private final String TAG  = FilterActivityActivity.class.getSimpleName();
    private Toolbar toolbar = null;
    private HashMap<String,String> defaultParamsMap = null;
    private TextView tv_selected_builders,tv_selected_city,tv_selected_micro,tv_selected_sector;
    private TextView Price_left,Price_right,tvBuildUpAreaLeft,tvBuildUpAreaRignt,tvpsf_left,tvpsf_right;
    private Button btn_apply_filter = null;
    private ImageButton iv_arrow_more_builders = null;

    //Status Type
    private CheckBox chk_new_launch, chk_under_construction, chk_readytomove;
    // Short By
    private Button btn_price, btn_pricepsf, btn_possession, btn_newest, btn_rating, btn_infra, btn_needs, btn_lofestyle, btn_returns, btn_residential, btn_commercial
            , BHKCheck,Relevance,projectFilter, btnClosePopup;
    // Possession in
    private Button btn1y,btn2y, btn3y, btn4y, btn5y, btn6y;
    private CheckBox chk_resi_flat, chk_resi_builder, chk_resi_plots, chk_resi_house_villa, chk_com_shops, chk_com_office, chk_com_service_apartment;
    // BHK
    private CheckBox chk_one_bhk, chk_two_bhk, chk_three_bhk, chk_four_bhk, chk_five_bhk,chk_five_pluse_bhk;
    // Amenities
    private CheckBox chk_gas, chk_lift, chk_parking,chk_gym,chk_wifi, chk_power_backup,chk_security, chk_ac,
            chk_swimming, chk_home_automation,chk_treated_water,chk_party_hall;

    // Residential and commertial
    private LinearLayout residential_container,commercial_container;
    private RelativeLayout rl_bhk_container,rl_select_builder,rl_select_city,rl_select_micro,rl_select_sector;
    private RangeSeekBar<Integer> seekBarPrice,seekBarBuildUp,seekBarPsf;
    private Typeface fond;
    String filer_type = "";
    String sort_value = "";
    String offer_value = "";
    String status_possession = "";
    //ArrayList<String> amenities_value = new ArrayList<>();
    String status_value = "";
    String price_range_min_value = "";
    String price_range_max_value = "";
    String built_area_min_value = "";
    String built_area_max_value = "";
    String price_psf_min_value = "";
    String price_psf_max_value = "";

    String residential_value = "";
    String commercial_value = "";
    private String special_category = "";


    private String[] priceLabels = { "25L", "50L", "1Cr", "2Cr", "3Cr", "4Cr", "5Cr" };
    private String[] priceLabelsInt = { "2500000", "5000000", "10000000", "20000000", "30000000", "40000000", "50000000" };
    private String[] builtUpAreaLabels = { "250", "500", "1000", "2500", "3000", "3500", "4000", "4500", "5000", "6000", "7000", "8000" };
    private String[] psfLabels = { "500", "1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000", "10000", "15000", "20000" };

    private int MIN_PRICE = 0, MAX_PRICE = priceLabels.length - 1;
    private int MIN_AREA = 0, MAX_AREA = builtUpAreaLabels.length - 1;
    private int MIN_PSF = 0, MAX_PSF = psfLabels.length - 1;

    private final int SELECT_BUILDER_REQ = 480;
    private final int SELECT_CITY_REQ = 481;
    private final int SELECT_MICRO_REQ = 482;
    private final int SELECT_SECTOR_REQ = 483;


    private String selectedBuilderNames = "";
    private String selectedBuilderIds = "";
    private ArrayList<Builder> selectedBuilders = null;

    private CityDataModel selectedCity = null;
    private MicroMarketData selectedMicroMarket = null;
    private SectorData selectedSector = null;

    private AsyncThread mAsyncThread = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        fond = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        initViews();
        setListeners();
        if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
            IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            defaultParamsMap = mIntentDataObject.getData();
            selectedBuilders = (ArrayList<Builder>) mIntentDataObject.getObj();
            HashMap<String,String>  appliedFilterStateMap = mIntentDataObject.getFilterStateMap();
            if( appliedFilterStateMap!= null){
                appliedFilterUIState(appliedFilterStateMap);
            }
            else if(defaultParamsMap != null){
                defaultUIState(defaultParamsMap);
            }
        }

    }

    private void initViews() {
        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.filter));

        Price_left = (TextView) findViewById(R.id.Price_left);
        Price_right = (TextView) findViewById(R.id.Price_right);
        Price_left.setTypeface(fond);
        Price_right.setTypeface(fond);

        tvBuildUpAreaLeft = (TextView) findViewById(R.id.built_area_left);
        tvBuildUpAreaRignt = (TextView) findViewById(R.id.built_area_right);
        tvBuildUpAreaLeft.setTypeface(fond);
        tvBuildUpAreaRignt.setTypeface(fond);

        tvpsf_left = (TextView) findViewById(R.id.price_psf_left);
        tvpsf_right = (TextView) findViewById(R.id.price_psf_right);
        tvpsf_left.setTypeface(fond);
        tvpsf_right.setTypeface(fond);

        tv_selected_builders = (TextView) findViewById(R.id.tv_selected_builders);
        iv_arrow_more_builders = (ImageButton)findViewById(R.id.iv_arrow_more_builders);

        // Relevance = (Button) findViewById(R.id.btn_relevance);
        btn_newest = (Button)findViewById(R.id.btn_newest);
        btn_price = (Button)findViewById(R.id.btn_price);
        btn_pricepsf = (Button)findViewById(R.id.btn_pricepsf);
        btn_rating = (Button)findViewById(R.id.btn_rating);
        btn_possession = (Button)findViewById(R.id.btn_possession);
        btn_infra = (Button)findViewById(R.id.btn_infra);
        btn_needs = (Button)findViewById(R.id.btn_needs);
        btn_lofestyle = (Button)findViewById(R.id.btn_lofestyle);
        btn_returns = (Button)findViewById(R.id.btn_returns);

        btn1y = (Button)findViewById(R.id.btn_1y);
        btn2y = (Button)findViewById(R.id.btn_2y);
        btn3y = (Button)findViewById(R.id.btn_3y);
        btn4y = (Button)findViewById(R.id.btn_4y);
        btn5y = (Button)findViewById(R.id.btn_5y);
        btn6y = (Button)findViewById(R.id.btn_6y);

        // btnlaunch = (Button)findViewById(R.id.btnNew_launch);
        // btnUnder = (Button)findViewById(R.id.btn_under_construction);
        // btnReady = (Button)findViewById(R.id.btn_readytomove);

        chk_new_launch = (CheckBox)findViewById(R.id.chk_new_launch);
        chk_under_construction = (CheckBox)findViewById(R.id.chk_under_construction);
        chk_readytomove = (CheckBox)findViewById(R.id.chk_readytomove);

        btn_residential = (Button)findViewById(R.id.btn_residential);
        btn_commercial = (Button)findViewById(R.id.btn_commercial);

        chk_resi_flat = (CheckBox)findViewById(R.id.chk_resi_flat);
        chk_resi_builder = (CheckBox)findViewById(R.id.chk_resi_builder);
        chk_resi_plots = (CheckBox)findViewById(R.id.chk_resi_plots);
        chk_resi_house_villa = (CheckBox)findViewById(R.id.chk_resi_house_villa);

        chk_com_shops = (CheckBox)findViewById(R.id.chk_com_shops);
        chk_com_office = (CheckBox)findViewById(R.id.chk_com_office);
        chk_com_service_apartment = (CheckBox)findViewById(R.id.chk_com_service_apartment);

        rl_bhk_container = (RelativeLayout)findViewById(R.id.rl_bhk_container);
        rl_select_builder = (RelativeLayout)findViewById(R.id.rl_select_builder);
        chk_one_bhk = (CheckBox)findViewById(R.id.chk_one_bhk);
        chk_two_bhk = (CheckBox)findViewById(R.id.chk_two_bhk);
        chk_three_bhk = (CheckBox)findViewById(R.id.chk_three_bhk);
        chk_four_bhk = (CheckBox)findViewById(R.id.chk_four_bhk);
        chk_five_bhk = (CheckBox)findViewById(R.id.chk_five_bhk);
        chk_five_pluse_bhk = (CheckBox)findViewById(R.id.chk_five_pluse_bhk);

        chk_power_backup = (CheckBox)findViewById(R.id.chk_power_backup);
        chk_gym = (CheckBox)findViewById(R.id.chk_gym);
        chk_wifi = (CheckBox)findViewById(R.id.chk_wifi);
        chk_gas = (CheckBox)findViewById(R.id.chk_gas);
        chk_lift = (CheckBox)findViewById(R.id.chk_lift);
        chk_parking = (CheckBox)findViewById(R.id.chk_parking);
        chk_security = (CheckBox)findViewById(R.id.chk_security);
        chk_ac = (CheckBox)findViewById(R.id.chk_ac);
        chk_swimming = (CheckBox)findViewById(R.id.chk_swimming);
        chk_home_automation = (CheckBox)findViewById(R.id.chk_home_automation);
        chk_treated_water = (CheckBox)findViewById(R.id.chk_treated_water);
        chk_party_hall = (CheckBox)findViewById(R.id.chk_party_hall);

        residential_container = (LinearLayout)findViewById(R.id.residential_container);
        commercial_container = (LinearLayout)findViewById(R.id.commercial_container);

        final LinearLayout llPrice = (LinearLayout)findViewById(R.id.llseekbar);
        LinearLayout llBuiltUpArea = (LinearLayout)findViewById(R.id.llseekbar_builtArea);
        LinearLayout llseekBarPSF = (LinearLayout)findViewById(R.id.llseekbar_pricePSF);

        //tv_reset_filter = (TextView)findViewById(R.id.tv_reset_filter);
        btn_apply_filter = (Button)findViewById(R.id.btn_apply_filter);

        // =========== Set only font static as Heading
        TextView status = (TextView)findViewById(R.id.status);
        TextView sort = (TextView)findViewById(R.id.text_sort);
        TextView project = (TextView)findViewById(R.id.with);
        final TextView possession = (TextView)findViewById(R.id.textView4);
        TextView select_b = (TextView)findViewById(R.id.builder_li);
        TextView property = (TextView)findViewById(R.id.tv_showing_floors);
        TextView property_text = (TextView)findViewById(R.id.tv_filter_by_text);
        TextView bhk = (TextView)findViewById(R.id.bhk_list);
        TextView price_rang = (TextView)findViewById(R.id.text_seekbar);
        TextView build = (TextView)findViewById(R.id.text_built);
        TextView pricepsf = (TextView)findViewById(R.id.text_price);
        TextView amenities = (TextView)findViewById(R.id.ame);

        TextView city_li = (TextView)findViewById(R.id.city_li);
        TextView micro_li = (TextView)findViewById(R.id.micro_li);
        TextView sector_li = (TextView)findViewById(R.id.sector_li);

        tv_selected_city = (TextView)findViewById(R.id.tv_selected_city);
        tv_selected_micro = (TextView)findViewById(R.id.tv_selected_micro);
        tv_selected_sector = (TextView)findViewById(R.id.tv_selected_sector);

        rl_select_city = (RelativeLayout)findViewById(R.id.rl_select_city);
        rl_select_micro = (RelativeLayout)findViewById(R.id.rl_select_micro);
        rl_select_sector = (RelativeLayout)findViewById(R.id.rl_select_sector);

        tv_selected_city.setTypeface(fond);
        tv_selected_micro.setTypeface(fond);
        tv_selected_sector.setTypeface(fond);

        city_li.setTypeface(fond);
        micro_li.setTypeface(fond);
        sector_li.setTypeface(fond);

        status.setTypeface(fond);
        sort.setTypeface(fond);
        project.setTypeface(fond);
        possession.setTypeface(fond);
        select_b.setTypeface(fond);
        tv_selected_builders.setTypeface(fond);
        property.setTypeface(fond);
        property_text.setTypeface(fond);
        bhk.setTypeface(fond);
        price_rang.setTypeface(fond);
        build.setTypeface(fond);
        pricepsf.setTypeface(fond);
        amenities.setTypeface(fond);
        // Relevance.setTypeface(regularTypeface);
        btn_newest.setTypeface(fond);
        btn_price.setTypeface(fond);
        btn_pricepsf.setTypeface(fond);
        btn_rating.setTypeface(fond);
        btn_possession.setTypeface(fond);
        btn_infra.setTypeface(fond);
        btn_needs.setTypeface(fond);
        btn_lofestyle.setTypeface(fond);
        btn_returns.setTypeface(fond);
        btn1y.setTypeface(fond);
        btn2y.setTypeface(fond);
        btn3y.setTypeface(fond);
        btn4y.setTypeface(fond);
        btn5y.setTypeface(fond);
        btn6y.setTypeface(fond);
        // btnlaunch.setTypeface(regularTypeface);
        // btnUnder.setTypeface(regularTypeface);
        // btnReady.setTypeface(regularTypeface);
        chk_new_launch.setTypeface(fond);
        chk_under_construction.setTypeface(fond);
        chk_readytomove.setTypeface(fond);

        btn_residential.setTypeface(fond);
        btn_residential.setSelected(true);
        btn_commercial.setTypeface(fond);
        chk_resi_flat.setTypeface(fond);
        chk_resi_builder.setTypeface(fond);
        chk_resi_plots.setTypeface(fond);
        chk_resi_house_villa.setTypeface(fond);
        chk_com_shops.setTypeface(fond);
        chk_com_service_apartment.setTypeface(fond);
        btn_apply_filter.setTypeface(fond);

        seekBarPrice = addPriceSeekBar();
        seekBarBuildUp = addBuildUpSeekBar();
        seekBarPsf = addSeekBarPsf();
        llPrice.addView(seekBarPrice);
        llBuiltUpArea.addView(seekBarBuildUp);
        llseekBarPSF.addView(seekBarPsf);


    }
    private void setListeners(){
        //listProperty.setOnItemClickListener(mOnItemClickListener);
        btn_newest.setOnClickListener(mOnClickListener);
        btn_price.setOnClickListener(mOnClickListener);
        btn_pricepsf.setOnClickListener(mOnClickListener);
        btn_rating.setOnClickListener(mOnClickListener);
        btn_possession.setOnClickListener(mOnClickListener);
        btn_infra.setOnClickListener(mOnClickListener);
        btn_needs.setOnClickListener(mOnClickListener);
        btn_lofestyle.setOnClickListener(mOnClickListener);
        btn_returns.setOnClickListener(mOnClickListener);

        btn1y.setOnClickListener(mOnClickListener);
        btn2y.setOnClickListener(mOnClickListener);
        btn3y.setOnClickListener(mOnClickListener);
        btn4y.setOnClickListener(mOnClickListener);
        btn5y.setOnClickListener(mOnClickListener);
        btn6y.setOnClickListener(mOnClickListener);

        btn_residential.setOnClickListener(mOnClickListener);
        btn_commercial.setOnClickListener(mOnClickListener);
        //tv_reset_filter.setOnClickListener(mOnClickListener);
        btn_apply_filter.setOnClickListener(mOnClickListener);

        chk_new_launch.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_under_construction.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_readytomove.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_resi_plots.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_resi_flat.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_resi_builder.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_resi_house_villa.setOnCheckedChangeListener(mOnCheckedChangeListener);

        chk_com_shops.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_com_office.setOnCheckedChangeListener(mOnCheckedChangeListener);
        chk_com_service_apartment.setOnCheckedChangeListener(mOnCheckedChangeListener);


        rl_select_builder.setOnClickListener(mOnClickListener);

        rl_select_city.setOnClickListener(mOnClickListener);
        rl_select_micro.setOnClickListener(mOnClickListener);
        rl_select_sector.setOnClickListener(mOnClickListener);

    }


    private void defaultUIState(HashMap<String,String> mapParams){
        if(mapParams == null || toolbar == null)return;
        if(mapParams.get(ParamsConstants.BUILDER_NAME) != null){
            tv_selected_builders.setText(mapParams.get(ParamsConstants.BUILDER_NAME));
            iv_arrow_more_builders.setVisibility(View.INVISIBLE);
        }else{
            tv_selected_builders.setText("All");
            iv_arrow_more_builders.setVisibility(View.VISIBLE);
        }
        if(mapParams.get(ParamsConstants.P_TYPE) != null){
            String pType = mapParams.get(ParamsConstants.P_TYPE);
            pType = Utils.removeLastAndSign(pType);
            List<String> list = new ArrayList<String>(Arrays.asList(pType.split(",")));
            if(list != null){
                if(list.contains(PropertyType.FLAT.value)|| list.contains(PropertyType.BUILDER_FLOOR.value) || list.contains(PropertyType.PLOT.value) || list.contains(PropertyType.HOUSE_VILLA.value)){
                    BMHConstants.SELECTED_PROPERTY_TYPE = RESIDENTIAL_PROPERTY;
                    residentialCommercialViewState(btn_residential);
                    if(list.contains(PropertyType.FLAT.value))chk_resi_flat.setChecked(true);
                    if(list.contains(PropertyType.BUILDER_FLOOR.value))chk_resi_builder.setChecked(true);
                    if(list.contains(PropertyType.PLOT.value))chk_resi_plots.setChecked(true);
                    if(list.contains(PropertyType.HOUSE_VILLA.value))chk_resi_house_villa.setChecked(true);

                }else if(list.contains(PropertyType.SHOPS_SHOWROOM.value)|| list.contains(PropertyType.OFFICE_SPACE.value) || list.contains(PropertyType.SERVICE_APARTMENT.value)){
                    BMHConstants.SELECTED_PROPERTY_TYPE = COMMERCIAL_PROPERTY;
                    residentialCommercialViewState(btn_commercial);
                    if(list.contains(PropertyType.SHOPS_SHOWROOM.value))chk_com_shops.setChecked(true);
                    if(list.contains(PropertyType.OFFICE_SPACE.value))chk_com_office.setChecked(true);
                    if(list.contains(PropertyType.SERVICE_APARTMENT.value))chk_com_service_apartment.setChecked(true);
                }else{
                    BMHConstants.SELECTED_PROPERTY_TYPE = BMHConstants.NONE;

                }
            }
        }
        LinearLayout linear1 = (LinearLayout)findViewById(R.id.linear1);
        RelativeLayout layout_text = (RelativeLayout)findViewById(R.id.layout_text);
        if (mapParams.get(ParamsConstants.SPECIAL_CATEGORY) != null && mapParams.get(ParamsConstants.SPECIAL_CATEGORY).equalsIgnoreCase(ProjectType.READY_TO_MOVE.value)) {
            linear1.setVisibility(View.GONE);
            layout_text.setVisibility(View.GONE);
        }else{
            linear1.setVisibility(View.VISIBLE);
            layout_text.setVisibility(View.VISIBLE);
        }
    }

    private void appliedFilterUIState(HashMap<String,String> mapParams){
        if(mapParams == null || toolbar == null)return;
        if(mapParams.get("project_status") != null){
            String status = mapParams.get("project_status");
            if(status.equals("new launch")){
                buildingStatusState(chk_new_launch);
            }else if(status.equals("under construction")){
                buildingStatusState(chk_under_construction);
            }else if(status.equals("ready to move")){
                buildingStatusState(chk_readytomove);
            }
        }
        if(mapParams.get("newest") != null){
            sort_value = mapParams.get("newest");
            shortByButtonState(btn_newest);
        }else if(mapParams.get("rating") != null){
            sort_value = mapParams.get("rating");
            shortByButtonState(btn_rating);
        }else if(mapParams.get("infra") != null){
            sort_value = mapParams.get("infra");
            shortByButtonState(btn_infra);
        }else if(mapParams.get("needs") != null){
            sort_value = mapParams.get("needs");
            shortByButtonState(btn_needs);
        }else if(mapParams.get("lifestyle") != null){
            sort_value = mapParams.get("lifestyle");
            shortByButtonState(btn_lofestyle);
        }else if(mapParams.get("returnsval") != null){
            sort_value = mapParams.get("returnsval");
            shortByButtonState(btn_returns);
        }else if(mapParams.get("priceltoh") != null){
            sort_value = "priceltoh";
            shortByButtonState(btn_price);
        }else if(mapParams.get("pricehtol") != null){
            sort_value = "pricehtol";
            shortByButtonState(btn_price);
        }else if(mapParams.get("psfltoh") != null){
            sort_value = "pricePSFltoh";
            shortByButtonState(btn_pricepsf);
        }else if(mapParams.get("psfhtol") != null){
            sort_value = "pricePSFhtol";
            shortByButtonState(btn_pricepsf);
        }else if(mapParams.get("possessionltoh") != null){
            sort_value = "Possessionltoh";
            shortByButtonState(btn_possession);
        }else if(mapParams.get("possessionhtol") != null){
            sort_value = "Possessionhtol";
            shortByButtonState(btn_possession);
        }
        if(mapParams.get("posession") != null){
            String possession = mapParams.get("posession");
            if(possession.equals("6m")){
                possessionButtonState(btn1y);
                status_possession = "6m";
            }else if(possession.equals("1y")){
                possessionButtonState(btn2y);
                status_possession = "1y";
            }else if(possession.equals("2y")){
                possessionButtonState(btn3y);
                status_possession = "2y";
            }else if(possession.equals("3y")){
                possessionButtonState(btn4y);
                status_possession = "3y";
            }else if(possession.equals("4y")){
                possessionButtonState(btn5y);
                status_possession = "4y";
            }else if(possession.equals("5y")){
                possessionButtonState(btn6y);
                status_possession = "5y";
            }
        }
        if(defaultParamsMap != null && defaultParamsMap.get(ParamsConstants.BUILDER_NAME) != null){
            tv_selected_builders.setText(defaultParamsMap.get(ParamsConstants.BUILDER_NAME));
            iv_arrow_more_builders.setVisibility(View.INVISIBLE);
        }else if(selectedBuilders != null ){
            iv_arrow_more_builders.setVisibility(View.VISIBLE);
            setSelectedBuilders(selectedBuilders);
        }else{
            tv_selected_builders.setText("All");
            iv_arrow_more_builders.setVisibility(View.VISIBLE);
        }

        if(mapParams.get(ParamsConstants.CITY_NAME) != null){
            selectedCity = new CityDataModel();
            selectedCity.setCity_id(mapParams.get(ParamsConstants.CITY_ID));
            selectedCity.setCity_name(mapParams.get(ParamsConstants.CITY_NAME));
            tv_selected_city.setText(mapParams.get(ParamsConstants.CITY_NAME));
            tv_selected_micro.setText("All");
            tv_selected_sector.setText("All");
        }if(mapParams.get(ParamsConstants.LOCATION) != null){
            selectedMicroMarket = new MicroMarketData();
            selectedMicroMarket.setMicromarket_id(mapParams.get(ParamsConstants.LOCATION));
            selectedMicroMarket.setMicromarket_name(mapParams.get(ParamsConstants.LOCATION_NAME_TEMP));
            tv_selected_micro.setText(mapParams.get(ParamsConstants.LOCATION_NAME_TEMP));
            tv_selected_sector.setText("All");
        }if(mapParams.get(ParamsConstants.SUB_LOCATION_NAME_TEMP) != null){
            selectedSector = new SectorData();
            selectedSector.setSector_id(mapParams.get(ParamsConstants.SUB_LOCATION_ID));
            selectedSector.setSector_name(mapParams.get(ParamsConstants.SUB_LOCATION_NAME_TEMP));
            tv_selected_sector.setText(mapParams.get(ParamsConstants.SUB_LOCATION_NAME_TEMP));
        }



        if(mapParams.get(ParamsConstants.P_TYPE) != null){
            String pType = mapParams.get(ParamsConstants.P_TYPE);
            pType = Utils.removeLastAndSign(pType);
            List<String> list = new ArrayList<String>(Arrays.asList(pType.split(",")));
            if(list != null){
                if(list.contains("Flat")|| list.contains("Builder Floor") || list.contains("Plot") || list.contains("House Villa")){
                    BMHConstants.SELECTED_PROPERTY_TYPE = RESIDENTIAL_PROPERTY;
                    residentialCommercialViewState(btn_residential);
                    if(list.contains("Flat"))chk_resi_flat.setChecked(true);
                    if(list.contains("Builder Floor"))chk_resi_builder.setChecked(true);
                    if(list.contains("Plot"))chk_resi_plots.setChecked(true);
                    if(list.contains("House Villa"))chk_resi_house_villa.setChecked(true);

                }else if(list.contains("Shop")|| list.contains("Office") || list.contains("Apartment")){
                    BMHConstants.SELECTED_PROPERTY_TYPE = COMMERCIAL_PROPERTY;
                    residentialCommercialViewState(btn_commercial);
                    if(list.contains("Shop"))chk_com_shops.setChecked(true);
                    if(list.contains("Office"))chk_com_office.setChecked(true);
                    if(list.contains("Apartment"))chk_com_service_apartment.setChecked(true);
                }else{
                    BMHConstants.SELECTED_PROPERTY_TYPE = BMHConstants.NONE;

                }
            }
        }
        LinearLayout linear1 = (LinearLayout)findViewById(R.id.linear1);
        RelativeLayout layout_text = (RelativeLayout)findViewById(R.id.layout_text);
        if (mapParams.get(ParamsConstants.SPECIAL_CATEGORY) != null && mapParams.get(ParamsConstants.SPECIAL_CATEGORY).equalsIgnoreCase(ProjectType.READY_TO_MOVE.value)) {
            linear1.setVisibility(View.GONE);
            layout_text.setVisibility(View.GONE);
        }else{
            linear1.setVisibility(View.VISIBLE);
            layout_text.setVisibility(View.VISIBLE);
        }
        if(mapParams.get("bhk") != null){
            String bhk = mapParams.get("bhk");
            bhk = Utils.removeLastComma(bhk);
            List<String> bhkLIst = new ArrayList<String>(Arrays.asList(bhk.split(",")));
            if(bhkLIst != null){
                for (String temp : bhkLIst){
                    if(temp.equals("1bhk")){
                        chk_one_bhk.setChecked(true);
                    }else if(temp.equals("2bhk")){
                        chk_two_bhk.setChecked(true);
                    }else if(temp.equals("3bhk")){
                        chk_three_bhk.setChecked(true);
                    }else if(temp.equals("4bhk")){
                        chk_four_bhk.setChecked(true);
                    }else if(temp.equals("5bhk")){
                        chk_five_bhk.setChecked(true);
                    }
                }
            }
        }
        if(mapParams.get("min-p") != null){
            String minP = mapParams.get("min-p");
            MIN_PRICE = Arrays.asList(priceLabelsInt).indexOf(minP);
            Price_left.setText(priceLabels[MIN_PRICE]);
            price_range_min_value = minP;
            seekBarPrice.setSelectedMinValue(MIN_PRICE);

        }if(mapParams.get("max-p") != null){
            String maxP = mapParams.get("max-p");
            MAX_PRICE = Arrays.asList(priceLabelsInt).indexOf(maxP);
            Price_right.setText(priceLabels[MAX_PRICE]);
            price_range_max_value = maxP;
            seekBarPrice.setSelectedMaxValue(MAX_PRICE);
        }
        if(mapParams.get("min_area_range") != null){
            String minArea = mapParams.get("min_area_range");
            MIN_AREA = Arrays.asList(builtUpAreaLabels).indexOf(minArea);
            tvBuildUpAreaLeft.setText(minArea);
            built_area_min_value = minArea;
            seekBarBuildUp.setSelectedMinValue(MIN_AREA);

        }if(mapParams.get("max_area_range") != null){
            String maxArea = mapParams.get("max_area_range");
            MAX_AREA = Arrays.asList(builtUpAreaLabels).indexOf(maxArea);
            tvBuildUpAreaRignt.setText(maxArea);
            built_area_max_value = maxArea;
            seekBarBuildUp.setSelectedMaxValue(MAX_AREA);
        }
        if(mapParams.get("min_psf") != null){
            String minPsf = mapParams.get("min_psf");
            MIN_PSF = Arrays.asList(psfLabels).indexOf(minPsf);
            tvpsf_left.setText(minPsf);
            price_psf_min_value = minPsf;
            seekBarPsf.setSelectedMinValue(MIN_PSF);

        }if(mapParams.get("max_psf") != null){
            String maxPsf = mapParams.get("max_psf");
            MAX_PSF = Arrays.asList(psfLabels).indexOf(maxPsf);
            tvpsf_right.setText(maxPsf);
            price_psf_max_value = maxPsf;
            seekBarPsf.setSelectedMaxValue(MAX_PSF);
        }

        if(mapParams.get("lifestyles") != null){
            String lifestyles = mapParams.get("lifestyles");
            lifestyles = Utils.removeLastComma(lifestyles);
            List<String> lifestylesLIst = new ArrayList<String>(Arrays.asList(lifestyles.split(",")));
            if(lifestylesLIst != null){
                for (String temp : lifestylesLIst){
                    if(temp.equals("Piped_Gas")){
                        chk_gas.setChecked(true);
                    }else if(temp.equals("Lift")){
                        chk_lift.setChecked(true);
                    }else if(temp.equals("Podium_Parking")){
                        chk_parking.setChecked(true);
                    }else if(temp.equals("Gymnasium")){
                        chk_gym.setChecked(true);
                    }else if(temp.equals("24*7_Security")){
                        chk_security.setChecked(true);
                    }else if(temp.equals("Central_Wi-fi")){
                        chk_wifi.setChecked(true);
                    }else if(temp.equals("Treated_Water_Supply")){
                        chk_treated_water.setChecked(true);
                    }else if(temp.equals("Party_Hall")){
                        chk_party_hall.setChecked(true);
                    }else if(temp.equals("AC_in_Lobby")){
                        chk_ac.setChecked(true);
                    }else if(temp.equals("Swimming")){
                        chk_swimming.setChecked(true);
                    }else if(temp.equals("Home_Automation")){
                        chk_home_automation.setChecked(true);
                    }else if(temp.equals("Power_Backup")){
                        chk_power_backup.setChecked(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        app =(BMHApplication) getApplication();
    }


    @Override
    protected String setActionBarTitle() {
        return "Filter";
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_reset:
                resetFilters();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void buildingStatusState(CheckBox selectedView) {
        chk_new_launch.setSelected(false);
        chk_under_construction.setSelected(false);
        chk_readytomove.setSelected(false);
        if(selectedView != null)
            selectedView.setSelected(true);
        if(selectedView != null && selectedView.getId() == R.id.chk_readytomove){
            btn_possession.setClickable(false);
            btn_possession.setSelected(false);
            btn_possession.setText(getString(R.string.project_Possession));
            btn_possession.setTextColor(getResources().getColor(R.color.white2));
            possessionButtonActiveState(false);
            possessionButtonState(null);

        }else{
            btn_possession.setClickable(true);
            possessionButtonActiveState(true);
            btn_possession.setTextColor(getResources().getColor(R.color.black));

        }
    }


    /*private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }*/
    private void shortByButtonState(View selectedBtn){
        btn_newest.setSelected(false);
        btn_price.setSelected(false);
        btn_pricepsf.setSelected(false);
        btn_rating.setSelected(false);
        btn_possession.setSelected(false);
        btn_infra.setSelected(false);
        btn_needs.setSelected(false);
        btn_lofestyle.setSelected(false);
        btn_returns.setSelected(false);
        btn_price.setText(getString(R.string.project_price));
        btn_pricepsf.setText(getString(R.string.project_price_psf));
        btn_possession.setText(getString(R.string.project_Possession));
        if(selectedBtn != null) {
            selectedBtn.setSelected(true);
            if (selectedBtn.getId() == R.id.btn_price) {
                if (sort_value.equals("priceltoh")) {
                    btn_price.setText(getString(R.string.low_to_high));
                } else if (sort_value.equals("pricehtol")) {
                    btn_price.setText(getString(R.string.high_to_low));
                }else if (sort_value.equals("")) {
                    btn_price.setText(getString(R.string.project_price));
                }

            } else if (selectedBtn.getId() == R.id.btn_pricepsf) {
                if (sort_value.equals("pricePSFltoh")) {
                    btn_pricepsf.setText(getString(R.string.low_to_high));
                } else if (sort_value.equals("pricePSFhtol")) {
                    btn_pricepsf.setText(getString(R.string.high_to_low));
                }else if (sort_value.equals("")) {
                    btn_pricepsf.setText(getString(R.string.project_price_psf));
                }

            } else if (selectedBtn.getId() == R.id.btn_possession) {
                if (sort_value.equals("Possessionltoh")) {
                    btn_possession.setText(getString(R.string.earliest_to_latest));
                } else if (sort_value.equals("Possessionhtol")) {
                    btn_possession.setText(getString(R.string.latest_to_earliest));
                }else if(sort_value.equals("")){
                    btn_possession.setText(getString(R.string.project_Possession));
                }
            }
        }
    }



    private void possessionButtonState(View selectedBtn){
        btn1y.setSelected(false);
        btn1y.setSelected(false);
        btn2y.setSelected(false);
        btn3y.setSelected(false);
        btn4y.setSelected(false);
        btn5y.setSelected(false);
        btn6y.setSelected(false);
        if(selectedBtn != null) selectedBtn.setSelected(true);
    }
    private void possessionButtonActiveState(boolean isEnables){
        if(!isEnables) {
            btn1y.setTextColor(getResources().getColor(R.color.white2));
            btn2y.setTextColor(getResources().getColor(R.color.white2));
            btn3y.setTextColor(getResources().getColor(R.color.white2));
            btn4y.setTextColor(getResources().getColor(R.color.white2));
            btn5y.setTextColor(getResources().getColor(R.color.white2));
            btn6y.setTextColor(getResources().getColor(R.color.white2));
        }else{
            btn1y.setTextColor(getResources().getColor(R.color.black));
            btn2y.setTextColor(getResources().getColor(R.color.black));
            btn3y.setTextColor(getResources().getColor(R.color.black));
            btn4y.setTextColor(getResources().getColor(R.color.black));
            btn5y.setTextColor(getResources().getColor(R.color.black));
            btn6y.setTextColor(getResources().getColor(R.color.black));
        }
        btn1y.setEnabled(isEnables);
        btn1y.setEnabled(isEnables);
        btn2y.setEnabled(isEnables);
        btn3y.setEnabled(isEnables);
        btn4y.setEnabled(isEnables);
        btn5y.setEnabled(isEnables);
        btn6y.setEnabled(isEnables);
    }


    private void residentialCommercialViewState(View selectedView){
        if(selectedView.getId() == R.id.btn_residential){
            residential_container.setVisibility(View.VISIBLE);
            commercial_container.setVisibility(View.INVISIBLE);
            btn_residential.setSelected(true);
            btn_commercial.setSelected(false);
            chk_com_shops.setChecked(false);
            chk_com_office.setChecked(false);
            chk_com_service_apartment.setChecked(false);
            resetBHKState();
            rl_bhk_container.setVisibility(View.VISIBLE);
        }else if(selectedView.getId() == R.id.btn_commercial){
            residential_container.setVisibility(View.INVISIBLE);
            commercial_container.setVisibility(View.VISIBLE);
            btn_residential.setSelected(false);
            btn_commercial.setSelected(true);
            chk_resi_flat.setChecked(false);
            chk_resi_builder.setChecked(false);
            chk_resi_plots.setChecked(false);
            chk_resi_house_villa.setChecked(false);
            resetBHKState();
            rl_bhk_container.setVisibility(View.GONE);
        }else{
            //TODO: do nothing
        }
    }

    private void showPopupLowHigh(final Button selectedBtn) {
        if(selectedBtn == null)return;
        View layout = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.high_low_layout, null);;
        final PopupWindow pw = new PopupWindow(layout, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, false);
        pw.setBackgroundDrawable(new ColorDrawable(mResources.getColor(android.R.color.transparent)));
        pw.setOutsideTouchable(true);
        final Button btnLtoH = (Button) layout.findViewById(R.id.btnLtoH);
        final Button btnHtoL = (Button) layout.findViewById(R.id.btnHtoL);
        final Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        btnLtoH.setTypeface(fond);
        btnHtoL.setTypeface(fond);
        btnCancel.setTypeface(fond);
        // For restore previous selected states
        if(selectedBtn.getId() == R.id.btn_price){
            btnLtoH.setText(getString(R.string.low_to_high));
            btnHtoL.setText(getString(R.string.high_to_low));
            btnCancel.setText(getString(R.string.cancel));
            if(sort_value.equals("priceltoh"))btnLtoH.setSelected(true);
            else if(sort_value.equals("pricehtol"))btnHtoL.setSelected(true);

        }else if(selectedBtn.getId() == R.id.btn_pricepsf){
            btnLtoH.setText(getString(R.string.low_to_high));
            btnHtoL.setText(getString(R.string.high_to_low));
            btnCancel.setText(getString(R.string.cancel));
            if(sort_value.equals("pricePSFltoh"))btnLtoH.setSelected(true);
            else if(sort_value.equals("pricePSFhtol"))btnHtoL.setSelected(true);
        }else if(selectedBtn.getId() == R.id.btn_possession){
            btnLtoH.setText("Earliest to latest");
            btnHtoL.setText("Latest to earliest");
            btnCancel.setText(getString(R.string.cancel));
            if(sort_value.equals("Possessionltoh"))btnLtoH.setSelected(true);
            else if(sort_value.equals("Possessionhtol"))btnHtoL.setSelected(true);
        }
        btnLtoH.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vv) {
                if(selectedBtn.getId() == R.id.btn_price){
                    sort_value = "priceltoh";
                }else if(selectedBtn.getId() == R.id.btn_pricepsf){
                    sort_value = "pricePSFltoh";
                }else if(selectedBtn.getId() == R.id.btn_possession){
                    if(chk_readytomove.isSelected()){
                        sort_value = "";
                    }else {
                        sort_value = "Possessionltoh";
                    }
                }
                btnLtoH.setSelected(true);
                btnHtoL.setSelected(false);
                shortByButtonState(selectedBtn);
                pw.dismiss();

            }
        });

        btnHtoL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vv) {
                if(selectedBtn.getId() == R.id.btn_price){
                    sort_value = "pricehtol";
                }else if(selectedBtn.getId() == R.id.btn_pricepsf){
                    sort_value = "pricePSFhtol";
                }else if(selectedBtn.getId() == R.id.btn_possession){
                    if(chk_readytomove.isSelected()){
                        sort_value = "";
                    }else{
                        sort_value = "Possessionhtol";
                    }

                }else{

                }
                btnHtoL.setSelected(true);
                btnLtoH.setSelected(false);
                shortByButtonState(selectedBtn);
                pw.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vv) {
                if(selectedBtn.getId() == R.id.btn_price){
                    sort_value = "";
                }else if(selectedBtn.getId() == R.id.btn_pricepsf){
                    sort_value = "";
                }else if(selectedBtn.getId() == R.id.btn_possession){
                    if(chk_readytomove.isSelected()){
                        sort_value = "";
                    }else{
                        sort_value = "";
                    }

                }else{

                }
                btnHtoL.setSelected(false);
                btnLtoH.setSelected(false);
                shortByButtonState(null);
                pw.dismiss();
            }
        });
        // finally show the popup in the center of the window or any position
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(selectedBtn);
    }

    private RangeSeekBar<Integer> addPriceSeekBar() {
        RangeSeekBar<Integer> seekBarPrice = new RangeSeekBar<Integer>(MIN_PRICE, MAX_PRICE, this);
        Price_left.setText(priceLabels[seekBarPrice.getAbsoluteMinValue()]);
        Price_right.setText(priceLabels[seekBarPrice.getAbsoluteMaxValue()]);
        seekBarPrice.setNotifyWhileDragging(true);
        seekBarPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                //MIN_PRICE = minValue;
                //MAX_PRICE = maxValue;
                Price_left.setText(priceLabels[minValue]);
                Price_right.setText(priceLabels[maxValue]);
                price_range_min_value = priceLabelsInt[minValue];
                price_range_max_value = priceLabelsInt[maxValue];
            }

        });
        return seekBarPrice;
    }

    private RangeSeekBar<Integer> addBuildUpSeekBar() {
        RangeSeekBar<Integer>  seekBarBuildUp = new RangeSeekBar<Integer>(MIN_AREA, MAX_AREA, this);
        tvBuildUpAreaLeft.setText("" + builtUpAreaLabels[seekBarBuildUp.getAbsoluteMinValue()] + " sqft");
        tvBuildUpAreaRignt.setText("" + builtUpAreaLabels[seekBarBuildUp.getAbsoluteMaxValue()] + " sqft");
        seekBarBuildUp.setNotifyWhileDragging(true);
        seekBarBuildUp.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                tvBuildUpAreaLeft.setText("" + builtUpAreaLabels[minValue] + " sqft");
                tvBuildUpAreaRignt.setText(builtUpAreaLabels[maxValue] + " sqft");
                built_area_min_value = builtUpAreaLabels[minValue];
                built_area_max_value = builtUpAreaLabels[maxValue];
            }
        });
        return seekBarBuildUp;
    }

    private RangeSeekBar<Integer> addSeekBarPsf() {
        RangeSeekBar<Integer> seekBarPsf = new RangeSeekBar<Integer>(MIN_PSF, MAX_PSF, this);
        tvpsf_left.setText(psfLabels[seekBarPsf.getAbsoluteMinValue()]);
        tvpsf_right.setText(psfLabels[seekBarPsf.getAbsoluteMaxValue()]);
        seekBarPsf.setNotifyWhileDragging(true);
        seekBarPsf.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                tvpsf_left.setText(psfLabels[minValue]);
                tvpsf_right.setText(psfLabels[maxValue]);
                price_psf_min_value = psfLabels[minValue];
                price_psf_max_value = psfLabels[maxValue];

            }
        });
        return seekBarPsf;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_newest:
                    if(btn_newest.isSelected()){
                        shortByButtonState(null);
                        sort_value = "";
                    }else {
                        sort_value = "newest";
                        shortByButtonState(view);
                    }
                    break;
                case R.id.btn_price:
                    /*if(btn_price.isSelected()){
                        showPopupLowHigh(null);
                    }else{*/
                        showPopupLowHigh(btn_price);
                   // }

                    break;
                case R.id.btn_pricepsf:
                    showPopupLowHigh(btn_pricepsf);
                    break;
                case R.id.btn_rating:
                    if(btn_rating.isSelected()){
                        sort_value = "";
                        shortByButtonState(null);
                    }else{
                        sort_value = "rating";
                        shortByButtonState(view);
                    }

                    break;
                case R.id.btn_possession:
                    showPopupLowHigh(btn_possession);
                    break;
                case R.id.btn_infra:
                    if(btn_infra.isSelected()){
                        sort_value = "";
                        shortByButtonState(null);
                    }else{
                        sort_value = "infra";
                        shortByButtonState(view);
                    }
                    break;
                case R.id.btn_needs:
                    if(btn_needs.isSelected()){
                        sort_value = "";
                        shortByButtonState(null);
                    }else{
                        sort_value = "needs";
                        shortByButtonState(view);
                    }
                    break;
                case R.id.btn_lofestyle:
                    if(btn_lofestyle.isSelected()){
                        sort_value = "";
                        shortByButtonState(null);
                    }else {
                        sort_value = "lifestyle";
                        shortByButtonState(view);
                    }
                    break;
                case R.id.btn_returns:
                    if(btn_returns.isSelected()){
                        sort_value = "";
                        shortByButtonState(null);
                    }else{
                        sort_value = "returnsval";
                        shortByButtonState(view);
                    }
                    break;
                case R.id.btn_1y:
                    status_possession = "6m";
                    possessionButtonState(view);
                    break;
                case R.id.btn_2y:
                    possessionButtonState(view);
                    status_possession = "1y";
                    break;
                case R.id.btn_3y:
                    status_possession = "2y";
                    possessionButtonState(view);
                    break;
                case R.id.btn_4y:
                    status_possession = "3y";
                    possessionButtonState(view);
                    break;
                case R.id.btn_5y:
                    status_possession = "4y";
                    possessionButtonState(view);
                    break;
                case R.id.btn_6y:
                    status_possession = "5y";
                    possessionButtonState(view);
                    break;
                case R.id.btn_residential:
                    if(BMHConstants.SELECTED_PROPERTY_TYPE == BMHConstants.NONE)
                        residentialCommercialViewState(view);
                    break;
                case R.id.btn_commercial:
                    if(BMHConstants.SELECTED_PROPERTY_TYPE == BMHConstants.NONE)
                        residentialCommercialViewState(view);
                    break;
                case R.id.back_button:
                    //setVisibility(View.GONE);
                    //Intent intent = new Intent(ProjectsListActivity.this, ProjectMapActivity.class);
                    //finish();
                    break;
			/*	case R.id.tv_reset_filter:
					resetFilters();
					break;*/
                case R.id.btn_apply_filter:
                    HashMap<String,String> filterParamsMap = getFilterParamsMap();
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    for (Map.Entry<String, String> entry : defaultParamsMap.entrySet()) {
                        mIntentDataObject.putData(entry.getKey(),entry.getValue());
                    }
                    for (Map.Entry<String, String> entry : filterParamsMap.entrySet()) {
                        mIntentDataObject.putData(entry.getKey(),entry.getValue());
                    }

                    mIntentDataObject.setObj(selectedBuilders);
                    Intent filterIntent = getIntent();

                    filterIntent.putExtra(IntentDataObject.OBJ,mIntentDataObject);
                    filterIntent.putExtra("filterkey","filterkey");
                    setResult(RESULT_OK, filterIntent);
                    finish();
                    break;
                case R.id.offers:
                    offer_value = "Discount";
                    break;
                case R.id.rl_select_builder:
                    if(defaultParamsMap == null || defaultParamsMap.get(ParamsConstants.BUILDER_ID) == null){
                        Intent intent = new Intent(FilterActivityActivity.this, BuilderActivity.class);
                        intent.putExtra("selectedBuilders",selectedBuilders);
                        startActivityForResult(intent,SELECT_BUILDER_REQ);
                    }
                    break;

                case R.id.rl_select_city:
                    if(BMHConstants.cityDataModels == null) {
                        getCities();
                    }else{
                        Intent intent = new Intent(FilterActivityActivity.this, SearchCityActivity.class);
                        Bundle information = new Bundle();
                        information.putSerializable(BMHConstants.CITY_KEY, BMHConstants.cityDataModels);
                        intent.putExtra(BMHConstants.BUNDLE,information);
                        startActivityForResult(intent,SELECT_CITY_REQ);
                    }
                    break;

                case R.id.rl_select_micro:
                    if(selectedCity != null) {
                        if(BMHConstants.cityIdMicroListMap != null && BMHConstants.cityIdMicroListMap.get(selectedCity.getCity_id()) != null){
                            Intent intent = new Intent(FilterActivityActivity.this, SearchMicroActivity.class);
                            Bundle information = new Bundle();
                            information.putSerializable(BMHConstants.MICRO_KEY, BMHConstants.cityIdMicroListMap.get(selectedCity.getCity_id()));
                            intent.putExtra(BMHConstants.BUNDLE,information);
                            startActivityForResult(intent,SELECT_MICRO_REQ);
                        }else{
                            getMicroMarket();
                        }
                    }else{
                        showToast("Please select any City");
                    }
                    break;

                case R.id.rl_select_sector:
                    if(selectedCity != null && selectedMicroMarket != null) {
                        if(BMHConstants.microIdSectorListMap != null && BMHConstants.microIdSectorListMap.get(selectedMicroMarket.getMicromarket_id()) != null){
                            Intent intent = new Intent(FilterActivityActivity.this, SearchSectorActivity.class);
                            Bundle information = new Bundle();
                            information.putSerializable(BMHConstants.SECTOR_KEY, BMHConstants.microIdSectorListMap.get(selectedMicroMarket.getMicromarket_id()));
                            intent.putExtra(BMHConstants.BUNDLE,information);
                            startActivityForResult(intent,SELECT_SECTOR_REQ);
                        }else {
                            getSectors();
                        }
                    }else{
                        showToast("Please select any Micro Market");
                    }
                    break;

                default:
                    break;
            }

        }
    };


    private HashMap<String, String> getFilterParamsMap(){
        HashMap <String,String> map = new HashMap<>();
        //For project status
        if(chk_new_launch.isSelected() || chk_under_construction.isSelected() || chk_readytomove.isSelected() ){
            String status = "";
            if(chk_new_launch.isSelected()){
                status = "new launch,";
            }if(chk_under_construction.isSelected()){
                status = "under construction,";
            }if(chk_readytomove.isSelected()){
                status = "ready to move,";
            }
            status = Utils.removeLastComma(status);
            map.put("project_status",status);
        }
        // For sort
        if(!sort_value.equals("")){
            if (sort_value.equals("newest")) {
                map.put("newest", sort_value);
            }else if (sort_value.equals("priceltoh")) {
                map.put("priceltoh", sort_value);
            }else if (sort_value.equals("pricehtol")) {
                map.put("pricehtol", sort_value);
            }else if (sort_value.equals("rating")) {
                map.put("rating", sort_value);
            }else if (sort_value.equals("infra")) {
                map.put("infra", sort_value);
            }else if (sort_value.equals("needs")) {
                map.put("needs", sort_value);
            }else if (sort_value.equals("lifestyle")) {
                map.put("lifestyle", sort_value);
            }else if (sort_value.equals("Possessionltoh")) {
                map.put("possessionltoh", sort_value);
            }else if (sort_value.equals("Possessionhtol")) {
                map.put("possessionhtol", sort_value);
            }else if (sort_value.equals("pricePSFhtol")) {
                map.put("psfhtol", sort_value);
            }else if (sort_value.equals("pricePSFltoh")) {
                map.put("psfltoh", sort_value);
            }else if (sort_value.equals("returnsval")) {
                map.put("returnsval", sort_value);
            }
        }
        //For Possession in
        if (!status_possession.equals("")) {
            map.put("posession", status_possession);
        }
        if (selectedBuilderNames.length() > 1) {
            map.put("builders", selectedBuilderNames);
            map.put("builderIds", selectedBuilderIds);
            //defaultParamsMap.remove("builder_id");
        }
        // For bhk selection
        //if(btn_residential.isSelected()){
        if(chk_one_bhk.isChecked() ||chk_two_bhk.isChecked() || chk_three_bhk.isChecked()
                || chk_four_bhk.isChecked() || chk_five_bhk.isChecked() || chk_five_pluse_bhk.isChecked()){
            String bhk_value = "";
            if(chk_one_bhk.isChecked()){
                bhk_value += "1bhk,";
            }if(chk_two_bhk.isChecked()){
                bhk_value += "2bhk,";
            }if(chk_three_bhk.isChecked()){
                bhk_value += "3bhk,";
            }if(chk_four_bhk.isChecked()){
                bhk_value += "4bhk,";
            }if(chk_five_bhk.isChecked()){
                bhk_value += "5bhk,";
            }if(chk_five_pluse_bhk.isChecked()){
                bhk_value += "5plusbhk,";
            }
            bhk_value = Utils.removeLastComma(bhk_value);
            map.put("bhk", bhk_value);
        }
        //}
        // For Residential and commercial
        String residential_value = "";
        if(btn_residential.isSelected()){
            if(chk_resi_flat.isChecked() || chk_resi_builder.isChecked() || chk_resi_plots.isChecked() || chk_resi_house_villa.isChecked()){
                if(chk_resi_flat.isChecked()){
                    residential_value+= "Flat," ;
                }if(chk_resi_builder.isChecked()){
                    residential_value += "Builder Floor,";
                }if(chk_resi_plots.isChecked()){
                    residential_value += "Plot,";
                }if(chk_resi_house_villa.isChecked()){
                    residential_value += "House Villa,";
                }
                residential_value = Utils.removeLastComma(residential_value);
                map.put(ParamsConstants.P_TYPE, residential_value);
            }
        }else if (btn_commercial.isSelected()){
            if(chk_com_shops.isChecked() || chk_com_office.isChecked() || chk_com_service_apartment.isChecked()){
                if(chk_com_shops.isChecked()){
                    residential_value+= "Shop," ;
                }if(chk_com_office.isChecked()){
                    residential_value+= "Office," ;
                }if(chk_com_service_apartment.isChecked()) {
                    residential_value += "Apartment,";
                }
                residential_value = Utils.removeLastComma(residential_value);
                map.put(ParamsConstants.P_TYPE, residential_value);
            }
        }else{
            //TODO: do nothing.
        }
        // For Amenities
        if(chk_gas.isChecked()|| chk_lift.isChecked() || chk_parking.isChecked() || chk_gym.isChecked()
                || chk_security.isChecked()|| chk_wifi.isChecked()|| chk_ac.isChecked() || chk_swimming.isChecked()
                ||chk_home_automation.isChecked() ||chk_power_backup.isChecked()){
            String lyfeStyle = "";
            if(chk_gas.isChecked()){
                lyfeStyle += "Piped_Gas,";
            }if(chk_lift.isChecked()) {
                lyfeStyle += "Lift,";
            }if(chk_parking.isChecked()) {
                lyfeStyle += "Podium_Parking,";
            }if(chk_gym.isChecked()){
                lyfeStyle += "Gymnasium,";
            }if(chk_security.isChecked()) {
                lyfeStyle += "24*7_Security,";
            }if(chk_wifi.isChecked()) {
                lyfeStyle += "Central_Wi-fi,";
            }if(chk_treated_water.isChecked()) {
                lyfeStyle += "Treated_Water_Supply,";
            }if(chk_party_hall.isChecked()) {
                lyfeStyle += "Party_Hall,";
            }if(chk_ac.isChecked()){
                lyfeStyle += "AC_in_Lobby,";
            }if(chk_swimming.isChecked()) {
                lyfeStyle += "Swimming,";
            }if(chk_home_automation.isChecked()){
                lyfeStyle += "Home_Automation,";
            }if(chk_power_backup.isChecked()){
                lyfeStyle += "Power_Backup,";
            }
            lyfeStyle = Utils.removeLastComma(lyfeStyle);
            map.put("lifestyles", lyfeStyle);
        }
        // For Price Range
        if (price_range_min_value.length() > 0) {
            map.put("min-p", price_range_min_value);
        }if (price_range_max_value.length() > 0) {
            map.put("max-p", price_range_max_value);
        }
        // For Area Range
        if (built_area_min_value.length() > 0) {
            map.put("min_area_range", built_area_min_value);
        }
        if (built_area_max_value.length() > 0) {
            map.put("max_area_range", built_area_max_value);
        }
        // For price PSF Range
        if (price_psf_min_value.length() > 0) {
            map.put("min_psf", price_psf_min_value);
        }
        if (price_psf_max_value.length() > 0) {
            map.put("max_psf", price_psf_max_value);
        }if(special_category != null && special_category.length() > 0){
            map.put("special_category", special_category);
        }

        //For city , micro market and sector
        if(selectedCity != null){
            map.put(ParamsConstants.CITY_ID,selectedCity.getCity_id());
            map.put(ParamsConstants.CITY_NAME,selectedCity.getCity_name());
        }else{
            if(map.containsKey(ParamsConstants.CITY_ID)){
                map.remove(ParamsConstants.CITY_ID);
                map.remove(ParamsConstants.CITY_NAME);
            }if(map.containsKey(ParamsConstants.LOCATION)){
                map.remove(ParamsConstants.LOCATION);
                map.remove(ParamsConstants.LOCATION_NAME_TEMP);
            }if(map.containsKey(ParamsConstants.SUB_LOCATION_ID)){
                map.remove(ParamsConstants.SUB_LOCATION_ID);
                map.remove(ParamsConstants.SUB_LOCATION_NAME_TEMP);
            }
        }
        if(selectedMicroMarket != null){
            map.put(ParamsConstants.LOCATION,selectedMicroMarket.getMicromarket_id());
            map.put(ParamsConstants.LOCATION_NAME_TEMP,selectedMicroMarket.getMicromarket_name());
        }else{
            if(map.containsKey(ParamsConstants.LOCATION)){
                map.remove(ParamsConstants.LOCATION);
                map.remove(ParamsConstants.LOCATION_NAME_TEMP);
            }if(map.containsKey(ParamsConstants.SUB_LOCATION_ID)){
                map.remove(ParamsConstants.SUB_LOCATION_ID);
                map.remove(ParamsConstants.SUB_LOCATION_NAME_TEMP);
            }
        }if(selectedSector != null){
            map.put(ParamsConstants.SUB_LOCATION_ID,selectedSector.getSector_id());
            map.put(ParamsConstants.SUB_LOCATION_NAME_TEMP,selectedSector.getSector_name());
        }else{
            if(map.containsKey(ParamsConstants.SUB_LOCATION_ID)){
                map.remove(ParamsConstants.SUB_LOCATION_ID);
                map.remove(ParamsConstants.SUB_LOCATION_NAME_TEMP);
            }
        }

        return map;
    }
    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.chk_new_launch:
                    if(chk_new_launch.isSelected()){
                        buildingStatusState(null);
                    }else {
                        buildingStatusState(chk_new_launch);
                    }
                    break;
                case R.id.chk_under_construction:
                    if(chk_under_construction.isSelected()){
                        buildingStatusState(null);
                    }else {
                        buildingStatusState(chk_under_construction);
                    }
                    break;
                case R.id.chk_readytomove:
                    if(chk_readytomove.isSelected()){
                        buildingStatusState(null);
                    }else {
                        buildingStatusState(chk_readytomove);
                    }
                    break;
                case R.id.chk_resi_plots:
                    if(chk_resi_plots.isChecked() && (!chk_resi_builder.isChecked() && !chk_resi_flat.isChecked()
                            && !chk_resi_house_villa.isChecked())){
                        rl_bhk_container.setVisibility(View.GONE);
                    }else{
                        rl_bhk_container.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.chk_resi_flat:
                case R.id.chk_resi_builder:
                case R.id.chk_resi_house_villa:
                    if(chk_resi_plots.isChecked() && (!chk_resi_builder.isChecked() && !chk_resi_flat.isChecked()
                            && !chk_resi_house_villa.isChecked())){
                        rl_bhk_container.setVisibility(View.GONE);
                    }else{
                        rl_bhk_container.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.chk_com_shops:
                case R.id.chk_com_office:
                case R.id.chk_com_service_apartment:
                    if(chk_com_service_apartment.isChecked() ){
                        rl_bhk_container.setVisibility(View.VISIBLE);
                    }else{
                        rl_bhk_container.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("hh location alert fragment acti result");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_BUILDER_REQ && resultCode == RESULT_OK) {
            selectedBuilders = (ArrayList<Builder>)data.getSerializableExtra("selectedBuilders");
            setSelectedBuilders(selectedBuilders);
        }else if (requestCode == SELECT_CITY_REQ && resultCode == RESULT_OK) {
            selectedCity = (CityDataModel)data.getSerializableExtra(BMHConstants.CITY_KEY);
            tv_selected_city.setText(selectedCity.getCity_name());
            tv_selected_micro.setText("All");
            tv_selected_sector.setText("All");
            selectedMicroMarket = null;
            selectedSector = null;
            if(selectedCity.getCity_id().equalsIgnoreCase("-1")){
                selectedCity = null;
            }

        }else if (requestCode == SELECT_MICRO_REQ && resultCode == RESULT_OK) {
            selectedMicroMarket = (MicroMarketData) data.getSerializableExtra(BMHConstants.MICRO_KEY);
            tv_selected_micro.setText(selectedMicroMarket.getMicromarket_name());
            tv_selected_sector.setText("All");
            selectedSector = null;
            if(selectedMicroMarket.getMicromarket_id().equalsIgnoreCase("-1")){
                selectedMicroMarket = null;
            }
        }else if (requestCode == SELECT_SECTOR_REQ && resultCode == RESULT_OK) {
            selectedSector = (SectorData)data.getSerializableExtra(BMHConstants.SECTOR_KEY);
            tv_selected_sector.setText(selectedSector.getSector_name());
            if(selectedSector.getSector_id().equalsIgnoreCase("-1")){
                selectedSector = null;
            }
        }

    }

    private void setSelectedBuilders(ArrayList<Builder> selectedBuilders){
        if(selectedBuilders == null || selectedBuilders.size() == 0){
            tv_selected_builders.setText("All");
            Log.i(TAG, "Selected Builders is null");
        } else {
            ArrayList<String> builderText = new ArrayList<>();
            ArrayList<String> builderIds = new ArrayList<>();
            for (Builder tempBuilder : selectedBuilders) {
                if(tempBuilder.isChecked()){
                    builderText.add(tempBuilder.getName());
                    builderIds.add(tempBuilder.getId());
                }
            }
            if(builderText.size() == 0){
                tv_selected_builders.setText("All");
            }else {
                String tempBuilderIds = TextUtils.join(",", builderIds);
                selectedBuilderIds = tempBuilderIds;
                String tempBuilderNames = TextUtils.join(",", builderText);
                selectedBuilderNames = tempBuilderNames;
                tv_selected_builders.setText(tempBuilderNames);
            }
        }
    }

    private void resetFilters(){
        sort_value = "";
        status_possession = "";

        price_range_min_value = "";
        price_range_max_value = "";
        MIN_PRICE = 0;
        MAX_PRICE =  priceLabels.length - 1;
        seekBarPrice.setSelectedMinValue(MIN_PRICE);
        seekBarPrice.setSelectedMaxValue(MAX_PRICE);
        Price_left.setText(priceLabels[MIN_PRICE]);
        Price_right.setText(priceLabels[MAX_PRICE]);

        built_area_min_value = "";
        built_area_max_value = "";
        MIN_AREA = 0;
        MAX_AREA = builtUpAreaLabels.length - 1;
        seekBarBuildUp.setSelectedMinValue(MIN_AREA);
        seekBarBuildUp.setSelectedMaxValue(MAX_AREA);
        tvBuildUpAreaLeft.setText(builtUpAreaLabels[MIN_AREA]);
        tvBuildUpAreaRignt.setText(builtUpAreaLabels[MAX_AREA]);

        price_psf_min_value = "";
        price_psf_max_value = "";
        MIN_PSF = 0;
        MAX_PSF = psfLabels.length - 1;
        seekBarPsf.setSelectedMinValue(MIN_PSF);
        seekBarPsf.setSelectedMaxValue(MAX_PSF);
        tvpsf_left.setText(psfLabels[MIN_PSF]);
        tvpsf_right.setText(psfLabels[MAX_PSF]);



        if(defaultParamsMap != null && defaultParamsMap.get(ParamsConstants.BUILDER_ID) != null){
            //TODO: do nothing
        }else{
            selectedBuilderNames = "";
            if(selectedBuilders != null)
                selectedBuilders.clear();
        }
        selectedCity = null;
        selectedMicroMarket = null;
        selectedSector = null;
        tv_selected_city.setText("All");
        tv_selected_micro.setText("All");
        tv_selected_sector.setText("All");
        chk_new_launch.setSelected(false);
        chk_under_construction.setSelected(false);
        chk_readytomove.setSelected(false);
        shortByButtonState(null);
        possessionButtonState(null);
        buildingStatusState(null);

        //chk_offer.setChecked(false);
        chk_resi_flat.setChecked(false);
        chk_resi_builder.setChecked(false);
        chk_resi_plots.setChecked(false);
        chk_resi_house_villa.setChecked(false);
        chk_com_shops.setChecked(false);
        chk_com_office.setChecked(false);
        chk_com_service_apartment.setChecked(false);

        resetBHKState();

        chk_gas.setChecked(false);
        chk_lift.setChecked(false);
        chk_parking.setChecked(false);
        chk_gym.setChecked(false);
        chk_security.setChecked(false);
        chk_wifi.setChecked(false);
        chk_ac.setChecked(false);
        chk_swimming.setChecked(false);
        chk_home_automation.setChecked(false);
        chk_power_backup.setChecked(false);
        defaultUIState(defaultParamsMap);
    }
    private void resetBHKState(){
        chk_one_bhk.setChecked(false);
        chk_two_bhk.setChecked(false);
        chk_three_bhk.setChecked(false);
        chk_four_bhk.setChecked(false);
        chk_five_bhk.setChecked(false);
        chk_five_pluse_bhk.setChecked(false);
    }


    private void getCities(){
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_CITY);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_CITY));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String dataParams = "builder_id="+BMHConstants.CURRENT_BUILDER_ID;
        if(dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(FilterActivityActivity.this,mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void getMicroMarket(){
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_MICROMARKET);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_MICROMARKET));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String dataParams = "builder_id="+BMHConstants.CURRENT_BUILDER_ID + "&city=" + selectedCity.getCity_id();
        if(dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(FilterActivityActivity.this,mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void getSectors(){
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_SECTOR);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_SECTOR));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String dataParams = "builder_id="+BMHConstants.CURRENT_BUILDER_ID + "&city=" + selectedCity.getCity_id() + "&location="+selectedMicroMarket.getMicromarket_id();
        if(dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(FilterActivityActivity.this,mOnCancelListener);
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
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                Log.i(TAG,mBean.getJson());
                switch (mBean.getApiType()) {
                    case GET_CITY:
                        if(mBean.getJson() != null) {
                            final CityRespData mCityRespData = (CityRespData) com.jsonparser.JsonParser.convertJsonToBean(APIType.GET_CITY, mBean.getJson());
                            if (mCityRespData != null) {
                                if(mCityRespData.isSuccess() && mCityRespData.getData() != null){
                                    BMHConstants.cityDataModels = mCityRespData.getData();
                                    Intent intent = new Intent(FilterActivityActivity.this, SearchCityActivity.class);
                                    Bundle information = new Bundle();
                                    information.putSerializable(BMHConstants.CITY_KEY, BMHConstants.cityDataModels);
                                    intent.putExtra(BMHConstants.BUNDLE,information);
                                    startActivityForResult(intent,SELECT_CITY_REQ);

                                }else{
                                    showToast(getString(R.string.something_went_wrong));
                                    BMHConstants.cityDataModels = null;
                                }

                            }else{
                                showToast(getString(R.string.something_went_wrong));
                                BMHConstants.cityDataModels = null;
                            }
                        }else{
                            BMHConstants.cityDataModels = null;
                        }

                        break;

                    case GET_MICROMARKET:
                        if(mBean.getJson() != null) {
                            final MicroMarketRespData microMarketRespData = (MicroMarketRespData) com.jsonparser.JsonParser.convertJsonToBean(APIType.GET_MICROMARKET, mBean.getJson());
                            if (microMarketRespData != null) {
                                if(microMarketRespData.isSuccess() && microMarketRespData.getData() != null){
                                    BMHConstants.cityIdMicroListMap.put(selectedCity.getCity_id(),microMarketRespData.getData());
                                    Intent intent = new Intent(FilterActivityActivity.this, SearchMicroActivity.class);
                                    Bundle information = new Bundle();
                                    information.putSerializable(BMHConstants.MICRO_KEY, microMarketRespData.getData());
                                    intent.putExtra(BMHConstants.BUNDLE,information);
                                    startActivityForResult(intent,SELECT_MICRO_REQ);

                                }else{
                                    showToast(getString(R.string.something_went_wrong));
                                }

                            }else{
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }else{
                            showToast(getString(R.string.something_went_wrong));
                        }

                        break;

                    case GET_SECTOR:
                        if(mBean.getJson() != null) {
                            final SectorRespData mSectorRespData = (SectorRespData) com.jsonparser.JsonParser.convertJsonToBean(APIType.GET_SECTOR, mBean.getJson());
                            if (mSectorRespData != null) {
                                if(mSectorRespData.isSuccess() && mSectorRespData.getData() != null){
                                    BMHConstants.microIdSectorListMap.put(selectedMicroMarket.getMicromarket_id(),mSectorRespData.getData());
                                    Intent intent = new Intent(FilterActivityActivity.this, SearchSectorActivity.class);
                                    Bundle information = new Bundle();
                                    information.putSerializable(BMHConstants.SECTOR_KEY, mSectorRespData.getData());
                                    intent.putExtra(BMHConstants.BUNDLE,information);
                                    startActivityForResult(intent,SELECT_SECTOR_REQ);

                                }else{
                                    showToast(getString(R.string.something_went_wrong));
                                }

                            }else{
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }else{
                            showToast(getString(R.string.something_went_wrong));
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
