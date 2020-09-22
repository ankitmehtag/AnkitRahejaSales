package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    private String office;
    private String client_address;
    private String other;

    public Location(String office, String client_address, String other) {
        this.office = office;
        this.client_address = client_address;
        this.other = other;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.office);
        dest.writeString(this.client_address);
        dest.writeString(this.other);
    }

    protected Location(Parcel in) {
        this.office = in.readString();
        this.client_address = in.readString();
        this.other = in.readString();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
