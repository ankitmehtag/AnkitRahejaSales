package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_universal_contacts")
public class UniversalContactsEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "mobile_no")
    private String mobileNo;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "time_stamp")
    private String timeStamp;
    @ColumnInfo(name = "is_synced")
    private int isSynced;
    @ColumnInfo(name = "recording_file_name")
    private String recordingFileName;

    public UniversalContactsEntity(@NonNull String mobileNo, String name, String timeStamp, int isSynced, String recordingFileName) {
        setMobileNo(mobileNo);
        setName(name);
        setTimeStamp(timeStamp);
        setIsSynced(isSynced);
        setRecordingFileName(recordingFileName);
    }

    @NonNull
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(@NonNull String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getRecordingFileName() {
        return recordingFileName;
    }

    public void setRecordingFileName(String recordingFileName) {
        this.recordingFileName = recordingFileName;
    }
}