package com.AppEnums;

/**
 * Created by Naresh on 05-Jan-17.
 */


public enum PropertyType {
    FLAT("FLAT"),
    BUILDER_FLOOR("Builder_Floor"),
    PLOT("PLOT"),
    HOUSE_VILLA("House_Villa"),
    SHOPS_SHOWROOM("Shop/Showroom"),
    OFFICE_SPACE("OFFICE_SPACE"),
    SERVICE_APARTMENT("Service_Apartment"),
    ERROR("Error");

    public String value;
    PropertyType(String value) {
        this.value = value;
    }

    public static PropertyType getEnum(String value) {
        if (value == null) return ERROR;
        for (PropertyType e : PropertyType.values()) {
            if (e.value.equalsIgnoreCase(value)) {
               // Log.d("NotificationAction","EnumValue"+e.value + ",ParamValue"+value);
                return e;
            }
        }
        return ERROR;
    }

}
