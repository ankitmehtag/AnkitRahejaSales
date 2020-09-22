package com.appnetwork;

import com.AppEnums.APIType;
import com.helper.UrlFactory;


public class WEBAPI {


    public static String getWEBAPI(APIType apiType) {
        String api = "";
        String separator = UrlFactory.BASEURL.endsWith("/") ? "" : "/";

        switch (apiType) {
            case SYNC_OFFLINE_LEADS:
                api = UrlFactory.BASEURL + separator + "sync_offline_leads.php";
                break;
            case SYNC_OFFLINE_SALES_LEADS:
                api = UrlFactory.BASEURL + separator + "sales/syn_sales_offline_leads.php";
                break;
            case SYNC_ADD_APPOINTMENTS:
                api = UrlFactory.BASEURL + separator + "sales/addAppointmentSync.php";
                break;
            case SYNC_CALL_RECORDING:
                api = UrlFactory.BASEURL + separator + "saveRecording.php";
                break;
            case SYNC_UNIVERSAL_RECORDING:
                api = UrlFactory.BASEURL + separator + "saveAllRecording.php";
                break;
            case HEATMAP:
                api = UrlFactory.BASEURL + separator + "heatmap.php";
                break;
            case HOT_PROJECTS:
                api = UrlFactory.BASEURL + separator + "specialprojects.php";
                break;
            case LOCALITY_SEARCH:
                api = UrlFactory.BASEURL + separator + "search.php";
                break;
            case GET_LOCATIONS:
                api = UrlFactory.BASEURL + separator + "get_locations.php";
                break;
            case SEARCH_PROJECTS:
                api = UrlFactory.BASEURL + separator + "searchProject.php";
                break;
            case FAV_PROJECT:
                api = UrlFactory.BASEURL + separator + "add_favorite.php";
                break;
            case PROJECT_DETAILS:
                api = UrlFactory.BASEURL + separator + "project_detail.php";
                break;
            case GET_GALLERY:
                api = UrlFactory.BASEURL + separator + "project_album.php";
                break;
            case SITE_VISIT:
                api = UrlFactory.BASEURL + separator + "site_visit.php";
                break;
            case GET_ALERT:
                api = UrlFactory.BASEURL + separator + "get_alerts.php";
                break;
            case GET_ENQUERY:
                api = UrlFactory.BASEURL + separator + "enquiry.php";
                break;
            case NOTIFICATIONS:
                api = UrlFactory.BASEURL + separator + "getNotificationsList.php";
                break;
            case FORGOT_PASSWORD:
                api = UrlFactory.BASEURL + separator + "forget_password.php";
                break;
            case CREATE_NEW_PASSWORD:
                api = UrlFactory.BASEURL + separator + "new_password.php";
                break;
            case VERIFY_OTP:
                api = UrlFactory.BASEURL + separator + "verify_otp.php";
                break;
            case NEW_BLOG_API:
                api = UrlFactory.BASEURL + separator + "blog/getBlogList.php";
                break;
            case BLOG_DETAILS:
                api = UrlFactory.BASEURL + separator + "blog/getBlogDetails.php";
                break;
            case READ_BLOG_DETAILS:
                api = UrlFactory.BASEURL + separator + "blog/addBlogRead.php";
                break;
            case BLOG_SUBSCRIBE:
                api = UrlFactory.BASEURL + separator + "blog/getSubscribe.php";
                break;
            case BLOG_ADD_TO_NOTE:
                api = UrlFactory.BASEURL + separator + "blog/addBlogNotes.php";
                break;
            case GET_FAV_BLOG:
                api = UrlFactory.BASEURL + separator + "blog/getBlogNotes.php";
                break;
            case GET_RECENT_BLOG:
                api = UrlFactory.BASEURL + separator + "blog/getRecentBlogs.php";
                break;
            case SHOW_USER_INFO:
                api = UrlFactory.BASEURL + separator + "showuserinfo.php";
                break;
            case PAYMENT_API:
                api = UrlFactory.BASEURL + separator + "payment_api.php";
                break;
            case PAYMENT_REDIRECT:
                api = UrlFactory.BASEURL + separator + "ccavResponseHandler.php";
                break;
            case GET_ALL_COMMENT:
                api = UrlFactory.BASEURL + separator + "get_all_comments.php";
                break;
            case GET_API_VERSION:
                api = UrlFactory.BASEURL + separator + "version.php";
                break;
            case GET_SITEVISIT_TIME:
                api = UrlFactory.BASEURL + separator + "time.php";
                break;
            case GET_PROJECT_LIST_NOTIFICATION:
                api = UrlFactory.BASEURL + separator + "getProjectList.php";
                break;
            case GET_THIRD_PARTY_LOGIN:
                api = UrlFactory.BASEURL + separator + "thirdPartyLogin.php";
                break;
            case USER_LOGIN:
                api = UrlFactory.BASEURL + separator + "salesuserLogin.php";
                break;
            case UNIVERSAL_CONTACTS:
                api = UrlFactory.BASEURL + separator + "getAllLeads.php";
                break;
            case LOTTERY_PROJECT:
                api = UrlFactory.BASEURL + separator + "unit_detail.php";
                break;
            case UPDATE_PERSONAL_DETAILS:
                api = UrlFactory.BASEURL + separator + "saveuserinfo.php";
                break;
            case PAYU_SUCCESS:
                api = UrlFactory.BASEURL + separator + "success.php";
                break;
            case USER_SALES:
                api = UrlFactory.BASEURL + separator + "sales/getUserSalesList.php";
                break;
            case BROKER_MEETING:
                api = UrlFactory.BASEURL + separator + "sales/addBrokerMeeting.php";
                break;
            case MASTERS:
                api = UrlFactory.BASEURL + separator + "sales/getActivityMaster.php";
                break;
            case ADD_LEADS:
                api = UrlFactory.BASEURL + separator + "sales/addLead.php";
                break;
            case UPDATE_LEAD_STATUS:
                api = UrlFactory.BASEURL + separator + "sales/updateStatus.php";
                break;
            case PAYU_FAILURE:
                api = UrlFactory.BASEURL + separator + "failure.php";
                break;

            case UPLOAD_IMAGE:
                api = UrlFactory.BASEURL + separator + "payu.php";
                break;
            case GET_BUILDER_PROJECTS:
                api = UrlFactory.BASEURL + separator + "builder_projects.php";
                break;
            case GET_CITY:
            case GET_MICROMARKET:
            case GET_SECTOR:
                api = UrlFactory.BASEURL + separator + "getLocationInfo.php";
                break;
            case ADD_LEAD:
                api = UrlFactory.BASEURL + separator + "builder_enquiry.php";
                break;
            case GET_BROKER_PROJECTS:
                api = UrlFactory.BASEURL + separator + "broker_project.php";
                break;
            case PROFILE_DETAILS:
                api = UrlFactory.BASEURL + separator + "userprofile.php";
                break;
            case UPLOAD_PROFILE_IMAGE:
            case CHANGE_PASSWORD:
            case EDIT_NAME:
                api = UrlFactory.BASEURL + separator + "updateuserprofile.php";
                break;
            case UPDATE_USER_MOBILE:
                api = UrlFactory.BASEURL + separator + "otp.php";
                break;
            case CHAT_TAGS:
                api = UrlFactory.BASEURL + separator + "chat_initate.php";
                break;
            case MY_CHAT_UNITS:
                api = UrlFactory.BASEURL + separator + "builder_chats.php";
                break;
            case GET_OFFER_CODE:
                api = UrlFactory.BASEURL + separator + "getOfferCode.php";
                break;
            case GET_ANOTHER_OFFER_CODE:
                api = UrlFactory.BASEURL + separator + "getNewOfferCode.php";
                break;
            case UPDATE_OFFER_STATUS:
                api = UrlFactory.BASEURL + separator + "getCouponCode.php";
                break;
            case REGISTER_USER:
                api = UrlFactory.BASEURL + separator + "brokerSignup.php";
                break;
            case REGISTER_FCM_TOKEN:
                api = UrlFactory.BASEURL + separator + "getDeviceToken.php";
                break;
            case UPLOAD_REGISTRATION_DOC:
                api = UrlFactory.BASEURL + separator + "broker_docs.php";
                break;
            case MY_TRANSACTIONS:
                api = UrlFactory.BASEURL + separator + "my_transactions.php";
                break;
            case READ_STATUS:
                api = UrlFactory.BASEURL + separator + "updateNotificationRead.php";
                break;
            case BADGE_COUNT:
                api = UrlFactory.BASEURL + separator + "getUnreadNotificationCount.php";
                break;
            case BROKER_TRANSACTIONS:
                api = UrlFactory.BASEURL + separator + "broker_transactions.php";
                break;
            case GET_CONTACT_US_DATA:
                api = UrlFactory.BASEURL + separator + "contact_data.php";
                break;
            case CONTACT_ENQUIRY:
                api = UrlFactory.BASEURL + separator + "contact_us.php";
                break;
            case VERIFY_CO_SLAES_PERSON_CODE:
            case VERIFY_BROKER_CODE:
            case VERIFY_COUPON_CODE:
                api = UrlFactory.BASEURL + separator + "Checkbrokercode.php";
                break;
            case SIGNUP_DONE:
                api = UrlFactory.BASEURL + separator + "brokerConfirm.php";
                break;
            case MY_DEALS:
                api = UrlFactory.BASEURL + separator + "salesuserDeals.php";
                break;
            case BROKERS_LIST:
                api = UrlFactory.BASEURL + separator + "brokers_list.php";
                break;
            case IVR_LEADS:
                api = UrlFactory.BASEURL + separator + "getIVRLeads.php";
                break;
            case SAVE_LEADS:
                api = UrlFactory.BASEURL + separator + "saveIVRLeads.php";
                break;
            case IVR_HISTORY:
                api = UrlFactory.BASEURL + separator + "getIVRLeadHistory.php";
                break;
            case BROKERS_LEADS:
                api = UrlFactory.BASEURL + separator + "broker_leads.php";
                break;
            case GET_BROKER_PROFILE_INFO:
                api = UrlFactory.BASEURL + separator + "brokerPersonelInfo.php";
                break;

            case GET_USER_PRE_SALES_LIST:
                api = UrlFactory.BASEURL + separator + "getUserPreSalesList.php";
                break;

            case UPDATE_ASM_DETAILS:
                api = UrlFactory.BASEURL + separator + "updateUnassignedLeadDetails.php";
                break;

            case GET_ASM_HISTORY:
                api = UrlFactory.BASEURL + separator + "getHistoryAndRemark.php";
                break;

            case GET_SP_LEADS_DETAILS:
                api = UrlFactory.BASEURL + separator + "getSPLeadDetails.php";
                break;
            case GET_LEADS_MASTER_TABLE:
                api = UrlFactory.BASEURL + separator + "getLeadMasterTable.php";
                break;
            case UPDATE_ENQUIRY_STATUS:
                api = UrlFactory.BASEURL + separator + "UpdateEnquiryStatus.php";
                break;
            case LEAD_DETAILS:
                api = UrlFactory.BASEURL + separator + "sales/getleadDetail.php";
                break;
            case CLOSE_LEADS_DETAILS:
                api = UrlFactory.BASEURL + separator + "sales/getClosureDetails.php";
                break;
            case SAVE_REMARK:
                api = UrlFactory.BASEURL + separator + "saveRemark.php";
                break;
            case PRE_SALES_LEAD_DETAIL:
                api = UrlFactory.BASEURL + separator + "getpresalesleaddetails.php";
                break;
            case REGISTER_NEW_USER:
                api = UrlFactory.CHAT_BASEURL + "v3/users";
                break;
            case GET_USER_CHANNEL:
                api = UrlFactory.CHAT_BASEURL + "v3/users";
                break;
            case CREATE_USER_CHANNEL:
                api = UrlFactory.CHAT_BASEURL + "v3/open_channels";
                break;
            case SEND_MESSAGE:
                api = UrlFactory.CHAT_BASEURL + "v3/open_channels";
                break;
            case LOAD_MESSAGE:
                api = UrlFactory.CHAT_BASEURL + "v3/open_channels";
                break;
            case SET_USER_STATE:
                api = UrlFactory.CHAT_BASEURL + "v3/open_channels";
                break;
            case ACCEPT_LEAD:
                api = UrlFactory.BASEURL + separator + "sales/accept.php";
                break;
            case REJECT_LEAD:
                api = UrlFactory.BASEURL + separator + "sales/reject.php";
                break;
        }
        return api;
    }

    public static final String GET = "get";
    public static final String POST = "post";

    public static final String contentTypeFormData = "application/x-www-form-urlencoded";
    public static final String contentTypeJson = "application/json";
    public static final String contentTypeAudio = "audio/mp3";
}

