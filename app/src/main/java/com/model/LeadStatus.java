package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LeadStatus implements Parcelable {
    private String disposition_id;
    private String title;

    public LeadStatus(String disposition_id, String title) {
        setDisposition_id(disposition_id);
        setTitle(title);
    }

    public void setDisposition_id(String disposition_id) {
        this.disposition_id = disposition_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisposition_id() {
        return disposition_id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.disposition_id);
        dest.writeString(this.title);
    }

    protected LeadStatus(Parcel in) {
        this.disposition_id = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<LeadStatus> CREATOR = new Parcelable.Creator<LeadStatus>() {
        @Override
        public LeadStatus createFromParcel(Parcel source) {
            return new LeadStatus(source);
        }

        @Override
        public LeadStatus[] newArray(int size) {
            return new LeadStatus[size];
        }
    };
}
