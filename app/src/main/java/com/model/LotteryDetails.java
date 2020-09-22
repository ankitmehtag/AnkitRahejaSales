package com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 11-Apr-17.
 */

public class LotteryDetails extends BaseRespModel implements Serializable{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable{
        private String possession_dt;
        private String original_bsp;
        private String bsp;
        private String price_SqFt;
        private String price_SqFt_unit;
        private String project_type;
        private String display_name;
        private String size;
        private String size_unit;
        private String plc;
        private String total_plc;
        private String discount;
        private String total_discounted_bsp;
        private String edc_idc;
        private String total_edc_idc;
        private String term_condition;
        private String builder_term_condition;
        private int parking_interval;
        private int min_parking;
        private int max_parking;
        private ArrayList<String> payment_plans;
        private String possession_plan;
        private String construction_plan;
        private String down_payment_plan;
        private String total_ibms;
        private String total_ifms;
        private String club_charges;
        private String parking_charges;
        private String parking_service_tax;
        private String ser_tax_club_plc;
        private String gst_vat;
        private String total_gst_vat;
        private ArrayList<ArrayList<String>> other_charges;
        private String booking_fees;
        private String total_bsp_tax;
        private String total_parking_tax;
        private String total_service_tax;
        private String total_gst_vat_tax;
        private String grand_total;
        private double grand_total_int;
        private String unit_image;
        private String builder_contactno;

        public String getBuilder_contactno() {
            return builder_contactno;
        }

        public void setBuilder_contactno(String builder_contactno) {
            this.builder_contactno = builder_contactno;
        }

        public String getUnit_image() {
            return unit_image;
        }

        public void setUnit_image(String unit_image) {
            this.unit_image = unit_image;
        }

        public String getPossession_dt() {
            return possession_dt;
        }

        public void setPossession_dt(String possession_dt) {
            this.possession_dt = possession_dt;
        }

        public String getOriginal_bsp() {
            return original_bsp;
        }

        public void setOriginal_bsp(String original_bsp) {
            this.original_bsp = original_bsp;
        }

        public String getBsp() {
            return bsp;
        }

        public void setBsp(String bsp) {
            this.bsp = bsp;
        }

        public String getPrice_SqFt() {
            return price_SqFt;
        }

        public void setPrice_SqFt(String price_SqFt) {
            this.price_SqFt = price_SqFt;
        }

        public String getPrice_SqFt_unit() {
            return price_SqFt_unit;
        }

        public void setPrice_SqFt_unit(String price_SqFt_unit) {
            this.price_SqFt_unit = price_SqFt_unit;
        }

        public String getProject_type() {
            return project_type;
        }

        public void setProject_type(String project_type) {
            this.project_type = project_type;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getSize_unit() {
            return size_unit;
        }

        public void setSize_unit(String size_unit) {
            this.size_unit = size_unit;
        }

        public String getPlc() {
            return plc;
        }

        public void setPlc(String plc) {
            this.plc = plc;
        }

        public String getTotal_plc() {
            return total_plc;
        }

        public void setTotal_plc(String total_plc) {
            this.total_plc = total_plc;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getTotal_discounted_bsp() {
            return total_discounted_bsp;
        }

        public void setTotal_discounted_bsp(String total_discounted_bsp) {
            this.total_discounted_bsp = total_discounted_bsp;
        }

        public String getEdc_idc() {
            return edc_idc;
        }

        public void setEdc_idc(String edc_idc) {
            this.edc_idc = edc_idc;
        }

        public String getTotal_edc_idc() {
            return total_edc_idc;
        }

        public void setTotal_edc_idc(String total_edc_idc) {
            this.total_edc_idc = total_edc_idc;
        }

        public String getTerm_condition() {
            return term_condition;
        }

        public void setTerm_condition(String term_condition) {
            this.term_condition = term_condition;
        }

        public String getBuilder_term_condition() {
            return builder_term_condition;
        }

        public void setBuilder_term_condition(String builder_term_condition) {
            this.builder_term_condition = builder_term_condition;
        }

        public int getParking_interval() {
            return parking_interval;
        }

        public void setParking_interval(int parking_interval) {
            this.parking_interval = parking_interval;
        }

        public int getMin_parking() {
            return min_parking;
        }

        public void setMin_parking(int min_parking) {
            this.min_parking = min_parking;
        }

        public int getMax_parking() {
            return max_parking;
        }

        public void setMax_parking(int max_parking) {
            this.max_parking = max_parking;
        }

        public ArrayList<String> getPayment_plans() {
            return payment_plans;
        }

        public void setPayment_plans(ArrayList<String> payment_plans) {
            this.payment_plans = payment_plans;
        }

        public String getPossession_plan() {
            return possession_plan;
        }

        public void setPossession_plan(String possession_plan) {
            this.possession_plan = possession_plan;
        }

        public String getConstruction_plan() {
            return construction_plan;
        }

        public void setConstruction_plan(String construction_plan) {
            this.construction_plan = construction_plan;
        }

        public String getDown_payment_plan() {
            return down_payment_plan;
        }

        public void setDown_payment_plan(String down_payment_plan) {
            this.down_payment_plan = down_payment_plan;
        }

        public String getTotal_ibms() {
            return total_ibms;
        }

        public void setTotal_ibms(String total_ibms) {
            this.total_ibms = total_ibms;
        }

        public String getTotal_ifms() {
            return total_ifms;
        }

        public void setTotal_ifms(String total_ifms) {
            this.total_ifms = total_ifms;
        }

        public String getClub_charges() {
            return club_charges;
        }

        public void setClub_charges(String club_charges) {
            this.club_charges = club_charges;
        }

        public String getParking_charges() {
            return parking_charges;
        }

        public void setParking_charges(String parking_charges) {
            this.parking_charges = parking_charges;
        }

        public String getParking_service_tax() {
            return parking_service_tax;
        }

        public void setParking_service_tax(String parking_service_tax) {
            this.parking_service_tax = parking_service_tax;
        }

        public String getSer_tax_club_plc() {
            return ser_tax_club_plc;
        }

        public void setSer_tax_club_plc(String ser_tax_club_plc) {
            this.ser_tax_club_plc = ser_tax_club_plc;
        }

        public String getGst_vat() {
            return gst_vat;
        }

        public void setGst_vat(String gst_vat) {
            this.gst_vat = gst_vat;
        }

        public String getTotal_gst_vat() {
            return total_gst_vat;
        }

        public void setTotal_gst_vat(String total_gst_vat) {
            this.total_gst_vat = total_gst_vat;
        }

        public ArrayList<ArrayList<String>> getOther_charges() {
            return other_charges;
        }

        public void setOther_charges(ArrayList<ArrayList<String>> other_charges) {
            this.other_charges = other_charges;
        }

        public String getBooking_fees() {
            return booking_fees;
        }

        public void setBooking_fees(String booking_fees) {
            this.booking_fees = booking_fees;
        }

        public String getTotal_bsp_tax() {
            return total_bsp_tax;
        }

        public void setTotal_bsp_tax(String total_bsp_tax) {
            this.total_bsp_tax = total_bsp_tax;
        }

        public String getTotal_parking_tax() {
            return total_parking_tax;
        }

        public void setTotal_parking_tax(String total_parking_tax) {
            this.total_parking_tax = total_parking_tax;
        }

        public String getTotal_service_tax() {
            return total_service_tax;
        }

        public void setTotal_service_tax(String total_service_tax) {
            this.total_service_tax = total_service_tax;
        }

        public String getTotal_gst_vat_tax() {
            return total_gst_vat_tax;
        }

        public void setTotal_gst_vat_tax(String total_gst_vat_tax) {
            this.total_gst_vat_tax = total_gst_vat_tax;
        }

        public String getGrand_total() {
            return grand_total;
        }

        public void setGrand_total(String grand_total) {
            this.grand_total = grand_total;
        }

        public double getGrand_total_int() {
            return grand_total_int;
        }

        public void setGrand_total_int(double grand_total_int) {
            this.grand_total_int = grand_total_int;
        }
    }

    /*public class OtherCharges implements Serializable{

    }*/
}
