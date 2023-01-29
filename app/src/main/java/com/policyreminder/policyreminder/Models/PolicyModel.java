package com.policyreminder.policyreminder.Models;

public class PolicyModel {
    String user_id , insurance_type ,company ,payment_cycle,sum_insured,premium,start_date,duration,plan_ncb_number
            , end_date  , policy_status , payment_status ,next_payment_date,policy_name, policy_id;

    public PolicyModel() {
    }

    public String getPolicy_name() {
        return policy_name;
    }

    public String getPolicy_id() {
        return policy_id;
    }

    public void setPolicy_id(String policy_id) {
        this.policy_id = policy_id;
    }

    public void setPolicy_name(String policy_name) {
        this.policy_name = policy_name;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPolicy_status() {
        return policy_status;
    }

    public void setPolicy_status(String policy_status) {
        this.policy_status = policy_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getNext_payment_date() {
        return next_payment_date;
    }

    public void setNext_payment_date(String next_payment_date) {
        this.next_payment_date = next_payment_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getInsurance_type() {
        return insurance_type;
    }

    public void setInsurance_type(String insurance_type) {
        this.insurance_type = insurance_type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPayment_cycle() {
        return payment_cycle;
    }

    public void setPayment_cycle(String payment_cycle) {
        this.payment_cycle = payment_cycle;
    }

    public String getSum_insured() {
        return sum_insured;
    }

    public void setSum_insured(String sum_insured) {
        this.sum_insured = sum_insured;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlan_ncb_number() {
        return plan_ncb_number;
    }

    public void setPlan_ncb_number(String plan_ncb_number) {
        this.plan_ncb_number = plan_ncb_number;
    }
}
