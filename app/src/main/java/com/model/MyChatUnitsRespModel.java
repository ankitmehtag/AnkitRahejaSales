package com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 23-Jan-18.
 */

public class MyChatUnitsRespModel implements Serializable {

    private boolean success;
    private String message;
    private ArrayList<Data> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private String unit_id;
        private String unit_name;
        private String project_name;
        private String unit_image;
        private String badge;
        private String last_updated;
        private String plan_name;
        private String user_chat_id;
        private String builder_chat_id;
        private String customer_name;
        private String unit_number;
        private String isUnitReserved;
        private String unread_count;

        public String getUnread_count() {
            return unread_count;
        }

        public void setUnread_count(String unread_count) {
            this.unread_count = unread_count;
        }

        public String getPlan_name() {
            return plan_name;
        }

        public void setPlan_name(String plan_name) {
            this.plan_name = plan_name;
        }

        public String getIsUnitReserved() {
            return isUnitReserved;
        }

        public void setIsUnitReserved(String isUnitReserved) {
            this.isUnitReserved = isUnitReserved;
        }

        public String getUnit_id() {
            return unit_id;
        }

        public void setUnit_id(String unit_id) {
            this.unit_id = unit_id;
        }

        public String getUnit_name() {
            return unit_name;
        }

        public void setUnit_name(String unit_name) {
            this.unit_name = unit_name;
        }

        public String getProject_name() {
            return project_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public String getUnit_image() {
            return unit_image;
        }

        public void setUnit_image(String unit_image) {
            this.unit_image = unit_image;
        }

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        public String getLast_updated() {
            return last_updated;
        }

        public void setLast_updated(String last_updated) {
            this.last_updated = last_updated;
        }

        public String getUser_chat_id() {
            return user_chat_id;
        }

        public void setUser_chat_id(String user_chat_id) {
            this.user_chat_id = user_chat_id;
        }

        public String getBuilder_chat_id() {
            return builder_chat_id;
        }

        public void setBuilder_chat_id(String builder_chat_id) {
            this.builder_chat_id = builder_chat_id;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getUnit_number() {
            return unit_number;
        }

        public void setUnit_number(String unit_number) {
            this.unit_number = unit_number;
        }
    }
}
