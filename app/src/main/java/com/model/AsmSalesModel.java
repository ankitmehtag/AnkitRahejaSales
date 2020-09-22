package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AsmSalesModel implements Parcelable {
    private String enquiry_id;
    private String customer_name;
    private String customer_mobile;
    private String customer_email;
    private String campaign_name;

    private String status;
    private String project_name;
    private String lastupdatedon;
    private String is_lead_type;
    private int isAssigned;
    private int isLeadAccepted;
    private String scheduledatetime;
    private AsmSalesLeadDetailModel details;

    public AsmSalesModel(String enquiry_id, String customer_name, String customer_mobile, String customer_email,
                         String campaign_name, String status, String project_name, String scheduledatetime,
                         String is_lead_type, int isAssigned, int isLeadAccepted, String lastupdatedon, AsmSalesLeadDetailModel details) {

        setEnquiry_id(enquiry_id);
        setCustomer_mobile(customer_mobile);
        setCustomer_name(customer_name);
        setCustomer_email(customer_email);
        setCampaign_name(campaign_name);
        setStatus(status);
        setProject_name(project_name);
        setScheduledatetime(scheduledatetime);
        setIs_lead_type(is_lead_type);
        setIsAssigned(isAssigned);
        setIsLeadAccepted(isLeadAccepted);
        setLastupdatedon(lastupdatedon);
        setDetails(details);
    }

    public int getIsLeadAccepted() {
        return isLeadAccepted;
    }

    public void setIsLeadAccepted(int isLeadAccepted) {
        this.isLeadAccepted = isLeadAccepted;
    }

    public String getEnquiry_id() {
        return enquiry_id;
    }

    public void setEnquiry_id(String enquiry_id) {
        this.enquiry_id = enquiry_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status)

          {
        this.status = status;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }

    public String getIs_lead_type() {
        return is_lead_type;
    }

    public void setIs_lead_type(String is_lead_type) {
        this.is_lead_type = is_lead_type;
    }

    public int getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(int isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getScheduledatetime() {
        return scheduledatetime;
    }

    public void setScheduledatetime(String scheduledatetime) {
        this.scheduledatetime = scheduledatetime;
    }

    public AsmSalesLeadDetailModel getDetails() {
        return details;
    }

    public void setDetails(AsmSalesLeadDetailModel details) {
        this.details = details;
    }

    public static Creator<AsmSalesModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.enquiry_id);
        dest.writeString(this.customer_name);
        dest.writeString(this.customer_mobile);
        dest.writeString(this.customer_email);
        dest.writeString(this.campaign_name);
        dest.writeString(this.status);
        dest.writeString(this.project_name);
        dest.writeString(this.lastupdatedon);
        dest.writeString(this.is_lead_type);
        dest.writeInt(this.isAssigned);
        dest.writeInt(this.isLeadAccepted);
        dest.writeString(this.scheduledatetime);
        dest.writeParcelable(this.details, flags);
    }

    protected AsmSalesModel(Parcel in) {
        this.enquiry_id = in.readString();
        this.customer_name = in.readString();
        this.customer_mobile = in.readString();
        this.customer_email = in.readString();
        this.campaign_name = in.readString();
        this.status = in.readString();
        this.project_name = in.readString();
        this.lastupdatedon = in.readString();
        this.is_lead_type = in.readString();
        this.isAssigned = in.readInt();
        this.isLeadAccepted = in.readInt();
        this.scheduledatetime = in.readString();
        this.details = in.readParcelable(AsmSalesLeadDetailModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<AsmSalesModel> CREATOR = new Parcelable.Creator<AsmSalesModel>() {
        @Override
        public AsmSalesModel createFromParcel(Parcel source) {
            return new AsmSalesModel(source);
        }

        @Override
        public AsmSalesModel[] newArray(int size) {
            return new AsmSalesModel[size];
        }
    };
}
