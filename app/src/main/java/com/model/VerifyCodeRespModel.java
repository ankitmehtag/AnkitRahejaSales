package com.model;

/**
 * Created by Naresh on 10-Jan-18.
 */

public class VerifyCodeRespModel {
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
        //For broker code
        private String broker_name;

        // for coupon code
        private String offered_text;
        private String payment_plan_name;

        // for co sales person code
        private String co_sales_Person_name;

        public String getBroker_name() {
            return broker_name;
        }

        public void setBroker_name(String broker_name) {
            this.broker_name = broker_name;
        }

        public String getOffered_text() {
            return offered_text;
        }

        public void setOffered_text(String offered_text) {
            this.offered_text = offered_text;
        }

        public String getPayment_plan_name() {
            return payment_plan_name;
        }

        public void setPayment_plan_name(String payment_plan_name) {
            this.payment_plan_name = payment_plan_name;
        }

        public String getCo_sales_Person_name() {
            return co_sales_Person_name;
        }

        public void setCo_sales_Person_name(String co_sales_Person_name) {
            this.co_sales_Person_name = co_sales_Person_name;
        }
    }
}
