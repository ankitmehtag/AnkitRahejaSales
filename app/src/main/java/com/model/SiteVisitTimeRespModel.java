package com.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Naresh on 31-Mar-17.
 */

public class SiteVisitTimeRespModel implements Serializable {
    private boolean success;
    @SerializedName("Office start time")
    private long Office_start_time;
    @SerializedName("Office End time")
    private long Office_End_time;
    @SerializedName("Current time")
    private long Current_time;
    @SerializedName("Difference")
    private long Difference;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getOffice_start_time() {
        return Office_start_time;
    }

    public void setOffice_start_time(long office_start_time) {
        Office_start_time = office_start_time;
    }

    public long getOffice_End_time() {
        return Office_End_time;
    }

    public void setOffice_End_time(long office_End_time) {
        Office_End_time = office_End_time;
    }

    public long getCurrent_time() {
        return Current_time;
    }

    public void setCurrent_time(long current_time) {
        Current_time = current_time;
    }

    public long getDifference() {
        return Difference;
    }

    public void setDifference(long difference) {
        Difference = difference;
    }
}
