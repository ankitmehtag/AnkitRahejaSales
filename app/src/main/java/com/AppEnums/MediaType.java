package com.AppEnums;

/**
 * Created by Naresh on 21-Jan-17.
 */

public enum MediaType {
    PHOTO("photo"),
    VIDEO("Video") ,
    PDF("Pdf"),
    ERROR("Error");

    public String value;
    MediaType(String value) {
        this.value = value;
    }

    public static MediaType getEnum(String value) {
        if (value == null) return ERROR;
        for (MediaType e : MediaType.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return ERROR;
    }
}
