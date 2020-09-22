package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationDataModel implements Parcelable {
    private String alert_time;
    private String alert_date;
    private String status;
    private String enquiry_id;
    private String customerName;
    private String mobileNo;
    private int isUpdated;

    public NotificationDataModel(String alert_time, String alert_date, String status, String enquiry_id, String customerName, String mobileNo, int isUpdated) {
        this.alert_time = alert_time;
        this.alert_date = alert_date;
        this.status = status;
        this.enquiry_id = enquiry_id;
        this.customerName = customerName;
        this.mobileNo = mobileNo;
        this.isUpdated = isUpdated;
    }

    public String getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(String alert_time) {
        this.alert_time = alert_time;
    }

    public String getAlert_date() {
        return alert_date;
    }

    public void setAlert_date(String alert_date) {
        this.alert_date = alert_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnquiry_id() {
        return enquiry_id;
    }

    public void setEnquiry_id(String enquiry_id) {
        this.enquiry_id = enquiry_id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(int isUpdated) {
        this.isUpdated = isUpdated;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.alert_time);
        dest.writeString(this.alert_date);
        dest.writeString(this.status);
        dest.writeString(this.enquiry_id);
        dest.writeString(this.customerName);
        dest.writeString(this.mobileNo);
        dest.writeInt(this.isUpdated);
    }

    protected NotificationDataModel(Parcel in) {
        this.alert_time = in.readString();
        this.alert_date = in.readString();
        this.status = in.readString();
        this.enquiry_id = in.readString();
        this.customerName = in.readString();
        this.mobileNo = in.readString();
        this.isUpdated = in.readInt();
    }

    public static final Creator<NotificationDataModel> CREATOR = new Creator<NotificationDataModel>() {
        @Override
        public NotificationDataModel createFromParcel(Parcel source) {
            return new NotificationDataModel(source);
        }

        @Override
        public NotificationDataModel[] newArray(int size) {
            return new NotificationDataModel[size];
        }
    };
}
