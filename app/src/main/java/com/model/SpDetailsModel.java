package com.model;

import java.util.List;

public class SpDetailsModel {

    private String campaign_name;
    private String campaign_date;
    private String customer_name;
    private String customer_email;
    private String customer_mobile;
    private String customer_alternate_mobile;
    private String Current_Status;
    private String budget;
    private List<Projects> projects = null;
    private List<Projects> selectedProjList = null;
    private List<LeadStatus> leadStatus = null;
    private String remark;
    private String lead_type;
    private String budget_min;
    private String budget_max;
    private int no_of_persons;
    String date_and_time, date, time, address, status;

    public SpDetailsModel(String campaign_name, String campaign_date, String customer_name, String customer_email,
                          String customer_mobile, String customer_alternate_mobile, String current_Status, String budget,
                          String date_and_time, String date, String time, String address, String status,
                          List<Projects> projectList, List<Projects> selectedProjList, List<LeadStatus> leadStatusList,
                          String remark, String lead_type, String budget_min, String budget_max, int no_of_persons) {
        setCampaign_name(campaign_name);
        setCampaign_date(campaign_date);
        setCustomer_name(customer_name);
        setCustomer_email(customer_email);
        setCustomer_mobile(customer_mobile);
        setCustomer_alternate_mobile(customer_alternate_mobile);
        setCurrent_Status(current_Status);
        setBudget(budget);
        setDate_And_Time(date_and_time);
        setDate(date);
        setTime(time);
        setAddress(address);
        setStatus(status);
        setProjectList(projectList);
        setSelectedProjList(selectedProjList);
        setLeadStatusList(leadStatusList);
        setRemark(remark);
        setLead_type(lead_type);
        setBudget_min(budget_min);
        setBudget_max(budget_max);
        setNoOfPersons(no_of_persons);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_And_Time() {
        return date_and_time;
    }

    public void setDate_And_Time(String date_and_time) {
        this.date_and_time = date_and_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrent_Status() {
        return Current_Status;
    }

    public void setCurrent_Status(String current_Status) {
        Current_Status = current_Status;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
    }

    public String getBudget_min() {
        return budget_min;
    }

    public void setBudget_min(String budget_min) {
        this.budget_min = budget_min;
    }

    public String getBudget_max() {
        return budget_max;
    }

    public void setBudget_max(String budget_max) {
        this.budget_max = budget_max;
    }

    public int getNoOfPersons() {
        return no_of_persons;
    }

    public void setNoOfPersons(int no_of_persons) {
        this.no_of_persons = no_of_persons;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

    public String getCampaign_date() {
        return campaign_date;
    }

    public void setCampaign_date(String campaign_date) {
        this.campaign_date = campaign_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_alternate_mobile() {
        return customer_alternate_mobile;
    }

    public void setCustomer_alternate_mobile(String customer_alternate_mobile) {
        this.customer_alternate_mobile = customer_alternate_mobile;
    }

    public List<Projects> getProjectList() {
        return projects;
    }

    public void setProjectList(List<Projects> projects) {
        this.projects = projects;
    }

    public List<Projects> getSelectedProjList() {
        return selectedProjList;
    }

    public void setSelectedProjList(List<Projects> selectedProjList) {
        this.selectedProjList = selectedProjList;
    }

    public List<LeadStatus> getLeadStatusList() {
        return leadStatus;
    }

    public void setLeadStatusList(List<LeadStatus> leadStatus) {
        this.leadStatus = leadStatus;
    }

}
