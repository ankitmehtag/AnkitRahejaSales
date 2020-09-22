package com.VO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewLaunch {
	private String banner_url;
	private String ID;
	private String show_text;
	private String address;
	private String project_name;
	private String builder_name;
	private String type;
	private float price;

	private String priceValue;

	private String infra;
	private String needs;
	private String life_style;

	private String bhk_1;
	private String bhk_2;
	private String bhk_3;
	private String bhk_4;
	private String bhk_5;

	private String bhk_sqft;
	private String bhk_sqft_1;
	private String bhk_sqft_2;
	private String bhk_sqft_3;
	private String bhk_sqft_4;

	private String bhk_price;
	private String bhk_price_1;
	private String bhk_price_2;
	private String bhk_price_3;
	private String bhk_price_4;

	private String price_trends;
	private String possession_date;

	private String under_construction;
	private String Exactlocation;

	private String unit_image = "";
	private String propertyType;
	private String unitId;
	private String unitStatus;
	private JSONArray unitDetailsJsonObj;
	private JSONArray unitSepcifications;

	private String propertySqft;
	private String prop_price_persq_unit;

	private boolean userFavourite = false;
	private String direction_facing;
	private String wpcf_flat_price_plc;
	private String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDirection_facing() {
		return direction_facing;
	}

	public void setDirection_facing(String direction_facing) {
		this.direction_facing = direction_facing;
	}

	public String getWpcf_flat_price_plc() {
		return wpcf_flat_price_plc;
	}

	public void setWpcf_flat_price_plc(String wpcf_flat_price_plc) {
		this.wpcf_flat_price_plc = wpcf_flat_price_plc;
	}

	public NewLaunch(JSONObject jobj) {
		setID(jobj.optString("ID"));
		setBanner_url(jobj.optString("banner_url"));
		setShow_text(jobj.optString("show_text"));
		setAddress(jobj.optString("address"));
		setProject_name(jobj.optString("project_name"));
		setBuilder_name(jobj.optString("builder_name"));
		setType(jobj.optString("type"));
		setPrice((float) jobj.optDouble("price"));
		setPriceValue(jobj.optString("price"));

		setInfra(jobj.optString("infra"));
		setNeeds(jobj.optString("needs"));
		setLife_style(jobj.optString("life_style"));

		setbhk_1(jobj.optString("1bhk"));
		setbhk_2(jobj.optString("2bhk"));
		setbhk_3(jobj.optString("3bhk"));
		setbhk_4(jobj.optString("4bhk"));
		setbhk_5(jobj.optString("5bhk"));

		setbhk_sqft(jobj.optString("1bhkarea_range"));
		setbhk_sqft_1(jobj.optString("2bhkarea_range"));
		setbhk_sqft_2(jobj.optString("3bhkarea_range"));
		setbhk_sqft_3(jobj.optString("4bhkarea_range"));
		setbhk_sqft_4(jobj.optString("5bhkarea_range"));

		setbhk_price(jobj.optString("1bhk_price"));
		setbhk_price_1(jobj.optString("2bhk_price"));
		setbhk_price_2(jobj.optString("3bhk_price"));
		setbhk_price_3(jobj.optString("4bhk_price"));
		setbhk_price_4(jobj.optString("5bhk_price"));

		setPrice_trends(jobj.optString("price_one_year"));

		setUnder_construction(jobj.optString("project_status"));

		setPossession_date(jobj.optString("possession_date"));

		setExactlocation(jobj.optString("exactlocation"));
		setPropertyType(jobj.optString("property_type"));

		setUser_favourite(jobj.optBoolean("user_favourite"));

		setUnitImage(jobj.optString("unit_image"));
		setUnitId(jobj.optString("unit_id"));
		setUnitStatus(jobj.optString("unit_status"));
		setProjectSqft(jobj.optString("prop_price_persq"));
		setProp_price_persq_unit(jobj.optString("prop_price_persq_unit"));
		setDirection_facing(jobj.optString("direction_facing"));
		setWpcf_flat_price_plc(jobj.optString("wpcf_flat_price_plc"));
		/// iche pura jugad hai unit kr liye
		if (getPropertyType().equalsIgnoreCase("Unit")) {
			setDisplayName(jobj.optString("display_name") + " " + "(" + jobj.optString("size") + " "+ jobj.optString("size_unit")+")");
			setBuilder_name(jobj.optString("project_name"));
			setID(jobj.optString("unit_id"));
			setUnder_construction(jobj.optString("unit_status"));
			setUser_favourite(jobj.optBoolean("user_favourites"));
			setShow_text("Floor: " + jobj.optString("UnitFloor"));
			setExactlocation("Unit No: " + jobj.optString("unit_no"));

		} else {
			try {
				setUnitDetails(jobj.getJSONArray("unit_details"));
				setUnitSpecification(jobj.getJSONArray("unit_specifications"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getProp_price_persq_unit() {
		return prop_price_persq_unit;
	}

	public void setProp_price_persq_unit(String prop_price_persq_unit) {
		this.prop_price_persq_unit = prop_price_persq_unit;
	}

	public String getBanner_url() {
		return banner_url;
	}

	public void setBanner_url(String banner_url) {
		this.banner_url = banner_url;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getShow_text() {
		return show_text;
	}

	public void setShow_text(String show_text) {
		this.show_text = show_text;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getBuilder_name() {
		return builder_name;
	}

	public void setBuilder_name(String builder_name) {
		this.builder_name = builder_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
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

	public String getbhk_1() {
		return bhk_1;
	}

	public void setbhk_1(String bhk_1) {
		this.bhk_1 = bhk_1;
	}

	public String getbhk_2() {
		return bhk_2;
	}

	public void setbhk_2(String bhk_2) {
		this.bhk_2 = bhk_2;
	}

	public String getbhk_3() {
		return bhk_3;
	}

	public void setbhk_3(String bhk_3) {
		this.bhk_3 = bhk_3;
	}

	public String getbhk_4() {
		return bhk_4;
	}

	public void setbhk_4(String bhk_4) {
		this.bhk_4 = bhk_4;
	}

	public String getbhk_5() {
		return bhk_5;
	}

	public void setbhk_5(String bhk_5) {
		this.bhk_5 = bhk_5;
	}

	public String getbhk_sqft() {
		return bhk_sqft;
	}

	public void setbhk_sqft(String bhk_sqft) {
		this.bhk_sqft = bhk_sqft;
	}

	public String getbhk_sqft_1() {
		return bhk_sqft_1;
	}

	public void setbhk_sqft_1(String bhk_sqft_1) {
		this.bhk_sqft_1 = bhk_sqft_1;
	}

	public String getbhk_sqft_2() {
		return bhk_sqft_2;
	}

	public void setbhk_sqft_2(String bhk_sqft_2) {
		this.bhk_sqft_2 = bhk_sqft_2;
	}

	public String getbhk_sqft_3() {
		return bhk_sqft_3;
	}

	public void setbhk_sqft_3(String bhk_sqft_3) {
		this.bhk_sqft_3 = bhk_sqft_3;
	}

	public String getbhk_sqft_4() {
		return bhk_sqft_4;
	}

	public void setbhk_sqft_4(String bhk_sqft_4) {
		this.bhk_sqft_4 = bhk_sqft_4;
	}

	public String getbhk_price() {
		return bhk_price;
	}

	public void setbhk_price(String bhk_price) {
		this.bhk_price = bhk_price;
	}

	public String getbhk_price_1() {
		return bhk_price_1;
	}

	public void setbhk_price_1(String bhk_price_1) {
		this.bhk_price_1 = bhk_price_1;
	}

	public String getbhk_price_2() {
		return bhk_price_2;
	}

	public void setbhk_price_2(String bhk_price_2) {
		this.bhk_price_2 = bhk_price_2;
	}

	public String getbhk_price_3() {
		return bhk_price_3;
	}

	public void setbhk_price_3(String bhk_price_3) {
		this.bhk_price_3 = bhk_price_3;
	}

	public String getbhk_price_4() {
		return bhk_price_4;
	}

	public void setbhk_price_4(String bhk_price_4) {
		this.bhk_price_4 = bhk_price_4;
	}

	public String getPossession_date() {
		return possession_date;
	}

	public void setPossession_date(String possession_date) {
		this.possession_date = possession_date;
	}

	public String getUnder_construction() {
		return under_construction;
	}

	public void setUnder_construction(String under_construction) {
		this.under_construction = under_construction;
	}

	public String getPrice_trends() {
		return price_trends;
	}

	public void setPrice_trends(String price_trends) {
		this.price_trends = price_trends;
	}

	public String getExactlocation() {
		return Exactlocation;
	}

	public void setExactlocation(String exactlocation) {
		this.Exactlocation = exactlocation;
	}

	public String getUnitImage() {
		return unit_image;
	}

	public void setUnitImage(String unit_image) {
		this.unit_image = unit_image;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unit_id) {
		this.unitId = unit_id;
	}

	public String getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(String unit_status) {
		this.unitStatus = unit_status;
	}

	public void setUnitDetails(JSONArray obj) {
		this.unitDetailsJsonObj = obj;
		String stat = "";

		for (int i = 0; i < obj.length(); i++) {
			try {
				stat = obj.getJSONObject(i).getString("unit_status");
				break;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// System.out.println("New Launch 411 : status "+stat);
		setUnder_construction(stat);

	}

	public JSONArray getUnitDetails() {
		return this.unitDetailsJsonObj;
	}

	public void setUnitSpecification(JSONArray obj) {
		this.unitSepcifications = obj;
	}

	public JSONArray getUnitSpecification() {
		return this.unitSepcifications;
	}

	public boolean isUser_favourite() {
		return userFavourite;
	}

	public void setUser_favourite(boolean user_favourite) {
		this.userFavourite = user_favourite;
	}

	public String getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(String price) {
		this.priceValue = price;
	}

	public String getProjectSqft() {
		return propertySqft;
	}

	public void setProjectSqft(String prop_price_persq) {
		this.propertySqft = prop_price_persq;
	}

}
