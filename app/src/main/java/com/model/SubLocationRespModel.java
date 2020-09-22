package com.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 23-Jan-17.
 */

public class SubLocationRespModel extends BaseRespModel implements Serializable {

    private ArrayList<SectorLists> sector_lists;

    public ArrayList<SectorLists> getSector_lists() {
        return sector_lists;
    }

    public void setSector_lists(ArrayList<SectorLists> sector_lists) {
        this.sector_lists = sector_lists;
    }

    public class SectorLists{
        private String avgPSFLocation;
        private String avgPSFLocation_unit;
        private int infra;
        private int lifeStyle;
        private String sector_id;
        private String sector_name;
        private String sector_image;
        private int needs;
        @SerializedName("return")
        private String returns;
        private String returnval;
        private int total_project;
        private int avg_rating;

        public String getAvgPSFLocation_unit() {
            return avgPSFLocation_unit;
        }

        public void setAvgPSFLocation_unit(String avgPSFLocation_unit) {
            this.avgPSFLocation_unit = avgPSFLocation_unit;
        }

        public String getAvgPSFLocation() {
            return avgPSFLocation;
        }

        public void setAvgPSFLocation(String avgPSFLocation) {
            this.avgPSFLocation = avgPSFLocation;
        }

        public int getInfra() {
            return infra;
        }

        public void setInfra(int infra) {
            this.infra = infra;
        }

        public int getLifeStyle() {
            return lifeStyle;
        }

        public void setLifeStyle(int lifeStyle) {
            this.lifeStyle = lifeStyle;
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

        public int getNeeds() {
            return needs;
        }

        public void setNeeds(int needs) {
            this.needs = needs;
        }

        public String getReturns() {
            return returns;
        }

        public void setReturns(String returns) {
            this.returns = returns;
        }

        public String getReturnval() {
            return returnval;
        }

        public void setReturnval(String returnval) {
            this.returnval = returnval;
        }

        public int getTotal_project() {
            return total_project;
        }

        public void setTotal_project(int total_project) {
            this.total_project = total_project;
        }

        public int getAvg_rating() {
            return avg_rating;
        }

        public void setAvg_rating(int avg_rating) {
            this.avg_rating = avg_rating;
        }
    }

}
