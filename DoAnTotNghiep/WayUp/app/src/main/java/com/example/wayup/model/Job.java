package com.example.wayup.model;

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


}