package com.model;

/**
 * Created by Naresh on 05-Feb-18.
 */

public class OfferStatusRespModel {
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

    public static class Data{
        private String offer_code;
        private String coupon_code;
        private String plan_title;
        private String description;
        private int offer_status;// 0 for reject,1- for accept

        public Data(String offer_code, String coupon_code, String plan_title, String description, int offer_status) {
            this.offer_code = offer_code;
            this.coupon_code = coupon_code;
            this.plan_title = plan_title;
            this.description = description;
            this.offer_status = offer_status;
        }

        public int getOffer_status() {
            return offer_status;
        }

        public void setOffer_status(int offer_status) {
            this.offer_status = offer_status;
        }

        public String getOffer_code() {
            return offer_code;
        }

        public void setOffer_code(String offer_code) {
            this.offer_code = offer_code;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }

        public String getPlan_title() {
            return plan_title;
        }

        public void setPlan_title(String plan_title) {
            this.plan_title = plan_title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
