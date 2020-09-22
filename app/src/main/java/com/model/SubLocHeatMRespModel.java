package com.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 20-Dec-16.
 */

public class SubLocHeatMRespModel extends BaseRespModel implements Serializable {
    private String city_location;
    private ArrayList<String> legend;
    private ArrayList<Coordinate> micromarket_location;
    private ArrayList<SectorLists> sector_lists;

    public String getCity_location() {
        return city_location;
    }

    public void setCity_location(String city_location) {
        this.city_location = city_location;
    }

    public ArrayList<String> getLegend() {
        return legend;
    }

    public void setLegend(ArrayList<String> legend) {
        this.legend = legend;
    }

    public ArrayList<Coordinate> getMicromarket_location() {
        return micromarket_location;
    }

    public void setMicromarket_location(ArrayList<Coordinate> micromarket_location) {
        this.micromarket_location = micromarket_location;
    }

    public ArrayList<SectorLists> getSector_lists() {
        return sector_lists;
    }

    public void setSector_lists(ArrayList<SectorLists> sector_lists) {
        this.sector_lists = sector_lists;
    }

    public class SectorLists implements Serializable{
        private String sector_id;
        private String sector_name;
        private String sector_image;
        private int infra;
        private int needs;
        private int lifeStyle;
        private String returnval;
        private String returns;
        private int total_Projects;
        private float avg_PSF_Sector;
        private String avg_PSF_Sector_unit;
        private float avg_rating;
        private int infra_code;
        private int needs_code;
        private int lifeStyle_code;
        private int return_code;
        private int avg_pricePSF_code;
        private ArrayList<Coordinate> sector_cord;

        public String getAvg_PSF_Sector_unit() {
            return avg_PSF_Sector_unit;
        }

        public void setAvg_PSF_Sector_unit(String avg_PSF_Sector_unit) {
            this.avg_PSF_Sector_unit = avg_PSF_Sector_unit;
        }

        public String getSector_id() {
            return sector_id;
        }

        public void setSector_id(String sector_id) {
            this.sector_id = sector_id;
        }

        public String getSector_name() {
            return sector_name;
        }

        public void setSector_name(String sector_name) {
            this.sector_name = sector_name;
        }

        public String getSector_image() {
            return sector_image;
        }

        public void setSector_image(String sector_image) {
            this.sector_image = sector_image;
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

        public String getReturnval() {
            return returnval;
        }

        public void setReturnval(String returnval) {
            this.returnval = returnval;
        }

        public String getReturns() {
            return returns;
        }

        public void setReturns(String returns) {
            this.returns = returns;
        }

        public int getTotal_Projects() {
            return total_Projects;
        }

        public void setTotal_Projects(int total_Projects) {
            this.total_Projects = total_Projects;
        }

        public float getAvg_PSF_Sector() {
            return avg_PSF_Sector;
        }

        public void setAvg_PSF_Sector(float avg_PSF_Sector) {
            this.avg_PSF_Sector = avg_PSF_Sector;
        }

        public float getAvg_rating() {
            return avg_rating;
        }

        public void setAvg_rating(float avg_rating) {
            this.avg_rating = avg_rating;
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

        public int getLifeStyle_code() {
            return lifeStyle_code;
        }

        public void setLifeStyle_code(int lifeStyle_code) {
            this.lifeStyle_code = lifeStyle_code;
        }

        public int getReturn_code() {
            return return_code;
        }

        public void setReturn_code(int return_code) {
            this.return_code = return_code;
        }

        public int getAvg_pricePSF_code() {
            return avg_pricePSF_code;
        }

        public void setAvg_pricePSF_code(int avg_pricePSF_code) {
            this.avg_pricePSF_code = avg_pricePSF_code;
        }

        public ArrayList<Coordinate> getSector_cord() {
            return sector_cord;
        }

        public void setSector_cord(ArrayList<Coordinate> sector_cord) {
            this.sector_cord = sector_cord;
        }
    }

    public class Coordinate implements Serializable{
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

}
