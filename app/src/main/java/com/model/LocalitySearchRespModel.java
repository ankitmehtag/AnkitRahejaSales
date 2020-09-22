package com.model;

import com.utils.LocalityData;

import java.util.ArrayList;
/**
 * Created by Naresh on 07-Jan-17.
 */

public class LocalitySearchRespModel {

    private int success;
    private ArrayList<LocalityData> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<LocalityData> getData() {
        return data;
    }

    public void setData(ArrayList<LocalityData> data) {
        this.data = data;
    }
}
