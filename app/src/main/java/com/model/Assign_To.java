package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Assign_To implements Parcelable {
    private String salesperson_id;
    private String salesperson_name;

    public Assign_To(String salesperson_id, String salesperson_name) {
        this.salesperson_id = salesperson_id;
        this.salesperson_name = salesperson_name;
    }

    public String getSalesperson_id() {
        return salesperson_id;
    }

    public String getSalesperson_name() {
        return salesperson_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.salesperson_id);
        dest.writeString(this.salesperson_name);
    }

    protected Assign_To(Parcel in) {
        this.salesperson_id = in.readString();
        this.salesperson_name = in.readString();
    }

    public static final Parcelable.Creator<Assign_To> CREATOR = new Parcelable.Creator<Assign_To>() {
        @Override
        public Assign_To createFromParcel(Parcel source) {
            return new Assign_To(source);
        }

        @Override
        public Assign_To[] newArray(int size) {
            return new Assign_To[size];
        }
    };
}
