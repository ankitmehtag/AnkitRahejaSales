package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BrokerLeadsAssignedModel implements Parcelable {
    private String customer_name;
    private String enquiry_code;
    private String unit_type;
    private String price_range;
    private String project_name;
    private String mobile_number;
    private String email_id;
    private String enquiry_status;
    private String lead_added_on;
    private String remark;
    private String follow_up_date_time;
    private String client_name;
    private String address;

    public String getCustomer_name() {
        return customer_name;
    }

    public String getEnquiry_code() {
        return enquiry_code;
    }

    public String getUnit_type() {
        return unit_type;
    }

    public String getPrice_range() {
        return price_range;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getEnquiry_status() {
        return enquiry_status;
    }

    public String getLead_added_on() {
        return lead_added_on;
    }

    public String getRemark() {
        return remark;
    }

    public String getFollow_up_date_time() {
        return follow_up_date_time;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customer_name);
        dest.writeString(this.enquiry_code);
        dest.writeString(this.unit_type);
        dest.writeString(this.price_range);
        dest.writeString(this.project_name);
        dest.writeString(this.mobile_number);
        dest.writeString(this.email_id);
        dest.writeString(this.enquiry_status);
        dest.writeString(this.lead_added_on);
        dest.writeString(this.remark);
        dest.writeString(this.follow_up_date_time);
        dest.writeString(this.client_name);
        dest.writeString(this.address);
    }

    public BrokerLeadsAssignedModel() {
    }

    protected BrokerLeadsAssignedModel(Parcel in) {
        this.customer_name = in.readString();
        this.enquiry_code = in.readString();
        this.unit_type = in.readString();
        this.price_range = in.readString();
        this.project_name = in.readString();
        this.mobile_number = in.readString();
        this.email_id = in.readString();
        this.enquiry_status = in.readString();
        this.lead_added_on = in.readString();
        this.remark = in.readString();
        this.follow_up_date_time = in.readString();
        this.client_name = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<BrokerLeadsAssignedModel> CREATOR = new Parcelable.Creator<BrokerLeadsAssignedModel>() {
        @Override
        public BrokerLeadsAssignedModel createFromParcel(Parcel source) {
            return new BrokerLeadsAssignedModel(source);
        }

        @Override
        public BrokerLeadsAssignedModel[] newArray(int size) {
            return new BrokerLeadsAssignedModel[size];
        }
    };
}
