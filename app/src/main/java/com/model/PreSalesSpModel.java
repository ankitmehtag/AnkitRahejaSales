package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PreSalesSpModel implements Parcelable {
    private String enquiry_id;
    private String customer_name;
    private String customer_mobile;
    private String customer_alt_mobile;
    private String customer_email;
    private String campaign_name;
    private String campaign_date;
    private String status;
    private String project_name;
    private String remark;
    private String budget;
    private String budgetMin;
    private String budgetMax;
    private String update_date;
    private String update_time;
    private String salesperson_name;
    private int isAssigned;
    private String scheduledatetime;
    private String lastupdatedon;

    public PreSalesSpModel(String enquiry_id, String customer_name, String customer_mobile,
                           String customer_email, String campaign_name, String campaign_date, String status,
                           String project_name, String remark,
                           String update_date, String update_time, String salesperson_name, int isAssigned,
                           String scheduledatetime, String customer_alt_mobile, String budget,
                           String budgetMin, String budgetMax,  String lastupdatedon) {
        setEnquiry_id(enquiry_id);
        setCustomer_name(customer_name);
        setCustomer_mobile(customer_mobile);
        setCustomer_email(customer_email);
        setCampaign_name(campaign_name);
        setStatus(status);
        setProject_name(project_name);
        setRemark(remark);
        setUpdate_date(update_date);
        setSalesperson_name(salesperson_name);
        setIsAssigned(isAssigned);
        setScheduledatetime(scheduledatetime);
        setCustomer_alt_mobile(customer_alt_mobile);
        setCampaign_date(campaign_date);
        setBudget(budget);
        setBudgetMin(budgetMin);
        setBudgetMax(budgetMax);
        setUpdate_time(update_time);
        setLastupdatedon(lastupdatedon);
    }

    public String getCustomer_alt_mobile() {
        return customer_alt_mobile;
    }

    public void setCustomer_alt_mobile(String customer_alt_mobile) {
        this.customer_alt_mobile = customer_alt_mobile;
    }

    public String getCampaign_date() {
        return campaign_date;
    }

    public void setCampaign_date(String campaign_date) {
        this.campaign_date = campaign_date;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBudgetMin() {
        return budgetMin;
    }

    public void setBudgetMin(String budgetMin) {
        this.budgetMin = budgetMin;
    }

    public String getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(String budgetMax) {
        this.budgetMax = budgetMax;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getEnquiry_id() {
        return enquiry_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public String getStatus() {
        return status;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public String getSalesperson_name() {
        return salesperson_name;
    }

    public int getIsAssigned() {
        return isAssigned;
    }

    public String getScheduledatetime() {
        return scheduledatetime;
    }

    public void setScheduledatetime(String scheduledatetime) {
        this.scheduledatetime = scheduledatetime;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }

    public void setEnquiry_id(String enquiry_id) {
        this.enquiry_id = enquiry_id;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public void setSalesperson_name(String salesperson_name) {
        this.salesperson_name = salesperson_name;
    }

    public void setIsAssigned(int isAssigned) {
        this.isAssigned = isAssigned;
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
        dest.writeString(this.remark);
        dest.writeString(this.update_date);
        dest.writeString(this.salesperson_name);
        dest.writeInt(this.isAssigned);
        dest.writeString(this.scheduledatetime);
        dest.writeString(this.customer_alt_mobile);
        dest.writeString(this.campaign_date);
        dest.writeString(this.budget);
        dest.writeString(this.budgetMin);
        dest.writeString(this.budgetMax);
        dest.writeString(this.update_time);
    }

    protected PreSalesSpModel(Parcel in) {
        this.enquiry_id = in.readString();
        this.customer_name = in.readString();
        this.customer_mobile = in.readString();
        this.customer_email = in.readString();
        this.campaign_name = in.readString();
        this.status = in.readString();
        this.project_name = in.readString();
        this.remark = in.readString();
        this.update_date = in.readString();
        this.salesperson_name = in.readString();
        this.isAssigned = in.readInt();
        this.scheduledatetime = in.readString();
        this.customer_alt_mobile = in.readString();
        this.campaign_date = in.readString();
        this.budget = in.readString();
        this.budgetMin = in.readString();
        this.budgetMax = in.readString();
        this.update_time = in.readString();
    }

    public static final Creator<PreSalesSpModel> CREATOR = new Creator<PreSalesSpModel>() {
        @Override
        public PreSalesSpModel createFromParcel(Parcel source) {
            return new PreSalesSpModel(source);
        }

        @Override
        public PreSalesSpModel[] newArray(int size) {
            return new PreSalesSpModel[size];
        }
    };
}
