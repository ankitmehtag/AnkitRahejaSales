package com.model;

/**
 * Created by Naresh on 28-Dec-17.
 */

public class RegisterUserRespModel {

    private boolean success;
    private String message;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String broker_id;
        private String broker_code;

        public String getBroker_id() {
            return broker_id;
        }

        public void setBroker_id(String broker_id) {
            this.broker_id = broker_id;
        }

        public String getBroker_code() {
            return broker_code;
        }

        public void setBroker_code(String broker_code) {
            this.broker_code = broker_code;
        }
    }
}
