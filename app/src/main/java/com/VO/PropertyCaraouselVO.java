package com.VO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PropertyCaraouselVO implements Parcelable{
	private String id;
	private String display_name;
	private String banner_img;
	private String type;
	private String price_min;
	private String price_max;
	private String psf;
	private String psf_unit;
	private String area_range_min;
	private String area_range_max;
	private String latitude;
	private String longitude;
	private String address;
	private String ratings_average;
	private String unit_type;
	private String status;
	private String builder_name;
	private String builder;
	private String builder_id;
	private String brokerage_term;

	public String getBrokerage_term() {
		return brokerage_term;
	}

	public void setBrokerage_term(String brokerage_term) {
		this.brokerage_term = brokerage_term;
	}

	public String getBuilder_id() {
		return builder_id;
	}

	public void setBuilder_id(String builder_id) {
		this.builder_id = builder_id;
	}

	private String infra;
	private String needs;
	private String life_style;

	private String under_construction;

	private String price_trends;

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

	private String sub_location_id;
	private String possession_date;
	private String project_price_range;
	private String Exactlocation;
	private JSONArray unitSepcifications;

	private boolean user_favourite = false;

	public PropertyCaraouselVO(JSONObject o) {
		setId(o.optString("project_id"));
		setDisplay_name(o.optString("display_name"));
		setBanner_img(o.optString("banner_img"));
		setType(o.optString("type"));
		setPrice_min(o.optString("price_min"));
		setPrice_max(o.optString("price_max"));
		setPsf(o.optString("psf"));
		setPsf_unit(o.optString("psf_unit"));
		setArea_range_min(o.optString("area_range_min"));
		setArea_range_max(o.optString("area_range_max"));
		setLatitude(o.optString("latitude"));
		setLongitude(o.optString("longitude"));
		setAddress(o.optString("address"));
		setRatings_average(o.optString("ratings_average"));
		setUnit_type(o.optString("unit_type"));
		setStatus(o.optString("status"));
		setBuilder_name(o.optString("builder_name"));
		setBuilder(o.optString("display_builder_name"));
		setBuilder_id(o.optString("builder_id"));
		setInfra(o.optString("infra"));
		setNeeds(o.optString("needs"));
		setLife_style(o.optString("life_style"));

		setUnder_construction(o.optString("project_status"));

		setPrice_trends(o.optString("price_one_year"));
		setPossession_date(o.optString("possession_date"));
		setProject_price_range(o.optString("project_price_range"));
		//setUser_favourite(o.optBoolean("user_favourite"));
		setUser_favourite(o.optBoolean("favorite"));
		setExactlocation(o.optString("exactlocation"));

		try {
			setUnitSpecification(o.getJSONArray("unit_specifications"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("exact location "+getDisplay_name());
		// ============ Set BHK


		setbhk_1(o.optString("1bhk"));
		setbhk_2(o.optString("2bhk"));
		setbhk_3(o.optString("3bhk"));
		setbhk_4(o.optString("4bhk"));
		setbhk_5(o.optString("5bhk"));

		setbhk_sqft(o.optString("1bhkarea_range"));
		setbhk_sqft_1(o.optString("2bhkarea_range"));
		setbhk_sqft_2(o.optString("3bhkarea_range"));
		setbhk_sqft_3(o.optString("4bhkarea_range"));
		setbhk_sqft_4(o.optString("5bhkarea_range"));

		setbhk_price(o.optString("1bhk_price"));
		setbhk_price_1(o.optString("2bhk_price"));
		setbhk_price_2(o.optString("3bhk_price"));
		setbhk_price_3(o.optString("4bhk_price"));
		setbhk_price_4(o.optString("5bhk_price"));
		setBrokerage_term(o.optString("brokerage_term"));


	}


	protected PropertyCaraouselVO(Parcel in) {
		id = in.readString();
		display_name = in.readString();
		banner_img = in.readString();
		type = in.readString();
		price_min = in.readString();
		price_max = in.readString();
		psf = in.readString();
		psf_unit = in.readString();
		area_range_min = in.readString();
		area_range_max = in.readString();
		latitude = in.readString();
		longitude = in.readString();
		address = in.readString();
		ratings_average = in.readString();
		unit_type = in.readString();
		status = in.readString();
		builder_name = in.readString();
		builder = in.readString();
		infra = in.readString();
		needs = in.readString();
		life_style = in.readString();
		under_construction = in.readString();
		price_trends = in.readString();
		bhk_1 = in.readString();
		bhk_2 = in.readString();
		bhk_3 = in.readString();
		bhk_4 = in.readString();
		bhk_5 = in.readString();
		bhk_sqft = in.readString();
		bhk_sqft_1 = in.readString();
		bhk_sqft_2 = in.readString();
		bhk_sqft_3 = in.readString();
		bhk_sqft_4 = in.readString();
		bhk_price = in.readString();
		bhk_price_1 = in.readString();
		bhk_price_2 = in.readString();
		bhk_price_3 = in.readString();
		bhk_price_4 = in.readString();
		sub_location_id = in.readString();
		possession_date = in.readString();
		project_price_range = in.readString();
		Exactlocation = in.readString();
		user_favourite = in.readByte() != 0;
		brokerage_term = in.readString();
	}

	public static final Creator<PropertyCaraouselVO> CREATOR = new Creator<PropertyCaraouselVO>() {
		@Override
		public PropertyCaraouselVO createFromParcel(Parcel in) {
			return new PropertyCaraouselVO(in);
		}

		@Override
		public PropertyCaraouselVO[] newArray(int size) {
			return new PropertyCaraouselVO[size];
		}
	};

	public String getPsf_unit() {
		return psf_unit;
	}

	public void setPsf_unit(String psf_unit) {
		this.psf_unit = psf_unit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getBanner_img() {
		return banner_img;
	}

	public void setBanner_img(String banner_img) {
		this.banner_img = banner_img;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrice_min() {
		return price_min;
	}

	public void setPrice_min(String price_min) {
		this.price_min = price_min;
	}

	public String getPrice_max() {
		return price_max;
	}

	public void setPrice_max(String price_max) {
		this.price_max = price_max;
	}

	public String getPsf() {
		return psf;
	}

	public void setPsf(String psf) {
		this.psf = psf;
	}

	public String getArea_range_min() {
		return area_range_min;
	}

	public void setArea_range_min(String area_range_min) {
		this.area_range_min = area_range_min;
	}

	public String getArea_range_max() {
		return area_range_max;
	}

	public void setArea_range_max(String area_range_max) {
		this.area_range_max = area_range_max;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRatings_average() {
		return ratings_average;
	}

	public void setRatings_average(String ratings_average) {
		this.ratings_average = ratings_average;
	}

	public String getUnit_type() {
		return unit_type;
	}

	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBuilder_name() {
		return builder_name;
	}

	public void setBuilder_name(String builder_name) {
		this.builder_name = builder_name;
	}

	public String getBuilder() {
		return builder;
	}

	public void setBuilder(String builder) {
		this.builder = builder;
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

	public String getProject_price_range() {
		return project_price_range;
	}

	public void setProject_price_range(String project_price_range) {
		this.project_price_range = project_price_range;
	}

	public boolean isUser_favourite() {
		return user_favourite;
	}

	public void setUser_favourite(boolean user_favourite) {
		this.user_favourite = user_favourite;
	}

	public String getExactlocation(){
		return this.Exactlocation;
	}

	public void setExactlocation (String exactlocation){
		this.Exactlocation = exactlocation;
	}

	public void setUnitSpecification(JSONArray obj) {
		this.unitSepcifications = obj;
	}

	public JSONArray getUnitSpecification() {
		return this.unitSepcifications;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(display_name);
		dest.writeString(banner_img);
		dest.writeString(type);
		dest.writeString(price_min);
		dest.writeString(price_max);
		dest.writeString(psf);
		dest.writeString(psf_unit);
		dest.writeString(area_range_min);
		dest.writeString(area_range_max);
		dest.writeString(latitude);
		dest.writeString(longitude);
		dest.writeString(address);
		dest.writeString(ratings_average);
		dest.writeString(unit_type);
		dest.writeString(status);
		dest.writeString(builder_name);
		dest.writeString(builder);
		dest.writeString(infra);
		dest.writeString(needs);
		dest.writeString(life_style);
		dest.writeString(under_construction);
		dest.writeString(price_trends);
		dest.writeString(bhk_1);
		dest.writeString(bhk_2);
		dest.writeString(bhk_3);
		dest.writeString(bhk_4);
		dest.writeString(bhk_5);
		dest.writeString(bhk_sqft);
		dest.writeString(bhk_sqft_1);
		dest.writeString(bhk_sqft_2);
		dest.writeString(bhk_sqft_3);
		dest.writeString(bhk_sqft_4);
		dest.writeString(bhk_price);
		dest.writeString(bhk_price_1);
		dest.writeString(bhk_price_2);
		dest.writeString(bhk_price_3);
		dest.writeString(bhk_price_4);
		dest.writeString(sub_location_id);
		dest.writeString(possession_date);
		dest.writeString(project_price_range);
		dest.writeString(Exactlocation);
		dest.writeByte((byte) (user_favourite ? 1 : 0));
		dest.writeString(brokerage_term);
	}
}


