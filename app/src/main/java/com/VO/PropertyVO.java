package com.VO;


import android.os.Parcelable;

import com.AppEnums.APIType;
import com.jsonparser.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PropertyVO extends BaseVO implements Parcelable{
	// area_delevered: "17000 Sq Yards"
	// : "1953"
	private String id;
	private String builder_logo;
	private String builder_description;
	private String builder_contactno;
	private String builder_mobile;
	private String area_delevered;
	private String establish_year;
	private boolean user_favourite = false;
//	private String user_favourite;
	private String ratings_average;
	private String proj_name;
	private String builder_name;
	private String prop_price_persq;
	private String std_project_price;
	private String infra;
	private String needs;
	private String life_style;
	private String returns;
	private String status;

	private String unit_block_no;
	private String floor_no;
	private String developer_name;

	// private String project_unit_type;

	private String price_one_year;
	private String description;
	private ArrayList<String> wow;
	private String address;
	private String sector;
	private String location;
	private String city;
	private String state;
	private String min_price;
	private String max_price;
	private String possession_dt;
	private boolean sample_flat;

	private ArrayList<String> arrType;
	private AmenitiesVO amenitiesVO;
	private ServicesVO servicesVO;
	private SafetyVO safetyVO;
	private RecreationVO recreationVO;

	private ArrayList<UnitType> arrUnitType;
	private String price_trends;
	private String Exactlocation;
	
	private String price;
	


	// private FlooringFittingWallsVO

	private String video_walk_through;
	private String project_plan_img;
	// private String project_name;
	private String lat_lng;
	private String se;
	private String nw;
	private String locality_insights;
	private ArrayList<String> arrUnit_ids;
	// private ArrayList<String> arrProject_images;
	private String priceTrendsJson;
	private ArrayList<MediaGellaryVO> mediaGellary;

	private String availabe_unit;
	
	private boolean is_static_unit;
	private boolean is_comment_list_needed;
	private String isLotteryProject;

	private String projectype;


	ArrayList<String> Floor_img_plan;



	private ArrayList<String> floreList;



	HashMap<String,Object> hashMapArrayList;



	ArrayList<String> keyList;
	public ArrayList<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(ArrayList<String> keyList) {
		this.keyList = keyList;
	}

	public HashMap<String, Object> getHashMapArrayList() {
		return hashMapArrayList;
	}

	public void setHashMapArrayList(HashMap<String, Object> hashMapArrayList) {
		this.hashMapArrayList = hashMapArrayList;
	}

	public ArrayList<String> getFloor_img_plan() {
		return Floor_img_plan;
	}

	public void setFloor_img_plan(ArrayList<String> floor_img_plan) {
		Floor_img_plan = floor_img_plan;
	}



	public ArrayList<String> getFloreList() {
		return floreList;
	}

	public void setFloreList(ArrayList<String> floreList) {
		this.floreList = floreList;
	}

	public String getProjectype() {
		return projectype;
	}

	public void setProjectype(String projectype) {
		this.projectype = projectype;
	}


	public String getIsLotteryProject() {
		return isLotteryProject;
	}

	public void setIsLotteryProject(String isLotteryProject) {
		this.isLotteryProject = isLotteryProject;
	}

	public boolean is_comment_list_needed() {
		return is_comment_list_needed;
	}

	public void setIs_comment_list_needed(boolean is_comment_list_needed) {
		this.is_comment_list_needed = is_comment_list_needed;
	}

	// ========================== New Section of Unit MediaGellary

	private ArrayList<MediaGellaryVO> unitGellary;

	private ArrayList<CommentsVO> comments_detail;
	private String min_area_range;
	private String max_area_range;
	private String std_unit_size;

	public String getStd_unit_size() {
		return std_unit_size;
	}

	public void setStd_unit_size(String std_unit_size) {
		this.std_unit_size = std_unit_size;
	}

	private ArrayList<PlacesLocationVO> arrLandmarks;
	private int total_units;

	// ============== Add new Unit Specification

	private static ArrayList<FlooringFittingWallsVO> flooring;
	private static ArrayList<FlooringFittingWallsVO> fitting;
	private static ArrayList<FlooringFittingWallsVO> walls;

	private String proj_unit_type;
	// private String media_count;
	private int no_media_image;

	// private String MediaCount;
	private String media_count;
	
	private String project_url;

	ArrayList<JSONArray> arrlist_jsonarray;
	JSONArray aaaaaa = null;
	String str_floor_img;

	public PropertyVO(JSONObject jobjMain) {
		super(jobjMain);

		keyList = new ArrayList<>();
		JSONObject jobj = null;
		JSONArray jobj_all = null;
		JSONArray jArrImage = null;

		Floor_img_plan  = new ArrayList<String>();
		try {
			//jobj = jobjMain.getJSONObject("data");
			jobj = jobjMain.getJSONObject("data");
			jobj_all = jobjMain.getJSONArray("floorList");
			jArrImage =  jobjMain.getJSONArray("floorImages");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		if(jArrImage != null){
			for (int p=0;p<jArrImage.length();p++){
				try {
					str_floor_img =  jArrImage.getJSONObject(p).optString("floor_plan_img");
					Floor_img_plan.add(str_floor_img);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//  setFloor_img_plan
			}
			setFloor_img_plan(Floor_img_plan);
		}

		if (jobj != null) {
			setIs_comment_list_needed(jobj.optBoolean("is_comment_list_needed"));
			setId(jobj.optString("id"));
			setBuilder_logo(jobj.optString("builder_logo"));
			setBuilder_description(jobj.optString("builder_description"));
			setBuilder_contactno(jobj.optString("builder_contactno"));
			setBuilder_mobile(jobj.optString("builder_mobile"));
			setArea_delevered(jobj.optString("area_delevered"));
			setEstablish_year(jobj.optString("establish_year"));
			setUser_favourite(jobj.optBoolean("user_favourite"));
			setRatings_average(jobj.optString("ratings_average"));
			setProj_name(jobj.optString("proj_name"));

			setProjectype(jobj.optString("project_type"));
			
			setDeveloper_name(jobj.optString("developer_name"));
			setBuilder_name(jobj.optString("builder_name"));
			setProp_price_persq(jobj.optString("prop_price_persq"));
			setStd_project_price(jobj.optString("std_project_price"));
			setInfra(jobj.optString("infra"));
			setNeeds(jobj.optString("needs"));
			setLife_style(jobj.optString("life_style"));
			setReturns(jobj.optString("return"));
			setStatus(jobj.optString("status"));
			setFloors(jobj.optString("floor_no"));
			setNo_Block(jobj.optString("unit_block_no"));
			setProject_unit_type((String) jobj.opt("proj_unit_type"));
			setPrice_year(jobj.optString("price_one_year"));
			setDescription(jobj.optString("description"));
			setAddress(jobj.optString("address"));
			setSector(jobj.optString("sector"));
			setLocation(jobj.optString("location"));
			setCity(jobj.optString("city"));
			setState(jobj.optString("state"));
			setMin_price(jobj.optString("min_price"));
			setMax_price(jobj.optString("max_price"));
			setPossession_dt(jobj.optString("possession_dt"));
			setSample_flat(jobj.optBoolean("sample_flat"));
			setVideo_walk_through(jobj.optString("video_walk_through"));
			setProject_plan_img(jobj.optString("project_plan_img"));
			// setProject_name(jobj.optString("project_name"));
			setLat_lng(jobj.optString("lat_lng"));
			setLocality_insights(jobj.optString("locality_insights"));
			setMin_area_range(jobj.optString("min_area_range"));
			setMax_area_range(jobj.optString("max_area_range"));
			setStd_unit_size(jobj.optString("std_unit_size"));
			setTotal_units(jobj.optInt("total_units"));
			
			setExactlocation(jobj.optString("exactlocation"));

			// setMediaCount(jobj.optInt("no_media_image"));
			// setMediaCount(jobj.optInt("media_count"));

			setMediaCount(jobj.optString("media_count"));
			setAvailable_Unit(jobj.optString("availabe_unit"));
			
			setIs_Static_Unit(jobj.optBoolean("is_static_unit"));
			setIsLotteryProject(jobj.optString("isLotteryProject"));


			setAmenitiesVO(new AmenitiesVO(jobj.optJSONArray("Amenities")));
			setServicesVO(new ServicesVO(jobj.optJSONArray("services")));
			setSafetyVO(new SafetyVO(jobj.optJSONArray("safety")));
			setRecreationVO(new RecreationVO(jobj.optJSONArray("recreation")));
			JSONArray arr = jobj.optJSONArray("price_trends");
			if(arr != null) {
				setPriceTrendsJson(arr.toString());
				System.out.println("hh getPriceTrendsJson=" + getPriceTrendsJson());
			}
			setPrice_Trends(jobj.optString("price_trends"));
			
			setProject_Url(jobj.optString("project_url"));
			setPrice(jobj.optString("price"));

			try {
				JSONObject cordsJson = jobj.getJSONObject("cords");
				setSe(cordsJson.optString("se"));
				setNw(cordsJson.optString("nw"));
			} catch (JSONException e) {
				e.printStackTrace();
			}


			//JSONArray floorList = jobj.optJSONArray("floorList");
			ArrayList<String> aListFlore= new ArrayList<String>();
			if (jobj_all!=null){
				for (int i=0;i<jobj_all.length();i++){
					aListFlore.add(jobj_all.optString(i));
					//   aListFlore.add(floorList.optJSONObject(i).toString());
				}
			}
			this.setFloreList(aListFlore);



			JSONArray arrwow = jobj.optJSONArray("wow");
			ArrayList<String> aListwow = new ArrayList<String>();
			if (arrwow != null) {
				for (int i = 0; i < arrwow.length(); i++) {
					aListwow.add(arrwow.optString(i));
				}
			}
			this.setWow(aListwow);

			JSONArray arrType = jobj.optJSONArray("type");
			ArrayList<String> aListType = new ArrayList<String>();
			if (arrType != null) {
				for (int i = 0; i < arrType.length(); i++) {
					aListType.add(arrType.optString(i));
				}
			}
			this.setArrType(aListType);

			JSONArray arrUnit_ids = jobj.optJSONArray("unit_ids");
			ArrayList<String> aListUnitIds = new ArrayList<String>();
			if (arrUnit_ids != null) {
				for (int i = 0; i < arrUnit_ids.length(); i++) {
					aListUnitIds.add(arrUnit_ids.optString(i));
				}
			}
			this.setArrUnit_ids(aListUnitIds);

			// JSONArray arrProjectImages =jobj.optJSONArray("project_images");
			// ArrayList<String> aListProjImg = new ArrayList<String>();
			// if(arrProjectImages != null){
			// for(int i = 0; i<arrProjectImages.length(); i++){
			// aListProjImg.add(arrProjectImages.optString(i));
			// }
			// this.setArrProject_images(aListProjImg);
			// }

			JSONArray arrjsonGellary = jobj.optJSONArray("media_gallery");
			ArrayList<MediaGellaryVO> arrMediaGallery = new ArrayList<MediaGellaryVO>();
			if (arrjsonGellary != null) {
				for (int i = 0; i < arrjsonGellary.length(); i++) {
					JSONObject obj = arrjsonGellary.optJSONObject(i);
					if (obj != null) {
						//MediaGellaryVO vo = new MediaGellaryVO(obj);
						MediaGellaryVO vo = (MediaGellaryVO) JsonParser.convertJsonToBean(APIType.MEDIA_GALLARY,obj.toString());
						arrMediaGallery.add(vo);
					}
				}
				this.setMediaGellary(arrMediaGallery);
			}

			// =============================== Unit Gallery
			JSONArray arrjsonUGellary = jobj.optJSONArray("flat_images");
			ArrayList<MediaGellaryVO> arrUnitGallery = new ArrayList<MediaGellaryVO>();
			if (arrjsonUGellary != null) {
				for (int i = 0; i < arrjsonUGellary.length(); i++) {
					JSONObject obj = arrjsonUGellary.optJSONObject(i);
					if (obj != null) {
						MediaGellaryVO vo = (MediaGellaryVO) JsonParser.convertJsonToBean(APIType.MEDIA_GALLARY,obj.toString());
						arrUnitGallery.add(vo);
					}
				}
				this.setUnitGellary(arrUnitGallery);
			}
			// =============================

			JSONArray arrjsoncomments_detail = jobj
					.optJSONArray("comments_detail");
			ArrayList<CommentsVO> arrComments = new ArrayList<CommentsVO>();
			if (arrjsoncomments_detail != null) {
				for (int i = 0; i < arrjsoncomments_detail.length(); i++) {
					JSONObject obj = arrjsoncomments_detail.optJSONObject(i);
					if (obj != null) {
						CommentsVO vo = (CommentsVO) JsonParser.convertJsonToBean(APIType.COMMENT,obj.toString());
						arrComments.add(vo);
					}
				}
				this.setComments_detail(arrComments);
			}

			JSONArray arrjsonLandmark = jobj.optJSONArray("project_landmark");
			ArrayList<PlacesLocationVO> arrLand = new ArrayList<PlacesLocationVO>();
			if (arrjsonLandmark != null) {
				for (int i = 0; i < arrjsonLandmark.length(); i++) {
					JSONObject obj = arrjsonLandmark.optJSONObject(i);
					if (obj != null) {
						PlacesLocationVO vo = new PlacesLocationVO(obj);

						arrLand.add(vo);
					}
				}
				this.setArrLandmarks(arrLand);
			}

			// ========================= Reference of Unit section

			ArrayList<FlooringFittingWallsVO> arrflooring = new ArrayList<FlooringFittingWallsVO>();
			try {
				JSONArray arrJsonFloor = jobj.getJSONArray("flooring");
				for (int i = 0; i < arrJsonFloor.length(); i++) {
					JSONObject o = arrJsonFloor.getJSONObject(i);
					FlooringFittingWallsVO vo = new FlooringFittingWallsVO(o);
					arrflooring.add(vo);
				}
				this.setFlooring(arrflooring);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ArrayList<FlooringFittingWallsVO> arrfitting = new ArrayList<FlooringFittingWallsVO>();
			try {
				JSONArray arrJsonFloor = jobj.getJSONArray("fitting");
				for (int i = 0; i < arrJsonFloor.length(); i++) {
					JSONObject o = arrJsonFloor.getJSONObject(i);
					FlooringFittingWallsVO vo = new FlooringFittingWallsVO(o);
					arrfitting.add(vo);
				}
				this.setFittings(arrfitting);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
			ArrayList<FlooringFittingWallsVO> arrWalls = new ArrayList<FlooringFittingWallsVO>();
			try {
				JSONArray arrJsonFloor = jobj.getJSONArray("walls");
				for (int i = 0; i < arrJsonFloor.length(); i++) {
					JSONObject o = arrJsonFloor.getJSONObject(i);
					FlooringFittingWallsVO vo = new FlooringFittingWallsVO(o);
					arrWalls.add(vo);
				}
				this.setWalls(arrWalls);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// =============Unit Type list

		ArrayList<UnitType> arrVO = null;
		JSONArray arrJson;
		ArrayList<ArrayList<UnitType>> arrMainList = null;
		//  HashMap<Integer,ArrayList<UnitType>> hashMapArrayList;
		JSONObject jsonObject;



		try {


			arrlist_jsonarray = new ArrayList<JSONArray>();
			arrVO = new ArrayList<UnitType>();
			arrMainList = new ArrayList<ArrayList<UnitType>>();
			hashMapArrayList = new HashMap<String, Object>();

			if(jobj.optString("project_type").contains("Residential")){

				arrJson = jobj.getJSONArray("unit_type");
			arrlist_jsonarray = new ArrayList<JSONArray>();
			if (arrJson != null) {
				arrVO = new ArrayList<UnitType>();
				for (int i = 0; i < arrJson.length(); i++) {
					UnitType launch = new UnitType(arrJson.optJSONObject(i));
					arrVO.add(launch);

					// System.out.println("Krishna  AAAAAAAAAAAAA");
					aaaaaa = arrJson.optJSONObject(i).optJSONArray(
							"flat_images");
					arrlist_jsonarray.add(aaaaaa);
					// System.out.println("Krishna BBBBBBBBBBBBBBB   "+aaaaaa.length());

				}
				this.setArrUnitType(arrVO);
			}
			}else if(jobj.optString("project_type").contains("Commercial"))
			{
				jsonObject = jobj.getJSONObject("unit_type");

				if (jsonObject != null) {

//int i = 0;
					Iterator<String> iter = jsonObject.keys();
					while (iter.hasNext()) {

						String key = iter.next();
						keyList.add(key);
						try {
							Object value = jsonObject.get(key);
							// JSONArray jsonArray = new JSONArray(value);
							// unitTypesoooo.

							hashMapArrayList.put(key,value);
							//UnitType launch = new UnitType(arrJson.optJSONObject(i));
							// UnitType launch = new UnitType(jsonObject.optJSONArray(key).getJSONObject(i));
							//arrVO.add(launch);
							JSONArray jsonArray = new JSONArray();

							jsonArray = (JSONArray) hashMapArrayList.get(key);

							for (int s=0;s<jsonArray.length();s++){
								aaaaaa =   jsonArray.getJSONObject(0).getJSONArray("flat_images");
								arrlist_jsonarray.add(aaaaaa);
							}


                           /* aaaaaa = jsonArray.optJSONObject(k).optJSONArray(
                                    "flat_images");
                            arrlist_jsonarray.add(aaaaaa);*/


						} catch (JSONException e) {
							// Something went wrong!
						}
					}
					setHashMapArrayList(hashMapArrayList);
				}
			}
				/*
				 * System.out.println("aaaaaa.length()   "+aaaaaa.length());
				 * for(int j = 0; j < arrlist_jsonarray.size(); j++){
				 * System.out.
				 * println("arrlist_jsonarray of j  "+arrlist_jsonarray.get(j));
				 * //System.out.println("Single Object "+
				 * aaaaaa.getJSONObject(j).getString("url")); }
				 */

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// try {
		// arrJson = jobj.getJSONArray("unit_type");
		// if (arrJson != null) {
		// arrVO = new ArrayList<UnitType>();
		// for (int i = 0; i < arrJson.length(); i++) {
		// UnitType launch = new UnitType(arrJson.optJSONObject(i));
		// arrVO.add(launch);
		// }
		// this.setArrUnitType(arrVO);
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

	}

	// ================ Unit Type List
	//
	// public PropertyVO(JSONObject obj) {
	// super(obj);
	//
	//
	// ArrayList<UnitType> arrVO = null;
	//
	// JSONArray arrJson;
	// try {
	// arrJson = obj.getJSONArray("data");
	// if(arrJson!= null ){
	// arrVO = new ArrayList<UnitType>();
	// for (int i = 0; i < arrJson.length(); i++) {
	// UnitType launch = new UnitType(arrJson.optJSONObject(i));
	// arrVO.add(launch);
	// }
	// this.setArrUnitType(arrVO);
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }

	// private void setMediaCount(int optInt) {
	// // TODO Auto-generated method stub
	//
	// }

	// private void setMediaCount(int optInt) {
	// // TODO Auto-generated method stub
	//
	// }

	

	public ArrayList<UnitType> getArrUnitType() {
		return arrUnitType;
	}

	public void setArrUnitType(ArrayList<UnitType> arrUnitType) {
		this.arrUnitType = arrUnitType;
	}

	// =============== Unit Type List End

	public static ArrayList<FlooringFittingWallsVO> getFlooring() {
		return flooring;
	}

	public void setFlooring(ArrayList<FlooringFittingWallsVO> flooring) {
		this.flooring = flooring;
	}

	public static ArrayList<FlooringFittingWallsVO> getFittings() {
		return fitting;
	}

	public void setFittings(ArrayList<FlooringFittingWallsVO> fittings) {
		this.fitting = fittings;
	}

	public static ArrayList<FlooringFittingWallsVO> getWalls() {
		return walls;
	}

	public void setWalls(ArrayList<FlooringFittingWallsVO> walls) {
		this.walls = walls;
	}


	public String getStd_project_price() {
		return std_project_price;
	}

	public void setStd_project_price(String std_project_price) {
		this.std_project_price = std_project_price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProj_name() {
		return proj_name;
	}

	public void setProj_name(String proj_name) {
		this.proj_name = proj_name;
	}

	public String getBuilder_name() {
		return builder_name;
	}

	public void setBuilder_name(String builder_name) {
		this.builder_name = builder_name;
	}
	
	///=====================
	
	public String getDeveloper_name() {
		return developer_name;
	}

	public void setDeveloper_name(String developer_name) {
		this.developer_name = developer_name;
	}
	
	
	

	public String getProp_price_persq() {
		return prop_price_persq;
	}

	public void setProp_price_persq(String prop_price_persq) {
		this.prop_price_persq = prop_price_persq;
	}

	public String getInfra() {
		return infra;
	}

	public void setInfra(String infra) {
		this.infra = infra;
	}

	public String getNeeds() {
		return needs;
	}

	public void setNeeds(String needs) {
		this.needs = needs;
	}

	public String getLife_style() {
		return life_style;
	}

	public void setLife_style(String life_style) {
		this.life_style = life_style;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// ==============No of Floors

	public String getFloors() {
		return floor_no;
	}

	public void setFloors(String floor_no) {
		this.floor_no = floor_no;
	}

	// ===========No of Block

	public String getNo_Block() {
		return unit_block_no;
	}

	public void setNo_Block(String unit_block_no) {
		this.unit_block_no = unit_block_no;
	}

	// ============project_unit_type

	public String getProject_unit_type() {

		return proj_unit_type;
	}

	public void setProject_unit_type(String proj_unit_type) {
		this.proj_unit_type = proj_unit_type;
	}

	// ===================== Gallery Count

	// public int getMediaCount (){
	//
	// return no_media_image;
	// }
	//
	//
	// public void setMediaCount(int no_media_image) {
	// this.no_media_image = no_media_image;
	//
	// }

	// =============================Gallery image count

	public String getMediaCount() {
		return media_count;
	}

	public void setMediaCount(String media_count) {
		this.media_count = media_count;
	}

	public String getAvailable_Unit() {
		return availabe_unit;
	}

	public void setAvailable_Unit(String availabe_unit) {
		this.availabe_unit = availabe_unit;
	}

	public boolean getIs_Static_Unit() {
		return is_static_unit;
	}

	public void setIs_Static_Unit(boolean is_static_unit) {
		this.is_static_unit = is_static_unit;
	}

	
	
	// =================== Unit list on click //// Krishna /////

	public ArrayList<JSONArray> getflat_img() {
		return arrlist_jsonarray;
	}

	// =========== price in year

	public String getPrice_year() {

		return price_one_year;
	}

	public void setPrice_year(String price_one_year) {
		this.price_one_year = price_one_year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getWow() {
		return wow;
	}

	public void setWow(ArrayList<String> wow) {
		this.wow = wow;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMin_price() {
		return min_price;
	}

	public void setMin_price(String min_price) {
		this.min_price = min_price;
	}

	public String getMax_price() {
		return max_price;
	}

	public void setMax_price(String max_price) {
		this.max_price = max_price;
	}

	public String getPossession_dt() {
		return possession_dt;
	}

	public void setPossession_dt(String possession_dt) {
		this.possession_dt = possession_dt;
	}

	public boolean isSample_flat() {
		return sample_flat;
	}

	public void setSample_flat(boolean sample_flat) {
		this.sample_flat = sample_flat;
	}

	public ArrayList<String> getArrType() {
		return arrType;
	}

	public void setArrType(ArrayList<String> arrType) {
		this.arrType = arrType;
	}

	public AmenitiesVO getAmenitiesVO() {
		return amenitiesVO;
	}

	public void setAmenitiesVO(AmenitiesVO amenitiesVO) {
		this.amenitiesVO = amenitiesVO;
	}

	public ServicesVO getServicesVO() {
		return servicesVO;
	}

	public void setServicesVO(ServicesVO servicesVO) {
		this.servicesVO = servicesVO;
	}

	public SafetyVO getSafetyVO() {
		return safetyVO;
	}

	public void setSafetyVO(SafetyVO safetyVO) {
		this.safetyVO = safetyVO;
	}

	public String getVideo_walk_through() {
		return video_walk_through;
	}

	public void setVideo_walk_through(String video_walk_through) {
		this.video_walk_through = video_walk_through;
	}

	public String getProject_plan_img() {
		return project_plan_img;
	}

	public void setProject_plan_img(String project_plan_img) {
		this.project_plan_img = project_plan_img;
	}

	// public String getProject_name() {
	// return project_name;
	// }
	//
	// public void setProject_name(String project_name) {
	// this.project_name = project_name;
	// }

	public String getLat_lng() {
		return lat_lng;
	}

	public void setLat_lng(String lat_lng) {
		this.lat_lng = lat_lng;
	}

	public String getSe() {
		return se;
	}

	public void setSe(String se) {
		this.se = se;
	}

	public String getNw() {
		return nw;
	}

	public void setNw(String nw) {
		this.nw = nw;
	}

	public ArrayList<String> getArrUnit_ids() {
		return arrUnit_ids;
	}

	public void setArrUnit_ids(ArrayList<String> arrUnit_ids) {
		this.arrUnit_ids = arrUnit_ids;
	}

	// public ArrayList<String> getArrProject_images() {
	// return arrProject_images;
	// }
	//
	// public void setArrProject_images(ArrayList<String> arrProject_images) {
	// this.arrProject_images = arrProject_images;
	// }

	public RecreationVO getRecreationVO() {
		return recreationVO;
	}

	public void setRecreationVO(RecreationVO recreationVO) {
		this.recreationVO = recreationVO;
	}

	public String getPriceTrendsJson() {
		return priceTrendsJson;
	}

	public void setPriceTrendsJson(String priceTrendsJson) {
		this.priceTrendsJson = priceTrendsJson;
	}

	public ArrayList<MediaGellaryVO> getMediaGellary() {
		return mediaGellary;
	}

	public void setMediaGellary(ArrayList<MediaGellaryVO> mediaGellary) {
		this.mediaGellary = mediaGellary;
	}

	// =========== Unit Gallery

	public ArrayList<MediaGellaryVO> getUnitGellary() {
		return unitGellary;
	}

	public void setUnitGellary(ArrayList<MediaGellaryVO> unitGellary) {
		this.unitGellary = unitGellary;
	}

	// =====================================

	public String getLocality_insights() {
		return locality_insights;
	}

	public void setLocality_insights(String locality_insights) {
		this.locality_insights = locality_insights;
	}

	public boolean isUser_favourite() {
		return user_favourite;
	}

	public void setUser_favourite(boolean user_favourite) {
		this.user_favourite = user_favourite;
	}

	public String getRatings_average() {
		return ratings_average;
	}

	public void setRatings_average(String ratings_average) {
		this.ratings_average = ratings_average;
	}

	public String getBuilder_logo() {
		return builder_logo;
	}

	public void setBuilder_logo(String builder_logo) {
		this.builder_logo = builder_logo;
	}

	public String getBuilder_description() {
		return builder_description;
	}

	public void setBuilder_description(String builder_description) {
		this.builder_description = builder_description;
	}

	public String getArea_delevered() {
		return area_delevered;
	}

	public void setArea_delevered(String area_delevered) {
		this.area_delevered = area_delevered;
	}

	public String getEstablish_year() {
		return establish_year;
	}

	public void setEstablish_year(String establish_year) {
		this.establish_year = establish_year;
	}

	public ArrayList<CommentsVO> getComments_detail() {
		return comments_detail;
	}

	public void setComments_detail(ArrayList<CommentsVO> comments_detail) {
		this.comments_detail = comments_detail;
	}

	public String getMin_area_range() {
		return min_area_range;
	}

	public void setMin_area_range(String min_area_range) {
		this.min_area_range = min_area_range;
	}

	public String getMax_area_range() {
		return max_area_range;
	}

	public void setMax_area_range(String max_area_range) {
		this.max_area_range = max_area_range;
	}

	public ArrayList<PlacesLocationVO> getArrLandmarks() {
		return arrLandmarks;
	}

	public void setArrLandmarks(ArrayList<PlacesLocationVO> arrLandmarks) {
		this.arrLandmarks = arrLandmarks;
	}

	public String getBuilder_contactno() {
		return builder_contactno;
	}

	public void setBuilder_contactno(String builder_contactno) {
		this.builder_contactno = builder_contactno;
	}

	public String getBuilder_mobile() {
		return builder_mobile;
	}

	public void setBuilder_mobile(String builder_mobile) {
		this.builder_mobile = builder_mobile;
	}

	public int getTotal_units() {
		return total_units;
	}

	public void setTotal_units(int total_units) {
		this.total_units = total_units;
	}

	public CharSequence getprice_one_year() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getProject_Url(){
		return project_url;
	}
	
	public void setProject_Url(String project_url){
		this.project_url = project_url;
	}
	
	public String getPrice_Trends(){
		return price_trends;
	}
	
	public void setPrice_Trends(String price_trends){
		this.price_trends = price_trends;
	}
	
	public String getExactlocation(){
		return Exactlocation;
	}
	
	public void setExactlocation (String exactlocation){
		this.Exactlocation = exactlocation;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;

	}
	
	
}
