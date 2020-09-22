package com.model;

import java.util.ArrayList;

/**
 * Created by Naresh on 07-Oct-17.
 */

public class AddLeadRespModel {
    private boolean success;
    private String message;
    private ArrayList<AddLeadData>data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AddLeadData> getData() {
        return data;
    }

    public void setData(ArrayList<AddLeadData> data) {
        this.data = data;
    }
}
