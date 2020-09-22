package com.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Naresh on 24-Jan-17.
 */

public class ProjectsListRespModel extends BaseRespModel implements Serializable {

    private ArrayList<String>legend;
    private ArrayList<Data> data;
    private String city_location;
    private ArrayList<ProjectsListRespModel.Cordinates> cityCodrs;// for local use

    public ArrayList<ProjectsListRespModel.Cordinates> getCityCodrs() {
        return cityCodrs;
    }

    public void setCityCodrs(ArrayList<ProjectsListRespModel.Cordinates> cityCodrs) {
        this.cityCodrs = cityCodrs;
    }

    public ArrayList<String> getLegend() {
        return legend;
    }

    public void setLegend(ArrayList<String> legend) {
        this.legend = legend;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getCity_location() {
        return city_location;
    }

    public void setCity_location(String city_location) {
        this.city_location = city_location;
    }

    public class Data implements Serializable{
        private String ID;
        private boolean is_commercial;
        private String banner_img;
        private String project_price;
        private String project_pricenum;
        private String builder_id;
        private String builder_name;
        private String price_min;
        private String price_max;
        private String psf;
        private String psf_unit;
        private String area_range_min;
        private String area_range_max;
        private String possession_date;
        private double latitude;
        private double longitude;
        private String possessionYear;
        private ArrayList<PriceTrends> price_trends;
        private String status;
        private String address;
        private String wpcf_prop_location;
        private String wpcf_prop_city;
        private String wpcf_prop_state;
        private String wpcf_prop_price_persq;
        private String wpcf_prop_size;
        private String display_name;
        private String project_status;
        private String unit_type;
        private String display_builder_name;
        private String infra;
        private String infraval;
        private String needs;
        private String needsval;
        private String life_style;
        private String life_styleval;
        private boolean gas;
        private boolean wifi;
        private boolean automation;
        private boolean parking;
        private boolean ac;
        private boolean power_backup;
        private boolean lift;
        private boolean security;
        private boolean swimming;
        private boolean gym;
        private String wardrobe;
        @SerializedName("min-p")
        private String min_p;
        @SerializedName("max-p")
        private String max_p;
        private String min_area_range;
        private String max_area_range;
        private String discount;
        private String ratings_average;
        private String type;
        private String project_type;
        private String project_price_range;
        private String gnirtSproject_unit_ids;
        private String project_id;
        private ArrayList<Cordinates> project_cord;
        private ArrayList<UnitSpecifications> unit_specifications;
        @SerializedName("2bhk")
        private String two_bhk;
        @SerializedName("2bhkarea_range")
        private String two_bhkarea_range;
        @SerializedName("2bhk_price")
        private String two_bhk_price;
        @SerializedName("4bhk")
        private String four_bhk;
        @SerializedName("4bhkarea_range")
        private String four_bhkarea_range;
        @SerializedName("4bhk_price")
        private String four_bhk_price;
        private String returnsval;
        private String price_one_year;
        private String exactlocation;
        private int psf_code;
        private int infra_code;
        private int needs_code;
        private int life_style_code;
        private int returns_code;
        private boolean favorite;

        public String getPsf_unit() {
            return psf_unit;
        }

        public void setPsf_unit(String psf_unit) {
            this.psf_unit = psf_unit;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public boolean is_commercial() {
            return is_commercial;
        }

        public void setIs_commercial(boolean is_commercial) {
            this.is_commercial = is_commercial;
        }

        public String getBanner_img() {
            return banner_img;
        }

        public void setBanner_img(String banner_img) {
            this.banner_img = banner_img;
        }

        public String getProject_price() {
            return project_price;
        }

        public void setProject_price(String project_price) {
            this.project_price = project_price;
        }

        public String getProject_pricenum() {
            return project_pricenum;
        }

        public void setProject_pricenum(String project_pricenum) {
            this.project_pricenum = project_pricenum;
        }

        public String getBuilder_id() {
            return builder_id;
        }

        public void setBuilder_id(String builder_id) {
            this.builder_id = builder_id;
        }

        public String getBuilder_name() {
            return builder_name;
        }

        public void setBuilder_name(String builder_name) {
            this.builder_name = builder_name;
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

        public String getPossession_date() {
            return possession_date;
        }

        public void setPossession_date(String possession_date) {
            this.possession_date = possession_date;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getPossessionYear() {
            return possessionYear;
        }

        public void setPossessionYear(String possessionYear) {
            this.possessionYear = possessionYear;
        }

        public ArrayList<PriceTrends> getPrice_trends() {
            return price_trends;
        }

        public void setPrice_trends(ArrayList<PriceTrends> price_trends) {
            this.price_trends = price_trends;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getWpcf_prop_location() {
            return wpcf_prop_location;
        }

        public void setWpcf_prop_location(String wpcf_prop_location) {
            this.wpcf_prop_location = wpcf_prop_location;
        }

        public String getWpcf_prop_city() {
            return wpcf_prop_city;
        }

        public void setWpcf_prop_city(String wpcf_prop_city) {
            this.wpcf_prop_city = wpcf_prop_city;
        }

        public String getWpcf_prop_state() {
            return wpcf_prop_state;
        }

        public void setWpcf_prop_state(String wpcf_prop_state) {
            this.wpcf_prop_state = wpcf_prop_state;
        }

        public String getWpcf_prop_price_persq() {
            return wpcf_prop_price_persq;
        }

        public void setWpcf_prop_price_persq(String wpcf_prop_price_persq) {
            this.wpcf_prop_price_persq = wpcf_prop_price_persq;
        }

        public String getWpcf_prop_size() {
            return wpcf_prop_size;
        }

        public void setWpcf_prop_size(String wpcf_prop_size) {
            this.wpcf_prop_size = wpcf_prop_size;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getProject_status() {
            return project_status;
        }

        public void setProject_status(String project_status) {
            this.project_status = project_status;
        }

        public String getUnit_type() {
            return unit_type;
        }

        public void setUnit_type(String unit_type) {
            this.unit_type = unit_type;
        }

        public String getDisplay_builder_name() {
            return display_builder_name;
        }

        public void setDisplay_builder_name(String display_builder_name) {
            this.display_builder_name = display_builder_name;
        }

        public String getInfra() {
            return infra;
        }

        public void setInfra(String infra) {
            this.infra = infra;
        }

        public String getInfraval() {
            return infraval;
        }

        public void setInfraval(String infraval) {
            this.infraval = infraval;
        }

        public String getNeeds() {
            return needs;
        }

        public void setNeeds(String needs) {
            this.needs = needs;
        }

        public String getNeedsval() {
            return needsval;
        }

        public void setNeedsval(String needsval) {
            this.needsval = needsval;
        }

        public String getLife_style() {
            return life_style;
        }

        public void setLife_style(String life_style) {
            this.life_style = life_style;
        }

        public String getLife_styleval() {
            return life_styleval;
        }

        public void setLife_styleval(String life_styleval) {
            this.life_styleval = life_styleval;
        }

        public boolean isGas() {
            return gas;
        }

        public void setGas(boolean gas) {
            this.gas = gas;
        }

        public boolean isWifi() {
            return wifi;
        }

        public void setWifi(boolean wifi) {
            this.wifi = wifi;
        }

        public boolean isAutomation() {
            return automation;
        }

        public void setAutomation(boolean automation) {
            this.automation = automation;
        }

        public boolean isParking() {
            return parking;
        }

        public void setParking(boolean parking) {
            this.parking = parking;
        }

        public boolean isAc() {
            return ac;
        }

        public void setAc(boolean ac) {
            this.ac = ac;
        }

        public boolean isPower_backup() {
            return power_backup;
        }

        public void setPower_backup(boolean power_backup) {
            this.power_backup = power_backup;
        }

        public boolean isLift() {
            return lift;
        }

        public void setLift(boolean lift) {
            this.lift = lift;
        }

        public boolean isSecurity() {
            return security;
        }

        public void setSecurity(boolean security) {
            this.security = security;
        }

        public boolean isSwimming() {
            return swimming;
        }

        public void setSwimming(boolean swimming) {
            this.swimming = swimming;
        }

        public boolean isGym() {
            return gym;
        }

        public void setGym(boolean gym) {
            this.gym = gym;
        }

        public String getWardrobe() {
            return wardrobe;
        }

        public void setWardrobe(String wardrobe) {
            this.wardrobe = wardrobe;
        }

        public String getMin_p() {
            return min_p;
        }

        public void setMin_p(String min_p) {
            this.min_p = min_p;
        }

        public String getMax_p() {
            return max_p;
        }

        public void setMax_p(String max_p) {
            this.max_p = max_p;
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

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
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

        public String getProject_type() {
            return project_type;
        }

        public void setProject_type(String project_type) {
            this.project_type = project_type;
        }

        public String getProject_price_range() {
            return project_price_range;
        }

        public void setProject_price_range(String project_price_range) {
            this.project_price_range = project_price_range;
        }

        public String getGnirtSproject_unit_ids() {
            return gnirtSproject_unit_ids;
        }

        public void setGnirtSproject_unit_ids(String gnirtSproject_unit_ids) {
            this.gnirtSproject_unit_ids = gnirtSproject_unit_ids;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public ArrayList<Cordinates> getProject_cord() {
            return project_cord;
        }

        public void setProject_cord(ArrayList<Cordinates> project_cord) {
            this.project_cord = project_cord;
        }

        public ArrayList<UnitSpecifications> getUnit_specifications() {
            return unit_specifications;
        }

        public void setUnit_specifications(ArrayList<UnitSpecifications> unit_specifications) {
            this.unit_specifications = unit_specifications;
        }

        public String getTwo_bhk() {
            return two_bhk;
        }

        public void setTwo_bhk(String two_bhk) {
            this.two_bhk = two_bhk;
        }

        public String getTwo_bhkarea_range() {
            return two_bhkarea_range;
        }

        public void setTwo_bhkarea_range(String two_bhkarea_range) {
            this.two_bhkarea_range = two_bhkarea_range;
        }

        public String getTwo_bhk_price() {
            return two_bhk_price;
        }

        public void setTwo_bhk_price(String two_bhk_price) {
            this.two_bhk_price = two_bhk_price;
        }

        public String getFour_bhk() {
            return four_bhk;
        }

        public void setFour_bhk(String four_bhk) {
            this.four_bhk = four_bhk;
        }

        public String getFour_bhkarea_range() {
            return four_bhkarea_range;
        }

        public void setFour_bhkarea_range(String four_bhkarea_range) {
            this.four_bhkarea_range = four_bhkarea_range;
        }

        public String getFour_bhk_price() {
            return four_bhk_price;
        }

        public void setFour_bhk_price(String four_bhk_price) {
            this.four_bhk_price = four_bhk_price;
        }

        public String getReturnsval() {
            return returnsval;
        }

        public void setReturnsval(String returnsval) {
            this.returnsval = returnsval;
        }

        public String getPrice_one_year() {
            return price_one_year;
        }

        public void setPrice_one_year(String price_one_year) {
            this.price_one_year = price_one_year;
        }

        public String getExactlocation() {
            return exactlocation;
        }

        public void setExactlocation(String exactlocation) {
            this.exactlocation = exactlocation;
        }

        public int getPsf_code() {
            return psf_code;
        }

        public void setPsf_code(int psf_code) {
            this.psf_code = psf_code;
        }

        public int getInfra_code() {
            return infra_code;
        }

        public void setInfra_code(int infra_code) {
            this.infra_code = infra_code;
        }

        public int getNeeds_code() {
            return needs_code;
        }

        public void setNeeds_code(int needs_code) {
            this.needs_code = needs_code;
        }

        public int getLife_style_code() {
            return life_style_code;
        }

        public void setLife_style_code(int life_style_code) {
            this.life_style_code = life_style_code;
        }

        public int getReturns_code() {
            return returns_code;
        }

        public void setReturns_code(int returns_code) {
            this.returns_code = returns_code;
        }

        public boolean isFavorite() {
            return favorite;
        }

        public void setFavorite(boolean favorite) {
            this.favorite = favorite;
        }

        public class PriceTrends implements Serializable{
            private String price_trend_date;
            private String price_trend_value;

            public String getPrice_trend_date() {
                return price_trend_date;
            }

            public void setPrice_trend_date(String price_trend_date) {
                this.price_trend_date = price_trend_date;
            }

            public String getPrice_trend_value() {
                return price_trend_value;
            }

            public void setPrice_trend_value(String price_trend_value) {
                this.price_trend_value = price_trend_value;
            }
        }


        public class UnitSpecifications implements Serializable{
            private String wpcf_flat_typology;
            private String area_range;
            private String price_range;
            private String projectId;

            public String getWpcf_flat_typology() {
                return wpcf_flat_typology;
            }

            public void setWpcf_flat_typology(String wpcf_flat_typology) {
                this.wpcf_flat_typology = wpcf_flat_typology;
            }

            public String getArea_range() {
                return area_range;
            }

            public void setArea_range(String area_range) {
                this.area_range = area_range;
            }

            public String getPrice_range() {
                return price_range;
            }

            public void setPrice_range(String price_range) {
                this.price_range = price_range;
            }

            public String getProjectId() {
                return projectId;
            }

            public void setProjectId(String projectId) {
                this.projectId = projectId;
            }
        }
    }

    public class Cordinates implements Serializable{
        private double lat;
        @SerializedName("long")
        private double lng;// hav to cast long

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }


    public ArrayList<ProjectsListRespModel.Cordinates> parseCords(String city_location){
        ArrayList<ProjectsListRespModel.Cordinates> tempCodrs = null;
        if(city_location != null){
            String removeLeftBraces = city_location.replace("[", "");
            String removeRightBraces = removeLeftBraces.replace("]", "");
            List<String> tempList = Arrays.asList(removeRightBraces.split(","));
            if(tempList != null){
                tempCodrs = new ArrayList<>();
                for(int i = 0; i < tempList.size()/2; i++){
                    ProjectsListRespModel.Cordinates mCordinates = new ProjectsListRespModel.Cordinates();
                    mCordinates.setLat(Double.parseDouble(tempList.get(2*i)));
                    mCordinates.setLng(Double.parseDouble(tempList.get((2*i)+1)));
                    tempCodrs.add(mCordinates);
                }
                return tempCodrs;
            }
        }
        return tempCodrs;
    }

}
