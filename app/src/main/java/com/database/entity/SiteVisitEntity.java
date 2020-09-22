package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_site_visit")
public class SiteVisitEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "site_location")
    private String siteLocation;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "lead_type")
    private String leadType;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "no_of_person_visited")
    private String noOfPersonVisited;
    @ColumnInfo(name = "site_visit_number")
    private String siteVisitNumber;
    @ColumnInfo(name = "vehicle_accommodated")
    private String vehicleAccommodated;
    @ColumnInfo(name = "scheduledatetime")
    private String scheduledatetime;
    @ColumnInfo(name = "site_visit_done_on")
    private String siteVisitDoneOn;
    @ColumnInfo(name = "creation_date")
    private String creationDate;
    @ColumnInfo(name = "isSynced")
    private int isSynced;
    @ColumnInfo(name = "recording_file_path")
    private String recordingFilePath;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;


    public SiteVisitEntity(String enquiryId, String status, String siteLocation, String date, String time, String address,
                           String leadType, String noOfPersonVisited, String remark, String siteVisitNumber,
                           String vehicleAccommodated, String scheduledatetime, String siteVisitDoneOn,
                           String creationDate, int isSynced, String recordingFilePath, String lastupdatedon) {
        setEnquiryId(enquiryId);
        setStatus(status);
        setSiteLocation(siteLocation);
        setNoOfPersonVisited(noOfPersonVisited);
        setDate(date);
        setTime(time);
        setAddress(address);
        setLeadType(leadType);
        setRemark(remark);
        setSiteVisitNumber(siteVisitNumber);
        setVehicleAccommodated(vehicleAccommodated);
        setScheduledatetime(scheduledatetime);
        setSiteVisitDoneOn(siteVisitDoneOn);
        setCreationDate(creationDate);
        setIsSynced(isSynced);
        setRecordingFilePath(recordingFilePath);
        setLastupdatedon(lastupdatedon);
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    private void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getNoOfPersonVisited() {
        return noOfPersonVisited;
    }

    private void setNoOfPersonVisited(String noOfPersonVisited) {
        this.noOfPersonVisited = noOfPersonVisited;
    }

    public String getScheduledatetime() {
        return scheduledatetime;
    }

    public void setScheduledatetime(String scheduledatetime) {
        this.scheduledatetime = scheduledatetime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSiteVisitNumber() {
        return siteVisitNumber;
    }

    private void setSiteVisitNumber(String siteVisitNumber) {
        this.siteVisitNumber = siteVisitNumber;
    }

    public String getVehicleAccommodated() {
        return vehicleAccommodated;
    }

    private void setVehicleAccommodated(String vehicleAccommodated) {
        this.vehicleAccommodated = vehicleAccommodated;
    }

    public String getSiteVisitDoneOn() {
        return siteVisitDoneOn;
    }

    private void setSiteVisitDoneOn(String siteVisitDoneOn) {
        this.siteVisitDoneOn = siteVisitDoneOn;
    }

    public String getCreationDate() {
        return creationDate;
    }

    private void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getIsSynced() {
        return isSynced;
    }

    private void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getRecordingFilePath() {
        return recordingFilePath;
    }

    private void setRecordingFilePath(String recordingFilePath) {
        this.recordingFilePath = recordingFilePath;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }
}
