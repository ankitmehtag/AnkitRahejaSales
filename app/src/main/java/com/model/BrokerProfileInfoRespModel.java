package com.model;

/**
 * Created by Naresh on 19-Jan-18.
 */

public class BrokerProfileInfoRespModel {

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
        private String first_name;
        private String last_name;
        private String address;
        private String mobile_no;
        private String email_id;
        private String broker_type;
        private String rera_no;
        private String pan_no;
        private String gst_no;
        private String document_status;

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getBroker_type() {
            return broker_type;
        }

        public void setBroker_type(String broker_type) {
            this.broker_type = broker_type;
        }

        public String getRera_no() {
            return rera_no;
        }

        public void setRera_no(String rera_no) {
            this.rera_no = rera_no;
        }

        public String getPan_no() {
            return pan_no;
        }

        public void setPan_no(String pan_no) {
            this.pan_no = pan_no;
        }

        public String getGst_no() {
            return gst_no;
        }

        public void setGst_no(String gst_no) {
            this.gst_no = gst_no;
        }

        public String getDocument_status() {
            return document_status;
        }

        public void setDocument_status(String document_status) {
            this.document_status = document_status;
        }
    }

}
