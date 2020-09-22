package com.model;
import android.os.Parcel;
import android.os.Parcelable;

public class BrokersListModel implements Parcelable {

    private String broker_id;
    private String broker_name;
    private String broker_code;
    private String status;
    private String broker_type;
    private String mobile_no;
    private String email_id;
    private String image;

    public String getBroker_id() {
        return broker_id;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public String getBroker_code() {
        return broker_code;
    }

    public String getStatus() {
        return status;
    }

    public String getBroker_type() {
        return broker_type;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.broker_id);
        dest.writeString(this.broker_name);
        dest.writeString(this.broker_code);
        dest.writeString(this.status);
        dest.writeString(this.broker_type);
        dest.writeString(this.mobile_no);
        dest.writeString(this.email_id);
        dest.writeString(this.image);
    }

    public BrokersListModel() {
    }

    protected BrokersListModel(Parcel in) {
        this.broker_id = in.readString();
        this.broker_name = in.readString();
        this.broker_code = in.readString();
        this.status = in.readString();
        this.broker_type = in.readString();
        this.mobile_no = in.readString();
        this.email_id = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<BrokersListModel> CREATOR = new Parcelable.Creator<BrokersListModel>() {
        @Override
        public BrokersListModel createFromParcel(Parcel source) {
            return new BrokersListModel(source);
        }

        @Override
        public BrokersListModel[] newArray(int size) {
            return new BrokersListModel[size];
        }
    };
}
