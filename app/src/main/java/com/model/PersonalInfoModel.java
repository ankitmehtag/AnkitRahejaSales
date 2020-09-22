package com.model;

import org.json.JSONObject;

public class PersonalInfoModel {

    private String broker_code;
    private String user_image;
    private String first_name;
    private String last_name;
    private String address;
    private String mobile_no;
    private String email_id;
    private String business_type;
    private String rera_no;
    private String pan_no;
    private String gst_no;
    private String document_status;

    public PersonalInfoModel(JSONObject object){
      setBroker_code(object.optString("broker_code"));
      setUser_image(object.optString("user_image"));
      setFirst_name(object.optString("first_name"));
      setLast_name(object.optString("last_name"));
      setAddress(object.optString("address"));
      setMobile_no(object.optString("mobile_no"));
      setEmail_id(object.optString("email_id"));
      setBusiness_type(object.optString("business_type"));
      setRera_no(object.optString("rera_no"));
      setPan_no(object.optString("pan_no"));
      setGst_no(object.optString("gst_no"));
      setDocument_status(object.optString("document_status"));
    }

    public void setBroker_code(String broker_code) {
        this.broker_code = broker_code;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public void setRera_no(String rera_no) {
        this.rera_no = rera_no;
    }

    public void setPan_no(String pan_no) {
        this.pan_no = pan_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }

    public String getBroker_code() {
        return broker_code;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public String getRera_no() {
        return rera_no;
    }

    public String getPan_no() {
        return pan_no;
    }

    public String getGst_no() {
        return gst_no;
    }

    public String getDocument_status() {
        return document_status;
    }
}
