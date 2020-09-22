package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentModel implements Parcelable {
    private String type;
    private String customer_name;
    private String campaign;
    private String email_id;

    private String mobile_no;
    private String project;
    private String alternate_no;
    private String budget;
    private String status_id;
    private String status_title;
    private String date_time;
    private String remark;
    private String no_of_persons;
    private String address;
    private String lead_type;
    private String activity_id;
    private String activity_name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomer_name() {
        return customer_name;
    }
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
    public String getCampaign() {
        return campaign;
    }
    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getAlternate_no() {
        return alternate_no;
    }

    public void setAlternate_no(String alternate_no) {
        this.alternate_no = alternate_no;
    }

    public String getBudget() {
        return budget;
    }


    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus_title() {
        return status_title;

    }

    public void setStatus_title(String status_title) {
        this.status_title = status_title;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNo_of_persons() {
        return no_of_persons;
    }

    public void setNo_of_persons(String no_of_persons) {
        this.no_of_persons = no_of_persons;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public static Creator<AppointmentModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.customer_name);
        dest.writeString(this.email_id);
        dest.writeString(this.mobile_no);
        dest.writeString(this.project);
        dest.writeString(this.alternate_no);
        dest.writeString(this.budget);
        dest.writeString(this.status_id);
        dest.writeString(this.status_title);
        dest.writeString(this.date_time);
        dest.writeString(this.remark);
        dest.writeString(this.no_of_persons);
        dest.writeString(this.address);
        dest.writeString(this.lead_type);
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
    }

    public AppointmentModel() {
    }

    protected AppointmentModel(Parcel in) {
        this.type = in.readString();
        this.customer_name = in.readString();
        this.email_id = in.readString();
        this.mobile_no = in.readString();
        this.project = in.readString();
        this.alternate_no = in.readString();
        this.budget = in.readString();
        this.status_id = in.readString();
        this.status_title = in.readString();
        this.date_time = in.readString();
        this.remark = in.readString();
        this.no_of_persons = in.readString();
        this.address = in.readString();
        this.lead_type = in.readString();
        this.activity_id = in.readString();
        this.activity_name = in.readString();
    }

    public static final Parcelable.Creator<AppointmentModel> CREATOR = new Parcelable.Creator<AppointmentModel>() {
        @Override
        public AppointmentModel createFromParcel(Parcel source) {
            return new AppointmentModel(source);
        }

        @Override
        public AppointmentModel[] newArray(int size) {
            return new AppointmentModel[size];
        }
    };
}
