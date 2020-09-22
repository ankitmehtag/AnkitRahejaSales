package com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 05-Oct-17.
 */

public class SectorRespData implements Serializable{
    private boolean isSuccess;
    private ArrayList<SectorData> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ArrayList<SectorData> getData() {
        return data;
    }

    public void setData(ArrayList<SectorData> data) {
        this.data = data;
    }
}
