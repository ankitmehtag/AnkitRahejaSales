package com.VO;

import android.os.Parcel;
import android.os.Parcelable;

import com.AppEnums.APIType;
import com.jsonparser.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnitDetailVO extends BaseVO implements Parcelable{
	private String id;
	private String display_name;
	private String discount;
	private boolean user_favourite = false;
	private String ratings_average;
	private String sold_status;
	private String category;
	private String description;
	private String carpet_area;
	private String balcony_area;
	private String terrace_area;
	private String basement_area;
	private String super_area_loading;
	private String address;
	private String location_name;
	private String city_name;
	private String possession_dt;
	private String bsp;
	private String price_SqFt;
	private String price_SqFt_unit;
	private String unit_status;
	private boolean sample_flat;
	private String size;
	private String size_unit;
	private boolean parking_space;
	private String direction;
	private String  balconies;
	private String openparking;
	private String covered_parking;
	private String toilet;

//	private String min_price;
//	private String max_price;

	private String property_id;
	private String floor_id;
	private FacitlitiesVO facitlities;
	private KitchenVO kitchen;
	private AcVO ac;
	private String club_charges;
	private String parking_charges;
	private String builder_contactno;

	private String typology;

	private String se;
	private String nw;

	private String total_parking_tax;
	private String total_gst_vat_tax;
	private String total_plc;
	private String total_discounted_bsp;
	private String total_edc_idc;
	private String total_ibms;
	private String total_ifms;
	private String ser_tax_club_plc;
	private String gst_club;
	private String gst_plc;
	private String total_bsp_tax;
	private String booking_fees;
	private String grand_total;


	private String type;
	private String auction_start_price;
	private String auction_current_price;
	private String auction_date_create;
	private String auction_date_end;
	private String auction_id;



	private ArrayList<MediaGellaryVO> mediaGellary;
	private ArrayList<String> wow = new ArrayList<String>();
	private ArrayList<FlooringFittingWallsVO> flooring;
	private ArrayList<FlooringFittingWallsVO> fittings;
	private ArrayList<FlooringFittingWallsVO> walls;
	private ArrayList<String> payment_plans;
	private ArrayList<String> formFields;

	private String possession_plan;
	private String construction_plan;
	private String down_payment_plan;
	private int max_parking;
	private int min_parking;
	private int parking_interval;
	private String parking_service_tax;
	private double grand_total_int;
	private boolean isformavailable;
	private String total_rent;
	private String security_period;
	private String security_amount;
	private String maintance_charge;
	private String minimum_rent_period;
	private String flat_available_rent;
	private String project_name;
	private String image_unit;
	private String term_condition;
	private String builder_term_condition;
	private String UnitSqFt;
	private String plc;
	private String unit_no;
	private String builder_name;
	private String unitfloor;
	private String bedroom;
	private String lat_lng;
	private String min_area_range;
	private String max_area_range;
	private ArrayList<PlacesLocationVO> arrLandmarks;
	private String locality_insights;
	private String proj_name;
	private String builder_description;
	private ArrayList<CommentsVO> comments_detail;
	private ArrayList<String> arrType;
	private AmenitiesVO amenities;
	private ServicesVO servicesVO;
	private SafetyVO safetyVO;
	private RecreationVO recreationVO;
	private String price_trends;
	private String orginal_bsp;
	private String unit_image;
	private String isLotteryProject = "0";
	private String other_charges;

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	String share_url;

	public String getGst_club() {
		return gst_club;
	}

	public void setGst_club(String gst_club) {
		this.gst_club = gst_club;
	}

	public String getGst_plc() {
		return gst_plc;
	}

	public void setGst_plc(String gst_plc) {
		this.gst_plc = gst_plc;
	}

	public String getOther_charges() {
		return other_charges;
	}

	public void setOther_charges(String other_charges) {
		this.other_charges = other_charges;
	}


	public String getIsLotteryProject() {
		return isLotteryProject;
	}

	public void setIsLotteryProject(String isLotteryProject) {
		this.isLotteryProject = isLotteryProject;
	}

	private boolean is_comment_list_needed;

	public boolean is_comment_list_needed() {
		return is_comment_list_needed;
	}

	public void setIs_comment_list_needed(boolean is_comment_list_needed) {
		this.is_comment_list_needed = is_comment_list_needed;
	}
	public UnitDetailVO() {
		super();
	}

	public UnitDetailVO(JSONObject obj) {
		super(obj);
		JSONObject objJson = obj.optJSONObject("data");
		if(objJson!=null){

			setShare_url(objJson.optString("sharing_url"));
			setIs_comment_list_needed(objJson.optBoolean("is_comment_list_needed"));
			setId(objJson.optString("id"));
			setDisplay_name(objJson.optString("display_name"));
			setDiscount(objJson.optString("discount"));
			setUser_favourite(objJson.optBoolean("user_favourite"));
			setRatings_average(objJson.optString("ratings_average"));
			setAddress(objJson.optString("address"));
			setBalconies(objJson.optString("balconies"));
			setBalcony_area(objJson.optString("balcony_area"));
			setBasement_area(objJson.optString("basement_area"));
			setCarpet_area(objJson.optString("carpet_area"));
			setCategory(objJson.optString("category"));
			setCity_name(objJson.optString("city_name"));
			setCovered_parking(objJson.optString("covered_parking"));
			setDescription(objJson.optString("description"));
			setDirection(objJson.optString("direction"));
			setTerrace_area(objJson.optString("terrace_area"));
			setSuper_area_loading(objJson.optString("super_area_loading"));
			setLocation_name(objJson.optString("location_name"));
			setPossession_dt(objJson.optString("possession_dt"));
			setBsp(objJson.optString("bsp"));
			setPrice_SqFt(objJson.optString("price_SqFt"));
			setPrice_SqFt_unit(objJson.optString("price_SqFt_unit"));
			setSample_flat(objJson.optBoolean("sample_flat"));
			setSize(objJson.optString("size"));
			setSize_unit(objJson.optString("size_unit"));
			setParking_space(objJson.optBoolean("parking_space"));
			setOpenparking(objJson.optString("openparking"));
			setToilet(objJson.optString("toilet"));

			setProperty_id(objJson.optString("property_id"));
			setFloor_id(objJson.optString("floor_id"));
			setUnit_status(objJson.optString("unit_status"));
			setTypology(objJson.optString("typology"));
			setType(objJson.optString("type"));
			setAuction_current_price(objJson.optString("auction_current_price"));
			setAuction_date_create(objJson.optString("auction_date_create"));
			setAuction_date_end(objJson.optString("auction_date_end"));
			setAuction_start_price(objJson.optString("auction_start_price"));

			setTerm_condition(objJson.optString("term_condition"));
			setBuilder_term_condition(objJson.optString("builder_term_condition"));
			//======================== Add New Feature ================================================================

			setUnit_SqFt(objJson.optString("UnitSqFt"));
			setplc(objJson.optString("total_plc"));
			setunitNo(objJson.optString("unit_no"));
			setBuilderName(objJson.optString("builder_name"));
			setUnitFloor(objJson.optString("UnitFloor"));
			setBedRoom(objJson.optString("bedroom_no"));

			setBuilder_description(objJson.optString("builder_description"));

			setProj_name(objJson.optString("proj_name"));
			setLat_lng(objJson.optString("lat_lng"));
			setLocality_insights(objJson.optString("locality_insights"));
			setMin_area_range(objJson.optString("min_area_range"));
			setMax_area_range(objJson.optString("max_area_range"));


			/*setAmenitiesVO(new AmenitiesVO(objJson.optJSONArray("Amenities")));
			setServicesVO(new ServicesVO(objJson.optJSONArray("services")));
			setSafetyVO(new SafetyVO(objJson.optJSONArray("safety")));
			setRecreationVO(new RecreationVO(objJson.optJSONArray("recreation")));*/

			setAmenitiesVO(new AmenitiesVO(objJson.optJSONArray("Amenities")));
			setServicesVO(new ServicesVO(objJson.optJSONArray("services")));
			setSafetyVO(new SafetyVO(objJson.optJSONArray("safety")));
			setRecreationVO(new RecreationVO(objJson.optJSONArray("recreation")));

			setImage_unit(objJson.optString("image_unit"));


			setTotal_plc(objJson.optString("total_plc"));
			setTotal_edc_idc(objJson.optString("total_edc_idc"));
			setTotal_ibms(objJson.optString("total_ibms"));
			setTotal_ifms(objJson.optString("total_ifms"));
			setClub_charges(objJson.optString("club_charges"));
			setParking_charges(objJson.optString("parking_charges"));
			setSer_tax_club_plc(objJson.optString("ser_tax_club_plc"));
			//TODO:
			setGst_club(objJson.optString("gst_club"));
			setGst_plc(objJson.optString("gst_plc"));

			setTotal_gst_vat_tax(objJson.optString("total_gst_vat_tax"));
			setBooking_fees(objJson.optString("booking_fees"));
			setTotal_discounted_bsp(objJson.optString("total_discounted_bsp"));
			setTotal_bsp_tax(objJson.optString("total_bsp_tax"));
			setTotal_parking_tax(objJson.optString("total_parking_tax"));
			setGrand_total(objJson.optString("grand_total"));
			setSold_status(objJson.optString("sold_status"));
			setBuilder_contactno(objJson.optString("builder_contactno"));
			JSONObject objfacitlities = objJson.optJSONObject("facitlities");

			setOrginalbsp(objJson.optString("original_bsp"));


			setPrice_Trends(objJson.optString("price_trends"));

//			setMin_price(objJson.optString("min_price"));
//			setMax_price(objJson.optString("max_price"));


			setUnit_image(objJson.optString("unit_image"));
			setIsLotteryProject(objJson.optString("isLotteryProject"));
			setOther_charges(objJson.optString("other_charges"));






//==================  Map Insights
			try {
				JSONObject cordsJson = objJson.getJSONObject("cords");
				setSe(cordsJson.optString("se"));
				setNw(cordsJson.optString("nw"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(objfacitlities != null){
				setFacitlities(new FacitlitiesVO(objfacitlities));
			}
			JSONObject objKitchen = objJson.optJSONObject("kitchen");
			if(objKitchen != null){
				setKitchen(new KitchenVO(objKitchen));
			}

			// check data for this field in json and then parse accordinglly
			JSONObject objAC = objJson.optJSONObject("AC");
			if(objAC != null){
				setAc(new AcVO(objAC));
			}

			JSONArray arrWow = objJson.optJSONArray("wow");
			ArrayList<String> aListwow = new ArrayList<String>();
			if(arrWow != null){
				for(int j = 0 ; j<arrWow.length() ; j++){
					aListwow.add(arrWow.optString(j));
				}
				setWow(aListwow);
			}

			//========================  Review Comment

			JSONArray arrjsoncomments_detail = objJson.optJSONArray("comments_detail");
			ArrayList<CommentsVO> arrComments = new ArrayList<CommentsVO>();
			if (arrjsoncomments_detail != null) {
				for (int i = 0; i < arrjsoncomments_detail.length(); i++) {
					JSONObject obj1 = arrjsoncomments_detail.optJSONObject(i);
					if (obj1 != null) {
						CommentsVO vo = (CommentsVO) JsonParser.convertJsonToBean(APIType.COMMENT,obj1.toString());
						arrComments.add(vo);
					}
				}
				this.setComments_detail(arrComments);
			}




			//============================   Place map
			JSONArray arrjsonLandmark = objJson.optJSONArray("project_landmark");
			ArrayList<PlacesLocationVO> arrLand = new ArrayList<PlacesLocationVO>();
			if (arrjsonLandmark != null) {
				for (int i = 0; i < arrjsonLandmark.length(); i++) {
					JSONObject obje = arrjsonLandmark.optJSONObject(i);
					if (obje != null) {
						PlacesLocationVO vo = new PlacesLocationVO(obje);

						arrLand.add(vo);
					}
				}
				this.setArrLandmarks(arrLand);
			}

			//==============================

			ArrayList<FlooringFittingWallsVO> arrflooring = new ArrayList<FlooringFittingWallsVO>();
			try {
				JSONArray arrJsonFloor = objJson.getJSONArray("flooring");
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
				JSONArray arrJsonFloor = objJson.getJSONArray("fittings");
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

			ArrayList<FlooringFittingWallsVO> arrWalls = new ArrayList<FlooringFittingWallsVO>();
			try {
				JSONArray arrJsonFloor = objJson.getJSONArray("walls");
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


//		JSONArray jarrImages= objJson.optJSONArray("images");
//		ArrayList<String> arrImages = new ArrayList<String>();
//		if(jarrImages != null)
//		{
//			for(int j = 0 ; j<jarrImages.length() ; j++)
//			{
//				arrImages.add(jarrImages.optString(j));
//			}
//			setImages(arrImages);
//		}

		JSONArray arrjsonGellary =objJson.optJSONArray("images");
		ArrayList<MediaGellaryVO> arrMediaGallery = new ArrayList<MediaGellaryVO>();
		if(arrjsonGellary != null){
			for(int i = 0; i<arrjsonGellary.length(); i++){
				JSONObject o = arrjsonGellary.optJSONObject(i);
				if(o != null){
					MediaGellaryVO vo = (MediaGellaryVO) JsonParser.convertJsonToBean(APIType.MEDIA_GALLARY,o.toString());
					arrMediaGallery.add(vo);
				}
			}
			this.setMediaGellary(arrMediaGallery);
		}

		JSONArray arrPlans = objJson.optJSONArray("payment_plans");
		payment_plans = new ArrayList<String>();
		if(arrPlans != null){
			for(int j = 0 ; j<arrPlans.length() ; j++){
				payment_plans.add(arrPlans.optString(j));
			}
			setPayment_plans(payment_plans);
		}

		JSONArray arrForm = objJson.optJSONArray("form_fields");
		formFields = new ArrayList<String>();
		if(arrForm != null){
			for(int j = 0 ; j<arrForm.length() ; j++){
				formFields.add(arrForm.optString(j));
			}
			setFormFields(formFields);
		}

//		======================================   Project unit Specification
		JSONArray arrType = objJson.optJSONArray("type");
		ArrayList<String> aListType = new ArrayList<String>();
		if (arrType != null) {
			for (int i = 0; i < arrType.length(); i++) {
				aListType.add(arrType.optString(i));
			}
		}
		this.setArrType(aListType);

		JSONArray arrUnit_ids = objJson.optJSONArray("unit_ids");
		ArrayList<String> aListUnitIds = new ArrayList<String>();
		if (arrUnit_ids != null) {
			for (int i = 0; i < arrUnit_ids.length(); i++) {
				aListUnitIds.add(arrUnit_ids.optString(i));
			}
		}
//		this.setArrUnit_ids(aListUnitIds);

		//===========================================

		setMax_parking(objJson.optInt("max_parking"));
		setMin_parking(objJson.optInt("min_parking"));
		setParking_interval(objJson.optInt("parking_interval"));
		setPossession_plan(objJson.optString("possession_plan"));
		setConstruction_plan(objJson.optString("construction_plan"));
		setDown_payment_plan(objJson.optString("down_payment_plan"));
		setParking_service_tax(objJson.optString("parking_service_tax"));
		setGrand_total_int(objJson.optDouble("grand_total_int"));
		setIsformavailable(objJson.optBoolean("isformavailable"));
		setAuction_id(objJson.optString("auction_id"));

		setTotal_rent(objJson.optString("total_rent"));
		setSecurity_amount(objJson.optString("security_amount"));
		setSecurity_period(objJson.optString("security_period"));
		setMaintance_charge(objJson.optString("maintance_charge"));
		setMinimum_rent_period(objJson.optString("minimum_rent_period"));
		setFlat_available_rent(objJson.optString("flat_available_rent"));
		setProject_name(objJson.optString("project_name"));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCarpet_area() {
		return carpet_area;
	}

	public void setCarpet_area(String carpet_area) {
		this.carpet_area = carpet_area;
	}

	public String getBalcony_area() {
		return balcony_area;
	}

	public void setBalcony_area(String balcony_area) {
		this.balcony_area = balcony_area;
	}

	public String getTerrace_area() {
		return terrace_area;
	}

	public void setTerrace_area(String terrace_area) {
		this.terrace_area = terrace_area;
	}

	public String getBasement_area() {
		return basement_area;
	}

	public void setBasement_area(String basement_area) {
		this.basement_area = basement_area;
	}

	public String getSuper_area_loading() {
		return super_area_loading;
	}

	public void setSuper_area_loading(String super_area_loading) {
		this.super_area_loading = super_area_loading;
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

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getPossession_dt() {
		return possession_dt;
	}

	public void setPossession_dt(String possession_dt) {
		this.possession_dt = possession_dt;
	}

	public String getPrice_SqFt() {
		return price_SqFt;
	}

	public void setPrice_SqFt(String price_SqFt) {
		this.price_SqFt = price_SqFt;
	}

	public String getPrice_SqFt_unit() {
		return price_SqFt_unit;
	}

	public void setPrice_SqFt_unit(String price_SqFt_unit) {
		this.price_SqFt_unit = price_SqFt_unit;
	}

	public boolean isSample_flat() {
		return sample_flat;
	}

	public void setSample_flat(boolean sample_flat) {
		this.sample_flat = sample_flat;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize_unit() {
		return size_unit;
	}

	public void setSize_unit(String size_unit) {
		this.size_unit = size_unit;
	}

	public boolean isParking_space() {
		return parking_space;
	}

	public void setParking_space(boolean parking_space) {
		this.parking_space = parking_space;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getBalconies() {
		return balconies;
	}

	public void setBalconies(String balconies) {
		this.balconies = balconies;
	}

	public String getOpenparking() {
		return openparking;
	}

	public void setOpenparking(String openparking) {
		this.openparking = openparking;
	}

	public String getToilet(){
		return toilet;
	}
	public void setToilet(String toilet){
		this.toilet= toilet;
	}



	public String getCovered_parking() {
		return covered_parking;
	}

	public void setCovered_parking(String covered_parking) {
		this.covered_parking = covered_parking;
	}

	public String getProperty_id() {
		return property_id;
	}

	public void setProperty_id(String property_id) {
		this.property_id = property_id;
	}

	public String getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}

	public ArrayList<FlooringFittingWallsVO> getFlooring() {
		return flooring;
	}

	public void setFlooring(ArrayList<FlooringFittingWallsVO> flooring) {
		this.flooring = flooring;
	}

	public ArrayList<FlooringFittingWallsVO> getFittings() {
		return fittings;
	}

	public void setFittings(ArrayList<FlooringFittingWallsVO> fittings) {
		this.fittings = fittings;
	}

	public ArrayList<FlooringFittingWallsVO> getWalls() {
		return walls;
	}

	public void setWalls(ArrayList<FlooringFittingWallsVO> walls) {
		this.walls = walls;
	}

	public FacitlitiesVO getFacitlities() {
		return facitlities;
	}

	public void setFacitlities(FacitlitiesVO facitlities) {
		this.facitlities = facitlities;
	}



	//=================================Add new Feature ====================================================================



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

	public String getUnit_SqFt(){
		return UnitSqFt;
	}

	public void setUnit_SqFt(String UnitSqFt){
		this.UnitSqFt = UnitSqFt;
	}


	public String getplc(){
		return total_plc;
	}

	public void setplc (String total_plc){
		this.total_plc = total_plc;
	}

	public String getunitNo(){
		return unit_no;
	}

	public void setunitNo (String unit_no){

		this.unit_no = unit_no;
	}


	public String getBuilderName(){
		return builder_name;
	}

	public void setBuilderName (String builder_name){
		this.builder_name = builder_name;
	}

	public String getUnitFloor(){
		return unitfloor;
	}

	public void setUnitFloor (String UnitFloor){
		this.unitfloor = UnitFloor;
	}

	public String getBedRoom(){
		return bedroom;
	}

	public void setBedRoom (String bedroom_no){
		this.bedroom = bedroom_no;
	}



	public ArrayList<String> getArrType() {
		return arrType;
	}

	public void setArrType(ArrayList<String> arrType) {
		this.arrType = arrType;
	}
	//===================================  Google place map.

	public String getProj_name() {
		return proj_name;
	}

	public void setProj_name(String proj_name) {
		this.proj_name = proj_name;
	}

	public String getLocality_insights() {
		return locality_insights;
	}

	public void setLocality_insights(String locality_insights) {
		this.locality_insights = locality_insights;
	}

	public String getLat_lng() {
		return lat_lng;
	}

	public void setLat_lng(String lat_lng) {
		this.lat_lng = lat_lng;
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



	public String getBuilder_description() {
		return builder_description;
	}

	public void setBuilder_description(String builder_description) {
		this.builder_description = builder_description;
	}

	public ArrayList<CommentsVO> getComments_detail() {
		return comments_detail;
	}

	public void setComments_detail(ArrayList<CommentsVO> comments_detail) {
		this.comments_detail = comments_detail;
	}



//==============================================  Unit Specification


	public AmenitiesVO getAmenitiesVO() {
		return amenities;
	}

	public void setAmenitiesVO(AmenitiesVO amenities) {
		this.amenities = amenities;
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

	public RecreationVO getRecreationVO() {
		return recreationVO;
	}

	public void setRecreationVO(RecreationVO recreationVO) {
		this.recreationVO = recreationVO;
	}

	//=====================================================================================

	public String getUnit_status() {
		return unit_status;
	}

	public void setUnit_status(String unit_status) {
		this.unit_status = unit_status;
	}

	public KitchenVO getKitchen() {
		return kitchen;
	}

	public void setKitchen(KitchenVO kitchen) {
		this.kitchen = kitchen;
	}


	public AcVO getAc() {
		return ac;
	}

	public void setAc(AcVO ac) {
		this.ac = ac;
	}

	public ArrayList<MediaGellaryVO> getMediaGellary() {
		return mediaGellary;
	}

	public void setMediaGellary(ArrayList<MediaGellaryVO> mediaGellary) {
		this.mediaGellary = mediaGellary;
	}


	public String getClub_charges() {
		return club_charges;
	}

	public void setClub_charges(String club_charges) {
		this.club_charges = club_charges;
	}

	public String getParking_charges() {
		return parking_charges;
	}

	public void setParking_charges(String parking_charges) {
		this.parking_charges = parking_charges;
	}

	//=======================================  Payment Plan Unit Image

 	public String getImage_unit() {
		return image_unit;
	}

	public void setImage_unit(String image_unit) {
		this.image_unit = image_unit;
	}




	public String getOrginalbsp() {
		return orginal_bsp;
	}

	public void setOrginalbsp(String orginal_bsp) {
		this.orginal_bsp = orginal_bsp;
	}



	public String getUnit_image(){
		return unit_image;
	}

	public void setUnit_image(String unit_image) {
		this.unit_image = unit_image;
	}



//	public String getService_tax() {
//		return service_tax;
//	}

//	public void setService_tax(String service_tax) {
//		this.service_tax = service_tax;
//	}

//	public String getGst_vat() {
//		return gst_vat;
//	}
//
//	public void setGst_vat(String gst_vat) {
//		this.gst_vat = gst_vat;
//	}

	public String getTypology() {
		return typology;
	}

	public void setTypology(String typology) {
		this.typology = typology;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getDisplay_name());
		dest.writeString(getId());
		dest.writeString(getPrice_SqFt());
		dest.writeString(getPrice_SqFt_unit());
		dest.writeString(getBsp());
		dest.writeString(getTotal_plc());
		dest.writeString(getTotal_edc_idc());
		dest.writeString(getTotal_ibms());
		dest.writeString(getTotal_ifms());
		dest.writeString(getClub_charges());
		dest.writeString(getParking_charges());
		dest.writeString(getSer_tax_club_plc());

		dest.writeString(getGst_club());
		dest.writeString(getGst_plc());

		dest.writeString(getTotal_gst_vat_tax());
		dest.writeString(getTotal_parking_tax());
		dest.writeString(getSize());
		dest.writeString(getSize_unit());
		dest.writeString(getTypology());
		dest.writeString(getBooking_fees());
		dest.writeString(getGrand_total());

		dest.writeString(getTotal_discounted_bsp());
		dest.writeString(getTotal_bsp_tax());

		dest.writeInt(getMax_parking());
		dest.writeInt(getMin_parking());
		dest.writeInt(getParking_interval());
		dest.writeString(getPossession_plan());
		dest.writeString(getConstruction_plan());
		dest.writeString(getDown_payment_plan());
		dest.writeStringList(getPayment_plans());
		dest.writeString(getParking_service_tax());
		dest.writeDouble(getGrand_total_int());
		dest.writeString(getProperty_id());
		dest.writeByte((byte) (isIsformavailable() ? 1 : 0));
		dest.writeStringList(getFormFields());

		dest.writeString(getAuction_current_price());
		dest.writeString(getAuction_date_end());
		dest.writeString(getAuction_start_price());

		dest.writeString(getTotal_rent());
		dest.writeString(getSecurity_amount());
		dest.writeString(getSecurity_period());
		dest.writeString(getMaintance_charge());
		dest.writeString(getMinimum_rent_period());
		dest.writeString(getFlat_available_rent());

		dest.writeString(getBuilderName());
		dest.writeString(getSize());
		dest.writeString(getSize_unit());
		dest.writeString(getunitNo());
		dest.writeString(getUnitFloor());
		dest.writeString(getProject_name());
		dest.writeString(getTypology());
		dest.writeString(getPossession_dt());
		dest.writeString(getBsp());
//		dest.writeList(getMediaGellary());

		dest.writeString(getImage_unit());
		dest.writeString(getOrginalbsp());
		dest.writeString(getUnit_image());
		dest.writeString(getTerm_condition());
		dest.writeString(getBuilder_term_condition());
		dest.writeString(getBuilder_contactno());
		dest.writeString(getIsLotteryProject());
		dest.writeString(getOther_charges());
	}

	public static final Creator<UnitDetailVO> CREATOR = new Creator<UnitDetailVO>() {
		public UnitDetailVO[] newArray(int size) {
			return new UnitDetailVO[size];
		}

		public UnitDetailVO createFromParcel(Parcel in) {
			return new UnitDetailVO(in);
		}
	};

	public UnitDetailVO(Parcel in){
		super(in);

		this.setDisplay_name(in.readString());
		this.setId(in.readString());
		this.setPrice_SqFt(in.readString());
		this.setPrice_SqFt_unit(in.readString());
		this.setBsp(in.readString());
		this.setTotal_plc(in.readString());
		this.setTotal_edc_idc(in.readString());
		this.setTotal_ibms(in.readString());
		this.setTotal_ifms(in.readString());
		this.setClub_charges(in.readString());
		this.setParking_charges(in.readString());
		this.setSer_tax_club_plc(in.readString());

		this.setGst_club(in.readString());
		this.setGst_plc(in.readString());

		this.setTotal_gst_vat_tax(in.readString());
		this.setTotal_parking_tax(in.readString());
		this.setSize(in.readString());
		this.setSize_unit(in.readString());
		this.setTypology(in.readString());
		this.setBooking_fees(in.readString());
		this.setGrand_total(in.readString());

		this.setTotal_discounted_bsp(in.readString());
		this.setTotal_bsp_tax(in.readString());
		this.setMax_parking(in.readInt());
		this.setMin_parking(in.readInt());
		this.setParking_interval(in.readInt());
		this.setPossession_plan(in.readString());
		this.setConstruction_plan(in.readString());
		this.setDown_payment_plan(in.readString());
		payment_plans = new ArrayList<String>();
		in.readStringList(payment_plans);
		this.setParking_service_tax(in.readString());
		this.setGrand_total_int(in.readDouble());
		this.setProperty_id(in.readString());
		isformavailable = in.readByte() != 0;
		formFields = new ArrayList<String>();
		in.readStringList(formFields);

		setAuction_current_price(in.readString());
		setAuction_date_end(in.readString());
		setAuction_start_price(in.readString());

		setTotal_rent(in.readString());
		setSecurity_amount(in.readString());
		setSecurity_period(in.readString());
		setMaintance_charge(in.readString());
		setMinimum_rent_period(in.readString());
		setFlat_available_rent(in.readString());

		setBuilderName(in.readString());
		setSize(in.readString());
		setSize_unit(in.readString());
		setunitNo(in.readString());
		setUnitFloor(in.readString());
		setProject_name(in.readString());
		setTypology(in.readString());
		setPossession_dt(in.readString());
		setBsp(in.readString());
//		setMediaGellary(in.readString());

		setImage_unit(in.readString());

		setOrginalbsp(in.readString());

		setUnit_image(in.readString());

		setTerm_condition(in.readString());
		setBuilder_term_condition(in.readString());
		setBuilder_contactno(in.readString());
		setIsLotteryProject(in.readString());
		setOther_charges(in.readString());


	}

	public String getBsp() {
		return bsp;
	}

	public void setBsp(String bsp) {
		this.bsp = bsp;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuction_start_price() {
		return auction_start_price;
	}

	public void setAuction_start_price(String auction_start_price) {
		this.auction_start_price = auction_start_price;
	}

	public String getAuction_current_price() {
		return auction_current_price;
	}

	public void setAuction_current_price(String auction_current_price) {
		this.auction_current_price = auction_current_price;
	}

	public String getAuction_date_create() {
		return auction_date_create;
	}

	public void setAuction_date_create(String auction_date_create) {
		this.auction_date_create = auction_date_create;
	}

	public String getAuction_date_end() {
		return auction_date_end;
	}

	public void setAuction_date_end(String auction_date_end) {
		this.auction_date_end = auction_date_end;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getTotal_parking_tax() {
		return total_parking_tax;
	}

	public void setTotal_parking_tax(String total_parking_tax) {
		this.total_parking_tax = total_parking_tax;
	}

	public String getTotal_gst_vat_tax() {
		return total_gst_vat_tax;
	}

	public void setTotal_gst_vat_tax(String total_gst_vat_tax) {
		this.total_gst_vat_tax = total_gst_vat_tax;
	}

	public String getTotal_plc() {
		return total_plc;
	}

	public void setTotal_plc(String total_plc) {
		this.total_plc = total_plc;
	}

	public String getTotal_edc_idc() {
		return total_edc_idc;
	}

	public void setTotal_edc_idc(String total_edc_idc) {
		this.total_edc_idc = total_edc_idc;
	}

	public String getTotal_ibms() {
		return total_ibms;
	}

	public void setTotal_ibms(String total_ibms) {
		this.total_ibms = total_ibms;
	}

	public String getTotal_ifms() {
		return total_ifms;
	}

	public void setTotal_ifms(String total_ifms) {
		this.total_ifms = total_ifms;
	}

	public String getSer_tax_club_plc() {
		return ser_tax_club_plc;
	}

	public void setSer_tax_club_plc(String ser_tax_club_plc) {
		this.ser_tax_club_plc = ser_tax_club_plc;
	}

//	public String getTotal_gst_vat() {
//		return total_gst_vat;
//	}
//
//	public void setTotal_gst_vat(String total_gst_vat) {
//		this.total_gst_vat = total_gst_vat;
//	}

	public String getBooking_fees() {
		return booking_fees;
	}

	public void setBooking_fees(String booking_fees) {
		this.booking_fees = booking_fees;
	}

	public String getGrand_total() {
		return grand_total;
	}

	public void setGrand_total(String grand_total) {
		this.grand_total = grand_total;
	}

	public String getTotal_bsp_tax() {
		return total_bsp_tax;
	}

	public void setTotal_bsp_tax(String total_bsp_tax) {
		this.total_bsp_tax = total_bsp_tax;
	}

	public String getTotal_discounted_bsp() {
		return total_discounted_bsp;
	}

	public void setTotal_discounted_bsp(String total_discounted_bsp) {
		this.total_discounted_bsp = total_discounted_bsp;
	}

	public String getSold_status() {
		return sold_status;
	}

	public void setSold_status(String sold_status) {
		this.sold_status = sold_status;
	}

	public String getBuilder_contactno() {
		return builder_contactno;
	}

	public void setBuilder_contactno(String builder_contactno) {
		this.builder_contactno = builder_contactno;
	}

	public ArrayList<String> getPayment_plans() {
		return payment_plans;
	}

	public void setPayment_plans(ArrayList<String> payment_plans) {
		this.payment_plans = payment_plans;
	}

	public String getPossession_plan() {
		return possession_plan;
	}

	public void setPossession_plan(String possession_plan) {
		this.possession_plan = possession_plan;
	}

	public String getConstruction_plan() {
		return construction_plan;
	}

	public void setConstruction_plan(String construction_plan) {
		this.construction_plan = construction_plan;
	}

	public String getDown_payment_plan() {
			return down_payment_plan;
	}

	public void setDown_payment_plan(String down_payment_plan) {
		this.down_payment_plan = down_payment_plan;
	}

	public int getMax_parking() {
		return max_parking;
	}

	public void setMax_parking(int max_parking) {
		this.max_parking = max_parking;
	}

	public int getMin_parking() {
		return min_parking;
	}

	public void setMin_parking(int min_parking) {
		this.min_parking = min_parking;
	}

	public int getParking_interval() {
		return parking_interval;
	}

	public void setParking_interval(int parking_interval) {
		this.parking_interval = parking_interval;
	}

	public String getParking_service_tax() {
		return parking_service_tax;
	}

	public void setParking_service_tax(String parking_service_tax) {
		this.parking_service_tax = parking_service_tax;
	}

	public double getGrand_total_int() {
		return grand_total_int;
	}

	public void setGrand_total_int(double grand_total_int) {
		this.grand_total_int = grand_total_int;
	}

	public boolean isIsformavailable() {
		return isformavailable;
	}

	public void setIsformavailable(boolean isformavailable) {
		this.isformavailable = isformavailable;
	}

	public ArrayList<String> getFormFields() {
		return formFields;
	}

	public void setFormFields(ArrayList<String> formFields) {
		this.formFields = formFields;
	}

	public String getAuction_id() {
		return auction_id;
	}

	public void setAuction_id(String auction_id) {
		this.auction_id = auction_id;
	}

	public String getTotal_rent() {
		return total_rent;
	}

	public void setTotal_rent(String total_rent) {
		this.total_rent = total_rent;
	}

	public String getSecurity_period() {
		return security_period;
	}

	public void setSecurity_period(String security_period) {
		this.security_period = security_period;
	}

	public String getSecurity_amount() {
		return security_amount;
	}

	public void setSecurity_amount(String security_amount) {
		this.security_amount = security_amount;
	}

	public String getMaintance_charge() {
		return maintance_charge;
	}

	public void setMaintance_charge(String maintance_charge) {
		this.maintance_charge = maintance_charge;
	}

	public String getMinimum_rent_period() {
		return minimum_rent_period;
	}

	public void setMinimum_rent_period(String minimum_rent_period) {
		this.minimum_rent_period = minimum_rent_period;
	}

	public String getFlat_available_rent() {
		return flat_available_rent;
	}

	public void setFlat_available_rent(String flat_available_rent) {
		this.flat_available_rent = flat_available_rent;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

//	public AmenitiesVO getAmenitiesVO() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public String getPrice_Trends(){
		return price_trends;
	}

	public void setPrice_Trends(String price_trends){
		this.price_trends = price_trends;
	}


	public String getTerm_condition(){
		return term_condition;
	}

	public void setTerm_condition(String term_condition){
		this.term_condition = term_condition;
	}

	public String getBuilder_term_condition() {
		return builder_term_condition;
	}

	public void setBuilder_term_condition(String builder_term_condition) {
		this.builder_term_condition = builder_term_condition;
	}
}
