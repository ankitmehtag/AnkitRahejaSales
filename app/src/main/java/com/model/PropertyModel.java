package com.model;

import android.util.Log;

import com.VO.BaseVO;
import com.VO.BuildersVO;
import com.VO.DevelopersVO;
import com.VO.NewLaunchVO;
import com.VO.PageVO;
import com.VO.ProjectsVO;
import com.VO.PropertyCaraouselListVO;
import com.VO.PropertyVO;
import com.VO.TandCVO;
import com.VO.TransactionVO;
import com.VO.UnitCaraouselListVO;
import com.VO.UnitDetailVO;
import com.VO.UnitTypesVO;
import com.sp.BMHApplication;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.ContentLoader;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PropertyModel {
    private final String TAG = PropertyModel.class.getSimpleName();

    // public BaseVO UploadPropertyInfo(String name, String address, String
    // city, String locality, String area ,String size, String price, int
    // isE_Bid, int propertyType) throws BMHException{
    // BaseVO basevo = null;
    // String url = UrlFactory.getUploadPropertyUrl();
    // String params =
    // "wpcf-posttitle="+name+
    // "&wpcf-post_city="+city+
    // "&wpcf-post_locality="+locality+
    // "&wpcf-post_address="+address+
    // "&wpcf-post_area="+area+
    // "&wpcf-post_size="+size+
    // "&wpcf-post_propertytype="+propertyType+
    // "&wpcf-post_elegiblity="+isE_Bid+
    // "&wpcf-post_price="+price;
    // String response=ContentLoader.getJsonUsingPost(url, params);
    // System.out.println("hs serverHit= "+url);
    // if(response==null){
    // System.out.println("hs response null");
    // return null;
    // }
    // System.out.println("hs response= "+response);
    // try {
    // JSONObject jsonObj= new JSONObject(response);
    // if(jsonObj != null){
    // basevo = new BaseVO(jsonObj);
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // return basevo;
    // }

    // public BaseVO ImageUploadToProperty(final Bundle b, final String id) {
    // String serverUrl = UrlFactory.getImageUploadUrl();
    //
    // BaseVO vo = null;
    // String response = null;
    //
    // try {
    // response = ContentLoader.UploadImage(b, serverUrl,b.getString("path"));
    // System.out.println("Response"+response);
    // JSONObject obj;
    // try {
    // obj = new JSONObject(response);
    // vo = new BaseVO(obj);
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // } catch (BMHException e) {
    // e.printStackTrace();
    // }
    //
    // return vo;
    // }

    // public PropertyListVO getProperties() throws BMHException{
    // PropertyListVO propvo = null;
    // String url = UrlFactory.getPropertyListUrl();
    // String response=ContentLoader.getJsonFromUrl(url);
    // System.out.println("hs serverHit= "+url);
    // if(response==null){
    // System.out.println("hs response null");
    // return null;
    // }
    // System.out.println("hs response= "+response);
    // try {
    // JSONObject jsonObj= new JSONObject(response);
    // if(jsonObj != null){
    // propvo = new PropertyListVO(jsonObj);
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // return propvo;
    // }

    public PropertyCaraouselListVO getPropertiesViaLocationId(String locationId) throws BMHException {
        PropertyCaraouselListVO propvo = null;
        String url = UrlFactory.getProjectViaLocationUrl();
        String param = "location_id=" + locationId;
        String response = ContentLoader.getJsonUsingPost(url, param);
        System.out.println("hs serverHit= " + url);
        System.out.println("hs value= " + param);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                propvo = new PropertyCaraouselListVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return propvo;
    }

    public PropertyVO getPropertyDetail(String propId, String userId, String type) throws BMHException {
        PropertyVO propvo = null;
        String url = UrlFactory.getProjectdetailsUrl();
        String param = "id=" + propId + "&user_id=" + userId + "&type=" + type;
        String response = ContentLoader.getJsonUsingPost(url, param);
        System.out.println("hs serverHit= " + url);
        System.out.println("hs value= " + param);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                propvo = new PropertyVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return propvo;
    }

    // =================================== Unit Type

    // public UnitTypesVO getUnitTypes(int projectId) throws BMHException{
    // UnitTypesVO propvo = null;
    // String url = UrlFactory.getUnitTypesUrl();
    // String params = "project_id="+ projectId;
    // String response=ContentLoader.getJsonUsingPost(url, params);
    // System.out.println("hs serverHit= "+url);
    // if(response==null){
    // System.out.println("hs response null");
    // return null;
    // }
    // System.out.println("hs response= "+response);
    // try {
    // JSONObject jsonObj= new JSONObject(response);
    // if(jsonObj != null){
    // propvo = new UnitTypesVO(jsonObj);
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // return propvo;
    // }

    public PropertyCaraouselListVO searchPropertyies(HashMap<String, String> searchValues,
                                                     HashMap<String, String> filter_type) throws BMHException {
        PropertyCaraouselListVO basevo = null;
        String ExtraFilter = "";
        try {
            if (searchValues != null && searchValues.get("location_type").equals("Sublocation")) {
                String id = searchValues.get("id");
                searchValues.remove("location");
                searchValues.remove("id");
                searchValues.put("sublocation_id", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> entry : filter_type.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // do stuff
            ExtraFilter = ExtraFilter.length() > 0 ? ExtraFilter + "&" + key + "=" + value : key + "=" + value;
        }
        System.out.println("Post data " + ExtraFilter);
        String url = UrlFactory.getSearchUrl();// +"?"+ ExtraFilter;

        String params = "";
        try {
            if (searchValues != null) {
                Set<String> keys = searchValues.keySet();
                for (String key : keys) {
                    System.out.println(key);
                    params = params + "&" + key + "=" + searchValues.get(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        params = params.replaceFirst("&", "?");
        System.out.println("hs value = " + params);
        url = url + params;
        String response = ContentLoader.getJsonUsingPost(url, ExtraFilter);

        // String response = ContentLoader.getJsonFromUrl(url);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new PropertyCaraouselListVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

    // Add new "isFilter" use key use in Sorting section

    public UnitCaraouselListVO getUnitList(String projectId, String unitIds, String filter_type, String userid) throws BMHException {

        UnitCaraouselListVO basevo = null;
        String url;

        // if (isFilter) {
        // url = UrlFactory.getUnitPrice_HtoL();
        // } else {
        // url = UrlFactory.getUnitListUrl();


        // }
        url = UrlFactory.getUnitListUrl() + "?sortby=" + filter_type;

        // UnitCaraouselListVO basevo = null;
        // String url = UrlFactory.getUnitListUrl();

        String params = "project_id=" + projectId + "&unit_ids=" + unitIds;
        if (userid != "") {
            params = params + "&user_id=" + userid;
        }
//		url = url + "?"+ params;
        System.out.println("hs params = " + params);
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs URL= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new UnitCaraouselListVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

    public UnitDetailVO getUnitDetail(String unitId, String userId) throws BMHException {
        UnitDetailVO basevo = null;
        String url = UrlFactory.getUnitDetailUrl();
        String params = "id=" + unitId + "&user_id=" + userId;
        System.out.println("hs value = " + params);
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new UnitDetailVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

    public TransactionVO getTransactionData(String userId, String unitId) throws BMHException {
        TransactionVO basevo = null;
        String url = UrlFactory.getTransactiondataUrl();
        String params = "user_id=" + userId + "&unit_id=" + unitId;
        System.out.println("hs value = " + params);
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new TransactionVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

    public BaseVO getTransactionStatus(String userID, String orderId) throws BMHException {
        BaseVO basevo = null;
        String url = UrlFactory.getTransactionStatusUrl();
        Log.i(TAG, "URL:" + url);
        String params = "order_id=" + orderId + "&" + ParamsConstants.USER_ID + "=" + userID;
        Log.i(TAG, "Params = " + params);
        String response = ContentLoader.getJsonUsingPost(url, params);
        Log.i(TAG, "Response:" + response);
        if (response == null) {
            return null;
        }
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new BaseVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

    public NewLaunchVO getNewLaunches(String user_id) throws BMHException {
        NewLaunchVO newlaunchvo = null;

        String url = UrlFactory.getNewLaunchesUrl();
        String params = "user_id=" + user_id;
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs serverHit= " + url);
        System.out.println("hs serverHit= " + params);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                newlaunchvo = new NewLaunchVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newlaunchvo;
    }

    public UnitTypesVO getUnitTypes(String projectId, String unitType) throws BMHException {
        UnitTypesVO unitTypevo = null;

        String url = UrlFactory.getUnitTypeUrl();

        String params = "project_id=" + projectId + "&type=" + unitType;

        String response = ContentLoader.getJsonUsingPost(url, params);

        System.out.println("hs params= " + params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                unitTypevo = new UnitTypesVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unitTypevo;
    }

    public BaseVO postComment(String unitId, String userId, String comment, String title, String rating, String commentType)
            throws BMHException {
        BaseVO unitTypevo = null;

        String url = UrlFactory.getAddCommentUrl();
        String params = "post_id=" + unitId + "&user_id=" + userId + "&title=" + title + "&comment=" + comment
                + "&mobile_type=android" + "&rating=" + rating + "&comment_type=" + commentType;

        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs params= " + params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                unitTypevo = new BaseVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unitTypevo;
    }

    public BaseVO addRating(String unitId, String rating, String username, String userId) throws BMHException {
        BaseVO unitTypevo = null;

        String url = UrlFactory.getRatingUrl();
        String params = "rating_unit_id=" + unitId + "&rating=" + rating + "&mobile_ip=" + "1" + "&username=" + username
                + "&user_id=" + userId;
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs params= " + params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                unitTypevo = new BaseVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unitTypevo;
    }

    public BaseVO Favorite(String userId, String unitOrProjectid, String type) throws BMHException {
        BaseVO unitTypevo = null;

        String url = UrlFactory.getFavUrl();
        String params = "user_id=" + userId + "&id=" + unitOrProjectid + "&type=" + type
                + "&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON + "&" + ParamsConstants.BUILDER_ID
                + "=" + BMHConstants.CURRENT_BUILDER_ID;
		/*StringBuilder mStringBuilder = new StringBuilder("");
		mStringBuilder.append(ParamsConstants.ID);
		mStringBuilder.append("=");
		mStringBuilder.append(unitOrProjectid);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.USER_ID);
		mStringBuilder.append("=");
		mStringBuilder.append(userId);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.TYPE);
		mStringBuilder.append("=");
		mStringBuilder.append(type);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.USER_TYPE);
		mStringBuilder.append("=");
		mStringBuilder.append(BMHConstants.BROKER);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.BUILDER_ID);
		mStringBuilder.append("=");
		mStringBuilder.append(BMHConstants.CURRENT_BUILDER_ID);*/


        if (type == "unit") {
            params = "user_id=" + userId + "&unit_id=" + unitOrProjectid + "&type=" + type
                    + "&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON + "&" + ParamsConstants.BUILDER_ID
                    + "=" + BMHConstants.CURRENT_BUILDER_ID;
        }
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs params= " + params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                unitTypevo = new BaseVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unitTypevo;
    }

	/*public AllCommentsVO getAllComments(String projId) throws BMHException {
		AllCommentsVO vo = null;

		String url = UrlFactory.getAllCommentsUrl();
		String params = "id=" + projId;
		String response = ContentLoader.getJsonUsingPost(url, params);
		System.out.println("hs params= " + params);
		System.out.println("hs serverHit= " + url);
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj != null) {
				vo = (AllCommentsVO) JsonParser.convertJsonToBean(APIType.GET_ALL_COMMENT,mBean.getJson() );
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vo;
	}*/

    public PageVO getPage(String url) throws BMHException {
        PageVO vo = null;
        url = url + "?" + BMHConstants.BUILDER_ID_KEY + "=" + BMHConstants.CURRENT_BUILDER_ID;
        String response = ContentLoader.getJsonFromUrl(url);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            response = ContentLoader.getJsonUsingPost(url, BMHConstants.BUILDER_ID_KEY + "=" + BMHConstants.CURRENT_BUILDER_ID);
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                vo = new PageVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vo;
    }

    public NewLaunchVO getFavourites(String userid) throws BMHException {
        NewLaunchVO newlaunchvo = null;

        String url = UrlFactory.getAllFavUrl();
        String params = "user_id=" + userid + "&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON + "&" + ParamsConstants.BUILDER_ID + "=" + BMHConstants.CURRENT_BUILDER_ID;
        System.out.println("hs params= " + params);
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                newlaunchvo = new NewLaunchVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newlaunchvo;
    }

    public DevelopersVO getFeaturedDevelopers(String city_id) throws BMHException {
        DevelopersVO newlaunchvo = null;

        String url = UrlFactory.getFeaturedDevelopersUrl();
        url = url + "?city_id=" + city_id;
        // String params = "user_id="+ userid;
        // System.out.println("hs params= "+params);
        String response = ContentLoader.getJsonFromUrl(url);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                newlaunchvo = new DevelopersVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newlaunchvo;
    }

    public ProjectsVO getFeaturedProjects(String city_id) throws BMHException {
        String url = UrlFactory.getFeaturedProjectsUrl();
        url = url + "?city_id=" + city_id;

        return getFeaturedProjectsByUrl(url);
    }

    public ProjectsVO getFeaturedProjects(String city_id, String user_id) throws BMHException {


        String url = UrlFactory.getFeaturedProjectsUrl();
        url = url + "?city_id=" + city_id;
        if (user_id != "") {
            url = url + "&user_id=" + user_id;
        }
        return getFeaturedProjectsByUrl(url);
    }

    private ProjectsVO getFeaturedProjectsByUrl(String url) {
        ProjectsVO newlaunchvo = null;

        // String params = "user_id="+ userid;
        // System.out.println("hs params= "+params);
        String response = null;
        try {
            response = ContentLoader.getJsonFromUrl(url);
        } catch (BMHException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                newlaunchvo = new ProjectsVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newlaunchvo;
    }

//	public ProjectsVO getFeaturedProjects(String city_id) throws BMHException {
//		ProjectsVO newlaunchvo = null;
//
//		String url = UrlFactory.getFeaturedProjectsUrl();
//		url = url + "?city_id=" + city_id;
//		// String params = "user_id="+ userid;
//		// System.out.println("hs params= "+params);
//		String response = ContentLoader.getJsonFromUrl(url);
//		System.out.println("hs serverHit= " + url);
//		if (response == null) {
//			System.out.println("hs response null");
//			return null;
//		}
//		System.out.println("hs response= " + response);
//		try {
//			JSONObject jsonObj = new JSONObject(response);
//			if (jsonObj != null) {
//				newlaunchvo = new ProjectsVO(jsonObj);
//
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return newlaunchvo;
//	}

    public BuildersVO getBuilders(BMHApplication app) throws BMHException {
        BuildersVO cityvo = null;

        String url = UrlFactory.getAllBuilders();
        String response = ContentLoader.getJsonFromUrl(url);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                app.saveIntoPrefs(BMHConstants.BUILDER_JSON, response);
                cityvo = new BuildersVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityvo;
    }

    public ProjectsVO getAllProjects(BMHApplication app) throws BMHException {
        ProjectsVO cityvo = null;

        String url = UrlFactory.getAllProjects();
        String response = ContentLoader.getJsonFromUrl(url);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                app.saveIntoPrefs(BMHConstants.PROJECTS_JSON, response);
                cityvo = new ProjectsVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityvo;
    }

    public TandCVO getTandC(String projId) throws BMHException {
        TandCVO vo = null;

        String url = UrlFactory.getTCViaProj();
        String param = "id=" + projId;
        String response = ContentLoader.getJsonUsingPost(url, param);
        System.out.println("hs serverHit= " + url);
        System.out.println("hs params= " + param);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                vo = new TandCVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vo;
    }

    public BaseVO doFileUpload(HashMap<String, String> mapPrams, String imagpath) {
        // System.out.println("child_id "+child_id);
        BaseVO vo = null;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String msg = null, photoid = null;
        // DataInputStream inStream = null;
        String exsistingFileName = imagpath;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String urlString = UrlFactory.getsubmitFormImageUrl();
        // System.out.println("Api hit at the time of photo upload "+urlString);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(exsistingFileName));
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            // conn.setRequestProperty("type", ext);

            Set<String> keys = mapPrams.keySet();
            for (String key : keys) {
                System.out.println("hs key =" + key + " value =" + mapPrams.get(key));
                conn.setRequestProperty(key, mapPrams.get(key));
            }

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes(
                    "Content-Disposition: form-data; name=\"image\";filename=\"" + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer tv = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                tv.append(inputLine);
            // System.out.println("tv.toString() "+tv.toString());
            try {
                JSONObject jsonObj = new JSONObject(tv.toString());
                vo = new BaseVO(jsonObj);
                // msg = jsonObj.optString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            in.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return vo;
    }

    public BaseVO submitFormFields(HashMap<String, String> searchValues) throws BMHException {
        BaseVO basevo = null;
        String url = UrlFactory.getSubmitFormStringsUrl();

        String params = "";
        Set<String> keys = searchValues.keySet();
        for (String key : keys) {
            System.out.println(key);
            params = params + "&" + key + "=" + searchValues.get(key);
        }
        params = params.replaceFirst("&", "");
        System.out.println("hs value = " + params);
        // url = url+params;
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new BaseVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

    public BaseVO submitBid(String auction_id, String bid_amt, String bidder_name, String bidder_email)
            throws BMHException {
        BaseVO vo = null;

        String url = UrlFactory.getSubmitBidUrl();
        String params = "auction_id=" + auction_id + "&bid_amt=" + bid_amt + "&bidder_name=" + bidder_name
                + "&bidder_email=" + bidder_email;
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs params= " + params);
        System.out.println("hs serverHit= " + url);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                vo = new BaseVO(jsonObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vo;
    }

    public BaseVO submitAlert(HashMap<String, String> searchValues) throws BMHException {
        BaseVO basevo = null;
        String url = UrlFactory.getSubmitAlertUrl();

        String params = "";
        Set<String> keys = searchValues.keySet();
        for (String key : keys) {
            System.out.println(key);
            params = params + "&" + key + "=" + searchValues.get(key);
        }
        params = params.replaceFirst("&", "");
        System.out.println("hs value = " + params);

        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs serverHit= " + url);
        System.out.println("hs params= " + params);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new BaseVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

    // ===================== Person Details ======================

    public BaseVO PersonalDetails(String first_name, String last_name, String email, String mobile, String dob,
                                  String coapplicant, String pan, String address, String city, String state) throws BMHException {
        BaseVO basevo = null;
        String url = UrlFactory.getPersonDetails();
        String zip = null;
        String params = "first_name=" + first_name + "last_name" + last_name + "&email=" + email + "&mobile=" + mobile
                + "&dob=" + dob + "coapplicant=" + coapplicant + "&pan=" + pan + "&address=" + address + "city=" + city
                + "state=" + state + "&zip=" + zip;
        String response = ContentLoader.getJsonUsingPost(url, params);
        System.out.println("hs serverHit= " + url);
        System.out.println("hs params= " + params);
        if (response == null) {
            System.out.println("hs response null");
            return null;
        }
        System.out.println("hs response= " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj != null) {
                basevo = new BaseVO(jsonObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return basevo;
    }

}
