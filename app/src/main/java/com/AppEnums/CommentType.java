package com.AppEnums;

/**
 * Created by Naresh on 23-Dec-16.
 */

public enum CommentType {
    PROJECT_COMMENT("project"),
    UNIT_COMMENT("unit") ;

    public String value;
    CommentType(String value) {
        this.value = value;
    }
}
