package com.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Naresh on 10-Mar-17.
 */

public class BhkUnitSpecification implements Parcelable {

    private String wpcf_flat_typology;
    private String area_range;
    private String price_range;
    private String area_range_unit;

    public String getArea_range_unit() {
        return area_range_unit;
    }

    public void setArea_range_unit(String area_range_unit) {
        this.area_range_unit = area_range_unit;
    }

    public String getWpcf_flat_typology() {
        return wpcf_flat_typology;
    }

    public void setWpcf_flat_typology(String wpcf_flat_typology) {
        this.wpcf_flat_typology = wpcf_flat_typology;
    }

    public String getArea_range() {
        return area_range;
    }

    public void setArea_range(String area_range) {
        this.area_range = area_range;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }


    protected BhkUnitSpecification(Parcel in) {
        wpcf_flat_typology = in.readString();
        area_range = in.readString();
        price_range = in.readString();
    }

    public static final Creator<BhkUnitSpecification> CREATOR = new Creator<BhkUnitSpecification>() {
        @Override
        public BhkUnitSpecification createFromParcel(Parcel in) {
            return new BhkUnitSpecification(in);
        }

        @Override
        public BhkUnitSpecification[] newArray(int size) {
            return new BhkUnitSpecification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wpcf_flat_typology);
        dest.writeString(area_range);
        dest.writeString(price_range);
        dest.writeString(area_range_unit);
    }
}
