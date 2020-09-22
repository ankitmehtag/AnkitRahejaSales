package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_pre_sales_lead_details")
public class PreSalesLeadDetailsEntity {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "customer_name")
    private String customerName;
    @ColumnInfo(name = "customer_mobile")
    private String customerMobile;
    @ColumnInfo(name = "customer_email")
    private String customerEmail;
    @ColumnInfo(name = "customer_Alternate_mobile")
    private String customerAlternateMobile;
    @ColumnInfo(name = "status_id")
    private int statusId;
    @ColumnInfo(name = "current_status")
    private String currentStatus;
    @ColumnInfo(name = "scheduledatetime")
    private String scheduledatetime;
    @ColumnInfo(name = "project")
    private String project;

    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "budget")
    private String budget;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "budget_min")
    private String budgetMin;
    @ColumnInfo(name = "budget_max")
    private String budgetMax;
    @ColumnInfo(name = "lead_owner")
    private String leadOwner;
    @ColumnInfo(name = "assigned_to")
    private String assignedTo;
    @ColumnInfo(name = "assigned_to_id")
    private String assignedToId;
    @ColumnInfo(name = "is_assigned")
    private int isAssigned;
    @ColumnInfo(name = "campaign_name")
    private String campaignName;
    @ColumnInfo(name = "campaign_date")
    private String campaignDate;
    @ColumnInfo(name = "campaign_id")
    private int campaignId;
    @ColumnInfo(name = "is_updated")
    private int isUpdated;
    @ColumnInfo(name = "is_synced")
    private int isSynced;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;
    @ColumnInfo(name = "designation")
    private String designationType;
    @ColumnInfo(name = "recording_file_path")
    private String recordingFilePath;

    public PreSalesLeadDetailsEntity(String enquiryId, String customerName, String customerMobile, String customerEmail,
                                     String customerAlternateMobile, int statusId, String currentStatus, String scheduledatetime,
                                     String project, String remark, String budget, String date, String time, String budgetMin, String budgetMax,
                                     String leadOwner, String assignedTo, String assignedToId, int isAssigned, String campaignName, String campaignDate,
                                     int campaignId, int isUpdated, int isSynced, String lastupdatedon, String designationType, String recordingFilePath) {
        setEnquiryId(enquiryId);
        setCustomerName(customerName);
        setCustomerMobile(customerMobile);
        setCustomerEmail(customerEmail);
        setCustomerAlternateMobile(customerAlternateMobile);
        setStatusId(statusId);
        setCurrentStatus(currentStatus);
        setProject(project);
        setRemark(remark);
        setBudget(budget);
        setDate(date);
        setTime(time);
        setScheduledatetime(scheduledatetime);
        setBudgetMin(budgetMin);
        setBudgetMax(budgetMax);
        setLeadOwner(leadOwner);
        setAssignedTo(assignedTo);
        setAssignedToId(assignedToId);
        setIsAssigned(isAssigned);
        setCampaignName(campaignName);
        setCampaignDate(campaignDate);
        setCampaignId(campaignId);
        setIsUpdated(isUpdated);
        setIsSynced(isSynced);
        setLastupdatedon(lastupdatedon);
        setDesignationType(designationType);
        setRecordingFilePath(recordingFilePath);
    }



    public String getScheduledatetime() {
        return scheduledatetime;
    }

    public void setScheduledatetime(String scheduledatetime) {
        this.scheduledatetime = scheduledatetime;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAlternateMobile() {
        return customerAlternateMobile;
    }

    public void setCustomerAlternateMobile(String customerAlternateMobile) {
        this.customerAlternateMobile = customerAlternateMobile;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId)

    {

         this.statusId = statusId;
    }

    public String getCurrentStatus()
    {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus)
              {

                  this.currentStatus = currentStatus;
         }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
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

    public String getLeadOwner() {
        return leadOwner;
    }

    public void setLeadOwner(String leadOwner) {
        this.leadOwner = leadOwner;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(String assignedToId) {
        this.assignedToId = assignedToId;
    }

    public int getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(int isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDate() {
        return campaignDate;
    }

    public void setCampaignDate(String campaignDate) {
        this.campaignDate = campaignDate;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(int isUpdated) {
        this.isUpdated = isUpdated;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getDesignationType() {
        return designationType;
    }

    public void setDesignationType(String designationType) {
        this.designationType = designationType;
    }

    public String getRecordingFilePath() {
        return recordingFilePath;
    }

    public void setRecordingFilePath(String recordingFilePath) {
        this.recordingFilePath = recordingFilePath;
    }
}
