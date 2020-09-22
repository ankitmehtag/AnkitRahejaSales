package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotInterestedModel implements Parcelable {
    private String id;
    private String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
    }

    public NotInterestedModel(String id, String title) {
        setId(id);
        setTitle(title);
    }

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

    protected NotInterestedModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<NotInterestedModel> CREATOR = new Parcelable.Creator<NotInterestedModel>() {
        @Override
        public NotInterestedModel createFromParcel(Parcel source) {
            return new NotInterestedModel(source);
        }

        @Override
        public NotInterestedModel[] newArray(int size) {
            return new NotInterestedModel[size];
        }
    };
}
