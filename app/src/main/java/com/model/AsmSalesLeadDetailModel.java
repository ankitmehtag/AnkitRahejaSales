package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AsmSalesLeadDetailModel implements Parcelable {
    private String Enquiry_ID;
    private String Campaign;
    private String Campaign_Date;
    private String Name;
    private String Email_ID;
    private String Mobile_No;
    private List<Projects> selectedProjList = null;
    private ArrayList<SubStatus> subStatus = null;
    private String Alternate_Mobile_No;
    private String Budget;
    private String Current_Status;
    private String scheduledatetime;
    private String date_value;
    private String date;
    private String time;
    private String newSubStatus;
    private String newSubStatusId;
    private String closureProjectId;
    private String Address;
    private String remark;
    private String lead_type;
    private int no_of_persons;
    private String broker_name;
    private String isLeadType;
    private String closureSubStatus;
    private String unit_no;
    private String amount;
    private String tower_no;
    private String cheque_number;
    private String cheque_date;
    private String bank_name;
    private String lastUpdatedOn;

    public AsmSalesLeadDetailModel(String enquiry_ID, String campaign, String campaign_Date, String name, String email_ID,
                                   String mobile_No, String alternate_Mobile_No, String budget, String current_Status,
                                   String scheduledatetime, String date, String time, String newSubStatus, String newSubStatusId,
                                   String closureProjectId, String address, List<Projects> selectedProjList,
                                   ArrayList<SubStatus> subStatus, String remark, String lead_type, int no_of_persons,
                                   String broker_name, String isLeadType, String closureSubStatus, String unit_no, String amount,
                                   String tower_no, String cheque_number, String cheque_date, String bank_name, String lastUpdatedOn) {
        setEnquiry_ID(enquiry_ID);
        setCampaign(campaign);
        setCampaign_Date(campaign_Date);
        setName(name);
        setEmail_ID(email_ID);
        setMobile_No(mobile_No);
        setAlternate_Mobile_No(alternate_Mobile_No);
        setBudget(budget);
        setCurrent_Status(current_Status);
        setScheduledatetime(scheduledatetime);
        setDate(date);
        setTime(time);
        setNewSubStatus(newSubStatus);
        setNewSubStatusId(newSubStatusId);
        setClosureProjectId(closureProjectId);
        setAddress(address);
        setSelectedProjList(selectedProjList);
        setSubStatusList(subStatus);
        setRemark(remark);
        setLead_type(lead_type);
        setNo_of_persons(no_of_persons);
        setBroker_name(broker_name);
        setIsLeadType(isLeadType);
        setClosureSubStatus(closureSubStatus);
        setUnit_no(unit_no);
        setAmount(amount);
        setTower_no(tower_no);
        setCheque_number(cheque_number);
        setCheque_date(cheque_date);
        setBank_name(bank_name);
        setLastUpdatedOn(lastUpdatedOn);
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }

    public String getEnquiry_ID() {
        return Enquiry_ID;
    }

    public void setEnquiry_ID(String enquiry_ID) {
        Enquiry_ID = enquiry_ID;
    }

    public String getCampaign() {
        return Campaign;
    }

    public void setCampaign(String campaign) {
        Campaign = campaign;
    }

    public String getCampaign_Date() {
        return Campaign_Date;
    }

    public void setCampaign_Date(String campaign_Date) {
        Campaign_Date = campaign_Date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail_ID() {
        return Email_ID;
    }

    public void setEmail_ID(String email_ID) {
        Email_ID = email_ID;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

    public List<Projects> getSelectedProjList() {
        return selectedProjList;
    }

    public void setSelectedProjList(List<Projects> selectedProjList) {
        this.selectedProjList = selectedProjList;
    }

    public String getAlternate_Mobile_No() {
        return Alternate_Mobile_No;
    }

    public void setAlternate_Mobile_No(String alternate_Mobile_No) {
        Alternate_Mobile_No = alternate_Mobile_No;
    }

    public String getBudget() {
        return Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }

    public String getCurrent_Status() {
        return Current_Status;
    }

    public void setCurrent_Status(String current_Status) {
        Current_Status = current_Status;
    }

    public String getScheduledatetime() {
        return scheduledatetime;
    }

    public void setScheduledatetime(String scheduledatetime) {
        this.scheduledatetime = scheduledatetime;
    }

    public String getDate_value() {
        return date_value;
    }

    public void setDate_value(String date_value) {
        this.date_value = date_value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNewSubStatus() {
        return newSubStatus;
    }

    public void setNewSubStatus(String newSubStatus) {
        this.newSubStatus = newSubStatus;
    }

    public String getNewSubStatusId() {
        return newSubStatusId;
    }

    public void setNewSubStatusId(String newSubStatusId) {
        this.newSubStatusId = newSubStatusId;
    }

    public String getClosureProjectId() {
        return closureProjectId;
    }

    public void setClosureProjectId(String closureProjectId) {
        this.closureProjectId = closureProjectId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
    }

    public int getNo_of_persons() {
        return no_of_persons;
    }

    public void setNo_of_persons(int no_of_persons) {
        this.no_of_persons = no_of_persons;
    }

    public ArrayList<SubStatus> getSubStatusList() {
        return subStatus;
    }

    public void setSubStatusList(ArrayList<SubStatus> subStatusList) {
        this.subStatus = subStatusList;
    }

    public void setClosureSubStatus(String closureSubStatus) {
        this.closureSubStatus = closureSubStatus;
    }

    public String getIsLeadType() {
        return isLeadType;
    }

    public void setIsLeadType(String isLeadType) {
        this.isLeadType = isLeadType;
    }

    public String getClosureSubStatus() {
        return closureSubStatus;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(String unit_no) {
        this.unit_no = unit_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTower_no() {
        return tower_no;
    }

    public void setTower_no(String tower_no) {
        this.tower_no = tower_no;
    }

    public String getCheque_number() {
        return cheque_number;
    }

    public void setCheque_number(String cheque_number) {
        this.cheque_number = cheque_number;
    }

    public String getCheque_date() {
        return cheque_date;
    }

    public void setCheque_date(String cheque_date) {
        this.cheque_date = cheque_date;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Enquiry_ID);
        dest.writeString(this.Campaign);
        dest.writeString(this.Campaign_Date);
        dest.writeString(this.Name);
        dest.writeString(this.Email_ID);
        dest.writeString(this.Mobile_No);
        dest.writeTypedList(this.selectedProjList);
        dest.writeTypedList(this.subStatus);
        dest.writeString(this.Alternate_Mobile_No);
        dest.writeString(this.Budget);
        dest.writeString(this.Current_Status);
        dest.writeString(this.scheduledatetime);
        dest.writeString(this.date_value);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeString(this.Address);
        dest.writeString(this.remark);
        dest.writeString(this.lead_type);
        dest.writeInt(this.no_of_persons);
        dest.writeString(this.broker_name);
        dest.writeString(this.closureSubStatus);
        dest.writeString(this.unit_no);
        dest.writeString(this.amount);
        dest.writeString(this.tower_no);
        dest.writeString(this.cheque_number);
        dest.writeString(this.cheque_date);
        dest.writeString(this.bank_name);
        dest.writeString(this.lastUpdatedOn);
    }

    protected AsmSalesLeadDetailModel(Parcel in) {
        this.Enquiry_ID = in.readString();
        this.Campaign = in.readString();
        this.Campaign_Date = in.readString();
        this.Name = in.readString();
        this.Email_ID = in.readString();
        this.Mobile_No = in.readString();
        this.selectedProjList = in.createTypedArrayList(Projects.CREATOR);
        this.subStatus = in.createTypedArrayList(SubStatus.CREATOR);
        this.Alternate_Mobile_No = in.readString();
        this.Budget = in.readString();
        this.Current_Status = in.readString();
        this.scheduledatetime = in.readString();
        this.date_value = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.Address = in.readString();
        this.remark = in.readString();
        this.lead_type = in.readString();
        this.no_of_persons = in.readInt();
        this.broker_name = in.readString();
        this.closureSubStatus = in.readString();
        this.unit_no = in.readString();
        this.amount = in.readString();
        this.tower_no = in.readString();
        this.cheque_number = in.readString();
        this.cheque_date = in.readString();
        this.bank_name = in.readString();
        this.lastUpdatedOn = in.readString();
    }

    public static final Parcelable.Creator<AsmSalesLeadDetailModel> CREATOR = new Parcelable.Creator<AsmSalesLeadDetailModel>() {
        @Override
        public AsmSalesLeadDetailModel createFromParcel(Parcel source) {
            return new AsmSalesLeadDetailModel(source);
        }

        @Override
        public AsmSalesLeadDetailModel[] newArray(int size) {
            return new AsmSalesLeadDetailModel[size];
        }
    };
}
