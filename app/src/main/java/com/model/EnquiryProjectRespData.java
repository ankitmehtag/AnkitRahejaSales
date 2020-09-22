package com.model;

import java.util.ArrayList;

/**
 * Created by Naresh on 19-Jun-17.
 */

public class EnquiryProjectRespData {
    private boolean success;
    private ArrayList<ProjectsData> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<ProjectsData> getData() {
        return data;
    }

    public void setData(ArrayList<ProjectsData> data) {
        this.data = data;
    }
}
