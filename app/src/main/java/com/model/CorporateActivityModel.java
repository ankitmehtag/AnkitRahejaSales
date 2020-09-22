package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CorporateActivityModel implements Parcelable {
    private String activity_id;
    private String activity_name;
    private ArrayList<Projects> project;

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

    public ArrayList<Projects> getProject() {
        return project;
    }

    public void setProject(ArrayList<Projects> project) {
        this.project = project;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activity_id);
        dest.writeString(this.activity_name);
        dest.writeTypedList(this.project);
    }

    public CorporateActivityModel(String activity_id, String activity_name, ArrayList<Projects> project) {
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.project = project;
    }

    public CorporateActivityModel(String activity_id, String activity_name) {
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.project = project;
    }

    protected CorporateActivityModel(Parcel in) {
        this.activity_id = in.readString();
        this.activity_name = in.readString();
        this.project = in.createTypedArrayList(Projects.CREATOR);
    }

    public static final Parcelable.Creator<CorporateActivityModel> CREATOR = new Parcelable.Creator<CorporateActivityModel>() {
        @Override
        public CorporateActivityModel createFromParcel(Parcel source) {
            return new CorporateActivityModel(source);
        }

        @Override
        public CorporateActivityModel[] newArray(int size) {
            return new CorporateActivityModel[size];
        }
    };
}
