package com.jsonparser;

import android.util.Log;

import com.AppEnums.APIType;
import com.ChatServerModel.CreateChannel;
import com.ChatServerModel.InitialMessageList;
import com.ChatServerModel.RegisterNewUser;
import com.ChatServerModel.SendMessage;
import com.VO.AllCommentsVO;
import com.VO.CommentsVO;
import com.VO.MediaGellaryVO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.model.AcceptOfferStatus;
import com.model.AddLeadData;
import com.model.AddLeadRespModel;
import com.model.AsmSalesLeadDetailModel;
import com.model.AsmSalesModel;
import com.model.BaseRespModel;
import com.model.BhkUnitSpecification;
import com.model.BlogDetails;
import com.model.BlogSubscibe;
import com.model.BrokerProfileInfoRespModel;
import com.model.BrokersRespDataModel;
import com.model.ChatTagsRespModel;
import com.model.ChequeDDPaymentRespModel;
import com.model.CityRespData;
import com.model.ClosureDetailsModel;
import com.model.ContactUsRespModel;
import com.model.CorporateActivityModel;
import com.model.CorporateModel;
import com.model.Data;
import com.model.Details;
import com.model.EnquiryProjectRespData;
import com.model.FavBlogModel;
import com.model.GalleryRespData;
import com.model.GetOfferCodeRespModel;
import com.model.HotProjectsCountModel;
import com.model.InventoryRespModel;
import com.model.LocalitySearchRespModel;
import com.model.LocationHeatmapRespModel;
import com.model.LoginRespData;
import com.model.LotteryDetails;
import com.model.MicroMarketRespData;
import com.model.MyChatUnitsRespModel;
import com.model.MyTransactionsRespModel;
import com.model.NotificationsListRespModel;
import com.model.OfferStatusRespModel;
import com.model.PayUResponseModel;
import com.model.PreSalesAsmModel;
import com.model.PreSalesSpModel;
import com.model.ProfileNameUpdatRespModel;
import com.model.ProjectDetailRespModel;
import com.model.ProjectsListRespModel;
import com.model.RecentBlogModel;
import com.model.RegisterUserRespModel;
import com.model.SearchRespBean;
import com.model.SectorRespData;
import com.model.SiteVisitTimeRespModel;
import com.model.SubLocHeatMRespModel;
import com.model.SubLocationRespModel;
import com.model.SubmitOfferJsonModel;
import com.model.UpdatePersonalDetailRespModel;
import com.model.UploadRegistrationDoc;
import com.model.VerifyCodeRespModel;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Naresh on 06-Dec-16.
 */

public class JsonParser {

    public static final String TAG = JsonParser.class.getSimpleName();

    public static Object convertJsonToBean(APIType apiType, String json) {
        Object obj = null;
        if (apiType == null || json == null) {
            Log.e(TAG, "apiType or json is null");
            return obj;
        }
        Gson mGson = new Gson();
        try {
            switch (apiType) {
                case SEARCH_RESPONSE:
                    obj = mGson.fromJson(json, SearchRespBean.class);
                    break;
                case G_LOGIN:
                    break;
                case HEATMAP:
                    obj = mGson.fromJson(json, LocationHeatmapRespModel.class);
                    break;
                case HOT_PROJECTS:
                    obj = mGson.fromJson(json, HotProjectsCountModel.class);
                    break;
                case LOCALITY_SEARCH:
                    obj = mGson.fromJson(json, LocalitySearchRespModel.class);
                    break;
                case PROJECT_DETAILS:
                    obj = mGson.fromJson(json, ProjectDetailRespModel.class);
                    break;
                case NOTIFICATIONS:
                    obj = mGson.fromJson(json, NotificationsListRespModel.class);
                    break;
                case MEDIA_GALLARY:
                    obj = mGson.fromJson(json, MediaGellaryVO.class);
                    break;
                case GET_GALLERY:
                    obj = mGson.fromJson(json, GalleryRespData.class);
                    break;
                case GET_SUBLOCATIONS:
                    obj = mGson.fromJson(json, SubLocationRespModel.class);
                    break;
                case GET_SUBLOCATIONS_HEATMAP:
                    obj = mGson.fromJson(json, SubLocHeatMRespModel.class);
                    break;
                case SEARCH_PROJECTS:
                    obj = mGson.fromJson(json, ProjectsListRespModel.class);
                    break;
                case GET_ALERT:
                case SITE_VISIT:
                case GET_ENQUERY:
                case FORGOT_PASSWORD:
                case GET_API_VERSION:
                case CONTACT_ENQUIRY:
                case SIGNUP_DONE:
                    obj = mGson.fromJson(json, BaseRespModel.class);
                    break;
                case GET_ALL_COMMENT:
                    obj = mGson.fromJson(json, AllCommentsVO.class);
                    break;
                case COMMENT:
                    obj = mGson.fromJson(json, CommentsVO.class);
                    break;
                case CLOSE_LEADS_DETAILS:
                    obj = mGson.fromJson(json, ClosureDetailsModel.class);
                    break;
                case LEAD_DETAILS:
                    obj = mGson.fromJson(json, AsmSalesLeadDetailModel.class);
                    break;
                case LEAD_DETAIL_ASM:
                    obj = mGson.fromJson(json, AsmSalesModel.class);
                    break;
                case PRE_SALES_LEAD_DETAIL:
                    obj = mGson.fromJson(json, Details.class);
                    break;
                case PRE_SALES_DATA:
                    obj = mGson.fromJson(json, PreSalesSpModel.class);
                    break;
                case PRE_SALES_ASM_MODEL:
                    obj = mGson.fromJson(json, PreSalesAsmModel.class);
                    break;
                case BHK_UNIT_SPECIFICATION:
                    Type listType = new TypeToken<List<BhkUnitSpecification>>() {
                    }.getType();
                    obj = (List<BhkUnitSpecification>) mGson.fromJson(json, listType);
                    break;
                case GET_SITEVISIT_TIME:
                    obj = mGson.fromJson(json, SiteVisitTimeRespModel.class);
                    break;

                case GET_THIRD_PARTY_LOGIN:
                case USER_LOGIN:
                    obj = mGson.fromJson(json, LoginRespData.class);
                    break;
                case LOTTERY_PROJECT:
                    obj = mGson.fromJson(json, LotteryDetails.class);
                    break;
                case VERIFY_OTP:
                    obj = mGson.fromJson(json, BaseRespModel.class);
                    break;
                case UPDATE_PERSONAL_DETAILS:
                    obj = mGson.fromJson(json, UpdatePersonalDetailRespModel.class);
                    break;
                case PAYU_RESPONSE:
                    obj = mGson.fromJson(json, PayUResponseModel.class);
                    break;
                case CHEQUE_DD_RESPONSE:
                    obj = mGson.fromJson(json, ChequeDDPaymentRespModel.class);
                    break;
                case GET_BUILDER_PROJECTS:
                    obj = mGson.fromJson(json, EnquiryProjectRespData.class);
                    break;
                case GET_CITY:
                    obj = mGson.fromJson(json, CityRespData.class);
                    break;
                case GET_MICROMARKET:
                    obj = mGson.fromJson(json, MicroMarketRespData.class);
                    break;
                case GET_SECTOR:
                    obj = mGson.fromJson(json, SectorRespData.class);
                    break;
                case ADD_LEAD:
                    obj = mGson.fromJson(json, AddLeadRespModel.class);
                    break;
                case LEAD_DATA:
                    Type leadDataList = new TypeToken<List<AddLeadData>>() {
                    }.getType();
                    obj = mGson.fromJson(json, leadDataList);
                    break;
                case REGISTER_USER:
                    obj = mGson.fromJson(json, RegisterUserRespModel.class);
                    break;
                case UPLOAD_REGISTRATION_DOC:
                    obj = mGson.fromJson(json, UploadRegistrationDoc.class);
                    break;
                case CHANGE_PASSWORD:
                case EDIT_NAME:
                    obj = mGson.fromJson(json, ProfileNameUpdatRespModel.class);
                    break;
                case MY_TRANSACTIONS:
                case BROKER_TRANSACTIONS:
                    obj = mGson.fromJson(json, MyTransactionsRespModel.class);
                    break;
                case GET_CONTACT_US_DATA:
                    obj = mGson.fromJson(json, ContactUsRespModel.class);
                    break;
                case VERIFY_COUPON_CODE:
                case VERIFY_BROKER_CODE:
                case VERIFY_CO_SLAES_PERSON_CODE:
                    obj = mGson.fromJson(json, VerifyCodeRespModel.class);
                    break;
                case MY_DEALS:
                    obj = mGson.fromJson(json, InventoryRespModel.class);
                    break;
                case BROKERS_LIST:
                    obj = mGson.fromJson(json, BrokersRespDataModel.class);
                    break;
                case GET_BROKER_PROFILE_INFO:
                    obj = mGson.fromJson(json, BrokerProfileInfoRespModel.class);
                    break;
                case MY_CHAT_UNITS:
                    obj = mGson.fromJson(json, MyChatUnitsRespModel.class);
                    break;
                case REGISTER_FCM_TOKEN:
                    obj = mGson.fromJson(json, BaseRespModel.class);
                    break;
                case GET_PROJECT_LIST_NOTIFICATION:
                    obj = mGson.fromJson(json, ProjectsListRespModel.class);
                    break;
                case CHAT_TAGS:
                    obj = mGson.fromJson(json, ChatTagsRespModel.class);
                    break;
                case GET_OFFER_CODE:
                    obj = mGson.fromJson(json, GetOfferCodeRespModel.class);
                    break;
                case GET_ANOTHER_OFFER_CODE:
                    obj = mGson.fromJson(json, GetOfferCodeRespModel.class);
                    break;
                case UPDATE_OFFER_STATUS:
                    obj = mGson.fromJson(json, OfferStatusRespModel.class);
                    break;
                case OFFER_JSON:
                    obj = mGson.fromJson(json, SubmitOfferJsonModel.class);
                    break;
                case ACCEPT_REJECT:
                    obj = mGson.fromJson(json, AcceptOfferStatus.class);
                    break;
                case REGISTER_NEW_USER:
                    obj = mGson.fromJson(json, RegisterNewUser.class);
                    break;
                case CREATE_USER_CHANNEL:
                    obj = mGson.fromJson(json, CreateChannel.class);
                    break;
                case LOAD_MESSAGE:
                    obj = mGson.fromJson(json, InitialMessageList.class);
                    break;
                case SEND_MESSAGE:
                    obj = mGson.fromJson(json, SendMessage.class);
                    break;
                case NEW_BLOG_API:
                    obj = mGson.fromJson(json, Data.class);
                    break;
                case BLOG_DETAILS:
                    obj = mGson.fromJson(json, BlogDetails.class);
                    break;
                case BLOG_SUBSCRIBE:
                    obj = mGson.fromJson(json, BlogSubscibe.class);
                    break;
                case GET_RECENT_BLOG:
                    obj = mGson.fromJson(json, RecentBlogModel.class);
                    break;
                case GET_FAV_BLOG:
                    obj = mGson.fromJson(json, FavBlogModel.class);
                    break;
                case MASTERS:
                    obj = mGson.fromJson(json, CorporateModel.class);
                    break;
            }
        } catch (JsonSyntaxException e) {
            Log.i(TAG, "Parsing Error:" + e);
        }
        return obj;
    }

    public static String convertBeanToJson(Object bean) {
        return new Gson().toJson(bean);
    }
}
