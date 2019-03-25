package com.example.wayupmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company extends AbstractModel {

    private int id_company;

    private String name;

    private String type;

    private String member;

    private String country;

    private String thumbnail;

    private String image;

    private String time_for_work;

    private String address;

    private String contact;

    private String description;

    private String over_time;

    private String title;


    public Company(String name, String type, String member, String country, String thumbnail, String image, String time_for_work, String address, String contact, String description, String over_time, String title) {
        this.name = name;
        this.type = type;
        this.member = member;
        this.country = country;
        this.thumbnail = thumbnail;
        this.image = image;
        this.time_for_work = time_for_work;
        this.address = address;
        this.contact = contact;
        this.description = description;
        this.over_time = over_time;
        this.title = title;
    }
}
