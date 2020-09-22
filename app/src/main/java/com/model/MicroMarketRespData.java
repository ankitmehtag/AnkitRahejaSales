package com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 05-Oct-17.
 */

public class MicroMarketRespData implements Serializable{
    private boolean isSuccess;
    private ArrayList<MicroMarketData> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ArrayList<MicroMarketData> getData() {
        return data;
    }

    public void setData(ArrayList<MicroMarketData> data) {
        this.data = data;
    }
}
