package com.policyreminder.policyreminder.Models;

public class CompanyModel {

    String company_name , user_id ,company_payment_link ,company_notes ,company_id;

    public CompanyModel(){

    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompany_payment_link() {
        return company_payment_link;
    }

    public void setCompany_payment_link(String company_payment_link) {
        this.company_payment_link = company_payment_link;
    }

    public String getCompany_notes() {
        return company_notes;
    }

    public void setCompany_notes(String company_notes) {
        this.company_notes = company_notes;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
