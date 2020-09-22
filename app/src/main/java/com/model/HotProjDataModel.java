package com.model;

import com.AppEnums.ProjectType;

import java.io.Serializable;

/**
 * Created by Naresh on 23-Dec-16.
 */

public class HotProjDataModel implements Serializable {
    private ProjectType projType;
    private String projName;
    private int imgRes;

    public HotProjDataModel(ProjectType projType,String projName,int imgRes){
        this.projType = projType;
        this.projName = projName;
        this.imgRes = imgRes;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public ProjectType getProjType() {
        return projType;
    }

    public void setProjType(ProjectType projType) {
        this.projType = projType;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }
}
