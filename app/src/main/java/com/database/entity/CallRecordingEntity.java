package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_call_recording")
public class CallRecordingEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "rec_file_name")
    private String recFileName;
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "mobile_no")
    private String mobileNo;
    @ColumnInfo(name = "is_synced")
    private int isSynced;
    @ColumnInfo(name = "lead_update_status")
    private int leadUpdateStatus;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "time_stamp")
    private String timeStamp;

    public CallRecordingEntity(@NonNull String recFileName, String enquiryId, String mobileNo, int isSynced, int leadUpdateStatus,
                               String remark,String timeStamp) {
        setRecFileName(recFileName);
        setEnquiryId(enquiryId);
        setMobileNo(mobileNo);
        setIsSynced(isSynced);
        setLeadUpdateStatus(leadUpdateStatus);
        setRemark(remark);
        setTimeStamp(timeStamp);
    }

    @NonNull
    public String getRecFileName() {
        return recFileName;
    }

    public void setRecFileName(@NonNull String recFileName) {
        this.recFileName = recFileName;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public int getLeadUpdateStatus() {
        return leadUpdateStatus;
    }

    public void setLeadUpdateStatus(int leadUpdateStatus) {
        this.leadUpdateStatus = leadUpdateStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
