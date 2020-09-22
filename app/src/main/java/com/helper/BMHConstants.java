package com.helper;

import com.sp.R;
import com.model.CityDataModel;
import com.model.MicroMarketData;
import com.model.SectorData;

import java.util.ArrayList;
import java.util.HashMap;

public class BMHConstants {
    public static final int BROKERAGE_MAX_TEXT_LENGTH = 65;
    public static final String DB_NAME = "SP-db";
    public static final String APP_ROOT_FOLDER = "SP_APP";
    public static final String DIR_CALL_RECORDING = "";
    public static final String MOBILE_REGEX = "^(?:9867|9975|9989)[0-9]{6}$";
    public static final boolean IS_STAGING = false;
    public static final String IS_BACK_FROM_UPDATE = "Lead_update";
    public static final String IS_REFRESH_ACTIVITY = "accept_lead";
    public static final int ACCESS_FINE_LOCATION = 1002;
    public static final int PICK_LOCATION = 1245;
    public static final int PICK_LOCATION_LIST = 1247;
    public static final int PICK_LOCATION_FILTER = 1248;
    public static final int PERMISSION_CODE = 23;
    public static final int PERMISSION_CODE_CALL = 28;
    public final static int GALLERY_PERMISSION_CODE = 24;
    public static final int MULTIPLE_PERMISSIONS = 10;
    public static final int CAMERA_STORAGE_PERMISSION = 15;
    //public static final String CITY_ID = "city_id";
    public static final String DEVICE_TYPE = "android";
    public static final int PICK_PROJECT_LIST = 127;
    public static final int FILTER_ACTION = 120;
    public static final int FILTER_PROJ = 1;
    public static final int CRAOUSEL_IMAGE_WIDTH = 200;
    public static final int CRAOUSEL_IMAGE_HEIGHT = 120;
    public static final int LIST_IMAGE_WIDTH = 200;
    public static final int PAGER_IMAGE_HEIGHT = 250;
    public static final int ANIM_TIME = 20;
    public static final int ALARM_DURATION = 5;
    public static final int ALARM_REPEAT_DURATION = 120000;
    public static final int ALARM_BEFORE_ONE_HOUR =3600000;
    public static final int ALARM_AFTER_TWO_HOUR = 2 * 60 * 60 * 1000;
    public static final int FIVE_MINUTES_DURATION = 300000;
    public static final int ALARM_BEFORE_24_HOUR = 86400000;
    public static final String ASM_DESIGNATION = "1";
    public static final String SP_DESIGNATION = "0";
    public static final int GAP_TIME = 5000;
    public static final String PREF_NAME = "BMH";
    public static final String PREF_TAB_NAME = "tab_name";
    public static final String CITY_JSON = "bmh.city.json";
    public static final String BUILDER_JSON = "bmh.builder.json";
    public static final String PROJECTS_JSON = "bmh.Projects.json";
    public static final String P_TYPE = "project.type";
    public static final String UNIT_TYPE = "unit.type";
    public static final String USERID_KEY = "userid";
    public static final String USER_DESIGNATION = "designation";
    public static final String MOBILE_KEY = "mobile";
    public static final String IS_PRE_SALES = "pre_sales";
    public static final String USERNAME_KEY = "username";
    public static final String USER_EMAIL = "useremail_bmh";
    public static final String TOKEN_MONEY_KEY = "isTokenMoneyPaid";
    public static final String DEFAULT_VALUE = "";
    public static final String CITYNAME = "cityname";
    public static final String CITYID = "cityid";
    public static final String IS_DOWNLOADING_KEY = "isdownloading";
    public static final int PLACE_HOLDER = R.drawable.app_icon_img;
    public static final int NO_IMAGE = R.drawable.no_image;
    public static final String REMARK = "remark";
    public static final String USER_ID_THIRD_PARTY = "USER_ID_THIRD_PARTY";
    public static final String USER_FNAME_THIRD_PARTY = "USER_FNAME_THIRD_PARTY";
    public static final String USER_EMIL_THIRD_PARTY = "USER_EMIL_THIRD_PARTY";
    public static final String THIRD_PARTY = "THIRD_PARTY";
    public static final String GCM_REG_ID = "gcm_registration_id";
    public static final String NOTIFICATION = "Notifications";
    public static final int TYPE_IMAGES = 1;
    public static final int TYPE_VIDEOS = 2;
    public static final int TYPE_PDF = 3;
    public static final int TYPE_COUNT = 4;

    public static final int SUB_TYPE_PROJ_VIDEO = 1;
    public static final int SUB_TYPE_CONSTRUCTION_VIDEO = 2;
    public static final int SUB_TYPE_TV_VIDEO = 3;

    public static final String ACTION_DOWNLOAD_COMPLETE = "action_download_complete";

    public static final int TRANSPORT = 10;
    public static final int NEEDS = 20;
    public static final int LANDMARK = 30;
    public static final int NEWALL = 50;

    public static final int EXPLORELOCATION = 40;

    public static final String G_TYPE = "gallery.type";

    public static final String PLACE_LANDMARKS = "place_landmarks";
    public static final String PLACE_AIRPORT = "place_airport";
    public static final String PLACE_RAILWAYS = "place_railways";
    public static final String PLACE_BUS_STAND = "place_bus_stands";
    public static final String PLACE_TEXI_SERVICE = "place_texi_service";

    public static final String PLACE_SCHOOL = "place_school";
    public static final String PLACE_HOSP = "place_hospital";
    public static final String PLACE_SHOPPING_MALL = "place_shoppingMall";
    public static final String PLACE_DEPARTMENT_STORES = "place_department_stores";
    public static final String PLACE_PHARMACIES = "place_pharmacies";
    public static final String PLACE_ATM = "place_atm";

    public static final String PLACE_RESTAURANT = "place_resturant";
    public static final String PLACE_BANK = "place_bank";
    public static final String PLACE_PARK = "place_park";

    public static final String PLACE_HOTELS = "place_hotels";
    public static final String PLACE_THEATER = "place_theater";
    public static final String PLACE_NIGHT_CLUB = "place_nightclub";

    // public static final String AYANA_YOUTUBEID = "pgygXJ7p4Y0";
    public static final String HOME_ACTI = "com.builder.SearchPropertyActivity";

    public static final int NONE = -1;
    public static final int RESIDENTIAL_PROPERTY = 1;
    public static final int COMMERCIAL_PROPERTY = 2;
    public static final String VALUE = "value";
    public static final String KEY = "key";
    public static final String CUSTOME_CARE = "tel:+91 9599789982";
    public static final String ADD_ENQUIRY = "add_enquiry";
    public static final long USER_TRACKING_FREQUENCY = 60 * 1000;// millisecond (60 second) ;
    public static final String BROKER_CODE = "broker_code";
    public static final String USER_IMAGE_URL = "user_image";
    public static final String IS_VERIFIED_USER = "is_verified_user";
    public static final String FIRM_NAME = "firm_name";
    public static final String RM_EMAIL_ID = "rm_email_id";
    public static final String RM_MOBILE_NO = "rm_mobile_no";
    public static final String RM_NAME = "rm_name";
    public static final String RM_CODE = "rm_code";
    public static final String SP_CODE = "sp_code";
    public static final String ACCOUNT_TYPE = "com.sp";
    public static final String ACCOUNT_NAME = "SP Sync Adapter";
    public static final String AUTHORITY = "com.sp.syncadapter";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an My Routes account";
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an My Routes account";
    public static int SELECTED_PROPERTY_TYPE = NONE;
    public static final double INDIA_LAT = 29.695214;
    public static final double INDIA_LNG = 76.832675;

    //LIVE
    public static String CURRENT_BUILDER_ID = "40";
    //DEMO
   /* public static String CURRENT_BUILDER_ID = "1";*/
    public static String BUILDER_NAME = "RAHEJA";
    public static final String BUILDER_ID_KEY = "builder_id";
    public static final String BUILDER_NAME_KEY = "builder_name";


    public static final String BUNDLE = "bundle";
    public static final String PROJECT_KEY = "project_key";
    public static final String PROJECT_NAME = "project_name";
    public static final String PROJECT_ID = "project_id";

    public static final String CITY_KEY = "city_key";
    public static final String CITY_NAME = "city_name";
    public static final String CITY_ID = "city_id";

    public static final String MICRO_KEY = "micro_key";
    public static final String MICRO_NAME = "micro_name";
    public static final String MICRO_ID = "micro_id";

    public static final String SECTOR_KEY = "sector_key";
    public static final String SECTOR_NAME = "sector_name";
    public static final String SECTOR_ID = "sector_id";
    public static String BLOG_URL = "";
    public static final String BROKER = "broker";
    public static final String CUSTOMER = "customer";
    public static final String SALES_PERSON = "salesperson";
    public static final String SP_MODEL = "sp_model";
    public static final String SP_DETAIL_DATA = "sp_details_model";
    public static final String ASM_MODEL_DATA = "asm_model_data";
    public static final String ASM_DETAIL_DATA = "asm_details_data";
    public static final String SELECTED_TAB_NAME = "tab_name";
    public static final String PROJECT_LIST = "project_list";
    public static final String LEAD_LIST = "lead_list";
    public static final String NOT_INTERESTED_LIST = "not_interested_list";
    public static final String CLOSURE_LIST = "closure_list";
    public static final String BROKER_LIST = "broker_list";
    public static final String ENQUIRY_ID = "enquiry_id";
    public static final String ALARM_INDEX = "index_id";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String MOBILE_NO = "mobile_no";
    public static final String LEAD_UPDATE_STATUS = "lead_update_status";
    public static final String TIME_STAMP = "time_stamp";
    public static final String RECORDING_FILE_PATH = "uploadedfile";
    public static final String ALERT_DIALOG_TAG = "alert_dialog_tag";
    public static final String REQUEST_SUCCESS = "success";
    public static final String PATH = "path";
    public static final String ASSIGN_TYPE = "assign_type";
    public static final String CURRENT_PHASE = "current_phase";
    public static final String LEAD_PHASE_SALES = "sales";
    public static final String LEAD_PHASE_PRE_SALES = "pre_sales";
    public static final String CALL_REC_EXTENSION_MP4 = ".mp4";
    public static final String CALL_REC_EXTENSION_MP3 = ".mp3";
    public static final String CALL_REC_SUB_DIR = "Call-Recordings";

    public static ArrayList<CityDataModel> cityDataModels;
    //public static ArrayList<MicroMarketData> microMarketDatas;
    //public static ArrayList<SectorData> sectorDatas;
    public static HashMap<String, ArrayList<MicroMarketData>> cityIdMicroListMap = new HashMap<>();
    public static HashMap<String, ArrayList<SectorData>> microIdSectorListMap = new HashMap<>();
    public static final java.lang.String FAQ_URL = "https://demo.bookmyhouse.co/BMH/api_v.1.0/faq.php";
  //  public static String HEADER = "d3430de096c54658b5c3bf4f10f6af4da8a2fba6";
  public static String HEADER = "d3430de096c54658b5c3bf4f10f6af4da8a2fba6";
 //   d3430de096c54658b5c3bf4f10f6af4da8a2fba6
    public static String IOS = "iOS";

    public static int VERIFIED = 1;
    public static final int TERMS_AND_CONDITION = 0, BLOG = 1, FAQ = 2, PROJECT_LIST_ = 3, PROJECT_DETAIL = 4, UNIT_Details = 5, UNIT_LIST = 6, PAYMENT = 7, BROKER_PAYMENT = 10;
    public static String NOTIFICAYION_TYPE_BLOG = "BLOG";
    public static String NOTIFICAYION_TYPE_FAQ = "FAQ";
    public static String NOTIFICAYION_TYPE_PROJECT_LIST = "PROJECT_LIST";
    public static String NOTIFICAYION_TYPE_PROJECT_DETAIL = "PROJECT_DETAIL";
    public static String NOTIFICAYION_TYPE_UNIT_Details = "UNIT_Details";
    public static String NOTIFICAYION_TYPE_UNIT_LIST = "UNIT_LIST";
    public static String NOTIFICAYION_TYPE_PAYMENT = "PAYMENT";
    public static String NOTIFICAYION_TYPE_BROKER_PAYMENT = "BROKER_PAYMENT";
    public static String UPDATE_FLAG = "update_flag";
    public static String LEAD_TYPE_BROKER = "1";
    public static int ACTION_ACCEPT = 1;
    public static int ACTION_REJECT = 0;
}
