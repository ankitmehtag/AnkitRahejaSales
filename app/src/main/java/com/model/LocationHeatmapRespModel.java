package com.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Naresh on 20-Dec-16.
 */

public class LocationHeatmapRespModel implements Serializable {
    private boolean success;
    private String city_location;
    private ArrayList<String> legend;
    //private ArrayList<ArrayList<String>> legend;
    private ArrayList<LocationList> location_list;
    private ArrayList<Cordinates> codrs;// for local use

   /* public ArrayList<ArrayList<String>> getLegend() {
        return legend;
    }

    public void setLegend(ArrayList<ArrayList<String>> legend) {
        this.legend = legend;
    }
*/
    public ArrayList<String> getLegend() {
        return legend;
    }

    public void setLegend(ArrayList<String> legend) {
        this.legend = legend;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCity_location() {
        return city_location;
    }

    public void setCity_location(String city_location) {
        this.city_location = city_location;
    }

    public ArrayList<LocationList> getLocation_list() {
        return location_list;
    }

    public void setLocation_list(ArrayList<LocationList> location_list) {
        this.location_list = location_list;
    }

    public ArrayList<Cordinates> getCodrs() {
        return codrs;
    }

    public void setCodrs(ArrayList<Cordinates> codrs) {
        this.codrs = codrs;
    }

    public class LocationList implements Serializable{
        private String location_id;
        private String location_name;
        private String location_img;
        private int infra;
        private int needs;
        private int lifeStyle;
        private float returnsval;
        private String returns;
        private int total_projects;
        private float avgPsfLocation;
        private String avgPsfLocation_unit;
        private float avg_rating;
        private ArrayList<Cordinates> location_cordinates;
        private ArrayList<SectorDetails> sector_details;

        public String getAvgPsfLocation_unit() {
            return avgPsfLocation_unit;
        }

        public void setAvgPsfLocation_unit(String avgPsfLocation_unit) {
            this.avgPsfLocation_unit = avgPsfLocation_unit;
        }

        public String getLocation_id() {
            return location_id;
        }

        public void setLocation_id(String location_id) {
            this.location_id = location_id;
        }

        public String getLocation_name() {
            return location_name;
        }

        public void setLocation_name(String location_name) {
            this.location_name = location_name;
        }

        public String getLocation_img() {
            return location_img;
        }

        public void setLocation_img(String location_img) {
            this.location_img = location_img;
        }

        public int getInfra() {
            return infra;
        }

        public void setInfra(int infra) {
            this.infra = infra;
        }

        public int getNeeds() {
            return needs;
        }

        public void setNeeds(int needs) {
            this.needs = needs;
        }

        public int getLifeStyle() {
            return lifeStyle;
        }

        public void setLifeStyle(int lifeStyle) {
            this.lifeStyle = lifeStyle;
        }

        public float getReturnsval() {
            return returnsval;
        }

        public void setReturnsval(float returnsval) {
            this.returnsval = returnsval;
        }

        public String getReturns() {
            return returns;
        }

        public void setReturns(String returns) {
            this.returns = returns;
        }

        public int getTotal_projects() {
            return total_projects;
        }

        public void setTotal_projects(int total_projects) {
            this.total_projects = total_projects;
        }

        public float getAvgPsfLocation() {
            return avgPsfLocation;
        }

        public void setAvgPsfLocation(float avgPsfLocation) {
            this.avgPsfLocation = avgPsfLocation;
        }

        public float getAvg_rating() {
            return avg_rating;
        }

        public void setAvg_rating(float avg_rating) {
            this.avg_rating = avg_rating;
        }

        public ArrayList<Cordinates> getLocation_cordinates() {
            return location_cordinates;
        }

        public void setLocation_cordinates(ArrayList<Cordinates> location_cordinates) {
            this.location_cordinates = location_cordinates;
        }

        public ArrayList<SectorDetails> getSector_details() {
            return sector_details;
        }

        public void setSector_details(ArrayList<SectorDetails> sector_details) {
            this.sector_details = sector_details;
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

    public class SectorDetails implements Serializable{
        private String sector_name;
        private String sector_id;
        private String infra;
        private String needs;
        private String lifeStyle;
        private String psf_average;
        @SerializedName("return")
        private String ret;// have to cast return
        private ArrayList<Cordinates> sector_cordinates;

        public String getSector_name() {
            return sector_name;
        }

        public void setSector_name(String sector_name) {
            this.sector_name = sector_name;
        }

        public String getSector_id() {
            return sector_id;
        }

        public void setSector_id(String sector_id) {
            this.sector_id = sector_id;
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

        public String getLifeStyle() {
            return lifeStyle;
        }

        public void setLifeStyle(String lifeStyle) {
            this.lifeStyle = lifeStyle;
        }

        public String getPsf_average() {
            return psf_average;
        }

        public void setPsf_average(String psf_average) {
            this.psf_average = psf_average;
        }

        public String getRet() {
            return ret;
        }

        public void setRet(String ret) {
            this.ret = ret;
        }

        public ArrayList<Cordinates> getSector_cordinates() {
            return sector_cordinates;
        }

        public void setSector_cordinates(ArrayList<Cordinates> sector_cordinates) {
            this.sector_cordinates = sector_cordinates;
        }
    }


    public ArrayList<Cordinates> parseCords(String city_location){
        ArrayList<Cordinates> tempCodrs = null;
        if(city_location != null){
            String removeLeftBraces = city_location.replace("[", "");
            String removeRightBraces = removeLeftBraces.replace("]", "");
            List<String> tempList = Arrays.asList(removeRightBraces.split(","));
            if(tempList != null){
                tempCodrs = new ArrayList<>();
                for(int i = 0; i < tempList.size()/2; i++){
                    Cordinates mCordinates = new Cordinates();
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
