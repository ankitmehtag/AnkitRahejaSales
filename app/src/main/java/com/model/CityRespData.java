package com.model;

import java.util.ArrayList;

/**
 * Created by Naresh on 19-Jun-17.
 */

public class CityRespData {
    private boolean isSuccess;
    private ArrayList<CityDataModel> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ArrayList<CityDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<CityDataModel> data) {
        this.data = data;
    }
}
