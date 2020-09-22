package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CorporateModel implements Parcelable {
    private boolean success;
    private ArrayList<CorporateActivityModel> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<CorporateActivityModel> getData() {
        return data;
    }

    public void setData(ArrayList<CorporateActivityModel> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.data);
    }

    public CorporateModel() {
    }

    protected CorporateModel(Parcel in) {
        this.success = in.readByte() != 0;
        this.data = in.createTypedArrayList(CorporateActivityModel.CREATOR);
    }

    public static final Parcelable.Creator<CorporateModel> CREATOR = new Parcelable.Creator<CorporateModel>() {
        @Override
        public CorporateModel createFromParcel(Parcel source) {
            return new CorporateModel(source);
        }

        @Override
        public CorporateModel[] newArray(int size) {
            return new CorporateModel[size];
        }
    };
}
