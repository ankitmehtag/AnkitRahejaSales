package com.model;

import java.io.Serializable;

/**
 * Created by Naresh on 20-Jan-17.
 */

public class BaseRespModel implements Serializable {
    private boolean success;
    private String message;
    private String builder_id;

    public String getBuilder_id() {
        return builder_id;
    }

    public void setBuilder_id(String builder_id) {
        this.builder_id = builder_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
