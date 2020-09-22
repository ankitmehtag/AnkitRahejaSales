package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Naresh on 30-Dec-17.
 */

public class MyTransactionsRespModel {
    private boolean success;
    private String message;
    private ArrayList<Data> data;

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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public static class Data implements Parcelable {
        private String project_name;
        private String display_name;
        private String unit_no;
        private String transaction_datetime;
        private String size;
        private String order_no;
        private String paid_amount;
        private String transaction_amount;
        private String Customer_Name;
        private String Customer_Mobile_no;
        private String transaction_no;
        private String coapplicant;
        private String coupen_code;
        private String coupon_code_description;
        private String payment_mode;
        private String status;
        private String payment_plan;
        private String payment_plan_desc;
        private String brokerage_terms;
        private String broker_code;
        private String broker_name;

        public String getPayment_plan_desc() {
            return payment_plan_desc;
        }

        public void setPayment_plan_desc(String payment_plan_desc) {
            this.payment_plan_desc = payment_plan_desc;
        }

        public String getCoupon_code_description() {
            return coupon_code_description;
        }

        public void setCoupon_code_description(String coupon_code_description) {
            this.coupon_code_description = coupon_code_description;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public void setUnit_no(String unit_no) {
            this.unit_no = unit_no;
        }

        public void setTransaction_datetime(String transaction_datetime) {
            this.transaction_datetime = transaction_datetime;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public void setPaid_amount(String paid_amount) {
            this.paid_amount = paid_amount;
        }

        public void setTransaction_amount(String transaction_amount) {
            this.transaction_amount = transaction_amount;
        }

        public void setCustomer_Name(String customer_Name) {
            Customer_Name = customer_Name;
        }

        public void setCustomer_Mobile_no(String customer_Mobile_no) {
            Customer_Mobile_no = customer_Mobile_no;
        }

        public void setTransaction_no(String transaction_no) {
            this.transaction_no = transaction_no;
        }

        public void setCoapplicant(String coapplicant) {
            this.coapplicant = coapplicant;
        }

        public void setCoupen_code(String coupen_code) {
            this.coupen_code = coupen_code;
        }

        public void setPayment_mode(String payment_mode) {
            this.payment_mode = payment_mode;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setPayment_plan(String payment_plan) {
            this.payment_plan = payment_plan;
        }

        public void setBrokerage_terms(String brokerage_terms) {
            this.brokerage_terms = brokerage_terms;
        }

        public void setBroker_code(String broker_code) {
            this.broker_code = broker_code;
        }

        public void setBroker_name(String broker_name) {
            this.broker_name = broker_name;
        }

        public String getProject_name() {
            return project_name;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public String getUnit_no() {
            return unit_no;
        }

        public String getTransaction_datetime() {
            return transaction_datetime;
        }

        public String getSize() {
            return size;
        }

        public String getOrder_no() {
            return order_no;
        }

        public String getPaid_amount() {
            return paid_amount;
        }

        public String getTransaction_amount() {
            return transaction_amount;
        }

        public String getCustomer_Name() {
            return Customer_Name;
        }

        public String getCustomer_Mobile_no() {
            return Customer_Mobile_no;
        }

        public String getTransaction_no() {
            return transaction_no;
        }

        public String getCoapplicant() {
            return coapplicant;
        }

        public String getCoupen_code() {
            return coupen_code;
        }

        public String getPayment_mode() {
            return payment_mode;
        }

        public String getStatus() {
            return status;
        }

        public String getPayment_plan() {
            return payment_plan;
        }

        public String getBrokerage_terms() {
            return brokerage_terms;
        }

        public String getBroker_code() {
            return broker_code;
        }

        public String getBroker_name() {
            return broker_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.project_name);
            dest.writeString(this.display_name);
            dest.writeString(this.unit_no);
            dest.writeString(this.transaction_datetime);
            dest.writeString(this.size);
            dest.writeString(this.order_no);
            dest.writeString(this.paid_amount);
            dest.writeString(this.transaction_amount);
            dest.writeString(this.Customer_Name);
            dest.writeString(this.Customer_Mobile_no);
            dest.writeString(this.transaction_no);
            dest.writeString(this.coapplicant);
            dest.writeString(this.coupen_code);
            dest.writeString(this.coupon_code_description);
            dest.writeString(this.payment_mode);
            dest.writeString(this.status);
            dest.writeString(this.payment_plan);
            dest.writeString(this.payment_plan_desc);
            dest.writeString(this.brokerage_terms);
            dest.writeString(this.broker_code);
            dest.writeString(this.broker_name);
        }

        public Data() {
        }

        protected Data(Parcel in) {
            this.project_name = in.readString();
            this.display_name = in.readString();
            this.unit_no = in.readString();
            this.transaction_datetime = in.readString();
            this.size = in.readString();
            this.order_no = in.readString();
            this.paid_amount = in.readString();
            this.transaction_amount = in.readString();
            this.Customer_Name = in.readString();
            this.Customer_Mobile_no = in.readString();
            this.transaction_no = in.readString();
            this.coapplicant = in.readString();
            this.coupen_code = in.readString();
            this.coupon_code_description = in.readString();
            this.payment_mode = in.readString();
            this.status = in.readString();
            this.payment_plan = in.readString();
            this.payment_plan_desc = in.readString();
            this.brokerage_terms = in.readString();
            this.broker_code = in.readString();
            this.broker_name = in.readString();
        }

        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }
}
