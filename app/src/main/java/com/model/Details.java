package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Details implements Parcelable {
    private String Enquiry_ID;
    private String Campaign;
    private String Campaign_Date;
    private String Name;
    private String Email_ID;
    private String Mobile_No;
    private String Alternate_Mobile_No;
    private String Budget;
    private String Current_Status;
    private String scheduledatetime;
    private String date;
    private String time;
    private String Address;
    private String Project_Name;
    //    private String last_updated_on;
    private ArrayList<Assign_To> assignList = null;
    private ArrayList<LeadStatus> statusList = null;
    private List<Projects> projects = null;
    private List<Projects> selectedProjList = null;
    private String remark;
    private String lead_type;
    private String budget_min;
    private String budget_max;
    private int no_of_persons;
    private String lastupdatedon;

    public Details(String enquiry_ID, String campaign, String campaign_Date, String name, String email_ID,
                   String mobile_No, String alternate_Mobile_No, String budget, String current_Status,
                   String scheduledatetime, String date, String time, String address, String Project_Name,
                   List<Projects> projectList, List<Projects> selectedProjList, ArrayList<Assign_To> assignList,
                   ArrayList<LeadStatus> leadStatusList, String remark, String lead_type,
                   String budget_min, String budget_max, int no_of_persons, String lastupdatedon) {
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
        setAddress(address);
        setProjectName(Project_Name);
        setProjectList(projectList);
        setSelectedProjList(selectedProjList);
        setAssignList(assignList);
        setLeadStatusList(leadStatusList);
        setRemark(remark);
        setLead_type(lead_type);
        setBudget_min(budget_min);
        setBudget_max(budget_max);
        setNoOfPersons(no_of_persons);
        setLastupdatedon(lastupdatedon);
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
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

    public String getBudget_min() {
        return budget_min;
    }

    public void setBudget_min(String budget_min) {
        this.budget_min = budget_min;
    }

    public String getBudget_max() {
        return budget_max;
    }

    public void setBudget_max(String budget_max) {
        this.budget_max = budget_max;
    }

    public int getNoOfPersons() {
        return no_of_persons;
    }

    public void setNoOfPersons(int no_of_persons) {
        this.no_of_persons = no_of_persons;
    }

    public List<Projects> getProjectList() {
        return projects;
    }

    public void setProjectList(List<Projects> projects) {
        this.projects = projects;
    }

    public List<Projects> getSelectedProjList() {
        return selectedProjList;
    }

    public void setSelectedProjList(List<Projects> selectedProjList) {
        this.selectedProjList = selectedProjList;
    }

    public ArrayList<LeadStatus> getLeadStatusList() {
        return statusList;
    }

    private void setLeadStatusList(ArrayList<LeadStatus> statusList) {
        this.statusList = statusList;
    }

    public String getEnquiry_ID() {
        return Enquiry_ID;
    }

    public String getCampaign() {
        return Campaign;
    }

    public String getCampaign_Date() {
        return Campaign_Date;
    }

    public String getName() {
        return Name;
    }

    public String getEmail_ID() {
        return Email_ID;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public String getAlternate_Mobile_No() {
        return Alternate_Mobile_No;
    }

    public String getBudget() {
        return Budget;
    }

    public String getCurrent_Status() {
        return Current_Status;
    }

    public String getScheduledatetime() {
        return scheduledatetime;
    }

    public void setScheduledatetime(String scheduledatetime) {
        this.scheduledatetime = scheduledatetime;
    }

    public String getAddress() {
        return Address;
    }

    public String getProjectName() {
        return Project_Name;
    }

    public void setProjectName(String projectName) {
        this.Project_Name = projectName;
    }

    public ArrayList<Assign_To> getAssignToList() {
        return assignList;
    }

    private void setEnquiry_ID(String enquiry_ID) {
        Enquiry_ID = enquiry_ID;
    }

    private void setCampaign(String campaign) {
        Campaign = campaign;
    }

    private void setCampaign_Date(String campaign_Date) {
        Campaign_Date = campaign_Date;
    }

    private void setName(String name) {
        Name = name;
    }

    private void setEmail_ID(String email_ID) {
        Email_ID = email_ID;
    }

    private void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

    private void setAlternate_Mobile_No(String alternate_Mobile_No) {
        Alternate_Mobile_No = alternate_Mobile_No;
    }

    private void setBudget(String budget) {
        Budget = budget;
    }

    private void setCurrent_Status(String current_Status) {
        Current_Status = current_Status;
    }


    private void setAddress(String address) {
        Address = address;
    }

    private void setAssignList(ArrayList<Assign_To> assignList) {
        this.assignList = assignList;
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
        dest.writeString(this.Alternate_Mobile_No);
        dest.writeString(this.Budget);
        dest.writeString(this.Current_Status);
        dest.writeString(this.scheduledatetime);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeString(this.Address);
        dest.writeString(this.Project_Name);
        dest.writeTypedList(this.assignList);
        dest.writeTypedList(this.statusList);
        dest.writeTypedList(this.projects);
        dest.writeTypedList(this.selectedProjList);
        dest.writeString(this.remark);
        dest.writeString(this.lead_type);
        dest.writeString(this.budget_min);
        dest.writeString(this.budget_max);
        dest.writeInt(this.no_of_persons);
    }

    protected Details(Parcel in) {
        this.Enquiry_ID = in.readString();
        this.Campaign = in.readString();
        this.Campaign_Date = in.readString();
        this.Name = in.readString();
        this.Email_ID = in.readString();
        this.Mobile_No = in.readString();
        this.Alternate_Mobile_No = in.readString();
        this.Budget = in.readString();
        this.Current_Status = in.readString();
        this.scheduledatetime = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.Address = in.readString();
        this.Project_Name = in.readString();
        this.assignList = in.createTypedArrayList(Assign_To.CREATOR);
        this.statusList = in.createTypedArrayList(LeadStatus.CREATOR);
        this.projects = in.createTypedArrayList(Projects.CREATOR);
        this.selectedProjList = in.createTypedArrayList(Projects.CREATOR);
        this.remark = in.readString();
        this.lead_type = in.readString();
        this.budget_min = in.readString();
        this.budget_max = in.readString();
        this.no_of_persons = in.readInt();
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel source) {
            return new Details(source);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };
}