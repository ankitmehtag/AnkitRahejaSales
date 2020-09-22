package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Projects implements Parcelable {
    private String project_id;
    private String project_name;

    public Projects(String project_id, String project_name) {
        this.project_id = project_id;
        this.project_name = project_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.project_id);
        dest.writeString(this.project_name);
    }

    protected Projects(Parcel in) {
        this.project_id = in.readString();
        this.project_name = in.readString();
    }

    public static final Parcelable.Creator<Projects> CREATOR = new Parcelable.Creator<Projects>() {
        @Override
        public Projects createFromParcel(Parcel source) {
            return new Projects(source);
        }

        @Override
        public Projects[] newArray(int size) {
            return new Projects[size];
        }
    };
}
