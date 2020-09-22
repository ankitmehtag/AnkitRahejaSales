package com.helper;

public class UrlFactory {

    public static final String YOUTUBE_BASE_URL = "http://img.youtube.com/vi/";

    //public static String BASEURL = "https://bookmyhouse.com/liveapi_01/";// Public server or live
    //public static String IMG_BASEURL = "https://bookmyhouse.com/";// Public server or live

    // ------- Staging Server ------------------------------- //
    //public static String BASEURL = "https://demo.bookmyhouse.co/apimain/api_08/";
    //public static String IMG_BASEURL = "https://demo.bookmyhouse.co/";// Staging server
    // --------------------------------------------------------------- //
    // ----------------------------------------------------------- //

    //public static String BASEURL = "https://bookmyhouse.com/api_17/";
    //public static String IMG_BASEURL = "https://bookmyhouse.com/";
    // ----------------------------------------------------------- //

    //public static String BASEURL = "http://staging.bookmyhouse.co/apimain/staging_api/";
    //public static String IMG_BASEURL = "http://staging.bookmyhouse.co/";
    //-----------------------------------------------------------//

    /*   public static String BASEURL = BMHConstants.IS_STAGING ? "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.0" : "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.0";
       public static String IMG_BASEURL = BMHConstants.IS_STAGING ? "http://services.bookmyhouse.com/" : "http://services.bookmyhouse.com/";
   */
    //*********************Live BaseUrl***************************//
    public static String BASEURL = BMHConstants.IS_STAGING ? "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.3/" : "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.3/";

   // public static String BASEURL = BMHConstants.IS_STAGING ? "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.1/" : "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.1/";
    public static String IMG_BASEURL = BMHConstants.IS_STAGING ? "http://services.bookmyhouse.com/" : "http://services.bookmyhouse.com/";
    public static String CHAT_BASEURL = BMHConstants.IS_STAGING ? "http://services.bookmyhouse.com/saas_api/chatserver/" : "http://services.bookmyhouse.com/saas_api/chatserver/";

    //*********************Demo BaseUrl***************************//
  /*  public static String BASEURL = BMHConstants.IS_STAGING ? "http://demo.bookmyhouse.co/services/saas_api/android/salesperson/api_v.1.1/" : "http://demo.bookmyhouse.co/services/saas_api/android/salesperson/api_v.1.1/";
    public static String IMG_BASEURL = BMHConstants.IS_STAGING ? "http://demo.bookmyhouse.co/" : "http://demo.bookmyhouse.co/";
    public static String CHAT_BASEURL = BMHConstants.IS_STAGING ? "http://demo.bookmyhouse.co/services/saas_api/chatserver/" : "http://demo.bookmyhouse.co/services/saas_api/chatserver/";
  */  // ----------------------------------------------------------- //

    //public static String DOMAIN = "http://bookmyhouse.com/";
//	      public static String BASEURL = "http://54.251.121.65/";
//        public static String BASEURL = "http://54.179.227.220/";	

    // demo url
    //	      http://demo.bookmyhouse.co/api/search

//		  public static String BASEURL = "http://bmhproduction.com/";
//		  public static String BASEURL = "http://bookmyhouse.com/appapi/";
//        public static String BASEURLIMG ="http://s3.ap-south-1.amazonaws.com/bookmyhouse/";
//add services/ before resize

    public static String API = "";
    public static String separator = UrlFactory.BASEURL.endsWith("/") ? "" : "/";
//need to add the services/ before resize.php
    public static String getShortImageByWidthUrl(int width, String bigImageUrl) {
        String temp = IMG_BASEURL + "resize.php?w=" + width + "&img=" + bigImageUrl;
        return temp;
    }

    public static String getShortImageByHeightUrl(int height, String bigImageUrl) {
        return IMG_BASEURL + "resize.php?h=" + height + "&img=" + bigImageUrl;
    }


//	 public static String DOMAIN = "http://52.77.73.171/";

    // public static String getShortImageByWidthUrl(int width, String
    // bigImageUrl) {
    // String temp = BASEURLIMG+"resize.php?w="+width+"&img="+bigImageUrl;
    // return temp;
    // }
    // public static String getShortImageByHeightUrl(int height, String
    // bigImageUrl) {
    // return BASEURLIMG+"resize.php?h="+height+"&img="+bigImageUrl;
    // }

    public static String getThumbUrlViaId(String id) {
        // return "http://img.youtube.com/vi/"+id+"/0.jpg";
        return getHDThumbUrlViaId(id);
    }

    public static String getHDThumbUrlViaId(String id) {
        return "http://img.youtube.com/vi/" + id + "/hqdefault.jpg";
    }

    public static String getPlacesUrl() {
        // https://maps.googleapis.com/maps/api/place/search/json?
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    }

    public static String getRegisterUrl() {
        return BASEURL + separator + "seekersignup.php";
    }

    public static String getLoginUrl() {
        return BASEURL + separator + "loginseeker.php";
    }

    public static String postPersionalInformation() {
        return BASEURL + separator + "saveuserinfo.php";
    }

    public static String getUploadPropertyUrl() {
        return BASEURL + separator + "property_add.php";
    }

    public static String getPropertyListUrl() {
        return BASEURL + separator + "propertygallery.php";
    }

    public static String getProjectViaLocationUrl() {
        return BASEURL + separator + "projects_by_location.php";
    }

    public static String getImageUploadUrl() {
        return BASEURL + separator + "imgupload.php";
    }

    /* < By City / Search /then click / city area crousel item > */

    public static String getSearchUrl() {
        return BASEURL + separator + "searchProject.php";
    }

    // --- Above url use City locality with use id
    // "location=213&type=Buy&p_type=FLAT&city=16"

    public static String getCitiesUrl() {
        return BASEURL + separator + "getcities.php";
    }

    public static String getAreaUrl() {
        return BASEURL + separator + "get_area.php";
    }

    public static String getLocationsUrl() {
        return BASEURL + separator + "get_locations.php"; /////////////////////
    }

    public static String getUnitTypesUrl() {
        return BASEURL + separator + "floor_plan.php";
    }

    public static String getImageMapUrl() {
        return BASEURL + separator + "image_map.php?id=";
    }

    public static String getProjectdetailsUrl() {
        return BASEURL + separator + "project_detail.php";
    }

    public static String getUnitListUrl() {
        return BASEURL + separator + "unit_list_byproj_id.php"; //////////////
    }

    public static String getPriceTrendsGraphUrl() {
        return BASEURL + separator + "line_chart.php?price_trends=";
    }

    public static String getUnitDetailUrl() {
        return BASEURL + separator + "unit_detail.php";
    }

    public static String getTransactiondataUrl() {
        return BASEURL + separator + "payment_api.php?action=payment_form";
    }

    public static String getTransactionStatusUrl() {
        return BASEURL + separator + "payment_api.php?action=payment_message";
    }

    public static String getNewLaunchesUrl() {
        return BASEURL + separator + "new_launches.php";
    }

    public static String getGCMRegisterUrl() {
        return BASEURL + separator + "gcm_reg.php";
    }

    public static String getUnitTypeUrl() {
        return BASEURL + separator + "unit_type_list.php";
    }

    public static String getAddCommentUrl() {
        return BASEURL + separator + "add_comment.php"; /////////////////////
    }

    public static String getRatingUrl() {
        return BASEURL + separator + "unit_rating.php";
    }

    public static String getFavUrl() {
        return BASEURL + separator + "add_favorite.php"; //////////////////////
    }

    public static String getAllCommentsUrl() {
        return BASEURL + separator + "get_all_comments.php"; //////////////////////
    }

    public static String getT_CUrl() {
        return BASEURL + separator + "term_condition.php"; /////////////////
    }

    public static String getPrivacyPolicy() {
        return BASEURL + separator + "privacy_policy.php"; ///////////////
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static String getAboutUsUrl() {
        return BASEURL + separator + "about_us.php";
    }

    public static String getAllFavUrl() {
        return BASEURL + separator + "get_user_favorites.php";
    }

	/*public static String getThirdPartyLoginUrl() {
		return BASEURL + separator + "thirdPartyLogin.php";
	}*/

    public static String getFeaturedDevelopersUrl() {
        return BASEURL + separator + "featured_developers.php";
    }

    public static String getFeaturedProjectsUrl() {
        return BASEURL + separator + "featured_projects.php";
    }

    public static String getAllProjects() {
        return BASEURL + separator + "get_all_projects.php";
    }

    public static String getAllBuilders() {
        return BASEURL + separator + "get_all_builders.php";
    }

    public static String getTCViaProj() {
        return BASEURL + separator + "get_project_term_condition.php";
    }

    public static String getsubmitFormImageUrl() {
        return BASEURL + separator + "image_upload.php";
    }

    public static String getSubmitFormStringsUrl() {
        return BASEURL + separator + "submit_application_form.php";
    }

    public static String getSubmitBidUrl() {
        return BASEURL + separator + "bid_submit.php";
    }

    public static String getSubmitAlertUrl() {
        return BASEURL + separator + "get_alerts.php";
    }

    public static String getGelleryImage() {
        return BASEURL + separator + "get_galleryimage.php";
    }

    public static String getGelleryVideo() {
        return BASEURL + separator + "get_video.php";
    }

    public static String getUnderImage() {
        return BASEURL + separator + "get_constructionupdate.php";
    }

    // Sorting separator for Select Locality

    public static String getSortPsfLtoH() {
        return BASEURL + separator + "get_locations.php?sortby=psf_ltoh";
    }

    public static String getSortPsfHtoL() {
        return BASEURL + separator + "get_locations.php?sortby=psf_htol";
    }

    public static String getSortInfra() {
        return BASEURL + separator + "get_locations.php?sortby=infra";
    }

    public static String getSortLifeStyle() {
        return BASEURL + separator + "get_locations.php?sortby=lifestyle";
    }

    public static String getSortNeeds() {
        return BASEURL + separator + "get_locations.php?sortby=needs";
    }

    public static String getSortRating() {
        return BASEURL + separator + "get_locations.php?sortby=rating";
    }

    // Sorting separator for Select Unit

    public static String getUnitPrice_LtoH() {
        return BASEURL + separator + "unit_list_byproj_id.php?sortby=price_ltoh";
    }

    public static String getUnitPrice_HtoL() {
        return BASEURL + separator + "unit_list_byproj_id.php?sortby=price_htol";
    }

    public static String getUnitFloor_LtoH() {
        return BASEURL + separator + "unit_list_byproj_id.php?filterOnly=floor_ltoh";
    }

    public static String getUnitFloor_HtoL() {
        return BASEURL + separator + "unit_list_byproj_id.php?filterOnly=floor_htol";
    }

    // Person details of payment section

    public static String getPersonDetails() {
        return BASEURL + separator + "saveuserinfo.php";
    }

    // ======================= Use Multiple filter
    public static String getFilterSearchUrl() {
        return BASEURL + separator + "searchProject.php?NewLaunch"; ////////////
    }

    public static String getProjectId() {
        // return "416447649043";
        return "49806243051";

    }

    public static String getMultiTypeSearch() {
//		return BASEURL+separator + "search.php";
        return BASEURL + separator + "search.php";
    }

    public static String getTopLocation() {
        return BASEURL + "api/showingrecent";

    }
    //=====================  Contact Us send user info data

    public static String getContactUs() {
        return BASEURL + separator + "contact_us.php";
    }

    //================= show the address, mobile number and email address

    public static String getContactDataUrl() {
        return BASEURL + separator + "contact_data.php";
    }


    public static String getEnquiryUrl() {
        return BASEURL + separator + "enquiry.php";
    }

    public static String getSiteVisitUrl() {
        return BASEURL + separator + "site_visit.php";
    }

    public static String getAlertUrl() {
        return BASEURL + separator + "get_alerts.php";
    }

    public static String getForgotUrl() {
        return BASEURL + API + "forget_password.php";
    }


}