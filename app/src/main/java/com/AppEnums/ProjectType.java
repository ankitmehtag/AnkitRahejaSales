package com.AppEnums;

/**
 * Created by Naresh on 23-Dec-16.
 */

public enum ProjectType {
    HOT("Hot"),
    LIVE("Live") ,
    READY_TO_MOVE("Ready To Move In"),
    LUXURY("Luxury"),
    AFFORDABLE("Affordable Homes");


    public String value;
    ProjectType(String value) {
        this.value = value;
    }
}
