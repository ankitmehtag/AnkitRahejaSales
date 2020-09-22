package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubStatus implements Parcelable {
    private String id;
    private String title;

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

    public static Creator<SubStatus> getCREATOR() {
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
    }

    public SubStatus(String id, String title) {
        this.id = id;
        this.title = title;
    }

    protected SubStatus(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<SubStatus> CREATOR = new Parcelable.Creator<SubStatus>() {
        @Override
        public SubStatus createFromParcel(Parcel source) {
            return new SubStatus(source);
        }

        @Override
        public SubStatus[] newArray(int size) {
            return new SubStatus[size];
        }
    };
}
