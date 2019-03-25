package com.example.wayupmanagement.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job extends AbstractModel implements Serializable {
    private int id_job;

    private String title;

    private String thumbnail;

    private String information;

    private String address;

    private String join_date;

    private int estimatetime;

    private int lock;

    private int salary;

    private String skills;

    private String fast_info;

    private Company company;

    public Job(String title, String thumbnail, String information, String address, String join_date, int estimatetime, int lock, int salary, String skills, String fast_info, Company company) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.information = information;
        this.address = address;
        this.join_date = join_date;
        this.estimatetime = estimatetime;
        this.lock = lock;
        this.salary = salary;
        this.skills = skills;
        this.fast_info = fast_info;
        this.company = company;
    }
}