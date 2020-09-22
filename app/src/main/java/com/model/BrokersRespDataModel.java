package com.model;

import java.util.ArrayList;

/**
 * Created by Naresh on 18-Jan-18.
 */

public class BrokersRespDataModel {
    private boolean success;
    private String message;
    private ArrayList<Data> data;

    public static final int ACTIVE = 1, INACTIVE = 0,SUSPENDED = 3;
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

    public class Data{
        private String first_name;
        private String last_name;
        private String broker_code;
        private String broker_id;
        private int status; // 0 inactive, 1 active, 3 suspended
        private String broker_type;
        private String email;
        private String mobile_no;
        private String user_image;

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getBroker_code() {
            return broker_code;
        }

        public void setBroker_code(String broker_code) {
            this.broker_code = broker_code;
        }

        public String getBroker_id() {
            return broker_id;
        }

        public void setBroker_id(String broker_id) {
            this.broker_id = broker_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getBroker_type() {
            return broker_type;
        }

        public void setBroker_type(String broker_type) {
            this.broker_type = broker_type;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }
    }


}
