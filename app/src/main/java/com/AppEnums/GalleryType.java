package com.AppEnums;

/**
 * Created by Naresh on 21-Jan-17.
 */

public enum GalleryType {
    IMAGE("Image"),
    VIDEO("video") ,
    CONSTRUCTION_UPDATE("Construction_Update"),
    VIRTUAL_SITE_VISIT("Virtual_Site_Visit"),
    THREE_SIXTY_DEGREE_TOUR("360_Degree_Tour"),
    ERROR("Error");

    public String value;
    GalleryType(String value) {
        this.value = value;
    }

    public static GalleryType getEnum(String value) {
        if (value == null) return ERROR;
        for (GalleryType e : GalleryType.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return ERROR;
    }
}
