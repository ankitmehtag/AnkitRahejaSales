package com.model;

/**
 * Created by Naresh on 03-Feb-18.
 */

public class GetOfferCodeRespModel {
    private boolean success;
    private String message;
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
    private String offer_code;
    private String description;

        public String getOffer_code() {
            return offer_code;
        }

        public void setOffer_code(String offer_code) {
            this.offer_code = offer_code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
