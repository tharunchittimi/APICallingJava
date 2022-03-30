package com.example.apicalling;

public class JobsDataItems {

    public String role;
    public String company;
    public String city;
    public String days;
    public String poster;
    public  String description;
    public  String apply;
    public String company_url;

    public JobsDataItems(String role, String company, String city, String days, String poster, String description, String apply, String company_url) {
        this.role = role;
        this.company = company;
        this.city = city;
        this.days = days;
        this.poster = poster;
        this.description = description;
        this.apply=apply;
        this.company_url=company_url;
    }

}
