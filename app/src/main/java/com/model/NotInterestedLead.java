package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotInterestedLead implements Parcelable {
    private String id;
    private String title;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Creator<NotInterestedLead> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.address);
    }

    public NotInterestedLead(String id, String title, String address) {
        setId(id);
        setTitle(title);
        setAddress(address);
    }

    protected NotInterestedLead(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<NotInterestedLead> CREATOR = new Parcelable.Creator<NotInterestedLead>() {
        @Override
        public NotInterestedLead createFromParcel(Parcel source) {
            return new NotInterestedLead(source);
        }

        @Override
        public NotInterestedLead[] newArray(int size) {
            return new NotInterestedLead[size];
        }
    };
}
