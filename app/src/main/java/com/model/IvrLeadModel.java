package com.model;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class IvrLeadModel implements Parcelable {


    private String mobile_no;
    private String campaign_title;
    private String datetime;
    private String dialstatus;
    private String isLead;
    private String history_url;
    private String project;


    public String getIsLead() {
        return isLead;
    }


    public String gethistory_url() {
        return history_url;
    }
    public String getmobile_no() {
        return mobile_no;
    }

    public String getcampaign_title() {


        return campaign_title;
    }

    public String getproject() {

        return project;
    }

    public String getdatetime() {

        return datetime;
    }

    public String getdialstatus() {

        return dialstatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isLead);
        dest.writeString(this.mobile_no);
        dest.writeString(this.campaign_title);
        dest.writeString(this.project);
        dest.writeString(this.datetime);
        dest.writeString(this.dialstatus);
        dest.writeString(this.history_url);
        dest.writeString(this.project);
    }

    public IvrLeadModel() {
    }

    protected IvrLeadModel(Parcel in) {


        this.isLead= in.readString();
        this.mobile_no = in.readString();
        this.campaign_title = in.readString();
        this.project = in.readString();
        this.datetime = in.readString();
        this.dialstatus = in.readString();
        this.dialstatus = in.readString();
        this.dialstatus = in.readString();
        this.history_url = in.readString();

    }


    public static final Parcelable.Creator<IvrLeadModel> CREATOR = new Parcelable.Creator<IvrLeadModel>() {
        @Override
        public IvrLeadModel createFromParcel(Parcel source) {
            return new IvrLeadModel(source);
        }

        @Override
        public IvrLeadModel[] newArray(int size) {
            return new IvrLeadModel[size];
        }
    };
}
