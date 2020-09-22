package com.AppEnums;

/**
 * Created by Naresh on 09-Jan-17.
 */

public enum LocalityType {
    LOCATION("Location"),
    BUILDER("Builder"),
    PROJECT("Project"),
    SUBLOCATION("Sublocation"),
    RECENT("Recent"),
    ERROR("Error");

    public String value;

    LocalityType(String value) {
        this.value = value;
    }

    public static LocalityType getEnum(String value) {
        if (value == null) return ERROR;
        for (LocalityType e : LocalityType.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return ERROR;
    }

}
