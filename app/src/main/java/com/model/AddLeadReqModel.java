package com.model;

import java.util.ArrayList;

/**
 * Created by Naresh on 07-Oct-17.
 */

public class AddLeadReqModel {
    private ArrayList<AddLeadData> datalist;
    private String userid;

    public ArrayList<AddLeadData> getDatalist() {
        return datalist;
    }

    public void setDatalist(ArrayList<AddLeadData> datalist) {
        this.datalist = datalist;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
