package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_meeting")
public class MeetingEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "meeting_number")
    private Long meetingId;
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "meeting_time")
    private String meetingTime;
    @ColumnInfo(name = "meeting_date")
    private String meetingDate;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "lead_type")
    private String leadType;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "scheduleDateTime")
    private String scheduleDateTime;
    @ColumnInfo(name = "meeting_done_on")
    private String meetingDoneOn;
    @ColumnInfo(name = "isSynced")
    private int isSynced;
    @ColumnInfo(name = "recording_file_path")
    private String recordingFilePath;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;

    public MeetingEntity(Long meetingId, String enquiryId, String status, String meetingTime, String meetingDate,
                         String address, String leadType, String remark, String scheduleDateTime, String meetingDoneOn,
                         int isSynced, String recordingFilePath, String lastupdatedon) {
        setMeetingId(meetingId);
        setEnquiryId(enquiryId);
        setStatus(status);
        setMeetingTime(meetingTime);
        setMeetingDate(meetingDate);
        setAddress(address);
        setLeadType(leadType);
        setRemark(remark);
        setScheduleDateTime(scheduleDateTime);
        setMeetingDoneOn(meetingDoneOn);
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

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
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

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getRemark() {
        return remark;
    }

    public String getScheduleDateTime() {
        return scheduleDateTime;
    }

    public void setScheduleDateTime(String scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getMeetingDoneOn() {
        return meetingDoneOn;
    }

    public void setMeetingDoneOn(String meetingDoneOn) {
        this.meetingDoneOn = meetingDoneOn;
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
