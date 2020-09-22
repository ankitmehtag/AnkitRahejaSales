package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_callback")
public class CallbackEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "scheduledatetime")
    private String scheduledatetime;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "creation_date")
    private String creationDate;
    @ColumnInfo(name = "lead_type")
    private String leadType;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "isSynced")
    private int isSynced;
    @ColumnInfo(name = "recording_file_path")
    private String recordingFilePath;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;


    public CallbackEntity(String enquiryId, String userId, String scheduledatetime,
                          String date, String time, String creationDate, String remark,
                          String leadType, int isSynced, String recordingFilePath, String lastupdatedon) {
        setEnquiryId(enquiryId);
        setUserId(userId);
        setScheduledatetime(scheduledatetime);
        setDate(date);
        setTime(time);
        setCreationDate(creationDate);
        setRemark(remark);
        setLeadType(leadType);
        setIsSynced(isSynced);
        setRecordingFilePath(recordingFilePath);
        setLastupdatedon(lastupdatedon);
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getRowId() {
        return rowId;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getRecordingFilePath() {
        return recordingFilePath;
    }

    public void setRecordingFilePath(String recordingFilePath) {
        this.recordingFilePath = recordingFilePath;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }
}
